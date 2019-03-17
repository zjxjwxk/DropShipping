package com.zjut.dropshipping.service.impl;

import com.alipay.api.AlipayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.demo.trade.utils.ZxingUtils;
import com.zjut.dropshipping.common.Const;
import com.zjut.dropshipping.common.ResponseCode;
import com.zjut.dropshipping.common.ServerResponse;
import com.zjut.dropshipping.dataobject.*;
import com.zjut.dropshipping.dto.*;
import com.zjut.dropshipping.repository.*;
import com.zjut.dropshipping.service.ExchangeRateService;
import com.zjut.dropshipping.service.OrderService;
import com.zjut.dropshipping.utils.BigDecimalUtil;
import com.zjut.dropshipping.utils.FTPUtil;
import com.zjut.dropshipping.utils.PropertiesUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zjxjwxk
 */
@Service("OrderService")
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;
    private final BuyerRepository buyerRepository;
    private final GoodsRepository goodsRepository;
    private final ProducerRepository producerRepository;
    private final LogisticRepository logisticRepository;
    private final OrderItemRepository orderItemRepository;
    private final SpecificationRepository specificationRepository;
    private final GoodsSpecItemRepository goodsSpecItemRepository;
    private final AgentRepository agentRepository;
    private final RefundStatusRepository refundStatusRepository;
    private final EvaluationRepository evaluationRepository;
    private final GoodsEvaluationRepository goodsEvaluationRepository;
    private final CountryCurrencyRepository countryCurrencyRepository;
    private final ExchangeRateRepository exchangeRateRepository;

    private final ExchangeRateService exchangeRateService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            BuyerRepository buyerRepository,
                            GoodsRepository goodsRepository,
                            ProducerRepository producerRepository,
                            LogisticRepository logisticRepository,
                            OrderItemRepository orderItemRepository,
                            SpecificationRepository specificationRepository,
                            GoodsSpecItemRepository goodsSpecItemRepository,
                            AgentRepository agentRepository,
                            RefundStatusRepository refundStatusRepository,
                            EvaluationRepository evaluationRepository,
                            GoodsEvaluationRepository goodsEvaluationRepository,
                            CountryCurrencyRepository countryCurrencyRepository,
                            ExchangeRateRepository exchangeRateRepository,
                            ExchangeRateService exchangeRateService) {
        this.orderRepository = orderRepository;
        this.buyerRepository = buyerRepository;
        this.goodsRepository = goodsRepository;
        this.producerRepository = producerRepository;
        this.logisticRepository = logisticRepository;
        this.orderItemRepository = orderItemRepository;
        this.specificationRepository = specificationRepository;
        this.goodsSpecItemRepository = goodsSpecItemRepository;
        this.agentRepository = agentRepository;
        this.refundStatusRepository = refundStatusRepository;
        this.evaluationRepository = evaluationRepository;
        this.goodsEvaluationRepository = goodsEvaluationRepository;
        this.countryCurrencyRepository = countryCurrencyRepository;
        this.exchangeRateRepository = exchangeRateRepository;
        this.exchangeRateService = exchangeRateService;
    }


    @Override
    public ServerResponse agentSaveOrder(Integer orderId, Integer agentId,
                                         OrderItem[] orderItemList,
                                         String remark, String buyerName,
                                         String buyerPhone, String address) {
        Buyer buyer = buyerRepository.findByNameAndPhoneAndAddress(buyerName, buyerPhone, address);
        if (buyer == null) {
            Buyer buyer1 = new Buyer();
            buyer1.setName(buyerName);
            buyer1.setPhone(buyerPhone);
            buyer1.setAddress(address);
            buyer = buyerRepository.save(buyer1);
        }

        Order order;
        // 创建订单
        if (orderId == null) {
            order = new Order();
        // 修改订单
        } else {
            order = orderRepository.findOneByOrderId(orderId);
            if (order == null) {
                return ServerResponse.createByErrorMessage("该订单不存在");
            }
        }
        order.setAgentId(agentId);

        order.setBuyerId(buyer.getId());
        order.setProducerId(goodsRepository.findByGoodsId(orderItemList[0].getGoodsId()).getProducerId());
        order.setState(Const.OrderState.TO_BE_CONFIRMED);
        order.setRemark(remark);

        order = orderRepository.save(order);
        this.saveOrderItemList(order.getOrderId(), orderItemList);

        return ServerResponse.createBySuccess(order);
    }

    @Override
    public ServerResponse agentGetOrderList(Integer agentId) {
        List<Order> orderList = orderRepository.findByAgentId(agentId);
        if (orderList.size() == 0) {
            return ServerResponse.createByErrorMessage("暂无订单");
        }
        return ServerResponse.createBySuccess(this.getOrderDTOList(orderList));
    }

    @Override
    public ServerResponse getOrderDetail(String country, Integer orderId) {
        Order order = orderRepository.findOneByOrderId(orderId);
        if (order == null) {
            return ServerResponse.createByErrorMessage("该订单不存在");
        }
        return ServerResponse.createBySuccess(this.getOrderDetailDTO(country, order));
    }

    @Override
    public ServerResponse pay(Integer agentId, Integer orderId, String path) {
        Map<String, String> resultMap = new HashMap<>();
        Order order = orderRepository.findByAgentIdAndOrderId(agentId, orderId);
        if (order == null) {
            return ServerResponse.createByErrorMessage("用户没有该订单");
        }
        resultMap.put("orderId", String.valueOf(orderId));

        // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        // 需保证商户系统端不能重复，建议通过数据库sequence生成，
        String outTradeNo = order.getOrderId().toString();

        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
        String subject = "小机灵扫码支付，订单号：" + outTradeNo;

        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = this.getTotalAmount(orderId).toString();

        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = "0";

        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";

        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body = "订单" + outTradeNo + "购买商品共" + totalAmount + "元";

        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "test_operator_id";

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "test_store_id";

        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2088100200300400500");

        // 支付超时，定义为120分钟
        String timeoutExpress = "120m";

        // 商品明细列表，需填写购买商品详细信息
        List<GoodsDetail> goodsDetailList = new ArrayList<>();

        List<OrderItem> orderItemList = orderItemRepository.findByOrderId(orderId);
        for (OrderItem orderItem :
                orderItemList) {
            // 创建一个商品信息，参数含义分别为商品id（使用国标）、名称、单价（单位为分）、数量，如果需要添加商品类别，详见GoodsDetail
            GoodsDetail goodsDetail = GoodsDetail.newInstance(orderItem.getGoodsId().toString(), goodsRepository.findNameByGoodsId(orderItem.getGoodsId()),
                    BigDecimalUtil.mul(this.getItemPrice(orderItem), 100d).longValue(),
                    orderItem.getAmount());
            // 创建好一个商品后添加至商品明细列表
            goodsDetailList.add(goodsDetail);
        }

        /*
        扫码支付
         */
        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
                .setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
                .setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body)
                .setOperatorId(operatorId).setStoreId(storeId).setExtendParams(extendParams)
                .setTimeoutExpress(timeoutExpress)
                //支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
                .setNotifyUrl(PropertiesUtil.getProperty("alipay.callback.url"))
                .setGoodsDetailList(goodsDetailList);

        /* 一定要在创建AlipayTradeService之前调用Configs.init()设置默认参数
           Configs会读取classpath下的zfbinfo.properties文件配置信息，如果找不到该文件则确认该文件是否在classpath目录
         */
        Configs.init("zfbinfo.properties");

        /* 使用Configs提供的默认参数
           AlipayTradeService可以使用单例或者为静态成员对象，不需要反复new
         */
        AlipayTradeService tradeService = new AlipayTradeServiceImpl.ClientBuilder().setCharset("utf-8").build();

        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                logger.info("支付宝预下单成功:");

                AlipayTradePrecreateResponse response = result.getResponse();
                dumpResponse(response);

                File folder = new File(path);
                if (!folder.exists()) {
                    folder.setWritable(true);
                    folder.mkdirs();
                }

                // 需要修改为运行机器上的路径
                String qrPath = String.format(path + "/qr-%s.png", response.getOutTradeNo());
                String qrFileName = String.format("qr-%s.png", response.getOutTradeNo());
                ZxingUtils.getQRCodeImge(response.getQrCode(), 256, qrPath);

                String type = Const.UploadType.QRCODE;
                File targetFile = new File(path, qrFileName);
                List<File> fileList = new ArrayList<>();
                fileList.add(targetFile);
                try {
                    FTPUtil.upload(type, fileList, orderId);
                } catch (IOException e) {
                    logger.error("上传二维码异常", e);
                    e.printStackTrace();
                }
                logger.info("qrPath:" + qrPath);
                String qrUrl = PropertiesUtil.getProperty("ftp.server.http.prefix") + type + "/" + orderId + "/" + targetFile.getName();
                resultMap.put("qrUrl", qrUrl);
                return ServerResponse.createBySuccess(resultMap);

            case FAILED:
                logger.error("支付宝预下单失败!!!");
                return ServerResponse.createByErrorMessage("支付宝预下单失败!!!");

            case UNKNOWN:
                logger.error("系统异常，预下单状态未知!!!");
                return ServerResponse.createByErrorMessage("系统异常，预下单状态未知!!!");

            default:
                logger.error("不支持的交易状态，交易返回异常!!!");
                return ServerResponse.createByErrorMessage("不支持的交易状态，交易返回异常!!!");
        }
    }

    @Override
    public ServerResponse aliCallback(Map<String, String> params) {
        Integer orderNo = Integer.parseInt(params.get("out_trade_no"));
        String tradeStatus = params.get("trade_status");
        Order order = orderRepository.findOneByOrderId(orderNo);
        if (order == null) {
            return ServerResponse.createByErrorMessage("非小机灵订单，回调忽略");
        }
        if (!order.getState().equals(Const.OrderState.TO_BE_PAID)) {
            return ServerResponse.createBySuccess("支付宝重复调用");
        }
        if (Const.AlipayCallback.TRADE_STATUS_TRADE_SUCCESS.equals(tradeStatus)) {
            order.setState(Const.OrderState.TO_BE_CONFIRMED);
            orderRepository.save(order);
        }

        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse queryOrderPayStatus(Integer agentId, Integer orderId) {
        Order order = orderRepository.findByAgentIdAndOrderId(agentId, orderId);
        if (order == null) {
            return ServerResponse.createByErrorMessage("用户没有该订单");
        }
        if (order.getState().equals(Const.OrderState.TO_BE_CONFIRMED)) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }

    @Override
    public ServerResponse   producerGetOrderList(Integer producerId) {
        List<Order> orderList = orderRepository.findByProducerId(producerId);
        if (orderList.size() == 0) {
            return ServerResponse.createByErrorMessage("暂无订单");
        }
        return ServerResponse.createBySuccess(this.producerGetOrderDTOList(orderList));
    }


    @Override
    public ServerResponse agentModifyOrderState(Integer agent, Integer orderId, String type) {
        Order order = orderRepository.findOneByOrderId(orderId);
        // 确认收货
        if (Const.OrderModifyType.COMPLETED.equals(type)) {
            if (Const.OrderState.TO_BE_RECEIVED.equals(order.getState())) {
                order.setState(Const.OrderState.COMPLETED);
                orderRepository.save(order);
            } else {
                return ServerResponse.createByErrorMessage("该订单不处于待收货状态，确认收货失败");
            }
        // 取消订单
        } else if (Const.OrderModifyType.CANCEL.equals(type)) {
            if (Const.OrderState.TO_BE_CONFIRMED.equals(order.getState())) {
                order.setState(Const.OrderState.REFUND);
                RefundStatus refundStatus = new RefundStatus(orderId, Const.RefundStatus.REFUNDING);
                refundStatusRepository.save(refundStatus);
            } else {
                return ServerResponse.createByErrorMessage("该订单不处于待确认状态，取消订单失败");
            }
        // 退款
        } else if (Const.OrderModifyType.REFUND.equals(type)) {
            if (Const.OrderState.TO_BE_RECEIVED.equals(order.getState())) {
                order.setState(Const.OrderState.REFUND);
                RefundStatus refundStatus = new RefundStatus(orderId, Const.RefundStatus.REFUNDING);
                refundStatusRepository.save(refundStatus);
            } else {
                return ServerResponse.createByErrorMessage("该订单不处于待收货状态，退款失败");
            }
        // 退货
        } else if (Const.OrderModifyType.RETURN.equals(type)) {
            if (Const.OrderState.COMPLETED.equals(order.getState())) {
                order.setState(Const.OrderState.REFUND);
                RefundStatus refundStatus = new RefundStatus(orderId, Const.RefundStatus.REFUNDING);
                refundStatusRepository.save(refundStatus);
            } else {
                return ServerResponse.createByErrorMessage("该订单不处于已完成状态，退货失败");
            }
        } else {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        return ServerResponse.createBySuccess("修改订单状态成功");
    }

    @Override
    public ServerResponse getEvaluationFromAgentToProducer(Integer orderId) {
        Evaluation evaluation = evaluationRepository.findByOrderIdAndDirection(orderId, 2);
        if (evaluation == null) {
            return ServerResponse.createByErrorMessage("该订单不存在或暂无评价");
        } else {
            EvaluationDTO evaluationDTO = new EvaluationDTO(evaluation.getLevel(), evaluation.getContent());
            return ServerResponse.createBySuccess(evaluationDTO);
        }
    }

    @Override
    public ServerResponse agentEvaluateToProducer(Integer agentId, Integer orderId, Integer level, String content) {
        Evaluation evaluation = evaluationRepository.findByOrderIdAndDirection(orderId, 2);
        if (evaluation != null) {
            return ServerResponse.createByErrorMessage("已进行过评价");
        } else {
            Order order = orderRepository.findOneByOrderId(orderId);
            if (order == null) {
                return ServerResponse.createByErrorMessage("该订单不存在");
            } else if (!order.getAgentId().equals(agentId)) {
                return ServerResponse.createByErrorMessage("无法评价他人的订单");
            } else {
                evaluation = new Evaluation(orderId, order.getProducerId(), order.getAgentId(), 2, level, content);
                if (evaluationRepository.save(evaluation) != null) {
                    return ServerResponse.createBySuccess("评价成功");
                } else {
                    return ServerResponse.createByErrorMessage("评价失败");
                }
            }
        }
    }

    @Override
    public ServerResponse getGoodsEvaluation(Integer orderId) {
        List<GoodsEvaluation> goodsEvaluationList = goodsEvaluationRepository.findByOrderId(orderId);
        if (goodsEvaluationList.size() == 0) {
            return ServerResponse.createByErrorMessage("该订单不存在或暂无对商品的评价");
        }
        return ServerResponse.createBySuccess(this.getGoodsEvaluationDTOList(goodsEvaluationList));
    }

    @Override
    public ServerResponse agentEvaluateToGoods(Integer agentId, Integer orderId, Integer goodsId, Integer level, String content) {
        GoodsEvaluation goodsEvaluation = goodsEvaluationRepository.findByOrderIdAndGoodsId(orderId, goodsId);
        if (goodsEvaluation != null) {
            return ServerResponse.createByErrorMessage("已进行过评价");
        } else {
            Order order = orderRepository.findOneByOrderId(orderId);
            if (order == null) {
                return ServerResponse.createByErrorMessage("该订单不存在");
            } else if (!order.getAgentId().equals(agentId)) {
                return ServerResponse.createByErrorMessage("无法评价他人的订单");
            } else {
                goodsEvaluation = new GoodsEvaluation(orderId, goodsId, agentId, level, content);
                if (goodsEvaluationRepository.save(goodsEvaluation) != null) {
                    return ServerResponse.createBySuccess("评价成功");
                } else {
                    return ServerResponse.createByErrorMessage("评价失败");
                }
            }
        }
    }

    /**
     * 简单打印应答
     */
    private void dumpResponse(AlipayResponse response) {
        if (response != null) {
            logger.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
            if (StringUtils.isNotEmpty(response.getSubCode())) {
                logger.info(String.format("subCode:%s, subMsg:%s", response.getSubCode(),
                        response.getSubMsg()));
            }
            logger.info("body:" + response.getBody());
        }
    }

    private Double getItemPrice(OrderItem orderItem) {
        Double itemPrice = goodsRepository.findByGoodsId(orderItem.getGoodsId()).getPrice();
        String[] goodsSpecIds = orderItem.getGoodsSpecIds().split(";");
        for (String goodsSpecId :
                goodsSpecIds) {
            GoodsSpecItem goodsSpecItem = goodsSpecItemRepository.findByGoodsSpecId(Integer.parseInt(goodsSpecId));
            itemPrice += goodsSpecItem.getPriceDifference();
        }
        return itemPrice;
    }

    /**
     * 获得订单总金额
     * @param orderId 订单id
     * @return 订单总金额
     */
    private Double getTotalAmount(Integer orderId) {
        List<OrderItem> orderItemList = orderItemRepository.findByOrderId(orderId);
        double totalAmount = 0.0;
        for (OrderItem orderItem :
                orderItemList) {
            totalAmount += this.getItemPrice(orderItem) * orderItem.getAmount();
        }
        DecimalFormat df = new DecimalFormat("0.00");
        return Double.parseDouble(df.format(totalAmount));
    }

    private List<GoodsEvaluationDTO> getGoodsEvaluationDTOList(List<GoodsEvaluation> goodsEvaluationList) {
        List<GoodsEvaluationDTO> goodsEvaluationDTOList = new ArrayList<>();
        for (GoodsEvaluation goodsEvaluation:
             goodsEvaluationList) {
            GoodsEvaluationDTO goodsEvaluationDTO = new GoodsEvaluationDTO(goodsEvaluation.getGoodsId(),
                    goodsEvaluation.getLevel(), goodsEvaluation.getContent(), goodsEvaluation.getCreateTime());
            goodsEvaluationDTOList.add(goodsEvaluationDTO);
        }
        return goodsEvaluationDTOList;
    }

    private void saveOrderItemList(Integer orderId, OrderItem[] orderItemList) {
        orderItemRepository.deleteByOrderId(orderId);
        for (OrderItem orderItem:
             orderItemList) {
            orderItem.setOrderId(orderId);
            orderItemRepository.save(orderItem);
        }
    }

    private List<OrderDTO> getOrderDTOList(List<Order> orderList) {
        List<OrderDTO> orderDTOList = new ArrayList<>();
        for (Order order :
                orderList) {
            OrderDTO orderDTO = new OrderDTO();

            List<OrderItem> orderItemList = orderItemRepository.findByOrderId(order.getOrderId());
            Buyer buyer = buyerRepository.findBuyerById(order.getBuyerId());
            Logistic logistic = logisticRepository.findByOrderId(order.getOrderId());
            Producer producer = producerRepository.findIdAndNameById(order.getProducerId());

            orderDTO.setOrderId(order.getOrderId());
            orderDTO.setState(order.getState());

            buyer.setAddress(null);
            orderDTO.setBuyer(buyer);

            orderDTO.setOrderItemList(this.getOrderItemDTOList(agentRepository.findOneById(order.getAgentId()).getRegion(), orderItemList));

            if (logistic != null) {
                logistic.setOrderId(null);
                logistic.setDeliveryDate(null);
                logistic.setPrice(null);
            }
            orderDTO.setLogistic(logistic);

            orderDTO.setProducer(producer);

            orderDTOList.add(orderDTO);
        }
        return orderDTOList;
    }

    private List<ProducerOrderDTO> producerGetOrderDTOList(List<Order> orderList) {
        List<ProducerOrderDTO> producerOrderDTOList = new ArrayList<>();
        for (Order order :
                orderList) {
            ProducerOrderDTO producerOrderDTO = new ProducerOrderDTO();

            List<OrderItem> orderItemList = orderItemRepository.findByOrderId(order.getOrderId());
            Buyer buyer = buyerRepository.findBuyerById(order.getBuyerId());
            Logistic logistic = logisticRepository.findByOrderId(order.getOrderId());
            Agent agent = agentRepository.findIdAndName(order.getAgentId());

            producerOrderDTO.setOrderId(order.getOrderId());
            producerOrderDTO.setState(order.getState());

            buyer.setAddress(null);
            producerOrderDTO.setBuyer(buyer);


            producerOrderDTO.setOrderItemList(this.getOrderItemDTOList(producerRepository.findOneById(order.getProducerId()).getRegion(), orderItemList));

            if (logistic != null) {
                logistic.setOrderId(null);
                logistic.setDeliveryDate(null);
                logistic.setPrice(null);
            }
            producerOrderDTO.setLogistic(logistic);

            producerOrderDTO.setAgent(agent);

            producerOrderDTOList.add(producerOrderDTO);
        }
        return producerOrderDTOList;
    }

    private List<OrderItemDTO> getOrderItemDTOList(String country, List<OrderItem> orderItemList) {
        List<OrderItemDTO> orderItemDTOList = new ArrayList<>();
        for (OrderItem orderItem :
                orderItemList) {
            OrderItemDTO orderItemDTO = new OrderItemDTO();
            Goods goods = goodsRepository.findByGoodsId(orderItem.getGoodsId());
            // 获得基本价格
            orderItemDTO.setPrice(goods.getPrice());
            String[] goodsSpecIds = orderItem.getGoodsSpecIds().split(";");
            List<SpecificationDTO> specificationDTOList = new ArrayList<>();
            for (String goodsSpecId :
                goodsSpecIds) {
                GoodsSpecItem goodsSpecItem = goodsSpecItemRepository.findByGoodsSpecId(Integer.parseInt(goodsSpecId));
                Specification specification = specificationRepository.findBySpecId(goodsSpecItem.getSpecId());
                SpecificationDTO specificationDTO = new SpecificationDTO(Integer.parseInt(goodsSpecId),
                        specification.getName(), specification.getValue(), null);
                specificationDTOList.add(specificationDTO);

                orderItemDTO.addPrice(goodsSpecItem.getPriceDifference());
            }

            // 根据汇率转换为对应货币价格
            orderItemDTO.setPrice(exchangeRateService.getExchangePrice(country, orderItemDTO.getPrice()));

            orderItemDTO.setGoodsId(goods.getGoodsId());
            orderItemDTO.setSpecificationList(specificationDTOList);
            orderItemDTO.setName(goods.getName());
            orderItemDTO.setAmount(orderItem.getAmount());

            orderItemDTOList.add(orderItemDTO);
        }
        return orderItemDTOList;
    }

    private OrderDetailDTO getOrderDetailDTO(String country, Order order) {
        OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
        Logistic logistic = logisticRepository.findByOrderId(order.getOrderId());
        Buyer buyer = buyerRepository.findBuyerById(order.getBuyerId());

        if (logistic != null) {
            logistic.setOrderId(null);
            logistic.setPrice(exchangeRateService.getExchangePrice(country, logistic.getPrice()));
        }
        orderDetailDTO.setLogistic(logistic);

        buyer.setId(null);
        orderDetailDTO.setBuyer(buyer);

        return orderDetailDTO;
    }

    @Override
    public ServerResponse orderStateReceive(Integer orderId){
        Order order = orderRepository.findOneByOrderId(orderId);
        order.setState(Const.OrderState.TO_BE_RECEIVED);
        orderRepository.save(order);
        return  ServerResponse.createBySuccess("接受");
    }

    @Override
    public ServerResponse orderStateReject(Integer orderId){
        Order order = orderRepository.findOneByOrderId(orderId);
        order.setState(Const.OrderState.REJECTED);
        orderRepository.save(order);
        return  ServerResponse.createBySuccessMessage("驳回");
    }
    @Override
    public ServerResponse refundOrderStateReceieve(Integer orderId){
        RefundStatus refundStatus = refundStatusRepository.findOneByOrderId(orderId);
        refundStatus.setRefundStatus(Const.RefundStatus.REFUND_SUCCESS);
        refundStatusRepository.save(refundStatus);
        return  ServerResponse.createBySuccessMessage("退款成功");
    }

    @Override
    public ServerResponse refundOrderStateReject(Integer orderId){
        RefundStatus refundStatus = refundStatusRepository.findOneByOrderId(orderId);
        refundStatus.setRefundStatus(Const.RefundStatus.REFUND_FAILED);
        refundStatusRepository.save(refundStatus);
        return  ServerResponse.createBySuccessMessage("退款失败");
    }

    @Override
    public ServerResponse producerGetEvaluation(Integer producerId,Integer orderId){
        EvaluationDTO evaluationDTO=new EvaluationDTO();
        Evaluation evaluation=evaluationRepository.findByOrderIdAndProducerId(producerId,orderId);
        if (evaluation.getContent()==null){
            return ServerResponse.createByErrorMessage("请评价");
        }
        evaluationDTO.setContent(evaluation.getContent());
        evaluationDTO.setCreateTime(evaluation.getCreateTime());;
        evaluationDTO.setLevel(evaluation.getLevel());
        return  ServerResponse.createBySuccess(evaluationDTO);
    }

    @Override
    public ServerResponse producerSetEvaluation(Integer producerId,Integer orderId,Integer agentId,Integer level,String content){
        Evaluation evaluation=evaluationRepository.findOneByOrderId(orderId);
        evaluation.setOrderId(orderId);
        evaluation.setAgentId(agentId);
        evaluation.setProducerId(producerId);
        evaluation.setDirection(1);
        evaluation.setLevel(evaluation.getLevel());
        evaluation.setContent(evaluation.getContent());


        return  ServerResponse.createBySuccessMessage("评价成功");
    }
}
