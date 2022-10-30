package org.knowm.xchange.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.interceptor.InterceptorProvider;
import si.mazi.rescu.ClientConfig;
import si.mazi.rescu.IRestProxyFactory;
import si.mazi.rescu.Interceptor;
import si.mazi.rescu.RestProxyFactoryImpl;
/**
 *Exchange Rest 代理生成器
 */
public final class ExchangeRestProxyBuilder<T> {
/**
 * rest接口
 */
  private final Class<T> restInterface;
  /**
   * 交换规范
   */
  private final ExchangeSpecification exchangeSpecification;
  /**
   * 自定义拦截器
   */
  private final List<Interceptor> customInterceptors = new ArrayList<>();
  /**
   * 客户端配置定制器
   */
  private final List<ClientConfigCustomizer> clientConfigCustomizers = new ArrayList<>();
  /**
   * 客户配置
   */
  private ClientConfig clientConfig;
  /**
   * 恢复注册
   */
  private ResilienceRegistries resilienceRegistries;
  /**
   * 基础地址
   */
  private String baseUrl;
  /**
   * rest代理工厂
   */
  private IRestProxyFactory restProxyFactory = new RestProxyFactoryImpl();
/**
 * Exchange Rest 代理生成器
 */
  private ExchangeRestProxyBuilder(
      Class<T> restInterface, ExchangeSpecification exchangeSpecification) {
    this.restInterface = restInterface;
    this.exchangeSpecification = exchangeSpecification;
    this.baseUrl =
        Optional.ofNullable(exchangeSpecification.getSslUri())
            .orElseGet(exchangeSpecification::getPlainTextUri);
  }
/**
 * 用于接口
 */
  public static <T> ExchangeRestProxyBuilder<T> forInterface(
      Class<T> restInterface, ExchangeSpecification exchangeSpecification) {
    return new ExchangeRestProxyBuilder<>(restInterface, exchangeSpecification)
        .customInterceptors(InterceptorProvider.provide());
  }
/**
 * 客户配置
 */
  public ExchangeRestProxyBuilder<T> clientConfig(ClientConfig value) {
    this.clientConfig = value;
    return this;
  }
/**
 * 客户配置定制器
 */
  public ExchangeRestProxyBuilder<T> clientConfigCustomizer(
      ClientConfigCustomizer clientConfigCustomizer) {
    this.clientConfigCustomizers.add(clientConfigCustomizer);
    return this;
  }
/**
 * 基础地址
 */
  public ExchangeRestProxyBuilder<T> baseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
    return this;
  }
/**
 *  自定义拦截器
 */
  public ExchangeRestProxyBuilder<T> customInterceptor(Interceptor value) {
    this.customInterceptors.add(value);
    return this;
  }

  /**
   * 自定义拦截器
   * @param interceptors 拦截器
   * @return
   */
  public ExchangeRestProxyBuilder<T> customInterceptors(Collection<Interceptor> interceptors) {
    customInterceptors.addAll(interceptors);
    return this;
  }

  /**
   * rest代理工厂
   * @param restProxyFactory rest代理工厂
   * @return
   */
  public ExchangeRestProxyBuilder<T> restProxyFactory(IRestProxyFactory restProxyFactory) {
    this.restProxyFactory = restProxyFactory;
    return this;
  }

  /**
   * 构建
   * @return
   */
  public T build() {
    if (clientConfig == null) {
      clientConfig = createClientConfig(exchangeSpecification);
    }
    if (resilienceRegistries == null) {
      resilienceRegistries = new ResilienceRegistries();
    }
    clientConfigCustomizers.forEach(
        clientConfigCustomizer -> clientConfigCustomizer.customize(clientConfig));
    return restProxyFactory.createProxy(
        restInterface, baseUrl, clientConfig, customInterceptors.toArray(new Interceptor[0]));
  }

  /**
   * Get a ClientConfig object which contains exchange-specific timeout values  (<i>httpConnTimeout</i> and <i>httpReadTimeout</i>)
    if they were present in the   ExchangeSpecification of this instance.
   获取包含特定于交换的超时值（<i>httpConnTimeout</i> 和 <i>httpReadTimeout</i>）的 ClientConfig 对象
   如果它们出现在此实例的 ExchangeSpecification 中。
   *
   * @return a rescu client config object
   * * @return 一个 rescu 客户端配置对象
   */
  public static ClientConfig createClientConfig(ExchangeSpecification exchangeSpecification) {

    ClientConfig rescuConfig = new ClientConfig(); // create default rescu config  // 创建默认rescu配置

    // set per exchange connection- and read-timeout (if they have been set in the ExchangeSpecification)
    // 设置每个交换连接和读取超时（如果它们已在 ExchangeSpecification 中设置）
    int customHttpConnTimeout = exchangeSpecification.getHttpConnTimeout();
    if (customHttpConnTimeout > 0) {
      rescuConfig.setHttpConnTimeout(customHttpConnTimeout);
    }
    int customHttpReadTimeout = exchangeSpecification.getHttpReadTimeout();
    if (customHttpReadTimeout > 0) {
      rescuConfig.setHttpReadTimeout(customHttpReadTimeout);
    }
    if (exchangeSpecification.getProxyHost() != null) {
      rescuConfig.setProxyHost(exchangeSpecification.getProxyHost());
    }
    if (exchangeSpecification.getProxyPort() != null) {
      rescuConfig.setProxyPort(exchangeSpecification.getProxyPort());
    }
    return rescuConfig;
  }
}
