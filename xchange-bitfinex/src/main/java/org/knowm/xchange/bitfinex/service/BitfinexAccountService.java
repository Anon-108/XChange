package org.knowm.xchange.bitfinex.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.knowm.xchange.bitfinex.BitfinexErrorAdapter;
import org.knowm.xchange.bitfinex.BitfinexExchange;
import org.knowm.xchange.bitfinex.dto.BitfinexException;
import org.knowm.xchange.bitfinex.v1.BitfinexUtils;
import org.knowm.xchange.bitfinex.v1.dto.account.BitfinexDepositAddressResponse;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.dto.account.Fee;
import org.knowm.xchange.dto.account.FundingRecord;
import org.knowm.xchange.service.account.AccountService;
import org.knowm.xchange.service.trade.params.DefaultTradeHistoryParamsTimeSpan;
import org.knowm.xchange.service.trade.params.DefaultWithdrawFundsParams;
import org.knowm.xchange.service.trade.params.MoneroWithdrawFundsParams;
import org.knowm.xchange.service.trade.params.RippleWithdrawFundsParams;
import org.knowm.xchange.service.trade.params.TradeHistoryParamCurrency;
import org.knowm.xchange.service.trade.params.TradeHistoryParamCurrencyPair;
import org.knowm.xchange.service.trade.params.TradeHistoryParamLimit;
import org.knowm.xchange.service.trade.params.TradeHistoryParams;
import org.knowm.xchange.service.trade.params.TradeHistoryParamsTimeSpan;
import org.knowm.xchange.service.trade.params.WithdrawFundsParams;
import org.knowm.xchange.utils.DateUtils;

/**
 * Bitfinex 账户服务
 */
public class BitfinexAccountService extends BitfinexAccountServiceRaw implements AccountService {

  /**
   * Constructor 构造器
   * Bitfinex 账户服务
   * @param exchange
   */
  public BitfinexAccountService(
      BitfinexExchange exchange, ResilienceRegistries resilienceRegistries) {

    super(exchange, resilienceRegistries);
  }

  /**
   * 获取账户信息
   * @return
   * @throws IOException
   */
  @Override
  public AccountInfo getAccountInfo() throws IOException {

    return new AccountInfo(BitfinexAdapters.adaptWallets(getBitfinexAccountInfo()));
  }

  /**
   * 提款
   * Withdrawal support
   * 提款支持
   *
   * @param currency
   *    货币
   * @param amount
   *      数量
   * @param address
   *      地址
   * @return
   * @throws IOException
   */
  @Override
  public String withdrawFunds(Currency currency, BigDecimal amount, String address)
      throws IOException {
    try {
      // determine withdrawal type
      // 判断提现类型
      String type = BitfinexUtils.convertToBitfinexWithdrawalType(currency.toString());
      // Bitfinex withdeawal can be from different type of wallets    *  we have to use one of these for now: Exchange -  to be able to withdraw instantly after trading for example  The wallet to withdraw from, can be “trading”, “exchange”, or “deposit”.
      // Bitfinex 提款可以来自不同类型的钱包 * 我们现在必须使用其中一种： 交易所 - 例如，能够在交易后立即提款 要提款的钱包，可以是“交易”、“交易所”、 或“存款”。
      String walletSelected = "exchange";
      // We have to convert XChange currencies to Bitfinex currencies: can be “bitcoin”, “litecoin”  or  “ether” or “tether” or “wire”.
      // 我们必须将 XChange 货币转换为 Bitfinex 货币：可以是“bitcoin”、“litecoin”或“ether”或“tether”或“wire”。
      return withdraw(type, walletSelected, amount, address);
    } catch (BitfinexException e) {
      throw BitfinexErrorAdapter.adapt(e);
    }
  }

  /**
   * Used for XRP withdrawals
   * * 用于 XRP 提款
   *
   * @param currency
   *      货币
   * @param amount
   *      数量
   * @param address
   *      地址
   * @param tagOrPaymentId
   *      标签或付款 ID
   *
   * @return
   * @throws IOException
   */
  public String withdrawFunds(
      Currency currency, BigDecimal amount, String address, String tagOrPaymentId)
      throws IOException {
    try {
      // determine withdrawal type
      // 判断提现类型
      String type = BitfinexUtils.convertToBitfinexWithdrawalType(currency.toString());
      // Bitfinex withdeawal can be from different type of wallets    *   we have to use one of these for now: Exchange -  to be able to withdraw instantly after trading for example The wallet to withdraw from, can be “trading”, “exchange”, or “deposit”.
      // Bitfinex 提款可以来自不同类型的钱包 * 我们现在必须使用其中一种： 交易所 - 例如，能够在交易后立即提款 要提款的钱包，可以是“交易”、“交易所”、 或“存款”。
      String walletSelected = "exchange";
      // We have to convert XChange currencies to Bitfinex currencies: can be “bitcoin”, “litecoin”  or  “ether” or “tether” or “wire”.
      // 我们必须将 XChange 货币转换为 Bitfinex 货币：可以是“bitcoin”、“litecoin”或“ether”或“tether”或“wire”。
      return withdraw(type, walletSelected, amount, address, tagOrPaymentId);
    } catch (BitfinexException e) {
      throw BitfinexErrorAdapter.adapt(e);
    }
  }

