package org.knowm.xchange.binance.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

/**
 * 资产详情
 */
public class AssetDetail {
  /**
   * 最小提款金额
   */
  private final String minWithdrawAmount;
  /**
   * 存款状态
   */
  private final boolean depositStatus;
  /**
   * 提款费
   */
  private final BigDecimal withdrawFee;
  /**
   * 提取状态
   */
  private final boolean withdrawStatus;

  /**
   * 资产详情
   * @param minWithdrawAmount 最小提款金额
   * @param depositStatus 存款状态
   * @param withdrawFee 提款费
   * @param withdrawStatus 提取状态
   */
  public AssetDetail(
      @JsonProperty("minWithdrawAmount") String minWithdrawAmount,
      @JsonProperty("depositStatus") boolean depositStatus,
      @JsonProperty("withdrawFee") BigDecimal withdrawFee,
      @JsonProperty("withdrawStatus") boolean withdrawStatus) {
    this.minWithdrawAmount = minWithdrawAmount;
    this.depositStatus = depositStatus;
    this.withdrawFee = withdrawFee;
    this.withdrawStatus = withdrawStatus;
  }

  public String getMinWithdrawAmount() {
    return minWithdrawAmount;
  }

  public boolean isDepositStatus() {
    return depositStatus;
  }

  public BigDecimal getWithdrawFee() {
    return withdrawFee;
  }

  public boolean isWithdrawStatus() {
    return withdrawStatus;
  }

  @Override
  public String toString() {
    return "AssetDetail{"
        + "minWithdrawAmount = '"
        + minWithdrawAmount
        + '\''
        + ",depositStatus = '"
        + depositStatus
        + '\''
        + ",withdrawFee = '"
        + withdrawFee
        + '\''
        + ",withdrawStatus = '"
        + withdrawStatus
        + '\''
        + "}";
  }
}
