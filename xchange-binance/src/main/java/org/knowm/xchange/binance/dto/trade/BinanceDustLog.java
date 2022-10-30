package org.knowm.xchange.binance.dto.trade;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * 币安灰尘记录
 */
public final class BinanceDustLog {
  /**
   *合计 /总数
   */
  private final Integer total;
  /**
   * 少量,小额?运球?
   */
  private final List<BinanceDribblets> dribblets;

  /**
   * 币安灰尘记录
   * @param total 合计 /总数
   * @param dribblets 少量,小额?运球?
   */
  public BinanceDustLog(
      @JsonProperty("total") Integer total,
      @JsonProperty("userAssetDribblets") List<BinanceDribblets> dribblets) {
    this.total = total;
    this.dribblets = dribblets;
  }

  public Integer getTotal() {
    return total;
  }

  public List<BinanceDribblets> getDribblets() {
    return dribblets;
  }
}
