package org.knowm.xchange.bitfinex.v1.dto.account;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;

/**
 * Bitfinex 保证金限制
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({"on_pair", "initial_margin", "margin_requirement", "tradable_balance"})
public class BitfinexMarginLimit {
  /**
   * 配对
   */
  @JsonProperty("on_pair")
  private String onPair;
  /**
   * 初始保证金
   */
  @JsonProperty("initial_margin")
  private BigDecimal initialMargin;
  /**
   * 保证金要求
   */
  @JsonProperty("margin_requirement")
  private BigDecimal marginRequirement;
  /**
   * 可交易余额
   */
  @JsonProperty("tradable_balance")
  private BigDecimal tradableBalance;
  /**
   * 附加属性
   */
  @JsonIgnore private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("on_pair")
  public String getOnPair() {

    return onPair;
  }

  @JsonProperty("on_pair")
  public void setOnPair(String onPair) {

    this.onPair = onPair;
  }

  @JsonProperty("initial_margin")
  public BigDecimal getInitialMargin() {

    return initialMargin;
  }

  @JsonProperty("initial_margin")
  public void setInitialMargin(BigDecimal initialMargin) {

    this.initialMargin = initialMargin;
  }

  @JsonProperty("margin_requirement")
  public BigDecimal getMarginRequirement() {

    return marginRequirement;
  }

  @JsonProperty("margin_requirement")
  public void setMarginRequirement(BigDecimal marginRequirement) {

    this.marginRequirement = marginRequirement;
  }

  @JsonProperty("tradable_balance")
  public BigDecimal getTradableBalance() {

    return tradableBalance;
  }

  @JsonProperty("tradable_balance")
  public void setTradableBalance(BigDecimal tradableBalance) {

    this.tradableBalance = tradableBalance;
  }

  @JsonAnyGetter
  public Map<String, Object> getAdditionalProperties() {

    return this.additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperty(String name, Object value) {

    this.additionalProperties.put(name, value);
  }

  @Override
  public String toString() {
    return "BitfinexMarginLimit{"
        + "onPair='"
        + onPair
        + '\''
        + ", initialMargin="
        + initialMargin
        + ", marginRequirement="
        + marginRequirement
        + ", tradableBalance="
        + tradableBalance
        + '}';
  }
}
