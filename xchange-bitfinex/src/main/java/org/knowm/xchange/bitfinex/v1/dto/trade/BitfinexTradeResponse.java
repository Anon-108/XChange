package org.knowm.xchange.bitfinex.v1.dto.trade;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

/**
 * 交易响应
 */
public class BitfinexTradeResponse {

  private final BigDecimal price;
  private final BigDecimal amount;
  private final BigDecimal timestamp;
  private final String exchange;
  private final String type;
  private final String tradeId;
  private final String orderId;
  /**
   * 费用数量
   */
  private final BigDecimal feeAmount;
  /**
   *费用 /手续费 货币
   */
  private final String feeCurrency;

  /**
   * Constructor
   *
   * @param price 价格
   * @param amount 数量
   * @param timestamp 时间戳
   * @param exchange 交换
   * @param type 类型
   * @param tradeId 贸易编号
   * @param orderId 订单号
   * @param feeAmount 收费多少
   * @param feeCurrency 费用货币
   */
  public BitfinexTradeResponse(
      @JsonProperty("price") final BigDecimal price,
      @JsonProperty("amount") final BigDecimal amount,
      @JsonProperty("timestamp") final BigDecimal timestamp,
      @JsonProperty("exchange") final String exchange,
      @JsonProperty("type") final String type,
      @JsonProperty("tid") final String tradeId,
      @JsonProperty("order_id") final String orderId,
      @JsonProperty("fee_amount") final BigDecimal feeAmount,
      @JsonProperty("fee_currency") String feeCurrency) {

    this.price = price;
    this.amount = amount;
    this.timestamp = timestamp;
    this.exchange = exchange;
    this.type = type;
    this.tradeId = tradeId;
    this.orderId = orderId;
    this.feeAmount = feeAmount;
    this.feeCurrency = feeCurrency;
  }

  public BigDecimal getPrice() {

    return price;
  }

  public BigDecimal getAmount() {

    return amount;
  }

  public BigDecimal getTimestamp() {

    return timestamp;
  }

  public String getType() {

    return type;
  }

  public String getOrderId() {

    return orderId;
  }

  public String getTradeId() {

    return tradeId;
  }

  public BigDecimal getFeeAmount() {

    return feeAmount;
  }

  public String getFeeCurrency() {

    return feeCurrency;
  }

  @Override
  public String toString() {

    final StringBuilder builder = new StringBuilder();
    builder.append("BitfinexTradeResponse [price=");
    builder.append(price);
    builder.append(", amount=");
    builder.append(amount);
    builder.append(", timestamp=");
    builder.append(timestamp);
    builder.append(", exchange=");
    builder.append(exchange);
    builder.append(", type=");
    builder.append(type);
    builder.append("]");
    builder.append(", tradeId=");
    builder.append(tradeId);
    builder.append("]");
    builder.append(", orderId=");
    builder.append(orderId);
    builder.append(", fee=");
    builder.append(feeAmount);
    builder.append(" ");
    builder.append(feeCurrency);
    builder.append("]");
    return builder.toString();
  }
}
