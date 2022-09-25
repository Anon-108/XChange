package org.knowm.xchange.bitfinex.v2.dto.trade;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** https://docs.bitfinex.com/v2/reference#rest-auth-trades-hist */
@JsonFormat(shape = JsonFormat.Shape.ARRAY)
@Setter
@Getter
@ToString
public class Trade {

  /** Trade database id
   * 贸易数据库编号*/
  private String id;
  /** Pair (BTCUSD, …)
   * 对 (BTCUSD, ...) */
  private String symbol;
  /** Execution timestamp millis
   * 执行时间戳毫秒 */
  private long timestamp;
  /** Order id
   * 订单编号 */
  private String orderId;
  /** Positive means buy, negative means sell
   * 正表示买入，负表示卖出*/
  private BigDecimal execAmount;
  /** Execution price
   * 执行价格*/
  private BigDecimal execPrice;
  /** Order type
   * 订单类型*/
  private String orderType;
  /** Order price
   * 订购价格*/
  private BigDecimal orderPrice;
  /** 1 if true, -1 if false
   * 1 为真，-1 为假*/
  private int maker;
  /** Fee 费用*/
  private BigDecimal fee;
  /** Fee currency 费用币种*/
  private String feeCurrency;

  public Date getTimestamp() {
    return new Date(timestamp);
  }
}
