package org.knowm.xchange.bitfinex.v2.dto.marketdata;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.ToString;

/**
 * 公共资金交易
 */
@JsonFormat(shape = JsonFormat.Shape.ARRAY)
@Getter
@ToString
public class BitfinexPublicFundingTrade {

  private long tradeId;
  private long timestamp;
  private BigDecimal amount;
  private BigDecimal rate;
  /**
   * 一段时间，时期；
   */
  private int period;

  public BitfinexPublicFundingTrade() {}

  public BitfinexPublicFundingTrade(
      long tradeId, long timestamp, BigDecimal amount, BigDecimal rate, int period) {

    this.tradeId = tradeId;
    this.timestamp = timestamp;
    this.amount = amount;
    this.rate = rate;
    this.period = period;
  }
}
