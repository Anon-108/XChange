package org.knowm.xchange;

import static org.knowm.xchange.ExchangeClassUtils.exchangeClassForName;

import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.utils.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory to provide the following to {@link Exchange}:
 * * 工厂向 {@link Exchange} 提供以下内容：
 *
 * <ul>
 *   <li>Manages the creation of specific Exchange implementations using runtime dependencies
 *   * <li>使用运行时依赖项管理特定 Exchange 实现的创建
 * </ul>
 */
public enum ExchangeFactory {
  INSTANCE;

  // flags
  private final Logger log = LoggerFactory.getLogger(ExchangeFactory.class);

  /** Constructor */
  ExchangeFactory() {}

  /**
   * Create an Exchange object with default ExchangeSpecification
   * * 使用默认的 ExchangeSpecification 创建一个 Exchange 对象
   *
   * <p>The factory is parameterized with the name of the exchange implementation class. This must be a class extending {@link org.knowm.xchange.Exchange}.
   * * <p>工厂用交换实现类的名称参数化。 这必须是扩展 {@link org.knowm.xchange.Exchange} 的类。
   *
   * @param exchangeClassName the fully-qualified class name of the exchange
   *                          交易所的完全限定类名
   *
   * @return a new exchange instance configured with the default {@link org.knowm.xchange.ExchangeSpecification}
   * @return 使用默认 {@link org.knowm.xchange.ExchangeSpecification} 配置的新交换实例
   */
  public Exchange createExchange(String exchangeClassName) {

    return createExchange(exchangeClassName, null, null);
  }

  /**
   * Create an Exchange object with default ExchangeSpecification
   * * 使用默认的 ExchangeSpecification 创建一个 Exchange 对象
   *
   * <p>The factory is parameterized with the name of the exchange implementation class. This must be a class extending {@link org.knowm.xchange.Exchange}.
   * * <p>工厂用交换实现类的名称参数化。 这必须是扩展 {@link org.knowm.xchange.Exchange} 的类。
   *
   * @param exchangeClass the class of the exchange
   *                      交易所的等级
   *
   * @return a new exchange instance configured with the default {@link org.knowm.xchange.ExchangeSpecification}
   * * @return 使用默认 {@link org.knowm.xchange.ExchangeSpecification} 配置的新交换实例
   */
  public <T extends Exchange> T createExchange(Class<T> exchangeClass) {

    return createExchange(exchangeClass, null, null);
  }

  /**
   * Create an Exchange object with default ExchangeSpecification with authentication info and API keys provided through parameters
   * * 使用默认的 ExchangeSpecification 创建一个 Exchange 对象，其中包含通过参数提供的身份验证信息和 API 密钥
   *
   * <p>The factory is parameterized with the name of the exchange implementation class. This must be a class extending {@link org.knowm.xchange.Exchange}.
   * * <p>工厂用交换实现类的名称参数化。 这必须是扩展 {@link org.knowm.xchange.Exchange} 的类。
   *
   * @param exchangeClassName the fully-qualified class name of the exchange
   *                          交易所的完全限定类名
   *
   * @param apiKey the public API key
   *               公开 API 密钥
   *
   * @param secretKey the secret API key
   *                  秘密 API 密钥
   *
   * @return a new exchange instance configured with the default {@link org.knowm.xchange.ExchangeSpecification}
   * * @return 使用默认 {@link org.knowm.xchange.ExchangeSpecification} 配置的新交换实例
   */
  public Exchange createExchange(String exchangeClassName, String apiKey, String secretKey) {

    Assert.notNull(exchangeClassName, "exchangeClassName cannot be null | exchangeClassName 不能为空");

    log.debug("Creating default exchange from class name 从类名创建默认交换");

    Exchange exchange = createExchangeWithoutSpecification(exchangeClassName);

    ExchangeSpecification defaultExchangeSpecification = exchange.getDefaultExchangeSpecification();
    if (apiKey != null) defaultExchangeSpecification.setApiKey(apiKey);
    if (secretKey != null) defaultExchangeSpecification.setSecretKey(secretKey);
    exchange.applySpecification(defaultExchangeSpecification);

    return exchange;
  }

