package org.knowm.xchange.bitfinex.v2.dto.marketdata;

import java.math.BigDecimal;

/**
 * 代码
 */
public interface BitfinexTicker {

  default boolean isFundingCurrency() {
    return false;
  }

  default boolean isTradingPair() {
    return false;
  }

  /**
   * 获取符号
   * @return
   */
  String getSymbol();

  /**
   * 获取bid
   * @return
   */
  BigDecimal getBid();

  /**
   * 获取bid大小
   * @return
   */
  BigDecimal getBidSize();

  /**
   * 获取ask
   * @return
   */
  BigDecimal getAsk();

  /**
   * 获取ask大小
   * @return
   */
  BigDecimal getAskSize();

  /**
   * 获取每日的，日常的；一天的 变化
   * @return
   */
  BigDecimal getDailyChange();

  /**
   * 获取每日变化 Perc
   * @return
   */
  BigDecimal getDailyChangePerc();

  /**
   * 获取最后价格
   * @return
   */
  BigDecimal getLastPrice();

  /**
   * 获取总数，总量；
   * @return
   */
  BigDecimal getVolume();

  /**
   * 获取高
   * @return
   */
  BigDecimal getHigh();

  /**
   * 获取低
   * @return
   */
  BigDecimal getLow();
}
