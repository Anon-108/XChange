package org.knowm.xchange.binance.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 存款地址
 */
public final class DepositAddress {
  /**
   * 地址
   */
  public String address;
  /**
   * 链接 /地址
   */
  public String url;
  /**
   * 地址标签
   */
  public String addressTag;
  /**
   * 资产
   */
  public String asset;

  /**
   * 存款地址
   * @param address 地址
   * @param url 链接 /地址
   * @param addressTag 地址标签
   * @param asset 资产
   */
  public DepositAddress(
      @JsonProperty("address") String address,
      @JsonProperty("url") String url,
      @JsonProperty("tag") String addressTag,
      @JsonProperty("coin") String asset) {
    this.address = address;
    this.url = url;
    this.addressTag = addressTag;
    this.asset = asset;
  }
}
