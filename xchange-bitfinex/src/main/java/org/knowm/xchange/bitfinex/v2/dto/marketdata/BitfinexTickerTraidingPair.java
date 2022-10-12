package org.knowm.xchange.bitfinex.v2.dto.marketdata;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Bitfinex 股票交易对
 */
@JsonFormat(shape = JsonFormat.Shape.ARRAY)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class BitfinexTickerTraidingPair implements BitfinexTicker {
  /**
   * 符号
   */
  private String symbol;
  /**
   * 出价；投标，竞标
   */
  private BigDecimal bid;
  /**
   * 出价；投标，竞标大小
   */
  private BigDecimal bidSize;
  /**
   * 询问，打听；要求，请求；邀请
   */
  private BigDecimal ask;
  /**
   * 询问，打听；要求，请求；邀请 大小
   */
  private BigDecimal askSize;
  /**
   * 每日变化
   */
  private BigDecimal dailyChange;
  /**
   * 每日变化 Perc
   */
  private BigDecimal dailyChangePerc;
  /**
   * 最后价格
   */
  private BigDecimal lastPrice;
  /**
   * 总量
   */
  private BigDecimal volume;
  /**
   * 高
   */
  private BigDecimal high;
  /**
   * 低
   */
  private BigDecimal low;

  @Override
  public boolean isTradingPair() {
    return true;
  }
}
