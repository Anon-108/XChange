package org.knowm.xchange.bitfinex.v1.dto.account;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

/** http://docs.bitfinex.com/#deposit-withdrawal-history */
public class BitfinexDepositWithdrawalHistoryRequest {
  @JsonProperty("request")
  private final String request;

  @JsonProperty("nonce")
  private final String nonce;

  /** The currency to look for.
   * 要查找的货币。 */
  @JsonProperty("currency")
  private final String currency;

  /**
   * Optional. The method of the deposit/withdrawal (can be “bitcoin”, “litecoin”, “darkcoin”, “wire”).
   * 可选的。 存款/取款方式（可以是“比特币”、“莱特币”、“暗币”、“电汇”）。
   */
  @JsonProperty("method")
  @JsonInclude(Include.NON_NULL)
  private final String method;

  /** Optional. Return only the history after this timestamp.
   * 可选的。 仅返回此时间戳之后的历史记录。 */
  @JsonProperty("since")
  @JsonInclude(Include.NON_NULL)
  private final String since;

  /** Optional. Return only the history before this timestamp.
   * 可选的。 仅返回此时间戳之前的历史记录。 */
  @JsonProperty("until")
  @JsonInclude(Include.NON_NULL)
  private final String until;

  /** Optional. Limit the number of entries to return. Default is 500.
   * 可选的。 限制要返回的条目数。 默认值为 500。 */
  @JsonProperty("limit")
  @JsonInclude(Include.NON_NULL)
  private final Integer limit;

  public BitfinexDepositWithdrawalHistoryRequest(
      String nonce, String currency, String method, Date since, Date until, Integer limit) {
    this.request = "/v1/history/movements";
    this.nonce = String.valueOf(nonce);
    this.currency = currency;
    this.method = method;
    this.since = since == null ? null : String.valueOf(since.getTime() / 1000);
    this.until = until == null ? null : String.valueOf(until.getTime() / 1000);
    this.limit = limit;
  }
}
