package org.knowm.xchange.bitfinex;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.knowm.xchange.BaseExchange;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.bitfinex.dto.BitfinexException;
import org.knowm.xchange.bitfinex.service.BitfinexAccountService;
import org.knowm.xchange.bitfinex.service.BitfinexAdapters;
import org.knowm.xchange.bitfinex.service.BitfinexMarketDataService;
import org.knowm.xchange.bitfinex.service.BitfinexMarketDataServiceRaw;
import org.knowm.xchange.bitfinex.service.BitfinexTradeService;
import org.knowm.xchange.bitfinex.v1.dto.account.BitfinexAccountFeesResponse;
import org.knowm.xchange.bitfinex.v1.dto.marketdata.BitfinexSymbolDetail;
import org.knowm.xchange.bitfinex.v1.dto.trade.BitfinexAccountInfosResponse;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.utils.nonce.AtomicLongIncrementalTime2013NonceFactory;
import si.mazi.rescu.SynchronizedValueFactory;
@Slf4j
public class BitfinexExchange extends BaseExchange implements Exchange {

  private static ResilienceRegistries RESILIENCE_REGISTRIES;

  private SynchronizedValueFactory<Long> nonceFactory =
      new AtomicLongIncrementalTime2013NonceFactory();

  /**
   * 初始化服务
   */
  @Override
  protected void initServices() {
    this.marketDataService = new BitfinexMarketDataService(this, getResilienceRegistries());
    this.accountService = new BitfinexAccountService(this, getResilienceRegistries());
    this.tradeService = new BitfinexTradeService(this, getResilienceRegistries());
  }

  /**
   * 获取恢复注册表
   * @return
   */
  @Override
  public ResilienceRegistries getResilienceRegistries() {
    if (RESILIENCE_REGISTRIES == null) {
      RESILIENCE_REGISTRIES = BitfinexResilience.createRegistries();
    }
    return RESILIENCE_REGISTRIES;
  }

  /**
   * 获取默认交换规范
   * @return
   */
  @Override
  public ExchangeSpecification getDefaultExchangeSpecification() {
    // 请求代理接口  仅在本地测试开启
//    System.setProperty("proxyType", "4");
//    System.setProperty("proxyPort", Integer.toString(10809));
//    System.setProperty("proxyHost", "127.0.0.1");
//    System.setProperty("proxySet", "true");
    ExchangeSpecification exchangeSpecification = new ExchangeSpecification(this.getClass());
    exchangeSpecification.setSslUri("https://api.bitfinex.com/");
    exchangeSpecification.setHost("api.bitfinex.com");
    exchangeSpecification.setPort(80);
    exchangeSpecification.setExchangeName("BitFinex");
    exchangeSpecification.setExchangeDescription("BitFinex is a bitcoin exchange.");
    exchangeSpecification.getResilience().setRateLimiterEnabled(true);
    exchangeSpecification.getResilience().setRetryEnabled(true);
    // TODO 请求代理接口  仅在本地测试开启
    exchangeSpecification.setProxyHost("127.0.0.1");
    exchangeSpecification.setProxyPort(10809);
    log.info("当前代理地址为:"+exchangeSpecification.getProxyHost());
    log.info("当前代理端口为:"+exchangeSpecification.getProxyPort());


    return exchangeSpecification;
  }

  /**
   * 获得临时/随机数的工厂
   * @return
   */
  @Override
  public SynchronizedValueFactory<Long> getNonceFactory() {

    return nonceFactory;
  }

  /**
   * 远程初始化
   * @throws IOException
   * @throws ExchangeException
   */
  @Override
  public void remoteInit() throws IOException, ExchangeException {

    try {
      BitfinexMarketDataServiceRaw dataService =
          (BitfinexMarketDataServiceRaw) this.marketDataService;
      List<CurrencyPair> currencyPairs = dataService.getExchangeSymbols();
      exchangeMetaData = BitfinexAdapters.adaptMetaData(currencyPairs, exchangeMetaData);

      // Get the last-price of each pair. It is needed to infer XChange's priceScale out of Bitfinex's pricePercision
      // 获取每对的最后价格。 需要从 Bitfinex 的 pricePercision 中推断出 XChange 的 priceScale

      Map<CurrencyPair, BigDecimal> lastPrices =
          Arrays.stream(dataService.getBitfinexTickers(null))
              .map(BitfinexAdapters::adaptTicker)
              .collect(Collectors.toMap(t -> t.getCurrencyPair(), t -> t.getLast()));

      final List<BitfinexSymbolDetail> symbolDetails = dataService.getSymbolDetails();
      exchangeMetaData =
          BitfinexAdapters.adaptMetaData(exchangeMetaData, symbolDetails, lastPrices);

      if (exchangeSpecification.getApiKey() != null
          && exchangeSpecification.getSecretKey() != null) {
        // Bitfinex does not provide any specific wallet health info So instead of wallet status, fetch platform status to get wallet health
        // Bitfinex 不提供任何特定的钱包健康信息 所以不是钱包状态，而是获取平台状态来获取钱包健康
        Integer bitfinexPlatformStatusData = dataService.getBitfinexPlatformStatus()[0];
        boolean bitfinexPlatformStatusPresent = bitfinexPlatformStatusData != null;
        int bitfinexPlatformStatus = bitfinexPlatformStatusPresent ? bitfinexPlatformStatusData : 0;
        // Additional remoteInit configuration for authenticated instances
        // 已验证实例的附加 remoteInit 配置
        BitfinexAccountService accountService = (BitfinexAccountService) this.accountService;
        final BitfinexAccountFeesResponse accountFees = accountService.getAccountFees();
        exchangeMetaData =
            BitfinexAdapters.adaptMetaData(
                accountFees,
                bitfinexPlatformStatus,
                bitfinexPlatformStatusPresent,
                exchangeMetaData);

        BitfinexTradeService tradeService = (BitfinexTradeService) this.tradeService;
        final BitfinexAccountInfosResponse[] bitfinexAccountInfos =
            tradeService.getBitfinexAccountInfos();

        exchangeMetaData = BitfinexAdapters.adaptMetaData(bitfinexAccountInfos, exchangeMetaData);
      }
    } catch (BitfinexException e) {
      throw BitfinexErrorAdapter.adapt(e);
    }
  }
}
