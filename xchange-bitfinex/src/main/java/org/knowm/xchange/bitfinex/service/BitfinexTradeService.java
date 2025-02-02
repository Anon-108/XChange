package org.knowm.xchange.bitfinex.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.knowm.xchange.bitfinex.BitfinexErrorAdapter;
import org.knowm.xchange.bitfinex.BitfinexExchange;
import org.knowm.xchange.bitfinex.dto.BitfinexException;
import org.knowm.xchange.bitfinex.v1.BitfinexOrderType;
import org.knowm.xchange.bitfinex.v1.dto.trade.BitfinexOrderFlags;
import org.knowm.xchange.bitfinex.v1.dto.trade.BitfinexOrderStatusResponse;
import org.knowm.xchange.bitfinex.v1.dto.trade.BitfinexReplaceOrderRequest;
import org.knowm.xchange.bitfinex.v2.dto.trade.Trade;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.MarketOrder;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.knowm.xchange.dto.trade.StopOrder;
import org.knowm.xchange.dto.trade.UserTrades;
import org.knowm.xchange.exceptions.NotYetImplementedForExchangeException;
import org.knowm.xchange.service.trade.TradeService;
import org.knowm.xchange.service.trade.params.CancelAllOrders;
import org.knowm.xchange.service.trade.params.CancelOrderByIdParams;
import org.knowm.xchange.service.trade.params.CancelOrderParams;
import org.knowm.xchange.service.trade.params.DefaultTradeHistoryParamsTimeSpan;
import org.knowm.xchange.service.trade.params.TradeHistoryParamCurrencyPair;
import org.knowm.xchange.service.trade.params.TradeHistoryParamLimit;
import org.knowm.xchange.service.trade.params.TradeHistoryParamPaging;
import org.knowm.xchange.service.trade.params.TradeHistoryParams;
import org.knowm.xchange.service.trade.params.TradeHistoryParamsSorted;
import org.knowm.xchange.service.trade.params.TradeHistoryParamsTimeSpan;
import org.knowm.xchange.service.trade.params.orders.OpenOrdersParams;
import org.knowm.xchange.service.trade.params.orders.OrderQueryParams;
import org.knowm.xchange.utils.DateUtils;

/**
 * bitfinex交易服务
 */
public class BitfinexTradeService extends BitfinexTradeServiceRaw implements TradeService {

  private static final OpenOrders noOpenOrders = new OpenOrders(new ArrayList<LimitOrder>());

  /**
   * bitfinex交易服务
   * @param exchange
   * @param resilienceRegistries
   */
  public BitfinexTradeService(
      BitfinexExchange exchange, ResilienceRegistries resilienceRegistries) {

    super(exchange, resilienceRegistries);
  }

  /**
   * 得到开放订单
   * @return
   * @throws IOException
   */
  @Override
  public OpenOrders getOpenOrders() throws IOException {
    return getOpenOrders(createOpenOrdersParams());
  }

  /**
   * 得到开放订单
   * @param params The parameters describing the filter. Note that {@link OpenOrdersParams} is an empty interface. Exchanges should implement its own params object. Params should be create with {@link #createOpenOrdersParams()}.
   *               * @param params 描述过滤器的参数。 请注意，{@link OpenOrdersParams} 是一个空接口。 交易所应该实现自己的 params 对象。 应使用 {@link #createOpenOrdersParams()} 创建参数。
   *
   * @return
   * @throws IOException
   */
  @Override
  public OpenOrders getOpenOrders(OpenOrdersParams params) throws IOException {
    try {
      BitfinexOrderStatusResponse[] activeOrders = getBitfinexOpenOrders();

      if (activeOrders.length <= 0) {
        return noOpenOrders;
      } else {
        return filterOrders(BitfinexAdapters.adaptOrders(activeOrders), params);
      }
    } catch (BitfinexException e) {
      throw BitfinexErrorAdapter.adapt(e);
    }
  }

  /**
   * 过滤订单
   * Bitfinex API does not provide filtering option. So we should filter orders ourselves
   * Bitfinex API 不提供过滤选项。 所以我们应该自己过滤订单 */
  @SuppressWarnings("unchecked")
  private OpenOrders filterOrders(OpenOrders rawOpenOrders, OpenOrdersParams params) {
    if (params == null) {
      return rawOpenOrders;
    }

    List<LimitOrder> openOrdersList = rawOpenOrders.getOpenOrders();
    openOrdersList.removeIf(openOrder -> !params.accept(openOrder));

    return new OpenOrders(openOrdersList, (List<Order>) rawOpenOrders.getHiddenOrders());
  }

