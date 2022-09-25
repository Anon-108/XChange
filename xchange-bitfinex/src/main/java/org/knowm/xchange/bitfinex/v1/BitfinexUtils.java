package org.knowm.xchange.bitfinex.v1;

import org.apache.commons.lang3.StringUtils;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.exceptions.ExchangeException;

/** A central place for shared Bitfinex properties
 * 共享 Bitfinex 属性的中心位置 */
public final class BitfinexUtils {

  /** private Constructor */
  private BitfinexUtils() {}

  private static final String USDT_SYMBOL_BITFINEX = "UST";
  private static final String USDT_SYMBOL_XCHANGE = "USDT";

  public static String adaptXchangeCurrency(Currency xchangeSymbol) {

    if (xchangeSymbol == null) {
      return null;
    }

    if (USDT_SYMBOL_XCHANGE.equals(xchangeSymbol.toString())) {
      return USDT_SYMBOL_BITFINEX;
    }

    return xchangeSymbol.toString(); // .toLowerCase();
  }

  public static String toPairString(CurrencyPair currencyPair) {

    if (currencyPair == null) {
      return null;
    }

    String base = adaptXchangeCurrency(currencyPair.base);
    String counter = adaptXchangeCurrency(currencyPair.counter);
    return "t"
        + base
        + currencySeparator(base, counter)
        + adaptXchangeCurrency(currencyPair.counter);
  }

  public static String toPairStringV1(CurrencyPair currencyPair) {

    if (currencyPair == null) {
      return null;
    }

    String base = StringUtils.lowerCase(adaptXchangeCurrency(currencyPair.base));
    String counter = StringUtils.lowerCase(adaptXchangeCurrency(currencyPair.counter));
    return base + currencySeparator(base, counter) + adaptXchangeCurrency(currencyPair.counter);
  }

  /**
   * Unfortunately we need to go this way, since the pairs at Bitfinex are not very consistent see dusk:xxx pairs at https://api.bitfinex.com/v1/symbols_details the same for xxx:cnht
   * * 不幸的是，我们需要走这条路，因为 Bitfinex 上的对不是很一致，请参阅 https://api.bitfinex.com/v1/symbols_details 上的黄昏：xxx 对 xxx:cnht 相同
   *
   * @param base currency to build string with
   *             用于构建字符串的货币
   * @param counter currency to build string with
   *                用于构建字符串的货币
   * @return string based on pair
   *        基于对的字符串
   */
  private static String currencySeparator(String base, String counter) {
    if (base.length() > 3 || counter.length() > 3) {
      return ":";
    }
    return "";
  }

  /**
   * From the reference documentation for field withdraw_type (2018-02-14); can be one of the following ['bitcoin', 'litecoin', 'ethereum', 'ethereumc', 'mastercoin', 'zcash', 'monero',
    'wire', 'dash', 'ripple', 'eos', 'neo', 'aventus', 'qtum', 'eidoo'] From customer support via email on 2018-02-27;
   "... After discussing with our developers, you can use API to withdraw BCH and withdraw_type is bcash. ..."
   来自字段withdraw_type（2018-02-14）的参考文档； 可以是以下之一 ['bitcoin', 'litecoin', 'ethereum', 'ethereumc', 'mastercoin', 'zcash', 'monero',
   'wire', 'dash', 'ripple', 'eos', 'neo', 'aventus', 'qtum', 'eidoo'] 来自客户支持，于 2018 年 2 月 27 日通过电子邮件发送；
   “...与我们的开发人员讨论后，您可以使用API提取BCH，withdraw_type为bcash。...”
   *
   * @param currency
   * @return
   */
  public static String convertToBitfinexWithdrawalType(String currency) {
    switch (currency.toUpperCase()) {
      case "BTC":
        return "bitcoin";
      case "LTC":
        return "litecoin";
      case "ETH":
        return "ethereum";
      case "ETC":
        return "ethereumc";
      case "CLO":
        return "clo";
      case "ZEC":
        return "zcash";
      case "XMR":
        return "monero";
      case "USD":
        return "mastercoin";
      case "DASH":
        return "dash";
      case "XRP":
        return "ripple";
      case "EOS":
        return "eos";
      case "NEO":
        return "neo";
      case "AVT":
        return "aventus";
      case "QTUM":
        return "qtum";
      case "EDO":
        return "eidoo";
      case "BTG":
        return "bgold";
      case "BCH":
        return "bcash";
      case "USDT":
        return "tetheruso";
      default:
        throw new ExchangeException("Cannot determine withdrawal type. 无法确定提款类型。");
    }
  }
}
