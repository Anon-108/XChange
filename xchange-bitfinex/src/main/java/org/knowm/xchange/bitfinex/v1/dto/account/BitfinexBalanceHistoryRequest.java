package org.knowm.xchange.bitfinex.v1.dto.account;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

/**
 * Bitfinex 余额历史请求
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BitfinexBalanceHistoryRequest {

  @JsonProperty("request")
  protected String request;

  @JsonProperty("nonce")
  protected String nonce;

  @JsonProperty("currency")
  protected String currency;

  @JsonProperty("since")
  protected Date since;

  @JsonProperty("until")
  protected Date until;

  @JsonProperty("limit")
  protected int limit;

  @JsonProperty("wallet")
  protected String wallet;

  /**
   * Constructor
   *
   * @param nonce
   *      随机数
   * @param currency
   *        货币
   * @param wallet
   *      钱包
   * @param since
   *        从
   *
   * @param until
   *        直到
   * @param limit
   *        限制
   */
  public BitfinexBalanceHistoryRequest(
      String nonce, String currency, Long since, Long until, int limit, String wallet) {

    this.request = "/v1/history";
    this.nonce = nonce;
    this.currency = currency;
    this.wallet = wallet;
    this.since = since == null ? null : new Date(since);
    this.until = until == null ? null : new Date(until);
    this.limit = limit;
  }
}
