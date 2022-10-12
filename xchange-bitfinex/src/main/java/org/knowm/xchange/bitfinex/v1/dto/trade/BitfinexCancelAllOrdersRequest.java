package org.knowm.xchange.bitfinex.v1.dto.trade;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Bitfinex 取消所有订单请求
 */
public class BitfinexCancelAllOrdersRequest {

  @JsonProperty("request")
  protected String request;

  @JsonProperty("nonce")
  protected String nonce;

  /**
   * Constructor
   *
   * @param nonce
   */
  public BitfinexCancelAllOrdersRequest(String nonce) {

    this.request = "/v1/order/cancel/all";
    this.nonce = nonce;
  }
}
