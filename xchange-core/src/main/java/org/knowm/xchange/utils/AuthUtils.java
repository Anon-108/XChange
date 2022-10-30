package org.knowm.xchange.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.knowm.xchange.ExchangeSpecification;

public class AuthUtils {

  /**
   * Generates a BASE64 Basic Authentication String
   * 生成 BASE64 基本身份验证字符串
   *
   * @return BASE64 Basic Authentication String
   * * @return BASE64 基本认证字符串
   */
  public static String getBasicAuth(String user, final String pass) {

    return "Basic " + java.util.Base64.getEncoder().encodeToString((user + ":" + pass).getBytes());
  }

  /**
   * Read the API & Secret key from a resource called {@code secret.keys}. NOTE: This file MUST
    NEVER be commited to source control. It is therefore added to .gitignore.
   从名为 {@code secret.keys} 的资源中读取 API 和密钥。 注意：这个文件必须
   永远不要致力于源代码控制。 因此它被添加到 .gitignore。
   */
  public static void setApiAndSecretKey(ExchangeSpecification exchangeSpec) {

    setApiAndSecretKey(exchangeSpec, null);
  }

  /**
   * 设置 Api 和 SecretKey
   * Read the API & Secret key from a resource called {@code prefix}-{@code secret.keys}. NOTE: This
    file MUST NEVER be commited to source control. It is therefore added to .gitignore.
   从名为 {@code prefix}-{@code secret.keys} 的资源中读取 API 和密钥。 注意：这
   文件绝不能提交到源代码管理。 因此它被添加到 .gitignore。
   */
  public static void setApiAndSecretKey(ExchangeSpecification exchangeSpec, String prefix) {

    Properties props = getSecretProperties(prefix);

    if (props != null) {
      exchangeSpec.setApiKey(props.getProperty("apiKey"));
      exchangeSpec.setSecretKey(props.getProperty("secretKey"));
    }
  }

  /**
   * Read the secret properties from a resource called {@code prefix}-{@code secret.keys}. NOTE:
    This file MUST NEVER be commited to source control. It is therefore added to .gitignore.
   从名为 {@code prefix}-{@code secret.keys} 的资源中读取机密属性。 笔记：
   此文件绝不能提交到源代码管理。 因此它被添加到 .gitignore。
   *
   * @return The properties or null
   * @return 属性或 null
   */
  public static Properties getSecretProperties(String prefix) {

    String resource = prefix != null ? prefix + "-secret.keys" : "secret.keys";

    // First try to find the keys in the classpath
    // 首先尝试在类路径中查找键
    InputStream inStream = AuthUtils.class.getResourceAsStream("/" + resource);

    // Next try to find the keys in the user's home/.ssh dir
    // 接下来尝试在用户的 home/.ssh 目录中查找密钥
    File keyfile = new File(System.getProperty("user.home") + "/" + ".ssh", resource);
    if (inStream == null && keyfile.isFile()) {
      try {
        inStream = new FileInputStream(keyfile);
      } catch (IOException e) {
        // do nothing
        // 没做什么
      }
    }

    Properties props = null;
    if (inStream != null) {
      try {
        props = new Properties();
        props.load(inStream);
        return props;
      } catch (IOException e) {
        // do nothing
        // 没做什么
      }
    }
    return props;
  }
}
