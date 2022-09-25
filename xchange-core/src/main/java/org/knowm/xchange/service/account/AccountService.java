package org.knowm.xchange.service.account;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.dto.account.AddressWithTag;
import org.knowm.xchange.dto.account.Fee;
import org.knowm.xchange.dto.account.FundingRecord;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.exceptions.NotAvailableFromExchangeException;
import org.knowm.xchange.exceptions.NotYetImplementedForExchangeException;
import org.knowm.xchange.instrument.Instrument;
import org.knowm.xchange.service.BaseService;
import org.knowm.xchange.service.trade.params.DefaultWithdrawFundsParams;
import org.knowm.xchange.service.trade.params.TradeHistoryParams;
import org.knowm.xchange.service.trade.params.WithdrawFundsParams;

/**
 * Interface to provide the following to {@link Exchange}:
 * 向 {@link Exchange} 提供以下内容的接口：
 *
 * <ul>
 *   <li>Standard methods available to explore send/receive account-related data
 *   <li>可用于探索发送/接收帐户相关数据的标准方法
 * </ul>
 *
 * <p>The implementation of this service is expected to be based on a client polling mechanism of some kind
 * <p>此服务的实现预计将基于某种客户端轮询机制
 */
public interface AccountService extends BaseService {

  /**
   * Get account info
   * 获取账户信息
   *
   * @return the AccountInfo object, null if some sort of error occurred. Implementers should log  the error.
   * AccountInfo 对象，如果发生某种错误，则为 null。 实施者应该记录错误。
   *
   * @throws ExchangeException - Indication that the exchange reported some kind of error with the request or response
   *                          - 表示交易所报告了请求或响应的某种错误
   *
   * @throws NotAvailableFromExchangeException - Indication that the exchange does not support the  requested function or data
   *                                          - 指示交易所不支持请求的功能或数据
   *
   * @throws NotYetImplementedForExchangeException - Indication that the exchange supports the   requested function or data, but it has not yet been implemented
   *                                                 - 表示交易所支持请求的功能或数据，但尚未实现
   * @throws IOException - Indication that a networking error occurred while fetching JSON data
   *                    - 指示在获取 JSON 数据时发生网络错误
   */
  default AccountInfo getAccountInfo() throws IOException {
    throw new NotYetImplementedForExchangeException("getAccountInfo 获取账户信息");
  }

  /**
   * Convenience method, typically just delegates to withdrawFunds(WithdrawFundsParams params)
   ** 方便的方法，通常只是委托withdrawFunds(WithdrawFundsParams params)
   *
   * @param currency The currency to withdraw
   *                 提现的货币
   *
   * @param amount The amount to withdraw
   *               提款金额
   *
   * @param address The destination address
   *                目的地址
   *
   * @return The result of the withdrawal (usually a transaction ID)
   *        提款的结果（通常是交易ID）
   *
   * @throws ExchangeException - Indication that the exchange reported some kind of error with the  request or response
   *                          - 表示交易所报告了请求或响应的某种错误
   *
   * @throws NotAvailableFromExchangeException - Indication that the exchange does not support the requested function or data
   *                                - 指示交易所不支持请求的功能或数据
   *
   * @throws NotYetImplementedForExchangeException - Indication that the exchange supports the  requested function or data, but it has not yet been implemented
   *                          - 表示交易所支持请求的功能或数据，但尚未实现
   *
   * @throws IOException - Indication that a networking error occurred while fetching JSON data
   *                    - 指示在获取 JSON 数据时发生网络错误
   */
  default String withdrawFunds(Currency currency, BigDecimal amount, String address)
      throws IOException {
    return withdrawFunds(new DefaultWithdrawFundsParams(address, currency, amount));
  }

  /**
   * Convenience method, typically just delegates to withdrawFunds(WithdrawFundsParams params)
   * 方便的方法，通常只是委托withdrawFunds(WithdrawFundsParams params)
   *
   * @param currency The currency to withdraw
   *                 提现的货币
   *
   * @param amount The amount to withdraw
   *               提款金额
   *
   * @param address The destination address
   *                目的地址
   *
   * @return The result of the withdrawal (usually a transaction ID)
   *          提款的结果（通常是交易ID）
   *
   * @throws ExchangeException - Indication that the exchange reported some kind of error with the  request or response
   *                        - 表示交易所报告了请求或响应的某种错误
   *
   * @throws NotAvailableFromExchangeException - Indication that the exchange does not support the   requested function or data
   *                                  - 指示交易所不支持请求的功能或数据
   *
   * @throws NotYetImplementedForExchangeException - Indication that the exchange supports the  requested function or data, but it has not yet been implemented
   *                                  - 表示交易所支持请求的功能或数据，但尚未实现
   *
   * @throws IOException - Indication that a networking error occurred while fetching JSON data
   *                    - 指示在获取 JSON 数据时发生网络错误
   */
  default String withdrawFunds(Currency currency, BigDecimal amount, AddressWithTag address)
      throws IOException {
    return withdrawFunds(new DefaultWithdrawFundsParams(address, currency, amount));
  }

