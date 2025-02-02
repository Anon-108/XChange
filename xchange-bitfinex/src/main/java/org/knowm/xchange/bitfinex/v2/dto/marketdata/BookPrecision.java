package org.knowm.xchange.bitfinex.v2.dto.marketdata;

/**
 * 账簿精度
 * Level of price aggregation Precision Level Number of significant figures P0 5 P1 4 P2 3 P3 2 P4 1
 * 价格聚合水平 精度水平 有效数字数量 P0 5 P1 4 P2 3 P3 2 P4 1
 */
public enum BookPrecision {
  P0,
  P1,
  P2,
  P3,
  P4;
}
