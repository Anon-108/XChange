package org.knowm.xchange.service.marketdata;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.CandleStickData;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.marketdata.Trades;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.exceptions.NotAvailableFromExchangeException;
import org.knowm.xchange.exceptions.NotYetImplementedForExchangeException;
import org.knowm.xchange.instrument.Instrument;
import org.knowm.xchange.service.BaseService;
import org.knowm.xchange.service.marketdata.params.Params;
import org.knowm.xchange.service.trade.params.CandleStickDataParams;

/**
 * Interface to provide the following to {@link Exchange}:
 * 向 {@link Exchange} 提供以下内容的接口：
 *
 * <ul>
 *   <li>Standard methods available to explore the market data
 *   * <li>可用于探索市场数据的标准方法
 * </ul>
 *
 * <p>The implementation of this service is expected to be based on a client polling mechanism of some kind
 * * <p>该服务的实现预计将基于某种客户端轮询机制
 */
public interface MarketDataService extends BaseService {

  /**
   * Get a ticker representing the current exchange rate
   * * 获取代表当前汇率的股票代码
   *
   * @return The Ticker, null if some sort of error occurred. Implementers should log the error.
   * * @return 代码，如果发生某种错误，则返回 null。 实施者应该记录错误。
   *
   * @throws ExchangeException - Indication that the exchange reported some kind of error with the  request or response
   * * @throws ExchangeException - 表示交换报告了请求或响应的某种错误
   *
   * @throws NotAvailableFromExchangeException - Indication that the exchange does not support the   requested function or data
   * * @throws NotAvailableFromExchangeException - 表示交易所不支持请求的功能或数据
   *
   * @throws NotYetImplementedForExchangeException - Indication that the exchange supports the  requested function or data, but it has not yet been implemented
   * * @throws NotYetImplementedForExchangeException - 表示交易所支持请求的功能或数据，但尚未实现
   *
   * @throws IOException - Indication that a networking error occurred while fetching JSON data
   * * @throws IOException - 指示在获取 JSON 数据时发生网络错误
   */
  @Deprecated
  default Ticker getTicker(CurrencyPair currencyPair, Object... args) throws IOException {
    throw new NotYetImplementedForExchangeException("getTicker");
  }

  /**
   * Get a ticker representing the current exchange rate
   * * 获取代表当前汇率的股票代码
   *
   * @return The Ticker, null if some sort of error occurred. Implementers should log the error.
   * * @return 代码，如果发生某种错误，则返回 null。 实施者应该记录错误。
   *
   * @throws ExchangeException - Indication that the exchange reported some kind of error with the  request or response
   * * @throws ExchangeException - 表示交换报告了请求或响应的某种错误
   *
   * @throws NotAvailableFromExchangeException - Indication that the exchange does not support the  requested function or data
   * * @throws NotAvailableFromExchangeException - 表示交易所不支持请求的功能或数据
   *
   * @throws NotYetImplementedForExchangeException - Indication that the exchange supports the   requested function or data, but it has not yet been implemented
   * * @throws NotYetImplementedForExchangeException - 表示交易所支持请求的功能或数据，但尚未实现
   *
   * @throws IOException - Indication that a networking error occurred while fetching JSON data
   * * @throws IOException - 指示在获取 JSON 数据时发生网络错误
   */
  default Ticker getTicker(Instrument instrument, Object... args) throws IOException {
    throw new NotYetImplementedForExchangeException("getTicker");
  }

  /**
   * Get the tickers representing the current exchange rate for the provided parameters
   ** 为提供的参数获取代表当前汇率的代码
   *
   * @return The Tickers, null if some sort of error occurred. Implementers should log the error.
   * * @return The Tickers，如果发生某种错误，则返回 null。 实施者应该记录错误。
   *
   * @throws ExchangeException - Indication that the exchange reported some kind of error with the  request or response
   * * @throws ExchangeException - 表示交换报告了请求或响应的某种错误
   *
   * @throws NotAvailableFromExchangeException - Indication that the exchange does not support the   requested function or data
   * * @throws NotAvailableFromExchangeException - 表示交易所不支持请求的功能或数据
   *
   * @throws NotYetImplementedForExchangeException - Indication that the exchange supports the  requested function or data, but it has not yet been implemented
   * * @throws NotYetImplementedForExchangeException - 表示交易所支持请求的功能或数据，但尚未实现
   *
   * @throws IOException - Indication that a networking error occurred while fetching JSON data
   * * @throws IOException - 指示在获取 JSON 数据时发生网络错误
   */
  default List<Ticker> getTickers(Params params) throws IOException {
    throw new NotYetImplementedForExchangeException("getTickers");
  }

