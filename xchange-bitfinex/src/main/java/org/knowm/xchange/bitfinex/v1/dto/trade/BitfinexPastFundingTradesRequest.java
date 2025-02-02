package org.knowm.xchange.bitfinex.v1.dto.trade;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

/**
 * Bitfinex 过去的资金交易请求
 */
public class BitfinexPastFundingTradesRequest {

  @JsonProperty("request")
  protected String request;

  @JsonProperty("nonce")
  protected String nonce;

  @JsonProperty("symbol")
  protected String symbol;

  /** Trades made after this timestamp won’t be returned.
   * 在此时间戳之后进行的交易将不会被退回。 */
  @JsonProperty("until")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  protected Date until;
  /**
   * 限制交易/交易额度?
   */
  @JsonProperty("limit_trades")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  protected Integer limitTrades;

  public BitfinexPastFundingTradesRequest(
      String nonce, String symbol, Date until, Integer limitTrades) {

    this.request = "/v1/mytrades_funding";
    this.nonce = nonce;
    this.symbol = symbol;
    this.until = until;
    this.limitTrades = limitTrades;
  }
}
