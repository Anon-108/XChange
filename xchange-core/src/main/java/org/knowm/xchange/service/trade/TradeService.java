package org.knowm.xchange.service.trade;

import java.io.IOException;
import java.util.Collection;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.account.OpenPositions;
import org.knowm.xchange.dto.meta.CurrencyPairMetaData;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.MarketOrder;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.knowm.xchange.dto.trade.StopOrder;
import org.knowm.xchange.dto.trade.UserTrades;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.exceptions.NotAvailableFromExchangeException;
import org.knowm.xchange.exceptions.NotYetImplementedForExchangeException;
import org.knowm.xchange.service.BaseService;
import org.knowm.xchange.service.trade.params.CancelAllOrders;
import org.knowm.xchange.service.trade.params.CancelOrderByIdParams;
import org.knowm.xchange.service.trade.params.CancelOrderByInstrument;
import org.knowm.xchange.service.trade.params.CancelOrderParams;
import org.knowm.xchange.service.trade.params.DefaultCancelOrderParamId;
import org.knowm.xchange.service.trade.params.TradeHistoryParams;
import org.knowm.xchange.service.trade.params.TradeHistoryParamsAll;
import org.knowm.xchange.service.trade.params.orders.DefaultQueryOrderParam;
import org.knowm.xchange.service.trade.params.orders.OpenOrdersParams;
import org.knowm.xchange.service.trade.params.orders.OrderQueryParams;

/**
 * Interface to provide the following to {@link org.knowm.xchange.Exchange}:
 * * 向 {@link org.knowm.xchange.Exchange} 提供以下内容的接口：
 *
 * <ul>
 *   <li>Retrieve the user's open orders on the exchange
 *   <li>Cancel user's open orders on the exchange
 *   <li>Place market orders on the exchange
 *   <li>Place limit orders on the exchange
 *   <li>Change limit orders on the exchange
 *   <li>检索用户在交易所的未结订单
 *   * <li>取消用户在交易所的未结订单
 *   * <li>在交易所下市价单
 *   * <li>在交易所下限价单
 *   * <li>在交易所更改限价单
 * </ul>
 *
 * <p>The implementation of this service is expected to be based on a client polling mechanism of some kind
 * * <p>该服务的实现预计将基于某种客户端轮询机制
 */
public interface TradeService extends BaseService {

  /**
   * Gets the open orders
   * 获取未结订单
   *
   * @return the open orders, null if some sort of error occurred. Implementers should log the  error.
   * |* @return 未结订单，如果发生某种错误，则返回 null。 实施者应该记录错误。
   *
   * @throws ExchangeException - Indication that the exchange reported some kind of error with the  request or response
   * * @throws ExchangeException - 表示交换报告了请求或响应的某种错误
   *
   * @throws NotAvailableFromExchangeException - Indication that the exchange does not support the  requested function or data
   * * @throws NotAvailableFromExchangeException - 表示交易所不支持请求的功能或数据
   *
   * @throws NotYetImplementedForExchangeException - Indication that the exchange supports the   requested function or data, but it has not yet been implemented
   * * @throws NotYetImplementedForExchangeException - 表示交易所支持请求的功能或数据，但尚未实现
   *
   * @throws IOException - Indication that a networking error occurred while fetching JSON data
   * * @throws IOException - 指示在获取 JSON 数据时发生网络错误
   */
  default OpenOrders getOpenOrders() throws IOException {
    throw new NotYetImplementedForExchangeException("getOpenOrders");
  }

  /**
   * Gets the open orders
   * * 获取未结订单
   *
   * @param params The parameters describing the filter. Note that {@link OpenOrdersParams} is an empty interface. Exchanges should implement its own params object. Params should be create with {@link #createOpenOrdersParams()}.
   *               * @param params 描述过滤器的参数。 请注意，{@link OpenOrdersParams} 是一个空接口。 交易所应该实现自己的 params 对象。 应使用 {@link #createOpenOrdersParams()} 创建参数。
   *
   * @return the open orders, null if some sort of error occurred. Implementers should log the   error.
   * * @return 未结订单，如果发生某种错误，则返回 null。 实施者应该记录错误。
   *
   * @throws ExchangeException - Indication that the exchange reported some kind of error with the request or response
   * * @throws ExchangeException - 表示交换报告了请求或响应的某种错误
   *
   * @throws NotAvailableFromExchangeException - Indication that the exchange does not support the  requested function or data
   * * @throws NotAvailableFromExchangeException - 表示交易所不支持请求的功能或数据
   *
   * @throws NotYetImplementedForExchangeException - Indication that the exchange supports the  requested function or data, but it has not yet been implemented
   * * @throws NotYetImplementedForExchangeException - 表示交易所支持请求的功能或数据，但尚未实现
   *
   * @throws IOException - Indication that a networking error occurred while fetching JSON data
   * * @throws IOException - 指示在获取 JSON 数据时发生网络错误
   */
  default OpenOrders getOpenOrders(OpenOrdersParams params) throws IOException {
    throw new NotYetImplementedForExchangeException("getOpenOrders");
  }

