package org.knowm.xchange.dto.marketdata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order.OrderType;
import org.knowm.xchange.instrument.Instrument;
import org.knowm.xchange.service.marketdata.MarketDataService;

/** Data object representing a Trade
 * 代表交易的数据对象*/
@JsonDeserialize(builder = Trade.Builder.class)
public class Trade implements Serializable {

  private static final long serialVersionUID = -4078893146776655648L;

  /** Did this trade result from the execution of a bid or a ask?
   * 这种交易是由执行买价还是卖价产生的？ */
  protected final OrderType type;

  /** Amount that was traded
   * 交易金额 */
  protected final BigDecimal originalAmount;

  /** The instrument
   * 这个仪器 */
  protected final Instrument instrument;

  /** The price
   * 价格 */
  protected final BigDecimal price;

  /** The timestamp of the trade according to the exchange's server, null if not provided
   * 根据交易所服务器的交易时间戳，如果没有提供则为空*/
  protected final Date timestamp;

  /** The trade id
   * 贸易编号*/
  protected final String id;

  protected final String makerOrderId;

  protected final String takerOrderId;

  /**
   * This constructor is called to create a public Trade object in {@link
   MarketDataService#getTrades(org.knowm.xchange.currency.CurrencyPair, Object...)}
    implementations) since it's missing the orderId and fee parameters.
   调用此构造函数以在 {@link 中创建公共 Trade 对象
  MarketDataService#getTrades(org.knowm.xchange.currency.CurrencyPair, Object...)}
   实现），因为它缺少 orderId 和 fee 参数。
   *
   * @param type The trade type (BID side or ASK side)
   *             交易类型（BID 方或 ASK 方）
   *
   * @param originalAmount The depth of this trade
   *                       这笔交易的深度
   *
   * @param price The price (either the bid or the ask)
   *              价格（出价或要价）
   *
   * @param timestamp The timestamp of the trade according to the exchange's server, null if not provided
   *                  根据交易所服务器的交易时间戳，如果没有提供则为空
   *
   * @param id The id of the trade
   *           交易ID
   *
   * @param makerOrderId The orderId of the maker in the trade
   *                     交易中maker的orderId
   *
   * @param takerOrderId The orderId of the taker in the trade
   *                     交易中taker的orderId
   */
  public Trade(
      OrderType type,
      BigDecimal originalAmount,
      Instrument instrument,
      BigDecimal price,
      Date timestamp,
      String id,
      String makerOrderId,
      String takerOrderId) {

    this.type = type;
    this.originalAmount = originalAmount;
    this.instrument = instrument;
    this.price = price;
    this.timestamp = timestamp;
    this.id = id;
    this.makerOrderId = makerOrderId;
    this.takerOrderId = takerOrderId;
  }

  public OrderType getType() {

    return type;
  }

  public BigDecimal getOriginalAmount() {

    return originalAmount;
  }

  public Instrument getInstrument() {

    return instrument;
  }

  /**
   * @deprecated CurrencyPair is a subtype of Instrument - this method will throw an exception if
        the order was for a derivative     <p>use {@link #getInstrument()} instead
  CurrencyPair 是 Instrument 的子类型 - 如果出现以下情况，此方法将抛出异常
  订单是为了衍生<p>使用 {@link #getInstrument()} 代替
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

  public BigDecimal getPrice() {

    return price;
  }

  public Date getTimestamp() {

    return timestamp;
  }

  public String getId() {

    return id;
  }

  public String getMakerOrderId() {
    return makerOrderId;
  }

  public String getTakerOrderId() {
    return takerOrderId;
  }

  @Override
  public boolean equals(Object o) {

    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    return this.id.equals(((Trade) o).getId());
  }

  @Override
  public int hashCode() {

    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return "Trade{"
        + "type="
        + type
        + ", originalAmount="
        + originalAmount
        + ", instrument="
        + instrument
        + ", price="
        + price
        + ", timestamp="
        + timestamp
        + ", id='"
        + id
        + '\''
        + ", makerOrderId='"
        + makerOrderId
        + '\''
        + ", takerOrderId='"
        + takerOrderId
        + '\''
        + '}';
  }

  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder {

    protected OrderType type;
    protected BigDecimal originalAmount;
    protected Instrument instrument;
    protected BigDecimal price;
    protected Date timestamp;
    protected String id;
    protected String makerOrderId;
    protected String takerOrderId;

    public static Builder from(Trade trade) {
      return new Builder()
          .type(trade.getType())
          .originalAmount(trade.getOriginalAmount())
          .instrument(trade.getInstrument())
          .price(trade.getPrice())
          .timestamp(trade.getTimestamp())
          .id(trade.getId());
    }

    public Builder type(OrderType type) {

      this.type = type;
      return this;
    }

    public Builder originalAmount(BigDecimal originalAmount) {

      this.originalAmount = originalAmount;
      return this;
    }

    public Builder instrument(Instrument instrument) {

      this.instrument = instrument;
      return this;
    }

    /**
     * @deprecated CurrencyPair is a subtype of Instrument - this method will throw an exception if  the order was for a derivative
     * CurrencyPair 是 Instrument 的子类型 - 如果订单是衍生品，此方法将引发异常
     *     <p>use {@link #instrument(Instrument)} instead
     *     <p>改用 {@link #instrument(Instrument)}
     */
    @Deprecated
    public Builder currencyPair(CurrencyPair currencyPair) {

      return instrument(currencyPair);
    }

    public Builder price(BigDecimal price) {

      this.price = price;
      return this;
    }

    public Builder timestamp(Date timestamp) {

      this.timestamp = timestamp;
      return this;
    }

    public Builder id(String id) {

      this.id = id;
      return this;
    }

    public Builder makerOrderId(String makerOrderId) {

      this.makerOrderId = makerOrderId;
      return this;
    }

    public Builder takerOrderId(String takerOrderId) {

      this.takerOrderId = takerOrderId;
      return this;
    }

    public Trade build() {

      return new Trade(
          type, originalAmount, instrument, price, timestamp, id, makerOrderId, takerOrderId);
    }
  }
}
