package org.knowm.xchange.exceptions;

/**
 * An exception indicating that the server is overloaded and the service is temporally unavailable i.e. when server return an http error 502
 * 表示服务器过载且服务暂时不可用的异常，即服务器返回 http 错误 502
 */
public class SystemOverloadException extends ExchangeUnavailableException {

  public SystemOverloadException(String message) {
    super(message);
  }

  public SystemOverloadException(Throwable e) {
    super(e);
  }

  public SystemOverloadException(String message, Throwable cause) {
    super(message, cause);
  }

  public SystemOverloadException() {
    super("The system is currently overloaded. 系统当前过载。");
  }
}
