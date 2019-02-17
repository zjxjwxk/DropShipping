package com.zjut.dropshipping.service.impl;

import com.zjut.dropshipping.common.Const;
import com.zjut.dropshipping.common.ServerResponse;
import com.zjut.dropshipping.dataobject.*;
import com.zjut.dropshipping.dto.OrderDTO;
import com.zjut.dropshipping.dto.OrderDetailDTO;
import com.zjut.dropshipping.dto.OrderItemDTO;
import com.zjut.dropshipping.repository.*;
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
    private final LogisticRepository logisticRepository;
    private final OrderItemRepository orderItemRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            BuyerRepository buyerRepository,
                            GoodsRepository goodsRepository,
                            ProducerRepository producerRepository,
                            LogisticRepository logisticRepository,
                            OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.buyerRepository = buyerRepository;
        this.goodsRepository = goodsRepository;
        this.producerRepository = producerRepository;
        this.logisticRepository = logisticRepository;
        this.orderItemRepository = orderItemRepository;
    }


    @Override
    public ServerResponse agentSaveOrder(Integer orderId, Integer agentId,
                                         List<OrderItem> orderItemList,
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

        this.saveOrderItemList(order.getOrderId(), orderItemList);

        order.setBuyerId(buyer.getId());
        order.setProducerId(goodsRepository.findByGoodsId(orderItemList.get(0).getGoodsId()).getProducerId());
        order.setState(Const.OrderState.TO_BE_CONFIRMED);
        order.setRemark(remark);

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

    private void saveOrderItemList(Integer orderId, List<OrderItem> orderItemList) {
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

            orderDTO.setOrderItemList(this.getOrderItemDTOList(orderItemList));

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

    private List<OrderItemDTO> getOrderItemDTOList(List<OrderItem> orderItemList) {
        List<OrderItemDTO> orderItemDTOList = new ArrayList<>();
        for (OrderItem orderItem :
                orderItemList) {
            OrderItemDTO orderItemDTO = new OrderItemDTO();
            Goods goods = goodsRepository.findByGoodsId(orderItem.getGoodsId());
            orderItemDTO.setGoodsId(goods.getGoodsId());
            orderItemDTO.setName(goods.getName());
            orderItemDTO.setAmount(orderItem.getAmount());
            orderItemDTO.setPrice(goods.getPrice());

            orderItemDTOList.add(orderItemDTO);
        }
        return orderItemDTOList;
    }

    private OrderDetailDTO getOrderDetailDTO(Order order) {
        OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
        Logistic logistic = logisticRepository.findByOrderId(order.getOrderId());
        Buyer buyer = buyerRepository.findBuyerById(order.getBuyerId());

        if (logistic != null) {
            logistic.setOrderId(null);
        }
        orderDetailDTO.setLogistic(logistic);

        buyer.setId(null);
        orderDetailDTO.setBuyer(buyer);

        return orderDetailDTO;
    }
}
