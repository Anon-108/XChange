package org.knowm.xchange.service.trade.params.orders;

import org.knowm.xchange.service.trade.TradeService;

/**
 * Root interface for all interfaces used as a parameter type for {@link
  TradeService#getOrder(org.knowm.xchange.service.trade.params.orders.OrderQueryParams...)}.
  Exchanges should have their own implementation of this interface if querying an order requires information additional to orderId
 用作 {@link 的参数类型的所有接口的根接口
TradeService#getOrder(org.knowm.xchange.service.trade.params.orders.OrderQueryParams...)}。
 如果查询订单需要 orderId 以外的信息，则交易所应该有自己的此接口实现
 *
 * <ul>
 *   <li>{@link OpenOrdersParamCurrencyPair}.
 * </ul>
 */
public interface OrderQueryParams {
  String getOrderId();

  /** Sets the orderId
   * 设置 orderId*/
  void setOrderId(String orderId);
}
