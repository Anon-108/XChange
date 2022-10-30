package org.knowm.xchange.derivative;

import org.knowm.xchange.currency.CurrencyPair;

/**
 *  派生物，衍生物
 */
public interface Derivative {
  /**
   * 获取货币对
   * @return
   */
  CurrencyPair getCurrencyPair();
}
