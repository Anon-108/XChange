package org.knowm.xchange.bitfinex.v1.dto.trade;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

/**
 * 订单状态响应
 */
public class BitfinexOrderStatusResponse {

  private final long id;
  private final String symbol;
  /**
   * 价格
   */
  private final BigDecimal price;
  /**
   * 平均执行价格
   */
  private final BigDecimal avgExecutionPrice;
  private final String side;
  private final String type;
  private final BigDecimal timestamp;
  /**
   * 生存，活着；生活在
   */
  private final boolean isLive;
  /**
   *  取消；作废；解约
   */
  private final boolean isCancelled;
  /**
   * 被迫?
   */
  private final boolean wasForced;
  /**
   * 原始数量
   */
  private final BigDecimal originalAmount;
  /**
   * 剩下的，遗留的；数量
   */
  private final BigDecimal remainingAmount;
  /**
   *  实施；完成
   */
  private final BigDecimal executedAmount;

  /**
   * Constructor
   *
   * @param id
   * @param symbol
   * @param price 价格
   * @param avgExecutionPrice 平均执行价格
   * @param side 边
   * @param type 类型
   * @param timestamp 时间戳
   * @param isLive 是存活
   * @param isCancelled 取消
   * @param wasForced 被迫
   * @param originalAmount 原始金额
   * @param remainingAmount 剩余数量
   * @param executedAmount 执行金额
   */
  public BitfinexOrderStatusResponse(
      @JsonProperty("order_id") long id,
      @JsonProperty("symbol") String symbol,
      @JsonProperty("price") BigDecimal price,
      @JsonProperty("avg_execution_price") BigDecimal avgExecutionPrice,
      @JsonProperty("side") String side,
      @JsonProperty("type") String type,
      @JsonProperty("timestamp") BigDecimal timestamp,
      @JsonProperty("is_live") boolean isLive,
      @JsonProperty("is_cancelled") boolean isCancelled,
      @JsonProperty("was_forced") boolean wasForced,
      @JsonProperty("original_amount") BigDecimal originalAmount,
      @JsonProperty("remaining_amount") BigDecimal remainingAmount,
      @JsonProperty("executed_amount") BigDecimal executedAmount) {

    this.id = id;
    this.symbol = symbol;
    this.price = price;
    this.avgExecutionPrice = avgExecutionPrice;
    this.side = side;
    this.type = type;
    this.timestamp = timestamp;
    this.isLive = isLive;
    this.isCancelled = isCancelled;
    this.wasForced = wasForced;
    this.originalAmount = originalAmount;
    this.remainingAmount = remainingAmount;
    this.executedAmount = executedAmount;
  }

  public BigDecimal getExecutedAmount() {

    return executedAmount;
  }

  public BigDecimal getRemainingAmount() {

    return remainingAmount;
  }

  public BigDecimal getOriginalAmount() {

    return originalAmount;
  }

  public boolean getWasForced() {

    return wasForced;
  }

  public String getType() {

    return type;
  }

  public String getSymbol() {

    return symbol;
  }

  public boolean isCancelled() {

    return isCancelled;
  }

  public BigDecimal getPrice() {

    return price;
  }

  public String getSide() {

    return side;
  }

  public BigDecimal getTimestamp() {

    return timestamp;
  }

  public long getId() {

    return id;
  }

  public boolean isLive() {

    return isLive;
  }

  public BigDecimal getAvgExecutionPrice() {

    return avgExecutionPrice;
  }

  @Override
  public String toString() {

    StringBuilder builder = new StringBuilder();
    builder.append("BitfinexOrderStatusResponse [id=");
    builder.append(id);
    builder.append(", symbol=");
    builder.append(symbol);
    builder.append(", price=");
    builder.append(price);
    builder.append(", avgExecutionPrice=");
    builder.append(avgExecutionPrice);
    builder.append(", side=");
    builder.append(side);
    builder.append(", type=");
    builder.append(type);
    builder.append(", timestamp=");
    builder.append(timestamp);
    builder.append(", isLive=");
    builder.append(isLive);
    builder.append(", isCancelled=");
    builder.append(isCancelled);
    builder.append(", wasForced=");
    builder.append(wasForced);
    builder.append(", originalAmount=");
    builder.append(originalAmount);
    builder.append(", remainingAmount=");
    builder.append(remainingAmount);
    builder.append(", executedAmount=");
    builder.append(executedAmount);
    builder.append("]");
    return builder.toString();
  }
}
