package org.knowm.xchange;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.meta.ExchangeMetaData;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.service.BaseExchangeService;
import org.knowm.xchange.service.account.AccountService;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.knowm.xchange.service.trade.TradeService;
import org.knowm.xchange.utils.nonce.CurrentTimeIncrementalNonceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import si.mazi.rescu.SynchronizedValueFactory;

/**
 * 基础交换
 */
public abstract class BaseExchange implements Exchange {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  protected ExchangeSpecification exchangeSpecification;
  protected ExchangeMetaData exchangeMetaData;
  protected MarketDataService marketDataService;
  protected TradeService tradeService;
  protected AccountService accountService;

  private final SynchronizedValueFactory<Long> nonceFactory =
      new CurrentTimeIncrementalNonceFactory(TimeUnit.MILLISECONDS);

  protected abstract void initServices();

  @Override
  public void applySpecification(ExchangeSpecification exchangeSpecification) {

    ExchangeSpecification defaultSpecification = getDefaultExchangeSpecification();

    // Check if default is for everything
    // 检查默认值是否适用于所有内容
    if (exchangeSpecification == null) {
      this.exchangeSpecification = defaultSpecification;
    } else {
      // Using a configured exchange
      // 使用配置的交换
      // fill in null params with the default ones
      // 用默认参数填充空参数
      if (exchangeSpecification.getExchangeName() == null) {
        exchangeSpecification.setExchangeName(defaultSpecification.getExchangeName());
      }
      if (exchangeSpecification.getExchangeDescription() == null) {
        exchangeSpecification.setExchangeDescription(defaultSpecification.getExchangeDescription());
      }
      if (exchangeSpecification.getSslUri() == null) {
        exchangeSpecification.setSslUri(defaultSpecification.getSslUri());
      }
      if (exchangeSpecification.getHost() == null) {
        exchangeSpecification.setHost(defaultSpecification.getHost());
      }
      if (exchangeSpecification.getPlainTextUri() == null) {
        exchangeSpecification.setPlainTextUri(defaultSpecification.getPlainTextUri());
      }
      if (exchangeSpecification.getExchangeSpecificParameters() == null) {
        exchangeSpecification.setExchangeSpecificParameters(
            defaultSpecification.getExchangeSpecificParameters());
      } else {
        // add default value unless it is overridden by current spec
        // 添加默认值，除非它被当前规范覆盖
        for (Map.Entry<String, Object> entry :
            defaultSpecification.getExchangeSpecificParameters().entrySet()) {
          if (exchangeSpecification.getExchangeSpecificParametersItem(entry.getKey()) == null) {
            exchangeSpecification.setExchangeSpecificParametersItem(
                entry.getKey(), entry.getValue());
          }
        }
      }

      this.exchangeSpecification = exchangeSpecification;
    }

    if (this.exchangeSpecification.getMetaDataJsonFileOverride()
        != null) { // load the metadata from the file system // 从文件系统加载元数据

      try (InputStream is =
          new FileInputStream(this.exchangeSpecification.getMetaDataJsonFileOverride())) {
        loadExchangeMetaData(is);
      } catch (IOException e) {
        throw new ExchangeException(e);
      }

    } else if (this.exchangeSpecification.getExchangeName()
        != null) { // load the metadata from the classpath // 从类路径加载元数据

      String metadataFileName = getMetaDataFileName(this.exchangeSpecification) + ".json";
      logger.debug("Loading metadata from  从加载元数据{}", metadataFileName);
      try (InputStream is =
          BaseExchangeService.class.getClassLoader().getResourceAsStream(metadataFileName)) {

        loadExchangeMetaData(is);
      } catch (IOException e) {
        throw new ExchangeException(e);
      }

    } else {
      logger.warn(
          "No \"exchange name 交换名称\" found in the ExchangeSpecification 在 ExchangeSpecification 中找到. The name is used to load the meta data file from the classpath and may lead to unexpected results." +
                  "该名称用于从类路径加载元数据文件，可能会导致意外结果。");
    }

    initServices();

    if (this.exchangeSpecification.isShouldLoadRemoteMetaData()) {
      try {
        logger.info("Calling Remote Init...调用远程初始化...");
        remoteInit();
      } catch (IOException e) {
        throw new ExchangeException(e);
      }
    }
  }

  @Override
  public void remoteInit() throws IOException, ExchangeException {

    logger.info(
        "No remote initialization implemented for 没有实现远程初始化{}. " +
                "The exchange meta data for this exchange is loaded from a json file containing hard-coded exchange meta-data. " +
                "此交换的交换元数据是从包含硬编码交换元数据的 json 文件加载的。" +
                "This may or may not be OK for you, and you should understand exactly how this works." +
                "这对您来说可能合适，也可能不合适，您应该确切了解它是如何工作的。 " +
                "Each exchange can either 1) rely on the hard-coded json file that comes packaged with XChange's jar, 2) provide your own override json file," +
                "每个交换都可以 1) 依赖 XChange 的 jar 附带的硬编码 json 文件，2) 提供您自己的覆盖 json 文件， " +
                "3) properly implement the `remoteInit()` method for the exchange (please submit a pull request so the whole community can benefit) or " +
                "3) 正确实现交易所的 `remoteInit()` 方法（请提交拉取请求，以便整个社区都能受益）或" +
                "4) a combination of hard-coded JSON and remote API calls. For more info see: https://github.com/timmolter/XChange/wiki/Design-Notes#exchange-metadata" +
                "4) 硬编码 JSON 和远程 API 调用的组合。 有关更多信息，请参阅：https://github.com/timmolter/XChange/wiki/Design-Notes#exchange-metadata",
        exchangeSpecification.getExchangeName());
  }

  @Override
  public SynchronizedValueFactory<Long> getNonceFactory() {
    return nonceFactory;
  }

  protected void loadExchangeMetaData(InputStream is) {

    exchangeMetaData = loadMetaData(is, ExchangeMetaData.class);
  }

  protected <T> T loadMetaData(InputStream is, Class<T> type) {

    // Use Jackson to parse it
    // 使用 Jackson 解析
    ObjectMapper mapper = new ObjectMapper();

    try {
      T result = mapper.readValue(is, type);
      logger.debug(result.toString());
      return result;
    } catch (Exception e) {
      logger.warn(
          "An exception occured while loading the metadata file from the file system." +
                  "从文件系统加载元数据文件时发生异常。 " +
                  "This is just a warning and can be ignored, but it may lead to unexpected results, so it's better to address it." +
                  "这只是一个警告，可以忽略，但可能会导致意想不到的结果，因此最好解决它。",
          e);
      return null;
    }
  }

  @Override
  public List<CurrencyPair> getExchangeSymbols() {

    return new ArrayList<>(getExchangeMetaData().getCurrencyPairs().keySet());
  }

  public String getMetaDataFileName(ExchangeSpecification exchangeSpecification) {

    return exchangeSpecification
        .getExchangeName()
        .toLowerCase()
        .replace(" ", "")
        .replace("-", "")
        .replace(".", "");
  }

  @Override
  public ExchangeSpecification getExchangeSpecification() {

    return exchangeSpecification;
  }

  @Override
  public ExchangeMetaData getExchangeMetaData() {

    return exchangeMetaData;
  }

  public MarketDataService getMarketDataService() {

    return marketDataService;
  }

  public TradeService getTradeService() {

    return tradeService;
  }

  public AccountService getAccountService() {

    return accountService;
  }

  @Override
  public String toString() {

    String name =
        exchangeSpecification != null
            ? exchangeSpecification.getExchangeName()
            : getClass().getName();
    return name + "#" + hashCode();
  }
}