  /**
   * Returns required cancel order parameter as classes
   * * 返回所需的取消订单参数作为类
   *
   * <p>Different trading services requires different parameters for order cancellation.
    To provide  generic operation of the trade service interface. This method returns {@link Class} of the parameter objects as an array.
   This class information can be utilized by the caller of {@link #cancelOrder(CancelOrderParams)} to create instances of the required parameters such as {@link CancelOrderByIdParams},
   {@link CancelOrderByInstrument} etc...
   <p>不同的交易服务需要不同的订单取消参数。
   提供交易服务接口的通用操作。 此方法将参数对象的 {@link Class} 作为数组返回。
   {@link #cancelOrder(CancelOrderParams)} 的调用者可以使用此类信息来创建所需参数的实例，例如 {@link CancelOrderByIdParams}，
   {@link CancelOrderByInstrument} 等等...
   *
   * @return Class types for the required parameter classes. Default implementation returns an array with a single {@link CancelOrderByIdParams} element
   * * @return 所需参数类的类类型。 默认实现返回具有单个 {@link CancelOrderByIdParams} 元素的数组
   */
  default Class[] getRequiredCancelOrderParamClasses() {
    return new Class[] {CancelOrderByIdParams.class};
  }

  /** Get all openPositions of the exchange
   * 获取交易所的所有openPosition */
  default OpenPositions getOpenPositions() throws IOException {
    throw new NotYetImplementedForExchangeException();
  }

  /**
   * Place a market order
   * * 下市价单
   *
   * <p>If your orders amount does to meet the restrictions dictated by {@link CurrencyPairMetaData}
    then the exchange will reject your order. Use {@link org.knowm.xchange.utils.OrderValuesHelper}
    to validate and / or adjust it while you'r building an order.
   <p>如果您的订单金额符合 {@link CurrencyPairMetaData} 规定的限制
   那么交易所将拒绝您的订单。 使用 {@link org.knowm.xchange.utils.OrderValuesHelper}
   在您建立订单时验证和/或调整它。
   *
   * @param marketOrder
   * @return the order ID
   *        订单编号
   *
   * @throws ExchangeException - Indication that the exchange reported some kind of error with the  request or response
   * * @throws ExchangeException - 表示交换报告了请求或响应的某种错误
   *
   * @throws NotAvailableFromExchangeException - Indication that the exchange does not support the  requested function or data
   * * @throws NotAvailableFromExchangeException - 表示交易所不支持请求的功能或数据
   *
   * @throws NotYetImplementedForExchangeException - Indication that the exchange supports the   requested function or data, but it has not yet been implemented
   * * @throws NotYetImplementedForExchangeException - 表示交易所支持请求的功能或数据，但尚未实现
   *
   * @throws IOException - Indication that a networking error occurred while fetching JSON data @see org.knowm.xchange.utils.OrderValuesHelper
   *  @throws IOException - 指示在获取 JSON 数据时发生网络错误
   * @see org.knowm.xchange.utils.OrderValuesHelper
   */
  default String placeMarketOrder(MarketOrder marketOrder) throws IOException {
    throw new NotYetImplementedForExchangeException("placeMarketOrder 下市价单");
  }

