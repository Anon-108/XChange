package org.knowm.xchange.bitfinex.v1.dto.account;

/**
 * Bitfinex 交易费用请求
 */
public class BitfinexTradingFeesRequest extends BitfinexEmptyRequest {

  /**
   * Constructor
   *
   * @param nonce
   */
  public BitfinexTradingFeesRequest(String nonce) {
    super(nonce, "/v1/account_infos");
  }
}
