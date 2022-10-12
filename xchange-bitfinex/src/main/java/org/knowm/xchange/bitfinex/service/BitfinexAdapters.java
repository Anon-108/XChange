package org.knowm.xchange.bitfinex.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.knowm.xchange.bitfinex.v1.BitfinexOrderType;
import org.knowm.xchange.bitfinex.v1.BitfinexUtils;
import org.knowm.xchange.bitfinex.v1.dto.account.BitfinexAccountFeesResponse;
import org.knowm.xchange.bitfinex.v1.dto.account.BitfinexBalancesResponse;
import org.knowm.xchange.bitfinex.v1.dto.account.BitfinexDepositWithdrawalHistoryResponse;
import org.knowm.xchange.bitfinex.v1.dto.account.BitfinexTradingFeeResponse;
import org.knowm.xchange.bitfinex.v1.dto.marketdata.BitfinexDepth;
import org.knowm.xchange.bitfinex.v1.dto.marketdata.BitfinexLendLevel;
import org.knowm.xchange.bitfinex.v1.dto.marketdata.BitfinexLevel;
import org.knowm.xchange.bitfinex.v1.dto.marketdata.BitfinexSymbolDetail;
import org.knowm.xchange.bitfinex.v1.dto.marketdata.BitfinexTicker;
import org.knowm.xchange.bitfinex.v1.dto.marketdata.BitfinexTrade;
import org.knowm.xchange.bitfinex.v1.dto.trade.BitfinexAccountInfosResponse;
import org.knowm.xchange.bitfinex.v1.dto.trade.BitfinexOrderFlags;
import org.knowm.xchange.bitfinex.v1.dto.trade.BitfinexOrderStatusResponse;
import org.knowm.xchange.bitfinex.v1.dto.trade.BitfinexTradeResponse;
import org.knowm.xchange.bitfinex.v2.dto.account.Movement;
import org.knowm.xchange.bitfinex.v2.dto.marketdata.BitfinexPublicTrade;
import org.knowm.xchange.bitfinex.v2.dto.marketdata.BitfinexTickerFundingCurrency;
import org.knowm.xchange.bitfinex.v2.dto.marketdata.BitfinexTickerTraidingPair;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.Order.OrderStatus;
import org.knowm.xchange.dto.Order.OrderType;
import org.knowm.xchange.dto.account.Balance;
import org.knowm.xchange.dto.account.Fee;
import org.knowm.xchange.dto.account.FundingRecord;
import org.knowm.xchange.dto.account.Wallet;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.marketdata.Trade;
import org.knowm.xchange.dto.marketdata.Trades;
import org.knowm.xchange.dto.marketdata.Trades.TradeSortType;
import org.knowm.xchange.dto.meta.CurrencyMetaData;
import org.knowm.xchange.dto.meta.CurrencyPairMetaData;
import org.knowm.xchange.dto.meta.ExchangeMetaData;
import org.knowm.xchange.dto.meta.WalletHealth;
import org.knowm.xchange.dto.trade.FixedRateLoanOrder;
import org.knowm.xchange.dto.trade.FloatingRateLoanOrder;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.MarketOrder;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.knowm.xchange.dto.trade.StopOrder;
import org.knowm.xchange.dto.trade.UserTrade;
import org.knowm.xchange.dto.trade.UserTrades;
import org.knowm.xchange.utils.DateUtils;
import org.knowm.xchange.utils.jackson.CurrencyPairDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Bitfinex适配器
 */
public final class BitfinexAdapters {

  public static final Logger log = LoggerFactory.getLogger(BitfinexAdapters.class);
  private static final ObjectMapper mapper = new ObjectMapper();
  /**
   * 警告停止限制
   */
  private static final AtomicBoolean warnedStopLimit = new AtomicBoolean();
  private static final String USDT_SYMBOL_BITFINEX = "UST";
  private static final String USDT_SYMBOL_XCHANGE = "USDT";
  private static final int PLATFORM_STATUS_ONLINE = 1;
  private static final int PLATFORM_STATUS_OFFLINE = 0;

  private BitfinexAdapters() {}

  /**
   * 适应/适配/调整动态交易费用
   *
   * Each element in the response array contains a set of currencies that are at a given fee tier.
   * 响应数组中的每个元素都包含一组处于给定费用等级的货币。
   * The API returns the fee per currency in each tier and does not make any promises that they are all the same, so this adapter will use the fee per currency instead of the fee per tier.
   * * API 返回每个层级的每个货币的费用，并且不承诺它们都是相同的，所以这个适配器将使用每个货币的费用而不是每个层级的费用。
   */
  public static Map<CurrencyPair, Fee> adaptDynamicTradingFees(
      BitfinexTradingFeeResponse[] responses, List<CurrencyPair> currencyPairs) {
    Map<CurrencyPair, Fee> result = new HashMap<>();
    for (BitfinexTradingFeeResponse response : responses) {
      BitfinexTradingFeeResponse.BitfinexTradingFeeResponseRow[] responseRows =
          response.getTradingFees();
      for (BitfinexTradingFeeResponse.BitfinexTradingFeeResponseRow responseRow : responseRows) {
        Currency currency = Currency.getInstance(responseRow.getCurrency());
        BigDecimal percentToFraction = BigDecimal.ONE.divide(BigDecimal.ONE.scaleByPowerOfTen(2));
        Fee fee =
            new Fee(
                responseRow.getMakerFee().multiply(percentToFraction),
                responseRow.getTakerFee().multiply(percentToFraction));
        for (CurrencyPair pair : currencyPairs) {
          // Fee to trade for a currency is the fee to trade currency pairs with this base.
          //交易货币的费用是用此基础交易货币对的费用。
          // Fee is typically assessed in units counter.
          // 费用通常以单位计数器进行评估。
          if (pair.base.equals(currency)) {
            if (result.put(pair, fee) != null) {
              throw new IllegalStateException(
                  "Fee for currency pair 货币对费用" + pair + " is overspecified 被过度指定");
            }
          }
        }
      }
    }
    return result;
  }

