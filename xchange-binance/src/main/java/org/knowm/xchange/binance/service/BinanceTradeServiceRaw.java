package org.knowm.xchange.binance.service;

import static org.knowm.xchange.binance.BinanceResilience.ORDERS_PER_DAY_RATE_LIMITER;
import static org.knowm.xchange.binance.BinanceResilience.ORDERS_PER_SECOND_RATE_LIMITER;
import static org.knowm.xchange.binance.BinanceResilience.REQUEST_WEIGHT_RATE_LIMITER;
import static org.knowm.xchange.client.ResilienceRegistries.NON_IDEMPOTENT_CALLS_RETRY_CONFIG_NAME;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.knowm.xchange.binance.BinanceAdapters;
import org.knowm.xchange.binance.BinanceAuthenticated;
import org.knowm.xchange.binance.BinanceExchange;
import org.knowm.xchange.binance.dto.BinanceException;
import org.knowm.xchange.binance.dto.trade.BinanceCancelledOrder;
import org.knowm.xchange.binance.dto.trade.BinanceDustLog;
import org.knowm.xchange.binance.dto.trade.BinanceListenKey;
import org.knowm.xchange.binance.dto.trade.BinanceNewOrder;
import org.knowm.xchange.binance.dto.trade.BinanceOrder;
import org.knowm.xchange.binance.dto.trade.BinanceTrade;
import org.knowm.xchange.binance.dto.trade.OrderSide;
import org.knowm.xchange.binance.dto.trade.OrderType;
import org.knowm.xchange.binance.dto.trade.TimeInForce;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.exceptions.ExchangeException;

/**
 * 币安贸易服务 原生
 */
public class BinanceTradeServiceRaw extends BinanceBaseService {
  /**
   * 币安贸易服务 原生
   * @param exchange 交换
   * @param binance 币安
   * @param resilienceRegistries 恢复注册
   */
  protected BinanceTradeServiceRaw(
      BinanceExchange exchange,
      BinanceAuthenticated binance,
      ResilienceRegistries resilienceRegistries) {
    super(exchange, binance, resilienceRegistries);
  }

  /**
   * 打开订单s
   * @return
   * @throws BinanceException
   * @throws IOException
   */
  public List<BinanceOrder> openOrders() throws BinanceException, IOException {
    return openOrders(null);
  }

  /**
   * 打开订单s
   * @param pair 货币对
   * @return
   * @throws BinanceException
   * @throws IOException
   */
  public List<BinanceOrder> openOrders(CurrencyPair pair) throws BinanceException, IOException {
    return decorateApiCall(
            () ->
                binance.openOrders(
                    Optional.ofNullable(pair).map(BinanceAdapters::toSymbol).orElse(null),
                    getRecvWindow(),
                    getTimestampFactory(),
                    apiKey,
                    signatureCreator))
        .withRetry(retry("openOrders"))
        .withRateLimiter(rateLimiter(REQUEST_WEIGHT_RATE_LIMITER), openOrdersPermits(pair))
        .call();
  }

  /**
   * 新订单
   * @param pair 货币对
   * @param side 订购方
   * @param type 订单类型
   * @param timeInForce 生效时间
   * @param quantity 数量
   * @param quoteOrderQty 订单数量报价
   * @param price 价格
   * @param newClientOrderId 新客户订单id
   * @param stopPrice 停止价格
   * @param trailingDelta 尾随/后续 变量增量
   * @param icebergQty 冰山数量
   * @param newOrderRespType 新的订单响应类型
   * @return
   * @throws IOException
   * @throws BinanceException
   */
  public BinanceNewOrder newOrder(
      CurrencyPair pair,
      OrderSide side,
      OrderType type,
      TimeInForce timeInForce,
      BigDecimal quantity,
      BigDecimal quoteOrderQty,
      BigDecimal price,
      String newClientOrderId,
      BigDecimal stopPrice,
      Long trailingDelta,
      BigDecimal icebergQty,
      BinanceNewOrder.NewOrderResponseType newOrderRespType)
      throws IOException, BinanceException {
    return decorateApiCall(
            () ->
                binance.newOrder(
                    BinanceAdapters.toSymbol(pair),
                    side,
                    type,
                    timeInForce,
                    quantity,
                    quoteOrderQty,
                    price,
                    newClientOrderId,
                    stopPrice,
                    trailingDelta,
                    icebergQty,
                    newOrderRespType,
                    getRecvWindow(),
                    getTimestampFactory(),
                    apiKey,
                    signatureCreator))
        .withRetry(retry("newOrder", NON_IDEMPOTENT_CALLS_RETRY_CONFIG_NAME))
        .withRateLimiter(rateLimiter(ORDERS_PER_SECOND_RATE_LIMITER))
        .withRateLimiter(rateLimiter(ORDERS_PER_DAY_RATE_LIMITER))
        .withRateLimiter(rateLimiter(REQUEST_WEIGHT_RATE_LIMITER))
        .call();
  }

