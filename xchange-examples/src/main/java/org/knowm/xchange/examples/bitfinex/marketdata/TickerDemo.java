package org.knowm.xchange.examples.bitfinex.marketdata;

import java.io.IOException;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.bitfinex.BitfinexExchange;
import org.knowm.xchange.bitfinex.service.BitfinexMarketDataServiceRaw;
import org.knowm.xchange.bitfinex.v1.dto.marketdata.BitfinexTicker;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.service.marketdata.MarketDataService;

/** Demonstrate requesting Order Book at Bitfinex
 * 在 Bitfinex 展示请求订单簿*/
public class TickerDemo {

  public static void main(String[] args) throws Exception {

    // Use the factory to get Bitfinex exchange API using default settings
    // 使用出厂默认设置获取Bitfinex交易所API
    Exchange bitfinex = ExchangeFactory.INSTANCE.createExchange(BitfinexExchange.class);

    // Interested in the public market data feed (no authentication)
    // 对公开市场数据提要感兴趣（无需身份验证）
    MarketDataService marketDataService = bitfinex.getMarketDataService();

    generic(marketDataService);
    raw((BitfinexMarketDataServiceRaw) marketDataService);
  }

  private static void generic(MarketDataService marketDataService) throws IOException {

    // Get the latest ticker data showing BTC to USD
    // 获取显示 BTC 到 USD 的最新代码数据
    Ticker ticker = marketDataService.getTicker(CurrencyPair.BTC_USD);

    System.out.println(ticker.toString());
  }

  private static void raw(BitfinexMarketDataServiceRaw marketDataService) throws IOException {

    // Get the latest ticker data showing BTC to USD
    // 获取显示 BTC 到 USD 的最新代码数据
    BitfinexTicker ticker = marketDataService.getBitfinexTicker("btcusd");

    System.out.println(ticker.toString());
  }
}