  /**
   * 适应/适配/调整Bitfinex货币
   * @param bitfinexSymbol
   * @return
   */
  public static String adaptBitfinexCurrency(String bitfinexSymbol) {
    String result = bitfinexSymbol.toUpperCase();
    if (USDT_SYMBOL_BITFINEX.equals(result)) {
      result = USDT_SYMBOL_XCHANGE;
    }
    return result;
  }

  /**
   * 适应/适配/调整订单类型
   * @param type
   * @return
   */
  public static String adaptOrderType(OrderType type) {
    switch (type) {
      case BID:
      case EXIT_BID:
        return "buy";
      case ASK:
      case EXIT_ASK:
        return "sell";
    }

    throw new IllegalArgumentException(String.format("Unexpected type of order 意外的订单类型: %s", type));
  }

  /**
   * 调整/适应/适配订单标志的类型
   * @param flags 标志
   * @return
   */
  public static BitfinexOrderType adaptOrderFlagsToType(Set<Order.IOrderFlags> flags) {
    if (flags.contains(BitfinexOrderFlags.MARGIN)) {
      if (flags.contains(BitfinexOrderFlags.FILL_OR_KILL)) {
        return BitfinexOrderType.MARGIN_FILL_OR_KILL;
      } else if (flags.contains(BitfinexOrderFlags.TRAILING_STOP)) {
        return BitfinexOrderType.MARGIN_TRAILING_STOP;
      } else if (flags.contains(BitfinexOrderFlags.STOP)) {
        return BitfinexOrderType.MARGIN_STOP;
      } else {
        return BitfinexOrderType.MARGIN_LIMIT;
      }
    } else {
      if (flags.contains(BitfinexOrderFlags.FILL_OR_KILL)) {
        return BitfinexOrderType.FILL_OR_KILL;
      } else if (flags.contains(BitfinexOrderFlags.TRAILING_STOP)) {
        return BitfinexOrderType.TRAILING_STOP;
      } else if (flags.contains(BitfinexOrderFlags.STOP)) {
        return BitfinexOrderType.STOP;
      } else {
        return BitfinexOrderType.LIMIT;
      }
    }
  }

  /**
   * 调整/适应/适配货币对
   * @param bitfinexSymbol bitfinex 符号
   * @return
   */
  public static CurrencyPair adaptCurrencyPair(String bitfinexSymbol) {
    String tradableIdentifier;
    String transactionCurrency;
    int startIndex =
        bitfinexSymbol.startsWith("t") && Character.isUpperCase(bitfinexSymbol.charAt(1)) ? 1 : 0;
    if (bitfinexSymbol.contains(":")) {
      // ie 'dusk:usd' or 'btc:cnht'
      // 即 'dusk:usd' 或 'btc:cnht'
      int idx = bitfinexSymbol.indexOf(":");
      tradableIdentifier = bitfinexSymbol.substring(startIndex, idx);
      transactionCurrency = bitfinexSymbol.substring(idx + 1);
    } else {
      tradableIdentifier = bitfinexSymbol.substring(startIndex, startIndex + 3);
      transactionCurrency = bitfinexSymbol.substring(startIndex + 3);
    }

    return new CurrencyPair(
        adaptBitfinexCurrency(tradableIdentifier), adaptBitfinexCurrency(transactionCurrency));
  }

  /**
   * 适应/适配/调整 订单状态
   * @param order
   * @return
   */
  public static OrderStatus adaptOrderStatus(BitfinexOrderStatusResponse order) {

    if (order.isCancelled()) return OrderStatus.CANCELED;
    else if (order.getExecutedAmount().compareTo(BigDecimal.ZERO) == 0) return OrderStatus.NEW;
    else if (order.getExecutedAmount().compareTo(order.getOriginalAmount()) < 0)
      return OrderStatus.PARTIALLY_FILLED;
    else if (order.getExecutedAmount().compareTo(order.getOriginalAmount()) == 0)
      return OrderStatus.FILLED;
    else return null;
  }

  /**
   * 适应/适配/调整 货币对
   * @param pair 对
   * @return
   */
  public static String adaptCurrencyPair(CurrencyPair pair) {
    return BitfinexUtils.toPairString(pair);
  }

  /**
   * 适应/适配/调整 订单簿
   * @param btceDepth 比特币深度
   * @param currencyPair 货币对
   * @return
   */
  public static OrderBook adaptOrderBook(BitfinexDepth btceDepth, CurrencyPair currencyPair) {

    OrdersContainer asksOrdersContainer =
        adaptOrders(btceDepth.getAsks(), currencyPair, OrderType.ASK);
    OrdersContainer bidsOrdersContainer =
        adaptOrders(btceDepth.getBids(), currencyPair, OrderType.BID);

    return new OrderBook(
        new Date(Math.max(asksOrdersContainer.getTimestamp(), bidsOrdersContainer.getTimestamp())),
        asksOrdersContainer.getLimitOrders(),
        bidsOrdersContainer.getLimitOrders());
  }

  /**
   * 适应/适配/调整订单
   * @param bitfinexLevels bitfinex 级别
   * @param currencyPair 货币对
   * @param orderType 订单类型
   * @return
   */
  public static OrdersContainer adaptOrders(
      BitfinexLevel[] bitfinexLevels, CurrencyPair currencyPair, OrderType orderType) {

    BigDecimal maxTimestamp = new BigDecimal(Long.MIN_VALUE);
    List<LimitOrder> limitOrders = new ArrayList<>(bitfinexLevels.length);

    for (BitfinexLevel bitfinexLevel : bitfinexLevels) {
      if (bitfinexLevel.getTimestamp().compareTo(maxTimestamp) > 0) {
        maxTimestamp = bitfinexLevel.getTimestamp();
      }

      Date timestamp = convertBigDecimalTimestampToDate(bitfinexLevel.getTimestamp());
      limitOrders.add(
          adaptOrder(
              bitfinexLevel.getAmount(),
              bitfinexLevel.getPrice(),
              currencyPair,
              orderType,
              timestamp));
    }

    long maxTimestampInMillis = maxTimestamp.multiply(new BigDecimal(1000L)).longValue();
    return new OrdersContainer(maxTimestampInMillis, limitOrders);
  }

