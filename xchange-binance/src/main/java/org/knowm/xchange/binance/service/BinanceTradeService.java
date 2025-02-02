package org.knowm.xchange.binance.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Value;
import org.knowm.xchange.binance.BinanceAdapters;
import org.knowm.xchange.binance.BinanceAuthenticated;
import org.knowm.xchange.binance.BinanceErrorAdapter;
import org.knowm.xchange.binance.BinanceExchange;
import org.knowm.xchange.binance.dto.BinanceException;
import org.knowm.xchange.binance.dto.trade.BinanceNewOrder;
import org.knowm.xchange.binance.dto.trade.BinanceOrder;
import org.knowm.xchange.binance.dto.trade.BinanceTrade;
import org.knowm.xchange.binance.dto.trade.OrderType;
import org.knowm.xchange.binance.dto.trade.TimeInForce;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.Order.IOrderFlags;
import org.knowm.xchange.dto.marketdata.Trades;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.MarketOrder;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.knowm.xchange.dto.trade.StopOrder;
import org.knowm.xchange.dto.trade.UserTrade;
import org.knowm.xchange.dto.trade.UserTrades;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.exceptions.NotAvailableFromExchangeException;
import org.knowm.xchange.service.trade.TradeService;
import org.knowm.xchange.service.trade.params.CancelOrderByCurrencyPair;
import org.knowm.xchange.service.trade.params.CancelOrderByIdParams;
import org.knowm.xchange.service.trade.params.CancelOrderParams;
import org.knowm.xchange.service.trade.params.TradeHistoryParamCurrencyPair;
import org.knowm.xchange.service.trade.params.TradeHistoryParamLimit;
import org.knowm.xchange.service.trade.params.TradeHistoryParams;
import org.knowm.xchange.service.trade.params.TradeHistoryParamsIdSpan;
import org.knowm.xchange.service.trade.params.TradeHistoryParamsTimeSpan;
import org.knowm.xchange.service.trade.params.orders.DefaultOpenOrdersParam;
import org.knowm.xchange.service.trade.params.orders.DefaultOpenOrdersParamCurrencyPair;
import org.knowm.xchange.service.trade.params.orders.OpenOrdersParamCurrencyPair;
import org.knowm.xchange.service.trade.params.orders.OpenOrdersParams;
import org.knowm.xchange.service.trade.params.orders.OrderQueryParamCurrencyPair;
import org.knowm.xchange.service.trade.params.orders.OrderQueryParams;
import org.knowm.xchange.utils.Assert;

/**
 * 币安交易服务
 */
public class BinanceTradeService extends BinanceTradeServiceRaw implements TradeService {
  /**
   * 币安交易服务
   * @param exchange
   * @param binance
   * @param resilienceRegistries
   */
  public BinanceTradeService(
      BinanceExchange exchange,
      BinanceAuthenticated binance,
      ResilienceRegistries resilienceRegistries) {
    super(exchange, binance, resilienceRegistries);
  }

  @Override
  public OpenOrders getOpenOrders() throws IOException {
    return getOpenOrders(new DefaultOpenOrdersParam());
  }

  /**
   * 获取打开的订单s
   * @param pair 货币对
   * @return
   * @throws IOException
   */
  public OpenOrders getOpenOrders(CurrencyPair pair) throws IOException {
    return getOpenOrders(new DefaultOpenOrdersParamCurrencyPair(pair));
  }

  @Override
  public OpenOrders getOpenOrders(OpenOrdersParams params) throws IOException {
    try {
      List<BinanceOrder> binanceOpenOrders;
      if (params instanceof OpenOrdersParamCurrencyPair) {
        OpenOrdersParamCurrencyPair pairParams = (OpenOrdersParamCurrencyPair) params;
        CurrencyPair pair = pairParams.getCurrencyPair();
        binanceOpenOrders = super.openOrders(pair);
      } else {
        binanceOpenOrders = super.openOrders();
      }

      List<LimitOrder> limitOrders = new ArrayList<>();
      List<Order> otherOrders = new ArrayList<>();
      binanceOpenOrders.forEach(
          binanceOrder -> {
            Order order = BinanceAdapters.adaptOrder(binanceOrder);
            if (order instanceof LimitOrder) {
              limitOrders.add((LimitOrder) order);
            } else {
              otherOrders.add(order);
            }
          });
      return new OpenOrders(limitOrders, otherOrders);
    } catch (BinanceException e) {
      throw BinanceErrorAdapter.adapt(e);
    }
  }

