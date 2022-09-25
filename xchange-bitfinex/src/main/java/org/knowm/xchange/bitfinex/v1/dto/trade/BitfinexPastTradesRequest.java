package org.knowm.xchange.bitfinex.v1.dto.trade;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BitfinexPastTradesRequest {

  @JsonProperty("request")
  protected String request;

  @JsonProperty("nonce")
  protected String nonce;

  @JsonProperty("symbol")
  protected String symbol;

  /** REQUIRED Trades made before this timestamp won’t be returned
   * 在此时间戳之前进行的必需交易将不会被退回*/
  @JsonProperty("timestamp")
  protected long startTime;

  /** Trades made after this timestamp won’t be returned.
   * 在此时间戳之后进行的交易将不会被退回。 */
  @JsonProperty("until")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  protected Long endTime;

  @JsonProperty("limit_trades")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  protected Integer limitTrades;

  /**
   * Return trades in reverse order (the oldest comes first). Default is returning newest trades first. default: 0
   * * 以相反的顺序返回交易（最旧的先出现）。 默认是首先返回最新的交易。 默认值：0
   */
  @JsonProperty("reverse")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  protected Integer reverse;

  public BitfinexPastTradesRequest(
      String nonce,
      String symbol,
      long startTime,
      Long endTime,
      Integer limitTrades,
      Integer reverse) {
    this.request = "/v1/mytrades";
    this.nonce = nonce;
    this.symbol = symbol;
    this.startTime = startTime;
    this.endTime = endTime;
    this.limitTrades = limitTrades;
    this.reverse = reverse;
  }
}
