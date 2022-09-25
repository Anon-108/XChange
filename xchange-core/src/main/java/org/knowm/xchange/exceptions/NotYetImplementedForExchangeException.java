package org.knowm.xchange.exceptions;

/**
 * Exception to provide the following to API:
 * 向 API 提供以下内容的例外情况：
 *
 * <ul>
 *   <li>Indication that the exchange supports the requested function or data, but it's not yet been  implemented
 *   * <li>表示交易所支持请求的功能或数据，但尚未实现
 * </ul>
 */
public class NotYetImplementedForExchangeException extends UnsupportedOperationException {

  /**
   * Constructor
   *
   * @param message Message 信息
   */
  public NotYetImplementedForExchangeException(String message) {

    super(message);
  }

  /** Constructor */
  public NotYetImplementedForExchangeException() {

    this("Feature not yet implemented for exchange. 尚未实现交换功能。");
  }
}
