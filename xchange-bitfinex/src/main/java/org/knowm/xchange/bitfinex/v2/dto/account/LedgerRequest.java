package org.knowm.xchange.bitfinex.v2.dto.account;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Value;

@Value
/**
 * 分类帐 /总帐 /分类账 /帐户 请求
 * see https://docs.bitfinex.com/reference#formData-rest-auth-ledgers */
@JsonInclude(Include.NON_NULL)
public class LedgerRequest {
  private Long category;
}