  /**
   * 适应/适配/调整订单
   * @param originalAmount 原始数量
   * @param price 价格
   * @param currencyPair 货币对
   * @param orderType 订单类型
   * @param timestamp 时间戳
   * @return
   */
  public static LimitOrder adaptOrder(
      BigDecimal originalAmount,
      BigDecimal price,
      CurrencyPair currencyPair,
      OrderType orderType,
      Date timestamp) {

    return new LimitOrder(orderType, originalAmount, currencyPair, "", timestamp, price);
  }

  /**
   * 调整固定利率贷款订单
   * @param orders 订单
   * @param currency 货币
   * @param orderType 订单类型
   * @param id
   * @return
   */
  public static List<FixedRateLoanOrder> adaptFixedRateLoanOrders(
      BitfinexLendLevel[] orders, String currency, String orderType, String id) {

    List<FixedRateLoanOrder> loanOrders = new ArrayList<>(orders.length);

    for (BitfinexLendLevel order : orders) {
      if ("yes".equalsIgnoreCase(order.getFrr())) {
        continue;
      }

      // Bid orderbook is reversed order. Insert at reversed indices
      // 投标订单簿是相反的顺序。 在反向索引处插入
      if (orderType.equalsIgnoreCase("loan")) {
        loanOrders.add(
            0,
            adaptFixedRateLoanOrder(
                currency, order.getAmount(), order.getPeriod(), orderType, id, order.getRate()));
      } else {
        loanOrders.add(
            adaptFixedRateLoanOrder(
                currency, order.getAmount(), order.getPeriod(), orderType, id, order.getRate()));
      }
    }

    return loanOrders;
  }

  /**
   * 调整固定利率贷款订单
   * @param currency 货币
   * @param amount 数量
   * @param dayPeriod 天 期间
   * @param direction 方向
   * @param id
   * @param rate 比例
   * @return
   */
  public static FixedRateLoanOrder adaptFixedRateLoanOrder(
      String currency,
      BigDecimal amount,
      int dayPeriod,
      String direction,
      String id,
      BigDecimal rate) {

    OrderType orderType = direction.equalsIgnoreCase("loan") ? OrderType.BID : OrderType.ASK;

    return new FixedRateLoanOrder(orderType, currency, amount, dayPeriod, id, null, rate);
  }

  /**
   * 调整浮动利率贷款订单
   * @param orders 订单
   * @param currency 货币
   * @param orderType 订单类型
   * @param id
   * @return
   */
  public static List<FloatingRateLoanOrder> adaptFloatingRateLoanOrders(
      BitfinexLendLevel[] orders, String currency, String orderType, String id) {

    List<FloatingRateLoanOrder> loanOrders = new ArrayList<>(orders.length);

    for (BitfinexLendLevel order : orders) {
      if ("no".equals(order.getFrr())) {
        continue;
      }

      // Bid orderbook is reversed order. Insert at reversed indices
      // 投标订单簿是相反的顺序。 在反向索引处插入
      if (orderType.equalsIgnoreCase("loan")) {
        loanOrders.add(
            0,
            adaptFloatingRateLoanOrder(
                currency, order.getAmount(), order.getPeriod(), orderType, id, order.getRate()));
      } else {
        loanOrders.add(
            adaptFloatingRateLoanOrder(
                currency, order.getAmount(), order.getPeriod(), orderType, id, order.getRate()));
      }
    }

    return loanOrders;
  }

  /**
   * 适应/适配浮动利率贷款订单
   * @param currency 货币
   * @param amount 数量
   * @param dayPeriod 天 期间/阶段
   * @param direction 方向
   * @param id
   * @param rate 比例
   * @return
   */
  public static FloatingRateLoanOrder adaptFloatingRateLoanOrder(
      String currency,
      BigDecimal amount,
      int dayPeriod,
      String direction,
      String id,
      BigDecimal rate) {

    OrderType orderType = direction.equalsIgnoreCase("loan") ? OrderType.BID : OrderType.ASK;

    return new FloatingRateLoanOrder(orderType, currency, amount, dayPeriod, id, null, rate);
  }

  public static Trade adaptTrade(BitfinexTrade trade, CurrencyPair currencyPair) {

    OrderType orderType = trade.getType().equals("buy") ? OrderType.BID : OrderType.ASK;
    BigDecimal amount = trade.getAmount();
    BigDecimal price = trade.getPrice();
    Date date =
        DateUtils.fromMillisUtc(trade.getTimestamp() * 1000L); // Bitfinex uses Unix timestamps  // Bitfinex 使用 Unix 时间戳
    final String tradeId = String.valueOf(trade.getTradeId());
    return new Trade.Builder()
        .type(orderType)
        .originalAmount(amount)
        .currencyPair(currencyPair)
        .price(price)
        .timestamp(date)
        .id(tradeId)
        .build();
  }

  /**
   *  适应/适配/调整 交易
   * @param trades 交易
   * @param currencyPair 货币对
   * @return
   */
  public static Trades adaptTrades(BitfinexTrade[] trades, CurrencyPair currencyPair) {

    List<Trade> tradesList = new ArrayList<>(trades.length);
    long lastTradeId = 0;
    for (BitfinexTrade trade : trades) {
      long tradeId = trade.getTradeId();
      if (tradeId > lastTradeId) {
        lastTradeId = tradeId;
      }
      tradesList.add(adaptTrade(trade, currencyPair));
    }
    return new Trades(tradesList, lastTradeId, TradeSortType.SortByID);
  }

