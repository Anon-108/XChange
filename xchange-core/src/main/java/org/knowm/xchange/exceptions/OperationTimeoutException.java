package org.knowm.xchange.exceptions;

/**
 * Exception indicating that the operation took to long and the exchange decided to timeout it
  <p>This exception should only be thrown in situations in which you know that the operation was
  disregarded due to the timeout. This is in contrast to timeouts like {@link java.net.SocketTimeoutException} where despite the exception the request might have been compleated on the server.
 异常表明操作花费了很长时间并且交易所决定超时
 <p>只有在您知道该操作是
 由于超时而忽略。 这与 {@link java.net.SocketTimeoutException} 之类的超时形成对比，尽管出现异常，但请求可能已在服务器上完成。
 */
public class OperationTimeoutException extends OrderNotValidException {

  public OperationTimeoutException() {
    super("Operation took to long and the exchange decided to timeout it 操作花费了很长时间，交易所决定将其超时");
  }

  public OperationTimeoutException(String message) {
    super(message);
  }

  public OperationTimeoutException(String message, Throwable cause) {
    super(message, cause);
  }
}
