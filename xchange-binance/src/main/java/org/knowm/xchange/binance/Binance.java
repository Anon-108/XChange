package org.knowm.xchange.binance;

import java.io.IOException;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.knowm.xchange.binance.dto.BinanceException;
import org.knowm.xchange.binance.dto.marketdata.BinanceAggTrades;
import org.knowm.xchange.binance.dto.marketdata.BinanceOrderbook;
import org.knowm.xchange.binance.dto.marketdata.BinancePrice;
import org.knowm.xchange.binance.dto.marketdata.BinancePriceQuantity;
import org.knowm.xchange.binance.dto.marketdata.BinanceTicker24h;
import org.knowm.xchange.binance.dto.meta.BinanceSystemStatus;
import org.knowm.xchange.binance.dto.meta.BinanceTime;
import org.knowm.xchange.binance.dto.meta.exchangeinfo.BinanceExchangeInfo;

@Path("")
@Produces(MediaType.APPLICATION_JSON)
public interface Binance {

  /**
   * 系统状态
   *
   * Fetch system status which is normal or system maintenance.
   * 获取正常或系统维护的系统状态。
   *
   * @throws IOException
   */
  @GET
  @Path("sapi/v1/system/status")
  BinanceSystemStatus systemStatus() throws IOException;

  /**
   * 测试到Rest API的连接性。
   * Test connectivity to the Rest API.
   *
   * @return
   * @throws IOException
   */
  @GET
  @Path("api/v3/ping")
  Object ping() throws IOException;

  /**
   * Test connectivity to the Rest API and get the current server time.
   *  测试与 Rest API 的连接并获取当前服务器时间。
   * @return
   * @throws IOException
   */
  @GET
  @Path("api/v3/time")
  BinanceTime time() throws IOException;

  /**
   * Current exchange trading rules and symbol information.
   * 当前的交易所交易规则和符号信息。
   *
   * @return
   * @throws IOException
   */
  @GET
  @Path("api/v3/exchangeInfo")
  BinanceExchangeInfo exchangeInfo() throws IOException;

  /**
   * @param symbol
   * @param limit optional, default 100 max 5000. Valid limits: [5, 10, 20, 50, 100, 500, 1000, 5000]
   *              @param limit 可选，默认 100 最大 5000。有效限制：[5, 10, 20, 50, 100, 500, 1000, 5000]
   * @return
   * @throws IOException
   * @throws BinanceException
   */
  @GET
  @Path("api/v3/depth")
  BinanceOrderbook depth(@QueryParam("symbol") String symbol, @QueryParam("limit") Integer limit)
      throws IOException, BinanceException;

  /**
   * Get compressed, aggregate trades. Trades that fill at the time, from the same order, with the same price will have the quantity aggregated.<br>
   * * 获得压缩的聚合交易。 同一订单以相同价格在同一时间成交的交易将汇总数量。<br>
   *
   * If both startTime and endTime are sent, limit should not be sent AND the distance between startTime and endTime must be less than 24 hours.<br>
   * * 如果同时发送 startTime 和 endTime，则不应发送 limit 并且 startTime 和 endTime 之间的距离必须小于 24 小时。<br>
   *
   * If frondId, startTime, and endTime are not sent, the most recent aggregate trades will be returned.
   * * 如果没有发送 frondId、startTime 和 endTime，将返回最近的聚合交易。
   *
   * @param symbol
   * @param fromId optional, ID to get aggregate trades from INCLUSIVE.
   *               * @param fromId 可选，从 INCLUSIVE 获取聚合交易的 ID。
   *
   * @param startTime optional, Timestamp in ms to get aggregate trades from INCLUSIVE.
   *                  * @param startTime 可选，以毫秒为单位的时间戳，用于从 INCLUSIVE 获取聚合交易。
   *
   * @param endTime optional, Timestamp in ms to get aggregate trades until INCLUSIVE.
   *                * @param endTime 可选，以毫秒为单位的时间戳以获取聚合交易，直到包含在内。
   *
   * @param limit optional, Default 500; max 500.
   *                * @param limit 可选，默认 500； 最多 500 个。
   * @return
   * @throws IOException
   * @throws BinanceException
   */
  @GET
  @Path("api/v3/aggTrades")
  List<BinanceAggTrades> aggTrades(
      @QueryParam("symbol") String symbol,
      @QueryParam("fromId") Long fromId,
      @QueryParam("startTime") Long startTime,
      @QueryParam("endTime") Long endTime,
      @QueryParam("limit") Integer limit)
      throws IOException, BinanceException;

  /**
   * Kline/candlestick bars for a symbol. Klines are uniquely identified by their open time.<br>
   * 符号的 Kline/烛台柱。 Klines 通过其开放时间进行唯一标识。<br>
   *
   * If startTime and endTime are not sent, the most recent klines are returned.
   * 如果未发送 startTime 和 endTime，则返回最近的 klines。
   *
   * @param symbol
   * @param interval
   * @param limit optional, default 500; max 500.
   *              @param limit 可选，默认 500； 最多 500 个。
   * @param startTime optional 可选
   * @param endTime optional 可选
   * @return
   * @throws IOException
   * @throws BinanceException
   */
  @GET
  @Path("api/v3/klines")
  List<Object[]> klines(
      @QueryParam("symbol") String symbol,
      @QueryParam("interval") String interval,
      @QueryParam("limit") Integer limit,
      @QueryParam("startTime") Long startTime,
      @QueryParam("endTime") Long endTime)
      throws IOException, BinanceException;

  /**
   * 24 hour price change statistics for all symbols. - bee carreful this api call have a big weight, only about 4 call per minut can be without ban.
   * * 所有品种的 24 小时价格变化统计。 - 注意这个 api 调用权重很大，每分钟只有大约 4 个调用可以不受禁止。
   *
   * @return
   * @throws IOException
   * @throws BinanceException
   */
  @GET
  @Path("api/v3/ticker/24hr")
  List<BinanceTicker24h> ticker24h() throws IOException, BinanceException;

  /**
   * 24 hour price change statistics.
   * 24小时价格变化统计。
   *
   * @param symbol
   * @return
   * @throws IOException
   * @throws BinanceException
   */
  @GET
  @Path("api/v3/ticker/24hr")
  BinanceTicker24h ticker24h(@QueryParam("symbol") String symbol)
      throws IOException, BinanceException;

  /**
   * Latest price for a symbol.
   * 符号的最新价格。
   *
   * @return
   * @throws IOException
   * @throws BinanceException
   */
  @GET
  @Path("api/v3/ticker/price")
  BinancePrice tickerPrice(@QueryParam("symbol") String symbol)
      throws IOException, BinanceException;

  /**
   * Latest price for all symbols.
   * 所有符号的最新价格。
   *
   * @return
   * @throws IOException
   * @throws BinanceException
   */
  @GET
  @Path("api/v3/ticker/price")
  List<BinancePrice> tickerAllPrices() throws IOException, BinanceException;

  /**
   * Best price/qty on the order book for all symbols.
   * * 所有符号的订单簿上的最佳价格/数量。
   *
   * @return
   * @throws IOException
   * @throws BinanceException
   */
  @GET
  @Path("api/v3/ticker/bookTicker")
  List<BinancePriceQuantity> tickerAllBookTickers() throws IOException, BinanceException;
}