  /**
   * Place a limit order
   ** 下限价单
   *
   * <p>If your orders amount or limit price does to meet the restrictions dictated by {@link CurrencyPairMetaData} then the exchange will reject your order.
    Use {@link org.knowm.xchange.utils.OrderValuesHelper} to validate and / or adjust those values while you'r building an order.
   <p>如果您的订单金额或限价确实符合 {@link CurrencyPairMetaData} 规定的限制，那么交易所将拒绝您的订单。
   使用 {@link org.knowm.xchange.utils.OrderValuesHelper} 在您构建订单时验证和/或调整这些值。
   *
   * @param limitOrder
   * @return the order ID 订单编号
   *
   * @throws ExchangeException - Indication that the exchange reported some kind of error with the     request or response
   * * @throws ExchangeException - 表示交换报告了请求或响应的某种错误
   *
   * @throws NotAvailableFromExchangeException - Indication that the exchange does not support the  requested function or data
   * * @throws NotAvailableFromExchangeException - 表示交易所不支持请求的功能或数据
   *
   * @throws NotYetImplementedForExchangeException - Indication that the exchange supports the   requested function or data, but it has not yet been implemented
   * * @throws NotYetImplementedForExchangeException - 表示交易所支持请求的功能或数据，但尚未实现
   *
   * @throws IOException - Indication that a networking error occurred while fetching JSON data
   * * @throws IOException - 指示在获取 JSON 数据时发生网络错误
   *
   * @see org.knowm.xchange.utils.OrderValuesHelper
   */
  default String placeLimitOrder(LimitOrder limitOrder) throws IOException {
    throw new NotYetImplementedForExchangeException("placeLimitOrder 下限价单");
  }

  /**
   * Place a stop order
   * * 下止损单
   *
   * <p>If your orders amount or spot price does to meet the restrictions dictated by {@link CurrencyPairMetaData} then the exchange will reject your order.
   Use {@link org.knowm.xchange.utils.OrderValuesHelper} to validate and / or adjust those values while you'r building an order.
   <p>如果您的订单金额或现货价格符合 {@link CurrencyPairMetaData} 规定的限制，那么交易所将拒绝您的订单。
   使用 {@link org.knowm.xchange.utils.OrderValuesHelper} 在您构建订单时验证和/或调整这些值。
   *
   * @param stopOrder
   * @return the order ID
   * 订单编号
   *
   * @throws ExchangeException - Indication that the exchange reported some kind of error with the   request or response
   * * @throws ExchangeException - 表示交换报告了请求或响应的某种错误
   *
   * @throws NotAvailableFromExchangeException - Indication that the exchange does not support the  requested function or data
   * * @throws NotAvailableFromExchangeException - 表示交易所不支持请求的功能或数据
   *
   * @throws NotYetImplementedForExchangeException - Indication that the exchange supports the   requested function or data, but it has not yet been implemented
   * * @throws NotYetImplementedForExchangeException - 表示交易所支持请求的功能或数据，但尚未实现
   *
   * @throws IOException - Indication that a networking error occurred while fetching JSON data
   * * @throws IOException - 指示在获取 JSON 数据时发生网络错误
   *
   * @see org.knowm.xchange.utils.OrderValuesHelper
   */
  default String placeStopOrder(StopOrder stopOrder) throws IOException {
    throw new NotYetImplementedForExchangeException("placeStopOrder 放置止损单");
  }

  /**
   * Modify or cancel/replace an existing limit order
   * * 修改或取消/替换现有限价单
   *
   * @implNote Some exchanges have API methods that allow to modify an order or cancel an existing  one and create a new one in one request.
   * 一些交易所具有允许修改订单或取消现有订单并在一个请求中创建新订单的 API 方法。
   *
        <p>Based on exchange API there are 3 ways, how this function works:
            基于 exchange API 有 3 种方式，这个函数是如何工作的：
        <ol>
          <li>Exchange supports existing order modify operation. Then function returns {@code
              limitOrder} order ID.
          <li>Exchange supports order cancel/replace by one request. Then function returns new
              order ID.
          <li>Exchange doesn't support any of these operations. Then function performs
              cancel/replace by two separate requests, and returns new order ID (default behavior)
        </ol>
        <ol>
        <li>交易所支持现有的订单修改操作。 然后函数返回 {@code
        limitOrder} 订单 ID。
        <li>交易所支持单请求取消/替换订单。 然后函数返回新的
        订单编号。
        <li>Exchange 不支持任何这些操作。 然后函数执行
        通过两个单独的请求取消/替换，并返回新的订单 ID（默认行为）
        </ol>
   *
   * @param limitOrder Order's data to change
   *                   要更改的订单数据
   *
   * @return Order ID
   *          订单编号
   *
   * @throws ExchangeException Indication that the exchange reported some kind of error with the  request or response
   * * @throws ExchangeException 表示交换报告了请求或响应的某种错误
   *
   * @throws NotAvailableFromExchangeException Indication that the exchange does not support the  requested function or data
   * * @throws NotAvailableFromExchangeException 表示交易所不支持请求的功能或数据
   *
   * @throws NotYetImplementedForExchangeException Indication that the exchange supports the  requested function or data, but it has not yet been implemented
   * * @throws NotYetImplementedForExchangeException 表示交易所支持请求的功能或数据，但尚未实现
   *
   * @throws IOException Indication that a networking error occurred while fetching JSON data
   * * @throws IOException 指示在获取 JSON 数据时发生网络错误
   */
  default String changeOrder(LimitOrder limitOrder) throws IOException {
    cancelOrder(limitOrder.getId());
    return placeLimitOrder(limitOrder);
  }

