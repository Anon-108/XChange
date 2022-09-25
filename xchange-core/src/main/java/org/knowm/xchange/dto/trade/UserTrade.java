package org.knowm.xchange.dto.trade;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order.OrderType;
import org.knowm.xchange.dto.marketdata.Trade;
import org.knowm.xchange.instrument.Instrument;
import org.knowm.xchange.service.trade.TradeService;
import org.knowm.xchange.service.trade.params.TradeHistoryParams;

/** Data object representing a user trade
 * 代表用户交易的数据对象*/
@JsonDeserialize(builder = UserTrade.Builder.class)
public class UserTrade extends Trade {

  private static final long serialVersionUID = -3021617981214969292L;

  /** The id of the order responsible for execution of this trade
   * 负责执行此交易的订单的 ID */
  private final String orderId;

  /** The fee that was charged by the exchange for this trade.
   * 交易所为此交易收取的费用。*/
  private final BigDecimal feeAmount;

  /** The currency in which the fee was charged.
   * 收取费用的货币。 */
  private final Currency feeCurrency;

  /** The order reference id which has been added by the user on the order creation
   * 用户在创建订单时添加的订单参考 id */
  private final String orderUserReference;

  /**
   * This constructor is called to construct user's trade objects (in {@link
    TradeService#getTradeHistory(TradeHistoryParams)} implementations).
   调用此构造函数来构造用户的交易对象（在 {@link
  TradeService#getTradeHistory(TradeHistoryParams)} 实现）。
   *
   * @param type The trade type (BID side or ASK side)
   *             交易类型（BID 方或 ASK 方）
   *
   * @param originalAmount The depth of this trade
   *                       这笔交易的深度
   *
   * @param instrument The exchange identifier (e.g. "BTC/USD")
   *                   交易所标识符（例如“BTC/USD”）
   *
   * @param price The price (either the bid or the ask)
   *              价格（出价或要价）
   *
   * @param timestamp The timestamp of the trade
   *                  交易的时间戳
   *
   * @param id The id of the trade
   *           交易ID
   *
   * @param orderId The id of the order responsible for execution of this trade
   *                负责执行此交易的订单的 ID
   *
   * @param feeAmount The fee that was charged by the exchange for this trade
   *                  交易所为此交易收取的费用
   *
   * @param feeCurrency The symbol of the currency in which the fee was charged
   *                    收取费用的货币符号
   *
   * @param orderUserReference The id that the user has insert to the trade
   *                           用户已插入交易的 id
   */
  public UserTrade(
      OrderType type,
      BigDecimal originalAmount,
      Instrument instrument,
      BigDecimal price,
      Date timestamp,
      String id,
      String orderId,
      BigDecimal feeAmount,
      Currency feeCurrency,
      String orderUserReference) {

    super(type, originalAmount, instrument, price, timestamp, id, null, null);

    this.orderId = orderId;
    this.feeAmount = feeAmount;
    this.feeCurrency = feeCurrency;
    this.orderUserReference = orderUserReference;
  }

  public static UserTrade.Builder builder() {
    return new UserTrade.Builder();
  }

  public String getOrderId() {

    return orderId;
  }

  public BigDecimal getFeeAmount() {

    return feeAmount;
  }

  public Currency getFeeCurrency() {

    return feeCurrency;
  }

  public String getOrderUserReference() {
    return orderUserReference;
  }

  @Override
  public String toString() {
    return "UserTrade[type="
        + type
        + ", originalAmount="
        + originalAmount
        + ", instrument="
        + instrument
        + ", price="
        + price
        + ", timestamp="
        + timestamp
        + ", id="
        + id
        + ", orderId='"
        + orderId
        + '\''
        + ", feeAmount="
        + feeAmount
        + ", feeCurrency='"
        + feeCurrency
        + '\''
        + ", orderUserReference='"
        + orderUserReference
        + '\''
        + "]";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    UserTrade userTrade = (UserTrade) o;
    return Objects.equals(orderId, userTrade.orderId)
        && Objects.equals(feeAmount, userTrade.feeAmount)
        && Objects.equals(feeCurrency, userTrade.feeCurrency);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), orderId, feeAmount, feeCurrency);
  }

  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder extends Trade.Builder {

    protected String orderId;
    protected BigDecimal feeAmount;
    protected Currency feeCurrency;
    protected String orderUserReference;

    public static Builder from(UserTrade trade) {
      return new Builder()
          .type(trade.getType())
          .originalAmount(trade.getOriginalAmount())
          .instrument(trade.getInstrument())
          .price(trade.getPrice())
          .timestamp(trade.getTimestamp())
          .id(trade.getId())
          .orderId(trade.getOrderId())
          .feeAmount(trade.getFeeAmount())
          .feeCurrency(trade.getFeeCurrency());
    }

    @Override
    public Builder type(OrderType type) {
      return (Builder) super.type(type);
    }

    @Override
    public Builder originalAmount(BigDecimal originalAmount) {
      return (Builder) super.originalAmount(originalAmount);
    }

    @Override
    public Builder instrument(Instrument instrument) {
      return (Builder) super.instrument(instrument);
    }

    @Override
    public Builder currencyPair(CurrencyPair currencyPair) {
      return (Builder) super.currencyPair(currencyPair);
    }

    @Override
    public Builder price(BigDecimal price) {
      return (Builder) super.price(price);
    }

    @Override
    public Builder timestamp(Date timestamp) {
      return (Builder) super.timestamp(timestamp);
    }

    @Override
    public Builder id(String id) {
      return (Builder) super.id(id);
    }

    public Builder orderId(String orderId) {
      this.orderId = orderId;
      return this;
    }

    public Builder feeAmount(BigDecimal feeAmount) {
      this.feeAmount = feeAmount;
      return this;
    }

    public Builder feeCurrency(Currency feeCurrency) {
      this.feeCurrency = feeCurrency;
      return this;
    }

    public Builder orderUserReference(String orderUserReference) {
      this.orderUserReference = orderUserReference;
      return this;
    }

    @Override
    public UserTrade build() {
      return new UserTrade(
          type,
          originalAmount,
          instrument,
          price,
          timestamp,
          id,
          orderId,
          feeAmount,
          feeCurrency,
          orderUserReference);
    }
  }
}
