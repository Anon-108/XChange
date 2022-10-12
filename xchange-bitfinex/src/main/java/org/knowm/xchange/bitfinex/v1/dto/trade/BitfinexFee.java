package org.knowm.xchange.bitfinex.v1.dto.trade;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

/**
 * bitfinex费用
 */
public class BitfinexFee {
  /**
   * 配对
   */
  private final String pairs;
  private final BigDecimal makerFees;
  private final BigDecimal takerFees;

  public BitfinexFee(
      @JsonProperty("pairs") String pairs,
      @JsonProperty("maker_fees") BigDecimal makerFees,
      @JsonProperty("taker_fees") BigDecimal takerFees) {
    this.pairs = pairs;
    this.makerFees = makerFees;
    this.takerFees = takerFees;
  }

  public String getPairs() {
    return pairs;
  }

  public BigDecimal getMakerFees() {
    return makerFees;
  }

  public BigDecimal getTakerFees() {
    return takerFees;
  }
}