  /**
   * 调整/适应/适配 股票代码
   * @param bitfinexTicker bitfinex 股票代码
   * @param currencyPair 货币对
   * @return
   */
  public static Ticker adaptTicker(BitfinexTicker bitfinexTicker, CurrencyPair currencyPair) {

    BigDecimal last = bitfinexTicker.getLast_price();
    BigDecimal bid = bitfinexTicker.getBid();
    BigDecimal bidSize = bitfinexTicker.getBidSize();
    BigDecimal ask = bitfinexTicker.getAsk();
    BigDecimal askSize = bitfinexTicker.getAskSize();
    BigDecimal high = bitfinexTicker.getHigh();
    BigDecimal low = bitfinexTicker.getLow();
    BigDecimal volume = bitfinexTicker.getVolume();

    Date timestamp = DateUtils.fromMillisUtc((long) (bitfinexTicker.getTimestamp() * 1000L));

    return new Ticker.Builder()
        .currencyPair(currencyPair)
        .last(last)
        .bid(bid)
        .bidSize(bidSize)
        .ask(ask)
        .askSize(askSize)
        .high(high)
        .low(low)
        .volume(volume)
        .timestamp(timestamp)
        .build();
  }

  /**
   * 适配钱包
   * @param response 响应
   * @return
   */
  public static List<Wallet> adaptWallets(BitfinexBalancesResponse[] response) {

    Map<String, Map<String, BigDecimal[]>> walletsBalancesMap = new HashMap<>();

    // for each currency we have multiple balances types: exchange, trading, deposit.
    // 对于每种货币，我们有多种余额类型：兑换、交易、存款。

    // each of those may be partially frozen/available
    // 这些中的每一个都可能部分冻结/可用
    for (BitfinexBalancesResponse balance : response) {
      String walletId = balance.getType();

      if (!walletsBalancesMap.containsKey(walletId)) {
        walletsBalancesMap.put(walletId, new HashMap<>());
      }
      Map<String, BigDecimal[]> balancesByCurrency =
          walletsBalancesMap.get(walletId); // {total, available}  // {总数，可用}

      String currencyName = adaptBitfinexCurrency(balance.getCurrency());
      BigDecimal[] balanceDetail = balancesByCurrency.get(currencyName);
      if (balanceDetail == null) {
        balanceDetail = new BigDecimal[] {balance.getAmount(), balance.getAvailable()};
      } else {
        balanceDetail[0] = balanceDetail[0].add(balance.getAmount());
        balanceDetail[1] = balanceDetail[1].add(balance.getAvailable());
      }
      balancesByCurrency.put(currencyName, balanceDetail);
    }

    List<Wallet> wallets = new ArrayList<>();
    for (Entry<String, Map<String, BigDecimal[]>> walletData : walletsBalancesMap.entrySet()) {
      Map<String, BigDecimal[]> balancesByCurrency = walletData.getValue();

      List<Balance> balances = new ArrayList<>(balancesByCurrency.size());
      for (Entry<String, BigDecimal[]> entry : balancesByCurrency.entrySet()) {
        String currencyName = entry.getKey();
        BigDecimal[] balanceDetail = entry.getValue();
        BigDecimal balanceTotal = balanceDetail[0];
        BigDecimal balanceAvailable = balanceDetail[1];
        balances.add(
            new Balance(Currency.getInstance(currencyName), balanceTotal, balanceAvailable));
      }
      wallets.add(Wallet.Builder.from(balances).id(walletData.getKey()).build());
    }

    return wallets;
  }

