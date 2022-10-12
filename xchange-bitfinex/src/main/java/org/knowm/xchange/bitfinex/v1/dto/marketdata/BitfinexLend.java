package org.knowm.xchange.bitfinex.v1.dto.marketdata;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

/**
 * bitfinex借出
 */
public class BitfinexLend {
  /**
   * 比例/费率
   */
  private final BigDecimal rate;
  /**
   * 借出数量
   */
  private final BigDecimal amountLent;
  /**
   * 时间戳
   */
  private final long timestamp;

  /**
   * Constructor
   *
   * @param rate
   *      比例费率
   * @param amountLent
   *      借出金额
   * @param timestamp
   *        时间戳
   */
  public BitfinexLend(
      @JsonProperty("rate") BigDecimal rate,
      @JsonProperty("amount_lent") BigDecimal amountLent,
      @JsonProperty("timestamp") long timestamp) {

    this.rate = rate;
    this.amountLent = amountLent;
    this.timestamp = timestamp;
  }

  public BigDecimal getRate() {

    return rate;
  }

  public BigDecimal getAmountLent() {

    return amountLent;
  }

  public long getTimestamp() {

    return timestamp;
  }

  @Override
  public String toString() {

    return "BitfinexLend [rate="
        + rate
        + ", amountLent="
        + amountLent
        + ", timestamp="
        + timestamp
        + "]";
  }
}
