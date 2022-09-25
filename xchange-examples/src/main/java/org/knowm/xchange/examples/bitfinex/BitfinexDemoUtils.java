package org.knowm.xchange.examples.bitfinex;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.bitfinex.BitfinexExchange;

public class BitfinexDemoUtils {

  public static Exchange createExchange() {

    // Use the factory to get BFX exchange API using default settings
    // 使用出厂设置获取 BFX 交易所 API 使用默认设置
    Exchange bfx = ExchangeFactory.INSTANCE.createExchange(BitfinexExchange.class);

    ExchangeSpecification bfxSpec = bfx.getDefaultExchangeSpecification();

    bfxSpec.setApiKey("1ObRGaVgwfcaYwebE0ave1ACiFzQs51evimKHCjEBj3");
    bfxSpec.setSecretKey("WLYzLXkgbmr3RnKx2OxHqVUVWyyKM9kK6IlrEDflEh8");

    bfx.applySpecification(bfxSpec);

    return bfx;
  }
}