  /**
   *测试新订单
   * @param pair 货币对
   * @param side 订单方
   * @param type 订单类型
   * @param timeInForce 生效时间
   * @param quantity  数量
   * @param quoteOrderQty 订单数量报价
   * @param price 价格
   * @param newClientOrderId 新客户订单id
   * @param stopPrice 停止价格
   * @param trailingDelta 尾随/后续 增量
   * @param icebergQty 冰山数量
   * @throws IOException
   * @throws BinanceException
   */
  public void testNewOrder(
      CurrencyPair pair,
      OrderSide side,
      OrderType type,
      TimeInForce timeInForce,
      BigDecimal quantity,
      BigDecimal quoteOrderQty,
      BigDecimal price,
      String newClientOrderId,
      BigDecimal stopPrice,
      Long trailingDelta,
      BigDecimal icebergQty)
      throws IOException, BinanceException {
    decorateApiCall(
            () ->
                binance.testNewOrder(
                    BinanceAdapters.toSymbol(pair),
                    side,
                    type,
                    timeInForce,
                    quantity,
                    quoteOrderQty,
                    price,
                    newClientOrderId,
                    stopPrice,
                    trailingDelta,
                    icebergQty,
                    getRecvWindow(),
                    getTimestampFactory(),
                    apiKey,
                    signatureCreator))
        .withRetry(retry("testNewOrder"))
        .withRateLimiter(rateLimiter(REQUEST_WEIGHT_RATE_LIMITER))
        .call();
  }

  /**
   * 订单状态
   * @param pair 货币对
   * @param orderId 订单id
   * @param origClientOrderId 原始客户订单id
   * @return
   * @throws IOException
   * @throws BinanceException
   */
  public BinanceOrder orderStatus(CurrencyPair pair, Long orderId, String origClientOrderId)
      throws IOException, BinanceException {
    return decorateApiCall(
            () ->
                binance.orderStatus(
                    BinanceAdapters.toSymbol(pair),
                    orderId,
                    origClientOrderId,
                    getRecvWindow(),
                    getTimestampFactory(),
                    super.apiKey,
                    super.signatureCreator))
        .withRetry(retry("orderStatus"))
        .withRateLimiter(rateLimiter(REQUEST_WEIGHT_RATE_LIMITER))
        .call();
  }

  /**
   * 取消订单
   * @param pair 货币对
   * @param orderId 订单id
   * @param origClientOrderId 原始客户订单id
   * @param newClientOrderId 新客户订单id
   * @return
   * @throws IOException
   * @throws BinanceException
   */
  public BinanceCancelledOrder cancelOrder(
      CurrencyPair pair, Long orderId, String origClientOrderId, String newClientOrderId)
      throws IOException, BinanceException {
    return decorateApiCall(
            () ->
                binance.cancelOrder(
                    BinanceAdapters.toSymbol(pair),
                    orderId,
                    origClientOrderId,
                    newClientOrderId,
                    getRecvWindow(),
                    getTimestampFactory(),
                    super.apiKey,
                    super.signatureCreator))
        .withRetry(retry("cancelOrder"))
        .withRateLimiter(rateLimiter(REQUEST_WEIGHT_RATE_LIMITER))
        .call();
  }

  /**
   * 取消所有打开订单
   * @param pair 货币对
   * @return
   * @throws IOException
   * @throws BinanceException
   */
  public List<BinanceCancelledOrder> cancelAllOpenOrders(CurrencyPair pair)
      throws IOException, BinanceException {
    return decorateApiCall(
            () ->
                binance.cancelAllOpenOrders(
                    BinanceAdapters.toSymbol(pair),
                    getRecvWindow(),
                    getTimestampFactory(),
                    super.apiKey,
                    super.signatureCreator))
        .withRetry(retry("cancelAllOpenOrders"))
        .withRateLimiter(rateLimiter(REQUEST_WEIGHT_RATE_LIMITER))
        .call();
  }

  /**
   * 所有订单
   * @param pair 货币对
   * @param orderId 订单id
   * @param limit 限制
   * @return
   * @throws BinanceException
   * @throws IOException
   */
  public List<BinanceOrder> allOrders(CurrencyPair pair, Long orderId, Integer limit)
      throws BinanceException, IOException {
    return decorateApiCall(
            () ->
                binance.allOrders(
                    BinanceAdapters.toSymbol(pair),
                    orderId,
                    limit,
                    getRecvWindow(),
                    getTimestampFactory(),
                    apiKey,
                    signatureCreator))
        .withRetry(retry("allOrders"))
        .withRateLimiter(rateLimiter(REQUEST_WEIGHT_RATE_LIMITER))
        .call();
  }

