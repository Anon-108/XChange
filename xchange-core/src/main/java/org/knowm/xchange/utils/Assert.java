package org.knowm.xchange.utils;

import java.util.Collection;

/**
 * Abstract class to provide the following to framework:
 * * 抽象类为框架提供以下内容：
 *
 * <ul>
 *   <li>Provision of useful assertions to trap programmer errors early
 *   <li>提供有用的断言以及早捕获程序员错误
 * </ul>
 */
public abstract class Assert {

  /**
   * Asserts that a condition is true
   * 断言条件为真
   *
   * @param condition The condition under test
   *                  测试条件
   *
   * @param message The message for any exception
   *                任何异常的消息
   */
  public static void isTrue(boolean condition, String message) {

    if (!condition) {
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * Asserts that an object is not null
   * 断言一个对象不为空
   *
   * @param object The object under test
   *               被测对象
   *
   * @param message The message for any exception
   *                任何异常的消息
   */
  public static void notNull(Object object, String message) {

    if (object == null) {
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * Asserts that a String is not null and of a certain length
   * * 断言一个字符串不为空并且有一定的长度
   *
   * @param input The input under test
   *              被测输入
   *
   * @param message The message for any exception
   *                任何异常的消息
   */
  public static void hasLength(String input, int length, String message) {

    notNull(input, message);
    if (input.trim().length() != length) {
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * Asserts that a Collection is not null and of a certain size
   * * 断言集合不为空且具有特定大小
   *
   * @param input The input under test
   *              被测输入
   *
   * @param message The message for any exception
   *                任何异常的消息
   */
  public static void hasSize(Collection<?> input, int length, String message) {

    notNull(input, message);
    if (input.size() != length) {
      throw new IllegalArgumentException(message);
    }
  }
}
