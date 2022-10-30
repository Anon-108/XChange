package org.knowm.xchange.binance.dto.trade;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 币安交易
 */
public final class BinanceTrade {
  /**
   * id
   */
  public final long id;
  /**
   * 交易id
   */
  public final long tradeId;
  /**
   * 订单id
   */
  public final long orderId;
  /**
   * 价格
   */
  public final BigDecimal price;
  /**
   * 数量
   */
  public final BigDecimal qty;
  /**
   * 佣金 /支付手续费
   */
  public final BigDecimal commission;
  /**
   * 佣金//支付手续费 资产
   */
  public final String commissionAsset;
  /**
   * 时间
   */
  public final long time;
  /**
   * 是买方
   */
  public final boolean isBuyer;
  /**
   * 是制造者 /制造商
   */
  public final boolean isMaker;
  /**
   * 是最佳匹配
   */
  public final boolean isBestMatch;

  /**
   * 币安订单
   * @param id id
   * @param tradeId 交易id
   * @param orderId 订单id
   * @param price 价格
   * @param qty 数量
   * @param commission 佣金//支付手续费
   * @param commissionAsset 佣金//支付手续费 资产
   * @param time 时间
   * @param isBuyer 是买方
   * @param isMaker 是制造者 /制造商
   * @param isBestMatch 是最佳匹配
   */
  public BinanceTrade(
      @JsonProperty("id") long id,
      @JsonProperty("tradeId") long tradeId,
      @JsonProperty("orderId") long orderId,
      @JsonProperty("price") BigDecimal price,
      @JsonProperty("qty") BigDecimal qty,
      @JsonProperty("commission") BigDecimal commission,
      @JsonProperty("commissionAsset") String commissionAsset,
      @JsonProperty("time") long time,
      @JsonProperty("isBuyer") boolean isBuyer,
      @JsonProperty("isMaker") boolean isMaker,
      @JsonProperty("isBestMatch") boolean isBestMatch) {
    this.id = id;
    this.tradeId = tradeId;
    this.orderId = orderId;
    this.price = price;
    this.qty = qty;
    this.commission = commission;
    this.commissionAsset = commissionAsset;
    this.time = time;
    this.isBuyer = isBuyer;
    this.isMaker = isMaker;
    this.isBestMatch = isBestMatch;
  }

  public Date getTime() {
    return new Date(time);
  }
}
