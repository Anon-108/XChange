package org.knowm.xchange.service;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import si.mazi.rescu.ParamsDigest;

/**
 * 基本参数摘要
 */
public abstract class BaseParamsDigest implements ParamsDigest {

  public static final String HMAC_SHA_512 = "HmacSHA512";
  public static final String HMAC_SHA_384 = "HmacSHA384";
  public static final String HMAC_SHA_256 = "HmacSHA256";
  public static final String HMAC_SHA_1 = "HmacSHA1";
  public static final String HMAC_MD5 = "HmacMD5";

  private final Mac mac;

  /**
   * Constructor
   *
   * @param secretKeyBase64 Base64 secret key  Base64 密钥
   *
   * @throws IllegalArgumentException if key is invalid (cannot be base-64-decoded or the decoded key is invalid).
   * * @throws IllegalArgumentException 如果密钥无效（不能进行 base-64 解码或解码的密钥无效）。
   */
  protected BaseParamsDigest(String secretKeyBase64, final String hmacString)
      throws IllegalArgumentException {
    this(secretKeyBase64.getBytes(StandardCharsets.UTF_8), hmacString);
  }

  /**
   * Constructor
   *
   * @param secretKeyBase64 Base64 secret key
   *                        Base64 密钥
   *
   * @throws IllegalArgumentException if key is invalid (cannot be base-64-decoded or the decoded  key is invalid).
   * * @throws IllegalArgumentException 如果密钥无效（不能进行 base-64 解码或解码的密钥无效）。
   */
  protected BaseParamsDigest(byte[] secretKeyBase64, final String hmacString)
      throws IllegalArgumentException {

    final SecretKey secretKey = new SecretKeySpec(secretKeyBase64, hmacString);
    mac = createMac(secretKey, hmacString);
  }

  private Mac createMac(SecretKey secretKey, String hmacString) {
    try {
      Mac mac = Mac.getInstance(hmacString);
      mac.init(secretKey);
      return mac;
    } catch (InvalidKeyException e) {
      throw new IllegalArgumentException("Invalid key for hmac initialization. hmac 初始化的密钥无效。", e);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(
          "Illegal algorithm for post body digest. Check the implementation. 后正文摘要的非法算法。 检查实施。");
    }
  }

  protected static byte[] decodeBase64(String secretKey) {
    return Base64.getDecoder().decode(secretKey);
  }

  public Mac getMac() {
    return mac;
  }
}
