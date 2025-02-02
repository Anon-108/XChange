package org.knowm.xchange.bitfinex.service;

import static org.knowm.xchange.bitfinex.BitfinexResilience.BITFINEX_RATE_LIMITER;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.knowm.xchange.bitfinex.BitfinexExchange;
import org.knowm.xchange.bitfinex.dto.BitfinexException;
import org.knowm.xchange.bitfinex.v1.BitfinexOrderType;
import org.knowm.xchange.bitfinex.v1.BitfinexUtils;
import org.knowm.xchange.bitfinex.v1.dto.account.BitfinexWithdrawalRequest;
import org.knowm.xchange.bitfinex.v1.dto.account.BitfinexWithdrawalResponse;
import org.knowm.xchange.bitfinex.v1.dto.trade.BitfinexAccountInfosResponse;
import org.knowm.xchange.bitfinex.v1.dto.trade.BitfinexActiveCreditsRequest;
import org.knowm.xchange.bitfinex.v1.dto.trade.BitfinexActivePositionsResponse;
import org.knowm.xchange.bitfinex.v1.dto.trade.BitfinexCancelAllOrdersRequest;
import org.knowm.xchange.bitfinex.v1.dto.trade.BitfinexCancelOfferRequest;
import org.knowm.xchange.bitfinex.v1.dto.trade.BitfinexCancelOrderMultiRequest;
import org.knowm.xchange.bitfinex.v1.dto.trade.BitfinexCancelOrderRequest;
import org.knowm.xchange.bitfinex.v1.dto.trade.BitfinexCreditResponse;
import org.knowm.xchange.bitfinex.v1.dto.trade.BitfinexFundingTradeResponse;
import org.knowm.xchange.bitfinex.v1.dto.trade.BitfinexLimitOrder;
import org.knowm.xchange.bitfinex.v1.dto.trade.BitfinexNewOfferRequest;
import org.knowm.xchange.bitfinex.v1.dto.trade.BitfinexNewOrder;
import org.knowm.xchange.bitfinex.v1.dto.trade.BitfinexNewOrderMultiRequest;
import org.knowm.xchange.bitfinex.v1.dto.trade.BitfinexNewOrderMultiResponse;
import org.knowm.xchange.bitfinex.v1.dto.trade.BitfinexNewOrderRequest;
import org.knowm.xchange.bitfinex.v1.dto.trade.BitfinexNonceOnlyRequest;
import org.knowm.xchange.bitfinex.v1.dto.trade.BitfinexOfferStatusRequest;
import org.knowm.xchange.bitfinex.v1.dto.trade.BitfinexOfferStatusResponse;
import org.knowm.xchange.bitfinex.v1.dto.trade.BitfinexOrderFlags;
import org.knowm.xchange.bitfinex.v1.dto.trade.BitfinexOrderStatusRequest;
import org.knowm.xchange.bitfinex.v1.dto.trade.BitfinexOrderStatusResponse;
import org.knowm.xchange.bitfinex.v1.dto.trade.BitfinexOrdersHistoryRequest;
import org.knowm.xchange.bitfinex.v1.dto.trade.BitfinexPastFundingTradesRequest;
import org.knowm.xchange.bitfinex.v1.dto.trade.BitfinexPastTradesRequest;
import org.knowm.xchange.bitfinex.v1.dto.trade.BitfinexReplaceOrderRequest;
import org.knowm.xchange.bitfinex.v1.dto.trade.BitfinexTradeResponse;
import org.knowm.xchange.bitfinex.v2.dto.EmptyRequest;
import org.knowm.xchange.bitfinex.v2.dto.trade.ActiveOrder;
import org.knowm.xchange.bitfinex.v2.dto.trade.OrderTrade;
import org.knowm.xchange.bitfinex.v2.dto.trade.Position;
import org.knowm.xchange.bitfinex.v2.dto.trade.Trade;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.Order.OrderType;
import org.knowm.xchange.dto.trade.FixedRateLoanOrder;
import org.knowm.xchange.dto.trade.FloatingRateLoanOrder;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.MarketOrder;
import org.knowm.xchange.exceptions.ExchangeException;

/**
 * Bitfinex 交易服务原生
 */
public class BitfinexTradeServiceRaw extends BitfinexBaseService {

  /**
   * Constructor
   *
   * @param exchange
   */
  public BitfinexTradeServiceRaw(
      BitfinexExchange exchange, ResilienceRegistries resilienceRegistries) {

    super(exchange, resilienceRegistries);
  }

