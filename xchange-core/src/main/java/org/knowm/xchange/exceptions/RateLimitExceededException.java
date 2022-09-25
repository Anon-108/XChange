package org.knowm.xchange.exceptions;

/** An exception indicating there the rate limit for making requests has been exceeded
 * 一个异常，表明已超出发出请求的速率限制 */
public class RateLimitExceededException extends ExchangeException {

  public RateLimitExceededException(String message) {
    super(message);
  }

  public RateLimitExceededException(Throwable e) {
    super(e);
  }

  public RateLimitExceededException(String message, Throwable cause) {
    super(message, cause);
  }

  public RateLimitExceededException() {
    super("Rate limit for making requests exceeded! 超出了发出请求的速率限制！");
  }
}
