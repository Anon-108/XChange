package org.knowm.xchange.bitfinex.v2.dto.trade;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 有效订单
 * https://docs.bitfinex.com/v2/reference#rest-auth-trades-hist */
@JsonFormat(shape = JsonFormat.Shape.ARRAY)
@Setter
@Getter
@ToString
public class ActiveOrder {

  /** Order ID
   * 订单编号*/
  private long id;
  /** Group ID 组 ID */
  private Long gid;
  /** Client Order ID  客户订单编号*/
  private Long cid;
  /** Pair (tBTCUSD, …) 对 (tBTCUSD, ...) */
  private String symbol;
  /** Millisecond timestamp of creation 创建的毫秒时间戳 */
  private long timestampCreate;
  /** Millisecond timestamp of update  更新的毫秒时间戳*/
  private Long timestampUpdate;
  /** Positive means buy, negative means sell. 正表示买入，负表示卖出。*/
  private BigDecimal amount;
  /** Original amount. 原始金额。*/
  private BigDecimal amountOrig;
  /**
   * The type of the order: LIMIT, MARKET, STOP, TRAILING STOP, EXCHANGE MARKET, EXCHANGE LIMIT, EXCHANGE STOP, EXCHANGE TRAILING STOP, FOK, EXCHANGE FOK, IOC, EXCHANGE IOC.
   * 订单类型：LIMIT、MARKET、STOP、TRAILING STOP、EXCHANGE MARKET、EXCHANGE LIMIT、EXCHANGE STOP、EXCHANGE TRAILING STOP、FOK、EXCHANGE FOK、IOC、EXCHANGE IOC。
   */
  private String type;
  /** Previous order type
   * 以前的订单类型 */
  private String typePrev;

  private Object placeHolder0;
  private Object placeHolder1;
  /** Upcoming Params Object (stay tuned)
   * 即将推出的 Params 对象（敬请期待）*/
  private int flags;
  /**
   * Order Status: ACTIVE, EXECUTED, PARTIALLY FILLED, CANCELED, RSN_DUST (amount is less than 0.00000001), RSN_PAUSE (trading is paused / paused due to AMPL rebase event)
   * * 订单状态：ACTIVE、EXECUTED、PARTIALLY FILLED、CANCELED、RSN_DUST（金额小于 0.00000001）、RSN_PAUSE（由于 AMPL rebase 事件，交易暂停/暂停）
   */
  private String orderStatus;

  private Object placeHolder2;
  private Object placeHolder3;
  /** Price 价格 */
  private BigDecimal price;
  /** Average price 平均价格*/
  private BigDecimal priceAvg;
  /** The trailing price 追踪价格*/
  private BigDecimal priceTrailing;
  /** Auxiliary Limit price (for STOP LIMIT) 辅助限价（针对 STOP LIMIT）*/
  private BigDecimal priceAuxLimit;

  private Object placeHolder4;
  private Object placeHolder5;
  private Object placeHolder6;
  /** 1 if Hidden, 0 if not hidden 1 表示隐藏，0 表示不隐藏*/
  private int hidden;
  /** If another order caused this order to be placed (OCO) this will be that other order's ID
   * 如果另一个订单导致该订单被放置 (OCO)，这将是该订单的 ID*/
  private Long placedId;

  public Date getTimestampCreate() {
    return new Date(timestampCreate);
  }
}
