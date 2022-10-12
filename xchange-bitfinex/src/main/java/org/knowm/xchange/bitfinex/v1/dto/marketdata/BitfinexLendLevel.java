package org.knowm.xchange.bitfinex.v1.dto.marketdata;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

/**
 * 借贷等级
 */
public class BitfinexLendLevel {
  /**
   * 比例/费率
   */
  private final BigDecimal rate;
  /**
   * 数量
   */
  private final BigDecimal amount;
  /**
   *时期/期间
   */
  private final int period;
  /**
   * 时间搓
   */
  private final float timestamp;
  private final String frr;

  /**
   * Constructor
   *
   * @param rate
   *      速度
   * @param amount
   *      数量
   * @param period
   *      时期
   * @param timestamp
   *      时间戳
   * @param frr
   */
  public BitfinexLendLevel(
      @JsonProperty("rate") BigDecimal rate,
      @JsonProperty("amount") BigDecimal amount,
      @JsonProperty("period") int period,
      @JsonProperty("timestamp") float timestamp,
      @JsonProperty("frr") String frr) {

    this.rate = rate;
    this.amount = amount;
    this.period = period;
    this.timestamp = timestamp;
    this.frr = frr;
  }

  public BigDecimal getRate() {

    return rate;
  }

  public BigDecimal getAmount() {

    return amount;
  }

  public int getPeriod() {

    return period;
  }

  public float getTimestamp() {

    return timestamp;
  }

  public String getFrr() {

    return frr;
  }

  @Override
  public String toString() {

    return "BitfinexLendLevel [rate="
        + rate
        + ", amount="
        + amount
        + ", period="
        + period
        + ", timestamp="
        + timestamp
        + ", frr="
        + frr
        + "]";
  }
}
