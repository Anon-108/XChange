package org.knowm.xchange.bitfinex.v1;

import java.io.IOException;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.knowm.xchange.bitfinex.v1.dto.BitfinexExceptionV1;
import org.knowm.xchange.bitfinex.v1.dto.account.BitfinexAccountFeesResponse;
import org.knowm.xchange.bitfinex.v1.dto.account.BitfinexBalanceHistoryRequest;
import org.knowm.xchange.bitfinex.v1.dto.account.BitfinexBalanceHistoryResponse;
import org.knowm.xchange.bitfinex.v1.dto.account.BitfinexBalancesRequest;
import org.knowm.xchange.bitfinex.v1.dto.account.BitfinexBalancesResponse;
import org.knowm.xchange.bitfinex.v1.dto.account.BitfinexDepositAddressRequest;
import org.knowm.xchange.bitfinex.v1.dto.account.BitfinexDepositAddressResponse;
import org.knowm.xchange.bitfinex.v1.dto.account.BitfinexDepositWithdrawalHistoryRequest;
import org.knowm.xchange.bitfinex.v1.dto.account.BitfinexDepositWithdrawalHistoryResponse;
import org.knowm.xchange.bitfinex.v1.dto.account.BitfinexMarginInfosRequest;
import org.knowm.xchange.bitfinex.v1.dto.account.BitfinexMarginInfosResponse;
import org.knowm.xchange.bitfinex.v1.dto.account.BitfinexTradingFeeResponse;
import org.knowm.xchange.bitfinex.v1.dto.account.BitfinexTradingFeesRequest;
import org.knowm.xchange.bitfinex.v1.dto.account.BitfinexWithdrawalRequest;
import org.knowm.xchange.bitfinex.v1.dto.account.BitfinexWithdrawalResponse;
import org.knowm.xchange.bitfinex.v1.dto.trade.BitfinexAccountInfosResponse;
import org.knowm.xchange.bitfinex.v1.dto.trade.BitfinexActiveCreditsRequest;
import org.knowm.xchange.bitfinex.v1.dto.trade.BitfinexActivePositionsResponse;
import org.knowm.xchange.bitfinex.v1.dto.trade.BitfinexCancelAllOrdersRequest;
import org.knowm.xchange.bitfinex.v1.dto.trade.BitfinexCancelOfferRequest;
import org.knowm.xchange.bitfinex.v1.dto.trade.BitfinexCancelOrderMultiRequest;
import org.knowm.xchange.bitfinex.v1.dto.trade.BitfinexCancelOrderMultiResponse;
import org.knowm.xchange.bitfinex.v1.dto.trade.BitfinexCancelOrderRequest;
import org.knowm.xchange.bitfinex.v1.dto.trade.BitfinexCreditResponse;
import org.knowm.xchange.bitfinex.v1.dto.trade.BitfinexFundingTradeResponse;
import org.knowm.xchange.bitfinex.v1.dto.trade.BitfinexNewOfferRequest;
import org.knowm.xchange.bitfinex.v1.dto.trade.BitfinexNewOrderMultiRequest;
import org.knowm.xchange.bitfinex.v1.dto.trade.BitfinexNewOrderMultiResponse;
import org.knowm.xchange.bitfinex.v1.dto.trade.BitfinexNewOrderRequest;
import org.knowm.xchange.bitfinex.v1.dto.trade.BitfinexNonceOnlyRequest;
import org.knowm.xchange.bitfinex.v1.dto.trade.BitfinexOfferStatusRequest;
import org.knowm.xchange.bitfinex.v1.dto.trade.BitfinexOfferStatusResponse;
import org.knowm.xchange.bitfinex.v1.dto.trade.BitfinexOrderStatusRequest;
import org.knowm.xchange.bitfinex.v1.dto.trade.BitfinexOrderStatusResponse;
import org.knowm.xchange.bitfinex.v1.dto.trade.BitfinexOrdersHistoryRequest;
import org.knowm.xchange.bitfinex.v1.dto.trade.BitfinexPastFundingTradesRequest;
import org.knowm.xchange.bitfinex.v1.dto.trade.BitfinexPastTradesRequest;
import org.knowm.xchange.bitfinex.v1.dto.trade.BitfinexReplaceOrderRequest;
import org.knowm.xchange.bitfinex.v1.dto.trade.BitfinexTradeResponse;
import si.mazi.rescu.ParamsDigest;

