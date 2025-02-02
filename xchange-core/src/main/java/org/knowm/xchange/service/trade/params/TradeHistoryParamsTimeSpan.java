package org.knowm.xchange.service.trade.params;

import java.util.Date;
import org.knowm.xchange.service.trade.TradeService;

/**
 * Parameters type for {@link TradeService#getTradeHistory(TradeHistoryParams)} with start and end timestamps.
 * * {@link TradeService#getTradeHistory(TradeHistoryParams)} 的参数类型，带有开始和结束时间戳。
 */
public interface TradeHistoryParamsTimeSpan extends TradeHistoryParams {

  Date getStartTime();

  void setStartTime(Date startTime);

  Date getEndTime();

  void setEndTime(Date endTime);
}
