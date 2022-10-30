package org.knowm.xchange.examples.binance.marketdata;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.databind.util.JSONPObject;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.binance.BinanceExchange;
import org.knowm.xchange.binance.dto.marketdata.BinanceKline;
import org.knowm.xchange.binance.dto.marketdata.BinanceTicker24h;
import org.knowm.xchange.binance.dto.marketdata.KlineInterval;
import org.knowm.xchange.binance.service.BinanceMarketDataService;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.dto.account.Balance;
import org.knowm.xchange.dto.account.Wallet;
import org.knowm.xchange.dto.meta.CurrencyPairMetaData;
import org.knowm.xchange.examples.binance.BinanceDemoUtils;
import org.knowm.xchange.service.account.AccountService;
import org.knowm.xchange.service.marketdata.MarketDataService;

/**
 * 币安市场数据演示
 */
public class BinanceMarketDataDemo {

    public static void main(String[] args) throws IOException {

        /*私有交换器*/

        ExchangeSpecification dExSp = new BinanceExchange().getDefaultExchangeSpecification();
        Exchange binance = ExchangeFactory.INSTANCE.createExchange(dExSp);
//    TradeService tradeService = binance.getTradeService();
        AccountService accountService = binance.getAccountService();
        AccountInfo accountInfo = accountService.getAccountInfo();
        String username = accountInfo.getUsername();
//    Map<String, Wallet> wallet = accountInfo.getWallets();
        Wallet wallet = accountInfo.getWallet();
        Balance balanceBNB = wallet.getBalance(Currency.BNB);
        Balance balanceVTHO = wallet.getBalance(Currency.VTHO);
        Balance balanceIOTA = wallet.getBalance(Currency.IOTA);
        Balance balanceUSDT = wallet.getBalance(Currency.USDT);

        System.out.println("balanceBNB"+balanceBNB);
        System.out.println("balanceVTHO:"+balanceVTHO);
        System.out.println("balanceIOTA:"+balanceIOTA);
        System.out.println("balanceUSDT:"+balanceUSDT);

        System.out.println("币安userName："+username);
        System.out.println("wallet："+wallet.toString());


//        Exchange exchange = BinanceDemoUtils.createExchange();
//
//        /* create a data service from the exchange
//         * 从交易所创建数据服务 */
//        MarketDataService marketDataService = exchange.getMarketDataService();
//
//        generic(exchange, marketDataService);
////        raw((BinanceExchange) exchange, (BinanceMarketDataService) marketDataService);
//        raw2((BinanceExchange) exchange, (BinanceMarketDataService) marketDataService);
        // rawAll((BinanceExchange) exchange, (BinanceMarketDataService) marketDataService);
    }

    public static void generic(Exchange exchange, MarketDataService marketDataService)
            throws IOException {
    }

    public static void raw(BinanceExchange exchange, BinanceMarketDataService marketDataService)
            throws IOException {

        List<BinanceTicker24h> tickerList = new ArrayList<>();

        Map<CurrencyPair, CurrencyPairMetaData> currencyPairs = exchange.getExchangeMetaData().getCurrencyPairs();
        System.out.println("currencyPairs的大小为：" + currencyPairs.size());


        System.out.println("tickers.add（） 开始：" + new Date());
        for (CurrencyPair cp : exchange.getExchangeMetaData().getCurrencyPairs().keySet()) {
            if (cp.counter == Currency.USDT) {
                tickerList.add(marketDataService.ticker24h(cp));
                System.out.println("===="+cp.toString()+"====");
            }
        }
        System.out.println("tickers.add（） 结束："+new Date());

        Collections.sort(
                tickerList,
                new Comparator<BinanceTicker24h>() {
                    @Override
                    public int compare(BinanceTicker24h t1, BinanceTicker24h t2) {
                        return t2.getPriceChangePercent().compareTo(t1.getPriceChangePercent());
                    }
                });

        tickerList.stream()
                .forEach(
                        t -> {
                            System.out.println(
                                    t.getCurrencyPair()
                                            + " 价格变化百分比=> "
                                            + String.format("%+.2f%%", t.getPriceChangePercent()));
                            System.out.println("---"+t+"---");
                        });
        System.out.println("raw out end");
    }

    public static void raw2(BinanceExchange exchange, BinanceMarketDataService marketDataService)
            throws IOException {

        List<BinanceTicker24h> tickerList = new ArrayList<>();

        Map<CurrencyPair, CurrencyPairMetaData> currencyPairs = exchange.getExchangeMetaData().getCurrencyPairs();
        System.out.println("currencyPairs的大小为：" + currencyPairs.size());

        System.out.println("---------------------------------------klines开始:");
        List<BinanceKline> klines = marketDataService.klines(CurrencyPair.EOS_USDT, KlineInterval.m15,5,null,null);
        System.out.println(klines);
//        klines.forEach(item->{
//            System.out.println(item);
//        });
        System.out.println("---------------------------------------结束klines.size:"+klines.size());


//        System.out.println("tickers.add（） 开始：" + new Date());
//        for (CurrencyPair cp : exchange.getExchangeMetaData().getCurrencyPairs().keySet()) {
//            if (cp.counter == Currency.USDT) {
//                tickerList.add(marketDataService.ticker24h(cp));
//                System.out.println("===="+cp.toString()+"====");
//            }
//        }
        System.out.println("tickers.add（） 结束："+new Date());
//
//        Collections.sort(
//                tickerList,
//                new Comparator<BinanceTicker24h>() {
//                    @Override
//                    public int compare(BinanceTicker24h t1, BinanceTicker24h t2) {
//                        return t2.getPriceChangePercent().compareTo(t1.getPriceChangePercent());
//                    }
//                });
//
//        tickerList.stream()
//                .forEach(
//                        t -> {
//                            System.out.println(
//                                    t.getCurrencyPair()
//                                            + " 价格变化百分比=> "
//                                            + String.format("%+.2f%%", t.getPriceChangePercent()));
//                            System.out.println("---"+t+"---");
//                        });
        System.out.println("raw out end");
    }

    public static void rawAll(BinanceExchange exchange, BinanceMarketDataService marketDataService)
            throws IOException {

        List<BinanceTicker24h> tickers = new ArrayList<>();
        tickers.addAll(marketDataService.ticker24h());
        Collections.sort(
                tickers,
                new Comparator<BinanceTicker24h>() {
                    @Override
                    public int compare(BinanceTicker24h t1, BinanceTicker24h t2) {
                        return t2.getPriceChangePercent().compareTo(t1.getPriceChangePercent());
                    }
                });

        tickers.stream()
                .forEach(
                        t -> {
                            System.out.println(
                                    t.getSymbol() + " => " + String.format("%+.2f%%", t.getLastPrice()));
                        });
    }
}
