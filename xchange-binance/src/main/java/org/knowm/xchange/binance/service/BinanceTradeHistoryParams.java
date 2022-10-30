package org.knowm.xchange.binance.service;

import java.util.Date;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.service.trade.params.TradeHistoryParamCurrencyPair;
import org.knowm.xchange.service.trade.params.TradeHistoryParamLimit;
import org.knowm.xchange.service.trade.params.TradeHistoryParamsIdSpan;
import org.knowm.xchange.service.trade.params.TradeHistoryParamsTimeSpan;

/**
 * 币安交易历史参数
 */
public class BinanceTradeHistoryParams
    implements TradeHistoryParamCurrencyPair,
        TradeHistoryParamLimit,
        TradeHistoryParamsIdSpan,
        TradeHistoryParamsTimeSpan {

  /** 货币对
   * mandatory  强制的 /强制性 / */
  private CurrencyPair currencyPair;
  /**
   * 限制/额度
   * optional
   * 可选择的，选修的
   * */
  private Integer limit;
  /**
   * 开始id
   * optional
   * 可选择的，选修的 */
  private String startId;
  /**
   * 结束id
   * ignored
   * 忽略 */
  private String endId;
  /**
   * 开始时间
   * optional
   * 可选择的，选修的 */
  private Date startTime;
  /**
   * 结束时间
   * optional
   * 可选择的，选修的 */
  private Date endTime;

  public BinanceTradeHistoryParams(CurrencyPair currencyPair) {
    this.currencyPair = currencyPair;
  }

  public BinanceTradeHistoryParams() {}

  public CurrencyPair getCurrencyPair() {
    return currencyPair;
  }

  public void setCurrencyPair(CurrencyPair currencyPair) {
    this.currencyPair = currencyPair;
  }

  public Integer getLimit() {
    return limit;
  }

  public void setLimit(Integer limit) {
    this.limit = limit;
  }

  public String getStartId() {
    return startId;
  }

  public void setStartId(String startId) {
    this.startId = startId;
  }

  public String getEndId() {
    return endId;
  }

  public void setEndId(String endId) {
    this.endId = endId;
  }

  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  public Date getEndTime() {
    return endTime;
  }

  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }
}
