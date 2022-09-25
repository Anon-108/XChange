package org.knowm.xchange.dto.account;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.knowm.xchange.currency.Currency;

/**
 * DTO representing funding information
 * * DTO 代表资金信息
 *
 * <p>Funding information contains the detail of deposit/withdrawal transaction for a specific currency
 * * <p>资金信息包含特定货币的存款/取款交易的详细信息
 */
public final class FundingRecord implements Serializable {

  private static final long serialVersionUID = 3788398035845873448L;

  /** Crypto currency address for deposit/withdrawal
   * 用于存款/取款的加密货币地址 */
  private final String address;

  /** Crypto currency destination tag for deposit/withdrawal
   * 用于存款/取款的加密货币目的地标签 */
  private final String addressTag;

  /** Date/Time of transaction
   * 交易日期/时间*/
  private final Date date;

  /** The transaction currency
   * 交易币种*/
  private final Currency currency;

  /** Amount deposited/withdrawn in given transaction currency (always positive)
   * 以给定交易货币存入/提取的金额（始终为正） */
  private final BigDecimal amount;

  /** Internal transaction identifier, specific to the Exchange.
   * 内部交易标识符，特定于交易所。*/
  private final String internalId;

  /**
   * External Transaction id that identifies the transaction within the public ledger, eg. blockchain transaction hash.
   * 标识公共分类帐中的交易的外部交易 id，例如。 区块链交易哈希。
   */
  private final String blockchainTransactionHash;
  /** Transaction Type
   * 交易类型*/
  private final Type type;
  /**
   * Status of the transaction whenever available (e.g. Open, Completed or any descriptive status of transaction)
   * * 可用的交易状态（例如打开、完成或任何描述性交易状态）
   */
  private final Status status;
  /** Balance of the associated account after the transaction is performed
   * 交易完成后关联账户的余额 */
  private final BigDecimal balance;
  /** Transaction Fee Amount in given transaction currency (always positive)
   * 给定交易货币的交易费用金额（始终为正） */
  private final BigDecimal fee;
  /** Description of the transaction
   * 交易说明*/
  private String description;

  /**
   * Constructs a {@link FundingRecord}.
   *
   * @param address Crypto currency address for deposit/withdrawal
   *                用于存款/取款的加密货币地址
   *
   * @param date Date/Time of transaction
   *             交易日期/时间
   *
   * @param currency The transaction currency
   *                 交易币种
   *
   * @param amount Amount deposited/withdrawn (always positive)
   *               存款/取款金额（始终为正）
   *
   * @param internalId Internal transaction identifier, specific to the Exchange
   *                   内部交易标识符，特定于交易所
   *
   * @param blockchainTransactionHash Transaction hash/id that identifies the transaction within the  public ledger
   *                                  交易哈希/ID，用于标识公共分类账中的交易
   *
   * @param type Transaction Type {@link Type}
   *             交易类型 {@link 类型}
   *
   * @param status Status of the transaction whenever available (e.g. Pending, Completed or any
        descriptive status of transaction). Will be naively converted to Status enum if possible, or else be prefixed to description.
  可用的交易状态（例如待处理、已完成或任何
  交易的描述状态）。 如果可能，将天真地转换为状态枚举，或者作为描述的前缀。

   * @param balance Balance of the associated account after the transaction is performed
   *                交易完成后关联账户的余额
   *
   * @param fee Transaction Fee Amount (always positive)
   *            交易费用金额（始终为正）
   *
   * @param description Description of the transaction. It is a good idea to put here any extra info
       sent back from the exchange that doesn't fit elsewhere so users can still access it.
        交易的描述。 将任何额外信息放在这里是个好主意
        从不适合其他地方的交易所发回，因此用户仍然可以访问它。

   * @deprecated Use the constructor with enum status parameter.
   * * @deprecated 使用带有枚举状态参数的构造函数。
   */
  @Deprecated
  public FundingRecord(
      final String address,
      final Date date,
      final Currency currency,
      final BigDecimal amount,
      final String internalId,
      final String blockchainTransactionHash,
      final Type type,
      final String status,
      final BigDecimal balance,
      final BigDecimal fee,
      final String description) {
    this(
        address,
        date,
        currency,
        amount,
        internalId,
        blockchainTransactionHash,
        type,
        Status.resolveStatus(status),
        balance,
        fee,
        description);
    if (this.status == null && status != null) {
      this.description =
          this.description == null || this.description.isEmpty()
              ? status
              : status + ": " + this.description;
    }
  }

