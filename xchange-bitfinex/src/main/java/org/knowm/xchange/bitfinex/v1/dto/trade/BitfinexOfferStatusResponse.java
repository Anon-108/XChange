package org.knowm.xchange.bitfinex.v1.dto.trade;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

/**
 * Bitfinex 报价状态响应
 */
public class BitfinexOfferStatusResponse {

  private final long id;
  /**
   * 货币
   */
  private final String currency;
  /**
   * 比例/费率
   */
  private final BigDecimal rate;
  /**
   *  一段时间，时期
   */
  private final int period;
  /**
   * 方向，方位；趋势
   */
  private final String direction;
  /**
   * 类型
   */
  private final String type;
  /**
   * 时间戳
   */
  private final BigDecimal timestamp;
  /**
   * 生存，活着
   */
  private final boolean isLive;
  /**
   *  取消；作废
   */
  private final boolean isCancelled;
  /**
   *  起初的，原先的 数量
   */
  private final BigDecimal originalAmount;
  /**
   * 剩下的，遗留的； 数量
   */
  private final BigDecimal remainingAmount;
  /**
   * 处决；实施；完成 数量
   */
  private final BigDecimal executedAmount;

  public BitfinexOfferStatusResponse(
      @JsonProperty("id") long id,
      @JsonProperty("currency") String currency,
      @JsonProperty("rate") BigDecimal rate,
      @JsonProperty("period") int period,
      @JsonProperty("direction") String direction,
      @JsonProperty("type") String type,
      @JsonProperty("timestamp") BigDecimal timestamp,
      @JsonProperty("is_live") boolean isLive,
      @JsonProperty("is_cancelled") boolean isCancelled,
      @JsonProperty("original_amount") BigDecimal originalAmount,
      @JsonProperty("remaining_amount") BigDecimal remainingAmount,
      @JsonProperty("executed_amount") BigDecimal executedAmount) {

    this.id = id;
    this.currency = currency;
    this.rate = rate;
    this.period = period;
    this.direction = direction;
    this.type = type;
    this.timestamp = timestamp;
    this.isLive = isLive;
    this.isCancelled = isCancelled;
    this.originalAmount = originalAmount;
    this.remainingAmount = remainingAmount;
    this.executedAmount = executedAmount;
  }

  public long getId() {

    return id;
  }

  public String getCurrency() {

    return currency;
  }

  public BigDecimal getRate() {

    return rate;
  }

  public int getPeriod() {

    return period;
  }

  public String getDirection() {

    return direction;
  }

  public String getType() {

    return type;
  }

  public BigDecimal getTimestamp() {

    return timestamp;
  }

  public boolean isLive() {

    return isLive;
  }

  public boolean isCancelled() {

    return isCancelled;
  }

  public BigDecimal getOriginalAmount() {

    return originalAmount;
  }

  public BigDecimal getRemainingAmount() {

    return remainingAmount;
  }

  public BigDecimal getExecutedAmount() {

    return executedAmount;
  }

  @Override
  public String toString() {

    return "BitfinexOfferStatusResponse [id="
        + id
        + ", currency="
        + currency
        + ", rate="
        + rate
        + ", period="
        + period
        + ", direction="
        + direction
        + ", type="
        + type
        + ", timestamp="
        + timestamp
        + ", isLive="
        + isLive
        + ", isCancelled="
        + isCancelled
        + ", originalAmount="
        + originalAmount
        + ", remainingAmount="
        + remainingAmount
        + ", executedAmount="
        + executedAmount
        + "]";
  }
}
