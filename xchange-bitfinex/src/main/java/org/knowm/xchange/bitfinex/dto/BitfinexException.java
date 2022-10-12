package org.knowm.xchange.bitfinex.dto;

/**
 * Bitfinex 异常
 */
public class BitfinexException extends RuntimeException {

  public BitfinexException() {}

  public BitfinexException(String message) {
    super(message);
  }
}