  /**
   * Constructs a {@link FundingRecord}.
   *
   * @param address Crypto currency address for deposit/withdrawal
   *                用于存款/取款的加密货币地址
   *
   * @param addressTag Crypto address destination tag for deposit/withdrawal
   *                   用于存款/取款的加密地址目的地标签
   *
   * @param date Date/Time of transaction
   *             交易日期/时间
   *
   * @param currency The transaction currency
   *                 交易币种
   *
   * @param amount Amount deposited/withdrawn (always positive)
   *               存款/取款金额（始终为正）
   *
   * @param internalId Internal transaction identifier, specific to the Exchange
   *                   内部交易标识符，特定于交易所
   *
   * @param blockchainTransactionHash Transaction hash/id that identifies the transaction within the  public ledger
   *                                  交易哈希/ID，用于标识公共分类账中的交易
   *
   * @param type Transaction Type {@link Type}
   *             交易类型 {@link 类型}
   *
   * @param status Status of the transaction whenever available
   *               可用时的交易状态
   *
   * @param balance Balance of the associated account after the transaction is performed
   *                交易完成后关联账户的余额
   *
   * @param fee Transaction Fee Amount (always positive)
   *            交易费用金额（始终为正）
   *
   * @param description Description of the transaction. It is a good idea to put here any extra info sent back from the exchange that doesn't fit elsewhere so users can still access it.
   *                    * @param description 交易的描述。 最好将交易所发回的任何其他不适合其他地方的额外信息放在这里，以便用户仍然可以访问它。
   */
  public FundingRecord(
      final String address,
      final String addressTag,
      final Date date,
      final Currency currency,
      final BigDecimal amount,
      final String internalId,
      final String blockchainTransactionHash,
      final Type type,
      final Status status,
      final BigDecimal balance,
      final BigDecimal fee,
      final String description) {
    this.address = address;
    this.addressTag = addressTag == null || addressTag.isEmpty() ? null : addressTag;
    this.date = date;
    this.currency = currency;
    this.amount = amount == null ? null : amount.abs();
    this.internalId = internalId;
    this.blockchainTransactionHash = blockchainTransactionHash;
    this.type = type;
    this.status = status;
    this.balance = balance;
    this.fee = fee == null ? null : fee.abs();
    this.description = description;
  }

  /**
   * Constructs a {@link FundingRecord}.
   *
   * @param address Crypto currency address for deposit/withdrawal
   *                用于存款/取款的加密货币地址
   *
   * @param date Date/Time of transaction
   *             交易日期/时间
   *
   * @param currency The transaction currency
   *                 交易币种
   *
   * @param amount Amount deposited/withdrawn (always positive)
   *               存款/取款金额（始终为正）
   *
   * @param internalId Internal transaction identifier, specific to the Exchange
   *                   内部交易标识符，特定于交易所
   *
   * @param blockchainTransactionHash Transaction hash/id that identifies the transaction within the  public ledger
   *                                  交易哈希/ID，用于标识公共分类账中的交易
   *
   * @param type Transaction Type {@link Type}
   *             交易类型 {@link 类型}
   *
   * @param status Status of the transaction whenever available
   *               可用时的交易状态
   *
   * @param balance Balance of the associated account after the transaction is performed
   *                交易完成后关联账户的余额
   *
   * @param fee Transaction Fee Amount (always positive)
   *            交易费用金额（始终为正）
   *
   * @param description Description of the transaction. It is a good idea to put here any extra info sent back from the exchange that doesn't fit elsewhere so users can still access it.
   *                    * @param description 交易的描述。 最好将交易所发回的任何其他不适合其他地方的额外信息放在这里，以便用户仍然可以访问它。
   */
  public FundingRecord(
      final String address,
      final Date date,
      final Currency currency,
      final BigDecimal amount,
      final String internalId,
      final String blockchainTransactionHash,
      final Type type,
      final Status status,
      final BigDecimal balance,
      final BigDecimal fee,
      final String description) {
    this(
        address,
        null,
        date,
        currency,
        amount,
        internalId,
        blockchainTransactionHash,
        type,
        status,
        balance,
        fee,
        description);
  }

  /** @return Crypto currency address
   * 加密货币地址 */
  public String getAddress() {
    return address;
  }

