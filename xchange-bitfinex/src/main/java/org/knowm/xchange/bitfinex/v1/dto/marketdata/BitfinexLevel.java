package org.knowm.xchange.bitfinex.v1.dto.marketdata;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

/**
 * Bitfinex 级别
 */
public class BitfinexLevel {
  /**
   * 价格
   */
  private final BigDecimal price;
  /**
   * 数量
   */
  private final BigDecimal amount;
  /**
   * 时间戳
   */
  private final BigDecimal timestamp;

  /**
   * Constructor
   *
   * @param price
   *        价格
   * @param amount
   *      数量
   * @param timestamp
   *      时间戳
   */
  public BitfinexLevel(
      @JsonProperty("price") BigDecimal price,
      @JsonProperty("amount") BigDecimal amount,
      @JsonProperty("timestamp") BigDecimal timestamp) {

    this.price = price;
    this.amount = amount;
    this.timestamp = timestamp;
  }

  public BigDecimal getPrice() {

    return price;
  }

  public BigDecimal getAmount() {

    return amount;
  }

  public BigDecimal getTimestamp() {

    return timestamp;
  }

  @Override
  public String toString() {

    return "BitfinexLevel [price="
        + price
        + ", amount="
        + amount
        + ", timestamp="
        + timestamp
        + "]";
  }
}
