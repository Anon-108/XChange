package org.knowm.xchange.binance.service.trade;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.junit.Assert.assertEquals;
import static org.knowm.xchange.binance.dto.trade.OrderType.LIMIT;
import static org.knowm.xchange.binance.dto.trade.OrderType.MARKET;
import static org.knowm.xchange.binance.dto.trade.OrderType.STOP_LOSS_LIMIT;
import static org.knowm.xchange.binance.dto.trade.OrderType.TAKE_PROFIT_LIMIT;
import static org.knowm.xchange.dto.Order.OrderType.BID;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.knowm.xchange.binance.BinanceExchange;
import org.knowm.xchange.binance.BinanceExchangeIntegration;
import org.knowm.xchange.binance.dto.trade.BinanceDustLog;
import org.knowm.xchange.binance.dto.trade.TimeInForce;
import org.knowm.xchange.binance.service.BinanceTradeService;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.MarketOrder;
import org.knowm.xchange.dto.trade.StopOrder;

/**
 * 贸易服务集成
 */
public class TradeServiceIntegration extends BinanceExchangeIntegration {

  static BinanceTradeService tradeService;

  @BeforeClass
  public static void beforeClass() throws Exception {
    createExchange();
    tradeService = (BinanceTradeService) exchange.getTradeService();
  }

  @Before
  public void before() {
    Assume.assumeNotNull(exchange.getExchangeSpecification().getApiKey());
  }

  /**
   * test Place 测试订单 限价订单不应该抛出任何异常
   * @throws IOException
   */
  @Test
  public void testPlaceTestOrderLimitOrderShouldNotThrowAnyException() throws IOException {
    final LimitOrder limitOrder = sampleLimitOrder();

    tradeService.placeTestOrder(LIMIT, limitOrder, limitOrder.getLimitPrice(), null);
  }

  /**
   * 限价单样本
   * @return
   * @throws IOException
   */
  private LimitOrder sampleLimitOrder() throws IOException {
    final CurrencyPair currencyPair = CurrencyPair.BTC_USDT;
    final BigDecimal amount = BigDecimal.ONE;
    final BigDecimal limitPrice = limitPriceForCurrencyPair(currencyPair);
    return new LimitOrder.Builder(BID, currencyPair)
        .originalAmount(amount)
        .limitPrice(limitPrice)
        .flag(TimeInForce.GTC)
        .build();
  }

  /**
   * 货币对限价
   * @param currencyPair
   * @return
   * @throws IOException
   */
  private BigDecimal limitPriceForCurrencyPair(CurrencyPair currencyPair) throws IOException {
    return exchange
        .getMarketDataService()
        .getOrderBook(currencyPair)
        .getAsks()
        .get(0)
        .getLimitPrice();
  }

  /**
   * test Place 测试订单 市价单不应该抛出任何异常
   * @throws IOException
   */
  @Test
  public void testPlaceTestOrderMarketOrderShouldNotThrowAnyException() throws IOException {
    final MarketOrder marketOrder = sampleMarketOrder();

    tradeService.placeTestOrder(MARKET, marketOrder, null, null);
  }

  /**
   * 市价单样本
   * @return
   */
  private MarketOrder sampleMarketOrder() {
    final CurrencyPair currencyPair = CurrencyPair.BTC_USDT;
    final BigDecimal amount = BigDecimal.ONE;
    return new MarketOrder.Builder(BID, currencyPair).originalAmount(amount).build();
  }

  /**
   * 测试地点 测试订单 止损限价单不应抛出任何异常
   * @throws IOException
   */
  @Test
  public void testPlaceTestOrderStopLossLimitOrderShouldNotThrowAnyException() throws IOException {
    final StopOrder stopLimitOrder = sampleStopLimitOrder();

    tradeService.placeTestOrder(
        STOP_LOSS_LIMIT,
        stopLimitOrder,
        stopLimitOrder.getLimitPrice(),
        stopLimitOrder.getStopPrice());
  }

  /**
   * 样本止损限价单
   * @return
   * @throws IOException
   */
  private StopOrder sampleStopLimitOrder() throws IOException {
    final CurrencyPair currencyPair = CurrencyPair.BTC_USDT;
    final BigDecimal amount = BigDecimal.ONE;
    final BigDecimal limitPrice = limitPriceForCurrencyPair(currencyPair);
    final BigDecimal stopPrice =
        limitPrice.multiply(new BigDecimal("0.9")).setScale(2, RoundingMode.HALF_UP);
    return new StopOrder.Builder(BID, currencyPair)
        .originalAmount(amount)
        .limitPrice(limitPrice)
        .stopPrice(stopPrice)
        .intention(StopOrder.Intention.STOP_LOSS)
        .flag(TimeInForce.GTC)
        .build();
  }

  /**
   * 测试地点 测试订单 止盈限价订单不应抛出任何异常
   * @throws IOException
   */
  @Test
  public void testPlaceTestOrderTakeProfitLimitOrderShouldNotThrowAnyException()
      throws IOException {
    final StopOrder takeProfitLimitOrder = sampleTakeProfitLimitOrder();

    tradeService.placeTestOrder(
        TAKE_PROFIT_LIMIT,
        takeProfitLimitOrder,
        takeProfitLimitOrder.getLimitPrice(),
        takeProfitLimitOrder.getStopPrice());
  }

  /**
   * 样本止盈限价单
   * @return
   * @throws IOException
   */
  private StopOrder sampleTakeProfitLimitOrder() throws IOException {
    final CurrencyPair currencyPair = CurrencyPair.BTC_USDT;
    final BigDecimal amount = BigDecimal.ONE;
    final BigDecimal limitPrice = limitPriceForCurrencyPair(currencyPair);
    final BigDecimal takeProfitPrice =
        limitPrice.multiply(new BigDecimal("1.1")).setScale(2, RoundingMode.HALF_UP);
    return new StopOrder.Builder(BID, currencyPair)
        .originalAmount(amount)
        .stopPrice(takeProfitPrice)
        .limitPrice(limitPrice)
        .intention(StopOrder.Intention.TAKE_PROFIT)
        .flag(TimeInForce.GTC)
        .build();
  }

  /**
   * 测试灰尘日志
   * @throws IOException
   */
  @Test
  public void testDustLog() throws IOException {
    BinanceExchange exchangeMocked = createExchangeMocked();
    tradeService = (BinanceTradeService) exchangeMocked.getTradeService();
    stubFor(
        get(urlPathEqualTo("/sapi/v1/asset/dribblet"))
            .willReturn(
                ok().withHeader("Content-Type", "application/json").withBodyFile("dustlog.json")));

    BinanceDustLog dustLog = tradeService.getDustLog(1639094400000L, 1639180800000L);
    assertEquals(1, dustLog.getDribblets().size());
    assertEquals(28, dustLog.getDribblets().get(0).getBinanceDribbletDetails().size());
    assertEquals((Long) 1639129045000L, dustLog.getDribblets().get(0).getOperateTime());
    assertEquals(
        "90698471826",
        dustLog.getDribblets().get(0).getBinanceDribbletDetails().get(0).getTransId());
  }
}
