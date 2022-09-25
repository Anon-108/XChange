package org.knowm.xchange.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.MarketOrder;
import org.knowm.xchange.dto.trade.StopOrder;
import org.knowm.xchange.instrument.Instrument;

/** Data object representing an order
 * 表示订单的数据对象*/
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "trigger")
@JsonSubTypes({
  @JsonSubTypes.Type(value = LimitOrder.class, name = "limit"),
  @JsonSubTypes.Type(value = StopOrder.class, name = "stop"),
  @JsonSubTypes.Type(value = MarketOrder.class, name = "market")
})
public abstract class Order implements Serializable {

  private static final long serialVersionUID = -8132103343647993249L;
  private static final Random random = new Random();

  /** Order type i.e. bid or ask
   * 订单类型，即出价或询价*/
  private final OrderType type;
  /** Amount to be ordered / amount that was ordered
   * 订购数量/订购数量 */
  private final BigDecimal originalAmount;
  /** The instrument could be a currency pair of derivative
   * 该工具可以是衍生品的货币对*/
  private final Instrument instrument;
  /** An identifier set by the exchange that uniquely identifies the order
   * 交易所设置的唯一标识订单的标识符 */
  private final String id;
  /** An identifier provided by the user on placement that uniquely identifies the order
   * 用户在放置时提供的唯一标识订单的标识符*/
  private final String userReference;
  /** The timestamp on the order according to the exchange's server, null if not provided
   * 根据交易所服务器的订单时间戳，如果没有提供则为空*/
  private final Date timestamp;
  /** Any applicable order flags
   * 任何适用的订单标志 */
  private final Set<IOrderFlags> orderFlags = new HashSet<>();
  /** Status of order during it lifecycle
   * 生命周期内的订单状态 */
  private OrderStatus status;
  /** Amount to be ordered / amount that has been matched against order on the order book/filled
   * 订单金额/已与订单簿上的订单匹配的金额/已填写*/
  private BigDecimal cumulativeAmount;
  /** Weighted Average price of the fills in the order
   * 订单中的加权平均价格 */
  private BigDecimal averagePrice;
  /** The total of the fees incurred for all transactions related to this order
   * 与此订单相关的所有交易产生的费用总额 */
  private BigDecimal fee;
  /** The leverage to use for margin related to this order
   * 用于与此订单相关的保证金的杠杆 */
  private String leverage = null;