/**
 * Bitfinex 认证
 */
@Path("v1")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface BitfinexAuthenticated extends Bitfinex {
  /**
   * 账户信息
   * @param apiKey
   * @param payload
   * @param signature
   * @param accountInfosRequest
   * @return
   * @throws IOException
   * @throws BitfinexExceptionV1
   */
  @POST
  @Path("account_infos")
  BitfinexAccountInfosResponse[] accountInfos(
      @HeaderParam("X-BFX-APIKEY") String apiKey,
      @HeaderParam("X-BFX-PAYLOAD") ParamsDigest payload,
      @HeaderParam("X-BFX-SIGNATURE") ParamsDigest signature,
      BitfinexNonceOnlyRequest accountInfosRequest)
      throws IOException, BitfinexExceptionV1;

  /**
   * 账户费用
   * @param apiKey
   * @param payload
   * @param signature
   * @param accountInfosRequest
   * @return
   * @throws IOException
   */
  @POST
  @Path("account_fees")
  BitfinexAccountFeesResponse accountFees(
      @HeaderParam("X-BFX-APIKEY") String apiKey,
      @HeaderParam("X-BFX-PAYLOAD") ParamsDigest payload,
      @HeaderParam("X-BFX-SIGNATURE") ParamsDigest signature,
      BitfinexNonceOnlyRequest accountInfosRequest)
      throws IOException;

  /**
   * 新订单
   * @param apiKey
   * @param payload
   * @param signature
   * @param newOrderRequest
   * @return
   * @throws IOException
   * @throws BitfinexExceptionV1
   */
  @POST
  @Path("order/new")
  BitfinexOrderStatusResponse newOrder(
      @HeaderParam("X-BFX-APIKEY") String apiKey,
      @HeaderParam("X-BFX-PAYLOAD") ParamsDigest payload,
      @HeaderParam("X-BFX-SIGNATURE") ParamsDigest signature,
      BitfinexNewOrderRequest newOrderRequest)
      throws IOException, BitfinexExceptionV1;

  /**
   * 新订单多
   * @param apiKey
   * @param payload
   * @param signature
   * @param newOrderMultiRequest
   * @return
   * @throws IOException
   * @throws BitfinexExceptionV1
   */
  @POST
  @Path("order/new/multi")
  BitfinexNewOrderMultiResponse newOrderMulti(
      @HeaderParam("X-BFX-APIKEY") String apiKey,
      @HeaderParam("X-BFX-PAYLOAD") ParamsDigest payload,
      @HeaderParam("X-BFX-SIGNATURE") ParamsDigest signature,
      BitfinexNewOrderMultiRequest newOrderMultiRequest)
      throws IOException, BitfinexExceptionV1;

  /**
   * 新出价
   * @param apiKey
   * @param payload
   * @param signature
   * @param newOfferRequest
   * @return
   * @throws IOException
   * @throws BitfinexExceptionV1
   */
  @POST
  @Path("offer/new")
  BitfinexOfferStatusResponse newOffer(
      @HeaderParam("X-BFX-APIKEY") String apiKey,
      @HeaderParam("X-BFX-PAYLOAD") ParamsDigest payload,
      @HeaderParam("X-BFX-SIGNATURE") ParamsDigest signature,
      BitfinexNewOfferRequest newOfferRequest)
      throws IOException, BitfinexExceptionV1;

  /**
   * 余额
   * @param apiKey
   * @param payload
   * @param signature
   * @param balancesRequest
   * @return
   * @throws IOException
   * @throws BitfinexExceptionV1
   */
  @POST
  @Path("balances")
  BitfinexBalancesResponse[] balances(
      @HeaderParam("X-BFX-APIKEY") String apiKey,
      @HeaderParam("X-BFX-PAYLOAD") ParamsDigest payload,
      @HeaderParam("X-BFX-SIGNATURE") ParamsDigest signature,
      BitfinexBalancesRequest balancesRequest)
      throws IOException, BitfinexExceptionV1;

  /**
   * 交易费用
   * @param apiKey
   * @param payload
   * @param signature
   * @param tradingFeeRequest
   * @return
   * @throws IOException
   * @throws BitfinexExceptionV1
   */
  @POST
  @Path("account_infos")
  BitfinexTradingFeeResponse[] tradingFees(
      @HeaderParam("X-BFX-APIKEY") String apiKey,
      @HeaderParam("X-BFX-PAYLOAD") ParamsDigest payload,
      @HeaderParam("X-BFX-SIGNATURE") ParamsDigest signature,
      BitfinexTradingFeesRequest tradingFeeRequest)
      throws IOException, BitfinexExceptionV1;

  /**
   * 取消订单
   * @param apiKey
   * @param payload
   * @param signature
   * @param cancelOrderRequest
   * @return
   * @throws IOException
   * @throws BitfinexExceptionV1
   */
  @POST
  @Path("order/cancel")
  BitfinexOrderStatusResponse cancelOrders(
      @HeaderParam("X-BFX-APIKEY") String apiKey,
      @HeaderParam("X-BFX-PAYLOAD") ParamsDigest payload,
      @HeaderParam("X-BFX-SIGNATURE") ParamsDigest signature,
      BitfinexCancelOrderRequest cancelOrderRequest)
      throws IOException, BitfinexExceptionV1;

  /**
   * 取消所有订单
   * @param apiKey
   * @param payload
   * @param signature
   * @param cancelAllOrdersRequest
   * @return
   * @throws IOException
   * @throws BitfinexExceptionV1
   */
  @POST
  @Path("order/cancel/all")
  BitfinexOrderStatusResponse cancelAllOrders(
      @HeaderParam("X-BFX-APIKEY") String apiKey,
      @HeaderParam("X-BFX-PAYLOAD") ParamsDigest payload,
      @HeaderParam("X-BFX-SIGNATURE") ParamsDigest signature,
      BitfinexCancelAllOrdersRequest cancelAllOrdersRequest)
      throws IOException, BitfinexExceptionV1;

  /**
   * 取消多个订单
   * @param apiKey
   * @param payload
   * @param signature
   * @param cancelOrderRequest
   * @return
   * @throws IOException
   * @throws BitfinexExceptionV1
   */
  @POST
  @Path("order/cancel/multi")
  BitfinexCancelOrderMultiResponse cancelOrderMulti(
      @HeaderParam("X-BFX-APIKEY") String apiKey,
      @HeaderParam("X-BFX-PAYLOAD") ParamsDigest payload,
      @HeaderParam("X-BFX-SIGNATURE") ParamsDigest signature,
      BitfinexCancelOrderMultiRequest cancelOrderRequest)
      throws IOException, BitfinexExceptionV1;

  /**
   * 取代；（用……）替换订单
   * @param apiKey
   * @param payload
   * @param signature
   * @param newOrderRequest
   * @return
   * @throws IOException
   * @throws BitfinexExceptionV1
   */
  @POST
  @Path("order/cancel/replace")
  BitfinexOrderStatusResponse replaceOrder(
      @HeaderParam("X-BFX-APIKEY") String apiKey,
      @HeaderParam("X-BFX-PAYLOAD") ParamsDigest payload,
      @HeaderParam("X-BFX-SIGNATURE") ParamsDigest signature,
      BitfinexReplaceOrderRequest newOrderRequest)
      throws IOException, BitfinexExceptionV1;

  /**
   * 取消出价，报价
   * @param apiKey
   * @param payload
   * @param signature
   * @param cancelOfferRequest
   * @return
   * @throws IOException
   * @throws BitfinexExceptionV1
   */
  @POST
  @Path("offer/cancel")
  BitfinexOfferStatusResponse cancelOffer(
      @HeaderParam("X-BFX-APIKEY") String apiKey,
      @HeaderParam("X-BFX-PAYLOAD") ParamsDigest payload,
      @HeaderParam("X-BFX-SIGNATURE") ParamsDigest signature,
      BitfinexCancelOfferRequest cancelOfferRequest)
      throws IOException, BitfinexExceptionV1;

  /**
   * 活跃订单
   * @param apiKey
   * @param payload
   * @param signature
   * @param nonceOnlyRequest
   * @return
   * @throws IOException
   * @throws BitfinexExceptionV1
   */
  @POST
  @Path("orders")
  BitfinexOrderStatusResponse[] activeOrders(
      @HeaderParam("X-BFX-APIKEY") String apiKey,
      @HeaderParam("X-BFX-PAYLOAD") ParamsDigest payload,
      @HeaderParam("X-BFX-SIGNATURE") ParamsDigest signature,
      BitfinexNonceOnlyRequest nonceOnlyRequest)
      throws IOException, BitfinexExceptionV1;

  /**
   * 订单列表
   * @param apiKey
   * @param payload
   * @param signature
   * @param ordersHistoryRequest
   * @return
   * @throws IOException
   * @throws BitfinexExceptionV1
   */
  @POST
  @Path("orders/hist")
  BitfinexOrderStatusResponse[] ordersHist(
      @HeaderParam("X-BFX-APIKEY") String apiKey,
      @HeaderParam("X-BFX-PAYLOAD") ParamsDigest payload,
      @HeaderParam("X-BFX-SIGNATURE") ParamsDigest signature,
      BitfinexOrdersHistoryRequest ordersHistoryRequest)
      throws IOException, BitfinexExceptionV1;

  /**
   * 有效出价，报价
   * @param apiKey
   * @param payload
   * @param signature
   * @param nonceOnlyRequest
   * @return
   * @throws IOException
   * @throws BitfinexExceptionV1
   */
  @POST
  @Path("offers")
  BitfinexOfferStatusResponse[] activeOffers(
      @HeaderParam("X-BFX-APIKEY") String apiKey,
      @HeaderParam("X-BFX-PAYLOAD") ParamsDigest payload,
      @HeaderParam("X-BFX-SIGNATURE") ParamsDigest signature,
      BitfinexNonceOnlyRequest nonceOnlyRequest)
      throws IOException, BitfinexExceptionV1;

  /**
   * 有效持仓 /仓位
   * @param apiKey
   * @param payload
   * @param signature
   * @param nonceOnlyRequest
   * @return
   * @throws IOException
   * @throws BitfinexExceptionV1
   */
  @POST
  @Path("positions")
  BitfinexActivePositionsResponse[] activePositions(
      @HeaderParam("X-BFX-APIKEY") String apiKey,
      @HeaderParam("X-BFX-PAYLOAD") ParamsDigest payload,
      @HeaderParam("X-BFX-SIGNATURE") ParamsDigest signature,
      BitfinexNonceOnlyRequest nonceOnlyRequest)
      throws IOException, BitfinexExceptionV1;

  /**
   * 订单状态
   * @param apiKey
   * @param payload
   * @param signature
   * @param orderStatusRequest
   * @return
   * @throws IOException
   * @throws BitfinexExceptionV1
   */
  @POST
  @Path("order/status")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  BitfinexOrderStatusResponse orderStatus(
      @HeaderParam("X-BFX-APIKEY") String apiKey,
      @HeaderParam("X-BFX-PAYLOAD") ParamsDigest payload,
      @HeaderParam("X-BFX-SIGNATURE") ParamsDigest signature,
      BitfinexOrderStatusRequest orderStatusRequest)
      throws IOException, BitfinexExceptionV1;

  /**
   * 出价状态
   * @param apiKey
   * @param payload
   * @param signature
   * @param offerStatusRequest
   * @return
   * @throws IOException
   * @throws BitfinexExceptionV1
   */
  @POST
  @Path("offer/status")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  BitfinexOfferStatusResponse offerStatus(
      @HeaderParam("X-BFX-APIKEY") String apiKey,
      @HeaderParam("X-BFX-PAYLOAD") ParamsDigest payload,
      @HeaderParam("X-BFX-SIGNATURE") ParamsDigest signature,
      BitfinexOfferStatusRequest offerStatusRequest)
      throws IOException, BitfinexExceptionV1;

  /**
   *  过去的，昔日的 交易
   * @param apiKey
   * @param payload
   * @param signature
   * @param pastTradesRequest
   * @return
   * @throws IOException
   * @throws BitfinexExceptionV1
   */
  @POST
  @Path("mytrades")
  BitfinexTradeResponse[] pastTrades(
      @HeaderParam("X-BFX-APIKEY") String apiKey,
      @HeaderParam("X-BFX-PAYLOAD") ParamsDigest payload,
      @HeaderParam("X-BFX-SIGNATURE") ParamsDigest signature,
      BitfinexPastTradesRequest pastTradesRequest)
      throws IOException, BitfinexExceptionV1;

  /**
   * 过去的出资 /经费 交易
   * @param apiKey
   * @param payload
   * @param signature
   * @param bitfinexPastFundingTradesRequest
   * @return
   * @throws IOException
   * @throws BitfinexExceptionV1
   */
  @POST
  @Path("mytrades_funding")
  BitfinexFundingTradeResponse[] pastFundingTrades(
      @HeaderParam("X-BFX-APIKEY") String apiKey,
      @HeaderParam("X-BFX-PAYLOAD") ParamsDigest payload,
      @HeaderParam("X-BFX-SIGNATURE") ParamsDigest signature,
      BitfinexPastFundingTradesRequest bitfinexPastFundingTradesRequest)
      throws IOException, BitfinexExceptionV1;

  /**
   * 有效的 信用 /贷方 /学分 /信誉
   * @param apiKey
   * @param payload
   * @param signature
   * @param activeCreditsRequest
   * @return
   * @throws IOException
   * @throws BitfinexExceptionV1
   */
  @POST
  @Path("credits")
  BitfinexCreditResponse[] activeCredits(
      @HeaderParam("X-BFX-APIKEY") String apiKey,
      @HeaderParam("X-BFX-PAYLOAD") ParamsDigest payload,
      @HeaderParam("X-BFX-SIGNATURE") ParamsDigest signature,
      BitfinexActiveCreditsRequest activeCreditsRequest)
      throws IOException, BitfinexExceptionV1;

  /**
   * 保证金信息
   * @param apiKey
   * @param payload
   * @param signature
   * @param marginInfosRequest
   * @return
   * @throws IOException
   * @throws BitfinexExceptionV1
   */
  @POST
  @Path("margin_infos")
  BitfinexMarginInfosResponse[] marginInfos(
      @HeaderParam("X-BFX-APIKEY") String apiKey,
      @HeaderParam("X-BFX-PAYLOAD") ParamsDigest payload,
      @HeaderParam("X-BFX-SIGNATURE") ParamsDigest signature,
      BitfinexMarginInfosRequest marginInfosRequest)
      throws IOException, BitfinexExceptionV1;

  /**
   * 提，取（银行账户中的钱款）
   * @param apiKey
   * @param payload
   * @param signature
   * @param withdrawalRequest
   * @return
   * @throws IOException
   * @throws BitfinexExceptionV1
   */
  @POST
  @Path("withdraw")
  BitfinexWithdrawalResponse[] withdraw(
      @HeaderParam("X-BFX-APIKEY") String apiKey,
      @HeaderParam("X-BFX-PAYLOAD") ParamsDigest payload,
      @HeaderParam("X-BFX-SIGNATURE") ParamsDigest signature,
      BitfinexWithdrawalRequest withdrawalRequest)
      throws IOException, BitfinexExceptionV1;

  /**
   * 请求存款
   * @param apiKey
   * @param payload
   * @param signature
   * @param depositRequest
   * @return
   * @throws IOException
   * @throws BitfinexExceptionV1
   */
  @POST
  @Path("deposit/new")
  BitfinexDepositAddressResponse requestDeposit(
      @HeaderParam("X-BFX-APIKEY") String apiKey,
      @HeaderParam("X-BFX-PAYLOAD") ParamsDigest payload,
      @HeaderParam("X-BFX-SIGNATURE") ParamsDigest signature,
      BitfinexDepositAddressRequest depositRequest)
      throws IOException, BitfinexExceptionV1;

  /**
   * 存款取款记录
   * @param apiKey
   * @param payload
   * @param signature
   * @param request
   * @return
   * @throws IOException
   * @throws BitfinexExceptionV1
   */
  @POST
  @Path("history/movements")
  BitfinexDepositWithdrawalHistoryResponse[] depositWithdrawalHistory(
      @HeaderParam("X-BFX-APIKEY") String apiKey,
      @HeaderParam("X-BFX-PAYLOAD") ParamsDigest payload,
      @HeaderParam("X-BFX-SIGNATURE") ParamsDigest signature,
      BitfinexDepositWithdrawalHistoryRequest request)
      throws IOException, BitfinexExceptionV1;

  /**
   *  余额历史记录
   * @param apiKey
   * @param payload
   * @param signature
   * @param balanceHistoryRequest
   * @return
   * @throws IOException
   * @throws BitfinexExceptionV1
   */
  @POST
  @Path("history")
  BitfinexBalanceHistoryResponse[] balanceHistory(
      @HeaderParam("X-BFX-APIKEY") String apiKey,
      @HeaderParam("X-BFX-PAYLOAD") ParamsDigest payload,
      @HeaderParam("X-BFX-SIGNATURE") ParamsDigest signature,
      BitfinexBalanceHistoryRequest balanceHistoryRequest)
      throws IOException, BitfinexExceptionV1;
}
