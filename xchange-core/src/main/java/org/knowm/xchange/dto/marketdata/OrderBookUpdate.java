package org.knowm.xchange.dto.marketdata;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.knowm.xchange.dto.Order.OrderType;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.instrument.Instrument;

/** Immutable data object representing a Market Depth update.
 * 表示市场深度更新的不可变数据对象。*/
public final class OrderBookUpdate implements Serializable {

  private static final long serialVersionUID = -7283757982319511254L;

  private final LimitOrder limitOrder;

  /** this is the total volume at this price in the order book
   * 这是订单簿中此价格的总交易量*/
  private final BigDecimal totalVolume;

  /**
   * Build an order book update.
   * 构建订单簿更新。
   *
   * @param type the order type (BID/ASK)
   *             订单类型 (BID/ASK)
   *
   * @param volume volume of the limit order in the base currency (i.e. BTC for BTC/USD)
   *               基础货币限价单的交易量（即 BTC 对 BTC/USD）
   *
   * @param instrument the instrument traded (e.g. BTC/USD)
   *                   交易的工具（例如 BTC/USD）
   *
   * @param limitPrice the price of this update in counter currency per base currency (i.e. $/BTC in   BTC/USD)
   *                   本次更新的价格，以每个基础货币对应的货币（即 BTC/USD 中的 $/BTC）
   *
   * @param timestamp the timestamp for the update
   *                  更新的时间戳
   *
   * @param totalVolume the total new volume of open orders for this price in the order book, in the  base currency
   *                    订单簿中该价格的未平仓订单总量，以基础货币计算
   */
  // TODO clarify what should be provided for volume parameter
  //TODO 澄清应该为交易量参数提供什么
  public OrderBookUpdate(
      OrderType type,
      BigDecimal volume,
      Instrument instrument,
      BigDecimal limitPrice,
      Date timestamp,
      BigDecimal totalVolume) {

    this.limitOrder = new LimitOrder(type, volume, instrument, "", timestamp, limitPrice);
    this.totalVolume = totalVolume;
  }

  /**
   * Get the limit order.
   * 获取限价单。
   *
   * @return the limit order
   *        限价单
   */
  public LimitOrder getLimitOrder() {

    return limitOrder;
  }

  /**
   * Get the total volume.
   * 获取总音量。
   *
   * @return the total volume
   *        总体积
   */
  public BigDecimal getTotalVolume() {

    return totalVolume;
  }

  @Override
  public String toString() {

    return "OrderBookUpdate [limitOrder=" + limitOrder + ", totalVolume=" + totalVolume + "]";
  }
}
