package org.knowm.xchange.bitfinex.v1;

/**
 * 订单类型
 */
public enum BitfinexOrderType {
  MARKET("exchange market"),
  LIMIT("exchange limit"),
  STOP("exchange stop"),
  STOP_LIMIT("exchange stop limit"),
  TRAILING_STOP("exchange trailing-stop"),
  FILL_OR_KILL("exchange fill-or-kill"),
  MARGIN_MARKET("market"),
  MARGIN_LIMIT("limit"),
  MARGIN_STOP("stop"),
  MARGIN_STOP_LIMIT("stop limit"),
  MARGIN_TRAILING_STOP("trailing-stop"),
  MARGIN_FILL_OR_KILL("fill-or-kill");

  private String value;

  BitfinexOrderType(String value) {

    this.value = value;
  }

  public String getValue() {

    return value;
  }

  @Override
  public String toString() {

    return this.getValue();
  }
}
