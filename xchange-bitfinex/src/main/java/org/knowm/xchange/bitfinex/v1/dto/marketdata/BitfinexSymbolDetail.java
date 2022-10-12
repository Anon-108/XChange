package org.knowm.xchange.bitfinex.v1.dto.marketdata;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

/**
 *
 * Bitfinex 符号详细信息
 * <code>[
 * {
 * "pair": "btcusd",
 * "price_precision": 5,
 * "initial_margin": "30.0",
 * "minimum_margin": "15.0",
 * "maximum_order_size": "2000.0",
 * "minimum_order_size": "0.002",
 * "expiration": "NA",
 * "margin": true
 * },
 * {
 * "pair": "ltcusd",
 * "price_precision": 5,
 * "initial_margin": "30.0",
 * "minimum_margin": "15.0",
 * "maximum_order_size": "5000.0",
 * "minimum_order_size": "0.08",
 * "expiration": "NA",
 * "margin": true
 * },
 * ...
 * ]</code>
 *
 * @author ujjwal on 23/02/18.
 */
public class BitfinexSymbolDetail {
  /**
   * 对
   */
  private final String pair;
  /**
   * 价格精度
   */
  private final int price_precision;
  /**
   * 初始保证金
   */
  private final BigDecimal initial_margin;
  /**
   * 最低保证金
   */
  private final BigDecimal minimum_margin;
  /**
   * 最大订单大小
   */
  private final BigDecimal maximum_order_size;
  /**
   * 最小订单大小
   */
  private final BigDecimal minimum_order_size;
  /**
   * `到期/结束
   */
  private final String expiration;
  /**
   * 保证金
   */
  private final boolean margin;

  public BitfinexSymbolDetail(
      @JsonProperty("pair") String pair,
      @JsonProperty("price_precision") int price_precision,
      @JsonProperty("initial_margin") BigDecimal initial_margin,
      @JsonProperty("minimum_margin") BigDecimal minimum_margin,
      @JsonProperty("maximum_order_size") BigDecimal maximum_order_size,
      @JsonProperty("minimum_order_size") BigDecimal minimum_order_size,
      @JsonProperty("expiration") String expiration,
      @JsonProperty("margin") boolean margin) {
    this.pair = pair;
    this.price_precision = price_precision;
    this.initial_margin = initial_margin;
    this.minimum_margin = minimum_margin;
    this.maximum_order_size = maximum_order_size;
    this.minimum_order_size = minimum_order_size;
    this.expiration = expiration;
    this.margin = margin;
  }

  public String getPair() {
    return pair;
  }

  public int getPrice_precision() {
    return price_precision;
  }

  public BigDecimal getInitial_margin() {
    return initial_margin;
  }

  public BigDecimal getMinimum_margin() {
    return minimum_margin;
  }

  public BigDecimal getMaximum_order_size() {
    return maximum_order_size;
  }

  public BigDecimal getMinimum_order_size() {
    return minimum_order_size;
  }

  public String getExpiration() {
    return expiration;
  }

  public boolean isMargin() {
    return margin;
  }
}
