package org.knowm.xchange.binance.dto.trade;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 币安监听键
 * Created by cyril on 11-Oct-17. */
public class BinanceListenKey {
  /**
   * 监听键
   */
  private String listenKey;

  /**
   * 币安监听键
   * @param listenKey 监听键
   */
  public BinanceListenKey(@JsonProperty("listenKey") String listenKey) {
    this.listenKey = listenKey;
  }

  public String getListenKey() {
    return listenKey;
  }

  public void setListenKey(String listenKey) {
    this.listenKey = listenKey;
  }
}
