package org.knowm.xchange;

import static org.knowm.xchange.ExchangeClassUtils.exchangeClassForName;

import java.util.HashMap;
import java.util.Map;

/**
 * 交换规范
 * Specification to provide the following to {@link ExchangeFactory}:
 * * 向 {@link ExchangeFactory} 提供以下内容的规范：
 *
 * <ul>
 *   <li>Provision of required exchangeSpecificParameters for creating an {@link Exchange}
 *   <li>Provision of optional exchangeSpecificParameters for additional configuration
 *   <li>提供创建{@link Exchange}所需的exchangeSpecificParameters
 *   * <li>提供可选的exchangeSpecificParameters用于额外的配置
 * </ul>
 */
public class ExchangeSpecification {

  private final Class<? extends Exchange> exchangeClass;
  private String exchangeName;
  private String exchangeDescription;
  private String userName;
  private String password;
  private String secretKey;
  private String apiKey;
  private String sslUri;
  private String plainTextUri;
  private String overrideWebsocketApiUri;
  private String host;
  private int port = 80;
  private String proxyHost;
  private Integer proxyPort;
  private int httpConnTimeout = 0; // default rescu configuration will be used if value not changed // 如果值没有改变，将使用默认的 rescu 配置
  private int httpReadTimeout = 0; // default rescu configuration will be used if value not changed  // 如果值没有改变，将使用默认的 rescu 配置
  private ResilienceSpecification resilience = new ResilienceSpecification();
  private String metaDataJsonFileOverride = null;
  private boolean shouldLoadRemoteMetaData = true; // default value
  /** arbitrary exchange params that can be set for unique cases
   * 可以为特殊情况设置的任意交换参数*/
  private Map<String, Object> exchangeSpecificParameters = new HashMap<>();

  /**
   * Dynamic binding
   * * 动态绑定
   *
   * @param exchangeClassName The exchange class name (e.g. "org.knowm.xchange.mtgox.v1.MtGoxExchange")
   *                          * @param exchangeClassName 交换类名称（例如“org.knowm.xchange.mtgox.v1.MtGoxExchange”）
   *
   * @deprecated use constructor with exchange class for better performance
   * * @deprecated 使用带有交换类的构造函数以获得更好的性能
   */
  @Deprecated
  public ExchangeSpecification(String exchangeClassName) {
    this(exchangeClassForName(exchangeClassName));
  }

  /**
   * Static binding
   * * 静态绑定
   *
   * @param exchangeClass The exchange class
   *                      交换类
   */
  public ExchangeSpecification(Class<? extends Exchange> exchangeClass) {
    this.exchangeClass = exchangeClass;
  }

  /** @return The exchange class for loading at runtime
   *      运行时加载的交换类 */
  public Class<? extends Exchange> getExchangeClass() {
    return exchangeClass;
  }

  /**
   * @return The exchange class name for loading at runtime
   * * @return 运行时加载的交换类名
   *
   * @see this#getExchangeClass @看到这个#getExchangeClass
   * @deprecated use getExchangeClass * @不推荐使用getExchangeClass
   */
  @Deprecated
  public String getExchangeClassName() {
    return exchangeClass.getName();
  }

  /**
   * @param key The key into the parameter map (recommend using the provided standard static  entries)
   *            * @param key 参数映射的键（推荐使用提供的标准静态条目）
   *
   * @return Any additional exchangeSpecificParameters that the {@link Exchange} may consume to   configure services
   * * @return {@link Exchange} 可能用于配置服务的任何其他 exchangeSpecificParameters
   */
  public Object getParameter(String key) {

    return exchangeSpecificParameters.get(key);
  }

  /**
   * Get the host name of the server providing data (e.g. "mtgox.com").
   * * 获取提供数据的服务器的主机名（例如“mtgox.com”）。
   *
   * @return the host name  主机名
   */
  public String getHost() {

    return host;
  }

  /**
   * Set the host name of the server providing data.
   * * 设置提供数据的服务器的主机名。
   *
   * @param host the host name 主机名
   */
  public void setHost(String host) {

    this.host = host;
  }

  /**
   * Get the host name of the http proxy server (e.g. "proxy.com").
   * * 获取 http 代理服务器的主机名（例如“proxy.com”）。
   *
   * @return the proxy host name
   *      代理主机名
   */
  public String getProxyHost() {

    return proxyHost;
  }

