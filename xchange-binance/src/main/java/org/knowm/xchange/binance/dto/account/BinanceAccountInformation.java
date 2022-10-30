package org.knowm.xchange.binance.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.List;

/**
 * 币安账户信息
 */
public final class BinanceAccountInformation {
  /**
   * 制造者 /制造商 /出票人  佣金 /支付手续费
   */
  public final BigDecimal makerCommission;
  /**
   * 接受者 佣金 /支付手续费
   */
  public final BigDecimal takerCommission;
  /**
   * 买方 /买主 /买家 /买手 佣金 /支付手续费
   */
  public final BigDecimal buyerCommission;
  /**
   * 卖方 /卖家 佣金 /支付手续费
   */
  public final BigDecimal sellerCommission;
  /**
   * can交易
   */
  public final boolean canTrade;
  /**
   *can提款
   */
  public final boolean canWithdraw;
  /**
   * can存款 /押金 /定金 /保证金
   */
  public final boolean canDeposit;
  /**
   * 更新时间
   */
  public final long updateTime;
  /**
   *余额
   */
  public List<BinanceBalance> balances;
  /**
   *许可，权限
   */
  public List<String> permissions;

  /**
   * 币安账户信息
   * @param makerCommission 制造者 /制造商 /出票人  佣金 /支付手续费
   * @param takerCommission 接受者 佣金 /支付手续费
   * @param buyerCommission  买方 /买主 /买家 /买手 佣金 /支付手续费
   * @param sellerCommission  卖方 /卖家 佣金 /支付手续费
   * @param canTrade  can交易
   * @param canWithdraw can提款
   * @param canDeposit  can存款 /押金 /定金 /保证金
   * @param updateTime  更新时间
   * @param balances 余额
   * @param permissions 许可，权限
   */
  public BinanceAccountInformation(
      @JsonProperty("makerCommission") BigDecimal makerCommission,
      @JsonProperty("takerCommission") BigDecimal takerCommission,
      @JsonProperty("buyerCommission") BigDecimal buyerCommission,
      @JsonProperty("sellerCommission") BigDecimal sellerCommission,
      @JsonProperty("canTrade") boolean canTrade,
      @JsonProperty("canWithdraw") boolean canWithdraw,
      @JsonProperty("canDeposit") boolean canDeposit,
      @JsonProperty("updateTime") long updateTime,
      @JsonProperty("balances") List<BinanceBalance> balances,
      @JsonProperty("permissions") List<String> permissions) {
    this.makerCommission = makerCommission;
    this.takerCommission = takerCommission;
    this.buyerCommission = buyerCommission;
    this.sellerCommission = sellerCommission;
    this.canTrade = canTrade;
    this.canWithdraw = canWithdraw;
    this.canDeposit = canDeposit;
    this.updateTime = updateTime;
    this.balances = balances;
    this.permissions = permissions;
  }
}