  /**
   * @param type Either BID (buying) or ASK (selling)
   *             BID（买入）或 ASK（卖出）
   *
   * @param originalAmount The amount to trade
   *                       交易金额
   *
   * @param instrument instrument The identifier (e.g. BTC/USD)
   *                   工具 标识符（例如 BTC/USD）
   *
   * @param id An id (usually provided by the exchange)
   *           一个 id（通常由交易所提供）
   *
   * @param timestamp the absolute time for this order according to the exchange's server, null if  not provided
   *                  此订单的绝对时间根据交易所的服务器，如果没有提供，则为空
   */
  public Order(
      OrderType type, BigDecimal originalAmount, Instrument instrument, String id, Date timestamp) {
    this(type, originalAmount, instrument, id, timestamp, null, null, null, null);
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
   * @param timestamp the absolute time for this order according to the exchange's server, null if  not provided
   *                  此订单的绝对时间根据交易所的服务器，如果没有提供，则为空
   *
   * @param averagePrice the averagePrice of fill belonging to the order
   *                     属于订单的平均成交价
   *
   * @param cumulativeAmount the amount that has been filled
   *                         已填写的金额
   *
   * @param fee the fee associated with this order
   *            与此订单相关的费用
   *
   * @param status the status of the order at the exchange
   *               交易所的订单状态
   */
  public Order(
      OrderType type,
      BigDecimal originalAmount,
      Instrument instrument,
      String id,
      Date timestamp,
      BigDecimal averagePrice,
      BigDecimal cumulativeAmount,
      BigDecimal fee,
      OrderStatus status) {

    this(
        type,
        originalAmount,
        instrument,
        id,
        timestamp,
        averagePrice,
        cumulativeAmount,
        fee,
        status,
        Integer.toString(100000000 + random.nextInt(100000000)));
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
   * @param timestamp the absolute time for this order according to the exchange's server, null if  not provided
   *                  此订单的绝对时间根据交易所的服务器，如果没有提供，则为空
   *
   * @param averagePrice the averagePrice of fill belonging to the order
   *                     属于订单的平均成交价
   *
   * @param cumulativeAmount the amount that has been filled
   *                         已填写的金额
   *
   * @param fee the fee associated with this order
   *            与此订单相关的费用
   *
   * @param status the status of the order at the exchange
   *               交易所的订单状态
   *
   * @param userReference a reference provided by the user to identify the order
   *                      用户提供的用于识别订单的参考
   */
  public Order(
      OrderType type,
      BigDecimal originalAmount,
      Instrument instrument,
      String id,
      Date timestamp,
      BigDecimal averagePrice,
      BigDecimal cumulativeAmount,
      BigDecimal fee,
      OrderStatus status,
      String userReference) {

    this.type = type;
    this.originalAmount = originalAmount;
    this.instrument = instrument;
    this.id = id;
    this.timestamp = timestamp;
    this.averagePrice = averagePrice;
    this.cumulativeAmount = cumulativeAmount;
    this.fee = fee;
    this.status = status;
    this.userReference = userReference;
  }

  private static String print(BigDecimal value) {
    return value == null ? null : value.toPlainString();
  }

  /**
   * The total of the fees incurred for all transactions related to this order
   * 与此订单相关的所有交易产生的费用总额
   *
   * @return null if this information is not available on the order level on the given exchange in  which case you will have to navigate trades which filled this order to calculate it
   * * @return null 如果此信息在给定交易所的订单级别上不可用，在这种情况下，您将必须浏览填写此订单的交易来计算它
   */
  public BigDecimal getFee() {
    return fee;
  }

  public void setFee(BigDecimal fee) {
    this.fee = fee;
  }

  /** @return The type (BID or ASK)
   * @return 类型（BID 或 ASK）*/
  public OrderType getType() {

    return type;
  }

  /**
   * @return The type (PENDING_NEW, NEW, PARTIALLY_FILLED, FILLED, PENDING_CANCEL, CANCELED,  PENDING_REPLACE, REPLACED, STOPPED, REJECTED or EXPIRED)
   * 类型（PENDING_NEW、NEW、PARTIALLY_FILLED、FILLED、PENDING_CANCEL、CANCELED、PENDING_REPLACE、REPLACED、STOPPED、REJECTED 或 EXPIRED）
   */
  public OrderStatus getStatus() {

    return status;
  }

  /** The amount to trade
   * 交易金额*/
  public BigDecimal getOriginalAmount() {

    return originalAmount;
  }

  /** The amount that has been filled
   * 已充值金额*/
  public BigDecimal getCumulativeAmount() {

    return cumulativeAmount;
  }

  public void setCumulativeAmount(BigDecimal cumulativeAmount) {

    this.cumulativeAmount = cumulativeAmount;
  }

  @JsonIgnore
  public BigDecimal getCumulativeCounterAmount() {
    if (cumulativeAmount != null
        && averagePrice != null
        && averagePrice.compareTo(BigDecimal.ZERO) > 0) {
      return cumulativeAmount.multiply(averagePrice);
    }
    return null;
  }

  /** @return The remaining order amount
   * 剩余订单金额 */
  public BigDecimal getRemainingAmount() {
    if (cumulativeAmount != null && originalAmount != null) {
      return originalAmount.subtract(cumulativeAmount);
    }
    return originalAmount;
  }

  /**
   * The average price of the fills in the order.
   * 订单中成交的平均价格。
   *
   * @return null if this information is not available on the order level on the given exchange in  which case you will have to navigate trades which filled this order to calculate it
   * 如果此信息在给定交易所的订单级别上不可用，则为空，在这种情况下，您必须浏览填写此订单的交易来计算它
   */
  public BigDecimal getAveragePrice() {

    return averagePrice;
  }

  public void setAveragePrice(BigDecimal averagePrice) {

    this.averagePrice = averagePrice;
  }

  /**
   * @deprecated CurrencyPair is a subtype of Instrument - this method will throw an exception if
        the order was for a derivative
   @deprecated CurrencyPair 是 Instrument 的子类型 - 如果出现这种情况，此方法将抛出异常
   该订单用于衍生品
   *     <p>use {@link #getInstrument()} instead
   *     <p>改用 {@link #getInstrument()}
   */
  @Deprecated
  @JsonIgnore
  public CurrencyPair getCurrencyPair() {
    if (instrument == null) {
      return null;
    }
    if (!(instrument instanceof CurrencyPair)) {
      throw new IllegalStateException(
          "The instrument of this order is not a currency pair 此订单的工具不是货币对: " + instrument);
    }
    return (CurrencyPair) instrument;
  }

  /** @return The instrument to be bought or sold
   * 要买卖的乐器*/
  public Instrument getInstrument() {
    return instrument;
  }

  /** @return A unique identifier (normally provided by the exchange)
   * 唯一标识符（通常由交易所提供） */
  public String getId() {

    return id;
  }

  /** @return A unique identifier provided by the user on placement
   * 用户在放置时提供的唯一标识符 */
  public String getUserReference() {

    return userReference;
  }

  public Date getTimestamp() {

    return timestamp;
  }

  public Set<IOrderFlags> getOrderFlags() {

    return orderFlags;
  }

  public void setOrderFlags(Set<IOrderFlags> flags) {

    this.orderFlags.clear();
    if (flags != null) {
      this.orderFlags.addAll(flags);
    }
  }

  public boolean hasFlag(IOrderFlags flag) {

    return orderFlags.contains(flag);
  }

  public void addOrderFlag(IOrderFlags flag) {

    orderFlags.add(flag);
  }

  public void setOrderStatus(OrderStatus status) {

    this.status = status;
  }

  public String getLeverage() {
    return leverage;
  }

  public void setLeverage(String leverage) {
    this.leverage = leverage;
  }

  @Override
  public String toString() {

    return "Order [type="
        + type
        + ", originalAmount="
        + print(originalAmount)
        + ", cumulativeAmount="
        + print(cumulativeAmount)
        + ", averagePrice="
        + print(averagePrice)
        + ", fee="
        + print(fee)
        + ", instrument="
        + instrument
        + ", id="
        + id
        + ", timestamp="
        + timestamp
        + ", status="
        + status
        + ", flags="
        + orderFlags
        + ", userReference="
        + userReference
        + "]";
  }

  @Override
  public int hashCode() {

    int hash = 7;
    hash = 83 * hash + (this.type != null ? this.type.hashCode() : 0);
    hash = 83 * hash + (this.originalAmount != null ? this.originalAmount.hashCode() : 0);
    hash = 83 * hash + (this.instrument != null ? this.instrument.hashCode() : 0);
    hash = 83 * hash + (this.id != null ? this.id.hashCode() : 0);
    hash = 83 * hash + (this.timestamp != null ? this.timestamp.hashCode() : 0);
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
    final Order other = (Order) obj;
    if (this.type != other.type) {
      return false;
    }
    if ((this.originalAmount == null)
        ? (other.originalAmount != null)
        : this.originalAmount.compareTo(other.originalAmount) != 0) {
      return false;
    }
    if ((this.instrument == null)
        ? (other.instrument != null)
        : !this.instrument.equals(other.instrument)) {
      return false;
    }
    if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
      return false;
    }
    if (this.timestamp != other.timestamp
        && (this.timestamp == null || !this.timestamp.equals(other.timestamp))) {
      return false;
    }
    return true;
  }

  public enum OrderType {

    /** Buying order (the trader is providing the counter currency)
     * 买单（交易者提供对应货币）*/
    BID,
    /** Selling order (the trader is providing the base currency)
     * 卖单（交易者提供基础货币） */
    ASK,
    /**
     * This is to close a short position when trading crypto currency derivatives such as swaps, futures for CFD's.
     * 这是为了在交易加密货币衍生品（例如掉期、差价合约期货）时关闭空头头寸。
     */
    EXIT_ASK,
    /**
     * This is to close a long position when trading crypto currency derivatives such as swaps, futures for CFD's.
     * 这是在交易加密货币衍生品（例如掉期、差价合约期货）时关闭多头头寸。
     */
    EXIT_BID;

    public OrderType getOpposite() {
      switch (this) {
        case BID:
          return ASK;
        case ASK:
          return BID;
        case EXIT_ASK:
          return EXIT_BID;
        case EXIT_BID:
          return EXIT_ASK;
        default:
          return null;
      }
    }
  }

  public enum OrderStatus {

    /** Initial order when instantiated
     * 实例化时的初始顺序*/
    PENDING_NEW,
    /** Initial order when placed on the order book at exchange
     * 在交易所下订单时的初始订单*/
    NEW,
    /** Partially match against opposite order on order book at exchange
     * 部分匹配交易所订单簿上的相反订单 */
    PARTIALLY_FILLED,
    /** Fully match against opposite order on order book at exchange
     * 与交易所订单簿上的相反订单完全匹配 */
    FILLED,
    /** Waiting to be removed from order book at exchange
     * 等待在交易所从订单簿中删除 */
    PENDING_CANCEL,
    /** Order was partially canceled at exchange
     * 订单在交易所被部分取消 */
    PARTIALLY_CANCELED,
    /** Removed from order book at exchange
     * 在交易所从订单簿中删除*/
    CANCELED,
    /** Waiting to be replaced by another order on order book at exchange
     * 在交易所等待被订单簿上的另一个订单替换*/
    PENDING_REPLACE,
    /** Order has been replace by another order on order book at exchange
     * 订单已被交易所订单簿上的另一个订单替换 */
    REPLACED,
    /** Order has been triggered at stop price
     * 订单已在止损价触发 */
    STOPPED,
    /** Order has been rejected by exchange and not place on order book
     * 订单已被交易所拒绝，未放入订单簿*/
    REJECTED,
    /** Order has expired it's time to live or trading session and been removed from order book
     *订单已过期，到了生存时间或交易时段，并从订单簿中删除 */
    EXPIRED,
    /** Order is open and waiting to be filled  订单已打开，等待填写*/
    OPEN,
    /** Order has been either filled or cancelled 订单已成交或取消*/
    CLOSED,
    /**
     * The exchange returned a state which is not in the exchange's API documentation. The state of the order cannot be confirmed.
     * 交易所返回的状态不在交易所的 API 文档中。 订单状态无法确认。
     */
    UNKNOWN;

    /** Returns true for final {@link OrderStatus} 最终 {@link OrderStatus} 返回 true */
    public boolean isFinal() {
      switch (this) {
        case FILLED:
        case PARTIALLY_CANCELED: // Cancelled, partially-executed order is final status. 已取消、部分执行的订单为最终状态。
        case CANCELED:
        case REPLACED:
        case STOPPED:
        case REJECTED:
        case EXPIRED:
        case CLOSED: // Filled or Cancelled  填写或取消
          return true;
        default:
          return false;
      }
    }

    /** Returns true when open {@link OrderStatus}
     * 打开时返回 true {@link OrderStatus}*/
    public boolean isOpen() {
      switch (this) {
        case PENDING_NEW:
        case NEW:
        case PARTIALLY_FILLED:
        case OPEN:
          return true;
        default:
          return false;
      }
    }
  }

  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.WRAPPER_OBJECT)
  public interface IOrderFlags {}

