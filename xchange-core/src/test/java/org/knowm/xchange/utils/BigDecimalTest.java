package org.knowm.xchange.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.Test;

/** Tests various BigDecimal and BigDecimal behavior
 * 测试各种 BigDecimal 和 BigDecimal 行为*/
public class BigDecimalTest {

  @Test
  public void test() {

    int BTC_VOLUME_AND_AMOUNT_INT_2_DECIMAL_FACTOR = 100000000;

    long amount_int = 23385868;

    // FYI DO NOT divide like this
    // 仅供参考，不要这样划分
    BigDecimal testAmount =
        new BigDecimal((double) amount_int / BTC_VOLUME_AND_AMOUNT_INT_2_DECIMAL_FACTOR);
    assertThat(testAmount.toPlainString())
        .isEqualTo("0.2338586800000000132104815975253586657345294952392578125");

    // FYI DO divide like this
    // 仅供参考，像这样划分
    BigDecimal testAmount2 =
        new BigDecimal(amount_int)
            .divide(new BigDecimal(BTC_VOLUME_AND_AMOUNT_INT_2_DECIMAL_FACTOR));
    assertThat(testAmount2.toPlainString()).isEqualTo("0.23385868");
  }
}
