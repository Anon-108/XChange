package org.knowm.xchange.examples.bitfinex.marketdata;

import java.io.IOException;
import java.util.List;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.bitfinex.BitfinexExchange;
import org.knowm.xchange.bitfinex.service.BitfinexMarketDataServiceRaw;
import org.knowm.xchange.bitfinex.v2.dto.marketdata.BitfinexCandle;
import org.knowm.xchange.service.marketdata.MarketDataService;

/**
 * 蜡烛演示
 * @author cyrus13 */
public class CandlesDemo {

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

  private static void generic(Exchange bitfinex) {}

  private static void raw(BitfinexMarketDataServiceRaw marketDataService) throws IOException {
    List<BitfinexCandle> candleList =
//        marketDataService.getFundingHistoricCandles("15m", "fEUR", 2, 10);
        marketDataService.getFundingHistoricCandles("1m", "fUSD", 2, 3);
//    System.out.println(candleList);
    System.out.println("======================================================");
    candleList.forEach(item->{
      System.out.println("时间: "+item.getCandleDateTime());
      System.out.println(item);
    });
  }
}
