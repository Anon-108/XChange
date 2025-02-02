package org.knowm.xchange.bitfinex.service;

import org.knowm.xchange.bitfinex.BitfinexExchange;
import org.knowm.xchange.bitfinex.v1.BitfinexAuthenticated;
import org.knowm.xchange.bitfinex.v1.BitfinexDigest;
import org.knowm.xchange.bitfinex.v2.BitfinexHmacSignature;
import org.knowm.xchange.client.ExchangeRestProxyBuilder;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.service.BaseResilientExchangeService;
import org.knowm.xchange.service.BaseService;
import si.mazi.rescu.ParamsDigest;

/**
 * Bitfinex基础服务
 */
public class BitfinexBaseService extends BaseResilientExchangeService<BitfinexExchange>
    implements BaseService {

  protected final String apiKey;
  protected final BitfinexAuthenticated bitfinex;
  /**
   * 签名创作者
   */
  protected final ParamsDigest signatureCreator;
  /**
   * 有效载荷创建者
   */
  protected final ParamsDigest payloadCreator;

  protected final org.knowm.xchange.bitfinex.v2.BitfinexAuthenticated bitfinexV2;
  /**
   * 签名V2
   */
  protected final BitfinexHmacSignature signatureV2;

  /**
   * Constructor
   *
   * @param exchange
   */
  public BitfinexBaseService(BitfinexExchange exchange, ResilienceRegistries resilienceRegistries) {

    super(exchange, resilienceRegistries);

    this.bitfinex =
        ExchangeRestProxyBuilder.forInterface(
                BitfinexAuthenticated.class, exchange.getExchangeSpecification())
            .build();
    this.apiKey = exchange.getExchangeSpecification().getApiKey();
    this.signatureCreator =
        BitfinexDigest.createInstance(exchange.getExchangeSpecification().getSecretKey());
    this.payloadCreator = new BitfinexPayloadDigest();

    this.bitfinexV2 =
        ExchangeRestProxyBuilder.forInterface(
                org.knowm.xchange.bitfinex.v2.BitfinexAuthenticated.class,
                exchange.getExchangeSpecification())
            .build();
    this.signatureV2 =
        BitfinexHmacSignature.createInstance(exchange.getExchangeSpecification().getSecretKey());
  }
}
