package org.knowm.xchange.binance.dto.trade;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

/**
 * 币安 Dribblet/少量:小额?运球?? 详细信息
 */
public final class BinanceDribbletDetails {
  /**
   * 交易id
   */
  private final String transId;
  /**
   * 服务收费金额
   */
  private final BigDecimal serviceChargeAmount;
  /**
   * 数量/金额
   */
  private final BigDecimal amount;
  /**
   * 营业时间/操作时间
   */
  private final Long operateTime;
  /**
   * 转账/转移金额
   */
  private final BigDecimal transferedAmount;
  /**
   * 从……起；从……开始 资产
   */
  private final String fromAsset;

  /**
   * 币安 Dribblet/少量:小额?运球?? 详细信息
   * @param transId 交易id
   * @param serviceChargeAmount 服务收费金额
   * @param amount 数量/金额
   * @param operateTime 营业时间/操作时间
   * @param transferedAmount 转账/转移金额
   * @param fromAsset 从……起；从……开始 资产
   */
  public BinanceDribbletDetails(
      @JsonProperty("transId") String transId,
      @JsonProperty("serviceChargeAmount") BigDecimal serviceChargeAmount,
      @JsonProperty("amount") BigDecimal amount,
      @JsonProperty("operateTime") Long operateTime,
      @JsonProperty("transferedAmount") BigDecimal transferedAmount,
      @JsonProperty("fromAsset") String fromAsset) {
    this.transId = transId;
    this.serviceChargeAmount = serviceChargeAmount;
    this.amount = amount;
    this.operateTime = operateTime;
    this.transferedAmount = transferedAmount;
    this.fromAsset = fromAsset;
  }

  public String getTransId() {
    return transId;
  }

  public BigDecimal getServiceChargeAmount() {
    return serviceChargeAmount;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public Long getOperateTime() {
    return operateTime;
  }

  public BigDecimal getTransferedAmount() {
    return transferedAmount;
  }

  public String getFromAsset() {
    return fromAsset;
  }
}
