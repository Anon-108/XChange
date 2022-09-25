package org.knowm.xchange.exceptions;

import org.knowm.xchange.instrument.Instrument;

/**
 * Exception indicating that a request was made with a {@link Instrument} that is not supported on this exchange.
 ** 表示使用此交换不支持的 {@link Instrument} 发出请求的异常。
 * @author bryant_harris
 */
public class InstrumentNotValidException extends ExchangeException {
  private Instrument instrument;

  public InstrumentNotValidException() {
    super("Invalid currency pair for this operation 此操作的货币对无效");
  }

  public InstrumentNotValidException(String message, Throwable cause, Instrument instrument) {
    super(message, cause);
    this.instrument = instrument;
  }

  public InstrumentNotValidException(String message, Throwable cause) {
    super(message, cause);
  }

  public InstrumentNotValidException(String message) {
    super(message);
  }

  public InstrumentNotValidException(String message, Instrument instrument) {
    super(message);
    this.instrument = instrument;
  }

  public InstrumentNotValidException(Throwable cause) {
    super(cause);
  }

  public InstrumentNotValidException(Throwable cause, Instrument instrument) {
    super(instrument + " is not valid for this operation 对此操作无效", cause);
    this.instrument = instrument;
  }

  public InstrumentNotValidException(Instrument instrument) {
    this(instrument + " is not valid for this operation 对此操作无效");
    this.instrument = instrument;
  }

  /** @return The Instrument that caused the exception.
   * 导致异常的仪器。 */
  public Instrument getInstrument() {
    return instrument;
  }
}
