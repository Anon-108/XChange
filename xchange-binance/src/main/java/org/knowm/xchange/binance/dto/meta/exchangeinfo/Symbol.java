package org.knowm.xchange.binance.dto.meta.exchangeinfo;

/**
 * 符号/股票代码
 */
public class Symbol {
  /**
   *  报价 /引用 资产
   */
  private String quoteAsset;
  /**
   * 冰山允许
   */
  private String icebergAllowed;
  /**
   * oco 允许
   */
  private String ocoAllowed;
  /**
   * 是否允许保证金/融资融券交易
   */
  private String isMarginTradingAllowed;
  /**
   * 是否允许现货交易
   */
  private String isSpotTradingAllowed;

  /**
   * 基础资产
   */
  private String baseAsset;
  /**
   *  符号/股票代码
   */
  private String symbol;
  /**
   * 状态
   */
  private String status;

  /**
   * 报价精度
   */
  private String quotePrecision;

  /**
   * 报价资产精度
   */
  private String quoteAssetPrecision;
  /**
   * 基础资产精度
   */
  private String baseAssetPrecision;
  /**
   * 订单类型
   */
  private String[] orderTypes;
  /**
   * 过滤器
   */
  private Filter[] filters;

  /**
   * 许可，权限
   */
  private String[] permissions;

  public String getQuoteAsset() {
    return quoteAsset;
  }

  public void setQuoteAsset(String quoteAsset) {
    this.quoteAsset = quoteAsset;
  }

  public String getIcebergAllowed() {
    return icebergAllowed;
  }

  public void setIcebergAllowed(String icebergAllowed) {
    this.icebergAllowed = icebergAllowed;
  }

  public String getOcoAllowed() {
    return ocoAllowed;
  }

  public void setOcoAllowed(String ocoAllowed) {
    this.ocoAllowed = ocoAllowed;
  }

  public String getIsMarginTradingAllowed() {
    return isMarginTradingAllowed;
  }

  public void setIsMarginTradingAllowed(String isMarginTradingAllowed) {
    this.isMarginTradingAllowed = isMarginTradingAllowed;
  }

  public String getIsSpotTradingAllowed() {
    return isSpotTradingAllowed;
  }

  public void setIsSpotTradingAllowed(String isSpotTradingAllowed) {
    this.isSpotTradingAllowed = isSpotTradingAllowed;
  }

  public String getBaseAsset() {
    return baseAsset;
  }

  public void setBaseAsset(String baseAsset) {
    this.baseAsset = baseAsset;
  }

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getQuotePrecision() {
    return quotePrecision;
  }

  public void setQuotePrecision(String quotePrecision) {
    this.quotePrecision = quotePrecision;
  }

  public String getQuoteAssetPrecision() {
    return quoteAssetPrecision;
  }

  public void setQuoteAssetPrecision(String quoteAssetPrecision) {
    this.quoteAssetPrecision = quoteAssetPrecision;
  }

  public String getBaseAssetPrecision() {
    return baseAssetPrecision;
  }

  public void setBaseAssetPrecision(String baseAssetPrecision) {
    this.baseAssetPrecision = baseAssetPrecision;
  }

  public String[] getOrderTypes() {
    return orderTypes;
  }

  public void setOrderTypes(String[] orderTypes) {
    this.orderTypes = orderTypes;
  }

  public Filter[] getFilters() {
    return filters;
  }

  public void setFilters(Filter[] filters) {
    this.filters = filters;
  }

  public String[] getPermissions() {
    return permissions;
  }

  public void setPermissions(String[] permissions) {
    this.permissions = permissions;
  }

  @Override
  public String toString() {
    return "ClassPojo [quoteAsset = "
        + quoteAsset
        + ", icebergAllowed = "
        + icebergAllowed
        + ", baseAsset = "
        + baseAsset
        + ", symbol = "
        + symbol
        + ", status = "
        + status
        + ", quotePrecision = "
        + quotePrecision
        + ", quoteAssetPrecision = "
        + quoteAssetPrecision
        + ", baseAssetPrecision = "
        + baseAssetPrecision
        + ", orderTypes = "
        + orderTypes
        + ", filters = "
        + filters
        + ", permissions = "
        + permissions
        + "]";
  }
}
