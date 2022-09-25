package org.knowm.xchange.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.knowm.xchange.dto.Order.OrderType;

/** Data object representing an order for a loan
 * 表示贷款订单的数据对象*/
public class LoanOrder implements Serializable {

  private static final long serialVersionUID = -8311018082902024121L;

  /** Order type i.e. bid or ask
   * 订单类型，即出价或询价*/
  private final OrderType type;

  /** The loan currency
   * 贷款货币 */
  private final String currency;

  /** Amount to be ordered / amount that was ordered
   * 订购数量/订购数量*/
  private final BigDecimal originalAmount;

  /** Duration of loan in days
   * 贷款期限（天） */
  private final int dayPeriod;

  /** An identifier that uniquely identifies the order
   * 唯一标识订单的标识符 */
  private final String id;

  /** The timestamp on the order according to the exchange's server, null if not provided
   * 根据交易所服务器的订单时间戳，如果没有提供则为空*/
  private final Date timestamp;

  /**
   * Constructor
   *
   * @param type Order type i.e. bid or ask
   *             订单类型，即出价或询价
   *
   * @param currency The loan currency
   *                 贷款货币
   *
   * @param originalAmount Amount to be ordered / amount that was ordered
   *                       订购数量/订购数量
   *
   * @param dayPeriod Duration of loan in days
   *                  贷款期限（天）
   *
   * @param id An identifier that uniquely identifies the order
   *           唯一标识订单的标识符
   *
   * @param timestamp The timestamp on the order according to the exchange's server, null if not  provided
   *                  根据交易所服务器的订单时间戳，如果没有提供则为空
   */
  public LoanOrder(
      OrderType type,
      String currency,
      BigDecimal originalAmount,
      int dayPeriod,
      String id,
      Date timestamp) {

    this.type = type;
    this.currency = currency;
    this.originalAmount = originalAmount;
    this.dayPeriod = dayPeriod;
    this.id = id;
    this.timestamp = timestamp;
  }

  public OrderType getType() {

    return type;
  }

  public String getCurrency() {

    return currency;
  }

  public BigDecimal getOriginalAmount() {

    return originalAmount;
  }

  public int getDayPeriod() {

    return dayPeriod;
  }

  public String getId() {

    return id;
  }

  public Date getTimestamp() {

    return timestamp;
  }

  @Override
  public String toString() {

    return "LoanOrder [type="
        + type
        + ", currency="
        + currency
        + ", originalAmount="
        + originalAmount
        + ", dayPeriod="
        + dayPeriod
        + ", id="
        + id
        + ", timestamp="
        + timestamp
        + "]";
  }

  @Override
  public int hashCode() {

    final int prime = 31;
    int result = 1;
    result = prime * result + ((currency == null) ? 0 : currency.hashCode());
    result = prime * result + dayPeriod;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
    result = prime * result + ((originalAmount == null) ? 0 : originalAmount.hashCode());
    result = prime * result + ((type == null) ? 0 : type.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {

    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    LoanOrder other = (LoanOrder) obj;
    if (currency == null) {
      if (other.currency != null) {
        return false;
      }
    } else if (!currency.equals(other.currency)) {
      return false;
    }
    if (dayPeriod != other.dayPeriod) {
      return false;
    }
    if (id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!id.equals(other.id)) {
      return false;
    }
    if (timestamp == null) {
      if (other.timestamp != null) {
        return false;
      }
    } else if (!timestamp.equals(other.timestamp)) {
      return false;
    }
    if (originalAmount == null) {
      if (other.originalAmount != null) {
        return false;
      }
    } else if (!originalAmount.equals(other.originalAmount)) {
      return false;
    }
    return type == other.type;
  }
}
