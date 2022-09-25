package org.knowm.xchange.bitfinex.v2.dto.account;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;

/** https://docs.bitfinex.com/reference#rest-auth-wallets */
@JsonFormat(shape = JsonFormat.Shape.ARRAY)
@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class Wallet {

  /** Wallet name (exchange, margin, funding)
   * 钱包名称（交易所、保证金、资金）*/
  private String walletType;
  /** Currency (e.g. USD, ...)
   * 货币（例如美元，...）*/
  private String currency;
  /** Wallet balance
   * 钱包余额*/
  private BigDecimal balance;
  /** Unsettled interest
   * 未解决的兴趣*/
  private BigDecimal unsettledInterest;
  /** Wallet balance available for orders/withdrawal/transfer
   * 可用于订单/提款/转账的钱包余额*/
  private BigDecimal availableBalance;
  /** Description of the last ledger entry
   * 最后一个分类帐条目的描述*/
  private String lastChange;
  /** If the last change was a trade, this object will show the trade details
   * 如果上次更改是交易，此对象将显示交易详情*/
  private Object tradeDetails;
}