  /**
   * 获取bitfinex账户信息
   * @return
   * @throws IOException
   */
  public BitfinexAccountInfosResponse[] getBitfinexAccountInfos() throws IOException {
    return decorateApiCall(
            () ->
                bitfinex.accountInfos(
                    apiKey,
                    payloadCreator,
                    signatureCreator,
                    new BitfinexNonceOnlyRequest(
                        "/v1/account_infos",
                        String.valueOf(exchange.getNonceFactory().createValue()))))
        .withRateLimiter(rateLimiter(BITFINEX_RATE_LIMITER))
        .call();
  }

  /**
   * 获取 Bitfinex 打开的订单
   * @return
   * @throws IOException
   */
  public BitfinexOrderStatusResponse[] getBitfinexOpenOrders() throws IOException {
    return decorateApiCall(
            () ->
                bitfinex.activeOrders(
                    apiKey,
                    payloadCreator,
                    signatureCreator,
                    new BitfinexNonceOnlyRequest(
                        "/v1/orders", String.valueOf(exchange.getNonceFactory().createValue()))))
        .withRateLimiter(rateLimiter(BITFINEX_RATE_LIMITER))
        .call();
  }

  public BitfinexOrderStatusResponse[] getBitfinexOrdersHistory(long limit) throws IOException {
    return decorateApiCall(
            () ->
                bitfinex.ordersHist(
                    apiKey,
                    payloadCreator,
                    signatureCreator,
                    new BitfinexOrdersHistoryRequest(
                        String.valueOf(exchange.getNonceFactory().createValue()), limit)))
        .withRateLimiter(rateLimiter(BITFINEX_RATE_LIMITER))
        .call();
  }

  /**
   * 获取 Bitfinex 公开报价
   * @return
   * @throws IOException
   */
  public BitfinexOfferStatusResponse[] getBitfinexOpenOffers() throws IOException {
    return decorateApiCall(
            () ->
                bitfinex.activeOffers(
                    apiKey,
                    payloadCreator,
                    signatureCreator,
                    new BitfinexNonceOnlyRequest(
                        "/v1/offers", String.valueOf(exchange.getNonceFactory().createValue()))))
        .withRateLimiter(rateLimiter(BITFINEX_RATE_LIMITER))
        .call();
  }

  /**
   * 下 Bitfinex 市价单
   * @param marketOrder 市价单
   * @param bitfinexOrderType bitfinex订单类型
   * @return
   * @throws IOException
   */
  public BitfinexOrderStatusResponse placeBitfinexMarketOrder(
      MarketOrder marketOrder, BitfinexOrderType bitfinexOrderType) throws IOException {

    String pair = BitfinexUtils.toPairStringV1(marketOrder.getCurrencyPair());
    String type =
        (marketOrder.getType().equals(OrderType.BID)
                || marketOrder.getType().equals(OrderType.EXIT_ASK))
            ? "buy"
            : "sell";
    String orderType = bitfinexOrderType.toString();

    return decorateApiCall(
            () ->
                bitfinex.newOrder(
                    apiKey,
                    payloadCreator,
                    signatureCreator,
                    new BitfinexNewOrderRequest(
                        String.valueOf(exchange.getNonceFactory().createValue()),
                        pair,
                        marketOrder.getOriginalAmount(),
                        BigDecimal.ONE,
                        "bitfinex",
                        type,
                        orderType,
                        null)))
        .withRateLimiter(rateLimiter(BITFINEX_RATE_LIMITER))
        .call();
  }

  /**
   * 下限价单
   * @param limitOrder 限价单
   * @param orderType 订单类型
   * @return
   * @throws IOException
   */
  public BitfinexOrderStatusResponse placeBitfinexLimitOrder(
      LimitOrder limitOrder, BitfinexOrderType orderType) throws IOException {

    return sendLimitOrder(limitOrder, orderType, Long.MIN_VALUE);
  }

  public BitfinexOrderStatusResponse replaceBitfinexLimitOrder(
      LimitOrder limitOrder, BitfinexOrderType orderType, long replaceOrderId) throws IOException {
    if (limitOrder instanceof BitfinexLimitOrder
        && ((BitfinexLimitOrder) limitOrder).getOcoStopLimit() != null) {
      throw new ExchangeException("OCO orders are not yet editable OCO 订单尚不可编辑");
    }
    return sendLimitOrder(limitOrder, orderType, replaceOrderId);
  }

