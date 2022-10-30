package org.knowm.xchange.binance.dto.marketdata;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import org.knowm.xchange.binance.BinanceAdapters;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Ticker;

/**
 * 币安簿册代码
 */
public final class BinanceBookTicker {
  /**
   * 修改id
   */
  public long updateId;
  /**
   * （成双的两物品）一对
   */
  private CurrencyPair pair;
  /**
   * 买价 /买入价
   */
  private final BigDecimal bidPrice;
  /**
   * 买价 /买入数量
   */
  private final BigDecimal bidQty;
  /**
   * 卖价；卖盘价
   */
  private final BigDecimal askPrice;
  /**
   * 卖；卖盘数量
   */
  private final BigDecimal askQty;
  /**
   *  象征，标志；符号
   */
  private final String symbol;

  // The cached ticker 缓存的代码
  private Ticker ticker;

  /**
   * 币安簿册代码
   * @param bidPrice 买价 /买入价
   * @param bidQty 买价 /买入数量
   * @param askPrice 卖价；卖盘价
   * @param askQty 卖；卖盘数量
   * @param symbol 象征，标志；符号
   */
  public BinanceBookTicker(
      @JsonProperty("bidPrice") BigDecimal bidPrice,
      @JsonProperty("bidQty") BigDecimal bidQty,
      @JsonProperty("askPrice") BigDecimal askPrice,
      @JsonProperty("askQty") BigDecimal askQty,
      @JsonProperty("symbol") String symbol) {
    this.bidPrice = bidPrice;
    this.bidQty = bidQty;
    this.askPrice = askPrice;
    this.askQty = askQty;
    this.symbol = symbol;
  }

  public long getUpdateId() {
    return updateId;
  }

  public void setUpdateId(long updateId) {
    this.updateId = updateId;
  }

  public CurrencyPair getCurrencyPair() {
    return pair;
  }

  public void setCurrencyPair(CurrencyPair pair) {
    this.pair = pair;
  }

  public BigDecimal getBidPrice() {
    return bidPrice;
  }

  public BigDecimal getBidQty() {
    return bidQty;
  }

  public BigDecimal getAskPrice() {
    return askPrice;
  }

  public BigDecimal getAskQty() {
    return askQty;
  }

  public String getSymbol() {
    return symbol;
  }

  public synchronized Ticker toTicker() {
    CurrencyPair currencyPair = pair;
    if (currencyPair == null) {
      currencyPair = BinanceAdapters.adaptSymbol(symbol);
    }
    if (ticker == null) {
      ticker =
          new Ticker.Builder()
              .currencyPair(currencyPair)
              .ask(askPrice)
              .bid(bidPrice)
              .askSize(askQty)
              .bidSize(bidQty)
              .build();
    }
    return ticker;
  }
}
