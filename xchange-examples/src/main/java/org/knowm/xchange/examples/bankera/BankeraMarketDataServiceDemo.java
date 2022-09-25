package org.knowm.xchange.examples.bankera;

import java.io.IOException;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.service.marketdata.MarketDataService;

public class BankeraMarketDataServiceDemo {
  public static void main(String[] args) throws IOException {
    Exchange exchange = BankeraDemoUtils.createExchange();
    MarketDataService marketDataService = exchange.getMarketDataService();

    // Get the market data ticker
    // 获取市场数据代码
    Ticker ticker = marketDataService.getTicker(CurrencyPair.ETH_BTC);
    System.out.println("Ticker as String: " + ticker.toString());
  }
}