  @Override
  public String placeMarketOrder(MarketOrder mo) throws IOException {
    return placeOrder(OrderType.MARKET, mo, null, null, null, null, null);
  }

  @Override
  public String placeLimitOrder(LimitOrder limitOrder) throws IOException {
    TimeInForce tif = timeInForceFromOrder(limitOrder).orElse(TimeInForce.GTC);
    OrderType type;
    if (limitOrder.hasFlag(org.knowm.xchange.binance.dto.trade.BinanceOrderFlags.LIMIT_MAKER)) {
      type = OrderType.LIMIT_MAKER;
      tif = null;
    } else {
      type = OrderType.LIMIT;
    }
    return placeOrder(type, limitOrder, limitOrder.getLimitPrice(), null, null, null, tif);
  }

  @Override
  public String placeStopOrder(StopOrder order) throws IOException {
    // Time-in-force should not be provided for market orders but is required for
    // limit orders, order we only default it for limit orders. If the caller
    // specifies one for a market order, we don't remove it, since Binance might
    // allow
    // it at some point.
    TimeInForce tif =
        timeInForceFromOrder(order).orElse(order.getLimitPrice() != null ? TimeInForce.GTC : null);

    OrderType orderType = BinanceAdapters.adaptOrderType(order);

    return placeOrder(
        orderType, order, order.getLimitPrice(), order.getStopPrice(), null, null, tif);
  }

  /**
   * 订单生效时间
   * @param order 订单
   * @return
   */
  private Optional<TimeInForce> timeInForceFromOrder(Order order) {
    return order.getOrderFlags().stream()
        .filter(flag -> flag instanceof TimeInForce)
        .map(flag -> (TimeInForce) flag)
        .findFirst();
  }

  /**
   * 下订单
   * @param type 类型
   * @param order 订单
   * @param limitPrice 限制价格
   * @param stopPrice 停止价格
   * @param quoteOrderQty 订单数量报价
   * @param trailingDelta 尾随  变量增量
   * @param tif 生效时间
   * @return
   * @throws IOException
   */
  private String placeOrder(
      OrderType type,
      Order order,
      BigDecimal limitPrice,
      BigDecimal stopPrice,
      BigDecimal quoteOrderQty,
      Long trailingDelta,
      TimeInForce tif)
      throws IOException {
    try {
      Long recvWindow =
          (Long)
              exchange.getExchangeSpecification().getExchangeSpecificParametersItem("recvWindow");
      BinanceNewOrder newOrder =
          newOrder(
              order.getCurrencyPair(),
              BinanceAdapters.convert(order.getType()),
              type,
              tif,
              order.getOriginalAmount(),
              quoteOrderQty, // TODO (BigDecimal)order.getExtraValue("quoteOrderQty")
              limitPrice,
              getClientOrderId(order),
              stopPrice,
              trailingDelta, // TODO (Long)order.getExtraValue("trailingDelta")
              null,
              null);
      return Long.toString(newOrder.orderId);
    } catch (BinanceException e) {
      throw BinanceErrorAdapter.adapt(e);
    }
  }

  /**
   * 下测试订单
   * @param type 类型
   * @param order 订单
   * @param limitPrice 限制价格
   * @param stopPrice 停止价格
   * @throws IOException
   */
  public void placeTestOrder(
      OrderType type, Order order, BigDecimal limitPrice, BigDecimal stopPrice) throws IOException {
    placeTestOrder(type, order, limitPrice, stopPrice, null, null);
  }

