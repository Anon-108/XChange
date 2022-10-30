package org.knowm.xchange.binance.dto.marketdata;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

/**
 * Binance价格数量
 */
public final class BinancePriceQuantity {
  /**
   * 符号
   */
  public final String symbol;
  /**
   * 收购价 /买价
   */
  public final BigDecimal bidPrice;
  /**
   * 收购 /买数量
   */
  public final BigDecimal bidQty;
  /**
   * 卖价；卖盘价
   */
  public final BigDecimal askPrice;
  /**
   * 卖；卖盘数量
   */
  public final BigDecimal askQty;

  /**
   * Binance价格数量
   * @param symbol 符号
   * @param bidPrice 收购价 /买价
   * @param bidQty 收购 /买数量
   * @param askPrice 卖价；卖盘价
   * @param askQty 卖；卖盘数量
   */
  public BinancePriceQuantity(
      @JsonProperty("symbol") String symbol,
      @JsonProperty("bidPrice") BigDecimal bidPrice,
      @JsonProperty("bidQty") BigDecimal bidQty,
      @JsonProperty("askPrice") BigDecimal askPrice,
      @JsonProperty("askQty") BigDecimal askQty) {
    this.symbol = symbol;
    this.bidPrice = bidPrice;
    this.bidQty = bidQty;
    this.askPrice = askPrice;
    this.askQty = askQty;
  }
}
