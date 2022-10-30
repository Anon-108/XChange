package org.knowm.xchange.binance;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Before;
import org.junit.Rule;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;

/**
 * 抽象弹性测试
 */
public class AbstractResilienceTest {

  @Rule public WireMockRule wireMockRule = new WireMockRule();

  public static int READ_TIMEOUT_MS = 1000;

  /**
   * 恢复弹性注册表
   */
  @Before
  public void resertResilienceRegistries() {
    BinanceExchange.resetResilienceRegistries();
  }

  /**
   * 创建启用重试的 Exchange
   * @return
   */
  protected BinanceExchange createExchangeWithRetryEnabled() {
    return createExchange(true, false);
  }

  /**
   * 创建禁用重试的 Exchange
   * @return
   */
  protected BinanceExchange createExchangeWithRetryDisabled() {
    return createExchange(false, false);
  }

  /**
   * 创建启用了速率限制器的 Exchange
   * @return
   */
  protected BinanceExchange createExchangeWithRateLimiterEnabled() {
    return createExchange(false, true);
  }

  protected BinanceExchange createExchange(boolean retryEnabled, boolean rateLimiterEnabled) {
    BinanceExchange exchange =
        (BinanceExchange)
            ExchangeFactory.INSTANCE.createExchangeWithoutSpecification(BinanceExchange.class);
    ExchangeSpecification specification = exchange.getDefaultExchangeSpecification();
    specification.setHost("localhost");
    specification.setSslUri("http://localhost:" + wireMockRule.port() + "/");
    specification.setPort(wireMockRule.port());
    specification.setShouldLoadRemoteMetaData(false);
    specification.setHttpReadTimeout(READ_TIMEOUT_MS);
    specification.getResilience().setRetryEnabled(retryEnabled);
    specification.getResilience().setRateLimiterEnabled(rateLimiterEnabled);
    exchange.applySpecification(specification);
    return exchange;
  }
}
