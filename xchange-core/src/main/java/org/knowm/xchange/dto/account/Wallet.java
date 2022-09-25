package org.knowm.xchange.dto.account;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.*;
import org.knowm.xchange.currency.Currency;

/**
 * DTO representing a wallet
 * 代表钱包的 DTO
 *
 * <p>A wallet has a set of current balances in various currencies held on the exchange.
 * * <p>一个钱包有一组在交易所持有的各种货币的当前余额。
 */
public final class Wallet implements Serializable {

  private static final long serialVersionUID = -4136681413143690633L;

  public enum WalletFeature {
    /** The wallet has the ability to deposit external funds and withdraw funds allocated on it
     * 钱包能够存入外部资金并提取分配在其上的资金*/
    FUNDING,
    /** You can trade funds allocated to this wallet
     * 您可以交易分配给此钱包的资金 */
    TRADING,
    /** You can do margin trading with funds allocated to this wallet
     * 您可以使用分配到此钱包的资金进行保证金交易 */
    MARGIN_TRADING,
    /** You can fund other margin traders with funds allocated to this wallet to earn an interest
     * 您可以使用分配到此钱包的资金为其他保证金交易者提供资金以赚取利息 */
    MARGIN_FUNDING
  }

  /** The keys represent the currency of the wallet.
   * 密钥代表钱包的货币。*/
  private final Map<Currency, Balance> balances;
  /** Collection of balances for deserialization
   * 为反序列化收集余额* */
  private final Collection<Balance> balanceCollection;
  /** A unique identifier for this wallet
   * 此钱包的唯一标识符 */
  private String id;
  /** A descriptive name for this wallet. Defaults to {@link #id}
   * 此钱包的描述性名称。 默认为 {@link #id}*/
  private String name;
  /** Features supported by this wallet
   *  Features supported by this wallet */
  private final Set<WalletFeature> features;
  /** Maximum leverage for margin trading supported by this wallet
   * 此钱包支持的杠杆交易最大杠杆 */
  private BigDecimal maxLeverage = BigDecimal.ZERO;
  /** Current leverage for margin trading done on this wallet
   * 当前在此钱包上进行的保证金交易杠杆*/
  private BigDecimal currentLeverage = BigDecimal.ZERO;

  /**
   * Constructs a {@link Wallet}.
   *
   * @param id the wallet id
   *           钱包编号
   *
   * @param name a descriptive name for the wallet
   *             钱包的描述性名称
   *
   * @param balances the balances, the currencies of the balances should not be duplicated.
   *                 余额，余额的货币不应重复。
   *
   * @param features all the features that wallet supports  <p>maxLeverage and currentLeverage are BigDecimal.ZERO for the default constructor
   *                 钱包支持的所有功能 <p>maxLeverage 和 currentLeverage 对于默认构造函数都是 BigDecimal.ZERO
   */
  public Wallet(
      @JsonProperty("id") String id,
      @JsonProperty("name") String name,
      @JsonProperty("balances") Collection<Balance> balances,
      @JsonProperty("features") Set<WalletFeature> features,
      @JsonProperty("maxLeverage") BigDecimal maxLeverage,
      @JsonProperty("currentLeverage") BigDecimal currentLeverage) {

    this.id = id;
    if (name == null) {
      this.name = id;
    } else {
      this.name = name;
    }
    this.balanceCollection = balances;
    if (balances.size() == 0) {
      this.balances = Collections.emptyMap();
    } else if (balances.size() == 1) {
      Balance balance = balances.iterator().next();
      this.balances = Collections.singletonMap(balance.getCurrency(), balance);
    } else {
      this.balances = new HashMap<>();
      for (Balance balance : balances) {
        if (this.balances.containsKey(balance.getCurrency()))
          // this class could merge balances, but probably better to catch mistakes and let the exchange merge them
          // 这个类可以合并余额，但最好能发现错误并让交易所合并它们
          throw new IllegalArgumentException("duplicate balances in wallet 钱包中的重复余额");
        this.balances.put(balance.getCurrency(), balance);
      }
    }
    this.features = features;
    this.maxLeverage = maxLeverage;
    this.currentLeverage = currentLeverage;
  }

  /** @return The wallet id
   * 钱包编号*/
  public String getId() {

    return id;
  }

  /** @return A descriptive name for the wallet
   * 钱包的描述性名称 */
  public String getName() {

    return name;
  }

  /** @return The available colletion of balances
   * 可用的余额集合 */
  @JsonGetter
  public Collection<Balance> balances() {

    return balanceCollection;
  }

  /** @return The available balances (amount and currency)
   * 可用余额（金额和货币） */
  @JsonIgnore
  public Map<Currency, Balance> getBalances() {

    return Collections.unmodifiableMap(balances);
  }

  /** @return All wallet operation features
   * 所有钱包操作功能 */
  public Set<WalletFeature> getFeatures() {
    return features;
  }

  /** @return Max leverage of wallet
   * 钱包最大杠杆*/
  public BigDecimal getMaxLeverage() {
    return maxLeverage;
  }

  /** @return current leverage of wallet
   * 当前钱包杠杆 */
  public BigDecimal getCurrentLeverage() {
    return currentLeverage;
  }

  /**
   * Returns the balance for the specified currency.
   * 返回指定货币的余额。
   *
   * @param currency a {@link Currency}. {@link 货币}。
   * @return the balance of the specified currency, or a zero balance if currency not present
   * 指定货币的余额，如果货币不存在，则余额为零
   */
  public Balance getBalance(Currency currency) {

    Balance balance = this.balances.get(currency);
    return balance == null ? Balance.zero(currency) : balance;
  }

  @Override
  public boolean equals(Object object) {

    if (object == this) return true;
    if (!(object instanceof Wallet)) return false;

    Wallet wallet = (Wallet) object;
    return Objects.equals(id, wallet.id)
        && Objects.equals(name, wallet.name)
        && balances.equals(wallet.balances);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, balances);
  }

  @Override
  public String toString() {
    return "Wallet{"
        + "balances="
        + balanceCollection
        + ", id='"
        + id
        + '\''
        + ", name='"
        + name
        + '\''
        + ", walletFeatures="
        + features
        + ", maxLeverage="
        + maxLeverage
        + ", currentLeverage="
        + currentLeverage
        + '}';
  }

  public static class Builder {

    private Collection<Balance> balances;

    private String id;

    private String name;
    /** These are the default wallet features
     * 这些是默认的钱包功能 */
    private Set<WalletFeature> features =
        Stream.of(WalletFeature.TRADING, WalletFeature.FUNDING).collect(Collectors.toSet());

    private BigDecimal maxLeverage = BigDecimal.ZERO;

    private BigDecimal currentLeverage = BigDecimal.ZERO;

    public static Builder from(Collection<Balance> balances) {
      return new Builder().balances(balances);
    }

    private Builder balances(Collection<Balance> balances) {
      this.balances = balances;
      return this;
    }

    public Builder id(String id) {

      this.id = id;
      return this;
    }

    public Builder name(String name) {

      this.name = name;
      return this;
    }

    public Builder features(Set<WalletFeature> features) {

      this.features = features;
      return this;
    }

    public Builder maxLeverage(BigDecimal maxLeverage) {

      this.maxLeverage = maxLeverage;
      return this;
    }

    public Builder currentLeverage(BigDecimal currentLeverage) {

      this.currentLeverage = currentLeverage;
      return this;
    }

    public Wallet build() {

      return new Wallet(id, name, balances, features, maxLeverage, currentLeverage);
    }
  }
}
