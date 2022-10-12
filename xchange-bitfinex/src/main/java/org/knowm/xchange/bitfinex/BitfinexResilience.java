package org.knowm.xchange.bitfinex;

import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import java.time.Duration;
import org.knowm.xchange.client.ResilienceRegistries;

/**
 * 恢复 回弹 /回弹性 /抗逆力 /恢复力
 */
public final class BitfinexResilience {

  public static final String BITFINEX_RATE_LIMITER = "bitfinexRateLimiter";

  /**
   * 回弹 /回弹性 /抗逆力 /恢复力
   */
  private BitfinexResilience() {}

  /**
   * 创建注册
   * @return
   */
  public static ResilienceRegistries createRegistries() {
    ResilienceRegistries registries = new ResilienceRegistries();
    registries
        .rateLimiters()
        .rateLimiter(
            BITFINEX_RATE_LIMITER,
            RateLimiterConfig.from(registries.rateLimiters().getDefaultConfig())
                .limitRefreshPeriod(Duration.ofMinutes(1))
                .limitForPeriod(90)
                .build());
    return registries;
  }
}
