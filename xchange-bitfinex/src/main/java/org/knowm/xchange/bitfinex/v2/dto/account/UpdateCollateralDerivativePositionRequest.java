package org.knowm.xchange.bitfinex.v2.dto.account;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
/**
 * 更新抵押衍生品头寸请求
 * see https://docs.bitfinex.com/reference#rest-auth-deriv-pos-collateral-set */
@JsonInclude(Include.NON_NULL)
public class UpdateCollateralDerivativePositionRequest {
  /** The derivative symbol, e.g. tBTCF0:USTF0
   * 衍生符号，例如 tBTCF0:USTF0*/
  @NonNull private String symbol;

  /** The amount of collateral to apply to the open position
   * 适用于未平仓头寸的抵押品数量*/
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  @NonNull
  private BigDecimal collateral;
}
