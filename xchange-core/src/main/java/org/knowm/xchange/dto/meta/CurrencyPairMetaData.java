package org.knowm.xchange.dto.meta;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import org.knowm.xchange.currency.Currency;

public class CurrencyPairMetaData implements Serializable {

  private static final long serialVersionUID = 4749144540694704221L;

  /** Trading fee (fraction)
   * 交易费（分数）*/
  @JsonProperty("trading_fee")
  private final BigDecimal tradingFee;

  /** Trading fee tiers by volume (fraction). Sorted in ascending order by quantity
   * 按交易量（分数）计算的交易费用等级。 按数量升序排列*/
  @JsonProperty("fee_tiers")
  private final FeeTier[] feeTiers;

  /** Minimum trade amount
   * 最低交易金额 */
  @JsonProperty("min_amount")
  private final BigDecimal minimumAmount;

  /** Maximum trade amount
   * 最大交易金额 */
  @JsonProperty("max_amount")
  private final BigDecimal maximumAmount;

  /** Minimum trade amount
   * 最低交易金额*/
  @JsonProperty("counter_min_amount")
  private final BigDecimal counterMinimumAmount;

  /** Maximum trade amount
   * 最大交易金额*/
  @JsonProperty("counter_max_amount")
  private final BigDecimal counterMaximumAmount;

  /** Decimal places for base amount
   * 基本金额的小数位 */
  @JsonProperty("base_scale")
  private final Integer baseScale;

  /** Decimal places for counter amount
   * 计数器金额的小数位 */
  @JsonProperty("price_scale")
  private final Integer priceScale;

  /** Decimal places for volume amount
   * 数量的小数位*/
  @JsonProperty("volume_scale")
  private final Integer volumeScale;

  /** Amount step size. If set, any amounts must be a multiple of this
   * 量步长。 如果设置，任何金额必须是这个的倍数*/
  @JsonProperty("amount_step_size")
  private final BigDecimal amountStepSize;

  /** Currency that will be used to change for this trade.
   * 将用于更改此交易的货币。 */
  private final Currency tradingFeeCurrency;

  /** Is market order type allowed on this pair.
   * 该货币对是否允许市价单类型。 */
  private final boolean marketOrderEnabled;

  /**
   * Constructor
   *
   * @param tradingFee Trading fee (fraction)
   *                   交易费（分数）
   *
   * @param minimumAmount Minimum trade amount
   *                      最低交易金额
   *
   * @param maximumAmount Maximum trade amount
   *                      最大交易金额
   *
   * @param priceScale Price scale
   *                   价格尺度
   */
  public CurrencyPairMetaData(
      BigDecimal tradingFee,
      BigDecimal minimumAmount,
      BigDecimal maximumAmount,
      Integer priceScale,
      FeeTier[] feeTiers) {
    this(
        tradingFee,
        minimumAmount,
        maximumAmount,
        null,
        null,
        null,
        priceScale,
        null,
        feeTiers,
        null,
        null,
        true);
  }

  /**
   * Constructor
   *
   * @param tradingFee Trading fee (fraction)
   *                   交易费（分数）
   *
   * @param minimumAmount Minimum trade amount
   *                      最低交易金额
   *
   * @param maximumAmount Maximum trade amount
   *                      最大交易金额
   *
   * @param priceScale Price scale
   *                   价格尺度
   *
   * @param amountStepSize Amounts must be a multiple of this amount if set.
   *                       如果设置，金额必须是该金额的倍数。
   */
  public CurrencyPairMetaData(
      BigDecimal tradingFee,
      BigDecimal minimumAmount,
      BigDecimal maximumAmount,
      Integer priceScale,
      FeeTier[] feeTiers,
      BigDecimal amountStepSize) {
    this(
        tradingFee,
        minimumAmount,
        maximumAmount,
        null,
        null,
        null,
        priceScale,
        null,
        feeTiers,
        amountStepSize,
        null,
        true);
  }

  public CurrencyPairMetaData(
      BigDecimal tradingFee,
      BigDecimal minimumAmount,
      BigDecimal maximumAmount,
      Integer priceScale,
      Integer volumeScale,
      FeeTier[] feeTiers,
      Currency tradingFeeCurrency) {
    this(
        tradingFee,
        minimumAmount,
        maximumAmount,
        null,
        null,
        null,
        priceScale,
        volumeScale,
        feeTiers,
        null,
        tradingFeeCurrency,
        true);
  }

  /**
   * Constructor
   *
   * @param tradingFee Trading fee (fraction)
   *                   交易费（分数）
   *
   * @param minimumAmount Minimum trade amount
   *                      最低交易金额
   *
   * @param maximumAmount Maximum trade amount
   *                      最大交易金额
   *
   * @param priceScale Price scale
   *                   价格尺度
   *
   * @param amountStepSize Amounts must be a multiple of this amount if set.
   *                       如果设置，金额必须是该金额的倍数。
   */
  public CurrencyPairMetaData(
      @JsonProperty("trading_fee") BigDecimal tradingFee,
      @JsonProperty("min_amount") BigDecimal minimumAmount,
      @JsonProperty("max_amount") BigDecimal maximumAmount,
      @JsonProperty("counter_min_amount") BigDecimal counterMinimumAmount,
      @JsonProperty("counter_max_amount") BigDecimal counterMaximumAmount,
      @JsonProperty("base_scale") Integer baseScale,
      @JsonProperty("price_scale") Integer priceScale,
      @JsonProperty("volume_scale") Integer volumeScale,
      @JsonProperty("fee_tiers") FeeTier[] feeTiers,
      @JsonProperty("amount_step_size") BigDecimal amountStepSize,
      @JsonProperty("trading_fee_currency") Currency tradingFeeCurrency,
      @JsonProperty("market_order_enabled") boolean marketOrderEnabled) {

    this.tradingFee = tradingFee;
    this.minimumAmount = minimumAmount;
    this.maximumAmount = maximumAmount;
    this.counterMinimumAmount = counterMinimumAmount;
    this.counterMaximumAmount = counterMaximumAmount;
    this.baseScale = baseScale;
    this.priceScale = priceScale;
    this.volumeScale = volumeScale;
    if (feeTiers != null) {
      Arrays.sort(feeTiers);
    }
    this.feeTiers = feeTiers;
    this.amountStepSize = amountStepSize;
    this.tradingFeeCurrency = tradingFeeCurrency;
    this.marketOrderEnabled = marketOrderEnabled;
  }

