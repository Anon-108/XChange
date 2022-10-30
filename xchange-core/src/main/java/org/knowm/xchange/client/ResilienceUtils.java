package org.knowm.xchange.client;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.retry.Retry;
import io.vavr.control.Either;
import java.io.IOException;
import java.util.concurrent.Callable;
import javax.ws.rs.core.Response;
import org.knowm.xchange.ExchangeSpecification;
import si.mazi.rescu.HttpStatusExceptionSupport;

/**
 * 恢复工具类
 */
public final class ResilienceUtils {
  /**
   * 恢复工具类
   */
  private ResilienceUtils() {}

  /**
   * 装修Api调用
   * @param resilienceSpecification 回弹/恢复规格
   * @param callable
   * @param <T>
   * @return
   */
  public static <T> DecorateCallableApi<T> decorateApiCall(
      ExchangeSpecification.ResilienceSpecification resilienceSpecification,
      CallableApi<T> callable) {
    return new DecorateCallableApi<>(resilienceSpecification, callable);
  }

  /** Function which can be used check if a particular HTTP status code was returned
   * 可用于检查是否返回特定 HTTP 状态代码的函数*/
  public static boolean matchesHttpCode(
      final Either<? extends Throwable, ?> e, final Response.Status status) {
    if (e.isRight()) {
      return false;
    }
    final Throwable throwable = e.getLeft();
    return throwable instanceof HttpStatusExceptionSupport
        && ((HttpStatusExceptionSupport) throwable).getHttpStatusCode() == status.getStatusCode();
  }

  /**
   * 可调用 API
   * @param <T>
   */
  public interface CallableApi<T> extends Callable<T> {

    T call() throws IOException;

    /**
     * 包装可调用
     * @param callable 调用
     * @param <T>
     * @return
     */
    static <T> CallableApi<T> wrapCallable(Callable<T> callable) {
      return () -> {
        try {
          return callable.call();
        } catch (IOException | RuntimeException e) {
          throw e;
        } catch (Throwable e) {
          throw new IllegalStateException(e);
        }
      };
    }
  }

  /**
   * 装修可调用的Api
   * @param <T>
   */
  public static class DecorateCallableApi<T> {
    private final ExchangeSpecification.ResilienceSpecification resilienceSpecification;
    private CallableApi<T> callable;

    /**
     * 装修可调用的Api
     * @param resilienceSpecification 弹性/恢复规范
     * @param callable 可调用
     */
    private DecorateCallableApi(
        ExchangeSpecification.ResilienceSpecification resilienceSpecification,
        CallableApi<T> callable) {
      this.resilienceSpecification = resilienceSpecification;
      this.callable = callable;
    }

    /**
     * 重试
     * @param retryContext 重试上下文
     * @return
     */
    public DecorateCallableApi<T> withRetry(Retry retryContext) {
      if (resilienceSpecification.isRetryEnabled()) {
        this.callable =
            CallableApi.wrapCallable(Retry.decorateCallable(retryContext, this.callable));
      }
      return this;
    }

    /**
     * 速率限制器
     * @param rateLimiter 速率限制器
     * @return
     */
    public DecorateCallableApi<T> withRateLimiter(RateLimiter rateLimiter) {
      if (resilienceSpecification.isRateLimiterEnabled()) {
        return this.withRateLimiter(rateLimiter, 1);
      }
      return this;
    }

    /**
     * 速率限制器
     * @param rateLimiter 速率限制器
     * @param permits 许可证
     * @return
     */
    public DecorateCallableApi<T> withRateLimiter(RateLimiter rateLimiter, int permits) {
      if (resilienceSpecification.isRateLimiterEnabled()) {
        this.callable =
            CallableApi.wrapCallable(
                RateLimiter.decorateCallable(rateLimiter, permits, this.callable));
      }
      return this;
    }

    public T call() throws IOException {
      return this.callable.call();
    }
  }
}