  /**
   * Set the host name of the http proxy server.
   * 设置 http 代理服务器的主机名。
   *
   * @param proxyHost the proxy host name
   *                  代理主机名
   */
  public void setProxyHost(String proxyHost) {

    this.proxyHost = proxyHost;
  }

  /**
   * Get the port of the http proxy server (e.g. "80").
   * * 获取http代理服务器的端口（例如“80”）。
   *
   * @return the http proxy port
   * * @return http 代理端口
   */
  public Integer getProxyPort() {

    return proxyPort;
  }

  /**
   * Get the port of the http proxy server.
   * * 获取http代理服务器的端口。
   *
   * @param proxyPort the host name
   *                  主机名
   */
  public void setProxyPort(Integer proxyPort) {

    this.proxyPort = proxyPort;
  }

  /**
   * Get the API key. For MtGox this would be the "Rest-Key" field.
   * * 获取 API 密钥。 对于 MtGox，这将是“Rest-Key”字段。
   *
   * @return the API key
   */
  public String getApiKey() {

    return apiKey;
  }

  /**
   * Set the API key. For MtGox this would be the "Rest-Key" field.
   * * 设置 API 密钥。 对于 MtGox，这将是“Rest-Key”字段。
   *
   * @param apiKey the API key
   *               API 密钥
   */
  public void setApiKey(String apiKey) {

    this.apiKey = apiKey;
  }

  /**
   * Get the port number of the server providing direct socket data (e.g. "1337").
   * * 获取提供直接套接字数据的服务器的端口号（例如“1337”）。
   *
   * @return the port number
   * * @return 端口号
   */
  public int getPort() {

    return port;
  }

  /**
   * Set the port number of the server providing direct socket data (e.g. "1337").
   * * 设置提供直接套接字数据的服务器的端口号（例如“1337”）。
   *
   * @param port the port number
   *             * @param port 端口号
   */
  public void setPort(int port) {

    this.port = port;
  }

  /**
   * Get the http connection timeout for the connection. If the default value of zero is returned
    then the default rescu timeout will be applied. Check the exchange code to see if this option has been implemented.
   获取连接的 http 连接超时。 如果返回默认值零
   然后将应用默认的救援超时。 检查交换代码以查看是否已实施此选项。
   *
   * @return the http read timeout in milliseconds
   * * @return 以毫秒为单位的 http 读取超时
   */
  public int getHttpConnTimeout() {

    return httpConnTimeout;
  }

  /**
   * Set the http connection timeout for the connection. If not supplied the default rescu timeout
    will be used. Check the exchange code to see if this option has been implemented. (This value
    can also be set globally in "rescu.properties" by setting the property "rescu.http.connTimeoutMillis".)
   设置连接的 http 连接超时。 如果未提供默认救援超时
   将会被使用。 检查交换代码以查看是否已实施此选项。 （这个值
   也可以通过设置属性“rescu.http.connTimeoutMillis”在“rescu.properties”中全局设置。）
   *
   * @param milliseconds the http read timeout in milliseconds
   *                     http 读取超时（以毫秒为单位）
   */
  public void setHttpConnTimeout(int milliseconds) {

    this.httpConnTimeout = milliseconds;
  }

  /**
   * Get the http read timeout for the connection. If the default value of zero is returned then the
    default rescu timeout will be applied. Check the exchange code to see if this option has been implemented.
   获取连接的 http 读取超时。 如果返回默认值零，则
   将应用默认救援超时。 检查交换代码以查看是否已实施此选项。
   *
   * @return the http read timeout in milliseconds
   * * @return 以毫秒为单位的 http 读取超时
   */
  public int getHttpReadTimeout() {

    return httpReadTimeout;
  }

  /**
   * Set the http read timeout for the connection. If not supplied the default rescu timeout will be
    used. Check the exchange code to see if this option has been implemented. (This value can also
    be set globally in "rescu.properties" by setting the property "rescu.http.readTimeoutMillis".)
   设置连接的 http 读取超时。 如果未提供默认救援超时将是
   用过的。 检查交换代码以查看是否已实施此选项。 （这个值也可以
   通过设置属性“rescu.http.readTimeoutMillis”在“rescu.properties”中全局设置。）
   *
   * @param milliseconds the http read timeout in milliseconds
   *                     http 读取超时（以毫秒为单位）
   */
  public void setHttpReadTimeout(int milliseconds) {

    this.httpReadTimeout = milliseconds;
  }