  /**
   * Withdraw funds from this account. Allows to withdraw digital currency funds from the exchange account to an external address
   * 从该账户中提取资金。 允许将数字货币资金从交易所账户提取到外部地址
   *
   * @param params The withdrawl details
   *               提款详情
   *
   * @return The result of the withdrawal (usually a transaction ID)
   *          提款的结果（通常是交易ID）
   *
   * @throws ExchangeException - Indication that the exchange reported some kind of error with the  request or response
   *                          - 表示交易所报告了请求或响应的某种错误
   *
   * @throws NotAvailableFromExchangeException - Indication that the exchange does not support the  requested function or data
   *                                - 指示交易所不支持请求的功能或数据
   *
   * @throws NotYetImplementedForExchangeException - Indication that the exchange supports the  requested function or data, but it has not yet been implemented
   * - 表示交易所支持请求的功能或数据，但尚未实现
   *
   * @throws IOException - Indication that a networking error occurred while fetching JSON data
   * - 指示在获取 JSON 数据时发生网络错误
   */
  default String withdrawFunds(WithdrawFundsParams params) throws IOException {
    throw new NotYetImplementedForExchangeException("withdrawFunds 提款");
  }

  /**
   * Request a digital currency address to fund this account. Allows to fund the exchange account with digital currency from an external address
   * * 请求一个数字货币地址来为这个账户注资。 允许从外部地址用数字货币为交易所账户注资
   *
   * @param currency The digital currency that corresponds to the desired deposit address.
   *                 所需存款地址对应的数字货币。
   *
   * @param args Necessary argument(s) as a {@code String}
   *             {@code String} 形式的必要参数
   *
   * @return the internal deposit address to send funds to
   * * @return 内部存款地址发送资金
   *
   * @throws ExchangeException - Indication that the exchange reported some kind of error with the request or response
   * - 表示交易所报告了请求或响应的某种错误
   *
   * @throws NotAvailableFromExchangeException - Indication that the exchange does not support the requested function or data
   *      - 指示交易所不支持请求的功能或数据
   *
   * @throws NotYetImplementedForExchangeException - Indication that the exchange supports the  requested function or data, but it has not yet been implemented
   *    - 表示交易所支持请求的功能或数据，但尚未实现
   *
   * @throws IOException - Indication that a networking error occurred while fetching JSON data
   *      - 指示在获取 JSON 数据时发生网络错误
   */
  default String requestDepositAddress(Currency currency, String... args) throws IOException {
    throw new NotYetImplementedForExchangeException("requestDepositAddress 请求存款地址");
  }

  /**
   * Request a digital currency address to fund this account. Allows to fund the exchange account with digital currency from an external address
   * 请求一个数字货币地址来为这个账户注资。 允许从外部地址用数字货币为交易所账户注资
   *
   * @param currency The digital currency that corresponds to the desired deposit address.
   *                 * @param currency 所需充值地址对应的数字货币。
   *
   * @return the internal deposit address to send funds to
   * @return 内部存款地址发送资金
   *
   * @throws ExchangeException - Indication that the exchange reported some kind of error with the request or response
   * * @throws ExchangeException - 表示交换报告了请求或响应的某种错误
   *
   * @throws NotAvailableFromExchangeException - Indication that the exchange does not support the requested function or data
   * * @throws NotAvailableFromExchangeException - 表示交易所不支持请求的功能或数据
   *
   * @throws NotYetImplementedForExchangeException - Indication that the exchange supports the  requested function or data, but it has not yet been implemented
   * * @throws NotYetImplementedForExchangeException - 表示交易所支持请求的功能或数据，但尚未实现
   *
   * @throws IOException - Indication that a networking error occurred while fetching JSON data
   * * @throws IOException - 指示在获取 JSON 数据时发生网络错误
   */
  default AddressWithTag requestDepositAddressData(Currency currency, String... args)
      throws IOException {
    throw new NotYetImplementedForExchangeException("requestDepositAddressData 请求存款地址数据");
  }

