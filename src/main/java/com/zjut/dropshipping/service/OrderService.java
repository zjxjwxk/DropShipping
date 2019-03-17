package com.zjut.dropshipping.service;

import com.zjut.dropshipping.common.ServerResponse;
import com.zjut.dropshipping.dataobject.OrderItem;

import java.util.List;
import java.util.Map;

/**
 * @author zjxjwxk
 */
public interface OrderService {

    ServerResponse agentSaveOrder(Integer orderId, Integer agentId,
                                  OrderItem[] orderItemList,
                                  String remark, String buyerName,
                                  String buyerPhone, String address);

    ServerResponse agentGetOrderList(Integer agentId);

    ServerResponse producerGetOrderList(Integer producerId);

    ServerResponse getOrderDetail(String country, Integer orderId);

    ServerResponse pay(Integer agentId, Integer orderId, String path);

    ServerResponse aliCallback(Map<String, String> params);

    ServerResponse queryOrderPayStatus(Integer agentId, Integer orderId);

    ServerResponse orderStateReceive(Integer orderId);

    ServerResponse orderStateReject(Integer orderId);

    ServerResponse refundOrderStateReceieve(Integer orderId);

    ServerResponse refundOrderStateReject(Integer orderId);

    ServerResponse agentModifyOrderState(Integer agentId, Integer orderId, String type);

    ServerResponse getEvaluationFromAgentToProducer(Integer orderId);

    ServerResponse agentEvaluateToProducer(Integer agentId, Integer orderId, Integer level, String content);

    ServerResponse getGoodsEvaluation(Integer orderId);

    ServerResponse agentEvaluateToGoods(Integer agentId, Integer orderId, Integer goodsId, Integer level, String content);

    ServerResponse producerGetEvaluation(Integer producerId,Integer orderId);

    ServerResponse producerSetEvaluation(Integer producerId,Integer orderId,Integer agentId,Integer level,String content);

}