  /**
   * Get the API secret key typically used in HMAC signing of requests. For MtGox this would be the "Rest-Sign" field.
   * * 获取通常在请求的 HMAC 签名中使用的 API 密钥。 对于 MtGox，这将是“Rest-Sign”字段。
   *
   * @return the secret key
   * * @return 密钥
   */
  public String getSecretKey() {

    return secretKey;
  }

  /**
   * Set the API secret key typically used in HMAC signing of requests. For MtGox this would be the "Rest-Sign" field.
   * * 设置通常在请求的 HMAC 签名中使用的 API 密钥。 对于 MtGox，这将是“Rest-Sign”字段。
   *
   * @param secretKey the secret key
   *                  密钥
   */
  public void setSecretKey(String secretKey) {

    this.secretKey = secretKey;
  }

  /**
   * Get the URI to reach the <b>root</b> of the exchange API for SSL queries (e.g. use "https://example.com:8443/exchange", not "https://example.com:8443/exchange/api/v3/trades").
   ** 获取 URI 以到达用于 SSL 查询的交换 API 的 <b>root</b>（例如，使用“https://example.com:8443/exchange”，而不是“https://example.com:8443” /exchange/api/v3/trades”）。
   *
   * @return the SSL URI
   */
  public String getSslUri() {

    return sslUri;
  }

  /**
   * Set the URI to reach the <b>root</b> of the exchange API for SSL queries (e.g. use "https://example.com:8443/exchange", not "https://example.com:8443/exchange/api/v3/trades").
   * * 将 URI 设置为到达 Exchange API 的 <b>root</b> 以进行 SSL 查询（例如，使用“https://example.com:8443/exchange”，而不是“https://example.com:8443” /exchange/api/v3/trades”）。
   *
   * @param uri the SSL URI
   */
  public void setSslUri(String uri) {

    this.sslUri = uri;
  }

  /**
   * Get the URI to reach the <b>root</b> of the exchange API for plaintext (non-SSL) queries (e.g. use "http://example.com:8080/exchange", not "http://example.com:8080/exchange/api/v3/trades")
   * * 获取 URI 以到达用于纯文本（非 SSL）查询的交换 API 的 <b>root</b>（例如，使用“http://example.com:8080/exchange”，而不是“http://” example.com:8080/exchange/api/v3/trades")
   *
   * @return the plain text URI
   * * @return 纯文本 URI
   */
  public String getPlainTextUri() {

    return plainTextUri;
  }

  /**
   * Set the URI to reach the <b>root</b> of the exchange API for plaintext (non-SSL) queries (e.g. use "http://example.com:8080/exchange", not "http://example.com:8080/exchange/api/v3/trades")
   * * 将 URI 设置为到达交换 API 的 <b>root</b> 以进行纯文本（非 SSL）查询（例如，使用“http://example.com:8080/exchange”，而不是“http:// example.com:8080/exchange/api/v3/trades")
   *
   * @param plainTextUri the plain text URI
   *                     纯文本 URI
   */
  public void setPlainTextUri(String plainTextUri) {

    this.plainTextUri = plainTextUri;
  }

  /**
   * Get the arbitrary exchange-specific parameters to be passed to the exchange implementation.
   * * 获取要传递给交换实现的任意特定于交换的参数。
   *
   * @return a Map of named exchange-specific parameter values
   * * @return 指定交换特定参数值的映射
   */
  public Map<String, Object> getExchangeSpecificParameters() {

    return exchangeSpecificParameters;
  }

  /**
   * Set the arbitrary exchange-specific parameters to be passed to the exchange implementation.
   * * 设置要传递给交换实现的任意交换特定参数。
   *
   * @param exchangeSpecificParameters a Map of named exchange-specific parameter values
   *                                   命名交换特定参数值的映射
   */
  public void setExchangeSpecificParameters(Map<String, Object> exchangeSpecificParameters) {

    this.exchangeSpecificParameters = exchangeSpecificParameters;
  }

