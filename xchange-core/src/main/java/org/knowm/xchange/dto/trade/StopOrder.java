package org.knowm.xchange.dto.trade;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.lang3.ObjectUtils;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.instrument.Instrument;

/**
 * DTO representing a stop order
 * DTO 代表止损单
 *
 * <p>A stop order lets you set a minimum or maximum price before your trade will be treated by the
  exchange as a {@link MarketOrder} unless a limit price is also set. There is no guarantee that
  your conditions will be met on the exchange, so your order may not be executed.
 <p>止损单可让您设置最低或最高价格，然后您的交易将被
 除非还设置了限价，否则以 {@link MarketOrder} 的形式进行交易。 不能保证
 您的条件将在交易所得到满足，因此您的订单可能不会被执行。
 */
@JsonDeserialize(builder = StopOrder.Builder.class)
public class StopOrder extends Order implements Comparable<StopOrder> {

  private static final long serialVersionUID = -7341286101341375106L;

  public enum Intention {
    STOP_LOSS,
    TAKE_PROFIT
  }

  /** The stop price
   * 止损价 */
  protected final BigDecimal stopPrice;
  /**
   * The limit price this should be null if the stop order should be treated as a market order once the stop price is hit
   * 如果止损单被视为市价单，则限价应该为空
   */
  protected BigDecimal limitPrice = null;

  /** Some exchanges requires to define the goal of stop order
   * 一些交易所需要定义止损单的目标*/
  protected Intention intention = null;

  /** TrailValue is being used when the stop order is a trailing one
   * 当止损单是尾随单时，使用 TrailValue */
  protected BigDecimal trailValue = null;

  /**
   * @param type Either BID (buying) or ASK (selling)
   *             BID（买入）或 ASK（卖出）
   *
   * @param originalAmount The amount to trade
   *                       交易金额
   *
   * @param instrument The identifier (e.g. BTC/USD)
   *                   标识符（例如 BTC/USD）
   *
   * @param id An id (usually provided by the exchange)
   *           一个 id（通常由交易所提供）
   *
   * @param timestamp a Date object representing the order's timestamp according to the exchange's  server, null if not provided
   *                  一个 Date 对象，表示根据交易所服务器的订单时间戳，如果未提供，则为 null
   *
   * @param stopPrice In a BID this is the highest acceptable price, in an ASK this is the lowest  acceptable price
   *                  在 BID 中，这是可接受的最高价格，在 ASK 中，这是可接受的最低价格
   */
  public StopOrder(
      OrderType type,
      BigDecimal originalAmount,
      Instrument instrument,
      String id,
      Date timestamp,
      BigDecimal stopPrice) {

    super(type, originalAmount, instrument, id, timestamp);
    this.stopPrice = stopPrice;
  }

  /**
   * @param type Either BID (buying) or ASK (selling)
   *             BID（买入）或 ASK（卖出）
   *
   * @param originalAmount The amount to trade
   *                       交易金额
   *
   * @param cumulativeAmount The cumulative amount
   *                         累计金额
   *
   * @param instrument The identifier (e.g. BTC/USD)
   *                   标识符（例如 BTC/USD）
   *
   * @param id An id (usually provided by the exchange)
   *           一个 id（通常由交易所提供）
   *
   * @param timestamp a Date object representing the order's timestamp according to the exchange's   server, null if not provided
   *                  一个 Date 对象，表示根据交易所服务器的订单时间戳，如果未提供，则为 null
   *
   * @param stopPrice In a BID this is the highest acceptable price, in an ASK this is the lowest  acceptable price
   *                  在 BID 中，这是可接受的最高价格，在 ASK 中，这是可接受的最低价格
   */
  public StopOrder(
      OrderType type,
      BigDecimal originalAmount,
      BigDecimal cumulativeAmount,
      Instrument instrument,
      String id,
      Date timestamp,
      BigDecimal stopPrice) {

    super(
        type,
        originalAmount,
        instrument,
        id,
        timestamp,
        BigDecimal.ZERO,
        cumulativeAmount,
        BigDecimal.ZERO,
        OrderStatus.PENDING_NEW);
    this.stopPrice = stopPrice;
  }

