package org.knowm.xchange.dto.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.knowm.xchange.currency.Currency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DTO representing a balance in a currency
 * * DTO 代表一种货币的余额
 *
 * <p>This is simply defined by an amount of money in a given currency, contained in the cash object.
 * * <p>这只是由现金对象中包含的给定货币的金额定义。
 *
 * <p>This class is immutable.
 * * <p>这个类是不可变的。
 */
@JsonDeserialize(builder = Balance.Builder.class)
public final class Balance implements Comparable<Balance>, Serializable {

  private static final long serialVersionUID = -1460694403597268635L;
  private static final Logger log = LoggerFactory.getLogger(Balance.class);

  private final Currency currency;

  // Invariant:
  //不变量：
  // total = available + frozen - borrowed + loaned + withdrawing + depositing;
  // 总计 = 可用 + 冻结 - 借入 + 借出 + 取款 + 存款；
  private final BigDecimal total;
  private final BigDecimal available;
  private final BigDecimal frozen;
  private final BigDecimal loaned;
  private final BigDecimal borrowed;
  private final BigDecimal withdrawing;
  private final BigDecimal depositing;
  private final Date timestamp;

  /**
   * Constructs a balance, the {@link #available} will be the same as the <code>total</code>, and
   the {@link #frozen} is zero. The <code>borrowed</code> and <code>loaned</code> will be zero.
   构造一个余额，{@link #available} 将与 <code>total</code> 相同，并且
   {@link #frozen} 为零。 <code>borrowed</code> 和 <code>loaned</code> 将为零。
   *
   * @param currency The underlying currency
   *                 基础货币
   * @param total The total 总数
   */
  public Balance(Currency currency, BigDecimal total) {

    this(
        currency,
        total,
        total,
        BigDecimal.ZERO,
        BigDecimal.ZERO,
        BigDecimal.ZERO,
        BigDecimal.ZERO,
        BigDecimal.ZERO,
        null);
  }

  /**
   * Constructs a balance, the {@link #frozen} will be assigned as <code>total</code> - <code>
   available</code>. The <code>borrowed</code> and <code>loaned</code> will be zero.
   构造一个余额，{@link #frozen} 将被分配为 <code>total</code> - <code>
   可用</code>。 <code>borrowed</code> 和 <code>loaned</code> 将为零。
   *
   * @param currency the underlying currency of this balance.
   *                 该余额的基础货币。
   *
   * @param total the total amount of the <code>currency</code> in this balance.
   *              此余额中<code>currency</code> 的总金额。
   *
   * @param available the amount of the <code>currency</code> in this balance that is available to  trade.
   *                  此余额中可供交易的 <code>currency</code> 的金额。
   */
  public Balance(Currency currency, BigDecimal total, BigDecimal available) {

    this(
        currency,
        total,
        available,
        total.add(available.negate()),
        BigDecimal.ZERO,
        BigDecimal.ZERO,
        BigDecimal.ZERO,
        BigDecimal.ZERO);
  }

  /**
   * Constructs a balance. The <code>borrowed</code> and <code>loaned</code> will be zero.
   * * 构建平衡。 <code>borrowed</code> 和 <code>loaned</code> 将为零。
   *
   * @param currency the underlying currency of this balance.
   *                 该余额的基础货币。
   *
   * @param total the total amount of the <code>currency</code> in this balance, including the <code>available</code> and <code>frozen</code>.
   *              * @param total 该余额中<code>currency</code>的总金额，包括<code>available</code>和<code>frozen</code>。
   *
   * @param available the amount of the <code>currency</code> in this balance that is available to   trade.
   *                  此余额中可供交易的 <code>currency</code> 的金额。
   *
   * @param frozen the frozen amount of the <code>currency</code> in this balance that is locked in  trading.
   *               此余额中被锁定在交易中的<code>currency</code>的冻结金额。
   */
  public Balance(Currency currency, BigDecimal total, BigDecimal available, BigDecimal frozen) {

    this(
        currency,
        total,
        available,
        frozen,
        BigDecimal.ZERO,
        BigDecimal.ZERO,
        BigDecimal.ZERO,
        BigDecimal.ZERO,
        null);
  }