  /**
   * cancels order with matching orderId (conveniance method, typical just delegate to cancelOrder(CancelOrderByIdParams))
   * * 取消匹配 orderId 的订单（方便的方法，典型的只是委托给 cancelOrder(CancelOrderByIdParams)）
   *
   * @param orderId
   * @return true if order was successfully cancelled, false otherwise.
   *    如果订单成功取消，则为 true，否则为 false。
   *
   * @throws ExchangeException - Indication that the exchange reported some kind of error with the  request or response
   * * @throws ExchangeException - 表示交换报告了请求或响应的某种错误
   *
   * @throws NotAvailableFromExchangeException - Indication that the exchange does not support the  requested function or data
   * * @throws NotAvailableFromExchangeException - 表示交易所不支持请求的功能或数据
   *
   * @throws NotYetImplementedForExchangeException - Indication that the exchange supports the   requested function or data, but it has not yet been implemented
   * * @throws NotYetImplementedForExchangeException - 表示交易所支持请求的功能或数据，但尚未实现
   *
   * @throws IOException - Indication that a networking error occurred while fetching JSON data
   *            获取 JSON 数据时发生网络错误的指示
   */
  default boolean cancelOrder(String orderId) throws IOException {
    return cancelOrder(new DefaultCancelOrderParamId(orderId));
  }

  /**
   * cancels order with matching orderParams
   * 取消具有匹配 orderParams 的订单
   *
   * @param orderParams
   * @return true if order was successfully cancelled, false otherwise.
   *        * @return 如果订单成功取消，则返回 true，否则返回 false。
   *
   * @throws ExchangeException - Indication that the exchange reported some kind of error with the   request or response
   *      * @throws ExchangeException - 表示交换报告了请求或响应的某种错误
   *
   * @throws NotAvailableFromExchangeException - Indication that the exchange does not support the  requested function or data
   * * @throws NotAvailableFromExchangeException - 表示交易所不支持请求的功能或数据
   *
   * @throws NotYetImplementedForExchangeException - Indication that the exchange supports the  requested function or data, but it has not yet been implemented
   * * @throws NotYetImplementedForExchangeException - 表示交易所支持请求的功能或数据，但尚未实现
   *
   * @throws IOException - Indication that a networking error occurred while fetching JSON data
   * * @throws IOException - 指示在获取 JSON 数据时发生网络错误
   */
  default boolean cancelOrder(CancelOrderParams orderParams) throws IOException {
    throw new NotYetImplementedForExchangeException("cancelOrder");
  }

  default Collection<String> cancelAllOrders(CancelAllOrders orderParams) throws IOException {
    throw new NotYetImplementedForExchangeException("cancelAllOpenOrders 取消所有未结订单");
  }

