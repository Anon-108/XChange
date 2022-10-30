package org.knowm.xchange.binance;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.knowm.xchange.BaseExchange;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.binance.dto.account.AssetDetail;
import org.knowm.xchange.binance.dto.meta.exchangeinfo.BinanceExchangeInfo;
import org.knowm.xchange.binance.dto.meta.exchangeinfo.Filter;
import org.knowm.xchange.binance.dto.meta.exchangeinfo.Symbol;
import org.knowm.xchange.binance.service.BinanceAccountService;
import org.knowm.xchange.binance.service.BinanceMarketDataService;
import org.knowm.xchange.binance.service.BinanceTradeService;
import org.knowm.xchange.client.ExchangeRestProxyBuilder;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.meta.CurrencyMetaData;
import org.knowm.xchange.dto.meta.CurrencyPairMetaData;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.utils.AuthUtils;
import si.mazi.rescu.SynchronizedValueFactory;

/**
 * 币安交换
 */
@Slf4j
public class BinanceExchange extends BaseExchange {
  /**
   * 特定/具体 参数使用沙盒
   */
  public static final String SPECIFIC_PARAM_USE_SANDBOX = "Use_Sandbox";
  /**
   * 恢复注册表
   */
  protected static ResilienceRegistries RESILIENCE_REGISTRIES;
  /**
   * 交换信息
   */
  protected BinanceExchangeInfo exchangeInfo;
  /**
   * Binance认证
   */
  protected BinanceAuthenticated binance;
  /**
   * 同步值工厂
   */
  protected SynchronizedValueFactory<Long> timestampFactory;

  /**
   * 初始化服务
   */
  @Override
  protected void initServices() {
    this.binance =
        ExchangeRestProxyBuilder.forInterface(
                BinanceAuthenticated.class, getExchangeSpecification())
            .build();
    this.timestampFactory =
        new BinanceTimestampFactory(
            binance, getExchangeSpecification().getResilience(), getResilienceRegistries());
    this.marketDataService = new BinanceMarketDataService(this, binance, getResilienceRegistries());
    this.tradeService = new BinanceTradeService(this, binance, getResilienceRegistries());
    this.accountService = new BinanceAccountService(this, binance, getResilienceRegistries());
  }

  public SynchronizedValueFactory<Long> getTimestampFactory() {
    return timestampFactory;
  }

  /**
   * 获得临时的工厂
   * @return
   */
  @Override
  public SynchronizedValueFactory<Long> getNonceFactory() {
    throw new UnsupportedOperationException(
        "Binance uses timestamp/recvwindow rather than a nonce Binance 使用时间戳/recvwindow 而不是 nonce");
  }

  /**
   * 重置恢复注册表
   */
  public static void resetResilienceRegistries() {
    RESILIENCE_REGISTRIES = null;
  }

  /**
   * 得到恢复注册表
   * @return
   */
  @Override
  public ResilienceRegistries getResilienceRegistries() {
    if (RESILIENCE_REGISTRIES == null) {
      RESILIENCE_REGISTRIES = BinanceResilience.createRegistries();
    }
    return RESILIENCE_REGISTRIES;
  }

  @Override
  public ExchangeSpecification getDefaultExchangeSpecification() {

    ExchangeSpecification spec = new ExchangeSpecification(this.getClass());
    spec.setSslUri("https://api.binance.com");
    spec.setHost("www.binance.com");
    spec.setPort(80);
    spec.setExchangeName("Binance");
    spec.setExchangeDescription("Binance Exchange.");
    AuthUtils.setApiAndSecretKey(spec, "binance");
    return spec;
  }

  @Override
  public void applySpecification(ExchangeSpecification exchangeSpecification) {
    concludeHostParams(exchangeSpecification);
    super.applySpecification(exchangeSpecification);
  }

  public BinanceExchangeInfo getExchangeInfo() {
    return exchangeInfo;
  }

  /**
   * 使用沙盒
   * @return
   */
  public boolean usingSandbox() {
    return enabledSandbox(exchangeSpecification);
  }

  @Override
  public void remoteInit() {

    try {
      BinanceMarketDataService marketDataService =
          (BinanceMarketDataService) this.marketDataService;
      exchangeInfo = marketDataService.getExchangeInfo();

      BinanceAccountService accountService = (BinanceAccountService) getAccountService();
      Map<String, AssetDetail> assetDetailMap = null;
      if (!usingSandbox() && isAuthenticated()) {
        assetDetailMap = accountService.getAssetDetails(); // not available in sndbox 在沙盒中不可用
      }

      postInit(assetDetailMap);

    } catch (Exception e) {
      throw new ExchangeException("Failed to initialize  初始化失败: " + e.getMessage(), e);
    }
  }

