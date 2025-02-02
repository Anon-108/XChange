package org.knowm.xchange.examples.bitfinex.marketdata;

import java.io.IOException;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.bitfinex.BitfinexExchange;
import org.knowm.xchange.bitfinex.service.BitfinexMarketDataServiceRaw;
import org.knowm.xchange.bitfinex.v1.dto.marketdata.BitfinexDepth;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.service.marketdata.MarketDataService;

/** Demonstrate requesting Order Book at Bitfinex
 * 在 Bitfinex 展示请求订单簿 */
public class BitfinexDepthDemo {

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

    // Get the latest order book data for CurrencyPair.BTC_USD
    // 获取货币对的最新订单簿数据。BTC_USD
    OrderBook orderBook = marketDataService.getOrderBook(CurrencyPair.BTC_USD, 10000, 10000);

    System.out.println(
        "Current Order Book size for BTC / USD:BTC / USD的当前订单簿大小： "
            + (orderBook.getAsks().size() + orderBook.getBids().size()));

    System.out.println("First Ask: " + orderBook.getAsks().get(0).toString());

    System.out.println("First Bid: " + orderBook.getBids().get(0).toString());

    System.out.println(orderBook.toString());
  }

  private static void raw(BitfinexMarketDataServiceRaw marketDataService) throws IOException {

    // Get the latest order book data for BTC/CAD
    // 获取最新的 BTC/CAD 订单簿数据
    BitfinexDepth bitfinexDepth = marketDataService.getBitfinexOrderBook("btcusd", 50, 50);

    System.out.println(
        "Current Order Book size for BTC / USD BTC / USD的当前订单簿大小: "
            + (bitfinexDepth.getAsks().length + bitfinexDepth.getBids().length));

    System.out.println("First Ask: " + bitfinexDepth.getAsks()[0].toString());

    System.out.println("First Bid: " + bitfinexDepth.getBids()[0].toString());

    System.out.println(bitfinexDepth.toString());
  }
}
