package org.knowm.xchange.examples.bitfinex.marketdata;

import java.io.IOException;
import java.util.Arrays;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.bitfinex.BitfinexExchange;
import org.knowm.xchange.bitfinex.service.BitfinexMarketDataServiceRaw;
import org.knowm.xchange.bitfinex.v1.dto.marketdata.BitfinexTrade;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Trades;
import org.knowm.xchange.service.marketdata.MarketDataService;

/** Demonstrate requesting Order Book at BTC-E */
public class TradesDemo {

  public static void main(String[] args) throws Exception {

    // Use the factory to get BTC-E exchange API using default settings
    // 使用出厂默认设置获取BTC-E兑换API
    Exchange bitfinex = ExchangeFactory.INSTANCE.createExchange(BitfinexExchange.class);

    // Interested in the public market data feed (no authentication)
    // 对公开市场数据提要感兴趣（无需身份验证）
    MarketDataService marketDataService = bitfinex.getMarketDataService();

    generic(marketDataService);
    raw((BitfinexMarketDataServiceRaw) marketDataService);
  }

  private static void generic(MarketDataService marketDataService) throws IOException {

    // Get the latest trade data for BTC/USD
    // 获取 BTC/USD 的最新交易数据
    Trades trades = marketDataService.getTrades(CurrencyPair.BTC_USD);
    System.out.println("Trades, Size= " + trades.getTrades().size());
    System.out.println(trades.toString());
  }

  private static void raw(BitfinexMarketDataServiceRaw marketDataService) throws IOException {

    // Get the latest trade data for BTC/USD
    // 获取 BTC/USD 的最新交易数据
    BitfinexTrade[] trades =
        marketDataService.getBitfinexTrades("btcusd", System.currentTimeMillis() / 1000 - 120);
    System.out.println("Trades, default. Size 交易，默认。 大小= " + trades.length);
    System.out.println(Arrays.toString(trades));
  }
}
