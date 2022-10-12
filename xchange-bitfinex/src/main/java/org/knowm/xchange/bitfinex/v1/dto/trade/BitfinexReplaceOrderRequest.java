package org.knowm.xchange.bitfinex.v1.dto.trade;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

/**
 * Bitfinex 更换订单请求
 */
public class BitfinexReplaceOrderRequest extends BitfinexNewOrderRequest {
  /**
   * 取代；（用……）替换，订单id
   */
  @JsonProperty("order_id")
  protected long replaceOrderId;
  /**
   * 使用 剩下的，遗留的
   */
  @JsonProperty("use_remaining")
  protected boolean useRemaining = false;

  public BitfinexReplaceOrderRequest(
      String nonce,
      long replaceOrderId,
      String symbol,
      BigDecimal amount,
      BigDecimal price,
      String exchange,
      String side,
      String type,
      boolean isHidden,
      boolean isPostOnly,
      boolean useRemaining) {

    super(nonce, symbol, amount, price, exchange, side, type, isHidden, isPostOnly, null);

    request = "/v1/order/cancel/replace";
    this.replaceOrderId = replaceOrderId;
    this.useRemaining = useRemaining;
  }
}
