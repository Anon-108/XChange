package org.knowm.xchange.utils.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.util.Date;
import org.knowm.xchange.utils.DateUtils;

/**
 * Deserializes an ISO 8601 formatted Date String to a Java Date ISO 8601 format:
  yyyy-MM-dd'T'HH:mm:ssX
 将 ISO 8601 格式的日期字符串反序列化为 Java 日期 ISO 8601 格式：
 yyyy-MM-dd'T'HH:mm:ssX
 *
 * @author jamespedwards42
 */
public class ISO8601DateDeserializer extends JsonDeserializer<Date> {

  @Override
  public Date deserialize(JsonParser jp, final DeserializationContext ctxt) throws IOException {

    return DateUtils.fromISO8601DateString(jp.getValueAsString());
  }
}
