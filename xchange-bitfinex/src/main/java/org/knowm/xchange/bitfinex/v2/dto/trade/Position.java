package org.knowm.xchange.bitfinex.v2.dto.trade;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** https://docs.bitfinex.com/v2/reference#rest-auth-positions */
@JsonFormat(shape = JsonFormat.Shape.ARRAY)
@Setter
@Getter
@ToString
public class Position {

  /** Pair (tBTCUSD, …). 对 (tBTCUSD, ...)。*/
  private String symbol;
  /** Status (ACTIVE, CLOSED). 状态（活动、关闭）。*/
  private String status;
  /**
   * ±amount Size of the position. Positive values means a long position, negative values means a short position.
   * ±amount 仓位大小。 正值表示多头头寸，负值表示空头头寸。
   */
  private BigDecimal amount;
  /** The price at which you entered your position.
   * 您进入头寸的价格。*/
  private BigDecimal basePrice;
  /** The amount of funding being used for this position.
   * 用于该职位的资金数额。*/
  private BigDecimal marginFunding;
  /** 0 for daily, 1 for term.
   * 0 为每日，1 为期限？长期。*/
  private int marginFundingType;
  /** Profit  利润 & Loss 损失*/
  private BigDecimal pl;
  /** Profit & Loss Percentage 损益百分比 */
  private BigDecimal plPercent;
  /** Liquidation price 强平价格 */
  private BigDecimal priceLiq;
  /** Beta value  贝塔值*/
  private BigDecimal leverage;

  private Object placeHolder0;
  /** Position ID 职位编号 */
  private Integer positionId;
  /** Millisecond timestamp of creation  创建的毫秒时间戳*/
  private long timestampCreate;
  /** Millisecond timestamp of update  更新的毫秒时间戳*/
  private Long timestampUpdate;

  private Object placeHolder1;
  /** Identifies the type of position, 0 = Margin position, 1 = Derivatives position
   * 标识头寸类型，0 = 保证金头寸，1 = 衍生品头寸*/
  private Integer type;

  private Object placeHolder2;
  /** The amount of collateral applied to the open position
   * 应用于未平仓头寸的抵押品数量*/
  private BigDecimal collateral;
  /** The minimum amount of collateral required for the position
   * 该头寸所需的最低抵押品数量*/
  private BigDecimal collateralMin;
  /** Additional meta information about the position
   * 有关该职位的其他元信息*/
  private Object meta;
}
