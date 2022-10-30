package org.knowm.xchange.binance.dto.trade;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单类
 */
public final class BinanceOrder {
  /**
   * 符号
   */
  public final String symbol;
  /**
   * 订单id
   */
  public final long orderId;
  /**
   * 客户订单id
   */
  public final String clientOrderId;
  /**
   * 价格
   */
  public final BigDecimal price;
  /**
   * 其实数量
   */
  public final BigDecimal origQty;
  /**
   * 执行数量
   */
  public final BigDecimal executedQty;
  /**
   * 累计报价数量
   */
  public final BigDecimal cummulativeQuoteQty;
  /**
   * 状态
   */
  public final OrderStatus status;
  /**
   * 生效/有效时间
   */
  public final TimeInForce timeInForce;
  /**
   * 类型
   */
  public final OrderType type;
  /**
   * （事物左方或右方的）一旁；（由想像的中线分出的）一边，一侧；
   */
  public final OrderSide side;
  /**
   * 停止价格
   */
  public final BigDecimal stopPrice;
  /**
   * 冰山数量
   */
  public final BigDecimal icebergQty;
  /**
   * 时间
   */
  public final long time;

  /**
   * 订单
   *
   * @param symbol 符号
   * @param orderId 订单id
   * @param clientOrderId 客户订单id
   * @param price 价格
   * @param origQty 起始数量
   * @param executedQty 执行数量
   * @param cummulativeQuoteQty 累计报价数量
   * @param status 状态
   * @param timeInForce 生效/有效时间
   * @param type 类型
   * @param side 事物左方或右方的）一旁；（由想像的中线分出的）一边，一侧；
   * @param stopPrice 停止价格
   * @param icebergQty 冰山数量
   * @param time 时间
   */
  public BinanceOrder(
      @JsonProperty("symbol") String symbol,
      @JsonProperty("orderId") long orderId,
      @JsonProperty("clientOrderId") String clientOrderId,
      @JsonProperty("price") BigDecimal price,
      @JsonProperty("origQty") BigDecimal origQty,
      @JsonProperty("executedQty") BigDecimal executedQty,
      @JsonProperty("cummulativeQuoteQty") BigDecimal cummulativeQuoteQty,
      @JsonProperty("status") OrderStatus status,
      @JsonProperty("timeInForce") TimeInForce timeInForce,
      @JsonProperty("type") OrderType type,
      @JsonProperty("side") OrderSide side,
      @JsonProperty("stopPrice") BigDecimal stopPrice,
      @JsonProperty("icebergQty") BigDecimal icebergQty,
      @JsonProperty("time") long time) {
    this.symbol = symbol;
    this.orderId = orderId;
    this.clientOrderId = clientOrderId;
    this.price = price;
    this.origQty = origQty;
    this.executedQty = executedQty;
    this.cummulativeQuoteQty = cummulativeQuoteQty;
    this.status = status;
    this.timeInForce = timeInForce;
    this.type = type;
    this.side = side;
    this.stopPrice = stopPrice;
    this.icebergQty = icebergQty;
    this.time = time;
  }

  /**
   * 获取时间
   * @return
   */
  public Date getTime() {
    return new Date(time);
  }
}
