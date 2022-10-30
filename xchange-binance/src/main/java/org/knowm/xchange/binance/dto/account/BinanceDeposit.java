package org.knowm.xchange.binance.dto.account;

import java.math.BigDecimal;
import lombok.Data;

/**
 * Binance存款
 */
@Data
public final class BinanceDeposit {
  /**
   * 数量
   */
  private BigDecimal amount;
  /**
   * 币/钱币
   */
  private String coin;
  /**
   * 网络
   */
  private String network;
  /**
   * 状态
   *  (0:pending,1:success)
   * （0：待定，1：成功）
   * */
  private int status;

  /**
   * 地址
   */
  private String address;
  /**
   * 地址标签
   */
  private String addressTag;
  /**
   * txId
   */
  private String txId;
  /**
   * 插入时间
   */
  private long insertTime;
  /**
   * 转账类型
   * 1表示内部转移，0表示外部转移
   */
  private int transferType; // 1 for internal transfer, 0 for external transfer
  /**
   * 确认时间
   */
  private String confirmTimes;
}
