package org.knowm.xchange.service.trade.params.orders;

import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.service.trade.TradeService;

/**
 * Root interface for all interfaces used as a parameter type for {@link  TradeService#getOpenOrders(OpenOrdersParams)}. Each exchange should have its own class
  implementing at least one from following available interfaces:
 用作 {@link TradeService#getOpenOrders(OpenOrdersParams)} 参数类型的所有接口的根接口。 每个交易所都应该有自己的类
 至少实现以下可用接口之一：
 *
 * <ul>
 *   <li>{@link OpenOrdersParamCurrencyPair}.
 * </ul>
 *
 * When suitable exchange params definition can extend from default classes, eg. {@link DefaultOpenOrdersParamCurrencyPair}.
 * * 当合适的交换参数定义可以从默认类扩展时，例如。 {@link DefaultOpenOrdersParamCurrencyPair}。
 */
public interface OpenOrdersParams {
  /**
   * Checks if passed order is suitable for open orders params. May be used for XChange side orders filtering
   * * 检查通过的订单是否适用于未结订单参数。 可用于 XChange 侧单过滤
   *
   * @param order The order to filter.
   *              要过滤的顺序。
   *
   * @return true if order is ok
   * * @return 如果订单正常则返回 true
   */
  boolean accept(LimitOrder order);

  /**
   * Added later, this method allows the filter to also to apply to stop orders, at a small cost. It
    should be explicitly implemented for better performance.
   稍后添加，此方法允许过滤器也以较低的成本应用于止损单。 它
   应该明确实现以获得更好的性能。
   *
   * @param order The order to filter.
   *              要过滤的顺序。
   *
   * @return true if order is ok.
   *        如果订单正常，则为 true。
   */
  default boolean accept(Order order) {
    return accept(LimitOrder.Builder.from(order).build());
  }
}
