package org.knowm.xchange.binance.service;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.service.trade.params.CancelOrderByCurrencyPair;
import org.knowm.xchange.service.trade.params.CancelOrderByIdParams;

/**
 * 币安取消订单参数
 */
public class BinanceCancelOrderParams implements CancelOrderByIdParams, CancelOrderByCurrencyPair {
  /**
   * 订单id
   */
  private final String orderId;
  /**
   * 货币对
   */
  private final CurrencyPair pair;

  /**
   * 币安取消订单参数
   * @param pair
   * @param orderId
   */
  public BinanceCancelOrderParams(CurrencyPair pair, String orderId) {
    this.pair = pair;
    this.orderId = orderId;
  }

  @Override
  public CurrencyPair getCurrencyPair() {
    return pair;
  }

  @Override
  public String getOrderId() {
    return orderId;
  }
}
