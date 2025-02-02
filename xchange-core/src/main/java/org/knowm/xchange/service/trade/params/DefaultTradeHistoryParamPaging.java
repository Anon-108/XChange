package org.knowm.xchange.service.trade.params;

/** Common implementation of {@link TradeHistoryParamPaging} interface
 * {@link TradeHistoryParamPaging} 接口的常见实现 */
public class DefaultTradeHistoryParamPaging implements TradeHistoryParamPaging {

  private Integer pageLength;
  /** 0-based page number
   * 基于 0 的页码 */
  private Integer pageNumber;

  public DefaultTradeHistoryParamPaging() {}

  public DefaultTradeHistoryParamPaging(Integer pageLength) {
    this(pageLength, 0);
  }

  /** @param pageNumber 0-based page number
   * 基于 0 的页码*/
  public DefaultTradeHistoryParamPaging(Integer pageLength, Integer pageNumber) {

    this.pageLength = pageLength;
    this.pageNumber = pageNumber;
  }

  @Override
  public Integer getPageLength() {

    return pageLength;
  }

  @Override
  public void setPageLength(Integer pageLength) {

    this.pageLength = pageLength;
  }

  /** 0-based page number
   * 基于 0 的页码*/
  @Override
  public Integer getPageNumber() {

    return pageNumber;
  }

  /** 0-based page number
   * 基于 0 的页码*/
  @Override
  public void setPageNumber(Integer pageNumber) {

    this.pageNumber = pageNumber;
  }
}
