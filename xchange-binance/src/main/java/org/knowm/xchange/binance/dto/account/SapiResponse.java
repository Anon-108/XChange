package org.knowm.xchange.binance.dto.account;

/**
 * Sapi响应
 * @param <T>
 */
public abstract class SapiResponse<T> {

  public abstract T getData();
}