  /**
   * Get an order book representing the current offered exchange rates (market depth)
   * * 获取代表当前提供的汇率（市场深度）的订单簿
   *
   * @param args Optional arguments. Exchange-specific
   *             * @param args 可选参数。 特定于交易所
   *
   * @return The OrderBook, null if some sort of error occurred. Implementers should log the error.
   * * @return 订单簿，如果发生某种错误，则返回 null。 实施者应该记录错误。
   *
   * @throws ExchangeException - Indication that the exchange reported some kind of error with the  request or response
   * * @throws ExchangeException - 表示交换报告了请求或响应的某种错误
   *
   * @throws NotAvailableFromExchangeException - Indication that the exchange does not support the  requested function or data
   * * @throws NotAvailableFromExchangeException - 表示交易所不支持请求的功能或数据
   *
   * @throws NotYetImplementedForExchangeException - Indication that the exchange supports the  requested function or data, but it has not yet been implemented
   * * @throws NotYetImplementedForExchangeException - 表示交易所支持请求的功能或数据，但尚未实现
   *
   * @throws IOException - Indication that a networking error occurred while fetching JSON data
   * * @throws IOException - 指示在获取 JSON 数据时发生网络错误
   */
  @Deprecated
  default OrderBook getOrderBook(CurrencyPair currencyPair, Object... args) throws IOException {
    throw new NotYetImplementedForExchangeException("getOrderBook");
  }

  /**
   * Get an order book representing the current offered exchange rates (market depth)
   * * 获取代表当前提供的汇率（市场深度）的订单簿
   *
   * @param args Optional arguments. Exchange-specific
   *             * @param args 可选参数。 特定于交易所
   *
   * @return The OrderBook, null if some sort of error occurred. Implementers should log the error.
   * * @return 订单簿，如果发生某种错误，则返回 null。 实施者应该记录错误。
   *
   * @throws ExchangeException - Indication that the exchange reported some kind of error with the   request or response
   * * @throws ExchangeException - 表示交换报告了请求或响应的某种错误
   *
   * @throws NotAvailableFromExchangeException - Indication that the exchange does not support the  requested function or data
   * * @throws NotAvailableFromExchangeException - 表示交易所不支持请求的功能或数据
   *
   * @throws NotYetImplementedForExchangeException - Indication that the exchange supports the   requested function or data, but it has not yet been implemented
   * * @throws NotYetImplementedForExchangeException - 表示交易所支持请求的功能或数据，但尚未实现
   *
   * @throws IOException - Indication that a networking error occurred while fetching JSON data
   * * @throws IOException - 指示在获取 JSON 数据时发生网络错误
   */
  default OrderBook getOrderBook(Instrument instrument, Object... args) throws IOException {
    throw new NotYetImplementedForExchangeException("getOrderBook");
  }

  /**
   * Get an order book representing the current offered exchange rates (market depth)
   * * 获取代表当前提供的汇率（市场深度）的订单簿
   *
   * @param params Exchange-specific
   *               特定于交易所
   *
   * @return The OrderBook, null if some sort of error occurred. Implementers should log the error.
   * * @return 订单簿，如果发生某种错误，则返回 null。 实施者应该记录错误。
   *
   * @throws ExchangeException - Indication that the exchange reported some kind of error with the  request or response
   * * @throws ExchangeException - 表示交换报告了请求或响应的某种错误
   *
   * @throws NotAvailableFromExchangeException - Indication that the exchange does not support the  requested function or data
   * * @throws NotAvailableFromExchangeException - 表示交易所不支持请求的功能或数据
   *
   * @throws NotYetImplementedForExchangeException - Indication that the exchange supports the   requested function or data, but it has not yet been implemented
   * * @throws NotYetImplementedForExchangeException - 表示交易所支持请求的功能或数据，但尚未实现
   *
   * @throws IOException - Indication that a networking error occurred while fetching JSON data
   * * @throws IOException - 指示在获取 JSON 数据时发生网络错误
   *
   */
  default OrderBook getOrderBook(Params params) throws IOException {
    throw new NotYetImplementedForExchangeException("getOrderBook");
  }

  /**
   * Get the trades recently performed by the exchange
   * * 获取交易所最近执行的交易
   *
   * @param args Optional arguments. Exchange-specific
   *             * @param args 可选参数。 特定于交易所
   *
   * @return The Trades, null if some sort of error occurred. Implementers should log the error.
   * * @return The Trades，如果发生某种错误，则返回 null。 实施者应该记录错误。
   *
   * @throws ExchangeException - Indication that the exchange reported some kind of error with the  request or response
   * * @throws ExchangeException - 表示交换报告了请求或响应的某种错误
   *
   * @throws NotAvailableFromExchangeException - Indication that the exchange does not support the  requested function or data
   * * @throws NotAvailableFromExchangeException - 表示交易所不支持请求的功能或数据
   *
   * @throws NotYetImplementedForExchangeException - Indication that the exchange supports the  requested function or data, but it has not yet been implemented
   * * @throws NotYetImplementedForExchangeException - 表示交易所支持请求的功能或数据，但尚未实现
   *
   * @throws IOException - Indication that a networking error occurred while fetching JSON data
   * * @throws IOException - 指示在获取 JSON 数据时发生网络错误
   */
  default Trades getTrades(CurrencyPair currencyPair, Object... args) throws IOException {
    throw new NotYetImplementedForExchangeException("getTrades");
  }

