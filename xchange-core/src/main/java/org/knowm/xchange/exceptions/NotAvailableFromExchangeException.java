package org.knowm.xchange.exceptions;

/**
 * Exception to provide the following to API:
 * 向 API 提供以下内容的例外情况：
 *
 * <ul>
 *   <li>Indication that the exchange does not support the requested function or data
 *   <li>表示交易所不支持请求的功能或数据
 * </ul>
 */
public class NotAvailableFromExchangeException extends UnsupportedOperationException {

  /**
   * Constructor
   *
   * @param message Message 信息
   */
  public NotAvailableFromExchangeException(String message) {

    super(message);
  }

  /** Constructor */
  public NotAvailableFromExchangeException() {

    this("Requested Information or function from Exchange is not available. 从 Exchange 请求的信息或功能不可用。");
  }
}
