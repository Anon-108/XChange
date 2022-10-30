package org.knowm.xchange.binance.dto.meta;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

/**
 * 币安时间
 */
public class BinanceTime {
  /**
   * 服务器时间
   */
  @JsonProperty private long serverTime;

  /**
   * 获取服务器时间
   * @return
   */
  public Date getServerTime() {
    return new Date(serverTime);
  }
}
