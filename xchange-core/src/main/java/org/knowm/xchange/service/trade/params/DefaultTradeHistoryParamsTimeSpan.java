package org.knowm.xchange.service.trade.params;

import java.util.Date;

/**
 * 默认交易历史参数时间跨度
 * Common implementation of {@link TradeHistoryParamsTimeSpan}.
 * {@link TradeHistoryParamsTimeSpan} 的通用实现。 */
public class DefaultTradeHistoryParamsTimeSpan implements TradeHistoryParamsTimeSpan {

  private Date endTime;
  private Date startTime;

  public DefaultTradeHistoryParamsTimeSpan() {}

  public DefaultTradeHistoryParamsTimeSpan(Date startTime, Date endTime) {

    this.endTime = endTime;
    this.startTime = startTime;
  }

  public DefaultTradeHistoryParamsTimeSpan(Date startTime) {

    this.startTime = startTime;
  }

  @Override
  public Date getEndTime() {

    return endTime;
  }

  @Override
  public void setEndTime(Date endTime) {

    this.endTime = endTime;
  }

  @Override
  public Date getStartTime() {

    return startTime;
  }

  @Override
  public void setStartTime(Date time) {

    startTime = time;
  }
}
