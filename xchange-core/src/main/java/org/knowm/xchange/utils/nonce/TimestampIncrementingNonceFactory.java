package org.knowm.xchange.utils.nonce;

import si.mazi.rescu.SynchronizedValueFactory;

public class TimestampIncrementingNonceFactory implements SynchronizedValueFactory<Long> {

  private static final long START_MILLIS =
      1356998400000L; // Jan 1st, 2013 in milliseconds from epoch  2013 年 1 月 1 日，从纪元开始的毫秒数

  private int lastNonce = 0;

  @Override
  public Long createValue() {

    lastNonce = Math.max(lastNonce + 1, (int) ((System.currentTimeMillis() - START_MILLIS) / 250L));
    return (long) lastNonce;
  }
}
