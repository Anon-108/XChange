package org.knowm.xchange.dto.marketdata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Objects;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.instrument.Instrument;
import org.knowm.xchange.utils.Assert;
import org.knowm.xchange.utils.DateUtils;

/**
 * A class encapsulating the information a "Ticker" can contain. Some fields can be empty if not provided by the exchange.
 * 封装“Ticker”可以包含的信息的类。 如果交易所未提供，某些字段可能为空。
 *
 * <p>A ticker contains data representing the latest trade.
 * <p>代码包含代表最新交易的数据。
 */
@JsonDeserialize(builder = Ticker.Builder.class)
public final class Ticker implements Serializable {

  private static final long serialVersionUID = -3247730106987193154L;

  private final Instrument instrument;
  private final BigDecimal open;
  private final BigDecimal last;
  private final BigDecimal bid;
  private final BigDecimal ask;
  private final BigDecimal high;
  private final BigDecimal low;
  private final BigDecimal vwap;
  private final BigDecimal volume;
  private final BigDecimal quoteVolume;
  /** the timestamp of the ticker according to the exchange's server, null if not provided
   * 根据交易所服务器的报价单的时间戳，如果未提供，则为 null*/
  private final Date timestamp;

  private final BigDecimal bidSize;
  private final BigDecimal askSize;
  private final BigDecimal percentageChange;

  /**
   * Constructor
   *
   * @param instrument The tradable identifier (e.g. BTC in BTC/USD)
   *                   可交易标识符（例如 BTC/USD 中的 BTC）
   *
   * @param last Last price
   *            最后价格
   *
   * @param bid Bid price
   *           竞标价格
   *
   * @param ask Ask price
   *          要价
   *
   * @param high High price
   *             高价
   *
   * @param low Low price
   *            低价
   *
   * @param vwap Volume Weighted Average Price
   *             成交量加权平均价
   *
   * @param volume 24h volume in base currency
   *               以基础货币计算的 24 小时交易量
   *
   * @param quoteVolume 24h volume in counter currency
   *                    24 小时本币交易量
   *
   * @param timestamp - the timestamp of the ticker according to the exchange's server, null if not  provided
   *                  - 根据交易所服务器的报价单的时间戳，如果未提供则为空
   *
   * @param bidSize The instantaneous size at the bid price
   *                买入价的瞬时大小
   *
   * @param askSize The instantaneous size at the ask price
   *                卖出价的瞬时大小
   *
   * @param percentageChange Price percentage change. Is compared against the last price value. Will
        be null if not provided and cannot be calculated. Should be represented as percentage (e.g. 0.5 equal 0.5%, 1 equal 1%, 50 equal 50%, 100 equal 100%)
        价格百分比变化。 与最后的价格值进行比较。 将要
        如果未提供且无法计算，则为 null。 应以百分比表示（例如 0.5 等于 0.5%、1 等于 1%、50 等于 50%、100 等于 100%）
   */
  private Ticker(
      Instrument instrument,
      BigDecimal open,
      BigDecimal last,
      BigDecimal bid,
      BigDecimal ask,
      BigDecimal high,
      BigDecimal low,
      BigDecimal vwap,
      BigDecimal volume,
      BigDecimal quoteVolume,
      Date timestamp,
      BigDecimal bidSize,
      BigDecimal askSize,
      BigDecimal percentageChange) {
    this.open = open;
    this.instrument = instrument;
    this.last = last;
    this.bid = bid;
    this.ask = ask;
    this.high = high;
    this.low = low;
    this.vwap = vwap;
    this.volume = volume;
    this.quoteVolume = quoteVolume;
    this.timestamp = timestamp;
    this.bidSize = bidSize;
    this.askSize = askSize;
    this.percentageChange = percentageChange;
  }

  public Instrument getInstrument() {
    return instrument;
  }