  /**
   * Get the trades recently performed by the exchange
   * * 获取交易所最近执行的交易
   *
   * @param args Optional arguments. Exchange-specific
   *             * @param args 可选参数。 特定于交易所
   *
   * @return The Trades, null if some sort of error occurred. Implementers should log the error.
   * * @return The Trades，如果发生某种错误，则返回 null。 实施者应该记录错误。
   *
   * @throws ExchangeException - Indication that the exchange reported some kind of error with the  request or response
   * * @throws ExchangeException - 表示交换报告了请求或响应的某种错误
   *
   * @throws NotAvailableFromExchangeException - Indication that the exchange does not support the  requested function or data
   * * @throws NotAvailableFromExchangeException - 表示交易所不支持请求的功能或数据
   *
   * @throws NotYetImplementedForExchangeException - Indication that the exchange supports the  requested function or data, but it has not yet been implemented
   * * @throws NotYetImplementedForExchangeException - 表示交易所支持请求的功能或数据，但尚未实现
   *
   * @throws IOException - Indication that a networking error occurred while fetching JSON data
   * * @throws IOException - 指示在获取 JSON 数据时发生网络错误
   */
  default Trades getTrades(Instrument instrument, Object... args) throws IOException {
    throw new NotYetImplementedForExchangeException("getTrades");
  }

  /**
   * Get the trades recently performed by the exchange
   * * 获取交易所最近执行的交易
   *
   * @param params Exchange-specific
   *               * @param params 交易所特定
   *
   * @return The Trades, null if some sort of error occurred. Implementers should log the error.
   * * @return The Trades，如果发生某种错误，则返回 null。 实施者应该记录错误。
   *
   * @throws ExchangeException - Indication that the exchange reported some kind of error with the  request or response
   * * @throws ExchangeException - 表示交换报告了请求或响应的某种错误
   *
   * @throws NotAvailableFromExchangeException - Indication that the exchange does not support the  requested function or data
   * * @throws NotAvailableFromExchangeException - 表示交易所不支持请求的功能或数据
   *
   * @throws NotYetImplementedForExchangeException - Indication that the exchange supports the    requested function or data, but it has not yet been implemented
   * * @throws NotYetImplementedForExchangeException - 表示交易所支持请求的功能或数据，但尚未实现
   *
   * @throws IOException - Indication that a networking error occurred while fetching JSON data
   * * @throws IOException - 指示在获取 JSON 数据时发生网络错误
   */
  default Trades getTrades(Params params) throws IOException {
    throw new NotYetImplementedForExchangeException("getTrades");
  }


  /**
   * Get the CandleStickData for given currency between startDate to endDate.
   * * 在 startDate 到 endDate 之间获取给定货币的 CandleStickData。
   *
   * @param currencyPair currencyPair.
   *                     * @param currencyPair 货币对。
   *
   * @param params Params for query, including start(e.g. march 2022.) and end date, period etc.,
   *               * @param params 用于查询的参数，包括开始（例如 2022 年 3 月）和结束日期、期间等，
   *
   * @return The CandleStickData, null if some sort of error occurred. Implementers should log the error.
   * * @return CandleStickData，如果发生某种错误，则返回 null。 实施者应该记录错误。
   *
   * @throws ExchangeException - Indication that the exchange reported some kind of error with the request or response
   * * @throws ExchangeException - 表示交换报告了请求或响应的某种错误
   *
   * @throws NotAvailableFromExchangeException - Indication that the exchange does not support the requested function or data
   * * @throws NotAvailableFromExchangeException - 表示交易所不支持请求的功能或数据
   *
   * @throws NotYetImplementedForExchangeException - Indication that the exchange supports the requested function or data, but it has not yet been implemented
   * * @throws NotYetImplementedForExchangeException - 表示交易所支持请求的功能或数据，但尚未实现
   *
   * @throws IOException - Indication that a networking error occurred while fetching JSON data
   * * @throws IOException - 指示在获取 JSON 数据时发生网络错误
   */
  default CandleStickData getCandleStickData(CurrencyPair currencyPair, CandleStickDataParams params) throws IOException {
    throw new NotYetImplementedForExchangeException("getCandleStickData");
  }
}
