package org.knowm.xchange.binance.dto.account;

import java.math.BigDecimal;
import lombok.Data;

/**
 * 币安 提，取（银行账户中的钱款）
 */
@Data
public final class BinanceWithdraw {
  /**
   * 地址
   */
  private String address;
  /**
   * 地址标签
   */
  private String addressTag;
  /**
   * 数量
   */
  private BigDecimal amount;
  /**
   * 应用时间
   */
  private String applyTime;
  /**
   * 币
   */
  private String coin;
  private String id;
  /**
   *  提，取（银行账户中的钱款） 订单id
   */
  private String withdrawOrderId;
  /**
   * 网络
   */
  private String network;
  /**
   * 转账类型
   * 1表示内部转移，0表示外部转移
   */
  private int transferType; // 1 for internal transfer, 0 for external transfer
  /**
   * 状态
   * (0:Email Sent,1:Cancelled 2:Awaiting Approval 3:Rejected 4:Processing 5:Failure 6Completed)
   * (0:邮件已发送，1:取消 2:等待批准 3:拒绝 4:处理中 5:失败 6完成)
   * */
  private int status;
  /**
   * 交易手续费
   */
  private BigDecimal transactionFee;
  private String txId;
}
