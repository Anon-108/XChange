package org.knowm.xchange.bitfinex.v2.dto.account;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 分类帐输入
 * https://docs.bitfinex.com/reference#rest-auth-ledgers */
@JsonFormat(shape = JsonFormat.Shape.ARRAY)
@Data
public class LedgerEntry {

  /** Ledger identifier
   * 分类帐标识符*/
  private long id;
  /** The symbol of the currency (ex. "BTC")
   * 货币符号（例如“BTC”） */
  private String currency;

  private Object placeHolder0;
  /** Timestamp in milliseconds
   * 以毫秒为单位的时间戳*/
  private long timestamp;

  private Object placeHolder1;
  /** Amount of funds moved
   * 转移的资金数额*/
  private BigDecimal amount;
  /** New balance  新 余额*/
  private BigDecimal balance;

  private Object placeHolder2;
  /** Description of ledger transaction
   * 账本交易说明 */
  private String description;

  public Date getTimestamp() {
    return new Date(timestamp);
  }
}
