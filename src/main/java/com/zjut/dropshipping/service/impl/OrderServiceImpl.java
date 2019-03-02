package com.zjut.dropshipping.service.impl;

import com.zjut.dropshipping.common.Const;
import com.zjut.dropshipping.common.ResponseCode;
import com.zjut.dropshipping.common.ServerResponse;
import com.zjut.dropshipping.dataobject.*;
import com.zjut.dropshipping.dto.OrderDTO;
import com.zjut.dropshipping.dto.OrderDetailDTO;
import com.zjut.dropshipping.dto.OrderItemDTO;
import com.zjut.dropshipping.dto.ProducerOrderDTO;
import com.zjut.dropshipping.dto.SpecificationDTO;
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
    private final SpecificationRepository specificationRepository;
    private final GoodsSpecItemRepository goodsSpecItemRepository;
    private final AgentRepository agentRepository;
    private final RefundStatusRepository refundStatusRepository;

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
                            RefundStatusRepository refundStatusRepository) {
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
    public ServerResponse getOrderDetail(Integer orderId) {
        Order order = orderRepository.findOneByOrderId(orderId);
        if (order == null) {
            return ServerResponse.createByErrorMessage("该订单不存在");
        }
        return ServerResponse.createBySuccess(this.getOrderDetailDTO(order));
    }

    @Override
    public ServerResponse producerGetOrderList(Integer producerId) {
        List<Order> orderList = orderRepository.findByProducerId(producerId);
        if (orderList.size() == 0) {
            return ServerResponse.createByErrorMessage("暂无订单");
        }
        return ServerResponse.createBySuccess(this.producerGetOrderDTOList(orderList));
    }

    @Override
    public ServerResponse agentModifyOrderState(Integer agent, Integer orderId, String type) {
        Order order = orderRepository.findOneByOrderId(orderId);
        // 取消订单
        if (Const.OrderModifyType.CANCEL.equals(type)) {
            if (Const.OrderState.TO_BE_CONFIRMED.equals(order.getState())) {
                order.setState("退款");
                RefundStatus refundStatus = new RefundStatus(orderId, Const.RefundStatus.REFUNDING);
                refundStatusRepository.save(refundStatus);
            }
        // 退款
        } else if (Const.OrderModifyType.REFUND.equals(type)) {
            if (Const.OrderState.TO_BE_RECEIVED.equals(order.getState())) {
                order.setState("退款");
                RefundStatus refundStatus = new RefundStatus(orderId, Const.RefundStatus.REFUNDING);
                refundStatusRepository.save(refundStatus);
            }
        // 退货
        } else if (Const.OrderModifyType.RETURN.equals(type)) {
            if (Const.OrderState.COMPLETED.equals(order.getState())) {
                order.setState("退款");
                RefundStatus refundStatus = new RefundStatus(orderId, Const.RefundStatus.REFUNDING);
                refundStatusRepository.save(refundStatus);
            }
        } else {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        return ServerResponse.createBySuccess("修改订单状态成功");
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

            producerOrderDTO.setOrderItemList(this.getOrderItemDTOList(orderItemList));

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

    private List<OrderItemDTO> getOrderItemDTOList(List<OrderItem> orderItemList) {
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

            orderItemDTO.setGoodsId(goods.getGoodsId());
            orderItemDTO.setSpecificationList(specificationDTOList);
            orderItemDTO.setName(goods.getName());
            orderItemDTO.setAmount(orderItem.getAmount());

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
        return  ServerResponse.createBySuccess("驳回");
    }

}
