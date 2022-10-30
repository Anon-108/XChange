package org.knowm.xchange.binance.dto.marketdata;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.BiConsumer;

/**
 * 币安订单簿
 */
public final class BinanceOrderbook {
    /**
     * 最后更新Id
     */
  public final long lastUpdateId;
    /**
     * 出价，喊价
     */
  public final SortedMap<BigDecimal, BigDecimal> bids;
    /**
     * 卖/喊出价
     */
  public final SortedMap<BigDecimal, BigDecimal> asks;

    /**
     * 币安订单簿
     * @param lastUpdateId 最后更新Id
     * @param bidsJson 出价，喊价json
     * @param asksJson 卖/喊出价json
     */
  public BinanceOrderbook(
      @JsonProperty("lastUpdateId") long lastUpdateId,
      @JsonProperty("bids") List<Object[]> bidsJson,
      @JsonProperty("asks") List<Object[]> asksJson) {
    this.lastUpdateId = lastUpdateId;
    BiConsumer<Object[], Map<BigDecimal, BigDecimal>> entryProcessor =
        (obj, col) -> {
          BigDecimal price = new BigDecimal(obj[0].toString());
          BigDecimal qty = new BigDecimal(obj[1].toString());
          col.put(price, qty);
        };

    TreeMap<BigDecimal, BigDecimal> bids = new TreeMap<>((k1, k2) -> -k1.compareTo(k2));
    TreeMap<BigDecimal, BigDecimal> asks = new TreeMap<>();

    bidsJson.stream().forEach(e -> entryProcessor.accept(e, bids));
    asksJson.stream().forEach(e -> entryProcessor.accept(e, asks));

    this.bids = Collections.unmodifiableSortedMap(bids);
    this.asks = Collections.unmodifiableSortedMap(asks);
  }
}
