package org.knowm.xchange.bitfinex.v1.dto.marketdata;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;

/**
 * Bitfinex 深度
 */
public class BitfinexDepth {

  private final BitfinexLevel[] asks;
  private final BitfinexLevel[] bids;

  /**
   * Constructor
   *
   * @param asks
   * @param bids
   */
  public BitfinexDepth(
      @JsonProperty("asks") BitfinexLevel[] asks, @JsonProperty("bids") BitfinexLevel[] bids) {

    this.asks = asks;
    this.bids = bids;
  }

  public BitfinexLevel[] getAsks() {

    return asks;
  }

  public BitfinexLevel[] getBids() {

    return bids;
  }

  @Override
  public String toString() {

    return "BitfinexDepth [asks=" + Arrays.toString(asks) + ", bids=" + Arrays.toString(bids) + "]";
  }
}
