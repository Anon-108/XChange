package org.knowm.xchange.bitfinex.v1.dto.trade;

import org.knowm.xchange.dto.Order.IOrderFlags;

public enum BitfinexOrderFlags implements IOrderFlags {

  /**
   * This type of order is a limit order that must be filled in its entirety or cancelled (killed).
   * * 这种类型的订单是限价订单，必须全部成交或取消（杀死）。
   */
  FILL_OR_KILL,

  /**
   * This is an order which does not appear in the orderbook, and thus doesn't influence other market participants. the taker fee will apply to any trades.
   * * 此订单未出现在订单簿中，因此不会影响其他市场参与者。 接受者费用将适用于任何交易。
   */
  HIDDEN,

  /** These are orders that allow you to be sure to always pay the maker fee.  这些订单可让您确保始终支付制造商费用。*/
  POST_ONLY,

  /**
   * For order amends indicates that the new order should use the remaining amount of the original order.
   * For order amends 表示新订单应使用原订单的剩余金额。
   */
  USE_REMAINING,

  /**
   * This type of order a margin order that is leveraged in line with bitfinex current leverage rates.
   * 这种类型的订单是按照 bitfinex 当前杠杆率进行杠杆化的保证金订单。
   */
  MARGIN,

  /** Trailing stop order
   * 追踪止损单 */
  TRAILING_STOP,

  /** Stop order 止损单 */
  STOP
}
