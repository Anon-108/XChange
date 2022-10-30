package org.knowm.xchange.binance.dto.account;

import java.math.BigDecimal;
import lombok.Data;

/**
 * 转移子用户历史记录
 */
@Data
public final class TransferSubUserHistory {
  /**
   * 对方；合约对方；交易对方
   */
  private String counterParty;
  /**
   * 邮箱
   */
  private String email;
  /**
   *类型
   */
  private Integer type;
  /**
   * 资产
   */
  private String asset;
  /**
   * 数量
   */
  private BigDecimal qty;
  /**
   * 时间
   */
  private long time;
}
