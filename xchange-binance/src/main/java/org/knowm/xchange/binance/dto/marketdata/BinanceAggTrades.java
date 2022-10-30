package org.knowm.xchange.binance.dto.marketdata;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 币安 聚合/总数交易
 */
public final class BinanceAggTrades {

  /**
   * 聚合/总数交易id
   */
  public final long aggregateTradeId;
  /**
   * 价钱
   */
  public final BigDecimal price;
  /**
   * 量 /数量
   */
  public final BigDecimal quantity;
  /**
   * 第一个交易Id
   */
  public final long firstTradeId;
  /**
   * 最后一个交易Id
   */
  public final long lastTradeId;
  /**
   * 时间戳
   */
  public final long timestamp;
  /**
   * 买家 制造商
   */
  public final boolean buyerMaker;
  /**
   * 最佳价格匹配
   */
  public final boolean bestPriceMatch;

  /**
   * 币安 聚合/总数交易
   * @param aggregateTradeId 聚合/总数交易id
   * @param price 价钱
   * @param quantity 量 /数量
   * @param firstTradeId 第一个交易Id
   * @param lastTradeId 最后一个交易Id
   * @param timestamp 时间戳
   * @param buyerMaker 买家 制造商
   * @param bestPriceMatch 最佳价格匹配
   */
  public BinanceAggTrades(
      @JsonProperty("a") long aggregateTradeId,
      @JsonProperty("p") BigDecimal price,
      @JsonProperty("q") BigDecimal quantity,
      @JsonProperty("f") long firstTradeId,
      @JsonProperty("l") long lastTradeId,
      @JsonProperty("T") long timestamp,
      @JsonProperty("m") boolean buyerMaker,
      @JsonProperty("M") boolean bestPriceMatch) {
    this.aggregateTradeId = aggregateTradeId;
    this.price = price;
    this.quantity = quantity;
    this.firstTradeId = firstTradeId;
    this.lastTradeId = lastTradeId;
    this.timestamp = timestamp;
    this.buyerMaker = buyerMaker;
    this.bestPriceMatch = bestPriceMatch;
  }

  public Date getTimestamp() {
    return new Date(timestamp);
  }
}
