package org.knowm.xchange.bitfinex.v2.dto.marketdata;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * 交易订单
 * @see https://docs.bitfinex.com/reference#rest-public-book */
@JsonFormat(shape = JsonFormat.Shape.ARRAY)
@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class BitfinexTradingOrder {
  /** Price level
   * 价格水平*/
  BigDecimal price;
  /** Number of orders at that price level
   * 该价格水平的订单数量*/
  int count;
  /** Total amount available at that price level. if AMOUNT > 0 then bid else as
   * 在该价格水平下可用的总量。 如果 AMOUNT > 0 然后出价为 */
  BigDecimal amount;
}
