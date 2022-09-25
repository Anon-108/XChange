package org.knowm.xchange.bitfinex.v2.dto.account;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;

/** see https://docs.bitfinex.com/reference#rest-auth-transfer */
@JsonFormat(shape = JsonFormat.Shape.ARRAY)
@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class TransferBetweenWalletsResponse {
  /** Millisecond Time Stamp of the update
   * 更新的毫秒时间戳*/
  private long timestamp;
  /** acc_tf */
  private String type;
  /** unique ID of the message
   * 消息的唯一 ID*/
  private Long messageId;

  private Object placeHolder0;
  private Transfer transfer;

  /** Work in progress
   * 工作正在进行中*/
  private Integer code;
  /** Status of the notification; it may vary over time (SUCCESS, ERROR, FAILURE, ...)
   * 通知状态； 它可能会随着时间而变化（成功、错误、失败……）*/
  private String status;
  /** Text of the notification
   * 通知文本 */
  private String text;

  public Date getTimestamp() {
    return new Date(timestamp);
  }

  @JsonFormat(shape = JsonFormat.Shape.ARRAY)
  @Value
  @NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
  public static class Transfer {
    /** Millisecond Time Stamp when the transfer was created
     * 创建传输时的毫秒时间戳 */
    private long timestamp;
    /** Starting wallet
     * 起始钱包 */
    private String walletFrom;
    /** Destination wallet
     * 目的地钱包*/
    private String walletTo;

    private Object placeHolder0;
    /** Currency 货币*/
    private String currency;
    /** Currency converted to
     * 货币转换为 */
    private String currencyTo;

    private Object placeHolder1;
    /** Amount of Transfer
     * 转账金额 */
    private BigDecimal amount;

    public Date getTimestamp() {
      return new Date(timestamp);
    }
  }
}
