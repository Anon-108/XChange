package org.knowm.xchange.binance.dto.trade;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * 订单类型
 */
public enum OrderType {
  /** 限度，限制/额度 */
  LIMIT,
  /** 集市，市场；交易 */
  MARKET,
  /** 获利限制/额度 */
  TAKE_PROFIT_LIMIT,
  /** 止损限制/限额 */
  STOP_LOSS_LIMIT,
  /** 止损 */
  STOP_LOSS,
  /** 获利 */
  TAKE_PROFIT,
  /** 额度/限制制造者 /制造商 */
  LIMIT_MAKER;

  @JsonCreator
  public static OrderType getOrderType(String s) {
    try {
      return OrderType.valueOf(s);
    } catch (Exception e) {
      throw new RuntimeException("Unknown order type 未知订单类型 " + s + ".");
    }
  }
}
