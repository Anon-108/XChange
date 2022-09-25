package org.knowm.xchange.exceptions;

/**
 * Indicates that the cause the error ware wrong credentials or insufficient privileges.
 * 指示错误的原因是凭据错误或权限不足。
 *
 * <p>We throw this exception only for exchanges where we can’t clearly distinguish this cause from
  other error types. If an API does not provide proper error information or the modules
  implementation is lacking then an ExchangeException will be thrown in this situation.
 <p>我们仅在无法明确区分此原因的交易所抛出此异常
 其他错误类型。 如果 API 未提供正确的错误信息或模块
 缺少实现，则在这种情况下将引发 ExchangeException。
 *
 * @author walec51
 */
public class ExchangeSecurityException extends ExchangeException {

  private static final String DEFAULT_MESSAGE = "Wrong credentials or insufficient privileges 凭据错误或权限不足";

  public ExchangeSecurityException() {
    super(DEFAULT_MESSAGE);
  }

  public ExchangeSecurityException(String message) {
    super(message);
  }

  public ExchangeSecurityException(Throwable cause) {
    super(DEFAULT_MESSAGE, cause);
  }

  public ExchangeSecurityException(String message, Throwable cause) {
    super(message, cause);
  }
}
