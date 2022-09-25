package org.knowm.xchange.utils.nonce;

import java.util.concurrent.atomic.AtomicLong;
import si.mazi.rescu.SynchronizedValueFactory;

public class AtomicLongIncrementalTime2013NonceFactory implements SynchronizedValueFactory<Long> {

  private static final long START_MILLIS =
      1356998400000L; // Jan 1st, 2013 in milliseconds from epoch  2013 年 1 月 1 日，从纪元开始的毫秒数

  // counter for the nonce 随机数计数器
  private final AtomicLong lastNonce =
      new AtomicLong((System.currentTimeMillis() - START_MILLIS) / 250L);

  @Override
  public Long createValue() {

    return lastNonce.incrementAndGet();
  }
}