  /**
   * 适应/适配订单
   * @param activeOrders 活跃订单
   * @return
   */
  public static OpenOrders adaptOrders(BitfinexOrderStatusResponse[] activeOrders) {

    List<LimitOrder> limitOrders = new ArrayList<>();
    List<Order> hiddenOrders = new ArrayList<>();

    for (BitfinexOrderStatusResponse order : activeOrders) {

      OrderType orderType = order.getSide().equalsIgnoreCase("buy") ? OrderType.BID : OrderType.ASK;
      OrderStatus status = adaptOrderStatus(order);
      CurrencyPair currencyPair = adaptCurrencyPair(order.getSymbol());
      Date timestamp = convertBigDecimalTimestampToDate(order.getTimestamp());

      Supplier<MarketOrder> marketOrderCreator =
          () ->
              new MarketOrder(
                  orderType,
                  order.getOriginalAmount(),
                  currencyPair,
                  String.valueOf(order.getId()),
                  timestamp,
                  order.getAvgExecutionPrice(),
                  order.getExecutedAmount(),
                  null,
                  status);

      Supplier<LimitOrder> limitOrderCreator =
          () ->
              new LimitOrder(
                  orderType,
                  order.getOriginalAmount(),
                  currencyPair,
                  String.valueOf(order.getId()),
                  timestamp,
                  order.getPrice(),
                  order.getAvgExecutionPrice(),
                  order.getExecutedAmount(),
                  null,
                  status);

      Supplier<StopOrder> stopOrderCreator =
          () ->
              new StopOrder(
                  orderType,
                  order.getOriginalAmount(),
                  currencyPair,
                  String.valueOf(order.getId()),
                  timestamp,
                  order.getPrice(),
                  null,
                  order.getAvgExecutionPrice(),
                  order.getExecutedAmount(),
                  status);

      LimitOrder limitOrder = null;
      StopOrder stopOrder = null;
      MarketOrder marketOrder = null;

      Optional<BitfinexOrderType> bitfinexOrderType =
          Arrays.stream(BitfinexOrderType.values())
              .filter(v -> v.getValue().equals(order.getType()))
              .findFirst();

      if (bitfinexOrderType.isPresent()) {
        switch (bitfinexOrderType.get()) {
          case FILL_OR_KILL:
            limitOrder = limitOrderCreator.get();
            limitOrder.addOrderFlag(BitfinexOrderFlags.FILL_OR_KILL);
            break;
          case MARGIN_FILL_OR_KILL:
            limitOrder = limitOrderCreator.get();
            limitOrder.addOrderFlag(BitfinexOrderFlags.FILL_OR_KILL);
            limitOrder.addOrderFlag(BitfinexOrderFlags.MARGIN);
            break;
          case MARGIN_LIMIT:
            limitOrder = limitOrderCreator.get();
            limitOrder.addOrderFlag(BitfinexOrderFlags.MARGIN);
            break;
          case MARGIN_STOP:
            stopOrder = stopOrderCreator.get();
            stopOrder.addOrderFlag(BitfinexOrderFlags.STOP);
            stopOrder.addOrderFlag(BitfinexOrderFlags.MARGIN);
            break;
          case MARGIN_STOP_LIMIT:
            stopLimitWarning();
            stopOrder = stopOrderCreator.get();
            stopOrder.addOrderFlag(BitfinexOrderFlags.STOP);
            stopOrder.addOrderFlag(BitfinexOrderFlags.MARGIN);
            break;
          case MARGIN_TRAILING_STOP:
            limitOrder = limitOrderCreator.get();
            limitOrder.addOrderFlag(BitfinexOrderFlags.TRAILING_STOP);
            limitOrder.addOrderFlag(BitfinexOrderFlags.MARGIN);
            break;
          case STOP:
            stopOrder = stopOrderCreator.get();
            stopOrder.addOrderFlag(BitfinexOrderFlags.STOP);
            break;
          case STOP_LIMIT:
            stopLimitWarning();
            stopOrder = stopOrderCreator.get();
            stopOrder.addOrderFlag(BitfinexOrderFlags.STOP);
            break;
          case TRAILING_STOP:
            limitOrder = limitOrderCreator.get();
            limitOrder.addOrderFlag(BitfinexOrderFlags.TRAILING_STOP);
            break;
          case LIMIT:
            limitOrder = limitOrderCreator.get();
            break;
          case MARGIN_MARKET:
          case MARKET:
            marketOrder = marketOrderCreator.get();
            break;
          default:
            log.warn(
                "Unhandled Bitfinex order type 未处理的 Bitfinex 订单类型 [{}]. Defaulting to limit order 默认限价单", order.getType());
            limitOrder = limitOrderCreator.get();
            break;
        }
      } else {
        log.warn("Unknown Bitfinex order type 未知的 Bitfinex 订单类型 [{}]. Defaulting to limit order 默认限价单", order.getType());
        limitOrder = limitOrderCreator.get();
      }

      if (limitOrder != null) {
        limitOrders.add(limitOrder);
      } else if (stopOrder != null) {
        hiddenOrders.add(stopOrder);
      } else if (marketOrder != null) {
        hiddenOrders.add(marketOrder);
      }
    }

    return new OpenOrders(limitOrders, hiddenOrders);
  }

  /**
   * 停止限制警告
   */
  private static void stopLimitWarning() {
    if (warnedStopLimit.compareAndSet(false, true)) {
      log.warn(
          "Found a stop-limit order. Bitfinex v1 API does not return limit prices for stop-limit orders so these are returned as stop-at-market orders." +
                  "找到止损限价单。 Bitfinex v1 API 不返回限价止损订单的限价，因此这些订单作为市价止损订单返回。" +
                  " This warning will only appear once." +
                  "此警告只会出现一次。");
    }
  }

  /**
   * 适应/适配交易历史
   * @param trades
   * @param symbol
   * @return
   */
  public static UserTrades adaptTradeHistory(BitfinexTradeResponse[] trades, String symbol) {

    List<UserTrade> pastTrades = new ArrayList<>(trades.length);
    CurrencyPair currencyPair = adaptCurrencyPair(symbol);

    for (BitfinexTradeResponse trade : trades) {
      OrderType orderType = trade.getType().equalsIgnoreCase("buy") ? OrderType.BID : OrderType.ASK;
      Date timestamp = convertBigDecimalTimestampToDate(trade.getTimestamp());
      final BigDecimal fee = trade.getFeeAmount() == null ? null : trade.getFeeAmount().negate();
      pastTrades.add(
          new UserTrade.Builder()
              .type(orderType)
              .originalAmount(trade.getAmount())
              .currencyPair(currencyPair)
              .price(trade.getPrice())
              .timestamp(timestamp)
              .id(trade.getTradeId())
              .orderId(trade.getOrderId())
              .feeAmount(fee)
              .feeCurrency(Currency.getInstance(trade.getFeeCurrency()))
              .build());
    }

    return new UserTrades(pastTrades, TradeSortType.SortByTimestamp);
  }

  /**
   * 适应/适配交易历史V2
   * @param trades
   * @return
   */
  public static UserTrades adaptTradeHistoryV2(
      List<org.knowm.xchange.bitfinex.v2.dto.trade.Trade> trades) {

    List<UserTrade> pastTrades = new ArrayList<>(trades.size());

    for (org.knowm.xchange.bitfinex.v2.dto.trade.Trade trade : trades) {
      OrderType orderType = trade.getExecAmount().signum() >= 0 ? OrderType.BID : OrderType.ASK;
      BigDecimal amount =
          trade.getExecAmount().signum() == -1
              ? trade.getExecAmount().negate()
              : trade.getExecAmount();
      final BigDecimal fee = trade.getFee() != null ? trade.getFee().negate() : null;
      pastTrades.add(
          new UserTrade.Builder()
              .type(orderType)
              .originalAmount(amount)
              .currencyPair(adaptCurrencyPair(trade.getSymbol()))
              .price(trade.getExecPrice())
              .timestamp(trade.getTimestamp())
              .id(trade.getId())
              .orderId(trade.getOrderId())
              .feeAmount(fee)
              .feeCurrency(Currency.getInstance(trade.getFeeCurrency()))
              .build());
    }

    return new UserTrades(pastTrades, TradeSortType.SortByTimestamp);
  }