  /**
   * post 初始化
   * @param assetDetailMap 资产详情map
   */
  protected void postInit(Map<String, AssetDetail> assetDetailMap) {
    // populate currency pair keys only, exchange does not provide any other metadata for download
    //仅填充货币对密钥，交易所不提供任何其他元数据供下载
    Map<CurrencyPair, CurrencyPairMetaData> currencyPairs = exchangeMetaData.getCurrencyPairs();
    Map<Currency, CurrencyMetaData> currencies = exchangeMetaData.getCurrencies();

    // Clear all hardcoded currencies when loading dynamically from exchange.
    // 从交易所动态加载时清除所有硬编码货币。
    if (assetDetailMap != null) {
      currencies.clear();
    }

    Symbol[] symbols = exchangeInfo.getSymbols();

    for (Symbol symbol : symbols) {
      if (symbol.getStatus().equals("TRADING")) { // Symbols which are trading 正在交易的符号
        int basePrecision = Integer.parseInt(symbol.getBaseAssetPrecision());
        int counterPrecision = Integer.parseInt(symbol.getQuotePrecision());
        int pairPrecision = 8;
        int amountPrecision = 8;

        BigDecimal minQty = null;
        BigDecimal maxQty = null;
        BigDecimal stepSize = null;

        BigDecimal counterMinQty = null;
        BigDecimal counterMaxQty = null;

        Filter[] filters = symbol.getFilters();

        CurrencyPair currentCurrencyPair =
            new CurrencyPair(symbol.getBaseAsset(), symbol.getQuoteAsset());

        for (Filter filter : filters) {
          if (filter.getFilterType().equals("PRICE_FILTER")) {
            pairPrecision = Math.min(pairPrecision, numberOfDecimals(filter.getTickSize()));
            counterMaxQty = new BigDecimal(filter.getMaxPrice()).stripTrailingZeros();
          } else if (filter.getFilterType().equals("LOT_SIZE")) {
            amountPrecision = Math.min(amountPrecision, numberOfDecimals(filter.getStepSize()));
            minQty = new BigDecimal(filter.getMinQty()).stripTrailingZeros();
            maxQty = new BigDecimal(filter.getMaxQty()).stripTrailingZeros();
            stepSize = new BigDecimal(filter.getStepSize()).stripTrailingZeros();
          } else if (filter.getFilterType().equals("MIN_NOTIONAL")) {
            counterMinQty = new BigDecimal(filter.getMinNotional()).stripTrailingZeros();
          }
        }

        boolean marketOrderAllowed = Arrays.asList(symbol.getOrderTypes()).contains("MARKET");
        currencyPairs.put(
            currentCurrencyPair,
            new CurrencyPairMetaData(
                new BigDecimal("0.1"), // Trading fee at Binance is 0.1 % 币安的交易费为 0.1%
                minQty, // Min amount 最小数量
                maxQty, // Max amount 最大数量
                counterMinQty,
                counterMaxQty,
                amountPrecision, // base precision 基础精度
                pairPrecision, // counter precision  计数器精度
                null,
                null, /* TODO get fee tiers, although this is not necessary now   because their API returns current fee directly
                              获取费用等级，尽管现在不需要这样做，因为他们的 API 直接返回当前费用*/
                stepSize,
                null,
                marketOrderAllowed));

        Currency baseCurrency = currentCurrencyPair.base;
        CurrencyMetaData baseCurrencyMetaData =
            BinanceAdapters.adaptCurrencyMetaData(
                currencies, baseCurrency, assetDetailMap, basePrecision);
        currencies.put(baseCurrency, baseCurrencyMetaData);

        Currency counterCurrency = currentCurrencyPair.counter;
        CurrencyMetaData counterCurrencyMetaData =
            BinanceAdapters.adaptCurrencyMetaData(
                currencies, counterCurrency, assetDetailMap, counterPrecision);
        currencies.put(counterCurrency, counterCurrencyMetaData);
      }
    }
  }

  /**
   * 已通过身份验证
   * @return
   */
  private boolean isAuthenticated() {
    return exchangeSpecification != null
        && exchangeSpecification.getApiKey() != null
        && exchangeSpecification.getSecretKey() != null;
  }

  /**
   * 小数位数
   * @param value
   * @return
   */
  private int numberOfDecimals(String value) {
    return new BigDecimal(value).stripTrailingZeros().scale();
  }

  /**
   *断定，推断出；结束，终止 主机参数
   * Adjust host parameters depending on exchange specific parameters
   * 根据交易所特定参数调整主机参数
   * */
  private static void concludeHostParams(ExchangeSpecification exchangeSpecification) {
    if (exchangeSpecification.getExchangeSpecificParameters() != null) {
      if (enabledSandbox(exchangeSpecification)) {
        exchangeSpecification.setSslUri("https://testnet.binance.vision");
        exchangeSpecification.setHost("testnet.binance.vision");
      }
    }
  }

  /**
   * 启用沙箱
   * @param exchangeSpecification
   * @return
   */
  private static boolean enabledSandbox(ExchangeSpecification exchangeSpecification) {
    return Boolean.TRUE.equals(
        exchangeSpecification.getExchangeSpecificParametersItem(SPECIFIC_PARAM_USE_SANDBOX));
  }
}
