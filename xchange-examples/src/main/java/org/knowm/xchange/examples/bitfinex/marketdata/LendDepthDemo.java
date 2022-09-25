package org.knowm.xchange.examples.bitfinex.marketdata;

import java.io.IOException;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.bitfinex.BitfinexExchange;
import org.knowm.xchange.bitfinex.service.BitfinexMarketDataServiceRaw;
import org.knowm.xchange.bitfinex.v1.dto.marketdata.BitfinexLendDepth;
import org.knowm.xchange.service.marketdata.MarketDataService;

public class LendDepthDemo {

  public static void main(String[] args) throws Exception {

    // Use the factory to get BFX exchange API using default settings
    // 使用出厂设置获取 BFX 交易所 API 使用默认设置
    Exchange bfx = ExchangeFactory.INSTANCE.createExchange(BitfinexExchange.class);

    // Interested in the public market data feed (no authentication)
    // 对公开市场数据提要感兴趣（无需身份验证）
    MarketDataService marketDataService = bfx.getMarketDataService();

    raw((BitfinexMarketDataServiceRaw) marketDataService);
  }

  private static void raw(BitfinexMarketDataServiceRaw marketDataService) throws IOException {

    // Get the latest order book data for USD swaps
    // 获取美元掉期的最新订单簿数据
    BitfinexLendDepth bitfinexDepth = marketDataService.getBitfinexLendBook("USD", 50, 50);

    System.out.println(
        "Current Order Book size for USD 美元的当前订单簿大小: "
            + (bitfinexDepth.getAsks().length + bitfinexDepth.getBids().length));

    System.out.println("First Ask: " + bitfinexDepth.getAsks()[0].toString());

    System.out.println("First Bid: " + bitfinexDepth.getBids()[0].toString());

    System.out.println(bitfinexDepth.toString());
  }
}
