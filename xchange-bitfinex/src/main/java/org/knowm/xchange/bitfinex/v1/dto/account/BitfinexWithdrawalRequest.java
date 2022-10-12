package org.knowm.xchange.bitfinex.v1.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import java.math.BigDecimal;
import lombok.Data;

/**
 * Bitfinex 提款请求
 */
@Data
public class BitfinexWithdrawalRequest {
  /**
   * 提款类型
   */
  @JsonProperty("withdraw_type")
  private final String withdrawType;
  /**
   * 选择钱包
   */
  @JsonProperty("walletselected")
  private final String walletSelected;
  /**
   * 数量
   */
  private final String amount;
  /**
   * 地址
   */
  private final String address;
  /**
   * 支付id
   */
  @JsonProperty("payment_id")
  private final String paymentId;
  /**
   * 请求
   */
  protected String request;
  /**
   * 随机数
   */
  protected String nonce;
  /**
   * 选项
   */
  @JsonRawValue protected String options;
  /**
   * 货币
   */
  private String currency;

  /**
   * Constructor
   *
   * @param nonce
   *        随机数
   * @param withdrawType
   *      提取类型
   * @param walletSelected
   *      选择钱包
   * @param amount
   *        数量
   * @param address
   *      地址
   */
  public BitfinexWithdrawalRequest(
      String nonce,
      String withdrawType,
      String walletSelected,
      BigDecimal amount,
      String address,
      String paymentId) {

    this.request = "/v1/withdraw";
    this.nonce = String.valueOf(nonce);
    this.options = "[]";
    this.withdrawType = withdrawType;
    this.walletSelected = walletSelected;
    this.amount = amount.toString();
    this.address = address;
    this.paymentId = paymentId;
  }
}