  /**
   * 发送限价单
   * @param limitOrder 限价单
   * @param bitfinexOrderType 订单类型
   * @param replaceOrderId 替换订单id
   * @return
   * @throws IOException
   */
  private BitfinexOrderStatusResponse sendLimitOrder(
      LimitOrder limitOrder, BitfinexOrderType bitfinexOrderType, long replaceOrderId)
      throws IOException {

    String pair = BitfinexUtils.toPairStringV1(limitOrder.getCurrencyPair());
    String type =
        (limitOrder.getType().equals(Order.OrderType.BID)
                || limitOrder.getType().equals(Order.OrderType.EXIT_ASK))
            ? "buy"
            : "sell";
    String orderType = bitfinexOrderType.toString();

    boolean isHidden;
    if (limitOrder.hasFlag(BitfinexOrderFlags.HIDDEN)) {
      isHidden = true;
    } else {
      isHidden = false;
    }
    boolean isPostOnly;
    if (limitOrder.hasFlag(BitfinexOrderFlags.POST_ONLY)) {
      isPostOnly = true;
    } else {
      isPostOnly = false;
    }

    BitfinexOrderStatusResponse response;
    if (replaceOrderId == Long.MIN_VALUE) { // order entry // 订单入口
      BigDecimal ocoAmount =
          limitOrder instanceof BitfinexLimitOrder
              ? ((BitfinexLimitOrder) limitOrder).getOcoStopLimit()
              : null;
      BitfinexNewOrderRequest request =
          new BitfinexNewOrderRequest(
              String.valueOf(exchange.getNonceFactory().createValue()),
              pair,
              limitOrder.getOriginalAmount(),
              limitOrder.getLimitPrice(),
              "bitfinex",
              type,
              orderType,
              isHidden,
              isPostOnly,
              ocoAmount);
      response =
          decorateApiCall(
                  () -> bitfinex.newOrder(apiKey, payloadCreator, signatureCreator, request))
              .withRateLimiter(rateLimiter(BITFINEX_RATE_LIMITER))
              .call();

    } else { // order amend // 订单修改
      boolean useRemaining = limitOrder.hasFlag(BitfinexOrderFlags.USE_REMAINING);

      BitfinexReplaceOrderRequest request =
          new BitfinexReplaceOrderRequest(
              String.valueOf(exchange.getNonceFactory().createValue()),
              replaceOrderId,
              pair,
              limitOrder.getOriginalAmount(),
              limitOrder.getLimitPrice(),
              "bitfinex",
              type,
              orderType,
              isHidden,
              isPostOnly,
              useRemaining);
      response =
          decorateApiCall(
                  () -> bitfinex.replaceOrder(apiKey, payloadCreator, signatureCreator, request))
              .withRateLimiter(rateLimiter(BITFINEX_RATE_LIMITER))
              .call();
    }

    if (limitOrder instanceof BitfinexLimitOrder) {
      BitfinexLimitOrder bitfinexOrder = (BitfinexLimitOrder) limitOrder;
      bitfinexOrder.setResponse(response);
    }
    return response;
  }

  public BitfinexNewOrderMultiResponse placeBitfinexOrderMulti(
      List<? extends Order> orders, BitfinexOrderType bitfinexOrderType) throws IOException {

    BitfinexNewOrder[] bitfinexOrders = new BitfinexNewOrder[orders.size()];

    for (int i = 0; i < bitfinexOrders.length; i++) {
      Order o = orders.get(i);
      if (o instanceof LimitOrder) {
        LimitOrder limitOrder = (LimitOrder) o;
        String pair = BitfinexUtils.toPairStringV1(limitOrder.getCurrencyPair());
        String type =
            (limitOrder.getType().equals(OrderType.BID)
                    || limitOrder.getType().equals(OrderType.EXIT_ASK))
                ? "buy"
                : "sell";
        String orderType = bitfinexOrderType.toString();
        bitfinexOrders[i] =
            new BitfinexNewOrder(
                pair,
                "bitfinex",
                type,
                orderType,
                limitOrder.getOriginalAmount(),
                limitOrder.getLimitPrice());
      } else if (o instanceof MarketOrder) {
        MarketOrder marketOrder = (MarketOrder) o;
        String pair = BitfinexUtils.toPairStringV1(marketOrder.getCurrencyPair());
        String type =
            (marketOrder.getType().equals(OrderType.BID)
                    || marketOrder.getType().equals(OrderType.EXIT_ASK))
                ? "buy"
                : "sell";
        String orderType = bitfinexOrderType.toString();
        bitfinexOrders[i] =
            new BitfinexNewOrder(
                pair, "bitfinex", type, orderType, marketOrder.getOriginalAmount(), BigDecimal.ONE);
      }
    }

    BitfinexNewOrderMultiRequest request =
        new BitfinexNewOrderMultiRequest(
            String.valueOf(exchange.getNonceFactory().createValue()), bitfinexOrders);
    return decorateApiCall(
            () -> bitfinex.newOrderMulti(apiKey, payloadCreator, signatureCreator, request))
        .withRateLimiter(rateLimiter(BITFINEX_RATE_LIMITER))
        .call();
  }

