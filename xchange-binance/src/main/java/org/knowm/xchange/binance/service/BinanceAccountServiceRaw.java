package org.knowm.xchange.binance.service;

import static org.knowm.xchange.binance.BinanceResilience.REQUEST_WEIGHT_RATE_LIMITER;
import static org.knowm.xchange.client.ResilienceRegistries.NON_IDEMPOTENT_CALLS_RETRY_CONFIG_NAME;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.knowm.xchange.binance.BinanceAdapters;
import org.knowm.xchange.binance.BinanceAuthenticated;
import org.knowm.xchange.binance.BinanceExchange;
import org.knowm.xchange.binance.dto.BinanceException;
import org.knowm.xchange.binance.dto.account.AssetDetail;
import org.knowm.xchange.binance.dto.account.AssetDividendResponse;
import org.knowm.xchange.binance.dto.account.BinanceAccountInformation;
import org.knowm.xchange.binance.dto.account.BinanceDeposit;
import org.knowm.xchange.binance.dto.account.BinanceWithdraw;
import org.knowm.xchange.binance.dto.account.DepositAddress;
import org.knowm.xchange.binance.dto.account.TransferHistory;
import org.knowm.xchange.binance.dto.account.TransferSubUserHistory;
import org.knowm.xchange.binance.dto.account.WithdrawResponse;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.currency.Currency;

/**
 * 币安账户服务原始
 */
public class BinanceAccountServiceRaw extends BinanceBaseService {
  /**
   * 币安账户服务原始
   * @param exchange 交换
   * @param binance 币安
   * @param resilienceRegistries 回弹/恢复力  支持私有/登记/注册
   */
  public BinanceAccountServiceRaw(
      BinanceExchange exchange,
      BinanceAuthenticated binance,
      ResilienceRegistries resilienceRegistries) {
    super(exchange, binance, resilienceRegistries);
  }

  /**
   * 帐户
   * @return
   * @throws BinanceException
   * @throws IOException
   */
  public BinanceAccountInformation account() throws BinanceException, IOException {
    return decorateApiCall(
            () -> binance.account(getRecvWindow(), getTimestampFactory(), apiKey, signatureCreator))
        .withRetry(retry("account"))
        .withRateLimiter(rateLimiter(REQUEST_WEIGHT_RATE_LIMITER), 5)
        .call();
  }

  /**
   * 提，取
   * @param coin 币
   * @param address 地址
   * @param amount 数量
   * @return
   * @throws IOException
   * @throws BinanceException
   */
  public WithdrawResponse withdraw(String coin, String address, BigDecimal amount)
      throws IOException, BinanceException {
    // the name parameter seams to be mandatory
    String name = address.length() <= 10 ? address : address.substring(0, 10);
    return withdraw(coin, address, null, amount, name);
  }

  /**
   * 提，取
   * @param coin 币
   * @param address 地址
   * @param addressTag 地址标签
   * @param amount 数量
   * @return
   * @throws IOException
   * @throws BinanceException
   */
  public WithdrawResponse withdraw(
      String coin, String address, String addressTag, BigDecimal amount)
      throws IOException, BinanceException {
    // the name parameter seams to be mandatory
    String name = address.length() <= 10 ? address : address.substring(0, 10);
    return withdraw(coin, address, addressTag, amount, name);
  }

  /**
   * 提，取
   * @param coin 币
   * @param address 地址
   * @param addressTag 地址标签
   * @param amount 数量
   * @param name 名称
   * @return
   * @throws IOException
   * @throws BinanceException
   */
  private WithdrawResponse withdraw(
      String coin, String address, String addressTag, BigDecimal amount, String name)
      throws IOException, BinanceException {
    return decorateApiCall(
            () ->
                binance.withdraw(
                    coin,
                    address,
                    addressTag,
                    amount,
                    name,
                    getRecvWindow(),
                    getTimestampFactory(),
                    apiKey,
                    signatureCreator))
        .withRetry(retry("withdraw", NON_IDEMPOTENT_CALLS_RETRY_CONFIG_NAME))
        .withRateLimiter(rateLimiter(REQUEST_WEIGHT_RATE_LIMITER), 5)
        .call();
  }

  /**
   * 请求存款地址
   * @param currency 通货，货币；通用，
   * @return
   * @throws IOException
   */
  public DepositAddress requestDepositAddress(Currency currency) throws IOException {
    return decorateApiCall(
            () ->
                binance.depositAddress(
                    BinanceAdapters.toSymbol(currency),
                    getRecvWindow(),
                    getTimestampFactory(),
                    apiKey,
                    signatureCreator))
        .withRetry(retry("depositAddress"))
        .withRateLimiter(rateLimiter(REQUEST_WEIGHT_RATE_LIMITER))
        .call();
  }

