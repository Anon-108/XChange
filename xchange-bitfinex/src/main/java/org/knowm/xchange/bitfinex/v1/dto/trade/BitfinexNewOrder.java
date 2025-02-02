package org.knowm.xchange.bitfinex.v1.dto.trade;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

/**
 * 新订单
 */
public class BitfinexNewOrder {

  @JsonProperty("symbol")
  protected String symbol;

  @JsonProperty("exchange")
  protected String exchange;
  /**
   * 某人左方或右方的）一边
   */
  @JsonProperty("side")
  protected String side;

  @JsonProperty("type")
  protected String type;

  @JsonProperty("amount")
  protected BigDecimal amount;
  /**
   * 价格
   */
  @JsonProperty("price")
  protected BigDecimal price;

  public BitfinexNewOrder(
      String symbol,
      String exchange,
      String side,
      String type,
      BigDecimal amount,
      BigDecimal price) {

    this.symbol = symbol;
    this.exchange = exchange;
    this.side = side;
    this.type = type;
    this.amount = amount;
    this.price = price;
  }

  public String getSymbol() {

    return symbol;
  }

  public void setSymbol(String symbol) {

    this.symbol = symbol;
  }

  public String getExchange() {

    return exchange;
  }

  public void setExchange(String exchange) {

    this.exchange = exchange;
  }

  public String getSide() {

    return side;
  }

  public void setSide(String side) {

    this.side = side;
  }

  public String getType() {

    return type;
  }

  public void setType(String type) {

    this.type = type;
  }

  public BigDecimal getAmount() {

    return amount;
  }

  public void setAmount(BigDecimal amount) {

    this.amount = amount;
  }

  public BigDecimal getPrice() {

    return price;
  }

  public void setPrice(BigDecimal price) {

    this.price = price;
  }
}
