package org.knowm.xchange.exceptions;

import org.knowm.xchange.instrument.Instrument;

/**
 * Exception indicating the {@link Instrument} was recognized by the exchange but their market is suspended - either temporarly or permanently.
 * * 异常表明 {@link Instrument} 已被交易所认可，但其市场暂停 - 无论是暂时还是永久。
 *
 * <p>This exception does not suggest that the entire exhange is down (we have the {@link ExchangeUnavailableException} for that
 * <p>此异常并不表明整个交换已关闭（我们为此设置了 {@link Exchange UnavailableException}
 *
 * @author walec51
 */
public class MarketSuspendedException extends ExchangeException {

  private static final String DEFAULT_MESSAGE = "Market is suspended 市场暂停";

  public MarketSuspendedException() {
    super(DEFAULT_MESSAGE);
  }

  public MarketSuspendedException(String message) {
    super(message);
  }

  public MarketSuspendedException(Throwable cause) {
    super(DEFAULT_MESSAGE, cause);
  }

  public MarketSuspendedException(String message, Throwable cause) {
    super(message, cause);
  }
}
