package org.knowm.xchange.binance.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import lombok.Data;

/**
 * 资产红利响应
 */
public final class AssetDividendResponse
    extends SapiResponse<List<AssetDividendResponse.AssetDividend>> {
  /**
   * 行
   */
  private final AssetDividend[] rows;

  /**
   *  总的，全部的
   */
  private final BigDecimal total;

  /**
   * 资产红利响应
   * @param rows
   * @param total
   */
  public AssetDividendResponse(
      @JsonProperty("rows") AssetDividend[] rows, @JsonProperty("total") BigDecimal total) {
    this.rows = rows;
    this.total = total;
  }
  /**
   * 获取数据
   */
  @Override
  public List<AssetDividend> getData() {
    return Arrays.asList(rows);
  }

  public BigDecimal getTotal() {
    return total;
  }

  @Override
  public String toString() {
    return "assetDividendRow [rows=" + Arrays.toString(rows) + "]";
  }

  @Data
  public static final class AssetDividend {
    /**
     * 数量，量
     */
    private BigDecimal amount;
    /**
     * 资产
     */
    private String asset;
    /**
     * div 时间
     */
    private long divTime;
    /**
     * en信息
     */
    private String enInfo;
    private long tranId;

    public BigDecimal getAmount() {
      return amount;
    }

    public String getAsset() {
      return asset;
    }

    public long getDivTime() {
      return divTime;
    }

    public String getEnInfo() {
      return enInfo;
    }

    public long getTranId() {
      return tranId;
    }
  }
}