  /**
   * @param type Either BID (buying) or ASK (selling)
   *             BID（买入）或 ASK（卖出）
   * @param originalAmount The amount to trade
   *                       交易金额
   *
   * @param instrument The identifier (e.g. BTC/USD)
   *                   标识符（例如 BTC/USD）
   *
   * @param id An id (usually provided by the exchange)
   *           一个 id（通常由交易所提供）
   *
   * @param timestamp a Date object representing the order's timestamp according to the exchange's  server, null if not provided
   *                  一个 Date 对象，表示根据交易所服务器的订单时间戳，如果未提供，则为 null
   *
   * @param stopPrice In a BID this is the highest acceptable price, in an ASK this is the lowest   acceptable price
   *                  在 BID 中，这是可接受的最高价格，在 ASK 中，这是可接受的最低价格
   *
   * @param averagePrice the weighted average price of any fills belonging to the order
   *                     属于订单的任何成交的加权平均价格
   *
   * @param cumulativeAmount the amount that has been filled
   *                         已填写的金额
   *
   * @param status the status of the order at the exchange or broker
   *               交易所或经纪人的订单状态
   */
  public StopOrder(
      OrderType type,
      BigDecimal originalAmount,
      Instrument instrument,
      String id,
      Date timestamp,
      BigDecimal stopPrice,
      BigDecimal averagePrice,
      BigDecimal cumulativeAmount,
      OrderStatus status) {

    super(
        type,
        originalAmount,
        instrument,
        id,
        timestamp,
        averagePrice,
        cumulativeAmount,
        BigDecimal.ZERO,
        status);
    this.stopPrice = stopPrice;
  }

  /**
   * @param type Either BID (buying) or ASK (selling)
   *             BID（买入）或 ASK（卖出）
   *
   * @param originalAmount The amount to trade
   *                       交易金额
   *
   * @param instrument The identifier (e.g. BTC/USD)
   *                   标识符（例如 BTC/USD）
   *
   * @param id An id (usually provided by the exchange)
   *           一个 id（通常由交易所提供）
   *
   * @param timestamp a Date object representing the order's timestamp according to the exchange's  server, null if not provided
   *                  一个 Date 对象，表示根据交易所服务器的订单时间戳，如果未提供，则为 null
   *
   * @param stopPrice In a BID this is the highest acceptable price, in an ASK this is the lowest    acceptable price
   *                  在 BID 中，这是可接受的最高价格，在 ASK 中，这是可接受的最低价格
   *
   * @param limitPrice The limit price the order should be placed at once the stopPrice has been hit   null for market
   *                   一旦 stopPrice 被击中为空，订单应下达的限价
   *
   * @param averagePrice the weighted average price of any fills belonging to the order
   *                     属于订单的任何成交的加权平均价格
   *
   * @param cumulativeAmount the amount that has been filled
   *                         已填写的金额
   *
   * @param status the status of the order at the exchange or broker
   *               交易所或经纪人的订单状态
   *
   */
  public StopOrder(
      OrderType type,
      BigDecimal originalAmount,
      Instrument instrument,
      String id,
      Date timestamp,
      BigDecimal stopPrice,
      BigDecimal limitPrice,
      BigDecimal averagePrice,
      BigDecimal cumulativeAmount,
      OrderStatus status) {

    this(
        type,
        originalAmount,
        instrument,
        id,
        timestamp,
        stopPrice,
        limitPrice,
        averagePrice,
        cumulativeAmount,
        BigDecimal.ZERO,
        status);
  }

  /**
   * @param type Either BID (buying) or ASK (selling)
   *             BID（买入）或 ASK（卖出）
   *
   * @param originalAmount The amount to trade
   *                       交易金额
   *
   * @param instrument The identifier (e.g. BTC/USD)
   *                   标识符（例如 BTC/USD）
   *
   * @param id An id (usually provided by the exchange)
   *           一个 id（通常由交易所提供）
   *
   * @param timestamp a Date object representing the order's timestamp according to the exchange's   server, null if not provided
   *                  一个 Date 对象，表示根据交易所服务器的订单时间戳，如果未提供，则为 null
   *
   * @param stopPrice In a BID this is the highest acceptable price, in an ASK this is the lowest   acceptable price
   *                  在 BID 中，这是可接受的最高价格，在 ASK 中，这是可接受的最低价格
   *
   * @param limitPrice The limit price the order should be placed at once the stopPrice has been hit  null for market
   *                   一旦 stopPrice 被击中为空，订单应下达的限价
   *
   * @param averagePrice the weighted average price of any fills belonging to the order
   *                     属于订单的任何成交的加权平均价格
   *
   * @param cumulativeAmount the amount that has been filled
   *                         已填写的金额
   *
   * @param fee the fee associated with this order
   *            与此订单相关的费用
   *
   * @param status the status of the order at the exchange or broker
   *               交易所或经纪人的订单状态
   */
  public StopOrder(
      OrderType type,
      BigDecimal originalAmount,
      Instrument instrument,
      String id,
      Date timestamp,
      BigDecimal stopPrice,
      BigDecimal limitPrice,
      BigDecimal averagePrice,
      BigDecimal cumulativeAmount,
      BigDecimal fee,
      OrderStatus status) {

    super(
        type,
        originalAmount,
        instrument,
        id,
        timestamp,
        averagePrice,
        cumulativeAmount,
        fee,
        status);
    this.stopPrice = stopPrice;
    this.limitPrice = limitPrice;
  }

