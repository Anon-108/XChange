package org.knowm.xchange.client;

import com.google.common.annotations.Beta;
import io.github.resilience4j.core.IntervalFunction;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.time.Duration;
import org.knowm.xchange.exceptions.ExchangeUnavailableException;
import org.knowm.xchange.exceptions.InternalServerException;
import org.knowm.xchange.exceptions.OperationTimeoutException;

/**
 * 恢复注册
 */
@Beta
public class ResilienceRegistries {
  /**
   * 重试配置
   */
  public static final RetryConfig DEFAULT_RETRY_CONFIG =
      RetryConfig.custom()
          .maxAttempts(3)
          .intervalFunction(IntervalFunction.ofExponentialBackoff(Duration.ofMillis(50), 4))
          .retryExceptions(
              IOException.class,
              ExchangeUnavailableException.class,
              InternalServerException.class,
              OperationTimeoutException.class)
          .build();

  public static final String NON_IDEMPOTENT_CALLS_RETRY_CONFIG_NAME = "nonIdempotentCallsBase";

  /**
   * Suggested for calls that are not idempotent like placing order or withdrawing funds
   * 建议用于下单或提款等非幂等调用
   *
    <p>Well designed exchange APIs will have mechanisms that make even placing orders idempotent.
    Most however cannot handle retries on this type of calls and if you do one after a socket read
    timeout for eq. then this may result in placing two identical orders instead of one. For such
    exchanged this retry configuration is recommended.

   <p>设计良好的交易所 API 将具有使下单等幂等的机制。
   但是，大多数都无法处理此类调用的重试，并且如果您在套接字读取后进行重试
   eq 超时。 那么这可能会导致下两个相同的订单而不是一个。 对于这样
   建议交换此重试配置。
   */
  public static final RetryConfig DEFAULT_NON_IDEMPOTENT_CALLS_RETRY_CONFIG =
      RetryConfig.from(DEFAULT_RETRY_CONFIG)
          .retryExceptions(
              UnknownHostException.class, SocketException.class, ExchangeUnavailableException.class)
          .build();
  /**
   * 速度限制器配置
   */
  public static final RateLimiterConfig DEFAULT_GLOBAL_RATE_LIMITER_CONFIG =
      RateLimiterConfig.custom()
          .timeoutDuration(Duration.ofSeconds(30))
          .limitRefreshPeriod(Duration.ofMinutes(1))
          .limitForPeriod(1200)
          .build();
  /**
   * 重试注册
   */
  private final RetryRegistry retryRegistry;
  /**
   * 速度限制器注册
   */
  private final RateLimiterRegistry rateLimiterRegistry;

  /**
   * 恢复注册表
   */
  public ResilienceRegistries() {
    this(DEFAULT_RETRY_CONFIG, DEFAULT_NON_IDEMPOTENT_CALLS_RETRY_CONFIG);
  }

  /**
   * 恢复注册表
   * @param globalRetryConfig 全局重试配置
   * @param nonIdempotentCallsRetryConfig 非幂等调用重试配置
   */
  public ResilienceRegistries(
      RetryConfig globalRetryConfig, RetryConfig nonIdempotentCallsRetryConfig) {
    this(globalRetryConfig, nonIdempotentCallsRetryConfig, DEFAULT_GLOBAL_RATE_LIMITER_CONFIG);
  }

  /**
   * 恢复注册表
   * @param globalRetryConfig 全局重试配置
   * @param nonIdempotentCallsRetryConfig 非幂等调用重试配置
   * @param globalRateLimiterConfig 全局速率限制器配置
   */
  public ResilienceRegistries(
      RetryConfig globalRetryConfig,
      RetryConfig nonIdempotentCallsRetryConfig,
      RateLimiterConfig globalRateLimiterConfig) {
    this(
        retryRegistryOf(globalRetryConfig, nonIdempotentCallsRetryConfig),
        RateLimiterRegistry.of(globalRateLimiterConfig));
  }

  /**
   * 恢复注册表
   * @param retryRegistry 重试注册表
   * @param rateLimiterRegistry 速率限制器注册表
   */
  public ResilienceRegistries(
      RetryRegistry retryRegistry, RateLimiterRegistry rateLimiterRegistry) {
    this.retryRegistry = retryRegistry;
    this.rateLimiterRegistry = rateLimiterRegistry;
  }

  /**
   * 重试
   * @return
   */
  public RetryRegistry retries() {
    return retryRegistry;
  }

  /**
   * 速率限制器
   * @return
   */
  public RateLimiterRegistry rateLimiters() {
    return rateLimiterRegistry;
  }

  /**
   * 重试注册表
   * @param globalRetryConfig 全局重试配置
   * @param nonIdempotentCallsRetryConfig 非幂等调用重试配置
   * @return
   */
  private static RetryRegistry retryRegistryOf(
      RetryConfig globalRetryConfig, RetryConfig nonIdempotentCallsRetryConfig) {
    RetryRegistry registry = RetryRegistry.of(globalRetryConfig);
    registry.addConfiguration(
        NON_IDEMPOTENT_CALLS_RETRY_CONFIG_NAME, nonIdempotentCallsRetryConfig);
    return registry;
  }
}
