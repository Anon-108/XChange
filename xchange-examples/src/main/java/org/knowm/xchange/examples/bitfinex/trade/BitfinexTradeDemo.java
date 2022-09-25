package org.knowm.xchange.examples.bitfinex.trade;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.bitfinex.BitfinexExchange;
import org.knowm.xchange.bitfinex.service.BitfinexTradeServiceRaw;
import org.knowm.xchange.bitfinex.v1.dto.trade.BitfinexFundingTradeResponse;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.examples.bitfinex.BitfinexDemoUtils;
import org.knowm.xchange.service.marketdata.MarketDataService;

/**
 * Bitfinex 交易演示
 */
public class BitfinexTradeDemo {
  static {
    // TODO 请求代理接口  仅在本地测试开启
//    System.setProperty("proxyType", "4");
//    System.setProperty("proxyPort", Integer.toString(10809));
//    System.setProperty("proxyHost", "127.0.0.1");
//    System.setProperty("proxySet", "true");
  }
  public static void main(String[] args) throws IOException {

    Exchange bfx = ExchangeFactory.INSTANCE.createExchange(BitfinexExchange.class);
    MarketDataService marketDataService = bfx.getMarketDataService();
    Ticker ticker = marketDataService.getTicker(CurrencyPair.EOS_USD);
    System.out.println(ticker.toString());

//    Exchange bfx = BitfinexDemoUtils.createExchange();
//
//    raw(bfx);
  }

  private static void raw(Exchange bfx) throws IOException {

    /*
    BitfinexTradeServiceRaw tradeService = (BitfinexTradeServiceRaw) bfx.getTradeService();
    LimitOrder limitOrder = new LimitOrder.Builder(OrderType.BID, CurrencyPair.BTC_USD).limitPrice(new BigDecimal("481.69"))
        .originalAmount(new BigDecimal("0.001")).build();
    tradeService.placeBitfinexLimitOrder(limitOrder, BitfinexOrderType.LIMIT);
    */

    BitfinexTradeServiceRaw tradeService = (BitfinexTradeServiceRaw) bfx.getTradeService();

    Date tenDaysAgo =
        Date.from(LocalDate.now().minusDays(10).atStartOfDay(ZoneId.systemDefault()).toInstant());
    BitfinexFundingTradeResponse[] fundingTradeResponses =
        tradeService.getBitfinexFundingHistory("USD", tenDaysAgo, 2000);
  }
}