  /**
   * 提款
   * @param params The withdrawl details
   *               提款详情
   *
   * @return
   * @throws IOException
   */
  @Override
  public String withdrawFunds(WithdrawFundsParams params) throws IOException {
    try {
      if (params instanceof RippleWithdrawFundsParams) {
        RippleWithdrawFundsParams xrpParams = (RippleWithdrawFundsParams) params;
        return withdrawFunds(
            xrpParams.getCurrency(),
            xrpParams.getAmount(),
            xrpParams.getAddress(),
            xrpParams.getTag());
      } else if (params instanceof MoneroWithdrawFundsParams) {
        MoneroWithdrawFundsParams xmrParams = (MoneroWithdrawFundsParams) params;
        return withdrawFunds(
            xmrParams.getCurrency(),
            xmrParams.getAmount(),
            xmrParams.getAddress(),
            xmrParams.getPaymentId());
      } else if (params instanceof DefaultWithdrawFundsParams) {
        DefaultWithdrawFundsParams defaultParams = (DefaultWithdrawFundsParams) params;
        return withdrawFunds(
            defaultParams.getCurrency(), defaultParams.getAmount(), defaultParams.getAddress());
      }

      throw new IllegalStateException("Don't know how to withdraw 不知道怎么退: " + params);
    } catch (BitfinexException e) {
      throw BitfinexErrorAdapter.adapt(e);
    }
  }

  /**
   * 请求存款地址
   * @param currency The digital currency that corresponds to the desired deposit address.
   *                 所需存款地址对应的数字货币。
   *
   * @param arguments
   * @return
   * @throws IOException
   */
  @Override
  public String requestDepositAddress(Currency currency, String... arguments) throws IOException {
    try {
      final BitfinexDepositAddressResponse response =
          super.requestDepositAddressRaw(currency.getCurrencyCode());
      return response.getAddress();
    } catch (BitfinexException e) {
      throw BitfinexErrorAdapter.adapt(e);
    }
  }

  /**
   * 创建资金历史参数
   * @return
   */
  @Override
  public TradeHistoryParams createFundingHistoryParams() {
    return new BitfinexFundingHistoryParams(null, null, null, null);
  }

  /**
   * 获取资金历史
   * @param params
   * @return
   * @throws IOException
   */
  @Override
  public List<FundingRecord> getFundingHistory(TradeHistoryParams params) throws IOException {
    try {
      String currency = null;
      Long startTime = null;
      Long endTime = null;
      Integer limit = null;

      if (params instanceof TradeHistoryParamCurrencyPair
          && ((TradeHistoryParamCurrencyPair) params).getCurrencyPair() != null) {
        currency =
            BitfinexAdapters.adaptCurrencyPair(
                ((TradeHistoryParamCurrencyPair) params).getCurrencyPair());
      }

      if (params instanceof TradeHistoryParamsTimeSpan) {
        TradeHistoryParamsTimeSpan paramsTimeSpan = (TradeHistoryParamsTimeSpan) params;
        startTime = DateUtils.toMillisNullSafe(paramsTimeSpan.getStartTime());
        endTime = DateUtils.toMillisNullSafe(paramsTimeSpan.getEndTime());
      }

      if (params instanceof TradeHistoryParamLimit) {
        TradeHistoryParamLimit tradeHistoryParamLimit = (TradeHistoryParamLimit) params;
        if (tradeHistoryParamLimit.getLimit() != null) {
          limit = Integer.valueOf(tradeHistoryParamLimit.getLimit());
        }
      }

      return BitfinexAdapters.adaptFundingHistory(
          getMovementHistory(currency, startTime, endTime, limit));
    } catch (BitfinexException e) {
      throw BitfinexErrorAdapter.adapt(e);
    }
  }

  /**
   * 获取动态交易费用
   * @return
   * @throws IOException
   */
  @Override
  public Map<CurrencyPair, Fee> getDynamicTradingFees() throws IOException {
    try {
      List<CurrencyPair> allCurrencyPairs = exchange.getExchangeSymbols();
      return BitfinexAdapters.adaptDynamicTradingFees(
          getBitfinexDynamicTradingFees(), allCurrencyPairs);
    } catch (BitfinexException e) {
      throw BitfinexErrorAdapter.adapt(e);
    }
  }

  /**
   * BitfinexFundingHistoryParams
   */
  public static class BitfinexFundingHistoryParams extends DefaultTradeHistoryParamsTimeSpan
      implements TradeHistoryParamCurrency, TradeHistoryParamLimit {

    private Integer limit;
    private Currency currency;

    public BitfinexFundingHistoryParams(
        final Date startTime, final Date endTime, final Integer limit, final Currency currency) {

      super(startTime, endTime);

      this.limit = limit;
      this.currency = currency;
    }

    /**
     * 获取货币
     * @return
     */
    @Override
    public Currency getCurrency() {
      return this.currency;
    }

    /**
     * 设置货币
     * @param currency
     */
    @Override
    public void setCurrency(Currency currency) {
      this.currency = currency;
    }

    /**
     * 获取额度/限制
     * @return
     */
    @Override
    public Integer getLimit() {
      return this.limit;
    }

    /**
     * 设置额度/限制
     * @param limit
     */
    @Override
    public void setLimit(Integer limit) {
      this.limit = limit;
    }
  }
}
