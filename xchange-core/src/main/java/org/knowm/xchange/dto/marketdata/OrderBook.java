package org.knowm.xchange.dto.marketdata;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order.OrderType;
import org.knowm.xchange.dto.trade.LimitOrder;

/** DTO representing the exchange order book
 * 代表交换订单簿的 DTO*/
public final class OrderBook implements Serializable {

  private static final long serialVersionUID = -7788306758114464314L;

  /** the asks  */
  private final List<LimitOrder> asks;
  /** the bids */
  private final List<LimitOrder> bids;
  /** the timestamp of the orderbook according to the exchange's server, null if not provided
   * 根据交易所服务器的订单簿时间戳，如果没有提供则为空*/
  private Date timeStamp;

  /**
   * Constructor
   *
   * @param timeStamp - the timestamp of the orderbook according to the exchange's server, null if  not provided
   *                  - 根据交易所服务器的订单簿时间戳，如果没有提供则为空
   *
   * @param asks The ASK orders
   *             ASK 订单
   *
   * @param bids The BID orders
   *             投标订单
   */
  @JsonCreator
  public OrderBook(
      @JsonProperty("timeStamp") Date timeStamp,
      @JsonProperty("asks") List<LimitOrder> asks,
      @JsonProperty("bids") List<LimitOrder> bids) {

    this(timeStamp, asks, bids, false);
  }

  /**
   * Constructor
   *
   * @param timeStamp - the timestamp of the orderbook according to the exchange's server, null if  not provided
   *                  - 根据交易所服务器的订单簿时间戳，如果没有提供则为空
   *
   * @param asks The ASK orders
   *             ASK 订单
   *
   * @param bids The BID orders
   *             投标订单
   *
   * @param sort True if the asks and bids need to be sorted
   *             如果需要对询价和出价进行排序，则为真
   */
  public OrderBook(Date timeStamp, List<LimitOrder> asks, List<LimitOrder> bids, boolean sort) {

    this.timeStamp = timeStamp;
    if (sort) {
      this.asks = new ArrayList<>(asks);
      this.bids = new ArrayList<>(bids);
      Collections.sort(this.asks);
      Collections.sort(this.bids);
    } else {
      this.asks = asks;
      this.bids = bids;
    }
  }

  /**
   * Constructor
   *
   * @param timeStamp - the timestamp of the orderbook according to the exchange's server, null if  not provided
   *                  - 根据交易所服务器的订单簿时间戳，如果没有提供则为空
   *
   * @param asks The ASK orders
   *             ASK 订单
   *
   * @param bids The BID orders
   *             投标订单
   */
  public OrderBook(Date timeStamp, Stream<LimitOrder> asks, Stream<LimitOrder> bids) {

    this(timeStamp, asks, bids, false);
  }

  /**
   * Constructor
   *
   * @param timeStamp - the timestamp of the orderbook according to the exchange's server, null if   not provided
   *                  - 根据交易所服务器的订单簿时间戳，如果没有提供则为空
   *
   * @param asks The ASK orders
   *             ASK 订单
   *
   * @param bids The BID orders
   *             投标订单
   *
   * @param sort True if the asks and bids need to be sorted
   *             如果需要对询价和出价进行排序，则为真
   */
  public OrderBook(Date timeStamp, Stream<LimitOrder> asks, Stream<LimitOrder> bids, boolean sort) {

    this.timeStamp = timeStamp;
    if (sort) {
      this.asks = asks.sorted().collect(Collectors.toList());
      this.bids = bids.sorted().collect(Collectors.toList());
    } else {
      this.asks = asks.collect(Collectors.toList());
      this.bids = bids.collect(Collectors.toList());
    }
  }

  // Returns a copy of limitOrder with tradeableAmount replaced.
  // 返回 limitOrder 的副本，替换为 tradeableAmount。
  private static LimitOrder withAmount(LimitOrder limitOrder, BigDecimal tradeableAmount) {

    OrderType type = limitOrder.getType();
    CurrencyPair currencyPair = limitOrder.getCurrencyPair();
    String id = limitOrder.getId();
    Date date = limitOrder.getTimestamp();
    BigDecimal limit = limitOrder.getLimitPrice();
    return new LimitOrder(type, tradeableAmount, currencyPair, id, date, limit);
  }

  public Date getTimeStamp() {

    return timeStamp;
  }

  public List<LimitOrder> getAsks() {

    return asks;
  }

  public List<LimitOrder> getBids() {

    return bids;
  }

  public List<LimitOrder> getOrders(OrderType type) {

    return type == OrderType.ASK ? asks : bids;
  }