  /**
   * Constructs a balance.
   * 构建平衡。
   *
   * @param currency the underlying currency of this balance.
   *                 该余额的基础货币。
   *
   * @param total the total amount of the <code>currency</code> in this balance, equal to <code>  available + frozen - borrowed + loaned</code>.
   *              此余额中<code>货币</code>的总金额，等于<code>可用+冻结-借入+借出</code>。
   *
   * @param available the amount of the <code>currency</code> in this balance that is available to   trade, including the <code>borrowed</code>.
   *                  此余额中可供交易的 <code>currency</code> 金额，包括 <code>借入</code>。
   *
   * @param frozen the frozen amount of the <code>currency</code> in this balance that is locked in  trading.
   *               此余额中被锁定在交易中的<code>currency</code>的冻结金额。
   *
   * @param borrowed the borrowed amount of the available <code>currency</code> in this balance that   must be repaid.
   *                 此余额中必须偿还的可用<code>货币</code>的借入金额。
   *
   * @param loaned the loaned amount of the total <code>currency</code> in this balance that will be  returned.
   *               此余额中将归还的总 <code>currency</code> 的借出金额。
   *
   * @param withdrawing the amount of the <code>currency</code> in this balance that is scheduled  for withdrawal.
   *                    此余额中计划提款的 <code>currency</code> 金额。
   *
   * @param depositing the amount of the <code>currency</code> in this balance that is being   deposited but not available yet.
   *                   此余额中正在存入但尚不可用的 <code>currency</code> 金额。
   *
   * @param timestamp Time the balance was valid on the exchange server
   *                  余额在交换服务器上有效的时间
   */
  public Balance(
      Currency currency,
      BigDecimal total,
      BigDecimal available,
      BigDecimal frozen,
      BigDecimal borrowed,
      BigDecimal loaned,
      BigDecimal withdrawing,
      BigDecimal depositing,
      Date timestamp) {

    if (total != null && available != null) {
      BigDecimal sum =
          available.add(frozen).subtract(borrowed).add(loaned).add(withdrawing).add(depositing);
      if (0 != total.compareTo(sum)) {
        log.warn(
            "{} = total != available + frozen - borrowed + loaned + withdrawing + depositing = 总计！= 可用 + 冻结 - 借入 + 借出 + 取款 + 存款 ={}",
            total,
            sum);
      }
    }
    this.currency = currency;
    this.total = total;
    this.available = available;
    this.frozen = frozen;
    this.borrowed = borrowed;
    this.loaned = loaned;
    this.withdrawing = withdrawing;
    this.depositing = depositing;
    this.timestamp = timestamp;
  }

  /**
   * Constructs a balance.
   * 构建平衡。
   *
   * @param currency the underlying currency of this balance.
   *                 该余额的基础货币。
   *
   * @param total the total amount of the <code>currency</code> in this balance, equal to <code>   available + frozen - borrowed + loaned</code>.
   *              此余额中<code>货币</code>的总金额，等于<code>可用+冻结-借入+借出</code>。
   *
   * @param available the amount of the <code>currency</code> in this balance that is available to  trade, including the <code>borrowed</code>.
   *                  此余额中可供交易的 <code>currency</code> 金额，包括 <code>借入</code>。
   *
   * @param frozen the frozen amount of the <code>currency</code> in this balance that is locked in  trading.
   *               此余额中被锁定在交易中的<code>currency</code>的冻结金额。
   *
   * @param borrowed the borrowed amount of the available <code>currency</code> in this balance that    must be repaid.
   *                 此余额中必须偿还的可用<code>货币</code>的借入金额。
   *
   * @param loaned the loaned amount of the total <code>currency</code> in this balance that will be returned.
   *               此余额中将归还的总 <code>currency</code> 的借出金额。
   *
   * @param withdrawing the amount of the <code>currency</code> in this balance that is scheduled   for withdrawal.
   *                    此余额中计划提款的 <code>currency</code> 金额。
   *
   * @param depositing the amount of the <code>currency</code> in this balance that is being  deposited but not available yet.
   *                   此余额中正在存入但尚不可用的 <code>currency</code> 金额。
   */
  public Balance(
      Currency currency,
      BigDecimal total,
      BigDecimal available,
      BigDecimal frozen,
      BigDecimal borrowed,
      BigDecimal loaned,
      BigDecimal withdrawing,
      BigDecimal depositing) {

    if (total != null && available != null) {
      BigDecimal sum =
          available.add(frozen).subtract(borrowed).add(loaned).add(withdrawing).add(depositing);
      if (0 != total.compareTo(sum)) {
        log.warn(
            "{} = total != available + frozen - borrowed + loaned + withdrawing + depositing 总计 != 可用 + 冻结 - 借入 + 借出 + 取款 + 存款 = {}",
            total,
            sum);
      }
    }
    this.currency = currency;
    this.total = total;
    this.available = available;
    this.frozen = frozen;
    this.borrowed = borrowed;
    this.loaned = loaned;
    this.withdrawing = withdrawing;
    this.depositing = depositing;
    this.timestamp = null;
  }

