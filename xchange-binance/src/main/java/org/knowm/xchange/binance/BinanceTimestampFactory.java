package org.knowm.xchange.binance;

import static org.knowm.xchange.binance.BinanceResilience.REQUEST_WEIGHT_RATE_LIMITER;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.binance.dto.meta.BinanceTime;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.client.ResilienceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import si.mazi.rescu.SynchronizedValueFactory;

/**
 * 币安时间戳工厂
 */
public class BinanceTimestampFactory implements SynchronizedValueFactory<Long> {

  private static final Logger LOG = LoggerFactory.getLogger(BinanceTimestampFactory.class);

  private final Binance binance;
  /**
   * 恢复规范
   */
  private final ExchangeSpecification.ResilienceSpecification resilienceSpecification;
  /**
   * 恢复注册表
   */
  private final ResilienceRegistries resilienceRegistries;

  /**
   * delta 服务器时间过期
   */
  private Long deltaServerTimeExpire;
  /**
   * delta服务器时间
   */
  private Long deltaServerTime;

  /**
   * 币安时间戳工厂
   * @param binance
   * @param resilienceSpecification 恢复规范
   * @param resilienceRegistries 恢复注册
   */
  public BinanceTimestampFactory(
      Binance binance,
      ExchangeSpecification.ResilienceSpecification resilienceSpecification,
      ResilienceRegistries resilienceRegistries) {
    this.binance = binance;
    this.resilienceSpecification = resilienceSpecification;
    this.resilienceRegistries = resilienceRegistries;
  }

  @Override
  public Long createValue() {

    return System.currentTimeMillis();
  }

  public void clearDeltaServerTime() {
    deltaServerTime = null;
  }

  public long deltaServerTime() throws IOException {

    if (deltaServerTime == null || deltaServerTimeExpire <= System.currentTimeMillis()) {

      // Do a little warm up
      Date serverTime = new Date(binanceTime().getServerTime().getTime());

      // Assume that we are closer to the server time when we get the repose
      Date systemTime = new Date(System.currentTimeMillis());

      // Expire every 10min
      deltaServerTimeExpire = systemTime.getTime() + TimeUnit.MINUTES.toMillis(10);
      deltaServerTime = serverTime.getTime() - systemTime.getTime();

      SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
      LOG.trace(
          "deltaServerTime: {} - {} => {}",
          df.format(serverTime),
          df.format(systemTime),
          deltaServerTime);
    }

    return deltaServerTime;
  }

  private BinanceTime binanceTime() throws IOException {
    return ResilienceUtils.decorateApiCall(resilienceSpecification, () -> binance.time())
        .withRetry(resilienceRegistries.retries().retry("time"))
        .withRateLimiter(
            resilienceRegistries.rateLimiters().rateLimiter(REQUEST_WEIGHT_RATE_LIMITER))
        .call();
  }
}
