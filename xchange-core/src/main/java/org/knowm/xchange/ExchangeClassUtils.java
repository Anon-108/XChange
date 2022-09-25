package org.knowm.xchange;

import org.knowm.xchange.exceptions.ExchangeException;

public final class ExchangeClassUtils {

  private ExchangeClassUtils() {}

  public static Class<? extends Exchange> exchangeClassForName(String exchangeClassName) {
    // Attempt to create an instance of the exchange provider
    // 尝试创建交换提供者的实例
    try {
      Class<?> exchangeProviderClass = Class.forName(exchangeClassName);

      // Test that the class implements Exchange
      // 测试该类是否实现了 Exchange
      if (Exchange.class.isAssignableFrom(exchangeProviderClass)) {
        return (Class<? extends Exchange>) exchangeProviderClass;
      } else {
        throw new ExchangeException(
            "Class '" + exchangeClassName + "' does not implement Exchange 不实施 Exchange");
      }
    } catch (ClassNotFoundException e) {
      throw new ExchangeException("Problem creating Exchange (class not found) 创建 Exchange 时出现问题（找不到类）", e);
    }
  }
}