  public BigDecimal getTradingFee() {

    return tradingFee;
  }

  public BigDecimal getMinimumAmount() {

    return minimumAmount;
  }

  public BigDecimal getMaximumAmount() {

    return maximumAmount;
  }

  public Integer getBaseScale() {
    return baseScale;
  }

  public Integer getPriceScale() {
    return priceScale;
  }

  public Integer getVolumeScale() {
    return volumeScale;
  }

  public FeeTier[] getFeeTiers() {

    return feeTiers;
  }

  public BigDecimal getAmountStepSize() {

    return amountStepSize;
  }

  public Currency getTradingFeeCurrency() {
    return tradingFeeCurrency;
  }

  public BigDecimal getCounterMinimumAmount() {
    return counterMinimumAmount;
  }

  public BigDecimal getCounterMaximumAmount() {
    return counterMaximumAmount;
  }

  public boolean isMarketOrderEnabled() {
    return marketOrderEnabled;
  }

  public static class Builder {

    private BigDecimal tradingFee;
    private FeeTier[] feeTiers;
    private BigDecimal minimumAmount;
    private BigDecimal maximumAmount;
    private BigDecimal counterMinimumAmount;
    private BigDecimal counterMaximumAmount;
    private Integer baseScale;
    private Integer priceScale;
    private Integer volumeScale;
    private BigDecimal amountStepSize;
    private Currency tradingFeeCurrency;
    private boolean marketOrderEnabled;

    public static Builder from(CurrencyPairMetaData metaData) {
      return new Builder()
          .tradingFee(metaData.getTradingFee())
          .feeTiers(metaData.getFeeTiers())
          .minimumAmount(metaData.getMinimumAmount())
          .maximumAmount(metaData.getMaximumAmount())
          .counterMinimumAmount(metaData.getCounterMinimumAmount())
          .counterMaximumAmount(metaData.getCounterMaximumAmount())
          .baseScale(metaData.getBaseScale())
          .priceScale(metaData.getPriceScale())
          .volumeScale(metaData.getVolumeScale())
          .amountStepSize(metaData.getAmountStepSize())
          .tradingFee(metaData.getTradingFee())
          .tradingFeeCurrency(metaData.getTradingFeeCurrency());
    }

    public Builder tradingFee(BigDecimal tradingFee) {
      this.tradingFee = tradingFee;
      return this;
    }

    public Builder feeTiers(FeeTier[] feeTiers) {
      this.feeTiers = feeTiers;
      return this;
    }

    public Builder minimumAmount(BigDecimal minimumAmount) {
      this.minimumAmount = minimumAmount;
      return this;
    }

    public Builder maximumAmount(BigDecimal maximumAmount) {
      this.maximumAmount = maximumAmount;
      return this;
    }

    public Builder counterMinimumAmount(BigDecimal counterMinimumAmount) {
      this.counterMinimumAmount = counterMinimumAmount;
      return this;
    }

    public Builder counterMaximumAmount(BigDecimal counterMaximumAmount) {
      this.counterMaximumAmount = counterMaximumAmount;
      return this;
    }

    public Builder baseScale(Integer baseScale) {
      this.baseScale = baseScale;
      return this;
    }

    public Builder priceScale(Integer priceScale) {
      this.priceScale = priceScale;
      return this;
    }

    public Builder volumeScale(Integer volumeScale) {
      this.volumeScale = volumeScale;
      return this;
    }

    public Builder amountStepSize(BigDecimal amountStepSize) {
      this.amountStepSize = amountStepSize;
      return this;
    }

    public Builder tradingFeeCurrency(Currency tradingFeeCurrency) {
      this.tradingFeeCurrency = tradingFeeCurrency;
      return this;
    }

    public Builder marketOrderEnabled(boolean marketOrderEnabled) {
      this.marketOrderEnabled = marketOrderEnabled;
      return this;
    }

    public CurrencyPairMetaData build() {
      return new CurrencyPairMetaData(
          tradingFee,
          minimumAmount,
          maximumAmount,
          counterMinimumAmount,
          counterMaximumAmount,
          baseScale,
          priceScale,
          volumeScale,
          feeTiers,
          amountStepSize,
          tradingFeeCurrency,
          marketOrderEnabled);
    }
  }

  @Override
  public String toString() {

    return "CurrencyPairMetaData [tradingFee="
        + tradingFee
        + ", minimumAmount="
        + minimumAmount
        + ", maximumAmount="
        + maximumAmount
        + ", baseScale="
        + baseScale
        + ", priceScale="
        + priceScale
        + ", volumeScale="
        + volumeScale
        + ", amountStepSize="
        + amountStepSize
        + ", tradingFeeCurrency="
        + tradingFeeCurrency
        + "]";
  }
}
