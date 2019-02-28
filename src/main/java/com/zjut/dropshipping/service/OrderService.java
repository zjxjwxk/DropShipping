package com.zjut.dropshipping.service;

import com.zjut.dropshipping.common.ServerResponse;
import com.zjut.dropshipping.dataobject.OrderItem;

import java.util.List;

/**
 * @author zjxjwxk
 */
public interface OrderService {

    ServerResponse agentSaveOrder(Integer orderId, Integer agentId,
                                  List<OrderItem> orderItemList,
                                  String remark, String buyerName,
                                  String buyerPhone, String address);

    ServerResponse agentGetOrderList(Integer agentId);

    ServerResponse producerGetOrderList(Integer producerId);

    ServerResponse getOrderDetail(Integer orderId);
}