  /**
   * Create {@link TradeHistoryParams} object specific to this exchange. Object created by this
    method may be used to discover supported and required {@link #getFundingHistory(TradeHistoryParams)} parameters and should be passed only to the method in
    the same class as the createFundingHistoryParams that created the object.
   创建特定于此交易所的 {@link TradeHistoryParams} 对象。 由此创建的对象
   方法可用于发现支持和必需的 {@link #getFundingHistory(TradeHistoryParams)} 参数，并且应仅传递给
   与创建对象的 createFundingHistoryParams 相同的类。
   */
  default TradeHistoryParams createFundingHistoryParams() {
    throw new NotYetImplementedForExchangeException("createFundingHistoryParams 创建资金历史参数");
  }

  /**
   * @return list of funding history if available or an empty list otherwise. This should never  return null.
   * * @return 资金历史列表（如果可用）或空列表。 这不应该返回 null。
   *
   * @throws ExchangeException - Indication that the exchange reported some kind of error with the  request or response
   * * @throws ExchangeException - 表示交换报告了请求或响应的某种错误
   *
   * @throws NotAvailableFromExchangeException - Indication that the exchange does not support the  requested function or data
   *      - 指示交易所不支持请求的功能或数据
   *
   * @throws NotYetImplementedForExchangeException - Indication that the exchange supports the  requested function or data, but it has not yet been implemented
   *        - 表示交易所支持请求的功能或数据，但尚未实现
   *
   * @throws IOException - Indication that a networking error occurred while fetching JSON data
   *      - 指示在获取 JSON 数据时发生网络错误
   */
  default List<FundingRecord> getFundingHistory(TradeHistoryParams params) throws IOException {
    throw new NotYetImplementedForExchangeException("getFundingHistory 获取资金历史");
  }

  /**
   * Get the trading fees per instrument as determined by the given exchange's rules for adjusting
    fees by recent volume traded. Some exchanges will provide the current fees per currency via a
    single API request, while others require more logic to compute by hand.
   获取由给定交易所的调整规则确定的每种工具的交易费用
   按最近交易量计算的费用。 一些交易所将通过以下方式提供每种货币的当前费用
   单个 API 请求，而其他需要更多逻辑来手动计算。

   *
   * @return map between currency pairs and their fees at the time of invocation.
   * * @return 调用时货币对及其费用之间的映射。
   *
   * @throws ExchangeException - Indication that the exchange reported some kind of error with the  request or response
   * * @throws ExchangeException - 表示交换报告了请求或响应的某种错误
   *
   * @throws NotAvailableFromExchangeException - Indication that the exchange does not support the  requested function or data
   * * @throws NotAvailableFromExchangeException - 表示交易所不支持请求的功能或数据
   *
   * @throws NotYetImplementedForExchangeException - Indication that the exchange supports the  requested function or data, but it has not yet been implemented
   * * @throws NotYetImplementedForExchangeException - 表示交易所支持请求的功能或数据，但尚未实现
   *
   * @throws IOException - Indication that a networking error occurred while fetching JSON data
   * * @throws IOException - 指示在获取 JSON 数据时发生网络错误
   */
  default Map<Instrument, Fee> getDynamicTradingFeesByInstrument() throws IOException {
    throw new NotYetImplementedForExchangeException("getDynamicTradingFeesByInstrument  按工具获取动态交易费用");
  }

  /**
   * Get the trading fees per currency pair as determined by the given exchange's rules for
    adjusting fees by recent volume traded. Some exchanges will provide the current fees per
    currency via a single API request, while others require more logic to compute by hand.
   获取由给定交易所的规则确定的每个货币对的交易费用
   根据最近的交易量调整费用。 一些交易所将提供当前的费用
   货币通过单个 API 请求，而其他需要更多逻辑来手动计算。
   *
   * @return map between currency pairs and their fees at the time of invocation.
   * * @return 调用时货币对及其费用之间的映射。
   *
   * @throws ExchangeException - Indication that the exchange reported some kind of error with the  request or response
   * * @throws ExchangeException - 表示交换报告了请求或响应的某种错误
   *
   * @throws NotAvailableFromExchangeException - Indication that the exchange does not support the  requested function or data
   * * @throws NotAvailableFromExchangeException - 表示交易所不支持请求的功能或数据
   *
   * @throws NotYetImplementedForExchangeException - Indication that the exchange supports the  requested function or data, but it has not yet been implemented
   * * @throws NotYetImplementedForExchangeException - 表示交易所支持请求的功能或数据，但尚未实现
   *
   * @throws IOException - Indication that a networking error occurred while fetching JSON data
   * * @throws IOException - 指示在获取 JSON 数据时发生网络错误
   */
  default Map<CurrencyPair, Fee> getDynamicTradingFees() throws IOException {
    throw new NotYetImplementedForExchangeException("getDynamicTradingFees 获取动态交易费用");
  }
}
