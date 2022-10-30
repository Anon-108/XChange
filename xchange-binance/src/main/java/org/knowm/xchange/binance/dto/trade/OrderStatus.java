package org.knowm.xchange.binance.dto.trade;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * 订单状态
 */
public enum OrderStatus {
  /** 新 */
  NEW,
  /** 部分填充 */
  PARTIALLY_FILLED,
  /** 填充 */
  FILLED,
  /** /** 新 */
  CANCELED,
  /** 待取消 */
  PENDING_CANCEL,
  /** 拒绝，驳回 */
  REJECTED,
  /** 过期的；失效的 */
  EXPIRED;

  @JsonCreator
  public static OrderStatus getOrderStatus(String s) {
    try {
      return OrderStatus.valueOf(s);
    } catch (Exception e) {
      throw new RuntimeException("Unknown order status " + s + ".");
    }
  }
}
