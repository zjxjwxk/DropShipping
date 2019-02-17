package com.zjut.dropshipping.service;

import com.zjut.dropshipping.common.ServerResponse;

/**
 * @author zjxjwxk
 */
public interface OrderService {

    ServerResponse agentSaveOrder(Integer orderId, Integer agentId, Integer goodsId,
                                  Integer amount, String remark, String buyerName,
                                  String buyerPhone, String address);

    ServerResponse agentGetOrderList(Integer agentId);

    ServerResponse getOrderDetail(Integer orderId);
}