  /**
   * 下市价单
   * @param marketOrder 市价单
   * @return
   * @throws IOException
   */
  @Override
  public String placeMarketOrder(MarketOrder marketOrder) throws IOException {
    try {
      BitfinexOrderStatusResponse newOrder;
      if (marketOrder.hasFlag(BitfinexOrderFlags.MARGIN))
        newOrder = placeBitfinexMarketOrder(marketOrder, BitfinexOrderType.MARGIN_MARKET);
      else newOrder = placeBitfinexMarketOrder(marketOrder, BitfinexOrderType.MARKET);

      return String.valueOf(newOrder.getId());
    } catch (BitfinexException e) {
      throw BitfinexErrorAdapter.adapt(e);
    }
  }

  /**
   * 下限价单
   * @param limitOrder 限价单
   * @return
   * @throws IOException
   */
  @Override
  public String placeLimitOrder(LimitOrder limitOrder) throws IOException {
    try {
      BitfinexOrderType type = BitfinexAdapters.adaptOrderFlagsToType(limitOrder.getOrderFlags());
      BitfinexOrderStatusResponse newOrder = placeBitfinexLimitOrder(limitOrder, type);
      return String.valueOf(newOrder.getId());
    } catch (BitfinexException e) {
      throw BitfinexErrorAdapter.adapt(e);
    }
  }

  /**
   * 修改或取消/替换现有限价单
   * @param order 订单
   * @return
   * @throws IOException
   */
  @Override
  public String changeOrder(LimitOrder order) throws IOException {

    boolean useRemaining =
        order.getOriginalAmount() == null || order.hasFlag(BitfinexOrderFlags.USE_REMAINING);

    BitfinexReplaceOrderRequest request =
        new BitfinexReplaceOrderRequest(
            String.valueOf(exchange.getNonceFactory().createValue()),
            Long.valueOf(order.getId()),
            BitfinexAdapters.adaptCurrencyPair(order.getCurrencyPair()),
            order.getOriginalAmount(),
            order.getLimitPrice(),
            "bitfinex",
            BitfinexAdapters.adaptOrderType(order.getType()),
            BitfinexAdapters.adaptOrderFlagsToType(order.getOrderFlags()).getValue(),
            order.hasFlag(BitfinexOrderFlags.HIDDEN),
            order.hasFlag(BitfinexOrderFlags.POST_ONLY),
            useRemaining);
    try {
      BitfinexOrderStatusResponse response =
          bitfinex.replaceOrder(apiKey, payloadCreator, signatureCreator, request);
      return String.valueOf(response.getId());
    } catch (BitfinexException e) {
      throw BitfinexErrorAdapter.adapt(e);
    }
  }

  /**
   * 下止损单
   * @param stopOrder 停止订单
   * @return
   * @throws IOException
   */
  @Override
  public String placeStopOrder(StopOrder stopOrder) throws IOException {
    if (stopOrder.getLimitPrice() != null) {
      throw new NotYetImplementedForExchangeException(
          "Limit stops are not supported by the Bitfinex v1 API. Bitfinex v1 API 不支持限位点。");
    }
    LimitOrder limitOrder =
        new LimitOrder(
            stopOrder.getType(),
            stopOrder.getOriginalAmount(),
            stopOrder.getCurrencyPair(),
            stopOrder.getId(),
            stopOrder.getTimestamp(),
            stopOrder.getStopPrice());
    limitOrder.setOrderFlags(stopOrder.getOrderFlags());
    limitOrder.setLeverage(stopOrder.getLeverage());
    limitOrder.addOrderFlag(BitfinexOrderFlags.STOP);
    return placeLimitOrder(limitOrder);
  }

  /**
   * 取消订单
   * @param orderId 订单id
   * @return
   * @throws IOException
   */
  @Override
  public boolean cancelOrder(String orderId) throws IOException {
    try {
      return cancelBitfinexOrder(orderId);
    } catch (BitfinexException e) {
      throw BitfinexErrorAdapter.adapt(e);
    }
  }

  @Override
  public boolean cancelOrder(CancelOrderParams orderParams) throws IOException {
    try {
      if (orderParams instanceof CancelOrderByIdParams) {
        return cancelOrder(((CancelOrderByIdParams) orderParams).getOrderId());
      }
      if (orderParams instanceof CancelAllOrders) {
        return cancelAllBitfinexOrders();
      }

      throw new IllegalArgumentException(
          String.format("Unknown parameter type 未知参数类型: %s", orderParams.getClass()));
    } catch (BitfinexException e) {
      throw BitfinexErrorAdapter.adapt(e);
    }
  }

