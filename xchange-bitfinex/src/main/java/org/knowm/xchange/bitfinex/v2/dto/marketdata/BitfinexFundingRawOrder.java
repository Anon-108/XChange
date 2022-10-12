package org.knowm.xchange.bitfinex.v2.dto.marketdata;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * Bitfinex 资金原始订单
 *
 * @see https://docs.bitfinex.com/reference#rest-public-book funding order on raw books (precision
 *     of R0)
 */
@JsonFormat(shape = JsonFormat.Shape.ARRAY)
@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class BitfinexFundingRawOrder {
  /** Order ID 订单编号*/
  long orderId;
  /** Period level 期间级别*/
  int period;
  /** Rate level 费率水平*/
  BigDecimal rate;
  /** Total amount available at that price level. if AMOUNT > 0 then ask else bid.
   * 在该价格水平下可用的总量。 如果 AMOUNT > 0 则询问 else 出价。*/
  BigDecimal amount;
}