  /**
   * Create an Exchange object with default ExchangeSpecification with authentication info and API keys provided through parameters
   * * 使用默认的 ExchangeSpecification 创建一个 Exchange 对象，其中包含通过参数提供的身份验证信息和 API 密钥
   *
   * <p>The factory is parameterized with the name of the exchange implementation class. This must be a class extending {@link org.knowm.xchange.Exchange}.
   * * <p>工厂用交换实现类的名称参数化。 这必须是扩展 {@link org.knowm.xchange.Exchange} 的类。
   *
   * @param exchangeClass the class of the exchange
   *                      交易所的等级
   *
   * @param apiKey the public API key
   *               公开 API 密钥
   *
   * @param secretKey the secret API key
   *                  秘密 API 密钥
   *
   * @return a new exchange instance configured with the default {@link org.knowm.xchange.ExchangeSpecification}
   * * @return 使用默认 {@link org.knowm.xchange.ExchangeSpecification} 配置的新交换实例
   */
  public <T extends Exchange> T createExchange(
      Class<T> exchangeClass, String apiKey, String secretKey) {

    Assert.notNull(exchangeClass, "exchange cannot be null");

    log.debug("Creating default exchange from class name 从类名创建默认交换");

    T exchange = createExchangeWithoutSpecification(exchangeClass);

    ExchangeSpecification defaultExchangeSpecification = exchange.getDefaultExchangeSpecification();
    if (apiKey != null) defaultExchangeSpecification.setApiKey(apiKey);
    if (secretKey != null) defaultExchangeSpecification.setSecretKey(secretKey);
    exchange.applySpecification(defaultExchangeSpecification);

    return exchange;
  }

  /**
   * Create an Exchange object default ExchangeSpecification
   * * 创建一个Exchange对象默认ExchangeSpecification
   *
   * @param exchangeSpecification the exchange specification
   *                              交换规范
   *
   * @return a new exchange instance configured with the provided {@link  org.knowm.xchange.ExchangeSpecification}
   * * @return 使用提供的 {@link org.knowm.xchange.ExchangeSpecification} 配置的新交换实例
   */
  public Exchange createExchange(ExchangeSpecification exchangeSpecification) {

    Assert.notNull(exchangeSpecification, "exchangeSpecfication cannot be null");

    log.debug("Creating exchange from specification 从规范创建交换");

    final Class<? extends Exchange> exchangeClass = exchangeSpecification.getExchangeClass();
    Exchange exchange = createExchangeWithoutSpecification(exchangeClass);
    exchange.applySpecification(exchangeSpecification);
    return exchange;
  }

  /**
   * Create an Exchange object without default ExchangeSpecification
   * * 创建一个没有默认 ExchangeSpecification 的 Exchange 对象
   *
   * <p>The factory is parameterized with the name of the exchange implementation class. This must be a class extending {@link org.knowm.xchange.Exchange}.
   * * <p>工厂用交换实现类的名称参数化。 这必须是扩展 {@link org.knowm.xchange.Exchange} 的类。
   *
   * @param exchangeClassName the fully-qualified class name of the exchange
   *                          * @param exchangeClassName 交易所的全限定类名
   *
   * @return a new exchange instance configured with the default {@link  org.knowm.xchange.ExchangeSpecification}
   * * @return 使用默认 {@link org.knowm.xchange.ExchangeSpecification} 配置的新交换实例
   */
  public Exchange createExchangeWithoutSpecification(String exchangeClassName) {
    Assert.notNull(exchangeClassName, "exchangeClassName cannot be null");
    log.debug("Creating default exchange from class name 从类名创建默认交换");
    return createExchangeWithoutSpecification(exchangeClassForName(exchangeClassName));
  }

  /**
   * Create an Exchange object without default ExchangeSpecification
   * * 创建一个没有默认 ExchangeSpecification 的 Exchange 对象
   *
   * <p>The factory is parameterized with the name of the exchange implementation class. This must be a class extending {@link org.knowm.xchange.Exchange}.
   * * <p>工厂用交换实现类的名称参数化。 这必须是扩展 {@link org.knowm.xchange.Exchange} 的类。
   *
   * @param exchangeClass the class of the exchange  交易所的等级
   * @return a new exchange instance configured with the default {@link org.knowm.xchange.ExchangeSpecification}
   * * @return 使用默认 {@link org.knowm.xchange.ExchangeSpecification} 配置的新交换实例
   */
  public <T extends Exchange> T createExchangeWithoutSpecification(Class<T> exchangeClass) {

    Assert.notNull(exchangeClass, "exchangeClassName cannot be null");

    log.debug("Creating default exchange from class name 从类名创建默认交换");

    // Attempt to create an instance of the exchange provider
    // 尝试创建交换提供者的实例
    try {

      // Instantiate through the default constructor and use the default exchange specification
      // 通过默认构造函数实例化，使用默认交换规范
      return exchangeClass.newInstance();

    } catch (InstantiationException e) {
      throw new ExchangeException("Problem creating Exchange (instantiation) 创建 Exchange 时出现问题（实例化）", e);
    } catch (IllegalAccessException e) {
      throw new ExchangeException("Problem creating Exchange (illegal access) 创建 Exchange 时出现问题（非法访问）", e);
    }
  }
}
