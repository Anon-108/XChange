package org.knowm.xchange.binance.dto.marketdata;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

/**
 * Binance符号价格
 */
public final class BinanceSymbolPrice {
  /**
   * 符号
   */
  public final String symbol;
  /**
   * 价格
   */
  public final BigDecimal price;

  /**
   * Binance符号价格
   * @param symbol  符号
   * @param price 价格
   */
  public BinanceSymbolPrice(
      @JsonProperty("symbol") String symbol, @JsonProperty("price") BigDecimal price) {
    this.symbol = symbol;
    this.price = price;
  }
}
