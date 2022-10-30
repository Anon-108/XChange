package org.knowm.xchange.binance.dto.trade;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.List;

/**
 * 少量,小额?运球?
 */
public final class BinanceDribblets {
  /**
   * 操作时间
   */
  private final Long operateTime;
  /**
   * 总转账金额
   */
  private final BigDecimal totalTransferedAmount;
  /**
   * 服务费总额
   */
  private final BigDecimal totalServiceChargeAmount;
  /**
   * 交易id
   */
  private final String transId;
  /**
   * binance 少量,小额?运球? 细节
   */
  private final List<BinanceDribbletDetails> binanceDribbletDetails;

  /**
   * binance 少量,小额?运球? 细节
   * @param operateTime 操作时间
   * @param totalTransferedAmount 总转账金额
   * @param totalServiceChargeAmount 服务费总额
   * @param transId 交易id
   * @param binanceDribbletDetails binance 少量,小额?运球? 细节
   */
  public BinanceDribblets(
      @JsonProperty("operateTime") Long operateTime,
      @JsonProperty("totalTransferedAmount") BigDecimal totalTransferedAmount,
      @JsonProperty("totalServiceChargeAmount") BigDecimal totalServiceChargeAmount,
      @JsonProperty("transId") String transId,
      @JsonProperty("userAssetDribbletDetails")
          List<BinanceDribbletDetails> binanceDribbletDetails) {
    this.operateTime = operateTime;
    this.totalTransferedAmount = totalTransferedAmount;
    this.totalServiceChargeAmount = totalServiceChargeAmount;
    this.transId = transId;
    this.binanceDribbletDetails = binanceDribbletDetails;
  }

  public Long getOperateTime() {
    return operateTime;
  }

  public BigDecimal getTotalTransferedAmount() {
    return totalTransferedAmount;
  }

  public BigDecimal getTotalServiceChargeAmount() {
    return totalServiceChargeAmount;
  }

  public String getTransId() {
    return transId;
  }

  public List<BinanceDribbletDetails> getBinanceDribbletDetails() {
    return binanceDribbletDetails;
  }
}