  /**
   * @deprecated CurrencyPair is a subtype of Instrument - this method will throw an exception if
       the order was for a derivative  <p>use {@link #getInstrument()} instead
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

  public BigDecimal getOpen() {

    return open;
  }

  public BigDecimal getLast() {

    return last;
  }

  public BigDecimal getBid() {

    return bid;
  }

  public BigDecimal getAsk() {

    return ask;
  }

  public BigDecimal getHigh() {

    return high;
  }

  public BigDecimal getLow() {

    return low;
  }

  public BigDecimal getVwap() {

    return vwap;
  }

  public BigDecimal getVolume() {
    if (volume == null && quoteVolume != null && last != null && !last.equals(BigDecimal.ZERO)) {
      return quoteVolume.divide(last, RoundingMode.HALF_UP);
    }

    return volume;
  }

  public BigDecimal getQuoteVolume() {
    if (quoteVolume == null && volume != null && last != null) {
      return volume.multiply(last);
    }
    return quoteVolume;
  }

  public Date getTimestamp() {

    return timestamp;
  }

  public BigDecimal getBidSize() {
    return bidSize;
  }

  public BigDecimal getAskSize() {
    return askSize;
  }

  public BigDecimal getPercentageChange() {
    return percentageChange;
  }

  @Override
  public String toString() {

    return "Ticker [instrument="
        + instrument
        + ", open="
        + open
        + ", last="
        + last
        + ", bid="
        + bid
        + ", ask="
        + ask
        + ", high="
        + high
        + ", low="
        + low
        + ",avg="
        + vwap
        + ", volume="
        + volume
        + ", quoteVolume="
        + quoteVolume
        + ", timestamp="
        + DateUtils.toMillisNullSafe(timestamp)
        + ", bidSize="
        + bidSize
        + ", askSize="
        + askSize
        + ", percentageChange="
        + percentageChange
        + "]";
  }

  /**
   * Builder to provide the following to {@link Ticker}:
   * 生成器向 {@link Ticker} 提供以下内容：
   *
   * <ul>
   *   <li>Provision of fluent chained construction interface
   *   * <li>提供流畅的链式构建接口
   * </ul>
   */
  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder {

    private Instrument instrument;
    private BigDecimal open;
    private BigDecimal last;
    private BigDecimal bid;
    private BigDecimal ask;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal vwap;
    private BigDecimal volume;
    private BigDecimal quoteVolume;
    private Date timestamp;
    private BigDecimal bidSize;
    private BigDecimal askSize;
    private BigDecimal percentageChange;

    // Prevent repeat builds
    // 防止重复构建
    private boolean isBuilt = false;

    public Ticker build() {

      validateState();

      Ticker ticker =
          new Ticker(
              instrument,
              open,
              last,
              bid,
              ask,
              high,
              low,
              vwap,
              volume,
              quoteVolume,
              timestamp,
              bidSize,
              askSize,
              percentageChange);

      isBuilt = true;

      return ticker;
    }

    private void validateState() {

      if (isBuilt) {
        throw new IllegalStateException("The entity has been built  实体已建成");
      }
    }

    public Builder instrument(Instrument instrument) {
      Assert.notNull(instrument, "Null instrument");
      this.instrument = instrument;
      return this;
    }

    /** @deprecated Use {@link #instrument(Instrument)}
     * 使用 {@link #instrument(Instrument)}*/
    @Deprecated
    public Builder currencyPair(CurrencyPair currencyPair) {
      return instrument(currencyPair);
    }

    public Builder open(BigDecimal open) {

      this.open = open;
      return this;
    }

    public Builder last(BigDecimal last) {

      this.last = last;
      return this;
    }

    public Builder bid(BigDecimal bid) {

      this.bid = bid;
      return this;
    }

    public Builder ask(BigDecimal ask) {

      this.ask = ask;
      return this;
    }

    public Builder high(BigDecimal high) {

      this.high = high;
      return this;
    }

    public Builder low(BigDecimal low) {

      this.low = low;
      return this;
    }

    public Builder vwap(BigDecimal vwap) {

      this.vwap = vwap;
      return this;
    }

    public Builder volume(BigDecimal volume) {

      this.volume = volume;
      return this;
    }

    public Builder quoteVolume(BigDecimal quoteVolume) {

      this.quoteVolume = quoteVolume;
      return this;
    }

    public Builder timestamp(Date timestamp) {

      this.timestamp = timestamp;
      return this;
    }

    public Builder bidSize(BigDecimal bidSize) {
      this.bidSize = bidSize;
      return this;
    }

    public Builder askSize(BigDecimal askSize) {
      this.askSize = askSize;
      return this;
    }

    public Builder percentageChange(BigDecimal percentageChange) {
      this.percentageChange = percentageChange;
      return this;
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Ticker ticker = (Ticker) o;
    return Objects.equals(getInstrument(), ticker.getInstrument())
        && Objects.equals(getOpen(), ticker.getOpen())
        && Objects.equals(getLast(), ticker.getLast())
        && Objects.equals(getBid(), ticker.getBid())
        && Objects.equals(getAsk(), ticker.getAsk())
        && Objects.equals(getHigh(), ticker.getHigh())
        && Objects.equals(getLow(), ticker.getLow())
        && Objects.equals(getVwap(), ticker.getVwap())
        && Objects.equals(getVolume(), ticker.getVolume())
        && Objects.equals(getQuoteVolume(), ticker.getQuoteVolume())
        && Objects.equals(getTimestamp(), ticker.getTimestamp())
        && Objects.equals(getBidSize(), ticker.getBidSize())
        && Objects.equals(getAskSize(), ticker.getAskSize())
        && Objects.equals(getPercentageChange(), ticker.getPercentageChange());
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        getInstrument(),
        getOpen(),
        getLast(),
        getBid(),
        getAsk(),
        getHigh(),
        getLow(),
        getVwap(),
        getVolume(),
        getQuoteVolume(),
        getTimestamp(),
        getBidSize(),
        getAskSize());
  }
}
