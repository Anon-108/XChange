package org.knowm.xchange.binance.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.knowm.xchange.binance.BinanceAdapters;
import org.knowm.xchange.binance.BinanceAuthenticated;
import org.knowm.xchange.binance.BinanceErrorAdapter;
import org.knowm.xchange.binance.BinanceExchange;
import org.knowm.xchange.binance.dto.BinanceException;
import org.knowm.xchange.binance.dto.marketdata.BinanceAggTrades;
import org.knowm.xchange.binance.dto.marketdata.BinanceKline;
import org.knowm.xchange.binance.dto.marketdata.BinanceOrderbook;
import org.knowm.xchange.binance.dto.marketdata.BinancePriceQuantity;
import org.knowm.xchange.binance.dto.marketdata.BinanceTicker24h;
import org.knowm.xchange.binance.dto.marketdata.KlineInterval;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order.OrderType;
import org.knowm.xchange.dto.marketdata.CandleStickData;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.marketdata.Trade;
import org.knowm.xchange.dto.marketdata.Trades;
import org.knowm.xchange.dto.marketdata.Trades.TradeSortType;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.exceptions.NotYetImplementedForExchangeException;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.knowm.xchange.service.marketdata.params.Params;
import org.knowm.xchange.service.trade.params.CandleStickDataParams;
import org.knowm.xchange.service.trade.params.DefaultCandleStickParam;
import org.knowm.xchange.service.trade.params.DefaultCandleStickParamWithLimit;

/**
 * 币安市场数据服务
 */