  /**
   * Returns a zero balance.
   * 返回零余额。
   *
   * @param currency the balance currency.
   *                 余额货币。
   *
   * @return a zero balance.
   * @return 零余额。
   */
  public static Balance zero(Currency currency) {

    return new Balance(
        currency,
        BigDecimal.ZERO,
        BigDecimal.ZERO,
        BigDecimal.ZERO,
        BigDecimal.ZERO,
        BigDecimal.ZERO,
        BigDecimal.ZERO,
        BigDecimal.ZERO,
        null);
  }

  public Currency getCurrency() {

    return currency;
  }

  /**
   * Returns the total amount of the <code>currency</code> in this balance.
   * 返回此余额中<code>currency</code> 的总金额。
   *
   * @return the total amount.
   * * @return 总金额。
   */
  public BigDecimal getTotal() {

    if (total == null) {
      return available.add(frozen).subtract(borrowed).add(loaned).add(withdrawing).add(depositing);
    } else {
      return total;
    }
  }

  /**
   * Returns the amount of the <code>currency</code> in this balance that is available to trade.
   * * 返回此余额中可供交易的 <code>currency</code> 的金额。
   *
   * @return the amount that is available to trade.
   * @return 可交易的金额。
   */
  public BigDecimal getAvailable() {

    if (available == null) {
      return total
          .subtract(frozen)
          .subtract(loaned)
          .add(borrowed)
          .subtract(withdrawing)
          .subtract(depositing);
    } else {
      return available;
    }
  }

  /**
   * Returns the amount of the <code>currency</code> in this balance that may be withdrawn. Equal to <code>available - borrowed</code>.
   * * 返回此余额中<code>currency</code> 可以提取的金额。 等于 <code>available - borrowed</code>。
   *
   * @return the amount that is available to withdraw.
   * * @return 可提取的金额。
   */
  @JsonIgnore
  public BigDecimal getAvailableForWithdrawal() {

    return getAvailable().subtract(getBorrowed());
  }

  /**
   * Returns the frozen amount of the <code>currency</code> in this balance that is locked in trading.
   * 返回此余额中被锁定在交易中的 <code>currency</code> 的冻结金额。
   *
   * @return the amount that is locked in open orders.
   * @return 锁定在未结订单中的金额。
   */
  public BigDecimal getFrozen() {

    if (frozen == null) {
      return total.subtract(available);
    }
    return frozen;
  }

  /**
   * Returns the borrowed amount of the available <code>currency</code> in this balance that must be repaid.
   * * 返回此余额中必须偿还的可用<code>currency</code>的借入金额。
   *
   * @return the amount that must be repaid.
   * @return 必须偿还的金额。
   */
  public BigDecimal getBorrowed() {

    return borrowed;
  }

  /**
   * Returns the loaned amount of the total <code>currency</code> in this balance that will be returned.
   * * 返回此余额中将归还的总<code>currency</code>的借出金额。
   *
   * @return that amount that is loaned out.
   * @return 借出的金额。
   */
  public BigDecimal getLoaned() {

    return loaned;
  }

  /**
   * Returns the amount of the <code>currency</code> in this balance that is locked in withdrawal
   * 返回此余额中被锁定在提款中的 <code>currency</code> 的金额
   *
   * @return the amount in withdrawal.
   * @return 取款金额。
   */
  public BigDecimal getWithdrawing() {

    return withdrawing;
  }

  /**
   * Returns the amount of the <code>currency</code> in this balance that is locked in deposit
   * 返回此余额中锁定在存款中的 <code>currency</code> 金额
   *
   * @return the amount in deposit.
   * * @return 存款金额。
   */
  public BigDecimal getDepositing() {

    return depositing;
  }

  /**
   * Returns the time the balance was valid on the exchange server
   * * 返回余额在交易所服务器上的有效时间
   *
   * @return the timestamp.
   * * @return 时间戳。
   */
  public Date getTimestamp() {
    return timestamp;
  }

  @Override
  public String toString() {
    return "Balance{"
        + "currency="
        + currency
        + ", total="
        + total
        + ", available="
        + available
        + ", frozen="
        + frozen
        + ", loaned="
        + loaned
        + ", borrowed="
        + borrowed
        + ", withdrawing="
        + withdrawing
        + ", depositing="
        + depositing
        + ", timestamp="
        + timestamp
        + '}';
  }

