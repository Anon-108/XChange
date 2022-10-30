package org.knowm.xchange.binance.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 提，取（银行账户中的钱款）响应
 */
public final class WithdrawResponse {
  public final String id;

  /**
   * 提，取（银行账户中的钱款）响应
   * @param id
   */
  public WithdrawResponse(@JsonProperty("id") String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }
}
