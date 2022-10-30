package org.knowm.xchange.binance.service.account;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;
import org.junit.Rule;
import org.junit.Test;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.binance.BinanceExchange;
import org.knowm.xchange.binance.service.BinanceHmacDigest;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.exceptions.ExchangeSecurityException;
import org.knowm.xchange.service.account.AccountService;
import si.mazi.rescu.ParamsDigest;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * 账户服务测试
 */
public class AccountServiceTest {
  /**
   * 线模拟规则
   */
  @Rule public final WireMockRule wireMockRule = new WireMockRule();

  /**
   * 提取成功
   * @throws Exception
   */
  @Test(timeout = 2000)
  public void withdrawSuccess() throws Exception {
    String response = withdraw("withdraw-200.json", 200);
    assertThat(response).isEqualTo("9c7662xxxxxxxxxc8bd");
  }

  /**
   * 提取失败
   */
  @Test(timeout = 2000)
  public void withdrawFailure() {
    Throwable exception = catchThrowable(() -> withdraw("withdraw-400.json", 400));
    assertThat(exception)
        .isInstanceOf(ExchangeSecurityException.class)
        .hasMessage("error message (HTTP status code: 400)");
  }

  /**
   * 提取
   * @param responseFileName
   * @param statusCode
   * @return
   * @throws IOException
   */
  private String withdraw(String responseFileName, int statusCode) throws IOException {
    BinanceExchange exchange = createExchange();
    AccountService service = exchange.getAccountService();
    stubWithdraw(responseFileName, statusCode);

    return service.withdrawFunds(
        Currency.BTC, BigDecimal.TEN, "1A1zP1eP5QGefi2DMPTfTL5SLmv7DivfNa");
  }

  /**
   * . 存根 提取
   * @param fileName 文件名
   * @param statusCode 状态码
   */
  private void stubWithdraw(String fileName, int statusCode) {
    stubFor(
        post(urlPathEqualTo("/sapi/v1/capital/withdraw/apply"))
            .willReturn(
                aResponse()
                    .withStatus(statusCode)
                    .withHeader("Content-Type", "application/json")
                    .withBodyFile(fileName)));
  }

  private BinanceExchange createExchange() {
    BinanceExchange exchange =
        ExchangeFactory.INSTANCE.createExchangeWithoutSpecification(BinanceExchange.class);
    ExchangeSpecification specification = exchange.getDefaultExchangeSpecification();
    specification.setHost("localhost");
    specification.setSslUri("http://localhost:" + wireMockRule.port() + "/");
    specification.setPort(wireMockRule.port());
    specification.setShouldLoadRemoteMetaData(false);
    specification.setHttpReadTimeout(1000);
    exchange.applySpecification(specification);
    return exchange;
  }
}
