package org.knowm.xchange.bitfinex.v2.dto.trade;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 订单交易
 * https://docs.bitfinex.com/reference#rest-auth-order-trades */
@JsonFormat(shape = JsonFormat.Shape.ARRAY)
@Setter
@Getter
@ToString
public class OrderTrade {

  /** Trade database id
   * 贸易数据库编号*/
  private String id;
  /** Pair (BTCUSD, …)
   * 对 (BTCUSD, ...) */
  private String symbol;
  /** Execution timestamp millis
   * 执行时间戳毫秒*/
  private long timestamp;
  /** Order id
   * 订单编号*/
  private String orderId;
  /** Positive means buy, negative means sell
   * 正表示买入，负表示卖出*/
  private BigDecimal execAmount;
  /** Execution price
   * 执行价格*/
  private BigDecimal execPrice;
  /** placeHolder1
   * 占位符1*/
  private String placeHolder1;
  /** placeHolder1
   * 占位符1*/
  private String placeHolder2;
  /** 1 if true, -1 if false
   * 1 为真，-1 为假*/
  private int maker;
  /** Fee 费用*/
  private BigDecimal fee;
  /** Fee currency  费用币种*/
  private String feeCurrency;

  public Date getTimestamp() {
    return new Date(timestamp);
  }
}
