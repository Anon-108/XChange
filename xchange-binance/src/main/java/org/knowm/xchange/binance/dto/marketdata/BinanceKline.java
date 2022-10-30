package org.knowm.xchange.binance.dto.marketdata;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import org.knowm.xchange.currency.CurrencyPair;

/**
 * Binance k线
 */
public final class BinanceKline {

  /**
   * 成双的两物品） 对
   */
  private final CurrencyPair pair;
  /**
   * 区间 /时间间隔
   */
  private final KlineInterval interval;
  /**
   * 开时间
   */
  private final long openTime;
  /**
   * 开
   */
  private final BigDecimal open;
  /**
   * 高
   */
  private final BigDecimal high;
  /**
   * 低
   */
  private final BigDecimal low;
  /**
   * 关/收?
   */
  private final BigDecimal close;
  /**
   * 总数，总量
   */
  private final BigDecimal volume;
  /**
   * 关/收时间
   */
  private final long closeTime;
  /**
   * 报价资产量
   */
  private final BigDecimal quoteAssetVolume;
  /**
   * 交易数量
   */
  private final long numberOfTrades;
  /**
   * taker/接受者 购买基础资产量
   */
  private final BigDecimal takerBuyBaseAssetVolume;
  /**
   * taker/接受 购买报价资产量
   */
  private final BigDecimal takerBuyQuoteAssetVolume;

  /**
   *  Binance k线
   * @param pair 成双的两物品） 对
   * @param interval 区间 /时间间隔
   * @param obj 对象数组(注意各个位置的类型)
   */
  public BinanceKline(CurrencyPair pair, KlineInterval interval, Object[] obj) {
    this.pair = pair;
    this.interval = interval;
    this.openTime = Long.valueOf(obj[0].toString());
    this.open = new BigDecimal(obj[1].toString());
    this.high = new BigDecimal(obj[2].toString());
    this.low = new BigDecimal(obj[3].toString());
    this.close = new BigDecimal(obj[4].toString());
    this.volume = new BigDecimal(obj[5].toString());
    this.closeTime = Long.valueOf(obj[6].toString());
    this.quoteAssetVolume = new BigDecimal(obj[7].toString());
    this.numberOfTrades = Long.valueOf(obj[8].toString());
    this.takerBuyBaseAssetVolume = new BigDecimal(obj[9].toString());
    this.takerBuyQuoteAssetVolume = new BigDecimal(obj[10].toString());
  }

  public CurrencyPair getCurrencyPair() {
    return pair;
  }

  public KlineInterval getInterval() {
    return interval;
  }

  public long getOpenTime() {
    return openTime;
  }

  public BigDecimal getOpenPrice() {
    return open;
  }

  public BigDecimal getHighPrice() {
    return high;
  }

  public BigDecimal getLowPrice() {
    return low;
  }

  /**
   * 得到的平均价格
   * @return
   */
  public BigDecimal getAveragePrice() {
    return low.add(high).divide(new BigDecimal("2"));
  }

  public BigDecimal getClosePrice() {
    return close;
  }

  public BigDecimal getVolume() {
    return volume;
  }

  public long getCloseTime() {
    return closeTime;
  }

  public BigDecimal getQuoteAssetVolume() {
    return quoteAssetVolume;
  }

  public long getNumberOfTrades() {
    return numberOfTrades;
  }

  public BigDecimal getTakerBuyBaseAssetVolume() {
    return takerBuyBaseAssetVolume;
  }

  public BigDecimal getTakerBuyQuoteAssetVolume() {
    return takerBuyQuoteAssetVolume;
  }

  public String toString2() {
    String tstamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(openTime);
    return String.format(
        "[%s] %s %s O:%.6f A:%.6f C:%.6f", pair, tstamp, interval, open, getAveragePrice(), close);
  }

  @Override
  public String toString() {
    String openTime2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(openTime);
    String closeTime2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(closeTime);
    return "{" +
            "pair:" + pair +
            ", interval:" + interval +
            ", openTime:" + openTime2 +
            ", open:" + open +
            ", high:" + high +
            ", low:" + low +
            ", close:" + close +
            ", volume:" + volume +
            ", closeTime:" + closeTime2 +
            ", quoteAssetVolume:" + quoteAssetVolume +
            ", numberOfTrades:" + numberOfTrades +
            ", takerBuyBaseAssetVolume:" + takerBuyBaseAssetVolume +
            ", takerBuyQuoteAssetVolume:" + takerBuyQuoteAssetVolume +
            '}';
  }
}
