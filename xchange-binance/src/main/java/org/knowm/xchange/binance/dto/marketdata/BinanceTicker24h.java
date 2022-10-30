package org.knowm.xchange.binance.dto.marketdata;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.Date;
import org.knowm.xchange.binance.BinanceAdapters;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Ticker;

/**
 * Binance股票24小时
 */
public final class BinanceTicker24h {
  /**
   * 价格变化
   */
  private final BigDecimal priceChange;
  /**
   * 价格变化百分比
   */
  private final BigDecimal priceChangePercent;
  /**
   *  加权/加重的/权重 平均价格
   */
  private final BigDecimal weightedAvgPrice;
  /**
   * 上一个/前一个 收盘价
   */
  private final BigDecimal prevClosePrice;
  /**
   * 最后价格
   */
  private final BigDecimal lastPrice;
  /**
   * 最后数量
   */
  private final BigDecimal lastQty;
  /**
   * 买价 /买入价
   */
  private final BigDecimal bidPrice;
  /**
   * 买/买入数量
   */
  private final BigDecimal bidQty;
  /**
   * 卖价；卖盘价
   */
  private final BigDecimal askPrice;
  /**
   * 卖；卖盘数量
   */
  private final BigDecimal askQty;
  /**
   *  开盘价
   */
  private final BigDecimal openPrice;
  /**
   * 最高价
   */
  private final BigDecimal highPrice;
  /**
   *  最底价
   */
  private final BigDecimal lowPrice;
  /**
   * 交易量
   */
  private final BigDecimal volume;
  /**
   * 报价量
   */
  private final BigDecimal quoteVolume;
  /**
   * 开盘时间
   */
  private final long openTime;
  /**
   * 关闭时间
   */
  private final long closeTime;
  /**
   * 第一个id
   */
  private final long firstId;
  /**
   * 最后一个id
   */
  private final long lastId;
  /**
   * 统计/计数
   */
  private final long count;
  /**
   * 符号
   */
  private final String symbol;

  // The curency pair that is unfortunately not returned in the response
  //在响应中不幸没有返回的货币对
  private CurrencyPair pair;

  // The cached ticker
  //缓存的股票
  private Ticker ticker;

  /**
   * Binance股票24小时
   * @param priceChange 价格变化
   * @param priceChangePercent 价格变化百分比
   * @param weightedAvgPrice 加权/加重的/权重 平均价格
   * @param prevClosePrice  上一个/前一个 收盘价
   * @param lastPrice 最后价格
   * @param lastQty 最后价格
   * @param bidPrice 买价 /买入价
   * @param bidQty 买/买入数量
   * @param askPrice 卖价；卖盘价
   * @param askQty 卖；卖盘数量
   * @param openPrice 开盘价
   * @param highPrice 最高价
   * @param lowPrice 最底价
   * @param volume 交易量
   * @param quoteVolume 报价量
   * @param openTime 开盘时间
   * @param closeTime 关闭时间
   * @param firstId  第一个id
   * @param lastId 最后一个id
   * @param count  统计/计数
   * @param symbol 符号
   */
  public BinanceTicker24h(
      @JsonProperty("priceChange") BigDecimal priceChange,
      @JsonProperty("priceChangePercent") BigDecimal priceChangePercent,
      @JsonProperty("weightedAvgPrice") BigDecimal weightedAvgPrice,
      @JsonProperty("prevClosePrice") BigDecimal prevClosePrice,
      @JsonProperty("lastPrice") BigDecimal lastPrice,
      @JsonProperty("lastQty") BigDecimal lastQty,
      @JsonProperty("bidPrice") BigDecimal bidPrice,
      @JsonProperty("bidQty") BigDecimal bidQty,
      @JsonProperty("askPrice") BigDecimal askPrice,
      @JsonProperty("askQty") BigDecimal askQty,
      @JsonProperty("openPrice") BigDecimal openPrice,
      @JsonProperty("highPrice") BigDecimal highPrice,
      @JsonProperty("lowPrice") BigDecimal lowPrice,
      @JsonProperty("volume") BigDecimal volume,
      @JsonProperty("quoteVolume") BigDecimal quoteVolume,
      @JsonProperty("openTime") long openTime,
      @JsonProperty("closeTime") long closeTime,
      @JsonProperty("firstId") long firstId,
      @JsonProperty("lastId") long lastId,
      @JsonProperty("count") long count,
      @JsonProperty("symbol") String symbol) {
    this.priceChange = priceChange;
    this.priceChangePercent = priceChangePercent;
    this.weightedAvgPrice = weightedAvgPrice;
    this.prevClosePrice = prevClosePrice;
    this.lastPrice = lastPrice;
    this.lastQty = lastQty;
    this.bidPrice = bidPrice;
    this.bidQty = bidQty;
    this.askPrice = askPrice;
    this.askQty = askQty;
    this.openPrice = openPrice;
    this.highPrice = highPrice;
    this.lowPrice = lowPrice;
    this.volume = volume;
    this.quoteVolume = quoteVolume;
    this.openTime = openTime;
    this.closeTime = closeTime;
    this.firstId = firstId;
    this.lastId = lastId;
    this.count = count;
    this.symbol = symbol;
  }