  /**
   *请求资产详细信息
   * @return
   * @throws IOException
   */
  public Map<String, AssetDetail> requestAssetDetail() throws IOException {
    return decorateApiCall(
            () ->
                binance.assetDetail(
                    getRecvWindow(), getTimestampFactory(), apiKey, signatureCreator))
        .withRetry(retry("assetDetail"))
        .withRateLimiter(rateLimiter(REQUEST_WEIGHT_RATE_LIMITER))
        .call();
  }

  /**
   * depositHistory
   * @param asset 资产
   * @param startTime 开始时间
   * @param endTime 结束时间
   * @return
   * @throws BinanceException
   * @throws IOException
   */
  public List<BinanceDeposit> depositHistory(String asset, Long startTime, Long endTime)
      throws BinanceException, IOException {
    return decorateApiCall(
            () ->
                binance.depositHistory(
                    asset,
                    startTime,
                    endTime,
                    getRecvWindow(),
                    getTimestampFactory(),
                    apiKey,
                    signatureCreator))
        .withRetry(retry("depositHistory"))
        .withRateLimiter(rateLimiter(REQUEST_WEIGHT_RATE_LIMITER))
        .call();
  }

  /**
   * 提取历史
   * @param asset 资产
   * @param startTime 开始时间
   * @param endTime 结束时间
   * @return
   * @throws BinanceException
   * @throws IOException
   */
  public List<BinanceWithdraw> withdrawHistory(String asset, Long startTime, Long endTime)
      throws BinanceException, IOException {
    return decorateApiCall(
            () ->
                binance.withdrawHistory(
                    asset,
                    startTime,
                    endTime,
                    getRecvWindow(),
                    getTimestampFactory(),
                    apiKey,
                    signatureCreator))
        .withRetry(retry("withdrawHistory"))
        .withRateLimiter(rateLimiter(REQUEST_WEIGHT_RATE_LIMITER))
        .call();
  }

  /**
   * 获取资产红利
   * @param startTime 开始时间
   * @param endTime 结束时间
   * @return
   * @throws BinanceException
   * @throws IOException
   */
  public List<AssetDividendResponse.AssetDividend> getAssetDividend(Long startTime, Long endTime)
      throws BinanceException, IOException {
    return getAssetDividend("", startTime, endTime);
  }

  /**
   * getAssetDividend
   * @param asset 资产
   * @param startTime 开始时间
   * @param endTime 结束时间
   * @return
   * @throws BinanceException
   * @throws IOException
   */
  public List<AssetDividendResponse.AssetDividend> getAssetDividend(
      String asset, Long startTime, Long endTime) throws BinanceException, IOException {
    return decorateApiCall(
            () ->
                binance.assetDividend(
                    asset,
                    startTime,
                    endTime,
                    getRecvWindow(),
                    getTimestampFactory(),
                    super.apiKey,
                    super.signatureCreator))
        .withRetry(retry("assetDividend"))
        .withRateLimiter(rateLimiter(REQUEST_WEIGHT_RATE_LIMITER))
        .call()
        .getData();
  }

  /**
   * 获取转账记录
   * @param fromEmail 发件人邮箱地址
   * @param startTime 开始时间
   * @param endTime 结束时间
   * @param page 页
   * @param limit 限制/额度
   * @return
   * @throws BinanceException
   * @throws IOException
   */
  public List<TransferHistory> getTransferHistory(
      String fromEmail, Long startTime, Long endTime, Integer page, Integer limit)
      throws BinanceException, IOException {
    return decorateApiCall(
            () ->
                binance.transferHistory(
                    fromEmail,
                    startTime,
                    endTime,
                    page,
                    limit,
                    getRecvWindow(),
                    getTimestampFactory(),
                    super.apiKey,
                    super.signatureCreator))
        .withRetry(retry("transferHistory"))
        .withRateLimiter(rateLimiter(REQUEST_WEIGHT_RATE_LIMITER))
        .call();
  }

  /**
   * 获取子用户历史
   * @param asset 资产
   * @param type 类型
   * @param startTime 开始时间
   * @param endTime 结束时间
   * @param limit 限制/额度
   * @return
   * @throws BinanceException
   * @throws IOException
   */
  public List<TransferSubUserHistory> getSubUserHistory(
      String asset, Integer type, Long startTime, Long endTime, Integer limit)
      throws BinanceException, IOException {
    return decorateApiCall(
            () ->
                binance.transferSubUserHistory(
                    asset,
                    type,
                    startTime,
                    endTime,
                    limit,
                    getRecvWindow(),
                    getTimestampFactory(),
                    super.apiKey,
                    super.signatureCreator))
        .withRetry(retry("transferSubUserHistory"))
        .withRateLimiter(rateLimiter(REQUEST_WEIGHT_RATE_LIMITER))
        .call();
  }
}
