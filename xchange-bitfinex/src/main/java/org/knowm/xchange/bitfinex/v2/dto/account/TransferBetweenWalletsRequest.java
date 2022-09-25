package org.knowm.xchange.bitfinex.v2.dto.account;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
/** see https://docs.bitfinex.com/reference#rest-auth-transfer */
@JsonInclude(Include.NON_NULL)
public class TransferBetweenWalletsRequest {
  /**
   * Select the wallet from which to transfer (exchange, margin, funding (can also use the old labels which are exchange, trading and deposit respectively))
   * * 选择转账的钱包（交易所、保证金、资金（也可以使用旧标签分别为交易所、交易和存款））
   */
  @NonNull private String from;
  /**
   * Select the wallet from which to transfer (exchange, margin, funding (can also use the old labels which are exchange, trading and deposit respectively))
   * * 选择转账的钱包（交易所、保证金、资金（也可以使用旧标签分别为交易所、交易和存款））
   */
  @NonNull private String to;
  /** Select the currency that you would like to transfer (USD, UST, BTC, ....)
   * 选择您要转账的货币（USD、UST、BTC、....）*/
  @NonNull private String currency;
  /**
   * Select the currency that you would like to exchange to (USTF0 === USDT for derivatives pairs)
   * 选择您想兑换的货币（USTF0 === USDT 用于衍生品对）
   */
  @JsonProperty("currency_to")
  private String currencyTo;
  /** Select the amount to transfer
   * 选择转账金额*/
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  @NonNull
  private BigDecimal amount;
  /**
   * Allows transfer of funds to a sub- or master-account identified by the associated email address.
   * 允许将资金转移到由相关电子邮件地址标识的子账户或主账户。
   */
  @JsonProperty("email_dst")
  private String emailDestination;
}
