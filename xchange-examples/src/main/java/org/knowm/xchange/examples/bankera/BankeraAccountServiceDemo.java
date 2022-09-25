package org.knowm.xchange.examples.bankera;

import java.io.IOException;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.service.account.AccountService;

public class BankeraAccountServiceDemo {
  public static void main(String[] args) throws IOException {
    Exchange exchange = BankeraDemoUtils.createExchange();
    AccountService accountService = exchange.getAccountService();

    // Get the account information
    // 获取账户信息
    AccountInfo accountInfo = accountService.getAccountInfo();
    System.out.println("AccountInfo as String:AccountInfo 作为字符串： " + accountInfo.toString());
  }
}
