package org.knowm.xchange.utils.nonce;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;
import si.mazi.rescu.SynchronizedValueFactory;

/**
 * Class computes a current time based nonce.
 * 类计算基于当前时间的随机数。
 *
 * <p>This class while designed to be thread-safe does not protect against multiple processes where
  the system clock may be out of sync. It also does not protect a system where a lower granularity
  time unit like {@link TimeUnit#SECONDS} is used and the same nonce is computed at the same time from competing processes.
 <p>这个类虽然被设计为线程安全的，但不能防止多个进程
 系统时钟可能不同步。 它也不能保护粒度较低的系统
 使用像 {@link TimeUnit#SECONDS} 这样的时间单位，并且从竞争进程中同时计算相同的随机数。
 *
 * <p>Compatibility is limited to the time units specified.
 * <p>兼容性仅限于指定的时间单位。
 */
public class CurrentTimeIncrementalNonceFactory implements SynchronizedValueFactory<Long> {

  private final AtomicLong nonce = new AtomicLong(0);

  private final Supplier<Long> timeFn;

  public CurrentTimeIncrementalNonceFactory(final TimeUnit timeUnit) {
    switch (timeUnit) {
      case SECONDS:
        timeFn = () -> System.currentTimeMillis() / 1000;
        break;
      case MILLISECONDS:
        timeFn = System::currentTimeMillis;
        break;
      case MICROSECONDS:
        timeFn = () -> System.nanoTime() / 1000;
        break;
      case NANOSECONDS:
        timeFn = System::nanoTime;
        break;
      default:
        throw new IllegalArgumentException(String.format("TimeUnit 时间单位 %s not supported 不支持", timeUnit));
    }
  }

  @Override
  public Long createValue() {
    return nonce.updateAndGet(
        prevNonce -> {
          long newNonce = timeFn.get();

          if (newNonce <= prevNonce) {
            newNonce = prevNonce + 1;
          }
          return newNonce;
        });
  }
}
