package org.knowm.xchange.binance.service;

import org.knowm.xchange.service.trade.params.DefaultTradeHistoryParamsTimeSpan;

/**
 * 币安主账户转账历史参数
 */
public class BinanceMasterAccountTransferHistoryParams extends DefaultTradeHistoryParamsTimeSpan {
  /**
   * 电子邮箱
   */
  private String email;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
