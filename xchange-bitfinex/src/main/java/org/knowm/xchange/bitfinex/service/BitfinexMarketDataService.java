package org.knowm.xchange.bitfinex.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.knowm.xchange.bitfinex.BitfinexErrorAdapter;
import org.knowm.xchange.bitfinex.BitfinexExchange;
import org.knowm.xchange.bitfinex.dto.BitfinexException;
import org.knowm.xchange.bitfinex.v1.BitfinexUtils;
import org.knowm.xchange.bitfinex.v1.dto.marketdata.BitfinexDepth;
import org.knowm.xchange.bitfinex.v1.dto.marketdata.BitfinexLendDepth;
import org.knowm.xchange.bitfinex.v1.dto.marketdata.BitfinexTrade;
import org.knowm.xchange.bitfinex.v2.dto.marketdata.BitfinexTicker;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.LoanOrderBook;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.marketdata.Trades;
import org.knowm.xchange.dto.trade.FixedRateLoanOrder;
import org.knowm.xchange.dto.trade.FloatingRateLoanOrder;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.knowm.xchange.service.marketdata.params.CurrencyPairsParam;
import org.knowm.xchange.service.marketdata.params.Params;

/**
 * Bitfinex市场数据服务
 *
 * Implementation of the market data service for Bitfinex
 * 为 Bitfinex 实施市场数据服务
 *
 * <ul>
 *   <li>Provides access to various market data values
 *      提供对各种市场数据值的访问
 * </ul>
 */