public class BinanceMarketDataService extends BinanceMarketDataServiceRaw
    implements MarketDataService {
  /**
   * 币安市场数据服务
   * @param exchange
   * @param binance
   * @param resilienceRegistries 恢复注册
   */
  public BinanceMarketDataService(
      BinanceExchange exchange,
      BinanceAuthenticated binance,
      ResilienceRegistries resilienceRegistries) {
    super(exchange, binance, resilienceRegistries);
  }

  @Override
  public OrderBook getOrderBook(CurrencyPair pair, Object... args) throws IOException {
    try {
      int limitDepth = 100;

      if (args != null && args.length == 1) {
        Object arg0 = args[0];
        if (!(arg0 instanceof Integer)) {
          throw new ExchangeException("Argument 0 must be an Integer!");
        } else {
          limitDepth = (Integer) arg0;
        }
      }
      BinanceOrderbook binanceOrderbook = getBinanceOrderbook(pair, limitDepth);
      return convertOrderBook(binanceOrderbook, pair);
    } catch (BinanceException e) {
      throw BinanceErrorAdapter.adapt(e);
    }
  }

  /**
   * 转换订单簿
   * @param ob 订单薄
   * @param pair  对
   * @return
   */
  public static OrderBook convertOrderBook(BinanceOrderbook ob, CurrencyPair pair) {
    List<LimitOrder> bids =
        ob.bids.entrySet().stream()
            .map(e -> new LimitOrder(OrderType.BID, e.getValue(), pair, null, null, e.getKey()))
            .collect(Collectors.toList());
    List<LimitOrder> asks =
        ob.asks.entrySet().stream()
            .map(e -> new LimitOrder(OrderType.ASK, e.getValue(), pair, null, null, e.getKey()))
            .collect(Collectors.toList());
    return new OrderBook(null, asks, bids);
  }

  @Override
  public Ticker getTicker(CurrencyPair pair, Object... args) throws IOException {
    try {
      return ticker24h(pair).toTicker();
    } catch (BinanceException e) {
      throw BinanceErrorAdapter.adapt(e);
    }
  }

  @Override
  public List<Ticker> getTickers(Params params) throws IOException {
    try {
      return ticker24h().stream().map(BinanceTicker24h::toTicker).collect(Collectors.toList());
    } catch (BinanceException e) {
      throw BinanceErrorAdapter.adapt(e);
    }
  }

  /**
   * 获得交易
   *
   * optional parameters provided in the args array:
   * args 数组中提供的可选参数：
   *
   * <ul>
   *   <li>0: Long fromId optional, ID to get aggregate trades from INCLUSIVE.
   *   0：长 fromId 可选，ID 从 INCLUSIVE 获取聚合交易。
   *
   *   <li>1: Long startTime optional, Timestamp in ms to get aggregate trades from INCLUSIVE.
   *   1：Long startTime 可选，以毫秒为单位的时间戳，用于从 INCLUSIVE 获取聚合交易。
   *
   *   <li>2: Long endTime optional, Timestamp in ms to get aggregate trades until INCLUSIVE.
   *   2：Long endTime 可选，以毫秒为单位的时间戳以获取聚合交易，直到 INCLUSIVE。
   *
   *   <li>3: Integer limit optional, Default 500; max 500.
   *   3：整数限制可选，默认500； 最多 500 个。
   * </ul>
   * <p>
   */
  @Override
  public Trades getTrades(CurrencyPair pair, Object... args) throws IOException {
    try {
      Long fromId = tradesArgument(args, 0, Long::valueOf);
      Long startTime = tradesArgument(args, 1, Long::valueOf);
      Long endTime = tradesArgument(args, 2, Long::valueOf);
      Integer limit = tradesArgument(args, 3, Integer::valueOf);
      List<BinanceAggTrades> aggTrades =
          binance.aggTrades(BinanceAdapters.toSymbol(pair), fromId, startTime, endTime, limit);
      List<Trade> trades =
          aggTrades.stream()
              .map(
                  at ->
                      new Trade.Builder()
                          .type(BinanceAdapters.convertType(at.buyerMaker))
                          .originalAmount(at.quantity)
                          .currencyPair(pair)
                          .price(at.price)
                          .timestamp(at.getTimestamp())
                          .id(Long.toString(at.aggregateTradeId))
                          .build())
              .collect(Collectors.toList());
      return new Trades(trades, TradeSortType.SortByTimestamp);
    } catch (BinanceException e) {
      throw BinanceErrorAdapter.adapt(e);
    }
  }

  @Override
  public CandleStickData getCandleStickData(CurrencyPair currencyPair, CandleStickDataParams params)
      throws IOException {

    if (!(params instanceof DefaultCandleStickParam)) {
      throw new NotYetImplementedForExchangeException("Only DefaultCandleStickParam is supported");
    }
    try {
      DefaultCandleStickParam defaultCandleStickParam = (DefaultCandleStickParam) params;
      KlineInterval periodType =
          KlineInterval.getPeriodTypeFromSecs(defaultCandleStickParam.getPeriodInSecs());
      if (periodType == null) {
        throw new NotYetImplementedForExchangeException(
            "Only discrete period values are "
                + "supported;"
                + Arrays.toString(KlineInterval.values()));
      }
      int limit = 500;
      if (params instanceof DefaultCandleStickParamWithLimit) {
        if (((DefaultCandleStickParamWithLimit) params).getLimit() > 0) {
          limit = ((DefaultCandleStickParamWithLimit) params).getLimit();
        }
      }
      List<BinanceKline> klines = null;
      if (defaultCandleStickParam.getStartDate() == null
          || defaultCandleStickParam.getEndDate() == null) {
        klines = klines(currencyPair, periodType);
      } else {
        klines =
            klines(
                currencyPair,
                periodType,
                limit,
                defaultCandleStickParam.getStartDate().getTime(),
                defaultCandleStickParam.getEndDate().getTime());
      }

      return BinanceAdapters.adaptBinanceCandleStickData(klines, currencyPair);
    } catch (BinanceException e) {
      throw BinanceErrorAdapter.adapt(e);
    }
  }

  /**
   * 交易的论点
   * @param args  参数
   * @param index 下标
   * @param converter  转换
   * @param <T>
   * @return
   */
  private <T extends Number> T tradesArgument(
      Object[] args, int index, Function<String, T> converter) {
    if (index >= args.length) {
      return null;
    }
    Object arg = args[index];
    if (arg == null) {
      return null;
    }
    String argStr = arg.toString();
    try {
      return converter.apply(argStr);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException(
          "Argument on index " + index + " is not a number: " + argStr, e);
    }
  }

  /**
   * 获取所有的帐薄报价
   * @return
   * @throws IOException
   */
  public List<Ticker> getAllBookTickers() throws IOException {
    List<BinancePriceQuantity> binanceTickers = tickerAllBookTickers();
    return BinanceAdapters.adaptPriceQuantities(binanceTickers);
  }
}
