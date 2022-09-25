package org.knowm.xchange.dto.trade;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.knowm.xchange.dto.Order;

/**
 * DTO representing open orders
 * DTO 代表未结订单
 *
 * <p>Open orders are orders that you have placed with the exchange that have not yet been matched to a counter-party.
 * * <p>未结订单是您在交易所下达但尚未匹配到交易对手的订单。
 */
public final class OpenOrders implements Serializable {

  private static final long serialVersionUID = 6641558609478576563L;

  private final List<LimitOrder> openOrders;
  private final List<? extends Order> hiddenOrders;

  /**
   * Constructor
   *
   * @param openOrders The list of open orders
   *                   未结订单列表
   */
  public OpenOrders(List<LimitOrder> openOrders) {
    this.openOrders = openOrders;
    this.hiddenOrders = Collections.emptyList();
  }

  /**
   * Constructor
   *
   * @param openOrders The list of open orders
   *                   未结订单列表
   *
   * @param hiddenOrders The list of orders which are active but hidden from the order book.
   *                     活跃但隐藏在订单簿中的订单列表。
   */
  @JsonCreator
  public OpenOrders(
      @JsonProperty("openOrders") List<LimitOrder> openOrders,
      @JsonProperty("hiddenOrders") List<Order> hiddenOrders) {
    this.openOrders = openOrders;
    this.hiddenOrders = hiddenOrders;
  }

  /** @return LimitOrders which are shown on the order book.
   * 订单簿上显示的 LimitOrders。 */
  public List<LimitOrder> getOpenOrders() {
    return openOrders;
  }

  /** @return All Orders which are shown on the order book.
   * 订单簿上显示的所有订单。*/
  public List<Order> getAllOpenOrders() {
    List<Order> allOpenOrders = new ArrayList<>(openOrders);
    allOpenOrders.addAll(hiddenOrders);
    return allOpenOrders;
  }

  /** @return Orders which are not shown on the order book, such as untriggered stop orders.
   * 未显示在订单簿上的订单，例如未触发的止损订单。*/
  public List<? extends Order> getHiddenOrders() {
    return hiddenOrders;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    addToToString(sb, getOpenOrders(), "Open orders");
    addToToString(sb, getHiddenOrders(), "Hidden orders");
    return sb.toString();
  }

  private void addToToString(StringBuilder sb, List<? extends Order> orders, String description) {
    sb.append(description);
    sb.append(": ");
    if (orders.isEmpty()) {
      sb.append("None\n");
    } else {
      sb.append("\n");
      for (Order order : orders) {
        sb.append("  [order=");
        sb.append(order.toString());
        sb.append("]\n");
      }
    }
  }
}