  /**
   * 我的交易
   * @param pair 货币对
   * @param orderId 订单id
   * @param startTime 开始时间
   * @param endTime 结束时间
   * @param fromId 来自id
   * @param limit 限制/额度
   * @return
   * @throws BinanceException
   * @throws IOException
   */
  public List<BinanceTrade> myTrades(
      CurrencyPair pair, Long orderId, Long startTime, Long endTime, Long fromId, Integer limit)
      throws BinanceException, IOException {
    return decorateApiCall(
            () ->
                binance.myTrades(
                    BinanceAdapters.toSymbol(pair),
                    orderId,
                    startTime,
                    endTime,
                    fromId,
                    limit,
                    getRecvWindow(),
                    getTimestampFactory(),
                    apiKey,
                    signatureCreator))
        .withRetry(retry("myTrades"))
        .withRateLimiter(rateLimiter(REQUEST_WEIGHT_RATE_LIMITER), myTradesPermits(limit))
        .call();
  }

  /**
   *获取灰尘日志
   *
   * Retrieves the dust log from Binance. If you have many currencies with low amount (=dust) that
    cannot be traded, because their amount is less than the minimum amount required for trading
    them, you can convert all these currencies at once into BNB with the button "Convert Small Balance to BNB".
   从 Binance 检索灰尘日志。 如果您有许多数量较少（=灰尘）的货币，
   不能交易，因为他们的金额低于交易所需的最低金额
   他们，您可以使用“将小额余额转换为 BNB”按钮将所有这些货币一次转换为 BNB。
   *
   * @param startTime optional. If set, also the endTime must be set. If neither time is set, the  100 most recent dust logs are returned.
   *                  * @param startTime 可选。 如果设置，还必须设置 endTime。 如果两个时间都没有设置，则返回最近的 100 条灰尘日志。
   *
   * @param endTime optional. If set, also the startTime must be set. If neither time is set, the 100 most recent dust logs are returned.
   *                * @param endTime 可选。 如果设置，还必须设置 startTime。 如果两个时间都没有设置，则返回最近的 100 条灰尘日志。
   *
   * @return
   * @throws IOException
   */
  public BinanceDustLog getDustLog(Long startTime, Long endTime) throws IOException {

    if (((startTime != null) && (endTime == null)) || (startTime == null) && (endTime != null))
      throw new ExchangeException(
          "You need to specify both, the start and the end date, or none of them 您需要同时指定开始日期和结束日期，或者都不指定");

    return decorateApiCall(
            () ->
                binance.getDustLog(
                    startTime,
                    endTime,
                    getRecvWindow(),
                    getTimestampFactory(),
                    apiKey,
                    signatureCreator))
        .withRetry(retry("myDustLog"))
        .withRateLimiter(rateLimiter(REQUEST_WEIGHT_RATE_LIMITER))
        .call();
  }

  /**
   * 开始用户数据流
   * @return
   * @throws IOException
   */
  public BinanceListenKey startUserDataStream() throws IOException {
    return decorateApiCall(() -> binance.startUserDataStream(apiKey))
        .withRateLimiter(rateLimiter(REQUEST_WEIGHT_RATE_LIMITER))
        .call();
  }

  /**
   * 保持活动数据流
   * @param listenKey 监听键
   * @throws IOException
   */
  public void keepAliveDataStream(String listenKey) throws IOException {
    decorateApiCall(() -> binance.keepAliveUserDataStream(apiKey, listenKey))
        .withRateLimiter(rateLimiter(REQUEST_WEIGHT_RATE_LIMITER))
        .call();
  }

  /**
   * 关闭数据流
   * @param listenKey 监听键
   * @throws IOException
   */
  public void closeDataStream(String listenKey) throws IOException {
    decorateApiCall(() -> binance.closeUserDataStream(apiKey, listenKey))
        .withRateLimiter(rateLimiter(REQUEST_WEIGHT_RATE_LIMITER))
        .call();
  }

  /**
   * 打开订单许可
   * @param pair
   * @return
   */
  protected int openOrdersPermits(CurrencyPair pair) {
    return pair != null ? 1 : 40;
  }

  /**
   * 我的交易许可
   * @param limit
   * @return
   */
  protected int myTradesPermits(Integer limit) {
    if (limit != null && limit > 500) {
      return 10;
    }
    return 5;
  }
}
