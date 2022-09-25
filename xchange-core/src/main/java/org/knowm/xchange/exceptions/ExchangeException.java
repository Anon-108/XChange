package org.knowm.xchange.exceptions;

/**
 * Exception to provide the following to:
 * 向以下人员提供以下信息的异常情况：
 *
 * <ul>
 *   <li>Indication of generic Exchange exception
 *   <li>通用Exchange异常的指示
 * </ul>
 */
public class ExchangeException extends RuntimeException {

  /**
   * Constructs an <code>ExchangeException</code> with the specified detail message.
   * 使用指定的详细消息构造 <code>ExchangeException</code>。
   *
   * @param message the detail message.
   *                详细信息。
   */
  public ExchangeException(String message) {

    super(message);
  }

  /**
   * Constructs an <code>ExchangeException</code> with the specified cause.
   * 构造具有指定原因的 <code>ExchangeException</code>。
   *
   * @param cause the underlying cause.
   *              根本原因。
   */
  public ExchangeException(Throwable cause) {

    super(cause);
  }

  /**
   * Constructs an <code>ExchangeException</code> with the specified detail message and cause.
   * 使用指定的详细消息和原因构造 <code>ExchangeException</code>。
   *
   * @param message the detail message.
   *                详细信息。
   *
   * @param cause the underlying cause.
   *              根本原因。
   */
  public ExchangeException(String message, Throwable cause) {

    super(message, cause);
  }
}
