package com.zjut.dropshipping.service;

import com.zjut.dropshipping.common.ServerResponse;
import com.zjut.dropshipping.dataobject.OrderItem;

import java.util.List;

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

    ServerResponse getOrderDetail(Integer orderId);

    ServerResponse orderStateReceive(Integer orderId);

    ServerResponse orderStateReject(Integer orderId);

    ServerResponse refundOrderStateReceieve(Integer orderId);

    ServerResponse refundOrderStateReject(Integer orderId);

    ServerResponse agentModifyOrderState(Integer agentId, Integer orderId, String type);

    ServerResponse producerGetEvaluation(Integer producerId,Integer orderId);

    ServerResponse producerSetEvaluation(Integer producerId,Integer orderId,Integer agentId,Integer level,String content);

}
