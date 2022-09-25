package org.knowm.xchange.exceptions;

/**
 * Exception indicating that the amount in the order you tried to place of verify was under the minimum accepted by the exchange
 * 异常表明您尝试验证的订单中的金额低于交易所接受的最低金额
 */
public class OrderAmountUnderMinimumException extends OrderNotValidException {

  public OrderAmountUnderMinimumException() {
    super("Orders amount under minimum 订单金额低于最低限额");
  }

  public OrderAmountUnderMinimumException(String message) {
    super(message);
  }

  public OrderAmountUnderMinimumException(String message, Throwable cause) {
    super(message, cause);
  }
}
