package org.knowm.xchange.binance.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import org.knowm.xchange.binance.BinanceAuthenticated;
import org.knowm.xchange.binance.BinanceExchange;
import org.knowm.xchange.binance.dto.account.AssetDetail;
import org.knowm.xchange.binance.dto.meta.BinanceSystemStatus;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.dto.account.AddressWithTag;
import org.knowm.xchange.dto.account.Fee;
import org.knowm.xchange.dto.account.FundingRecord;
import org.knowm.xchange.service.trade.params.TradeHistoryParams;
import org.knowm.xchange.service.trade.params.WithdrawFundsParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 币安美国账户服务
 */
public class BinanceUsAccountService extends BinanceAccountService {

  private final String NOT_SUPPORTED = "Not Supported by Binance.US Binance.US 不支持";
  protected final Logger LOG = LoggerFactory.getLogger(getClass());

  /**
   * 币安美国账户服务
   * @param exchange 交换
   * @param binance 币安
   * @param resilienceRegistries 恢复注册
   */
  public BinanceUsAccountService(
      BinanceExchange exchange,
      BinanceAuthenticated binance,
      ResilienceRegistries resilienceRegistries) {
    super(exchange, binance, resilienceRegistries);
  }

  @Override
  public AccountInfo getAccountInfo() throws IOException {
    return super.getAccountInfo();
  }

  @Override
  public Map<CurrencyPair, Fee> getDynamicTradingFees() throws IOException {
    return super.getDynamicTradingFees();
  }

  @Override
  public String withdrawFunds(Currency currency, BigDecimal amount, String address)
      throws IOException {
    return NOT_SUPPORTED;
  }

  @Override
  public String withdrawFunds(Currency currency, BigDecimal amount, AddressWithTag address)
      throws IOException {
    return NOT_SUPPORTED;
  }

  @Override
  public String withdrawFunds(WithdrawFundsParams params) throws IOException {
    return NOT_SUPPORTED;
  }

  @Override
  public String requestDepositAddress(Currency currency, String... args) throws IOException {
    return NOT_SUPPORTED;
  }

  @Override
  public AddressWithTag requestDepositAddressData(Currency currency, String... args)
      throws IOException {
    return new AddressWithTag(NOT_SUPPORTED, NOT_SUPPORTED);
  }

  @Override
  public Map<String, AssetDetail> getAssetDetails() throws IOException {
    LOG.warn("getAssetDetails: {}", NOT_SUPPORTED);
    return new HashMap<>();
  }

  @Override
  public TradeHistoryParams createFundingHistoryParams() {
    return super.createFundingHistoryParams();
  }

  @Override
  public List<FundingRecord> getFundingHistory(TradeHistoryParams params) throws IOException {
    LOG.warn("getFundingHistory: {}", NOT_SUPPORTED);
    return new ArrayList<>();
  }

  @Override
  public BinanceSystemStatus getSystemStatus() throws IOException {
    LOG.warn("getSystemStatus: {}", NOT_SUPPORTED);
    return new BinanceSystemStatus();
  }
}
