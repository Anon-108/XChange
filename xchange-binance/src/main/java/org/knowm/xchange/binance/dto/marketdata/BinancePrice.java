package org.knowm.xchange.binance.dto.marketdata;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.utils.Assert;
import org.knowm.xchange.utils.jackson.CurrencyPairDeserializer;

/**
 * Binance价格
 */
public final class BinancePrice implements Comparable<BinancePrice> {

  /**
   * 对
   */
  private final CurrencyPair pair;
  /**
   * 价格
   */
  private final BigDecimal price;

  /**
   * Binance价格
   * @param symbol 符号
   * @param price 价格
   */
  public BinancePrice(
      @JsonProperty("symbol") String symbol, @JsonProperty("price") BigDecimal price) {
    this(CurrencyPairDeserializer.getCurrencyPairFromString(symbol), price);
  }

  /**
   * Binance价格
   * @param pair 对
   * @param price 价格
   */
  public BinancePrice(CurrencyPair pair, BigDecimal price) {
    Assert.notNull(price, "Null price");
    Assert.notNull(pair, "Null pair");
    this.pair = pair;
    this.price = price;
  }

  public CurrencyPair getCurrencyPair() {
    return pair;
  }

  public BigDecimal getPrice() {
    return price;
  }

  @Override
  public int compareTo(BinancePrice o) {
    if (pair.compareTo(o.pair) == 0) return price.compareTo(o.price);
    return pair.compareTo(o.pair);
  }

  @Override
  public int hashCode() {
    int result = 1;
    result = 31 * result + ((pair == null) ? 0 : pair.hashCode());
    result = 31 * result + ((price == null) ? 0 : price.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof BinancePrice)) return false;
    BinancePrice other = (BinancePrice) obj;
    return pair.equals(other.pair) && price.equals(other.price);
  }

  public String toString() {
    return "[" + pair + "] => " + price;
  }
}
