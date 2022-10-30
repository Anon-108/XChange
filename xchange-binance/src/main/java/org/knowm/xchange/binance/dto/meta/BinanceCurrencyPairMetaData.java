package org.knowm.xchange.binance.dto.meta;

import java.math.BigDecimal;
import org.knowm.xchange.dto.meta.CurrencyPairMetaData;
import org.knowm.xchange.dto.meta.FeeTier;

/**
 * 币安货币对元数据
 * @author ujjwal on 26/02/18. */
public class BinanceCurrencyPairMetaData extends CurrencyPairMetaData {
  /**
   * 最小/min 概念的，纯理
   */
  private final BigDecimal minNotional;

  /**
   * Constructor
   *币安货币对元数据
   * @param tradingFee Trading fee (fraction) 交易手续费
   * @param minimumAmount Minimum trade amount 最低交易数量
   * @param maximumAmount Maximum trade amount 最大的交易金额
   * @param priceScale Price scale  价格尺度/天秤
   */
  public BinanceCurrencyPairMetaData(
      BigDecimal tradingFee,
      BigDecimal minimumAmount,
      BigDecimal maximumAmount,
      Integer priceScale,
      BigDecimal minNotional,
      FeeTier[] feeTiers) {
    super(tradingFee, minimumAmount, maximumAmount, priceScale, feeTiers);
    this.minNotional = minNotional;
  }

  public BigDecimal getMinNotional() {
    return minNotional;
  }

  @Override
  public String toString() {
    return "BinanceCurrencyPairMetaData{" + "minNotional=" + minNotional + "} " + super.toString();
  }
}
