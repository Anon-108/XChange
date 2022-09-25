package org.knowm.xchange.service;

import java.math.BigDecimal;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.meta.CurrencyPairMetaData;
import org.knowm.xchange.dto.meta.ExchangeMetaData;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.MarketOrder;

/** Top of the hierarchy abstract class for an "exchange service"
 * “交换服务”的层次结构抽象类的顶部 */
public abstract class BaseExchangeService<E extends Exchange> {

  /**
   * The base Exchange. Every service has access to the containing exchange class, which hold meta data and the exchange specification
   * * 基础交易所。 每个服务都可以访问包含的交换类，该类包含元数据和交换规范
   */
  protected final E exchange;

  /** Constructor */
  protected BaseExchangeService(E exchange) {

    this.exchange = exchange;
  }

  public void verifyOrder(LimitOrder limitOrder) {

    ExchangeMetaData exchangeMetaData = exchange.getExchangeMetaData();
    verifyOrder(limitOrder, exchangeMetaData);
    BigDecimal price = limitOrder.getLimitPrice().stripTrailingZeros();

    if (price.scale()
        > exchangeMetaData.getCurrencyPairs().get(limitOrder.getCurrencyPair()).getPriceScale()) {
      throw new IllegalArgumentException("Unsupported price scale 不支持的价格范围" + price.scale());
    }
  }

  public void verifyOrder(MarketOrder marketOrder) {

    verifyOrder(marketOrder, exchange.getExchangeMetaData());
  }

  protected final void verifyOrder(Order order, ExchangeMetaData exchangeMetaData) {

    CurrencyPairMetaData metaData =
        exchangeMetaData.getCurrencyPairs().get(order.getCurrencyPair());
    if (metaData == null) {
      throw new IllegalArgumentException("Invalid CurrencyPair 无效货币对");
    }

    BigDecimal originalAmount = order.getOriginalAmount();
    if (originalAmount == null) {
      throw new IllegalArgumentException("Missing originalAmount 缺少原始金额");
    }

    BigDecimal amount = originalAmount.stripTrailingZeros();
    BigDecimal minimumAmount = metaData.getMinimumAmount();
    if (minimumAmount != null) {
      if (amount.scale() > minimumAmount.scale()) {
        throw new IllegalArgumentException("Unsupported amount scale 不支持的金额规模" + amount.scale());
      } else if (amount.compareTo(minimumAmount) < 0) {
        throw new IllegalArgumentException("Order amount less than minimum 订单金额小于最低金额");
      }
    }
  }
}