  /**
   * 将大十进制时间戳转换为日期
   * @param timestamp 时间戳
   * @return
   */
  private static Date convertBigDecimalTimestampToDate(BigDecimal timestamp) {

    BigDecimal timestampInMillis = timestamp.multiply(new BigDecimal("1000"));
    return new Date(timestampInMillis.longValue());
  }

  /**
   * 适配元数据
   * @param currencyPairs  货币对
   * @param metaData 元数据
   * @return
   */
  public static ExchangeMetaData adaptMetaData(
      List<CurrencyPair> currencyPairs, ExchangeMetaData metaData) {

    Map<CurrencyPair, CurrencyPairMetaData> pairsMap = metaData.getCurrencyPairs();
    Map<Currency, CurrencyMetaData> currenciesMap = metaData.getCurrencies();

    // Remove pairs that are no-longer in use
    // 删除不再使用的对
    pairsMap.keySet().retainAll(currencyPairs);

    // Remove currencies that are no-longer in use
    // 移除不再使用的货币
    Set<Currency> currencies =
        currencyPairs.stream()
            .flatMap(pair -> Stream.of(pair.base, pair.counter))
            .collect(Collectors.toSet());
    currenciesMap.keySet().retainAll(currencies);

    // Add missing pairs and currencies
    // 添加缺失的货币对和货币
    for (CurrencyPair c : currencyPairs) {
      if (!pairsMap.containsKey(c)) {
        pairsMap.put(c, null);
      }

      if (!currenciesMap.containsKey(c.base)) {
        currenciesMap.put(
            c.base,
            new CurrencyMetaData(
                2,
                null)); // When missing, add default meta-data with scale of 2 (Bitfinex's minimal scale)  // 缺失时，添加比例为 2 的默认元数据（Bitfinex 最小比例）
      }
      if (!currenciesMap.containsKey(c.counter)) {
        currenciesMap.put(c.counter, new CurrencyMetaData(2, null));
      }
    }

    return metaData;
  }

  /**
   * 适配元数据
   *
   * Flipped order of arguments to avoid type-erasure clash with {@link #adaptMetaData(List, ExchangeMetaData)}
   * * 翻转参数顺序以避免与 {@link #adaptMetaData(List, ExchangeMetaData)} 发生类型擦除冲突
   *
   * @param exchangeMetaData The exchange metadata provided from bitfinex.json.
   *                         从 bitfinex.json 提供的交换元数据。
   * @param symbolDetails The symbol data fetced from Bitfinex.
   *                      从 Bitfinex 获取的符号数据。
   * @return The combined result. 综合结果。
   */
  public static ExchangeMetaData adaptMetaData(
      ExchangeMetaData exchangeMetaData,
      List<BitfinexSymbolDetail> symbolDetails,
      Map<CurrencyPair, BigDecimal> lastPrices) {

    final Map<CurrencyPair, CurrencyPairMetaData> currencyPairs =
        exchangeMetaData.getCurrencyPairs();
    symbolDetails.parallelStream()
        .forEach(
            bitfinexSymbolDetail -> {
              final CurrencyPair currencyPair = adaptCurrencyPair(bitfinexSymbolDetail.getPair());

              // Infer price-scale from last and price-precision
              // 从最后一个和价格精度推断价格比例
              BigDecimal last = lastPrices.get(currencyPair);

              if (last != null) {
                int pricePercision = bitfinexSymbolDetail.getPrice_precision();
                int priceScale = last.scale() + (pricePercision - last.precision());

                CurrencyPairMetaData newMetaData =
                    new CurrencyPairMetaData(
                        currencyPairs.get(currencyPair) == null
                            ? null
                            : currencyPairs
                                .get(currencyPair)
                                .getTradingFee(), // Take tradingFee from static metaData if exists // 如果存在，则从静态元数据中获取 tradingFee
                        bitfinexSymbolDetail.getMinimum_order_size(),
                        bitfinexSymbolDetail.getMaximum_order_size(),
                        priceScale,
                        null);
                currencyPairs.put(currencyPair, newMetaData);
              }
            });
    return exchangeMetaData;
  }

  /**
   * 适配元数据
   * @param accountFeesResponse 账户费用回应
   * @param platformStatus 平台状态
   * @param platformStatusPresent 平台状态存在
   * @param metaData 元数据
   * @return
   */
  public static ExchangeMetaData adaptMetaData(
      BitfinexAccountFeesResponse accountFeesResponse,
      int platformStatus,
      boolean platformStatusPresent,
      ExchangeMetaData metaData) {
    final WalletHealth health;
    if (platformStatusPresent) {
      if (platformStatus == PLATFORM_STATUS_ONLINE) {
        health = WalletHealth.ONLINE;
      } else if (platformStatus == PLATFORM_STATUS_OFFLINE) {
        health = WalletHealth.OFFLINE;
      } else {
        health = WalletHealth.UNKNOWN;
      }
    } else {
      health = WalletHealth.UNKNOWN;
    }

    Map<Currency, CurrencyMetaData> currencies = metaData.getCurrencies();
    final Map<Currency, BigDecimal> withdrawFees = accountFeesResponse.getWithdraw();
    withdrawFees.forEach(
        (currency, withdrawalFee) -> {
          CurrencyMetaData newMetaData =
              new CurrencyMetaData(
                  // Currency should have at least the scale of the withdrawalFee
                      // 币种至少应该有提现手续费的规模
                  currencies.get(currency) == null
                      ? withdrawalFee.scale()
                      : Math.max(withdrawalFee.scale(), currencies.get(currency).getScale()),
                  withdrawalFee,
                  null,
                  health);
          currencies.put(currency, newMetaData);
        });
    return metaData;
  }

