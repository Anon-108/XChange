package org.knowm.xchange.dto.meta;

public enum WalletHealth {
  /** You can deposit and withdraw founds from the exchange
   * 您可以从交易所存入和提取资金 */
  ONLINE,
  DEPOSITS_DISABLED,
  WITHDRAWALS_DISABLED,
  /** You cannot deposit nor withdraw founds from the exchange
   * 您不能从交易所存入或提取资金 */
  OFFLINE,
  /** The exchange does not inform us about the health of this wallet
   * 交易所没有通知我们这个钱包的健康状况*/
  UNKNOWN;
}
