package org.knowm.xchange.binance.dto.trade;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.List;

/**
 * 新订单
 */
public final class BinanceNewOrder {

  /**
   * BinanceNewOrder 所需的响应类型。
   *
   * Desired response type for BinanceNewOrder. */
  public enum NewOrderResponseType {
    ACK,
    RESULT,
    FULL
  }

  /**
   * 符号
   */
  public final String symbol;
  /**
   * 订单id
   */
  public final long orderId;
  /**
   * 客户/端订单id
   */
  public final String clientOrderId;
  /**
   * 交易时间
   */
  public final long transactTime;
  /**
   * 价格
   */
  public final BigDecimal price;
  /**
   * 起源（origin）；起初的 数量
   */
  public final BigDecimal origQty;
  /**
   * 实施；完成 数量
   */
  public final BigDecimal executedQty;
  /**
   * 状态
   */
  public final OrderStatus status;
  /**
   * 有效时间/生效时间
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
   * 填充
   */
  public final List<BinanceTrade> fills;

  /**
   * 新订单
   * @param symbol 符号
   * @param orderId 订单id
   * @param clientOrderId 客户/端订单id
   * @param transactTime 交易时间
   * @param price 价格
   * @param origQty 起源（origin）；起初的 数量
   * @param executedQty 实施；完成 数量
   * @param status 状态
   * @param timeInForce 有效时间/生效时间
   * @param type 类型
   * @param side （事物左方或右方的）一旁；（由想像的中线分出的）一边，一侧；
   * @param fills 填充
   */
  public BinanceNewOrder(
      @JsonProperty("symbol") String symbol,
      @JsonProperty("orderId") long orderId,
      @JsonProperty("clientOrderId") String clientOrderId,
      @JsonProperty("transactTime") long transactTime,
      @JsonProperty("price") BigDecimal price,
      @JsonProperty("origQty") BigDecimal origQty,
      @JsonProperty("executedQty") BigDecimal executedQty,
      @JsonProperty("status") OrderStatus status,
      @JsonProperty("timeInForce") TimeInForce timeInForce,
      @JsonProperty("type") OrderType type,
      @JsonProperty("side") OrderSide side,
      @JsonProperty("fills") List<BinanceTrade> fills) {
    super();
    this.symbol = symbol;
    this.orderId = orderId;
    this.clientOrderId = clientOrderId;
    this.transactTime = transactTime;
    this.price = price;
    this.origQty = origQty;
    this.executedQty = executedQty;
    this.status = status;
    this.timeInForce = timeInForce;
    this.type = type;
    this.side = side;
    this.fills = fills;
  }
}