  /**
   * @param type Either BID (buying) or ASK (selling)
   *             BID（买入）或 ASK（卖出）
   * @param originalAmount The amount to trade
   *                       交易金额
   *
   * @param instrument The identifier (e.g. BTC/USD)
   *                   标识符（例如 BTC/USD）
   *
   * @param id An id (usually provided by the exchange)
   *           一个 id（通常由交易所提供）
   *
   * @param timestamp a Date object representing the order's timestamp according to the exchange's  server, null if not provided
   *                  一个 Date 对象，表示根据交易所服务器的订单时间戳，如果未提供，则为 null
   *
   * @param stopPrice In a BID this is the highest acceptable price, in an ASK this is the lowest  acceptable price
   *                  在 BID 中，这是可接受的最高价格，在 ASK 中，这是可接受的最低价格
   *
   * @param limitPrice The limit price the order should be placed at once the stopPrice has been hit  null for market
   *                   一旦 stopPrice 被击中为空，订单应下达的限价
   *
   * @param averagePrice the weighted average price of any fills belonging to the order
   *                     属于订单的任何成交的加权平均价格
   *
   * @param cumulativeAmount 已填写的金额
   *
   * @param status the status of the order at the exchange or broker
   *               交易所或经纪人的订单状态
   */
  public StopOrder(
      OrderType type,
      BigDecimal originalAmount,
      Instrument instrument,
      String id,
      Date timestamp,
      BigDecimal stopPrice,
      BigDecimal limitPrice,
      BigDecimal averagePrice,
      BigDecimal cumulativeAmount,
      BigDecimal fee,
      OrderStatus status,
      String userReference,
      Intention intention,
      BigDecimal trailValue) {

    super(
        type,
        originalAmount,
        instrument,
        id,
        timestamp,
        averagePrice,
        cumulativeAmount,
        fee,
        status,
        userReference);
    this.stopPrice = stopPrice;
    this.limitPrice = limitPrice;
    this.intention = intention;
    this.trailValue = trailValue;
  }

  /** @return The stop price
   * 止损价 */
  public BigDecimal getStopPrice() {

    return stopPrice;
  }

  /** @return The limit price
   * 限价 */
  public BigDecimal getLimitPrice() {

    return limitPrice;
  }

  /** @return The order intention
   * 订货意向*/
  public Intention getIntention() {
    return intention;
  }

  public BigDecimal getTrailValue() {
    return trailValue;
  }

  @Override
  public String toString() {
    return "StopOrder{" +
            "stopPrice=" + stopPrice +
            ", limitPrice=" + limitPrice +
            ", intention=" + intention +
            ", trailValue=" + trailValue +
            '}';
  }

