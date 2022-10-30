package org.knowm.xchange.binance.service;

import java.util.Date;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.account.FundingRecord.Type;
import org.knowm.xchange.service.trade.params.HistoryParamsFundingType;
import org.knowm.xchange.service.trade.params.TradeHistoryParamCurrency;
import org.knowm.xchange.service.trade.params.TradeHistoryParamsTimeSpan;

/**
 * 币安资金历史参数
 */
public class BinanceFundingHistoryParams
    implements TradeHistoryParamCurrency, TradeHistoryParamsTimeSpan, HistoryParamsFundingType {
  /**
   * 货币
   */
  private Currency currency;
  /**
   * 类型
   */
  private Type type;
  /**
   * 开始时间
   */
  private Date startTime;
  /**
   * 结束时间
   */
  private Date endTime;

  @Override
  public Currency getCurrency() {
    return currency;
  }

  @Override
  public void setCurrency(Currency currency) {
    this.currency = currency;
  }

  @Override
  public Type getType() {
    return type;
  }

  @Override
  public void setType(Type type) {
    this.type = type;
  }

  @Override
  public Date getStartTime() {
    return startTime;
  }

  @Override
  public void setStartTime(Date startTime) {
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
}
