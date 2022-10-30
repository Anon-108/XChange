package org.knowm.xchange.derivative;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.instrument.Instrument;

/**
 * 期货合约
 */
public class FuturesContract extends Instrument
    implements Derivative, Comparable<FuturesContract>, Serializable {

  private static final long serialVersionUID = 6876906648149216819L;
  /**
   * 比较仪；比测仪
   */
  private static final Comparator<FuturesContract> COMPARATOR =
      Comparator.comparing(FuturesContract::getCurrencyPair)
          .thenComparing(FuturesContract::getPrompt);

  /**
   * 货币对
   * The CurrencyPair the FuturesContract is based upon
   * 期货合约所基于的货币对 */
  private final CurrencyPair currencyPair;

  /**
   *提示 /温馨提示 /提示符
   * The Date when the FuturesContract expires, when null it is perpetual
   * FuturesContract 到期的日期，如果为 null，则为永久*/
  private final String prompt;

  /**
   * 期货合约
   * @param currencyPair 货币对
   * @param prompt  提示  /温馨提示 /提示符
   */
  public FuturesContract(CurrencyPair currencyPair, String prompt) {
    this.currencyPair = currencyPair;
    this.prompt = prompt;
  }

  /**
   * 期货合约
   * @param symbol
   */
  @JsonCreator
  public FuturesContract(final String symbol) {
    String[] parts = symbol.split("/");
    if (parts.length < 3) {
      throw new IllegalArgumentException("Could not parse futures contract from  无法解析期货合约'" + symbol + "'");
    }

    String base = parts[0];
    String counter = parts[1];

    String prompt = parts[2];
    this.currencyPair = new CurrencyPair(base, counter);

    this.prompt = prompt;
  }

  @Override
  public CurrencyPair getCurrencyPair() {
    return currencyPair;
  }

  public String getPrompt() {
    return prompt;
  }

  public boolean isPerpetual() {
    return this.prompt.matches("(?i)PERP|SWAP|PERPETUAL");
  }

  @Override
  public int compareTo(final FuturesContract that) {
    return COMPARATOR.compare(this, that);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    final FuturesContract contract = (FuturesContract) o;
    return Objects.equals(currencyPair, contract.currencyPair)
        && Objects.equals(prompt, contract.prompt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(currencyPair, prompt);
  }

  @JsonValue
  @Override
  public String toString() {

    return currencyPair + "/" + prompt;
  }
}