  public String getAddressTag() {
    return addressTag;
  }

  /** @return Date/Time of transaction
   * 交易日期/时间 */
  public Date getDate() {
    return date;
  }

  /** @return The transaction currency
   * 交易币种 */
  public Currency getCurrency() {
    return currency;
  }

  /** @return Amount deposited/withdrawn in given transaction currency (always positive)
   * 以给定交易货币存入/提取的金额（始终为正） */
  public BigDecimal getAmount() {
    return amount;
  }

  /** @return Internal transaction identifier, specific to the Exchange.
   * 内部交易标识符，特定于交易所。 */
  public String getInternalId() {
    return internalId;
  }

  @Deprecated // for backward compatibility.  Will be removed // 为了向后兼容。 将被移除
  public String getExternalId() {
    return blockchainTransactionHash;
  }

  /**
   * @return External Transaction id that identifies the transaction within the public ledger, eg.   blockchain transaction hash.
   * @return 标识公共分类帐中的交易的外部交易ID，例如。 区块链交易哈希。
   */
  public String getBlockchainTransactionHash() {
    return blockchainTransactionHash;
  }

  /** @return Transaction Type {@link Type}
   * 交易类型 {@link 类型} */
  public Type getType() {
    return type;
  }

  /**
   * @return Status of the transaction whenever available (e.g. Open, Completed or any descriptive  status of transaction)
   * @return 交易状态（例如，打开、完成或任何描述性交易状态）
   */
  public Status getStatus() {
    return status;
  }

  /** @return Balance of the associated account after the transaction is performed
   * 交易完成后关联账户的余额*/
  public BigDecimal getBalance() {
    return balance;
  }

  /** @return Transaction Fee Amount in given transaction currency (always positive)
   * 给定交易货币的交易费用金额（始终为正） */
  public BigDecimal getFee() {
    return fee;
  }

  /** @return Description of the transaction
   * 交易说明*/
  public String getDescription() {
    return description;
  }

  @Override
  public String toString() {
    return String.format(
        "FundingRecord{address='%s', date=%s, currency=%s, amount=%s, internalId=%s, blockchainTransactionHash=%s, description='%s', type=%s, status=%s, balance=%s, fee=%s}",
        address,
        date,
        currency,
        amount,
        internalId,
        blockchainTransactionHash,
        description,
        type,
        status,
        balance,
        fee);
  }

  /** Enum representing funding transaction type
   * 代表资金交易类型的枚举 */
  public enum Type {
    WITHDRAWAL(false),
    DEPOSIT(true),
    AIRDROP(true),
    /**
     * Used for inflows that are not a regular users deposit and are either different the inflows
     defined above or their nature could not have been deduced from the exchanges response
     用于非普通用户存款且与流入不同的流入
     上述定义或它们的性质无法从交易所的回复中推断出来
     */
    OTHER_INFLOW(true),
    /**
     * Used for outflows that are not a regular users withdrawal and are either different the
      outflows defined above or their nature could not have been deduced from the exchanges response
     用于非普通用户提款的流出，并且要么不同
     上述定义的流出或其性质无法从交易所的回应中推断出来
     */
    OTHER_OUTFLOW(false),

    /** Used for transfers between exchanges accounts
     * 用于交易所账户之间的转账 */
    INTERNAL_WITHDRAWAL(false),

    /** Used for transfers between exchanges accounts
     * 用于交易所账户之间的转账 */
    INTERNAL_DEPOSIT(true),

    /** Used for realised losses from derivatives
     * 用于衍生品的已实现损失*/
    REALISED_LOSS(false),

    /** Used for realised profits from derivatives
     * 用于衍生品的已实现利润 */
    REALISED_PROFIT(true);

    private static final Map<String, Type> fromString = new HashMap<>();

    static {
      for (Type type : values()) fromString.put(type.toString(), type);
    }

    private final boolean inflow;

    Type(final boolean inflow) {
      this.inflow = inflow;
    }

    public static Type fromString(String ledgerTypeString) {
      return fromString.get(ledgerTypeString.toUpperCase());
    }

    public boolean isInflowing() {
      return this.inflow;
    }

    public boolean isOutflowing() {
      return !this.inflow;
    }
  }

