package org.knowm.xchange.bitfinex.v2;

import java.io.IOException;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.knowm.xchange.bitfinex.v2.dto.BitfinexExceptionV2;
import org.knowm.xchange.bitfinex.v2.dto.EmptyRequest;
import org.knowm.xchange.bitfinex.v2.dto.account.LedgerEntry;
import org.knowm.xchange.bitfinex.v2.dto.account.LedgerRequest;
import org.knowm.xchange.bitfinex.v2.dto.account.Movement;
import org.knowm.xchange.bitfinex.v2.dto.account.TransferBetweenWalletsRequest;
import org.knowm.xchange.bitfinex.v2.dto.account.TransferBetweenWalletsResponse;
import org.knowm.xchange.bitfinex.v2.dto.account.UpdateCollateralDerivativePositionRequest;
import org.knowm.xchange.bitfinex.v2.dto.account.Wallet;
import org.knowm.xchange.bitfinex.v2.dto.trade.ActiveOrder;
import org.knowm.xchange.bitfinex.v2.dto.trade.OrderTrade;
import org.knowm.xchange.bitfinex.v2.dto.trade.Position;
import org.knowm.xchange.bitfinex.v2.dto.trade.Trade;
import si.mazi.rescu.ParamsDigest;
import si.mazi.rescu.SynchronizedValueFactory;

