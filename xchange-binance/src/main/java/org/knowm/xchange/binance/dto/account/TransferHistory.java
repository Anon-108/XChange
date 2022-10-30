package org.knowm.xchange.binance.dto.account;

import java.math.BigDecimal;
import lombok.Data;

/**
 * 转移历史
 */
@Data
public final class TransferHistory {
  /**
   * 从……开始
   */
  private String from;
  /**
   * 朝……到某处
   */
  private String to;
  /**
   * 资产
   */
  private String asset;
  /**
   * 数量
   */
  private BigDecimal qty;
  /**
   * 状态
   */
  private String status;
  /**
   * 时间
   */
  private long time;
}
