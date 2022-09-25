package org.knowm.xchange.examples.bitfinex.marketdata;

import java.io.IOException;
import java.util.Collection;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.bitfinex.BitfinexExchange;
import org.knowm.xchange.bitfinex.service.BitfinexMarketDataServiceRaw;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.service.marketdata.MarketDataService;

public class SymbolsDemo {

  public static void main(String[] args) throws Exception {

    // Use the factory to get Bitfinex exchange API using default settings
    // 使用出厂默认设置获取Bitfinex交易所API
    Exchange bitfinex = ExchangeFactory.INSTANCE.createExchange(BitfinexExchange.class);

    // Interested in the public market data feed (no authentication)
    // 对公开市场数据提要感兴趣（无需身份验证）
    MarketDataService marketDataService = bitfinex.getMarketDataService();

    generic(bitfinex);
    raw((BitfinexMarketDataServiceRaw) marketDataService);
  }

  private static void generic(Exchange bitfinex) {

    System.out.println(bitfinex.getExchangeSymbols().toString());
  }

  private static void raw(BitfinexMarketDataServiceRaw marketDataService) throws IOException {

    Collection<String> symbols = marketDataService.getBitfinexSymbols();
    System.out.println(symbols);

    Collection<CurrencyPair> currencyPairs = marketDataService.getExchangeSymbols();
    System.out.println(currencyPairs);
  }
}