  /**
   * 下测试订单
   * @param type 类型
   * @param order 订单
   * @param limitPrice 限制价格
   * @param stopPrice 停止价格
   * @param quoteOrderQty 报价订单数量
   * @param trailingDelta
   * @throws IOException
   */
  public void placeTestOrder(
      OrderType type,
      Order order,
      BigDecimal limitPrice,
      BigDecimal stopPrice,
      BigDecimal quoteOrderQty,
      Long trailingDelta)
      throws IOException {
    try {
      TimeInForce tif = timeInForceFromOrder(order).orElse(null);
      Long recvWindow =
          (Long)
              exchange.getExchangeSpecification().getExchangeSpecificParametersItem("recvWindow");
      testNewOrder(
          order.getCurrencyPair(),
          BinanceAdapters.convert(order.getType()),
          type,
          tif,
          order.getOriginalAmount(),
          quoteOrderQty,
          limitPrice,
          getClientOrderId(order),
          stopPrice,
          trailingDelta,
          null);
    } catch (BinanceException e) {
      throw BinanceErrorAdapter.adapt(e);
    }
  }

  /**
   * 获取客户订单Id
   * @param order 订单
   * @return
   */
  private String getClientOrderId(Order order) {

    String clientOrderId = null;
    for (IOrderFlags flags : order.getOrderFlags()) {
      if (flags instanceof BinanceOrderFlags) {
        BinanceOrderFlags bof = (BinanceOrderFlags) flags;
        if (clientOrderId == null) {
          clientOrderId = bof.getClientId();
        }
      }
    }
    return clientOrderId;
  }

  @Override
  public boolean cancelOrder(String orderId) {
    throw new ExchangeException("You need to provide the currency pair to cancel an order. 您需要提供货币对才能取消订单。");
  }

  @Override
  public boolean cancelOrder(CancelOrderParams params) throws IOException {
    try {
      if (!(params instanceof CancelOrderByCurrencyPair)
          && !(params instanceof CancelOrderByIdParams)) {
        throw new ExchangeException(
            "You need to provide the currency pair and the order id to cancel an order. 您需要提供货币对和订单 ID 才能取消订单。");
      }
      CancelOrderByCurrencyPair paramCurrencyPair = (CancelOrderByCurrencyPair) params;
      CancelOrderByIdParams paramId = (CancelOrderByIdParams) params;
      super.cancelOrder(
          paramCurrencyPair.getCurrencyPair(),
          BinanceAdapters.id(paramId.getOrderId()),
          null,
          null);
      return true;
    } catch (BinanceException e) {
      throw BinanceErrorAdapter.adapt(e);
    }
  }

  @Override
  public Class[] getRequiredCancelOrderParamClasses() {
    return new Class[] {CancelOrderByIdParams.class, CancelOrderByCurrencyPair.class};
  }

