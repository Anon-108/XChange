package org.knowm.xchange.binance.dto.meta.exchangeinfo;

/**
 * 过滤器
 */
public class Filter {
  /**
   * 最高价格
   */
  private String maxPrice;
  /**
   * 过滤器类型
   */
  private String filterType;
  /**
   * tick/钩? 大小
   */
  private String tickSize;
  /**
   * 最低价
   */
  private String minPrice;
  /**
   * 最小数量
   */
  private String minQty;
  /**
   * 最大数量
   */
  private String maxQty;
  /**
   * 步骤 /台阶/移动 大小
   */
  private String stepSize;
  /**
   * 最小 概念的，纯理论的?
   */
  private String minNotional;

  public String getMaxPrice() {
    return maxPrice;
  }

  public void setMaxPrice(String maxPrice) {
    this.maxPrice = maxPrice;
  }

  public String getFilterType() {
    return filterType;
  }

  public void setFilterType(String filterType) {
    this.filterType = filterType;
  }

  public String getTickSize() {
    return tickSize;
  }

  public void setTickSize(String tickSize) {
    this.tickSize = tickSize;
  }

  public String getMinPrice() {
    return minPrice;
  }

  public void setMinPrice(String minPrice) {
    this.minPrice = minPrice;
  }

  public String getMinQty() {
    return minQty;
  }

  public void setMinQty(String minQty) {
    this.minQty = minQty;
  }

  public String getMaxQty() {
    return maxQty;
  }

  public void setMaxQty(String maxQty) {
    this.maxQty = maxQty;
  }

  public String getStepSize() {
    return stepSize;
  }

  public void setStepSize(String stepSize) {
    this.stepSize = stepSize;
  }

  public String getMinNotional() {
    return minNotional;
  }

  public void setMinNotional(String minNotional) {
    this.minNotional = minNotional;
  }

  @Override
  public String toString() {
    return "Filter{"
        + "maxPrice='"
        + maxPrice
        + '\''
        + ", filterType='"
        + filterType
        + '\''
        + ", tickSize='"
        + tickSize
        + '\''
        + ", minPrice='"
        + minPrice
        + '\''
        + ", minQty='"
        + minQty
        + '\''
        + ", maxQty='"
        + maxQty
        + '\''
        + ", stepSize='"
        + stepSize
        + '\''
        + ", minNotional='"
        + minNotional
        + '\''
        + '}';
  }
}