  public enum Status {
    /**
     * The user has requested the withdrawal or deposit, or the exchange has detected an initiated
     deposit, but the exchange still has to fully process the funding. The funds are not available
     to the user. The funding request may possibly still be cancelled though.
     用户已请求提款或充值，或交易所检测到发起
     存款，但交易所仍然必须完全处理资金。 资金不可用
     给用户。 不过，资金请求可能仍会被取消。
     */
    PROCESSING(
        "WAIT CONFIRMATION",
        "EMAIL CONFIRMATION",
        "EMAIL SENT",
        "AWAITING APPROVAL",
        "VERIFYING",
        "PENDING_APPROVAL",
        "PENDING"),

    /**
     * The exchange has processed the transfer fully and successfully. The funding typically cannot
      be cancelled any more. For withdrawals, the funds are gone from the exchange, though they may
      have not reached their destination yet. For deposits, the funds are available to the user.
     交易所已完全成功地处理了转移。 资金通常不能
     被取消了。 对于提款，资金会从交易所消失，尽管它们可能
     还没有到达目的地。 对于存款，资金可供用户使用。
     */
    COMPLETE("COMPLETED"),

    /** The transfer was cancelled either by the user or by the exchange.
     * 转移被用户或交易所取消。*/
    CANCELLED("REVOKED", "CANCEL", "REFUND"),

    /**
     * The transfer has failed for any reason other than user cancellation after it was initiated
     and before it was successfully processed. For withdrawals, the funds are available to the user again.
     转移在启动后因用户取消以外的任何原因而失败
     在它被成功处理之前。 对于提款，资金再次可供用户使用。
     */
    FAILED("FAILURE"),
    ;

    private static final Map<String, Status> fromString = new HashMap<>();

    static {
      for (final Status status : values()) {
        final String[] statusArray = status.statusArray;
        if (statusArray != null) {
          for (final String statusStr : statusArray) {
            fromString.put(statusStr, status);
          }
        }
        fromString.put(status.toString(), status);
      }
    }

    private String[] statusArray;

    Status(String... statusArray) {
      this.statusArray = statusArray;
    }

    public static Status resolveStatus(String str) {
      if (str == null) {
        return null;
      }
      return fromString.get(str.toUpperCase());
    }
  }

  public static final class Builder {

    private String address;
    private String addressTag;
    private Date date;
    private Currency currency;
    private BigDecimal amount;
    private String internalId;
    private String blockchainTransactionHash;
    private String description;
    private Type type;
    private Status status;
    private BigDecimal balance;
    private BigDecimal fee;

    public static Builder from(FundingRecord record) {
      return new Builder()
          .setAddress(record.address)
          .setAddressTag(record.addressTag)
          .setBlockchainTransactionHash(record.blockchainTransactionHash)
          .setDate(record.date)
          .setCurrency(record.currency)
          .setAmount(record.amount)
          .setInternalId(record.internalId)
          .setDescription(record.description)
          .setType(record.type)
          .setStatus(record.status)
          .setBalance(record.balance)
          .setFee(record.fee);
    }

    public Builder setAddress(String address) {
      this.address = address;
      return this;
    }

    public Builder setAddressTag(String addressTag) {
      this.addressTag = addressTag;
      return this;
    }

    public Builder setDate(Date date) {
      this.date = date;
      return this;
    }

    public Builder setCurrency(Currency currency) {
      this.currency = currency;
      return this;
    }

    public Builder setAmount(BigDecimal amount) {
      this.amount = amount;
      return this;
    }

    public Builder setInternalId(String internalId) {
      this.internalId = internalId;
      return this;
    }

    public Builder setBlockchainTransactionHash(String blockchainTransactionHash) {
      this.blockchainTransactionHash = blockchainTransactionHash;
      return this;
    }

    public Builder setDescription(String description) {
      this.description = description;
      return this;
    }

    public Builder setType(Type type) {
      this.type = type;
      return this;
    }

    public Builder setStatus(Status status) {
      this.status = status;
      return this;
    }

    public Builder setBalance(BigDecimal balance) {
      this.balance = balance;
      return this;
    }

    public Builder setFee(BigDecimal fee) {
      this.fee = fee;
      return this;
    }

    public FundingRecord build() {
      return new FundingRecord(
          address,
          addressTag,
          date,
          currency,
          amount,
          internalId,
          blockchainTransactionHash,
          type,
          status,
          balance,
          fee,
          description);
    }
  }
}
