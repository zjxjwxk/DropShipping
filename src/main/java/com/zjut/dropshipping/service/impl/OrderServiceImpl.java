package com.zjut.dropshipping.service.impl;

import com.zjut.dropshipping.common.Const;
import com.zjut.dropshipping.common.ServerResponse;
import com.zjut.dropshipping.dataobject.Buyer;
import com.zjut.dropshipping.dataobject.Order;
import com.zjut.dropshipping.repository.BuyerRepository;
import com.zjut.dropshipping.repository.OrderRepository;
import com.zjut.dropshipping.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zjxjwxk
 */
@Service("OrderService")
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final BuyerRepository buyerRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            BuyerRepository buyerRepository) {
        this.orderRepository = orderRepository;
        this.buyerRepository = buyerRepository;
    }


    @Override
    public ServerResponse agentSaveOrder(Integer orderId, Integer agentId, Integer goodsId,
                                         Integer amount, String remark, String buyerName,
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
        order.setGoodsId(goodsId);
        order.setBuyerId(buyer.getId());
        order.setAmount(amount);
        order.setState(Const.OrderState.NORMAL);
        order.setRemark(remark);
        orderRepository.save(order);

        return ServerResponse.createBySuccess(order);
    }
}
