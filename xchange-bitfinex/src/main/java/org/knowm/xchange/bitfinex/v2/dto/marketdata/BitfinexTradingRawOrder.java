package org.knowm.xchange.bitfinex.v2.dto.marketdata;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 *
 * Bitfinex 交易原始订单
 * @see https://docs.bitfinex.com/reference#rest-public-book trading order on raw books (precision
 *     of R0)
 */
@JsonFormat(shape = JsonFormat.Shape.ARRAY)
@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class BitfinexTradingRawOrder {
  /** Order ID
   * 订单编号*/
  long orderId;
  /** Price level
   * 价格水平*/
  BigDecimal price;
  /** Total amount available at that price level. if AMOUNT > 0 then bid else ask.
   * 在该价格水平下可用的总量。 如果 AMOUNT > 0 然后出价，否则询问。*/
  BigDecimal amount;
}