  /**
   * Fetch the history of user trades.
   * * 获取用户交易历史。
   *
   * <p>If you are calling this method for single exchange, known at the development time, you may
    pass an object of specific *TradeHistoryParam class that is nested it that exchange's trade service.
   <p>如果您在开发时调用此方法进行单次交换，您可以
   传递嵌套在该交易所的交易服务中的特定 *TradeHistoryParam 类的对象。
   *
   * <p>If, however, you are fetching user trade history from many exchanges using the same code,
   you will find useful to create the parameter object with {@link #createTradeHistoryParams()}
   and check which parameters are required or supported using instanceof operator. See
   subinterfaces of {@link TradeHistoryParams}. Note that whether an interface is required or
   supported will vary from exchange to exchange and it's described only through the javadoc.
   <p>但是，如果您使用相同的代码从多个交易所获取用户交易历史记录，
   您会发现使用 {@link #createTradeHistoryParams()} 创建参数对象很有用
   并使用 instanceof 运算符检查需要或支持哪些参数。 看
   {@link TradeHistoryParams} 的子接口。 请注意，是否需要接口或
   支持的交易所因交易所而异，仅通过 javadoc 进行描述。

   *
   * <p>There is also implementation of all the common interfaces, {@link TradeHistoryParamsAll} ,
    that, with all properties set non-null, should work with any exchange.
   <p>还有所有通用接口的实现，{@link TradeHistoryParamsAll}，
   在所有属性都设置为非 null 的情况下，它应该适用于任何交换。
   *
   * <p>Some exchanges allow extra parameters, not covered by any common interface. To access them,
    you will have to use the object returned by {@link #createTradeHistoryParams()} and cast it to the exchange-specific type.
   <p>一些交换允许额外的参数，任何通用接口都没有涵盖。 要访问它们，
   您必须使用 {@link #createTradeHistoryParams()} 返回的对象并将其转换为特定于交易所的类型。
   *
   * @param params The parameters describing the filter. Note that {@link TradeHistoryParams} is an
       empty interface. Exact set of interfaces that are required or supported by this method is
       described by the type of object returned from {@link #createTradeHistoryParams()} and the
        javadoc of the method.
   @param params 描述过滤器的参数。 请注意，{@link TradeHistoryParams} 是
   空界面。 此方法需要或支持的确切接口集是
   由 {@link #createTradeHistoryParams()} 返回的对象类型和
   方法的 javadoc。

    @return UserTrades as returned by the exchange API
   @return 交易所 API 返回的 UserTrades

   * @throws ExchangeException - Indication that the exchange reported some kind of error with the  request or response
   * * @throws ExchangeException - 表示交换报告了请求或响应的某种错误
   *
   * @throws NotAvailableFromExchangeException - Indication that the exchange does not support the  requested function or data
   * * @throws NotAvailableFromExchangeException - 表示交易所不支持请求的功能或数据
   *
   * @throws NotYetImplementedForExchangeException - Indication that the exchange supports the   requested function or data, but it has not yet been implemented
   * * @throws NotYetImplementedForExchangeException - 表示交易所支持请求的功能或数据，但尚未实现
   *
   * @throws IOException - Indication that a networking error occurred while fetching JSON data
   * * @throws IOException - 指示在获取 JSON 数据时发生网络错误
   *
   * @see #createTradeHistoryParams()
   * @see TradeHistoryParamsAll
   */
  default UserTrades getTradeHistory(TradeHistoryParams params) throws IOException {
    throw new NotYetImplementedForExchangeException("getTradeHistory 获取贸易历史");
  }

  /**
   * Create {@link TradeHistoryParams} object specific to this exchange. Object created by this method may be used to discover supported and required
    {@link #getTradeHistory(TradeHistoryParams)} parameters and should be passed only to the method in the same class as the createTradeHistoryParams that created the object.
   创建特定于此交易所的 {@link TradeHistoryParams} 对象。 此方法创建的对象可用于发现支持的和需要的
   {@link #getTradeHistory(TradeHistoryParams)} 参数，并且应该只传递给与创建对象的 createTradeHistoryParams 相同的类中的方法。

   */
  default TradeHistoryParams createTradeHistoryParams() {
    throw new NotYetImplementedForExchangeException("createTradeHistoryParams 创建交易历史参数");
  }

  /**
   * Create {@link OpenOrdersParams} object specific to this exchange. Object created by this method
    may be used to discover supported and required {@link #getOpenOrders(OpenOrdersParams)}
    parameters and should be passed only to the method in the same class as the  createOpenOrdersParams that created the object.
   创建特定于此交换的 {@link OpenOrdersParams} 对象。 此方法创建的对象
   可用于发现支持和必需的 {@link #getOpenOrders(OpenOrdersParams)}
   参数，并且应该只传递给与创建对象的 createOpenOrdersParams 相同的类中的方法。

   */
  default OpenOrdersParams createOpenOrdersParams() {
    throw new NotYetImplementedForExchangeException("createOpenOrdersParams 创建未结订单参数");
  }

  /**
   * Verify the order against the exchange meta data. Most implementations will require that {@link org.knowm.xchange.Exchange#remoteInit()} be called before this method
   * * 根据交换元数据验证订单。 大多数实现都需要在此方法之前调用 {@link org.knowm.xchange.Exchange#remoteInit()}
   */
  default void verifyOrder(LimitOrder limitOrder) {
    throw new NotYetImplementedForExchangeException("verifyOrder 验证订单");
  }

  /**
   * Verify the order against the exchange meta data. Most implementations will require that {@link org.knowm.xchange.Exchange#remoteInit()} be called before this method
   * * 根据交换元数据验证订单。 大多数实现都需要在此方法之前调用 {@link org.knowm.xchange.Exchange#remoteInit()}
   */
  default void verifyOrder(MarketOrder marketOrder) {
    throw new NotYetImplementedForExchangeException("verifyOrder 验证订单");
  }

