package org.knowm.xchange.bitfinex.v1.dto.account;

/**
 * bitfinex余额请求
 */
public class BitfinexBalancesRequest extends BitfinexEmptyRequest {

  /**
   * Constructor
   *
   * @param nonce
   */
  public BitfinexBalancesRequest(String nonce) {
    super(nonce, "/v1/balances");
  }
}
