package org.knowm.xchange.exceptions;

/** An exception indicating there are not enough funds for the action requested
 * 指示没有足够资金用于请求的操作的异常*/
public class FundsExceededException extends ExchangeException {

  public FundsExceededException(String message) {
    super(message);
  }

  public FundsExceededException(Throwable e) {
    super(e);
  }

  public FundsExceededException(String message, Throwable cause) {
    super(message, cause);
  }

  public FundsExceededException() {
    super("Not enough funds are available. 没有足够的资金可用。");
  }
}