  @Override
  public int compareTo(StopOrder stopOrder) {

    if (this.getType() != stopOrder.getType()) {
      return this.getType() == OrderType.BID ? -1 : 1;
    }
    if (this.getStopPrice().compareTo(stopOrder.getStopPrice()) != 0) {
      return this.getType() == OrderType.BID ? -1 : 1;
    }
    return ObjectUtils.compare(this.getIntention(), stopOrder.getIntention());
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof StopOrder)) {
      return false;
    }
    if (!super.equals(obj)) {
      return false;
    }

    StopOrder stopOrder = (StopOrder) obj;

    if (!Objects.equals(stopPrice, stopOrder.stopPrice)) {
      return false;
    }
    if (!Objects.equals(limitPrice, stopOrder.limitPrice)) {
      return false;
    }
    if (!Objects.equals(intention, stopOrder.intention)) {
      return false;
    }
    if (!Objects.equals(trailValue, stopOrder.trailValue)) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + stopPrice.hashCode();
    result = 31 * result + (limitPrice != null ? limitPrice.hashCode() : 0);
    result = 31 * result + (intention != null ? intention.hashCode() : 0);
    result = 31 * result + (trailValue != null ? trailValue.hashCode() : 0);
    return result;
  }

  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder extends Order.Builder {

    protected BigDecimal stopPrice;

    protected BigDecimal limitPrice;

    protected Intention intention;

    protected BigDecimal trailValue;

    @JsonCreator
    public Builder(
        @JsonProperty("orderType") OrderType orderType,
        @JsonProperty("instrument") Instrument instrument) {

      super(orderType, instrument);
    }

    public static Builder from(Order order) {

      Builder builder =
          new Builder(order.getType(), order.getInstrument())
              .originalAmount(order.getOriginalAmount())
              .cumulativeAmount(order.getCumulativeAmount())
              .timestamp(order.getTimestamp())
              .id(order.getId())
              .flags(order.getOrderFlags())
              .orderStatus(order.getStatus())
              .fee(order.getFee())
              .averagePrice(order.getAveragePrice())
              .userReference(order.getUserReference());
      if (order instanceof StopOrder) {
        StopOrder stopOrder = (StopOrder) order;
        builder.stopPrice(stopOrder.getStopPrice());
        builder.limitPrice(stopOrder.getLimitPrice());
        builder.intention(stopOrder.getIntention());
        builder.trailValue(stopOrder.getTrailValue());
      }
      return builder;
    }

    @Override
    public Builder orderType(OrderType orderType) {

      return (Builder) super.orderType(orderType);
    }

    @Override
    public Builder originalAmount(BigDecimal originalAmount) {

      return (Builder) super.originalAmount(originalAmount);
    }

    @Override
    public Builder cumulativeAmount(BigDecimal originalAmount) {

      return (Builder) super.cumulativeAmount(originalAmount);
    }

    @Override
    public Builder fee(BigDecimal fee) {

      return (Builder) super.fee(fee);
    }

    @Override
    public Builder remainingAmount(BigDecimal remainingAmount) {

      return (Builder) super.remainingAmount(remainingAmount);
    }

    @Override
    @Deprecated
    public Builder currencyPair(CurrencyPair currencyPair) {

      return (Builder) super.currencyPair(currencyPair);
    }

    @Override
    public Builder instrument(Instrument instrument) {

      return (Builder) super.instrument(instrument);
    }

    @Override
    public Builder id(String id) {

      return (Builder) super.id(id);
    }

    @Override
    public Builder userReference(String userReference) {

      return (Builder) super.userReference(userReference);
    }

    @Override
    public Builder timestamp(Date timestamp) {

      return (Builder) super.timestamp(timestamp);
    }

    @Override
    public Builder orderStatus(OrderStatus status) {

      return (Builder) super.orderStatus(status);
    }

    @Override
    public Builder averagePrice(BigDecimal averagePrice) {

      return (Builder) super.averagePrice(averagePrice);
    }

    @Override
    public Builder flag(IOrderFlags flag) {

      return (Builder) super.flag(flag);
    }

    @Override
    public Builder flags(Set<IOrderFlags> flags) {

      return (Builder) super.flags(flags);
    }

    public Builder stopPrice(BigDecimal stopPrice) {

      this.stopPrice = stopPrice;
      return this;
    }

    public Builder limitPrice(BigDecimal limitPrice) {

      this.limitPrice = limitPrice;
      return this;
    }

    public Builder intention(Intention intention) {

      this.intention = intention;
      return this;
    }

    public Builder trailValue(BigDecimal trailValue) {

      this.trailValue = trailValue;
      return this;
    }

    @Override
    public StopOrder build() {

      StopOrder order =
          new StopOrder(
              orderType,
              originalAmount,
              instrument,
              id,
              timestamp,
              stopPrice,
              limitPrice,
              averagePrice,
              originalAmount == null || remainingAmount == null
                  ? cumulativeAmount
                  : originalAmount.subtract(remainingAmount),
              fee,
              status,
              userReference,
              intention,
              trailValue);

      order.setOrderFlags(flags);
      order.setLeverage(leverage);
      return order;
    }
  }
}
