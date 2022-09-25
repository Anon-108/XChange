package org.knowm.xchange;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.meta.ExchangeMetaData;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.exceptions.NotYetImplementedForExchangeException;
import org.knowm.xchange.instrument.Instrument;
import org.knowm.xchange.service.account.AccountService;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.knowm.xchange.service.trade.TradeService;
import si.mazi.rescu.SynchronizedValueFactory;

/**
 * Interface to provide the following to applications:
 * * 为应用程序提供以下功能的接口：
 *
 * <ul>
 *   <li>Entry point to the XChange APIs
 *   * <li>XChange API 的入口点
 * </ul>
 *
 * <p>The consumer is given a choice of a default (no-args) or configured accessor
 * * <p>消费者可以选择默认（无参数）或配置的访问器
 */
public interface Exchange {

  /** @return The ExchangeSpecification in use for this exchange
   * @return 用于此交换的 ExchangeSpecification*/
  ExchangeSpecification getExchangeSpecification();

  /**
   * The Meta Data defining some semi-static properties of an exchange such as currency pairs, trading fees, etc.
   ** 定义交易所的一些半静态属性的元数据，例如货币对、交易费用等。
   *
   * @return The exchange's meta data
   * @return 交易所的元数据
   */
  ExchangeMetaData getExchangeMetaData();

  /**
   * Returns a list of CurrencyPair objects. This list can either come originally from a loaded json file or from a remote call if the implementation override's the `remoteInit` method.
   * * 返回 CurrencyPair 对象的列表。 如果实现覆盖的 `remoteInit` 方法，此列表可以最初来自加载的 json 文件或来自远程调用。
   *
   * @return The exchange's symbols
   * * @return 交易所的符号
   */
  List<CurrencyPair> getExchangeSymbols();

  /**
   * Returns a list of Instrument objects. This list can either come originally from a loaded json
    file or from a remote call if the implementation override's the `remoteInit` method.
   返回 Instrument 对象的列表。 此列表可以最初来自加载的 json
   文件或远程调用，如果实现覆盖的 `remoteInit` 方法。
   *
   * @return The exchange's instruments
   * @return 交易所的工具
   */
  default List<Instrument> getExchangeInstruments() {
    return new ArrayList<>(getExchangeSymbols());
  }

  /**
   * The nonce factory used to create a nonce value. Allows services to accept a placeholder that is
    replaced with generated value just before message is serialized and sent. If a method of a rest
    accepts ValueFactory as a parameter, it's evaluated, the message is serialized and sent in a single synchronized block.
   用于创建 nonce 值的 nonce 工厂。 允许服务接受占位符
   在消息被序列化和发送之前替换为生成的值。 如果休息的方法
   接受 ValueFactory 作为参数，对其进行评估，消息被序列化并在单个同步块中发送。
   *
   * @return Synchronized value factory
   * * @return 同步值工厂
   */
  SynchronizedValueFactory<Long> getNonceFactory();

  /**
   * resilience4j registries with retry strategies, rate limiters, etc. used for this exchange.
   * * 用于此交换的具有重试策略、速率限制器等的弹性 4j 注册表。
   *
   * @return resilience4j registries
   * * @return 弹性4j 注册表
   *
   * @throws NotYetImplementedForExchangeException if the exchange module does not support  resilience features
   * * @throws NotYetImplementedForExchangeException 如果交换模块不支持弹性功能
   */
  default ResilienceRegistries getResilienceRegistries() {
    throw new NotYetImplementedForExchangeException();
  }

  /**
   * @return A default ExchangeSpecification to use during the creation process if one is not   supplied
   * * @return 如果未提供，则在创建过程中使用的默认 ExchangeSpecification
   */
  ExchangeSpecification getDefaultExchangeSpecification();

  /**
   * Applies any exchange specific parameters
   * * 应用任何交易所特定参数
   *
   * @param exchangeSpecification The {@link ExchangeSpecification}
   */
  void applySpecification(ExchangeSpecification exchangeSpecification);

  /**
   * A market data service typically consists of a regularly updated list of the available prices for the various symbols
   * * 市场数据服务通常包括定期更新的各种交易品种的可用价格列表
   *
   * <p>This is the non-streaming (blocking) version of the service
   * * <p>这是服务的非流式（阻塞）版本
   *
   * @return The exchange's market data service
   * * @return 交易所行情数据服务
   */
  MarketDataService getMarketDataService();

  /**
   * An trade service typically provides access to trading functionality
   * * 交易服务通常提供对交易功能的访问
   *
   * <p>Typically access is restricted by a secret API key and/or username password authentication which are usually provided in the {@link ExchangeSpecification}
   * * <p>通常访问受到 API 密钥和/或用户名密码身份验证的限制，这些身份验证通常在 {@link ExchangeSpecification} 中提供
   *
   * @return The exchange's trade service
   * * @return 交易所的交易服务
   */
  TradeService getTradeService();

  /**
   * An account service typically provides access to the user's private exchange data
   * * 帐户服务通常提供对用户私人交换数据的访问权限
   *
   * <p>Typically access is restricted by a secret API key and/or username password authentication which are usually provided in the {@link ExchangeSpecification}
   * * <p>通常访问受到 API 密钥和/或用户名密码身份验证的限制，这些身份验证通常在 {@link ExchangeSpecification} 中提供
   *
   * @return The exchange's account service
   * * @return 交易所的账户服务
   */
  AccountService getAccountService();

  /**
   * Initialize this instance with the remote meta data. Most exchanges require this method to be
    called before {@link #getExchangeMetaData()}. Some exchanges require it before using some of their services.
   使用远程元数据初始化此实例。 大多数交易所要求这种方法是
   在 {@link #getExchangeMetaData()} 之前调用。 一些交易所在使用他们的一些服务之前需要它。
   */
  void remoteInit() throws IOException, ExchangeException;
}
