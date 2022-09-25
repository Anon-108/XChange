package org.knowm.xchange.service.trade.params;

public interface TradeHistoryParamPaging extends TradeHistoryParams {

  Integer getPageLength();

  void setPageLength(Integer pageLength);

  /** 0-based page number
   * * 基于 0 的页码*/
  Integer getPageNumber();

  /** 0-based page number
   * 基于 0 的页码*/
  void setPageNumber(Integer pageNumber);
}
