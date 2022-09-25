package org.knowm.xchange.exceptions;

/** Exception indicating place of verify an order which was not valid
 * 异常指示验证无效订单的地点*/
public class OrderNotValidException extends ExchangeException {

  public OrderNotValidException() {
    super("Invalid order 无效订单");
  }

  public OrderNotValidException(String message) {
    super(message);
  }

  public OrderNotValidException(String message, Throwable cause) {
    super(message, cause);
  }
}
