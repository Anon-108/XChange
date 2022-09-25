package org.knowm.xchange.dto.meta;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.derivative.FuturesContract;
import org.knowm.xchange.derivative.OptionsContract;
import org.knowm.xchange.utils.ObjectMapperHelper;

/**
 * This class is loaded during creation of the Exchange and is intended to hold both data that is
  readily available from an HTTP API request at an exchange extended by semi-static data that is
  not available from an HTTP API, but is still important information to have. Examples include
  currency pairs, max polling rates, scaling factors, etc. For more info see:
  https://github.com/timmolter/XChange/wiki/Design-Notes
 此类在创建 Exchange 期间加载，旨在保存两个数据
 在由半静态数据扩展的交换中，可以从 HTTP API 请求中轻松获得
 不能从 HTTP API 获得，但仍然是重要的信息。 例子包括
 货币对、最大轮询率、缩放因子等。有关更多信息，请参阅：
 https://github.com/timmolter/XChange/wiki/Design-Notes
 *
 * <p>This class is used only in the API by the classes that merge metadata stored in custom JSON file and online info from the remote exchange.
 * <p>该类仅在 API 中由合并存储在自定义 JSON 文件中的元数据和来自远程交换的在线信息的类使用。
 */
public class ExchangeMetaData implements Serializable {

  private static final long serialVersionUID = -1495610469981534977L;

  @JsonProperty("currency_pairs")
  private Map<CurrencyPair, CurrencyPairMetaData> currencyPairs;

  @JsonProperty("currencies")
  private Map<Currency, CurrencyMetaData> currencies;

  @JsonProperty("futures")
  private Map<FuturesContract, DerivativeMetaData> futures;

  @JsonProperty("options")
  private Map<OptionsContract, DerivativeMetaData> options;

  @JsonProperty("public_rate_limits")
  private RateLimit[] publicRateLimits;

  @JsonProperty("private_rate_limits")
  private RateLimit[] privateRateLimits;

  /**
   * If true, both public and private calls use single rate limit policy, which is described in {@link #privateRateLimits}.
   * * 如果为 true，则公共和私人呼叫都使用单一速率限制策略，在 {@link #privateRateLimits} 中进行了描述。
   */
  @JsonProperty("share_rate_limits")
  private boolean shareRateLimits = true;

  /**
   * Constructor
   *
   * @param currencyPairs Map of {@link CurrencyPair} -> {@link CurrencyPairMetaData}
   *                      {@link CurrencyPair} -> {@link CurrencyPairMetaData} 的地图
   *
   * @param currency Map of currency -> {@link CurrencyMetaData}
   *                 货币地图 -> {@link Currency MetaData}
   */
  public ExchangeMetaData(
      Map<CurrencyPair, CurrencyPairMetaData> currencyPairs,
      Map<Currency, CurrencyMetaData> currency,
      RateLimit[] publicRateLimits,
      RateLimit[] privateRateLimits,
      Boolean shareRateLimits) {
    this(currencyPairs, currency, null, null, publicRateLimits, privateRateLimits, shareRateLimits);
  }

  public ExchangeMetaData(
      @JsonProperty("currency_pairs") Map<CurrencyPair, CurrencyPairMetaData> currencyPairs,
      @JsonProperty("currencies") Map<Currency, CurrencyMetaData> currency,
      @JsonProperty("futures") Map<FuturesContract, DerivativeMetaData> futures,
      @JsonProperty("options") Map<OptionsContract, DerivativeMetaData> options,
      @JsonProperty("public_rate_limits") RateLimit[] publicRateLimits,
      @JsonProperty("private_rate_limits") RateLimit[] privateRateLimits,
      @JsonProperty("share_rate_limits") Boolean shareRateLimits) {

    this.currencyPairs = currencyPairs;
    this.currencies = currency;
    this.futures = futures;
    this.options = options;

    this.publicRateLimits = publicRateLimits;
    this.privateRateLimits = privateRateLimits;

    this.shareRateLimits = shareRateLimits != null ? shareRateLimits : false;
  }

  /**
   * @return minimum number of milliseconds required between any two remote calls, assuming the
       client makes consecutive calls without any bursts or breaks for an infinite period of time.
       Returns null if the rateLimits collection is null or empty
      任何两个远程调用之间所需的最小毫秒数，假设
      客户端在无限时间段内连续调用，没有任何突发或中断。
      如果 rateLimits 集合为 null 或为空，则返回 null
   */
  @JsonIgnore
  public static Long getPollDelayMillis(RateLimit[] rateLimits) {
    if (rateLimits == null || rateLimits.length == 0) {
      return null;
    }
    long result = 0;
    for (RateLimit rateLimit : rateLimits) {
      // this is the delay between calls, we want max, any smaller number is for burst calls
      // 这是调用之间的延迟，我们想要最大值，任何较小的数字都用于突发调用
      result = Math.max(result, rateLimit.getPollDelayMillis());
    }
    return result;
  }

  public Map<CurrencyPair, CurrencyPairMetaData> getCurrencyPairs() {
    return currencyPairs;
  }

  public Map<Currency, CurrencyMetaData> getCurrencies() {
    return currencies;
  }

  public Map<FuturesContract, DerivativeMetaData> getFutures() {
    return futures;
  }

  public Map<OptionsContract, DerivativeMetaData> getOptions() {
    return options;
  }

  public RateLimit[] getPublicRateLimits() {
    return publicRateLimits;
  }

  public RateLimit[] getPrivateRateLimits() {
    return privateRateLimits;
  }

  public boolean isShareRateLimits() {
    return shareRateLimits;
  }

  @JsonIgnore
  public String toJSONString() {
    return ObjectMapperHelper.toJSON(this);
  }

  @Override
  public String toString() {
    return "ExchangeMetaData [currencyPairs="
        + currencyPairs
        + ", currencies="
        + currencies
        + ", futures="
        + futures
        + ", options="
        + options
        + ", publicRateLimits="
        + Arrays.toString(publicRateLimits)
        + ", privateRateLimits="
        + Arrays.toString(privateRateLimits)
        + ", shareRateLimits="
        + shareRateLimits
        + "]";
  }
}
