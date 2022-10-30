package org.knowm.xchange.binance.service;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.service.trade.params.orders.OrderQueryParamCurrencyPair;

/**
 * 币安查询订单参数
 */
public class BinanceQueryOrderParams implements OrderQueryParamCurrencyPair {
  /**
   * 订单id
   */
  private String orderId;
  /**
   * 货币对
   */
  private CurrencyPair pair;

  /**
   * 币安查询订单参数
   */
  public BinanceQueryOrderParams() {}

  /**
   * 币安查询订单参数
   * @param pair
   * @param orderId
   */
  public BinanceQueryOrderParams(CurrencyPair pair, String orderId) {
    this.pair = pair;
    this.orderId = orderId;
  }

  @Override
  public CurrencyPair getCurrencyPair() {
    return pair;
  }

  @Override
  public void setCurrencyPair(CurrencyPair pair) {
    this.pair = pair;
  }

  @Override
  public String getOrderId() {
    return orderId;
  }

  @Override
  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }
}