  @Override
  public int hashCode() {

    final int prime = 31;
    int result = 1;
    result = prime * result + ((total == null) ? 0 : total.hashCode());
    result = prime * result + ((currency == null) ? 0 : currency.hashCode());
    result = prime * result + ((available == null) ? 0 : available.hashCode());
    result = prime * result + ((frozen == null) ? 0 : frozen.hashCode());
    result = prime * result + ((borrowed == null) ? 0 : borrowed.hashCode());
    result = prime * result + ((loaned == null) ? 0 : loaned.hashCode());
    result = prime * result + ((withdrawing == null) ? 0 : withdrawing.hashCode());
    result = prime * result + ((depositing == null) ? 0 : depositing.hashCode());
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
    Balance other = (Balance) obj;
    if (total == null) {
      if (other.total != null) {
        return false;
      }
    } else if (!total.equals(other.total)) {
      return false;
    }
    if (available == null) {
      if (other.available != null) {
        return false;
      }
    } else if (!available.equals(other.available)) {
      return false;
    }
    if (frozen == null) {
      if (other.frozen != null) {
        return false;
      }
    } else if (!frozen.equals(other.frozen)) {
      return false;
    }
    if (currency == null) {
      if (other.currency != null) {
        return false;
      }
    } else if (!currency.equals(other.currency)) {
      return false;
    }
    if (borrowed == null) {
      if (other.borrowed != null) {
        return false;
      }
    } else if (!borrowed.equals(other.borrowed)) {
      return false;
    }
    if (loaned == null) {
      if (other.loaned != null) {
        return false;
      }
    } else if (!loaned.equals(other.loaned)) {
      return false;
    }
    if (withdrawing == null) {
      if (other.withdrawing != null) {
        return false;
      }
    } else if (!withdrawing.equals(other.withdrawing)) {
      return false;
    }
    if (depositing == null) {
      if (other.depositing != null) {
        return false;
      }
    } else if (!depositing.equals(other.depositing)) {
      return false;
    }
    return true;
  }

  @Override
  public int compareTo(Balance other) {

    int comparison = currency.compareTo(other.currency);
    if (comparison != 0) return comparison;
    comparison = total.compareTo(other.total);
    if (comparison != 0) return comparison;
    comparison = available.compareTo(other.available);
    if (comparison != 0) return comparison;
    comparison = frozen.compareTo(other.frozen);
    if (comparison != 0) return comparison;
    comparison = borrowed.compareTo(other.borrowed);
    if (comparison != 0) return comparison;
    comparison = loaned.compareTo(other.loaned);
    if (comparison != 0) return comparison;
    comparison = withdrawing.compareTo(other.withdrawing);
    if (comparison != 0) return comparison;
    comparison = depositing.compareTo(other.depositing);
    return comparison;
  }

  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder {

    private Currency currency;
    private BigDecimal total;
    private BigDecimal available;
    private BigDecimal frozen;
    private BigDecimal borrowed = BigDecimal.ZERO;
    private BigDecimal loaned = BigDecimal.ZERO;
    private BigDecimal withdrawing = BigDecimal.ZERO;
    private BigDecimal depositing = BigDecimal.ZERO;
    private Date timestamp;

    public static Builder from(Balance balance) {

      return new Builder()
          .currency(balance.getCurrency())
          .total(balance.getTotal())
          .available(balance.getAvailable())
          .frozen(balance.getFrozen())
          .borrowed(balance.getBorrowed())
          .loaned(balance.getLoaned())
          .withdrawing(balance.getWithdrawing())
          .depositing(balance.getDepositing())
          .timestamp(balance.getTimestamp());
    }

    public Builder currency(Currency currency) {

      this.currency = currency;
      return this;
    }

    public Builder total(BigDecimal total) {

      this.total = total;
      return this;
    }

    public Builder available(BigDecimal available) {

      this.available = available;
      return this;
    }

    public Builder frozen(BigDecimal frozen) {

      this.frozen = frozen;
      return this;
    }

    public Builder borrowed(BigDecimal borrowed) {

      this.borrowed = borrowed;
      return this;
    }

    public Builder loaned(BigDecimal loaned) {

      this.loaned = loaned;
      return this;
    }

    public Builder withdrawing(BigDecimal withdrawing) {

      this.withdrawing = withdrawing;
      return this;
    }

    public Builder depositing(BigDecimal depositing) {

      this.depositing = depositing;
      return this;
    }

    public Builder timestamp(Date timestamp) {

      this.timestamp = timestamp;
      return this;
    }

    public Balance build() {

      if (frozen == null) {
        if (total == null || available == null) {
          frozen = BigDecimal.ZERO;
        } else if (total != null) {
          frozen = total.subtract(available);
        }
      }

      return new Balance(
          currency, total, available, frozen, borrowed, loaned, withdrawing, depositing, timestamp);
    }
  }
}
