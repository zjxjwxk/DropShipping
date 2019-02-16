package com.zjut.dropshipping.service.impl;

import com.zjut.dropshipping.common.Const;
import com.zjut.dropshipping.common.ServerResponse;
import com.zjut.dropshipping.dataobject.Buyer;
import com.zjut.dropshipping.dataobject.Goods;
import com.zjut.dropshipping.dataobject.Order;
import com.zjut.dropshipping.dataobject.Producer;
import com.zjut.dropshipping.dto.OrderDTO;
import com.zjut.dropshipping.dto.OrderDetailDTO;
import com.zjut.dropshipping.repository.BuyerRepository;
import com.zjut.dropshipping.repository.GoodsRepository;
import com.zjut.dropshipping.repository.OrderRepository;
import com.zjut.dropshipping.repository.ProducerRepository;
import com.zjut.dropshipping.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zjxjwxk
 */
@Service("OrderService")
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final BuyerRepository buyerRepository;
    private final GoodsRepository goodsRepository;
    private final ProducerRepository producerRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            BuyerRepository buyerRepository,
                            GoodsRepository goodsRepository,
                            ProducerRepository producerRepository) {
        this.orderRepository = orderRepository;
        this.buyerRepository = buyerRepository;
        this.goodsRepository = goodsRepository;
        this.producerRepository = producerRepository;
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

    @Override
    public ServerResponse agentGetOrderList(Integer agentId) {
        List<Order> orderList = orderRepository.findByAgentId(agentId);
        if (orderList.size() == 0) {
            return ServerResponse.createByErrorMessage("暂无订单");
        }
        return ServerResponse.createBySuccess(this.getOrderDTOList(orderList));
    }

    @Override
    public ServerResponse getOrderDetail(Integer orderId) {
        Order order = orderRepository.findOneByOrderId(orderId);
        if (order == null) {
            return ServerResponse.createByErrorMessage("该订单不存在");
        }
        return ServerResponse.createBySuccess(this.getOrderDetailDTO(order));
    }

    private List<OrderDTO> getOrderDTOList(List<Order> orderList) {
        List<OrderDTO> orderDTOList = new ArrayList<>();
        for (Order order :
                orderList) {
            OrderDTO orderDTO = new OrderDTO();
            Goods goods = goodsRepository.findByGoodsId(order.getGoodsId());
            Buyer buyer = buyerRepository.findBuyerById(order.getBuyerId());
            orderDTO.setOrderId(order.getOrderId());
            orderDTO.setGoodsId(order.getGoodsId());
            orderDTO.setGoodsName(goods.getName());
            orderDTO.setAmount(order.getAmount());
            orderDTO.setCreateTime(order.getCreateTime());
            orderDTO.setBuyerName(buyer.getName());
            orderDTO.setBuyerPhone(buyer.getPhone());
            orderDTO.setBuyerAddress(buyer.getAddress());

            orderDTOList.add(orderDTO);
        }
        return orderDTOList;
    }

    private OrderDetailDTO getOrderDetailDTO(Order order) {
        OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
        Goods goods = goodsRepository.findByGoodsId(order.getGoodsId());
        Producer producer = producerRepository.findOneById(goods.getProducerId());
        Buyer buyer = buyerRepository.findBuyerById(order.getBuyerId());
        orderDetailDTO.setOrderId(order.getOrderId());
        orderDetailDTO.setProducerId(goods.getProducerId());
        orderDetailDTO.setProducerName(producer.getName());
        orderDetailDTO.setRegion(producer.getRegion());
        orderDetailDTO.setBuyerName(buyer.getName());
        orderDetailDTO.setBuyerPhone(buyer.getPhone());
        orderDetailDTO.setBuyerAddress(buyer.getAddress());
        orderDetailDTO.setBuyerRemark(order.getRemark());

        return orderDetailDTO;
    }
}
