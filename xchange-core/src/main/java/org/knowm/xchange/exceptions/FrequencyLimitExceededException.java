package org.knowm.xchange.exceptions;

/** An exception indicating the request rate limit has been exceeded
 * 表示已超出请求速率限制的异常*/
public class FrequencyLimitExceededException extends ExchangeException {

  public FrequencyLimitExceededException(String message) {
    super(message);
  }

  public FrequencyLimitExceededException() {
    super("Too many attempts made in a given time window. 在给定的时间窗口内进行了太多尝试。");
  }
}