  /**
   * 下Bitfinex固定利率贷款订单
   * @param loanOrder 贷款订单
   * @param orderType 订单类型
   * @return
   * @throws IOException
   */
  public BitfinexOfferStatusResponse placeBitfinexFixedRateLoanOrder(
      FixedRateLoanOrder loanOrder, BitfinexOrderType orderType) throws IOException {
    String direction = loanOrder.getType() == OrderType.BID ? "loan" : "lend";
    return decorateApiCall(
            () ->
                bitfinex.newOffer(
                    apiKey,
                    payloadCreator,
                    signatureCreator,
                    new BitfinexNewOfferRequest(
                        String.valueOf(exchange.getNonceFactory().createValue()),
                        loanOrder.getCurrency(),
                        loanOrder.getOriginalAmount(),
                        loanOrder.getRate(),
                        loanOrder.getDayPeriod(),
                        direction)))
        .withRateLimiter(rateLimiter(BITFINEX_RATE_LIMITER))
        .call();
  }

  /**
   * 下Bitfinex浮动利率贷款订单
   * @param loanOrder 贷款订单
   * @param orderType 订单类型
   * @return
   * @throws IOException
   */
  public BitfinexOfferStatusResponse placeBitfinexFloatingRateLoanOrder(
      FloatingRateLoanOrder loanOrder, BitfinexOrderType orderType) throws IOException {

    String direction = loanOrder.getType() == OrderType.BID ? "loan" : "lend";

    return decorateApiCall(
            () ->
                bitfinex.newOffer(
                    apiKey,
                    payloadCreator,
                    signatureCreator,
                    new BitfinexNewOfferRequest(
                        String.valueOf(exchange.getNonceFactory().createValue()),
                        loanOrder.getCurrency(),
                        loanOrder.getOriginalAmount(),
                        new BigDecimal("0.0"),
                        loanOrder.getDayPeriod(),
                        direction)))
        .withRateLimiter(rateLimiter(BITFINEX_RATE_LIMITER))
        .call();
  }

  /**
   * 取消Bitfinex订单
   * @param orderId 订单id
   * @return
   * @throws IOException
   */
  public boolean cancelBitfinexOrder(String orderId) throws IOException {

    try {
      decorateApiCall(
              () ->
                  bitfinex.cancelOrders(
                      apiKey,
                      payloadCreator,
                      signatureCreator,
                      new BitfinexCancelOrderRequest(
                          String.valueOf(exchange.getNonceFactory().createValue()),
                          Long.valueOf(orderId))))
          .withRateLimiter(rateLimiter(BITFINEX_RATE_LIMITER))
          .call();
      return true;
    } catch (BitfinexException e) {
      if (e.getMessage().equals("Order could not be cancelled. 无法取消订单。")) {
        return false;
      } else {
        throw e;
      }
    }
  }

  /**
   * 取消所有Bitfinex订单
   * @return
   * @throws IOException
   */
  public boolean cancelAllBitfinexOrders() throws IOException {

    try {
      decorateApiCall(
              () ->
                  bitfinex.cancelAllOrders(
                      apiKey,
                      payloadCreator,
                      signatureCreator,
                      new BitfinexCancelAllOrdersRequest(
                          String.valueOf(exchange.getNonceFactory().createValue()))))
          .withRateLimiter(rateLimiter(BITFINEX_RATE_LIMITER))
          .call();
      return true;
    } catch (BitfinexException e) {
      if (e.getMessage().equals("Orders could not be cancelled. 无法取消订单。")) {
        return false;
      } else {
        throw e;
      }
    }
  }

  public boolean cancelBitfinexOrderMulti(List<String> orderIds) throws IOException {

    long[] cancelOrderIds = new long[orderIds.size()];

    for (int i = 0; i < cancelOrderIds.length; i++) {
      cancelOrderIds[i] = Long.valueOf(orderIds.get(i));
    }

    decorateApiCall(
            () ->
                bitfinex.cancelOrderMulti(
                    apiKey,
                    payloadCreator,
                    signatureCreator,
                    new BitfinexCancelOrderMultiRequest(
                        String.valueOf(exchange.getNonceFactory().createValue()), cancelOrderIds)))
        .withRateLimiter(rateLimiter(BITFINEX_RATE_LIMITER))
        .call();
    return true;
  }