  /**
   * 适配元数据
   * @param bitfinexAccountInfos bitfinex 账户信息
   * @param exchangeMetaData 交换/交易所元数据
   * @return
   */
  public static ExchangeMetaData adaptMetaData(
      BitfinexAccountInfosResponse[] bitfinexAccountInfos, ExchangeMetaData exchangeMetaData) {
    final Map<CurrencyPair, CurrencyPairMetaData> currencyPairs =
        exchangeMetaData.getCurrencyPairs();

    // lets go with the assumption that the trading fees are common across all trading pairs for now.
    // 假设目前所有交易对的交易费用都是通用的。
    // also setting the taker_fee as the trading_fee for now.
    // 现在也将 taker_fee 设置为 trading_fee。
    final CurrencyPairMetaData metaData =
        new CurrencyPairMetaData(
            bitfinexAccountInfos[0].getTakerFees().movePointLeft(2), null, null, null, null);
    currencyPairs.keySet().parallelStream()
        .forEach(
            currencyPair ->
                currencyPairs.merge(
                    currencyPair,
                    metaData,
                    (oldMetaData, newMetaData) ->
                        new CurrencyPairMetaData(
                            newMetaData.getTradingFee(),
                            oldMetaData.getMinimumAmount(),
                            oldMetaData.getMaximumAmount(),
                            oldMetaData.getPriceScale(),
                            oldMetaData.getFeeTiers())));

    return exchangeMetaData;
  }

  /**
   * 适配资金历史
   * @param movementHistorys 运动/移动的历史
   * @return
   */
  public static List<FundingRecord> adaptFundingHistory(List<Movement> movementHistorys) {
    final List<FundingRecord> fundingRecords = new ArrayList<>();
    for (Movement movement : movementHistorys) {
      Currency currency = Currency.getInstance(movement.getCurency());

      FundingRecord.Type type =
          movement.getAmount().compareTo(BigDecimal.ZERO) < 0
              ? FundingRecord.Type.WITHDRAWAL
              : FundingRecord.Type.DEPOSIT;

      FundingRecord.Status status = FundingRecord.Status.resolveStatus(movement.getStatus());
      if (status == null
          && movement
              .getStatus()
              .equalsIgnoreCase("CANCELED")) // there's a spelling mistake in the protocol  // 协议中有拼写错误
      status = FundingRecord.Status.CANCELLED;

      BigDecimal amount = movement.getAmount().abs();
      BigDecimal fee = movement.getFees().abs();
      if (fee != null && type.isOutflowing()) {
        // The amount reported form Bitfinex on a withdrawal is without the fee, so it has to be added to get the full amount withdrawn from the wallet  Deposits don't seem to have fees, but it seems reasonable to assume that the reported value is the full amount added to the wallet
        // Bitfinex 报告的提款金额是免费的，因此必须添加才能从钱包中提取全部金额 存款似乎没有费用，但假设报告的值为 添加到钱包的全部金额
        amount = amount.add(fee);
      }

      FundingRecord fundingRecordEntry =
          new FundingRecord(
              movement.getDestinationAddress(),
              null,
              movement.getMtsUpdated(),
              currency,
              amount,
              movement.getId(),
              movement.getTransactionId(),
              type,
              status,
              null,
              fee,
              null);

      fundingRecords.add(fundingRecordEntry);
    }
    return fundingRecords;
  }

  /**
   * 适配资金历史
   * @param bitfinexDepositWithdrawalHistoryResponses bitfinex 存款取款历史响应
   * @return
   */
  public static List<FundingRecord> adaptFundingHistory(
      BitfinexDepositWithdrawalHistoryResponse[] bitfinexDepositWithdrawalHistoryResponses) {
    final List<FundingRecord> fundingRecords = new ArrayList<>();
    for (BitfinexDepositWithdrawalHistoryResponse responseEntry :
        bitfinexDepositWithdrawalHistoryResponses) {
      String address = responseEntry.getAddress();
      String description = responseEntry.getDescription();
      Currency currency = Currency.getInstance(responseEntry.getCurrency());

      FundingRecord.Status status = FundingRecord.Status.resolveStatus(responseEntry.getStatus());
      if (status == null
          && responseEntry
              .getStatus()
              .equalsIgnoreCase("CANCELED")) // there's a spelling mistake in the protocol  // 协议中有拼写错误
      status = FundingRecord.Status.CANCELLED;

      String txnId = null;
      if (status == null || !status.equals(FundingRecord.Status.CANCELLED)) {
        /*
        sometimes the description looks like this (with the txn hash in it):
        有时描述看起来像这样（其中包含 txn 哈希）：
        "description":"a9d387cf5d9df58ff2ac4a338e0f050fd3857cf78d1dbca4f33619dc4ccdac82","address":"1Enx...
        “描述”：“a9d387cf5d9df58ff2ac4a338e0f050fd3857cf78d1dbca4f33619dc4ccdac82”，“地址”：“1Enx ...

        and sometimes like this (with the address in it as well as the txn hash):
        有时像这样（其中包含地址以及 txn 哈希）：
        "description":"3AXVnDapuRiAn73pjKe7gukLSx5813oFyn, txid: aa4057486d5f73747167beb9949a0dfe17b5fc630499a66af075abdaf4986987","address":"3AX...
        “描述”：“3AXVnDapuRiAn73pjKe7gukLSx5813oFyn，txid：aa4057486d5f73747167beb9949a0dfe17b5fc630499a66af075abdaf4986987”，“地址”：“3AX ...

        and sometimes when cancelled description":"3LFVTLFZoDDzLCcLGDDQ7MNkk4YPe26Yva, expired","address":"3LFV...
        有时当取消描述":"3LFVTLFZoDDzLCcLGDDQ7MNkk4YPe26Yva, expired","address":"3LFV...
         */

        String cleanedDescription =
            description.replace(",", "").replace("txid:", "").trim().toLowerCase();

        // Address will only be present for crypto payments. It will be null for all fiat payments
        // 地址只会出现在加密支付中。 所有法定支付都将为空
        if (address != null) {
          cleanedDescription = cleanedDescription.replace(address.toLowerCase(), "").trim();
        }

        // check its just some hex characters, and if so lets assume its the txn hash
        // 检查它只是一些十六进制字符，如果是，假设它是 txn 哈希
        if (cleanedDescription.matches("^(0x)?[0-9a-f]+$")) {
          txnId = cleanedDescription;
        }
      }

      FundingRecord fundingRecordEntry =
          new FundingRecord(
              address,
              responseEntry.getTimestamp(),
              currency,
              responseEntry.getAmount(),
              String.valueOf(responseEntry.getId()),
              txnId,
              responseEntry.getType(),
              status,
              null,
              null,
              description);

      fundingRecords.add(fundingRecordEntry);
    }
    return fundingRecords;
  }