  /**
   * Given a new LimitOrder, it will replace a matching limit order in the orderbook if one is
    found, or add the new LimitOrder if one is not. timeStamp will be updated if the new timestamp is non-null and in the future.
   给定一个新的 LimitOrder，它将替换订单簿中匹配的限价单，如果
   找到，如果没有找到，则添加新的 LimitOrder。 如果新的时间戳是非空的并且在未来，timeStamp 将被更新。
   *
   * @param limitOrder the new LimitOrder
   *                   新的限价单
   */
  public void update(LimitOrder limitOrder) {

    update(getOrders(limitOrder.getType()), limitOrder);
    updateDate(limitOrder.getTimestamp());
  }

  // Replace the amount for limitOrder's price in the provided list.
  // 替换提供列表中limitOrder 价格的金额。
  private void update(List<LimitOrder> asks, LimitOrder limitOrder) {

    int idx = Collections.binarySearch(asks, limitOrder);
    if (idx >= 0) {
      asks.remove(idx);
    } else {
      idx = -idx - 1;
    }

    if (limitOrder.getRemainingAmount().compareTo(BigDecimal.ZERO) != 0) {
      asks.add(idx, limitOrder);
    }
  }

  /**
   * Given an OrderBookUpdate, it will replace a matching limit order in the orderbook if one is
    found, or add a new if one is not. timeStamp will be updated if the new timestamp is non-null  and in the future.
   给定一个 OrderBookUpdate，它将替换订单簿中匹配的限价单，如果
   找到，如果没有找到，则添加新的。 如果新的时间戳是非空的并且在未来，timeStamp 将被更新。
   *
   * @param orderBookUpdate the new OrderBookUpdate
   *                        新的 OrderBookUpdate
   */
  public void update(OrderBookUpdate orderBookUpdate) {

    LimitOrder limitOrder = orderBookUpdate.getLimitOrder();
    List<LimitOrder> limitOrders = getOrders(limitOrder.getType());
    int idx = Collections.binarySearch(limitOrders, limitOrder);
    if (idx >= 0) {
      limitOrders.remove(idx);
    } else {
      idx = -idx - 1;
    }

    if (orderBookUpdate.getTotalVolume().compareTo(BigDecimal.ZERO) != 0) {
      LimitOrder updatedOrder = withAmount(limitOrder, orderBookUpdate.getTotalVolume());
      limitOrders.add(idx, updatedOrder);
    }

    updateDate(limitOrder.getTimestamp());
  }

  // Replace timeStamp if the provided date is non-null and in the future
  // 如果提供的日期不为空且在未来，则替换 timeStamp
  // TODO should this raise an exception if the order timestamp is in the past?
  //如果订单时间戳是过去的，这是否会引发异常？
  private void updateDate(Date updateDate) {

    if (updateDate != null && (timeStamp == null || updateDate.after(timeStamp))) {
      this.timeStamp = updateDate;
    }
  }

  @Override
  public int hashCode() {

    int hash = 17;
    hash = 31 * hash + (this.timeStamp != null ? this.timeStamp.hashCode() : 0);
    for (LimitOrder order : this.bids) {
      hash = 31 * hash + order.hashCode();
    }
    for (LimitOrder order : this.asks) {
      hash = 31 * hash + order.hashCode();
    }
    return hash;
  }

  @Override
  public boolean equals(Object obj) {

    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final OrderBook other = (OrderBook) obj;
    if (this.timeStamp == null
        ? other.timeStamp != null
        : !this.timeStamp.equals(other.timeStamp)) {
      return false;
    }
    if (this.bids.size() != other.bids.size()) {
      return false;
    }
    for (int index = 0; index < this.bids.size(); index++) {
      if (!this.bids.get(index).equals(other.bids.get(index))) {
        return false;
      }
    }
    if (this.asks.size() != other.asks.size()) {
      return false;
    }
    for (int index = 0; index < this.asks.size(); index++) {
      if (!this.asks.get(index).equals(other.asks.get(index))) {
        return false;
      }
    }
    return true;
  }

  /**
   * Identical to {@link #equals(Object) equals} method except that this ignores different
    timestamps. In other words, this version of equals returns true if the order internal to the
    OrderBooks are equal but their timestamps are unequal. It returns false if any order between the two are different.
   与 {@link #equals(Object) equals} 方法相同，只是忽略了不同的
   时间戳。 换句话说，这个版本的 equals 如果订单内部的订单返回 true
   OrderBook 是相等的，但它们的时间戳不相等。 如果两者之间的任何顺序不同，则返回 false。
   *
   * @param ob
   * @return
   */
  public boolean ordersEqual(OrderBook ob) {

    if (ob == null) {
      return false;
    }

    Date timestamp = new Date();
    OrderBook thisOb = new OrderBook(timestamp, this.getAsks(), this.getBids());
    OrderBook thatOb = new OrderBook(timestamp, ob.getAsks(), ob.getBids());
    return thisOb.equals(thatOb);
  }

  @Override
  public String toString() {

    return "OrderBook [timestamp: "
        + timeStamp
        + ", asks="
        + asks.toString()
        + ", bids="
        + bids.toString()
        + "]";
  }
}
