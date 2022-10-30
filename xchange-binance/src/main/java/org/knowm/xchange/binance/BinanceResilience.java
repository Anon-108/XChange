package org.knowm.xchange.binance;

import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import java.time.Duration;
import org.knowm.xchange.client.ResilienceRegistries;

/**
 * 币安恢复
 */
public final class BinanceResilience {
  /**
   * 请求重量速度限制器
   */
  public static final String REQUEST_WEIGHT_RATE_LIMITER = "requestWeight";

  /**
   * 每秒订单数限制器
   */
  public static final String ORDERS_PER_SECOND_RATE_LIMITER = "ordersPerSecond";

  /**
   * 每日订单率限制器
   */
  public static final String ORDERS_PER_DAY_RATE_LIMITER = "ordersPerDay";

  /**
   * 币安恢复
   */
  private BinanceResilience() {}

  /**
   * 创建注册
   * @return
   */
  public static ResilienceRegistries createRegistries() {
    ResilienceRegistries registries = new ResilienceRegistries();
    registries
        .rateLimiters()
        .rateLimiter(
            REQUEST_WEIGHT_RATE_LIMITER,
            RateLimiterConfig.from(registries.rateLimiters().getDefaultConfig())
                .timeoutDuration(Duration.ofMinutes(1))
                .limitRefreshPeriod(Duration.ofMinutes(1))
                .limitForPeriod(1200)
                .build());
    registries
        .rateLimiters()
        .rateLimiter(
            ORDERS_PER_SECOND_RATE_LIMITER,
            RateLimiterConfig.from(registries.rateLimiters().getDefaultConfig())
                .limitRefreshPeriod(Duration.ofSeconds(1))
                .limitForPeriod(10)
                .build());
    registries
        .rateLimiters()
        .rateLimiter(
            ORDERS_PER_DAY_RATE_LIMITER,
            RateLimiterConfig.from(registries.rateLimiters().getDefaultConfig())
                .timeoutDuration(Duration.ZERO)
                .limitRefreshPeriod(Duration.ofDays(1))
                .limitForPeriod(200000)
                .build());
    return registries;
  }
}