@Path("v2")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface BitfinexAuthenticated extends Bitfinex {

  public static final String BFX_APIKEY = "bfx-apikey";
  public static final String BFX_SIGNATURE = "bfx-signature";
  public static final String BFX_NONCE = "bfx-nonce";

  /**
   * 活跃的 /活动的 持仓 /仓位
   * https://docs.bitfinex.com/v2/reference#rest-auth-positions */
  @POST
  @Path("auth/r/positions")
  List<Position> activePositions(
      @HeaderParam(BFX_NONCE) SynchronizedValueFactory<Long> nonce,
      @HeaderParam(BFX_APIKEY) String apiKey,
      @HeaderParam(BFX_SIGNATURE) ParamsDigest signature,
      EmptyRequest empty)
      throws IOException, BitfinexExceptionV2;

  /**
   * 获取钱包
   * https://docs.bitfinex.com/reference#rest-auth-wallets */
  @POST
  @Path("auth/r/wallets")
  List<Wallet> getWallets(
      @HeaderParam(BFX_NONCE) SynchronizedValueFactory<Long> nonce,
      @HeaderParam(BFX_APIKEY) String apiKey,
      @HeaderParam(BFX_SIGNATURE) ParamsDigest signature,
      EmptyRequest empty)
      throws IOException, BitfinexExceptionV2;

  /**
   *
   * 获取交易
   * https://docs.bitfinex.com/v2/reference#rest-auth-trades-hist
   *
   * <p>Two implementations: 1. returns trades of all symboles 2. returns trades of a specific symbol
   * * <p>两种实现方式： 1. 返回所有交易品种的交易 2. 返回特定交易品种的交易
   *
   * <p>This is necessary because @Path doesn't seems to support optional parameters
   * * <p>这是必要的，因为@Path 似乎不支持可选参数
   */
  @POST
  @Path("auth/r/trades/hist")
  List<Trade> getTrades(
      @HeaderParam(BFX_NONCE) SynchronizedValueFactory<Long> nonce,
      @HeaderParam(BFX_APIKEY) String apiKey,
      @HeaderParam(BFX_SIGNATURE) ParamsDigest signature,
      @QueryParam("start") Long startTimeMillis,
      @QueryParam("end") Long endTimeMillis,
      @QueryParam("limit") Long limit,
      @QueryParam("sort") Long sort,
      EmptyRequest empty)
      throws IOException, BitfinexExceptionV2;

  /**
   * 获取交易
   * @param nonce
   * @param apiKey
   * @param signature
   * @param symbol
   * @param startTimeMillis
   * @param endTimeMillis
   * @param limit
   * @param sort
   * @param empty
   * @return
   * @throws IOException
   * @throws BitfinexExceptionV2
   */
  @POST
  @Path("auth/r/trades/{symbol}/hist")
  List<Trade> getTrades(
      @HeaderParam(BFX_NONCE) SynchronizedValueFactory<Long> nonce,
      @HeaderParam(BFX_APIKEY) String apiKey,
      @HeaderParam(BFX_SIGNATURE) ParamsDigest signature,
      @PathParam("symbol") String symbol,
      @QueryParam("start") Long startTimeMillis,
      @QueryParam("end") Long endTimeMillis,
      @QueryParam("limit") Long limit,
      @QueryParam("sort") Long sort,
      EmptyRequest empty)
      throws IOException, BitfinexExceptionV2;

  /**
   * 获取活跃的 /活动的订单
   * https://docs.bitfinex.com/v2/reference#rest-auth-orders */
  @POST
  @Path("auth/r/orders/{symbol}")
  List<ActiveOrder> getActiveOrders(
      @HeaderParam(BFX_NONCE) SynchronizedValueFactory<Long> nonce,
      @HeaderParam(BFX_APIKEY) String apiKey,
      @HeaderParam(BFX_SIGNATURE) ParamsDigest signature,
      @PathParam("symbol") String symbol,
      EmptyRequest empty)
      throws IOException, BitfinexExceptionV2;

  /**
   * 获取分类帐条目
   * https://docs.bitfinex.com/reference#rest-auth-ledgers */
  @POST
  @Path("auth/r/ledgers/hist")
  List<LedgerEntry> getLedgerEntries(
      @HeaderParam(BFX_NONCE) SynchronizedValueFactory<Long> nonce,
      @HeaderParam(BFX_APIKEY) String apiKey,
      @HeaderParam(BFX_SIGNATURE) ParamsDigest signature,
      @QueryParam("start") Long startTimeMillis,
      @QueryParam("end") Long endTimeMillis,
      @QueryParam("limit") Long limit,
      LedgerRequest req)
      throws IOException, BitfinexExceptionV2;

  /**
   * 获取分类帐条目
   * @param nonce
   * @param apiKey
   * @param signature
   * @param currency
   * @param startTimeMillis
   * @param endTimeMillis
   * @param limit
   * @param req
   * @return
   * @throws IOException
   * @throws BitfinexExceptionV2
   */
  @POST
  @Path("auth/r/ledgers/{currency}/hist")
  List<LedgerEntry> getLedgerEntries(
      @HeaderParam(BFX_NONCE) SynchronizedValueFactory<Long> nonce,
      @HeaderParam(BFX_APIKEY) String apiKey,
      @HeaderParam(BFX_SIGNATURE) ParamsDigest signature,
      @PathParam("currency") String currency,
      @QueryParam("start") Long startTimeMillis,
      @QueryParam("end") Long endTimeMillis,
      @QueryParam("limit") Long limit,
      LedgerRequest req)
      throws IOException, BitfinexExceptionV2;

  /**
   * 获取订单交易
   * https://docs.bitfinex.com/reference#rest-auth-order-trades * */
  @POST
  @Path("auth/r/order/{symbol}:{orderId}/trades")
  List<OrderTrade> getOrderTrades(
      @HeaderParam(BFX_NONCE) SynchronizedValueFactory<Long> nonce,
      @HeaderParam(BFX_APIKEY) String apiKey,
      @HeaderParam(BFX_SIGNATURE) ParamsDigest signature,
      @PathParam("symbol") String symbol,
      @PathParam("orderId") Long orderId,
      EmptyRequest empty)
      throws IOException, BitfinexExceptionV2;

  /**
   * 获取运动，运转历史
   * @param nonce
   * @param apiKey
   * @param signature
   * @param symbol
   * @param startTimeMillis
   * @param endTimeMillis
   * @param limit
   * @param empty
   * @return
   * @throws IOException
   * @throws BitfinexExceptionV2
   */
  @POST
  @Path("/auth/r/movements/{symbol}/hist")
  List<Movement> getMovementsHistory(
      @HeaderParam(BFX_NONCE) SynchronizedValueFactory<Long> nonce,
      @HeaderParam(BFX_APIKEY) String apiKey,
      @HeaderParam(BFX_SIGNATURE) ParamsDigest signature,
      @PathParam("symbol") String symbol,
      @QueryParam("start") Long startTimeMillis,
      @QueryParam("end") Long endTimeMillis,
      @QueryParam("limit") Integer limit,
      EmptyRequest empty)
      throws IOException, BitfinexExceptionV2;

  /**
   * 获取运动，运转历史
   * @param nonce
   * @param apiKey
   * @param signature
   * @param startTimeMillis
   * @param endTimeMillis
   * @param limit
   * @param empty
   * @return
   * @throws IOException
   * @throws BitfinexExceptionV2
   */
  @POST
  @Path("/auth/r/movements/hist")
  List<Movement> getMovementsHistory(
      @HeaderParam(BFX_NONCE) SynchronizedValueFactory<Long> nonce,
      @HeaderParam(BFX_APIKEY) String apiKey,
      @HeaderParam(BFX_SIGNATURE) ParamsDigest signature,
      @QueryParam("start") Long startTimeMillis,
      @QueryParam("end") Long endTimeMillis,
      @QueryParam("limit") Integer limit,
      EmptyRequest empty)
      throws IOException, BitfinexExceptionV2;

  /**
   * 钱包间转账
   * @param nonce
   * @param apiKey
   * @param signature
   * @param req
   * @return
   * @throws IOException
   * @throws BitfinexExceptionV2
   */
  @POST
  @Path("/auth/w/transfer")
  TransferBetweenWalletsResponse transferBetweenWallets(
      @HeaderParam(BFX_NONCE) SynchronizedValueFactory<Long> nonce,
      @HeaderParam(BFX_APIKEY) String apiKey,
      @HeaderParam(BFX_SIGNATURE) ParamsDigest signature,
      TransferBetweenWalletsRequest req)
      throws IOException, BitfinexExceptionV2;

  /**
   * 更新抵押衍生品头寸
   * @param nonce
   * @param apiKey
   * @param signature
   * @param req
   * @return
   * @throws IOException
   * @throws BitfinexExceptionV2
   */
  @POST
  @Path("/auth/w/deriv/collateral/set")
  List<List<Integer>> updateCollateralDerivativePosition(
      @HeaderParam(BFX_NONCE) SynchronizedValueFactory<Long> nonce,
      @HeaderParam(BFX_APIKEY) String apiKey,
      @HeaderParam(BFX_SIGNATURE) ParamsDigest signature,
      UpdateCollateralDerivativePositionRequest req)
      throws IOException, BitfinexExceptionV2;
}