  public abstract static class Builder {

    protected final Set<IOrderFlags> flags = new HashSet<>();
    protected OrderType orderType;
    protected BigDecimal originalAmount;
    protected BigDecimal cumulativeAmount;
    protected BigDecimal remainingAmount;
    protected Instrument instrument;
    protected String id;
    protected String userReference;
    protected Date timestamp;
    protected BigDecimal averagePrice;
    protected OrderStatus status;
    protected BigDecimal fee;
    protected String leverage;

    protected Builder(OrderType orderType, Instrument instrument) {

      this.orderType = orderType;
      this.instrument = instrument;
    }

    @JsonProperty("type")
    public Builder orderType(OrderType orderType) {

      this.orderType = orderType;
      return this;
    }

    @JsonProperty("status")
    public Builder orderStatus(OrderStatus status) {

      this.status = status;
      return this;
    }

    public Builder originalAmount(BigDecimal originalAmount) {

      this.originalAmount = originalAmount;
      return this;
    }

    public Builder cumulativeAmount(BigDecimal cumulativeAmount) {

      this.cumulativeAmount = cumulativeAmount;
      return this;
    }

    public Builder fee(BigDecimal fee) {

      this.fee = fee;
      return this;
    }

    public Builder remainingAmount(BigDecimal remainingAmount) {

      this.remainingAmount = remainingAmount;
      return this;
    }

    public Builder averagePrice(BigDecimal averagePrice) {

      this.averagePrice = averagePrice;
      return this;
    }

    @Deprecated
    public Builder currencyPair(CurrencyPair currencyPair) {

      this.instrument = currencyPair;
      return this;
    }

    public Builder instrument(Instrument instrument) {
      this.instrument = instrument;
      return this;
    }

    public Builder id(String id) {

      this.id = id;
      return this;
    }

    public Builder userReference(String userReference) {

      this.userReference = userReference;
      return this;
    }

    public Builder timestamp(Date timestamp) {

      this.timestamp = timestamp;
      return this;
    }

    public Builder leverage(String leverage) {

      this.leverage = leverage;
      return this;
    }

    @JsonProperty("orderFlags")
    public Builder flags(Set<IOrderFlags> flags) {

      this.flags.addAll(flags);
      return this;
    }

    public Builder flag(IOrderFlags flag) {

      this.flags.add(flag);
      return this;
    }

    public abstract Order build();
  }
}