public class BitfinexMarketDataService extends BitfinexMarketDataServiceRaw
    implements MarketDataService {

  /**
   * Constructor
   * Bitfinex市场数据服务
   *
   * @param exchange
   */
  public BitfinexMarketDataService(
      BitfinexExchange exchange, ResilienceRegistries resilienceRegistries) {
    super(exchange, resilienceRegistries);
  }

  @Override
  public Ticker getTicker(CurrencyPair currencyPair, Object... args) throws IOException {
    // return getTickerV1(currencyPair, args);
    // return getTickerV1(currencyPair, args);
    return getTickerV2(currencyPair, args);
  }

  private Ticker getTickerV1(CurrencyPair currencyPair, Object... args) throws IOException {
    try {
      return BitfinexAdapters.adaptTicker(
          getBitfinexTicker(BitfinexUtils.toPairStringV1(currencyPair)), currencyPair);
    } catch (BitfinexException e) {
      throw BitfinexErrorAdapter.adapt(e);
    }
  }

  /**
   * 获取TickerV2
   * @param currencyPair 货币对
   * @param args  参数...
   * @return
   * @throws IOException
   */
  private Ticker getTickerV2(CurrencyPair currencyPair, Object... args) throws IOException {
    try {
      return BitfinexAdapters.adaptTicker(getBitfinexTickerV2(currencyPair));
    } catch (BitfinexException e) {
      throw BitfinexErrorAdapter.adapt(e);
    }
  }

  /** @param args If two integers are provided, then those count as limit bid and limit ask count
   * 如果提供了两个整数，则将它们计为限制出价和限制要价计数*/
  @Override
  public OrderBook getOrderBook(CurrencyPair currencyPair, Object... args) throws IOException {
    try {
      // null will cause fetching of full order book, the default behavior in XChange
      // null 将导致获取完整的订单簿，XChange 中的默认行为
      Integer limitBids = null;
      Integer limitAsks = null;

      if (args != null && args.length == 2) {
        Object arg0 = args[0];
        if (!(arg0 instanceof Integer)) {
          throw new ExchangeException("Argument 0 must be an Integer! 参数 0 必须是整数！");
        } else {
          limitBids = (Integer) arg0;
        }
        Object arg1 = args[1];
        if (!(arg1 instanceof Integer)) {
          throw new ExchangeException("Argument 1 must be an Integer! 参数 1 必须是整数！");
        } else {
          limitAsks = (Integer) arg1;
        }
      }

      BitfinexDepth bitfinexDepth =
          getBitfinexOrderBook(BitfinexUtils.toPairStringV1(currencyPair), limitBids, limitAsks);

      OrderBook orderBook = BitfinexAdapters.adaptOrderBook(bitfinexDepth, currencyPair);

      return orderBook;
    } catch (BitfinexException e) {
      throw BitfinexErrorAdapter.adapt(e);
    }
  }

  public LoanOrderBook getLendOrderBook(String currency, Object... args) throws IOException {
    try {
      // According to API docs, default is 50
      // 根据 API 文档，默认为 50
      int limitBids = 50;
      int limitAsks = 50;

      if (args != null && args.length == 2) {
        Object arg0 = args[0];
        if (!(arg0 instanceof Integer)) {
          throw new ExchangeException("Argument 0 must be an Integer! 参数 0 必须是整数！");
        } else {
          limitBids = (Integer) arg0;
        }
        Object arg1 = args[1];
        if (!(arg1 instanceof Integer)) {
          throw new ExchangeException("Argument 1 must be an Integer! 参数 1 必须是整数！");
        } else {
          limitAsks = (Integer) arg1;
        }
      }

      BitfinexLendDepth bitfinexLendDepth = getBitfinexLendBook(currency, limitBids, limitAsks);

      List<FixedRateLoanOrder> fixedRateAsks =
          BitfinexAdapters.adaptFixedRateLoanOrders(
              bitfinexLendDepth.getAsks(), currency, "ask", "");
      List<FixedRateLoanOrder> fixedRateBids =
          BitfinexAdapters.adaptFixedRateLoanOrders(
              bitfinexLendDepth.getBids(), currency, "bid", "");
      List<FloatingRateLoanOrder> floatingRateAsks =
          BitfinexAdapters.adaptFloatingRateLoanOrders(
              bitfinexLendDepth.getAsks(), currency, "ask", "");
      List<FloatingRateLoanOrder> floatingRateBids =
          BitfinexAdapters.adaptFloatingRateLoanOrders(
              bitfinexLendDepth.getBids(), currency, "bid", "");

      return new LoanOrderBook(
          null, fixedRateAsks, fixedRateBids, floatingRateAsks, floatingRateBids);
    } catch (BitfinexException e) {
      throw BitfinexErrorAdapter.adapt(e);
    }
  }

  /**
   * 获取交易
   * @param currencyPair
   * @param args Optional arguments. Exchange-specific
   *             * @param args 可选参数。 特定于交易所
   *
   * @return
   * @throws IOException
   */
  @Override
  public Trades getTrades(CurrencyPair currencyPair, Object... args) throws IOException {
    // return getTradesV1(currencyPair, args);
    return getTradesV2(currencyPair, args);
  }

  /**
   * @param currencyPair The CurrencyPair for which to query trades.  要查询交易的 CurrencyPair。
   * @param args One argument may be supplied which is the timestamp after which trades should be  collected. Trades before this time are not reported. The argument may be of type java.util.Date or Number (milliseconds since Jan 1, 1970)
   *             可以提供一个参数，即应收集交易的时间戳。 在此之前的交易不会被报告。 参数可以是 java.util.Date 或 Number 类型（自 1970 年 1 月 1 日以来的毫秒数）
   */
  private Trades getTradesV1(CurrencyPair currencyPair, Object... args) throws IOException {
    try {
      long lastTradeTime = 0;
      if (args != null && args.length == 1) {
        // parameter 1, if present, is the last trade timestamp
        // 参数 1，如果存在，是最后的交易时间戳
        if (args[0] instanceof Number) {
          Number arg = (Number) args[0];
          lastTradeTime =
              arg.longValue() / 1000; // divide by 1000 to convert to unix timestamp (seconds)  // 除以 1000 转换为 unix 时间戳（秒）
        } else if (args[0] instanceof Date) {
          Date arg = (Date) args[0];
          lastTradeTime =
              arg.getTime() / 1000; // divide by 1000 to convert to unix timestamp (seconds)  // 除以 1000 转换为 unix 时间戳（秒）
        } else {
          throw new IllegalArgumentException(
              "Extra argument #1, the last trade time, must be a Date or Long (millisecond timestamp) (was  额外参数 #1，最后一次交易时间，必须是 Date 或 Long（毫秒时间戳）（原为"
                  + args[0].getClass()
                  + ")");
        }
      }
      BitfinexTrade[] trades =
          getBitfinexTrades(BitfinexUtils.toPairStringV1(currencyPair), lastTradeTime);

      return BitfinexAdapters.adaptTrades(trades, currencyPair);
    } catch (BitfinexException e) {
      throw BitfinexErrorAdapter.adapt(e);
    }
  }

  /**
   * 获取交易V2
   * @param currencyPair The Currenpayload CreatorcyPair for which to query public trades.
   *                      要查询公共交易的 CurrencyPair。
   * @param args Upto 4 numeric arguments may be supplied limitTrades, startTimestamp (Unix  millisecs), endTimestamp (Unix millisecs), sort -1 / 1 (if = 1 it sorts results returned    with old > new)
   *             最多可以提供 4 个数字参数 limitTrades、startTimestamp（Unix 毫秒）、endTimestamp（Unix 毫秒）、sort -1 / 1（如果 = 1，它会按 old > new 返回的结果排序）
   */
  private Trades getTradesV2(CurrencyPair currencyPair, Object... args) throws IOException {
    try {
      int limitTrades = 1000;
      long startTimestamp = 0;
      long endTimestamp = 0;
      int sort = -1;

      if (args != null) {
        for (int i = 0; i < args.length; i++) {
          if (args[i] instanceof Number) {
            Number arg = (Number) args[i];
            switch (i) {
              case 0:
                limitTrades = arg.intValue();
                break;
              case 1:
                startTimestamp = arg.longValue();
                break;
              case 2:
                endTimestamp = arg.longValue();
                break;
              case 3:
                sort = arg.intValue();
                break;
            }
          } else {
            throw new IllegalArgumentException(
                "Extra argument 额外的论点#" + i + " must be an int/long was 必须是 int/long 是: " + args[i].getClass());
          }
        }
      }

      return BitfinexAdapters.adaptPublicTrades(
          getBitfinexPublicTrades(currencyPair, limitTrades, startTimestamp, endTimestamp, sort),
          currencyPair);
    } catch (BitfinexException e) {
      throw BitfinexErrorAdapter.adapt(e);
    }
  }

  /**
   * 获取代码
   * @param params
   * @return
   * @throws IOException
   */
  @Override
  public List<Ticker> getTickers(Params params) throws IOException {
    try {

      BitfinexTicker[] bitfinexTickers =
          params instanceof CurrencyPairsParam
              ? getBitfinexTickers(((CurrencyPairsParam) params).getCurrencyPairs())
              : getBitfinexTickers(null);

      return Arrays.stream(bitfinexTickers)
          .map(BitfinexAdapters::adaptTicker)
          .collect(Collectors.toList());
    } catch (BitfinexException e) {
      throw BitfinexErrorAdapter.adapt(e);
    }
  }
}
