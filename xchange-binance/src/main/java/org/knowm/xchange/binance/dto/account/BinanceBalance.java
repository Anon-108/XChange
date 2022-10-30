package org.knowm.xchange.binance.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import org.knowm.xchange.currency.Currency;

/**
 * 币安余额
 */
public final class BinanceBalance {

  /**
   *  通货 /货币
   */
  private final Currency currency;
  /**
   * . 免费的；自由的/费用?
   */
  private final BigDecimal free;
  /**
   * 锁定 /反锁 /锁护
   */
  private final BigDecimal locked;

  /**
   * 币安余额
   * @param asset 资产
   * @param free 免费的；自由的/费用?
   * @param locked 锁定/反锁 /锁护
   */
  public BinanceBalance(
      @JsonProperty("asset") String asset,
      @JsonProperty("free") BigDecimal free,
      @JsonProperty("locked") BigDecimal locked) {
    this.currency = Currency.getInstance(asset);
    this.locked = locked;
    this.free = free;
  }

  public Currency getCurrency() {
    return currency;
  }

  public BigDecimal getTotal() {
    return free.add(locked);
  }

  public BigDecimal getAvailable() {
    return free;
  }

  public BigDecimal getLocked() {
    return locked;
  }

  public String toString() {
    return "[" + currency + ", free=" + free + ", locked=" + locked + "]";
  }
}