  public String getSymbol() {
    return symbol;
  }

  public CurrencyPair getCurrencyPair() {
    return pair;
  }

  public void setCurrencyPair(CurrencyPair pair) {
    this.pair = pair;
  }

  public BigDecimal getPriceChange() {
    return priceChange;
  }

  public BigDecimal getPriceChangePercent() {
    return priceChangePercent;
  }

  public BigDecimal getWeightedAvgPrice() {
    return weightedAvgPrice;
  }

  public BigDecimal getPrevClosePrice() {
    return prevClosePrice;
  }

  public BigDecimal getLastPrice() {
    return lastPrice;
  }

  public BigDecimal getLastQty() {
    return lastQty;
  }

  public BigDecimal getBidPrice() {
    return bidPrice;
  }

  public BigDecimal getBidQty() {
    return bidQty;
  }

  public BigDecimal getAskPrice() {
    return askPrice;
  }

  public BigDecimal getAskQty() {
    return askQty;
  }

  public BigDecimal getOpenPrice() {
    return openPrice;
  }

  public BigDecimal getHighPrice() {
    return highPrice;
  }

  public BigDecimal getLowPrice() {
    return lowPrice;
  }

  public BigDecimal getVolume() {
    return volume;
  }

  public BigDecimal getQuoteVolume() {
    return quoteVolume;
  }

  public long getFirstTradeId() {
    return firstId;
  }

  public long getLastTradeId() {
    return lastId;
  }

  public long getTradeCount() {
    return count;
  }

  /**
   * 获取开盘时间
   * @return
   */
  public Date getOpenTime() {
    return new Date(openTime);
  }

  /**
   * 获取关闭时间
   * @return
   */
  public Date getCloseTime() {
    return new Date(closeTime);
  }

  public synchronized Ticker toTicker() {
    CurrencyPair currencyPair = pair;
    if (currencyPair == null) {
      currencyPair = BinanceAdapters.adaptSymbol(symbol);
    }
    if (ticker == null) {
      ticker =
          new Ticker.Builder()
              .currencyPair(currencyPair)
              .open(openPrice)
              .ask(askPrice)
              .bid(bidPrice)
              .last(lastPrice)
              .high(highPrice)
              .low(lowPrice)
              .volume(volume)
              .vwap(weightedAvgPrice)
              .askSize(askQty)
              .bidSize(bidQty)
              .quoteVolume(quoteVolume)
              .timestamp(closeTime > 0 ? new Date(closeTime) : null)
              .percentageChange(priceChangePercent)
              .build();
    }
    return ticker;
  }

  @Override
  public String toString() {
    return "BinanceTicker24h{" +
            "priceChange=" + priceChange +
            ", priceChangePercent=" + priceChangePercent +
            ", weightedAvgPrice=" + weightedAvgPrice +
            ", prevClosePrice=" + prevClosePrice +
            ", lastPrice=" + lastPrice +
            ", lastQty=" + lastQty +
            ", bidPrice=" + bidPrice +
            ", bidQty=" + bidQty +
            ", askPrice=" + askPrice +
            ", askQty=" + askQty +
            ", openPrice=" + openPrice +
            ", highPrice=" + highPrice +
            ", lowPrice=" + lowPrice +
            ", volume=" + volume +
            ", quoteVolume=" + quoteVolume +
            ", openTime=" + openTime +
            ", closeTime=" + closeTime +
            ", firstId=" + firstId +
            ", lastId=" + lastId +
            ", count=" + count +
            ", symbol='" + symbol + '\'' +
            ", pair=" + pair +
            ", ticker=" + ticker +
            '}';
  }
}