  @Override
  public UserTrades getTradeHistory(TradeHistoryParams params) throws IOException {
    try {
      Assert.isTrue(
          params instanceof TradeHistoryParamCurrencyPair,
          "You need to provide the currency pair to get the user trades. 您需要提供货币对才能让用户进行交易。");
      TradeHistoryParamCurrencyPair pairParams = (TradeHistoryParamCurrencyPair) params;
      CurrencyPair pair = pairParams.getCurrencyPair();
      if (pair == null) {
        throw new ExchangeException(
            "You need to provide the currency pair to get the user trades. 您需要提供货币对才能让用户进行交易。");
      }
      Long orderId = null;
      Long startTime = null;
      Long endTime = null;
      if (params instanceof TradeHistoryParamsTimeSpan) {
        if (((TradeHistoryParamsTimeSpan) params).getStartTime() != null) {
          startTime = ((TradeHistoryParamsTimeSpan) params).getStartTime().getTime();
        }
        if (((TradeHistoryParamsTimeSpan) params).getEndTime() != null) {
          endTime = ((TradeHistoryParamsTimeSpan) params).getEndTime().getTime();
        }
      }
      Long fromId = null;
      if (params instanceof TradeHistoryParamsIdSpan) {
        TradeHistoryParamsIdSpan idParams = (TradeHistoryParamsIdSpan) params;
        try {
          fromId = BinanceAdapters.id(idParams.getStartId());
        } catch (Throwable ignored) {
        }
      }
      if ((fromId != null) && (startTime != null || endTime != null)) {
        throw new ExchangeException(
            "You should either specify the id from which you get the user trades from or start and end times." +
                    "您应该指定从中获取用户交易的 ID，或者指定开始和结束时间。" +
                    " If you specify both, Binance will only honour the fromId parameter." +
                    "如果您同时指定两者，币安将只接受 fromId 参数。");
      }

      Integer limit = null;
      if (params instanceof TradeHistoryParamLimit) {
        TradeHistoryParamLimit limitParams = (TradeHistoryParamLimit) params;
        limit = limitParams.getLimit();
      }

      List<BinanceTrade> binanceTrades =
          super.myTrades(pair, orderId, startTime, endTime, fromId, limit);
      List<UserTrade> trades =
          binanceTrades.stream()
              .map(
                  t ->
                      new UserTrade.Builder()
                          .type(BinanceAdapters.convertType(t.isBuyer))
                          .originalAmount(t.qty)
                          .currencyPair(pair)
                          .price(t.price)
                          .timestamp(t.getTime())
                          .id(Long.toString(t.id))
                          .orderId(Long.toString(t.orderId))
                          .feeAmount(t.commission)
                          .feeCurrency(Currency.getInstance(t.commissionAsset))
                          .build())
              .collect(Collectors.toList());
      long lastId = binanceTrades.stream().map(t -> t.id).max(Long::compareTo).orElse(0L);
      return new UserTrades(trades, lastId, Trades.TradeSortType.SortByTimestamp);
    } catch (BinanceException e) {
      throw BinanceErrorAdapter.adapt(e);
    }
  }

  @Override
  public TradeHistoryParams createTradeHistoryParams() {

    return new BinanceTradeHistoryParams();
  }

  @Override
  public OpenOrdersParams createOpenOrdersParams() {

    return new DefaultOpenOrdersParamCurrencyPair();
  }

  @Override
  public Collection<Order> getOrder(String... orderIds) {

    throw new NotAvailableFromExchangeException();
  }

  @Override
  public Class getRequiredOrderQueryParamClass() {
    return OrderQueryParamCurrencyPair.class;
  }

  @Override
  public Collection<Order> getOrder(OrderQueryParams... params) throws IOException {
    try {
      Collection<Order> orders = new ArrayList<>();
      for (OrderQueryParams param : params) {
        if (!(param instanceof OrderQueryParamCurrencyPair)) {
          throw new ExchangeException(
              "Parameters must be an instance of OrderQueryParamCurrencyPair " +
                      "参数必须是 OrderQueryParamCurrencyPair 的实例");
        }
        OrderQueryParamCurrencyPair orderQueryParamCurrencyPair =
            (OrderQueryParamCurrencyPair) param;
        if (orderQueryParamCurrencyPair.getCurrencyPair() == null
            || orderQueryParamCurrencyPair.getOrderId() == null) {
          throw new ExchangeException(
              "You need to provide the currency pair and the order id to query an order." +
                      "您需要提供货币对和订单id来查询订单。");
        }

        orders.add(
            BinanceAdapters.adaptOrder(
                super.orderStatus(
                    orderQueryParamCurrencyPair.getCurrencyPair(),
                    BinanceAdapters.id(orderQueryParamCurrencyPair.getOrderId()),
                    null)));
      }
      return orders;
    } catch (BinanceException e) {
      throw BinanceErrorAdapter.adapt(e);
    }
  }

  /**
   * 币安订单标志
   */
  public interface BinanceOrderFlags extends IOrderFlags {
    /**
     * 客户 ID
     * @param clientId 客户 ID
     * @return
     */
    static BinanceOrderFlags withClientId(String clientId) {
      return new ClientIdFlag(clientId);
    }

    /**
     * 获取客户id
     * 在字段“newClientOrderId”中使用
     * Used in fields 'newClientOrderId'
     * */
    String getClientId();
  }

  /**
   * 客户id标记
   */
  @Value
  static final class ClientIdFlag implements BinanceOrderFlags {

    private final String clientId;
  }
}
