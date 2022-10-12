package org.knowm.xchange.bitfinex.v2.dto.marketdata;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Bitfinex Ticker 资金货币
 */
@JsonFormat(shape = JsonFormat.Shape.ARRAY)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class BitfinexTickerFundingCurrency implements BitfinexTicker {
  /**
   * 符号
   */
  private String symbol;
  /**
   *  拒真率 /快速重路由 /拒识率 /错误拒绝率?
   */
  private BigDecimal frr;
  /**
   * 出价；投标，竞标?
   */
  private BigDecimal bid;
  /**
   * 出价；投标，竞标? 期间 ,一段时间，时期
   */
  private BigDecimal bidPeriod;
  /**
   * 出价；投标，竞标? 大小
   */
  private BigDecimal bidSize;
  /**
   * 询问 /请求/邀请
   */
  private BigDecimal ask;
  /**
   * 一段时间，时期；阶段 请求/邀请
   */
  private BigDecimal askPeriod;
  /**
   * 请求/邀请 大小
   */
  private BigDecimal askSize;
  /**
   * 每日，每天 变化
   */
  private BigDecimal dailyChange;
  /**
   * 每日，每天 变化 Perc
   */
  private BigDecimal dailyChangePerc;
  /**
   * 最后价格
   */
  private BigDecimal lastPrice;
  /**
   * 总数，总量
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
  /**
   * 占位符 0
   */
  private Object placeHolder0;
  /**
   * 占位符 1
   */
  private Object placeHolder1;
  /**
   * frr 可用金额
   */
  private BigDecimal frrAmountAvailable;

  @Override
  public boolean isFundingCurrency() {
    return true;
  }
}
