package org.knowm.xchange.dto.account;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

/**
 * DTO representing account information
 * * DTO 代表账户信息
 *
 * <p>Account information is anything particular associated with the user's login
 * * <p>帐户信息是与用户登录相关的任何特定信息
 */
public final class AccountInfo implements Serializable {

  private static final long serialVersionUID = -3572240060624800060L;

  /** The name on the account
   * 账户上的名字 */
  private final String username;

  /**
   * The current fee this account must pay as a fraction of the value of each trade. Null if there is no such fee.
   * * 此账户必须支付的当前费用是每笔交易价值的一小部分。 如果没有此类费用，则为空。
   */
  private final BigDecimal tradingFee;

  /** The wallets owned by this account
   * 该账户拥有的钱包 */
  private final Map<String, Wallet> wallets;

  /** The open positions owned by this account
   * 该账户拥有的未平仓头寸 */
  private final Collection<OpenPosition> openPositions;

  /**
   * The timestamp at which this account information was generated. May be null if not provided by the exchange.
   * * 生成此帐户信息的时间戳。 如果交易所未提供，则可能为空。
   */
  @Nullable private final Date timestamp;

  /** @see #AccountInfo(String, BigDecimal, Collection) */
  public AccountInfo(Wallet... wallets) {

    // TODO when refactoring for separate feature interfaces, change this constructor to require at least two wallets
    //在为单独的功能接口重构时，将此构造函数更改为至少需要两个钱包
    this(null, null, wallets);
  }

  /** @see #AccountInfo(String, BigDecimal, Collection, Date) */
  public AccountInfo(Date timestamp, Wallet... wallets) {

    // TODO when refactoring for separate feature interfaces, change this constructor to require at least two wallets
    // TODO 重构单独的功能接口时，将此构造函数更改为至少需要两个钱包
    this(null, null, Arrays.asList(wallets), timestamp);
  }

  /** @see #AccountInfo(String, BigDecimal, Collection) */
  public AccountInfo(Collection<Wallet> wallets) {

    this(null, null, wallets);
  }

  /** @see #AccountInfo(String, BigDecimal, Collection) */
  public AccountInfo(String username, Wallet... wallets) {

    this(username, null, wallets);
  }

  /** @see #AccountInfo(String, BigDecimal, Collection) */
  public AccountInfo(String username, Collection<Wallet> wallets) {

    this(username, null, wallets);
  }

  /**
   * Constructs an {@link AccountInfo}.
   *
   * @param username the user name. 用户名。
   * @param tradingFee the trading fee. 交易费。
   * @param wallets the user's wallets 用户的钱包
   */
  public AccountInfo(String username, BigDecimal tradingFee, Collection<Wallet> wallets) {
    this(username, tradingFee, wallets, null);
  }

  /**
   * Constructs an {@link AccountInfo}.
   *
   * @param username the user name. 用户名
   * @param tradingFee the trading fee. 交易费。
   * @param wallets the user's wallets 用户的钱包
   * @param timestamp the timestamp for the account snapshot.  帐户快照的时间戳。
   */
  public AccountInfo(
      String username, BigDecimal tradingFee, Collection<Wallet> wallets, Date timestamp) {

    this(username, tradingFee, wallets, Collections.emptySet(), timestamp);
  }

  /**
   * Constructs an {@link AccountInfo}.
   *
   * @param username the user name. 用户名
   * @param tradingFee the trading fee. 交易费
   * @param wallets the user's wallets 用户的钱包
   * @param openPositions the users's open positions|
   *                      用户的空缺职位
   * @param timestamp the timestamp for the account snapshot.
   *                  帐户快照的时间戳。
   */
  public AccountInfo(
      String username,
      BigDecimal tradingFee,
      Collection<Wallet> wallets,
      Collection<OpenPosition> openPositions,
      Date timestamp) {

    this.username = username;
    this.tradingFee = tradingFee;
    this.timestamp = timestamp;
    this.openPositions = openPositions;

    if (wallets.size() == 0) {
      this.wallets = Collections.emptyMap();
    } else if (wallets.size() == 1) {
      Wallet wallet = wallets.iterator().next();
      this.wallets = Collections.singletonMap(wallet.getId(), wallet);
    } else {
      this.wallets = new HashMap<>();
      for (Wallet wallet : wallets) {
        if (this.wallets.containsKey(wallet.getId())) {
          throw new IllegalArgumentException("duplicate wallets passed to AccountInfo 传递给 AccountInfo 的重复钱包");
        }
        this.wallets.put(wallet.getId(), wallet);
      }
    }
  }

  /** @see #AccountInfo(String, BigDecimal, Collection) */
  public AccountInfo(String username, BigDecimal tradingFee, Wallet... wallets) {

    this(username, tradingFee, Arrays.asList(wallets));
  }

  /** Gets all wallets in this account
   * 获取该账户中的所有钱包 */
  public Map<String, Wallet> getWallets() {

    return Collections.unmodifiableMap(wallets);
  }

  /** Gets wallet for accounts which don't use multiple wallets with ids
   * 为不使用多个带 id 钱包的账户获取钱包 */
  public Wallet getWallet() {

    if (wallets.size() != 1) {
      throw new UnsupportedOperationException(wallets.size() + " wallets in account 账户中的钱包");
    }

    return getWallet(wallets.keySet().iterator().next());
  }

  /** Gets the wallet with a specific id  获取具有特定 id 的钱包*/
  public Wallet getWallet(String id) {

    return wallets.get(id);
  }

  /**
   * Get wallet with given feature
   * 获取具有给定功能的钱包
   *
   * @return null if no wallet on given exchange supports this feature
   * @return null 如果给定交易所没有钱包支持此功能
   * @throws UnsupportedOperationException if there are more then one wallets supporting the given  feature
   * * @throws UnsupportedOperationException 如果有多个钱包支持给定功能
   */
  public Wallet getWallet(Wallet.WalletFeature feature) {
    List<Wallet> walletWithFeatures = new ArrayList<>();

    wallets.forEach(
        (s, wallet) -> {
          if (wallet.getFeatures() != null) {
            if (wallet.getFeatures().contains(feature)) {
              walletWithFeatures.add(wallet);
            }
          }
        });

    if (walletWithFeatures.size() > 1) {
      throw new UnsupportedOperationException("More than one wallet offer this feature. 不止一个钱包提供此功能。");
    } else if (walletWithFeatures.size() == 0) {
      return null;
    }
    return walletWithFeatures.get(0);
  }

  /** @return The user name
   * @return 用户名 */
  public String getUsername() {

    return username;
  }

  /**
   * Returns the current trading fee
   * * 返回当前交易费用
   *
   * @return The trading fee
   * * @return 交易费用
   */
  public BigDecimal getTradingFee() {

    return tradingFee;
  }

  /**
   * @return The timestamp at which this account information was generated. May be null if not provided by the exchange.
   * * @return 生成此帐户信息的时间戳。 如果交易所未提供，则可能为空。
   */
  public Date getTimestamp() {
    return timestamp;
  }

  public Collection<OpenPosition> getOpenPositions() {
    return openPositions;
  }

  @Override
  public String toString() {

    return "AccountInfo [username="
        + username
        + ", tradingFee="
        + tradingFee
        + ", wallets="
        + wallets
        + "]";
  }
}