  /**
   * @param params Implementation of {@link TradeHistoryParamCurrencyPair} is mandatory.
   *               Can optionally implement {@link TradeHistoryParamPaging} and {@link   TradeHistoryParamsTimeSpan#getStartTime()}.
   *               All other TradeHistoryParams types will be  ignored.
   *               {@link TradeHistoryParamCurrencyPair} 的实施是强制性的。
   *     * 可以选择实现 {@link TradeHistoryParamPaging} 和 {@link TradeHistoryParamsTimeSpan#getStartTime()}。
   *     * 所有其他 TradeHistoryParams 类型将被忽略。
   */
  @Override
  public UserTrades getTradeHistory(TradeHistoryParams params) throws IOException {
    try {
      String symbol = null;
      if (params instanceof TradeHistoryParamCurrencyPair
          && ((TradeHistoryParamCurrencyPair) params).getCurrencyPair() != null) {
        symbol =
            BitfinexAdapters.adaptCurrencyPair(
                ((TradeHistoryParamCurrencyPair) params).getCurrencyPair());
      }

      Long startTime = 0L;
      Long endTime = null;
      Long limit = 50L;
      Long sort = null;

      if (params instanceof TradeHistoryParamsTimeSpan) {
        TradeHistoryParamsTimeSpan paramsTimeSpan = (TradeHistoryParamsTimeSpan) params;
        startTime = DateUtils.toMillisNullSafe(paramsTimeSpan.getStartTime());
        endTime = DateUtils.toMillisNullSafe(paramsTimeSpan.getEndTime());
      }

      if (params instanceof TradeHistoryParamLimit) {
        TradeHistoryParamLimit tradeHistoryParamLimit = (TradeHistoryParamLimit) params;
        if (tradeHistoryParamLimit.getLimit() != null) {
          limit = Long.valueOf(tradeHistoryParamLimit.getLimit());
        }
      }

      if (params instanceof TradeHistoryParamsSorted) {
        TradeHistoryParamsSorted tradeHistoryParamsSorted = (TradeHistoryParamsSorted) params;
        sort = tradeHistoryParamsSorted.getOrder() == TradeHistoryParamsSorted.Order.asc ? 1L : -1L;
      }

      final List<Trade> trades = getBitfinexTradesV2(symbol, startTime, endTime, limit, sort);
      return BitfinexAdapters.adaptTradeHistoryV2(trades);
    } catch (BitfinexException e) {
      throw BitfinexErrorAdapter.adapt(e);
    }
  }

  @Override
  public TradeHistoryParams createTradeHistoryParams() {

    return new BitfinexTradeHistoryParams();
  }

  @Override
  public OpenOrdersParams createOpenOrdersParams() {
    return null;
  }

  @Override
  public Collection<Order> getOrder(OrderQueryParams... orderQueryParams) throws IOException {
    try {
      List<Order> openOrders = new ArrayList<>();

      for (OrderQueryParams orderParam : orderQueryParams) {
        BitfinexOrderStatusResponse orderStatus = getBitfinexOrderStatus(orderParam.getOrderId());
        BitfinexOrderStatusResponse[] orderStatuses = new BitfinexOrderStatusResponse[1];
        if (orderStatus != null) {
          orderStatuses[0] = orderStatus;
          OpenOrders orders = BitfinexAdapters.adaptOrders(orderStatuses);
          openOrders.add(orders.getOpenOrders().get(0));
        }
      }
      return openOrders;
    } catch (BitfinexException e) {
      throw BitfinexErrorAdapter.adapt(e);
    }
  }

  public BigDecimal getMakerFee() throws IOException {
    return getBitfinexAccountInfos()[0].getMakerFees();
  }

  public BigDecimal getTakerFee() throws IOException {
    return getBitfinexAccountInfos()[0].getTakerFees();
  }

  /**
   * Bitfinex 交易历史参数
   */
  public static class BitfinexTradeHistoryParams extends DefaultTradeHistoryParamsTimeSpan
      implements TradeHistoryParamCurrencyPair, TradeHistoryParamLimit, TradeHistoryParamsSorted {

    private CurrencyPair pair;
    private Integer limit;
    private Order order;

    public BitfinexTradeHistoryParams() {}

    /**
     * 获取交易对
     * @return
     */
    @Override
    public CurrencyPair getCurrencyPair() {

      return pair;
    }

    /**
     * 设置交易对
     * @param pair 对
     */
    @Override
    public void setCurrencyPair(CurrencyPair pair) {

      this.pair = pair;
    }

    @Override
    public Integer getLimit() {
      return this.limit;
    }

    @Override
    public void setLimit(Integer limit) {
      this.limit = limit;
    }

    @Override
    public Order getOrder() {
      return this.order;
    }

    @Override
    public void setOrder(Order order) {
      this.order = order;
    }
  }
}
