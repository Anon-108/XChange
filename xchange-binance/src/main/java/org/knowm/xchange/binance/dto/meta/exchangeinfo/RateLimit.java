package org.knowm.xchange.binance.dto.meta.exchangeinfo;

/**
 * 速率限制
 */
public class RateLimit {
  /**
   * 限制
   */
  private String limit;
  /**
   * 间隔时间，间隙
   */
  private String interval;
  /**
   * 间隔，间隙 数
   */
  private String intervalNum;
  /**
   * 速率限制类型
   */
  private String rateLimitType;

  public String getLimit() {
    return limit;
  }

  public void setLimit(String limit) {
    this.limit = limit;
  }

  public String getInterval() {
    return interval;
  }

  public void setInterval(String interval) {
    this.interval = interval;
  }

  public String getIntervalNum() {
    return intervalNum;
  }

  public void setIntervalNum(String intervalNum) {
    this.intervalNum = intervalNum;
  }

  public String getRateLimitType() {
    return rateLimitType;
  }

  public void setRateLimitType(String rateLimitType) {
    this.rateLimitType = rateLimitType;
  }

  @Override
  public String toString() {
    return "ClassPojo [limit = "
        + limit
        + ", interval = "
        + interval
        + ", intervalNum = "
        + intervalNum
        + ", rateLimitType = "
        + rateLimitType
        + "]";
  }
}
