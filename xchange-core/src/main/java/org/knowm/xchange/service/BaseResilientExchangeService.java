package org.knowm.xchange.service;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.retry.Retry;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.client.ResilienceUtils;

/**
 * Abstract class for an "exchange service" which supports resiliency features like retries, rate limiting etc.
 * * “交换服务”的抽象类，它支持重试、速率限制等弹性特性。
 */
public abstract class BaseResilientExchangeService<E extends Exchange>
    extends BaseExchangeService<E> {

  protected final ResilienceRegistries resilienceRegistries;

  protected BaseResilientExchangeService(E exchange, ResilienceRegistries resilienceRegistries) {
    super(exchange);
    this.resilienceRegistries = resilienceRegistries;
  }

  /**
   * Use this method to decorate API calls with resiliency features like retries, rate limiters, etc.
   * * 使用此方法来装饰具有弹性功能的 API 调用，如重试、速率限制器等。
   *
   * @param callable call to exchange API
   *                 调用交换 API
   *
   * @param <R> type returned by the API call
   *           API 调用返回的类型
   *
   * @return builder of a decorated API call
   * * @return 修饰 API 调用的构建器
   */
  public <R> ResilienceUtils.DecorateCallableApi<R> decorateApiCall(
      ResilienceUtils.CallableApi<R> callable) {
    return ResilienceUtils.decorateApiCall(
        exchange.getExchangeSpecification().getResilience(), callable);
  }

  /**
   * Returns a managed {@link Retry} or creates a new one with the default Retry configuration from {@link ResilienceRegistries#DEFAULT_RETRY_CONFIG}.
   * * 返回一个托管的 {@link Retry} 或使用来自 {@link ResilienceRegistries#DEFAULT_RETRY_CONFIG} 的默认重试配置创建一个新的。
   *
   * @param name the name of the Retry
   *             重试的名称
   *
   * @return The {@link Retry} {@link 重试}
   * @see io.github.resilience4j.retry.RetryRegistry#retry(String)
   */
  protected Retry retry(String name) {
    return resilienceRegistries.retries().retry(name);
  }

  /**
   * Returns a managed {@link Retry} or creates a new one. The configuration must have been added
    upfront in {@link #resilienceRegistries} via {@link ResilienceRegistries#retries()} and the
    {@link io.github.resilience4j.retry.RetryRegistry#addConfiguration(String, Object)} method. You
    can also used a predefined retry like {@link ResilienceRegistries#NON_IDEMPOTENT_CALLS_RETRY_CONFIG_NAME}.
   返回一个托管的 {@link Retry} 或创建一个新的。 必须已添加配置
   通过 {@link ResilienceRegistries#retries()} 预先在 {@link #resilienceRegistries} 和
   {@link io.github.resilience4j.retry.RetryRegistry#addConfiguration(String, Object)} 方法。 你
   也可以使用预定义的重试，例如 {@link ResilienceRegistries#NON_IDEMPOTENT_CALLS_RETRY_CONFIG_NAME}。
   *
   * @param name the name of the Retry
   *             重试的名称
   *
   * @param configName the name of the shared configuration
   *                   共享配置的名称
   *
   * @return The {@link Retry}
   * @see io.github.resilience4j.retry.RetryRegistry#retry(String, String)
   */
  protected Retry retry(String name, String configName) {
    return resilienceRegistries.retries().retry(name, configName);
  }

  /**
   * Returns a managed {@link RateLimiter} or creates a new one with the default RateLimiter
    configuration. One main shared rate limiter should be defined for each exchange module via
    {@link ResilienceRegistries#rateLimiters()} ()} and the {@link  io.github.resilience4j.ratelimiter.RateLimiterRegistry#addConfiguration(String, Object)} method.
   返回一个托管的 {@link RateLimiter} 或使用默认的 RateLimiter 创建一个新的
   配置。 应该为每个交换模块定义一个主要的共享速率限制器
   {@link ResilienceRegistries#rateLimiters()} ()} 和 {@link io.github.resilience4j.ratelimiter.RateLimiterRegistry#addConfiguration(String, Object)} 方法。

   *
   * @param name the name of the RateLimiter
   *             RateLimiter 的名称
   *
   * @return The {@link RateLimiter}
   * @see io.github.resilience4j.ratelimiter.RateLimiterRegistry#rateLimiter(String)
   */
  protected RateLimiter rateLimiter(String name) {
    return resilienceRegistries.rateLimiters().rateLimiter(name);
  }

  /**
   * Returns a managed {@link RateLimiter} or creates a new one. The configuration must have been
    added upfront {@link #resilienceRegistries} via {@link ResilienceRegistries#rateLimiters()} ()}
    and the {@link io.github.resilience4j.ratelimiter.RateLimiterRegistry#addConfiguration(String, Object)} method.
   返回一个托管的 {@link RateLimiter} 或创建一个新的。 配置应该是
   通过 {@link ResilienceRegistries#rateLimiters()} ()} 预先添加 {@link #resilienceRegistries}
   和 {@link io.github.resilience4j.ratelimiter.RateLimiterRegistry#addConfiguration(String, Object)} 方法。

   *
   * @param name the name of the RateLimiter
   *             RateLimiter 的名称
   *
   * @param configName the name of the shared configuration
   *                   共享配置的名称
   *
   * @return The {@link RateLimiter}
   * @see io.github.resilience4j.ratelimiter.RateLimiterRegistry#rateLimiter(String, String)
   */
  protected RateLimiter rateLimiter(String name, String configName) {
    return resilienceRegistries.rateLimiters().rateLimiter(name, configName);
  }
}