  /**
   * Get an item from the arbitrary exchange-specific parameters to be passed to the exchange implementation.
   * * 从任意交换特定参数中获取要传递给交换实现的项目。
   *
   * @return a Map of named exchange-specific parameter values
   *        命名交换特定参数值的映射
   */
  public Object getExchangeSpecificParametersItem(String key) {

    return exchangeSpecificParameters.get(key);
  }

  /**
   * Set an item in the arbitrary exchange-specific parameters to be passed to the exchange implementation.
   * 在要传递给交换实现的任意交换特定参数中设置一个项目。
   */
  public void setExchangeSpecificParametersItem(String key, Object value) {

    this.exchangeSpecificParameters.put(key, value);
  }

  /**
   * Get the password for authentication.
   * 获取验证密码。
   *
   * @return the password 密码
   */
  public String getPassword() {

    return password;
  }

  /**
   * Set the password for authentication.
   * 设置验证密码。
   *
   * @param password the password
   */
  public void setPassword(String password) {

    this.password = password;
  }

  /**
   * Get the username for authentication.
   * * 获取用户名进行认证。
   *
   * @return the username
   */
  public String getUserName() {

    return userName;
  }

  /**
   * Set the username for authentication.
   * 设置用于认证的用户名。
   *
   * @param userName the username
   */
  public void setUserName(String userName) {

    this.userName = userName;
  }

  /**
   * Get the exchange name.
   * * 获取交易所名称。
   *
   * @return the exchange name (e.g. "Mt Gox")
   * * @return 交易所名称（例如“Mt Gox”）
   */
  public String getExchangeName() {

    return exchangeName;
  }

  /**
   * Set the exchange name (e.g. "Mt Gox").
   * * 设置交易所名称（例如“Mt Gox”）。
   *
   * @param exchangeName the exchange name
   */
  public void setExchangeName(String exchangeName) {

    this.exchangeName = exchangeName;
  }

  /**
   * Get the exchange description (e.g. "Major exchange specialising in USD, EUR, GBP").
   * * 获取交易所描述（例如“主要交易美元、欧元、英镑的交易所”）。
   *
   * @return the exchange description
   * * @return 交换描述
   */
  public String getExchangeDescription() {

    return exchangeDescription;
  }

  /**
   * Set the exchange description (e.g. "Major exchange specialising in USD, EUR, GBP").
   * * 设置交易所描述（例如“主要交易美元、欧元、英镑的交易所”）。
   *
   * @param exchangeDescription the exchange description
   *                            交换说明
   */
  public void setExchangeDescription(String exchangeDescription) {

    this.exchangeDescription = exchangeDescription;
  }

  public ResilienceSpecification getResilience() {
    return resilience;
  }

  public void setResilience(ResilienceSpecification resilience) {
    this.resilience = resilience;
  }

  /**
   * Get the override file for generating the {@link org.knowm.xchange.dto.meta.ExchangeMetaData}
    object. By default, the {@link org.knowm.xchange.dto.meta.ExchangeMetaData} object is loaded at
    startup from a json file on the classpath with the same name as the name of the exchange as
    defined in {@link ExchangeSpecification}. With this parameter, you can override that file with a file of your choice located outside of the classpath.
   获取用于生成 {@link org.knowm.xchange.dto.meta.ExchangeMetaData} 的覆盖文件
   目的。 默认情况下，{@link org.knowm.xchange.dto.meta.ExchangeMetaData} 对象在
   从类路径上的一个 json 文件启动，该文件的名称与交换的名称相同
   在 {@link ExchangeSpecification} 中定义。 使用此参数，您可以使用位于类路径之外的您选择的文件覆盖该文件。
   *
   * @return
   */
  public String getMetaDataJsonFileOverride() {

    return metaDataJsonFileOverride;
  }

