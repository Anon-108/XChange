package org.knowm.xchange.bitfinex.v2.dto.marketdata;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonFormat(shape = JsonFormat.Shape.ARRAY)
@JsonIgnoreProperties(ignoreUnknown = true)
@Setter
@Getter
@ToString

/** see https://docs.bitfinex.com/reference#rest-public-status */
public class Status {

  private String symbol;
  /** Millisecond timestamp
   * 毫秒时间戳 */
  private long timestamp;

  private Object placeHolder0;
  /** Derivative book mid price.
   * 衍生书中间价。*/
  private BigDecimal derivPrice;
  /** Book mid price of the underlying Bitfinex spot trading pair
   * 标的 Bitfinex 现货交易对的中间价 */
  private BigDecimal spotPrice;

  private Object placeHolder1;
  /** The balance available to the liquidation engine to absorb losses.
   * 清算引擎可用于吸收损失的余额。 */
  private BigDecimal insuranceFundBalance;

  private Object placeHolder2;
  /** Millisecond timestamp of next funding event
   * 下一次融资事件的毫秒时间戳 */
  private Long nextFundingEvtTimestampMillis;
  /** Current accrued funding for next 8h period
   * 下一个 8 小时的当前应计资金*/
  private BigDecimal nextFundingAccrued;
  /** Incremental accrual counter
   * 增量应计计数器*/
  private long nextFundingStep;

  private Object placeHolder4;
  /** Funding applied in the current 8h period
   * 当前 8 小时内申请的资金*/
  private BigDecimal currentFunding;

  private Object placeHolder5;
  private Object placeHolder6;
  /** Price based on the BFX Composite Index
   * 基于 BFX 综合指数的价格*/
  private BigDecimal markPrice;

  private Object placeHolder7;
  private Object placeHolder8;
  /** Total number of outstanding derivative contracts
   * 未平仓衍生品合约总数*/
  private BigDecimal openInterest;

  private Object placeHolder9;
  private Object placeHolder10;
  private Object placeHolder11;
  /** Range in the average spread that does not require a funding payment
   * 不需要资金支付的平均点差范围*/
  private BigDecimal clampMin;
  /** Funding payment cap
   * 资金支付上限*/
  private BigDecimal clampMax;
}
