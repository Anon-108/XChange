package org.knowm.xchange.utils;

import java.util.stream.Collector;
import java.util.stream.Collectors;

public final class StreamUtils {

  // Hide ctor
  // 隐藏 ctor
  private StreamUtils() {}

  public static <T> Collector<T, ?, T> singletonCollector() {
    return Collectors.collectingAndThen(
        Collectors.toList(),
        list -> {
          if (list.size() > 1) {
            throw new IllegalStateException("List contains more than one element 列表包含多个元素: " + list);
          }
          return list.size() > 0 ? list.get(0) : null;
        });
  }
}