  /**
   * 订单容器
   */
  public static class OrdersContainer {

    private final long timestamp;
    private final List<LimitOrder> limitOrders;

    /**
     * Constructor
     * 订单容器
     *
     * @param timestamp The timestamp for the data fetched.
     *                  获取数据的时间戳。
     * @param limitOrders The orders. 命令。
     */
    public OrdersContainer(long timestamp, List<LimitOrder> limitOrders) {

      this.timestamp = timestamp;
      this.limitOrders = limitOrders;
    }

    public long getTimestamp() {

      return timestamp;
    }

    public List<LimitOrder> getLimitOrders() {

      return limitOrders;
    }
  }

  ////// v2

  /**
   * 根据Tickers参数调整货币对
   * @param currencyPairs 货币对
   * @return
   */
  public static String adaptCurrencyPairsToTickersParam(Collection<CurrencyPair> currencyPairs) {
    return currencyPairs == null || currencyPairs.isEmpty()
        ? "ALL"
        : currencyPairs.stream()
            .map(BitfinexAdapters::adaptCurrencyPair)
            .collect(Collectors.joining(","));
  }

  /**
   * 适应/适配股票
   * @param bitfinexTicker bitfinex 代码
   * @return
   */
  public static Ticker adaptTicker(
      org.knowm.xchange.bitfinex.v2.dto.marketdata.BitfinexTicker bitfinexTicker) {

    BigDecimal last = bitfinexTicker.getLastPrice();
    BigDecimal bid = bitfinexTicker.getBid();
    BigDecimal bidSize = bitfinexTicker.getBidSize();
    BigDecimal ask = bitfinexTicker.getAsk();
    BigDecimal askSize = bitfinexTicker.getAskSize();
    BigDecimal high = bitfinexTicker.getHigh();
    BigDecimal low = bitfinexTicker.getLow();
    BigDecimal volume = bitfinexTicker.getVolume();
    BigDecimal percentageChange =
        bitfinexTicker.getDailyChangePerc().multiply(new BigDecimal("100"), new MathContext(8));

    CurrencyPair currencyPair =
        CurrencyPairDeserializer.getCurrencyPairFromString(bitfinexTicker.getSymbol().substring(1));

    return new Ticker.Builder()
        .currencyPair(currencyPair)
        .last(last)
        .bid(bid)
        .ask(ask)
        .high(high)
        .low(low)
        .volume(volume)
        .bidSize(bidSize)
        .askSize(askSize)
        .percentageChange(percentageChange)
        .build();
  }

  /**
   * 适应/适配公共贸易
   * @param trade 交易
   * @param currencyPair 货币对
   * @return
   */
  public static Trade adaptPublicTrade(BitfinexPublicTrade trade, CurrencyPair currencyPair) {

    OrderType orderType = trade.getType();
    BigDecimal amount = trade.getAmount();
    BigDecimal price = trade.getPrice();
    Date date = DateUtils.fromMillisUtc(trade.getTimestamp());
    final String tradeId = String.valueOf(trade.getTradeId());
    return new Trade.Builder()
        .type(orderType)
        .originalAmount(amount == null ? null : amount.abs())
        .currencyPair(currencyPair)
        .price(price)
        .timestamp(date)
        .id(tradeId)
        .build();
  }

  /**
   * 适配公共贸易
   * @param trades 交易
   * @param currencyPair 货币对
   * @return
   */
  public static Trades adaptPublicTrades(BitfinexPublicTrade[] trades, CurrencyPair currencyPair) {

    List<Trade> tradesList = new ArrayList<>(trades.length);
    long lastTradeId = 0;
    for (BitfinexPublicTrade trade : trades) {
      long tradeId = trade.getTradeId();
      if (tradeId > lastTradeId) {
        lastTradeId = tradeId;
      }
      tradesList.add(adaptPublicTrade(trade, currencyPair));
    }
    return new Trades(tradesList, lastTradeId, TradeSortType.SortByID);
  }

  /**
   * 采用/适配? Bitfinex Tickers
   * @param tickers 股票代码
   * @return
   * @throws IOException
   */
  public static org.knowm.xchange.bitfinex.v2.dto.marketdata.BitfinexTicker[] adoptBitfinexTickers(
      List<ArrayNode> tickers) throws IOException {

    return tickers.stream()
        .map(
            array -> {
              // tBTCUSD -> traiding pair  tBTCUSD -> 交易对

              // fUSD -> funding currency   fUSD -> 资金货币
              try {
                String symbol = array.get(0).asText();
                switch (symbol.charAt(0)) {
                  case 't':
                    return mapper.treeToValue(array, BitfinexTickerTraidingPair.class);
                  case 'f':
                    return mapper.treeToValue(array, BitfinexTickerFundingCurrency.class);
                  default:
                    throw new RuntimeException(
                        "Invalid symbol 无效symbol <" + symbol + ">, it must start with 't' or 'f' 它必须以“t”或“f”开头.");
                }
              } catch (JsonProcessingException e) {
                throw new RuntimeException("Could not convert ticker. 无法转换代码。", e);
              }
            })
        .toArray(org.knowm.xchange.bitfinex.v2.dto.marketdata.BitfinexTicker[]::new);
  }
}
