package org.knowm.xchange.bitfinex.v1.dto.trade;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;

/**
 * Bitfinex 取消订单请求
 */
public class BitfinexCancelOrderRequest {

  @JsonProperty("request")
  protected String request;

  @JsonProperty("nonce")
  protected String nonce;

  @JsonProperty("order_id")
  @JsonRawValue
  private long orderId;

  /**
   * Constructor
   *
   * @param nonce
   * @param orderId
   */
  public BitfinexCancelOrderRequest(String nonce, long orderId) {

    this.request = "/v1/order/cancel";
    this.orderId = orderId;
    this.nonce = nonce;
  }

  public String getOrderId() {

    return String.valueOf(orderId);
  }
}
