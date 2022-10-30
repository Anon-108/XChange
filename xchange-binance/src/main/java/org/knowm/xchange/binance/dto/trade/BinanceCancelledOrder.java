package org.knowm.xchange.binance.dto.trade;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Binance取消订单
 */
public final class BinanceCancelledOrder {
  /**
   * 符号
   */
  public final String symbol;
  /**
   * 原始客户订单 ID
   */
  public final String origClientOrderId;
  /**
   * 订单id
   */
  public final long orderId;
  /**
   * 客户/客户端订单Id
   */
  public final String clientOrderId;
  /**
   * 价格
   */
  public String price;
  /**
   * 起初的/起始 数量
   */
  public String origQty;
  /**
   * 实施；完成 数量
   */
  public String executedQty;
  /**
   * 累计报价数量
   */
  public String cummulativeQuoteQty;
  /**
   * 状态
   */
  public String status;
  /**
   *有效时间/生效时间
   */
  public String timeInForce;
  /**
   * 类型
   */
  public String type;
  /**
   * （事物左方或右方的）一旁；（由想像的中线分出的）一边，一侧；
   */
  public String side;

  /**
   * Binance取消订单
   * @param symbol 符号
   * @param origClientOrderId  原始客户订单 ID
   * @param orderId 订单id
   * @param clientOrderId 客户/客户端订单Id
   * @param price 价格
   * @param origQty 起初的/起始 数量
   * @param executedQty 实施；完成 数量
   * @param cummulativeQuoteQty 累计报价数量
   * @param status 状态
   * @param timeInForce 有效时间/生效时间
   * @param type 类型
   * @param side 事物左方或右方的）一旁；（由想像的中线分出的）一边，一侧；
   */
  public BinanceCancelledOrder(
      @JsonProperty("symbol") String symbol,
      @JsonProperty("origClientOrderId") String origClientOrderId,
      @JsonProperty("orderId") long orderId,
      @JsonProperty("clientOrderId") String clientOrderId,
      @JsonProperty("price") String price,
      @JsonProperty("origQty") String origQty,
      @JsonProperty("executedQty") String executedQty,
      @JsonProperty("cummulativeQuoteQty") String cummulativeQuoteQty,
      @JsonProperty("status") String status,
      @JsonProperty("timeInForce") String timeInForce,
      @JsonProperty("type") String type,
      @JsonProperty("side") String side) {
    super();
    this.symbol = symbol;
    this.origClientOrderId = origClientOrderId;
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
  }
}
