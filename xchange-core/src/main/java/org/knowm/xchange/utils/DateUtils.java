package org.knowm.xchange.utils;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.TimeZone;

/**
 * Utilities to provide the following to application:
 * * 为应用程序提供以下内容的实用程序：
 *
 * <ul>
 *   <li>Provision of standard date and time handling
 *   * <li>提供标准日期和时间处理
 * </ul>
 */
public class DateUtils {

  /** private Constructor
   * 私有构造函数*/
  private DateUtils() {}

  /**
   * Creates a date from a long representing milliseconds from epoch
   * * 从一个长的表示从纪元开始的毫秒数创建一个日期
   * @param millisecondsFromEpoch
   * @return the Date object
   * * @return 日期对象
   */
  public static Date fromMillisUtc(long millisecondsFromEpoch) {

    return new Date(millisecondsFromEpoch);
  }

  /**
   * Converts a date to a UTC String representation
   * * 将日期转换为 UTC 字符串表示
   *
   * @param date
   * @return the formatted date
   * * @return 格式化日期
   */
  public static String toUTCString(Date date) {

    SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
    sd.setTimeZone(TimeZone.getTimeZone("GMT"));
    return sd.format(date);
  }

  public static String toUTCISODateString(Date date) {
    SimpleDateFormat isoDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    isoDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    return isoDateFormat.format(date);
  }

  public static String toISODateString(Date date) {
    SimpleDateFormat isoDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    return isoDateFormat.format(date);
  }

  /**
   * Converts an ISO formatted Date String to a Java Date ISO format: yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
   * * 将 ISO 格式的日期字符串转换为 Java 日期 ISO 格式：yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
   *
   * @param isoFormattedDate
   * @return Date
   * @throws com.fasterxml.jackson.databind.exc.InvalidFormatException
   */
  public static Date fromISODateString(String isoFormattedDate)
      throws com.fasterxml.jackson.databind.exc.InvalidFormatException {

    SimpleDateFormat isoDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    // set UTC time zone - 'Z' indicates it
    // 设置 UTC 时区 - 'Z' 表示它
    isoDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    try {
      return isoDateFormat.parse(isoFormattedDate);
    } catch (ParseException e) {
      throw new InvalidFormatException(null, "Error parsing as date 解析为日期时出错", isoFormattedDate, Date.class);
    }
  }

  /**
   * Converts an ISO 8601 formatted Date String to a Java Date ISO 8601 format:
    yyyy-MM-dd'T'HH:mm:ss
   将 ISO 8601 格式的日期字符串转换为 Java 日期 ISO 8601 格式：
   yyyy-MM-dd'T'HH:mm:ss
   *
   * @param iso8601FormattedDate
   * @return Date
   * @throws com.fasterxml.jackson.databind.exc.InvalidFormatException
   */
  public static Date fromISO8601DateString(String iso8601FormattedDate)
      throws com.fasterxml.jackson.databind.exc.InvalidFormatException {

    SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    // set UTC time zone
    //设置UTC时区
    iso8601Format.setTimeZone(TimeZone.getTimeZone("UTC"));
    try {
      return iso8601Format.parse(iso8601FormattedDate);
    } catch (ParseException e) {
      throw new InvalidFormatException(
          null, "Error parsing as date 解析为日期时出错", iso8601FormattedDate, Date.class);
    }
  }

  /**
   * Converts an rfc1123 formatted Date String to a Java Date rfc1123 format: EEE, dd MMM yyyy
    HH:mm:ss zzz
   将 rfc1123 格式的日期字符串转换为 Java 日期 rfc1123 格式：EEE, dd MMM yyyy
   HH:mm:ss zzz
   *
   * @param rfc1123FormattedDate
   * @return Date
   * @throws com.fasterxml.jackson.databind.exc.InvalidFormatException
   */
  public static Date fromRfc1123DateString(String rfc1123FormattedDate, Locale locale)
      throws com.fasterxml.jackson.databind.exc.InvalidFormatException {

    SimpleDateFormat rfc1123DateFormat =
        new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", locale);
    try {
      return rfc1123DateFormat.parse(rfc1123FormattedDate);
    } catch (ParseException e) {
      throw new InvalidFormatException(
          null, "Error parsing as date 解析为日期时出错", rfc1123FormattedDate, Date.class);
    }
  }

  /**
   * Converts an RFC3339 formatted Date String to a Java Date RFC3339 format: yyyy-MM-dd HH:mm:ss
   * * 将 RFC3339 格式的日期字符串转换为 Java 日期 RFC3339 格式：yyyy-MM-dd HH:mm:ss
   *
   * @param rfc3339FormattedDate RFC3339 formatted Date  RFC3339 格式日期
   * @return an {@link Date} object  {@link Date} 对象
   * @throws InvalidFormatException the RFC3339 formatted Date is invalid or cannot be parsed.
   * * @throws InvalidFormatException RFC3339 格式的日期无效或无法解析。
   *
   * @see <a href="https://tools.ietf.org/html/rfc3339">The Internet Society - RFC 3339</a>
   */
  public static Date fromRfc3339DateString(String rfc3339FormattedDate)
      throws InvalidFormatException {

    SimpleDateFormat rfc3339DateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    try {
      return rfc3339DateFormat.parse(rfc3339FormattedDate);
    } catch (ParseException e) {
      throw new InvalidFormatException(
          null, "Error parsing as date 解析为日期时出错", rfc3339FormattedDate, Date.class);
    }
  }

  /** Convert java time long to unix time long, simply by dividing by 1000
   * 将 java time long 转换为 unix time long，只需除以 1000 */
  public static long toUnixTime(long javaTime) {
    return javaTime / 1000;
  }

  /** Convert java time to unix time long, simply by dividing by the time 1000
   * 将java时间转换为unix时间长，只需除以时间1000*/
  public static long toUnixTime(Date time) {
    return time.getTime() / 1000;
  }

  /** Convert java time to unix time long, simply by dividing by the time 1000. Null safe
   * 将 java 时间转换为 unix 时间长，只需除以时间 1000。空安全*/
  public static Long toUnixTimeNullSafe(Date time) {

    return time == null ? null : time.getTime() / 1000;
  }

  public static Optional<Long> toUnixTimeOptional(Date time) {

    return Optional.ofNullable(time).map(it -> it.getTime() / 1000);
  }

  public static Long toMillisNullSafe(Date time) {

    return time == null ? null : time.getTime();
  }

  /** Convert unix time to Java Date
   * 将 unix 时间转换为 Java 日期*/
  public static Date fromUnixTime(long unix) {
    return new Date(unix * 1000);
  }
}