  /**
   * Set the override file for generating the {@link org.knowm.xchange.dto.meta.ExchangeMetaData}  object.
    By default, the {@link org.knowm.xchange.dto.meta.ExchangeMetaData} object is loaded at startup from a json file on the classpath with the same name as the name of the exchange as
    defined in {@link ExchangeSpecification}. With this parameter, you can override that file with a file of your choice located outside of the classpath.
   设置用于生成 {@link org.knowm.xchange.dto.meta.ExchangeMetaData} 对象的覆盖文件。
   默认情况下，{@link org.knowm.xchange.dto.meta.ExchangeMetaData} 对象在启动时从类路径上的一个 json 文件加载，该文件的名称与交换的名称相同
   在 {@link ExchangeSpecification} 中定义。 使用此参数，您可以使用位于类路径之外的您选择的文件覆盖该文件。
   *
   * @return
   */
  public void setMetaDataJsonFileOverride(String metaDataJsonFileOverride) {

    this.metaDataJsonFileOverride = metaDataJsonFileOverride;
  }

  /**
   * By default, some meta data from the exchange is remotely loaded (if implemented).
   * 默认情况下，来自交易所的一些元数据是远程加载的（如果实施）。
   *
   * @return
   */
  public boolean isShouldLoadRemoteMetaData() {

    return shouldLoadRemoteMetaData;
  }

  /**
   * By default, some meta data from the exchange is remotely loaded (if implemented). Here you can set this default behavior.
   * 默认情况下，来自交易所的一些元数据是远程加载的（如果实施）。 您可以在此处设置此默认行为。
   *
   * @param shouldLoadRemoteMetaData
   */
  public void setShouldLoadRemoteMetaData(boolean shouldLoadRemoteMetaData) {

    this.shouldLoadRemoteMetaData = shouldLoadRemoteMetaData;
  }

  /**
   * Get uri to override websocket uri
   * 获取 uri 以覆盖 websocket uri
   *
   * @return The uri that will be used instead of standard exchange websocket uri
   *          将用于代替标准交换 websocket uri 的 uri
   */
  public String getOverrideWebsocketApiUri() {

    return overrideWebsocketApiUri;
  }

  /**
   * Set uri to override websocket uri
   * 设置 uri 以覆盖 websocket uri
   *
   * @param overrideWebsocketApiUri The uri that will be used instead of standard exchange websocket  uris
   *                                将使用的 uri 代替标准交换 websocket uris
   */
  public void setOverrideWebsocketApiUri(String overrideWebsocketApiUri) {

    this.overrideWebsocketApiUri = overrideWebsocketApiUri;
  }

  public static class ResilienceSpecification {
    private boolean retryEnabled = false;
    private boolean rateLimiterEnabled = false;

    /**
     * @see #setRetryEnabled(boolean)
     * @return true if enabled
     * * @return 如果启用则返回 true
     */
    public boolean isRetryEnabled() {
      return retryEnabled;
    }

    /**
     * Flag that lets you enable retry functionality if it was implemented for the given exchange.
     * 如果它是为给定的交换实现的，则允许您启用重试功能的标志。
     *
     * <p>If this feature is implemented and enabled then operations that can be safely retried on socket failures and timeouts will be retried.
     * * <p>如果实现并启用此功能，则可以在套接字失败和超时时安全重试的操作将被重试。
     */
    public void setRetryEnabled(boolean retryEnabled) {
      this.retryEnabled = retryEnabled;
    }

    /**
     * @see #setRetryEnabled(boolean)
     * @return true if enabled
     * @return 如果启用则返回 true
     */
    public boolean isRateLimiterEnabled() {
      return rateLimiterEnabled;
    }

    /**
     * Flag that lets you enable call rate limiting functionality if it was implemented for the given exchange.
     * * 如果它是为给定的交换实现的，则允许您启用呼叫速率限制功能的标志。
     *
     * <p>If this featrue is implemented and enabled then we will limit the amount of calls to the
      exchanges API to not exceeds its limits. This will result in delaying some calls or throwing
      a {@link io.github.resilience4j.ratelimiter.RequestNotPermitted} exception if we would have to wait to long.
     <p>如果此功能已实现并启用，那么我们将限制对
     交换 API 以不超过其限制。 这将导致延迟一些调用或抛出
     如果我们不得不等待很长时间，则会出现 {@link io.github.resilience4j.ratelimiter.RequestNotPermitted} 异常。
     */
    public void setRateLimiterEnabled(boolean rateLimiterEnabled) {
      this.rateLimiterEnabled = rateLimiterEnabled;
    }
  }
}
