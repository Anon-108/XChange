package org.knowm.xchange.dto.trade;

import java.math.BigDecimal;
import java.util.Date;
import org.knowm.xchange.dto.LoanOrder;
import org.knowm.xchange.dto.Order;

/**
 * DTO representing a floating rate loan order A floating rate loan order is a loan order whose rate
  is determined by the market. This type of loan order can be preferable for creditors when loans
  have a callable provision (i.e. the debtor can choose to pay off the loan early and acquire another loan at a more favorable rate).
 DTO 代表浮动利率贷款订单 浮动利率贷款订单是一种贷款订单，其利率
 是由市场决定的。 这种类型的贷款令在贷款时更适合债权人
 有可赎回条款（即债务人可以选择提前还清贷款并以更优惠的利率获得另一笔贷款）。
 */
public final class FloatingRateLoanOrder extends LoanOrder
    implements Comparable<FloatingRateLoanOrder> {

  private static final long serialVersionUID = -1474202797547840095L;

  private BigDecimal rate;

  /**
   * @param type Either BID (debtor) or ASK (creditor)
   *             BID（债务人）或 ASK（债权人）
   *
   * @param currency The loan currency code
   *                 贷款货币代码
   *
   * @param originalAmount Units of currency
   *                       货币单位
   *
   * @param dayPeriod Loan duration in days
   *                  贷款期限（天）
   *
   * @param id An id (usually provided by the exchange)
   *           一个 id（通常由交易所提供）
   *
   * @param timestamp The absolute time for this order
   *                  此订单的绝对时间
   */
  public FloatingRateLoanOrder(
      Order.OrderType type,
      String currency,
      BigDecimal originalAmount,
      int dayPeriod,
      String id,
      Date timestamp,
      BigDecimal rate) {

    super(type, currency, originalAmount, dayPeriod, id, timestamp);
    this.rate = rate;
  }

  public BigDecimal getRate() {

    return rate;
  }

  public void setRate(BigDecimal rate) {

    this.rate = rate;
  }

  @Override
  public int compareTo(FloatingRateLoanOrder order) {

    return this.getDayPeriod() - order.getDayPeriod();
  }
}
