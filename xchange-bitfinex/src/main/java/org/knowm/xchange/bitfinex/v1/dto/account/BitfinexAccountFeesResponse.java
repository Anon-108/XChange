package org.knowm.xchange.bitfinex.v1.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;
import org.knowm.xchange.bitfinex.service.BitfinexAdapters;
import org.knowm.xchange.currency.Currency;

/** @author ujjwal on 23/02/18. ujjwal 于 18 年 2 月 23 日。 */
public class BitfinexAccountFeesResponse {
  private final Map<Currency, BigDecimal> withdraw;

  public BitfinexAccountFeesResponse(
      @JsonProperty("withdraw") final Map<String, BigDecimal> withdraw) {
    this.withdraw =
        withdraw.entrySet().stream() // Sting needs to be adapted (i.e., DSH -> DASH)  // 需要调整 Sting（即 DSH -> DASH）
            .collect(
                Collectors.toMap(
                    entry -> new Currency(BitfinexAdapters.adaptBitfinexCurrency(entry.getKey())),
                    entry -> entry.getValue()));
  }

  /**
   * 获取提取
   * @return
   */
  public Map<Currency, BigDecimal> getWithdraw() {
    return withdraw;
  }
}