  /**
   * 取消Bitfinex 出价
   * @param offerId 出价id
   * @return
   * @throws IOException
   */
  public BitfinexOfferStatusResponse cancelBitfinexOffer(String offerId) throws IOException {
    return decorateApiCall(
            () ->
                bitfinex.cancelOffer(
                    apiKey,
                    payloadCreator,
                    signatureCreator,
                    new BitfinexCancelOfferRequest(
                        String.valueOf(exchange.getNonceFactory().createValue()),
                        Long.valueOf(offerId))))
        .withRateLimiter(rateLimiter(BITFINEX_RATE_LIMITER))
        .call();
  }

  /**
   * 获取bitfinex订单状态
   * @param orderId 订单id
   * @return
   * @throws IOException
   */
  public BitfinexOrderStatusResponse getBitfinexOrderStatus(String orderId) throws IOException {
    return decorateApiCall(
            () ->
                bitfinex.orderStatus(
                    apiKey,
                    payloadCreator,
                    signatureCreator,
                    new BitfinexOrderStatusRequest(
                        String.valueOf(exchange.getNonceFactory().createValue()),
                        Long.valueOf(orderId))))
        .withRateLimiter(rateLimiter(BITFINEX_RATE_LIMITER))
        .call();
  }

  public BitfinexOfferStatusResponse getBitfinexOfferStatusResponse(String offerId)
      throws IOException {
    return decorateApiCall(
            () ->
                bitfinex.offerStatus(
                    apiKey,
                    payloadCreator,
                    signatureCreator,
                    new BitfinexOfferStatusRequest(
                        String.valueOf(exchange.getNonceFactory().createValue()),
                        Long.valueOf(offerId))))
        .withRateLimiter(rateLimiter(BITFINEX_RATE_LIMITER))
        .call();
  }

  /**
   *获取 Bitfinex 资金历史
   * @param symbol 符号
   * @param until 直到/为止
   * @param limit_trades 交易额度
   * @return
   * @throws IOException
   */
  public BitfinexFundingTradeResponse[] getBitfinexFundingHistory(
      String symbol, Date until, int limit_trades) throws IOException {
    return decorateApiCall(
            () ->
                bitfinex.pastFundingTrades(
                    apiKey,
                    payloadCreator,
                    signatureCreator,
                    new BitfinexPastFundingTradesRequest(
                        String.valueOf(exchange.getNonceFactory().createValue()),
                        symbol,
                        until,
                        limit_trades)))
        .withRateLimiter(rateLimiter(BITFINEX_RATE_LIMITER))
        .call();
  }

  public BitfinexTradeResponse[] getBitfinexTradeHistory(
      String symbol, long startTime, Long endTime, Integer limit, Integer reverse)
      throws IOException {
    return decorateApiCall(
            () ->
                bitfinex.pastTrades(
                    apiKey,
                    payloadCreator,
                    signatureCreator,
                    new BitfinexPastTradesRequest(
                        String.valueOf(exchange.getNonceFactory().createValue()),
                        symbol,
                        startTime,
                        endTime,
                        limit,
                        reverse)))
        .withRateLimiter(rateLimiter(BITFINEX_RATE_LIMITER))
        .call();
  }

  /**
   * 获得 Bitfinex 活跃积分
   * @return
   * @throws IOException
   */
  public BitfinexCreditResponse[] getBitfinexActiveCredits() throws IOException {
    return decorateApiCall(
            () ->
                bitfinex.activeCredits(
                    apiKey,
                    payloadCreator,
                    signatureCreator,
                    new BitfinexActiveCreditsRequest(
                        String.valueOf(exchange.getNonceFactory().createValue()))))
        .withRateLimiter(rateLimiter(BITFINEX_RATE_LIMITER))
        .call();
  }

  /**
   * 提取
   * @param withdrawType 提取类型
   * @param walletSelected 选择钱包
   * @param amount 数量
   * @param address 地址
   * @return
   * @throws IOException
   */
  public String withdraw(
      String withdrawType, String walletSelected, BigDecimal amount, String address)
      throws IOException {
    return withdraw(withdrawType, walletSelected, amount, address, null);
  }

