package org.knowm.xchange.bitfinex.v2.dto.account;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Value;

/**
 * 移动，转移
 * https://docs.bitfinex.com/reference#rest-auth-movements */
@JsonFormat(shape = JsonFormat.Shape.ARRAY)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Value
public class Movement {
  /** Movement identifier
  * 运动标识符 */
  String id;

  /** The symbol of the currency (ex. "BTC")
   * 货币符号（例如“BTC”）*/
  String curency;

  /** String	The extended name of the currency (ex. "BITCOIN")
   * String 货币的扩展名称（例如“BITCOIN”）*/
  String currencyName;

  /** Movement started at
   * 运动开始于*/
  Date mtsStarted;

  /** Movement last updated at
   * 移动最后更新于*/
  Date mtsUpdated;

  /** Current status  当前状态*/
  String status;

  /** Amount of funds moved (positive for deposits, negative for withdrawals)
   * 转移的资金量（存款为正，取款为负） */
  BigDecimal amount;

  /** Tx Fees applied
   * 申请的 Tx 费用*/
  BigDecimal fees;

  /** Destination address
   * 目的地址*/
  String destinationAddress;

  /** Transaction identifier
   * 交易标识符*/
  String transactionId;

  /**
   nullField parameters are a trick to ignore empty positions in the receiving array
   nullField 参数是忽略接收数组中的空位置的技巧
   Deposit: [[13105603,'ETH','ETHEREUM',null,null,1569348774000,1569348774000,null,null,'COMPLETED',null,null,0.26300954,-0.00135,null,null,'DESTINATION_ADDRESS',null,null,null,'TRANSACTION_ID',null]]
   Withdrawal: [[13293039,'ETH','ETHEREUM',null,null,1574175052000,1574181326000,null,null,'CANCELED',null,null,-0.24,-0.00135,null,null,'DESTINATION_ADDRESS',null,null,null,'TRANSACTION_ID',null]
  */
  @JsonCreator
  public Movement(
      @JsonProperty("ID") final String id,
      @JsonProperty("CURRENCY") final String curency,
      @JsonProperty("CURRENCY_NAME") final String currencyName,
      @JsonProperty("nullField1") final Object nullField1,
      @JsonProperty("nullField2") final Object nullField2,
      @JsonProperty("MTS_STARTED") final Date mtsStarted,
      @JsonProperty("MTS_UPDATED") final Date mtsUpdated,
      @JsonProperty("nullField3") final Object nullField3,
      @JsonProperty("nullField4") final Object nullField4,
      @JsonProperty("STATUS") final String status,
      @JsonProperty("nullField5") final Object nullField5,
      @JsonProperty("nullField6") final Object nullField6,
      @JsonProperty("AMOUNT") final BigDecimal amount,
      @JsonProperty("FEES") final BigDecimal fees,
      @JsonProperty("nullField7") final Object nullField7,
      @JsonProperty("nullField8") final Object nullField8,
      @JsonProperty("DESTINATION_ADDRESS") final String destinationAddress,
      @JsonProperty("nullField9") final Object nullField9,
      @JsonProperty("nullField10") final Object nullField10,
      @JsonProperty("nullField11") final Object nullField11,
      @JsonProperty("TRANSACTION_ID") final String transactionId,
      @JsonProperty("nullField12") final Object nullField12) {
    this.id = id;
    this.curency = curency;
    this.currencyName = currencyName;
    this.mtsStarted = mtsStarted;
    this.mtsUpdated = mtsUpdated;
    this.status = status;
    this.amount = amount;
    this.fees = fees;
    this.destinationAddress = destinationAddress;
    this.transactionId = transactionId;
  }
}