  /**
   * get's the latest order form the order book that with matching orderId
   * * 获取与orderId匹配的订单簿的最新订单
   *
   * @return the order as it is on the exchange.
   * * @return 交易中的订单。
   *
   * @throws ExchangeException - Indication that the exchange reported some kind of error with the  request or response
   * * @throws ExchangeException - 表示交换报告了请求或响应的某种错误
   *
   * @throws NotAvailableFromExchangeException - Indication that the exchange does not support the  requested function or data
   * * @throws NotAvailableFromExchangeException - 表示交易所不支持请求的功能或数据
   *
   * @throws NotYetImplementedForExchangeException - Indication that the exchange supports the   requested function or data, but it has not yet been implemented
   * * @throws NotYetImplementedForExchangeException - 表示交易所支持请求的功能或数据，但尚未实现
   *
   * @throws IOException - Indication that a networking error occurred while fetching JSON data
   * * @throws IOException - 指示在获取 JSON 数据时发生网络错误
   */
  default Collection<Order> getOrder(String... orderIds) throws IOException {
    return getOrder(toOrderQueryParams(orderIds));
  }

  static OrderQueryParams[] toOrderQueryParams(String... orderIds) {
    OrderQueryParams[] res = new OrderQueryParams[orderIds.length];
    for (int i = 0; i < orderIds.length; i++) {
      String orderId = orderIds[i];
      res[i] = new DefaultQueryOrderParam(orderId);
    }
    return res;
  }

  static String[] toOrderIds(OrderQueryParams... orderQueryParams) {
    String[] orderIds = new String[orderQueryParams.length];
    int index = 0;
    for (OrderQueryParams orderQueryParam : orderQueryParams) {
      orderIds[index++] = orderQueryParam.getOrderId();
    }
    return orderIds;
  }

  /**
   * Returns required get order parameter as classes
   * * 返回所需的获取订单参数作为类
   *
   * <p>Different trading services requires different parameters for order querying. To provide
    generic operation of the trade service interface, This method returns {@link Class} of the
    parameter objects as an array. This class information can be utilized by the caller of {@link
    #getOrder(OrderQueryParams...)} to create instances of the required parameter such as {@link
    org.knowm.xchange.service.trade.params.orders.OrderQueryParamCurrencyPair}, {@link
    org.knowm.xchange.service.trade.params.orders.OrderQueryParamInstrument} etc...
   <p>不同的交易服务需要不同的订单查询参数。 提供
   交易服务接口的通用操作，该方法返回{@link Class}的
   参数对象作为数组。 {@link 的调用者可以使用此类信息
  #getOrder(OrderQueryParams...)} 创建所需参数的实例，例如 {@link
  org.knowm.xchange.service.trade.params.orders.OrderQueryParamCurrencyPair}, {@link
  org.knowm.xchange.service.trade.params.orders.OrderQueryParamInstrument} 等...
   *
   * @return Class type for the required parameter class. Default implementation returns an instance  of {@link OrderQueryParams} element
   * * @return 所需参数类的类类型。 默认实现返回 {@link OrderQueryParams} 元素的实例
   */
  default Class getRequiredOrderQueryParamClass() {
    return OrderQueryParams.class;
  }

  /**
   * get's the latest order form the order book that with matching orderQueryParams
   * * 从订单簿中获取与订单查询参数匹配的最新订单
   *
   * @return the order as it is on the exchange.
   * * @return 交易中的订单。
   *
   * @throws ExchangeException - Indication that the exchange reported some kind of error with the  request or response
   * * @throws ExchangeException - 表示交换报告了请求或响应的某种错误
   *
   * @throws NotAvailableFromExchangeException - Indication that the exchange does not support the   requested function or data
   * * @throws NotAvailableFromExchangeException - 表示交易所不支持请求的功能或数据
   *
   * @throws NotYetImplementedForExchangeException - Indication that the exchange supports the    requested function or data, but it has not yet been implemented
   * * @throws NotYetImplementedForExchangeException - 表示交易所支持请求的功能或数据，但尚未实现
   *
   * @throws IOException - Indication that a networking error occurred while fetching JSON data
   * * @throws IOException - 指示在获取 JSON 数据时发生网络错误
   */
  default Collection<Order> getOrder(OrderQueryParams... orderQueryParams) throws IOException {
    throw new NotAvailableFromExchangeException("getOrder");
  }
}