  /**
   * 提取
   * @param withdrawType 提取类型
   * @param walletSelected 选择钱包
   * @param amount 数量
   * @param address 地址
   * @param paymentId 支付id
   * @return
   * @throws IOException
   */
  public String withdraw(
      String withdrawType,
      String walletSelected,
      BigDecimal amount,
      String address,
      String paymentId)
      throws IOException {

    BitfinexWithdrawalResponse[] withdrawRepsonse =
        decorateApiCall(
                () ->
                    bitfinex.withdraw(
                        apiKey,
                        payloadCreator,
                        signatureCreator,
                        new BitfinexWithdrawalRequest(
                            String.valueOf(exchange.getNonceFactory().createValue()),
                            withdrawType,
                            walletSelected,
                            amount,
                            address,
                            paymentId)))
            .withRateLimiter(rateLimiter(BITFINEX_RATE_LIMITER))
            .call();
    return withdrawRepsonse[0].getWithdrawalId();
  }

  public BitfinexActivePositionsResponse[] getBitfinexActivePositions() throws IOException {
    return decorateApiCall(
            () ->
                bitfinex.activePositions(
                    apiKey,
                    payloadCreator,
                    signatureCreator,
                    new BitfinexNonceOnlyRequest(
                        "/v1/positions", String.valueOf(exchange.getNonceFactory().createValue()))))
        .withRateLimiter(rateLimiter(BITFINEX_RATE_LIMITER))
        .call();
  }

  public List<Position> getBitfinexActivePositionsV2() throws IOException {
    return decorateApiCall(
            () ->
                bitfinexV2.activePositions(
                    exchange.getNonceFactory(), apiKey, signatureV2, EmptyRequest.INSTANCE))
        .withRateLimiter(rateLimiter(BITFINEX_RATE_LIMITER))
        .call();
  }

  /**
   * 获取bitfinex交易V2
   * @param symbol 符号
   * @param startTimeMillis 开始时间 毫秒
   * @param endTimeMillis 结束时间
   * @param limit 额度
   * @param sort 种类/排序
   * @return
   * @throws IOException
   */
  public List<Trade> getBitfinexTradesV2(
      String symbol, Long startTimeMillis, Long endTimeMillis, Long limit, Long sort)
      throws IOException {
    if (StringUtils.isBlank(symbol)) {
      return decorateApiCall(
              () ->
                  bitfinexV2.getTrades(
                      exchange.getNonceFactory(),
                      apiKey,
                      signatureV2,
                      startTimeMillis,
                      endTimeMillis,
                      limit,
                      sort,
                      EmptyRequest.INSTANCE))
          .withRateLimiter(rateLimiter(BITFINEX_RATE_LIMITER))
          .call();
    }

    return decorateApiCall(
            () ->
                bitfinexV2.getTrades(
                    exchange.getNonceFactory(),
                    apiKey,
                    signatureV2,
                    symbol,
                    startTimeMillis,
                    endTimeMillis,
                    limit,
                    sort,
                    EmptyRequest.INSTANCE))
        .withRateLimiter(rateLimiter(BITFINEX_RATE_LIMITER))
        .call();
  }

  public List<ActiveOrder> getBitfinexActiveOrdesV2(String symbol) throws IOException {
    if (symbol == null) {
      symbol = ""; // for empty symbol all active orders are returned  // 对于空品种，返回所有活跃订单
    }
    final String symbol2 = symbol;
    return decorateApiCall(
            () ->
                bitfinexV2.getActiveOrders(
                    exchange.getNonceFactory(),
                    apiKey,
                    signatureV2,
                    symbol2,
                    EmptyRequest.INSTANCE))
        .withRateLimiter(rateLimiter(BITFINEX_RATE_LIMITER))
        .call();
  }

  public List<OrderTrade> getBitfinexOrderTradesV2(final String symbol, final Long orderId)
      throws IOException {
    if (symbol == null || orderId == null)
      throw new NullPointerException(
          String.format(
              "Invalid request fields symbol 无效的请求字段符号 [%s] and orderId 和 orderId [%s] are mandatory for get order trades call 对于获取订单交易调用是强制性的",
              symbol, orderId));

    return decorateApiCall(
            () ->
                bitfinexV2.getOrderTrades(
                    exchange.getNonceFactory(),
                    apiKey,
                    signatureV2,
                    symbol,
                    orderId,
                    EmptyRequest.INSTANCE))
        .withRateLimiter(rateLimiter(BITFINEX_RATE_LIMITER))
        .call();
  }
}
