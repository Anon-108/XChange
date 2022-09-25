package org.knowm.xchange.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.meta.CurrencyPairMetaData;

/**
 * Helps you to validate and / or adjust order values like price and amount to the restrictions
  dictated by {@link CurrencyPairMetaData}
 帮助您验证和/或调整订单价值，如价格和金额限制
 由 {@link CurrencyPairMetaData} 规定
 *
 * @author walec51
 */
public class OrderValuesHelper {

  private final CurrencyPairMetaData metaData;

  public OrderValuesHelper(CurrencyPairMetaData metaData) {
    this.metaData = metaData;
  }

  /**
   * @return true if the minimum amount is specified in the currency pair and if the amount is under  it
   * @return 如果在货币对中指定了最小金额并且金额低于该金额，则返回 true
   */
  public boolean amountUnderMinimum(BigDecimal amount) {
    BigDecimal minimalAmount = metaData.getMinimumAmount();
    if (minimalAmount == null) {
      return false;
    }
    return amount.compareTo(minimalAmount) < 0;
  }

  /**
   * Adjusts the given amount to the restrictions dictated by {@link CurrencyPairMetaData}.
   * * 将给定金额调整为 {@link CurrencyPairMetaData} 规定的限制。
   *
   * <p>This mainly does rounding based on {@link CurrencyPairMetaData#getBaseScale()} and {@link  CurrencyPairMetaData#getAmountStepSize()} if they are present in the metadata. It will also
    return the maximum allowed amount if {@link CurrencyPairMetaData#getMaximumAmount() ()} is set and your amount is greater.
   <p>这主要基于 {@link CurrencyPairMetaData#getBaseScale()} 和 {@link CurrencyPairMetaData#getAmountStepSize()} 进行舍入（如果它们存在于元数据中）。 它还将
   如果设置了 {@link CurrencyPairMetaData#getMaximumAmount() ()} 并且您的金额更大，则返回允许的最大金额。
   *
   * @param amount the amount your derived from your users input or your calculations
   *               * @param amount 您从用户输入或计算中得出的金额
   *
   * @return amount adjusted to the restrictions dictated by {@link CurrencyPairMetaData}
   * * @return 金额调整为 {@link CurrencyPairMetaData} 规定的限制
   */
  public BigDecimal adjustAmount(BigDecimal amount) {
    BigDecimal maximumAmount = metaData.getMaximumAmount();
    if (maximumAmount != null && amount.compareTo(maximumAmount) > 0) {
      return maximumAmount;
    }
    BigDecimal result = amount;
    BigDecimal stepSize = metaData.getAmountStepSize();
    if (stepSize != null && stepSize.compareTo(BigDecimal.ZERO) != 0) {
      result = BigDecimalUtils.roundToStepSize(result, stepSize, RoundingMode.FLOOR);
    }
    Integer baseScale = metaData.getBaseScale();
    if (baseScale != null) {
      result = result.setScale(baseScale, RoundingMode.FLOOR);
    }
    return result;
  }

  /**
   * Adjusts the given price to the restrictions dictated by {@link CurrencyPairMetaData}.
   * * 将给定价格调整为 {@link CurrencyPairMetaData} 规定的限制。
   *
   * <p>Convenience method that chooses the adequate rounding mode for you order type. See {@link #adjustPrice(java.math.BigDecimal, java.math.RoundingMode)} for more information.
   * * <p>为您的订单类型选择适当的舍入模式的便捷方法。 请参阅 {@link #adjustPrice(java.math.BigDecimal, java.math.RoundingMode)} 了解更多信息。
   *
   * @see #adjustPrice(java.math.BigDecimal, java.math.RoundingMode)
   */
  public BigDecimal adjustPrice(BigDecimal price, Order.OrderType orderType) {
    return adjustPrice(
        price,
        orderType == Order.OrderType.ASK || orderType == Order.OrderType.EXIT_ASK
            ? RoundingMode.CEILING
            : RoundingMode.FLOOR);
  }

  /**
   * Adjusts the given price to the restrictions dictated by {@link CurrencyPairMetaData}.
   * 将给定价格调整为 {@link CurrencyPairMetaData} 规定的限制。
   *
   * <p>This mainly does rounding based on {@link CurrencyPairMetaData#getPriceScale()} if it is present in the metadata.
   * * <p>如果它存在于元数据中，这主要基于 {@link CurrencyPairMetaData#getPriceScale()} 进行舍入。
   *
   * @param price the price your derived from your users input or your calculations
   *              * @param price 您从用户输入或计算得出的价格
   *
   * @return price adjusted to the restrictions dictated by {@link CurrencyPairMetaData}
   * * @return 价格调整为 {@link CurrencyPairMetaData} 规定的限制
   */
  public BigDecimal adjustPrice(BigDecimal price, RoundingMode roundingMode) {
    BigDecimal result = price;
    Integer scale = metaData.getPriceScale();
    if (scale != null) {
      result = result.setScale(scale, roundingMode);
    }
    return result;
  }
}
