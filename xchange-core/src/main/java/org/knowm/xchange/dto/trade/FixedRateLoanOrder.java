package org.knowm.xchange.dto.trade;

import java.math.BigDecimal;
import java.util.Date;
import org.knowm.xchange.dto.LoanOrder;
import org.knowm.xchange.dto.Order.OrderType;

/**
 * DTO representing a fixed rate loan order A fixed rate loan order lets you specify a fixed rate
  for your loan order. When offering loan orders, you should be aware as to whether or not loans
  have callable or putable provisions. These provisions can serve to be advantageous to either the debtor or the creditor.
 代表固定利率贷款订单的 DTO 固定利率贷款订单可让您指定固定利率
 为您的贷款订单。 在提供贷款订单时，您应该注意是否贷款
 有可赎回或可赎回的规定。 这些规定可能对债务人或债权人都有利。
 */
public final class FixedRateLoanOrder extends LoanOrder implements Comparable<FixedRateLoanOrder> {

  private static final long serialVersionUID = 2627042395091155053L;

  /** The fixed rate of return for a day
   * 一天的固定收益率*/
  private final BigDecimal rate;

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
   *
   * @param rate The fixed rate of return for a day
   *             一天的固定收益率
   */
  public FixedRateLoanOrder(
      OrderType type,
      String currency,
      BigDecimal originalAmount,
      int dayPeriod,
      String id,
      Date timestamp,
      BigDecimal rate) {

    super(type, currency, originalAmount, dayPeriod, id, timestamp);

    this.rate = rate;
  }

  /** @return The fixed rate of return for a day
   * 一天的固定收益率*/
  public BigDecimal getRate() {

    return rate;
  }

  @Override
  public int compareTo(FixedRateLoanOrder fixedRateLoanOrder) {

    if (!this.getRate().equals(fixedRateLoanOrder.getRate())) {
      return this.getRate().compareTo(fixedRateLoanOrder.getRate());
    } else {
      return this.getDayPeriod() - fixedRateLoanOrder.getDayPeriod();
    }
  }

  @Override
  public int hashCode() {

    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((rate == null) ? 0 : rate.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {

    if (this == obj) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    FixedRateLoanOrder other = (FixedRateLoanOrder) obj;
    if (rate == null) {
      if (other.rate != null) {
        return false;
      }
    } else if (!rate.equals(other.rate)) {
      return false;
    }
    return true;
  }
}
