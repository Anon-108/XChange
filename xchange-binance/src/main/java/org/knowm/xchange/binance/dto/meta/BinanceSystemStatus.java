package org.knowm.xchange.binance.dto.meta;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Binance系统状态
 */
public class BinanceSystemStatus {

  // 0: normal，1：system maintenance 0:正常,1:系统维护
  @JsonProperty private String status;
  // normal or system maintenance 正常或系统维护
  @JsonProperty private String msg;

  public String getStatus() {
    return status;
  }

  public String getMsg() {
    return msg;
  }
}
