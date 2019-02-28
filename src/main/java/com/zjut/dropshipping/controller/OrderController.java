package com.zjut.dropshipping.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zjut.dropshipping.common.Const;
import com.zjut.dropshipping.common.ResponseCode;
import com.zjut.dropshipping.common.ServerResponse;
import com.zjut.dropshipping.dataobject.Agent;
import com.zjut.dropshipping.dataobject.OrderItem;
import com.zjut.dropshipping.dataobject.Producer;
import com.zjut.dropshipping.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author zjxjwxk
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/agent_save_order")
    @ResponseBody
    public ServerResponse agentAddOrder(HttpSession session,
                                        @RequestParam(required = false) Integer orderId,
                                        String orderItemList,
                                        String remark, String buyerName,
                                        String buyerPhone, String address) {
        Agent agent = (Agent) session.getAttribute(Const.CURRENT_AGENT);
        if (agent == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        OrderItem[] orderItems;
        try {
            orderItems = objectMapper.readValue(orderItemList, OrderItem[].class);
            return orderService.agentSaveOrder(orderId, agent.getId(), orderItems, remark, buyerName, buyerPhone, address);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
    }

    @GetMapping("/agent_get_order_list")
    @ResponseBody
    public ServerResponse agentGetOrderList(HttpSession session) {
        Agent agent = (Agent) session.getAttribute(Const.CURRENT_AGENT);
        if (agent == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        return orderService.agentGetOrderList(agent.getId());
    }

    @GetMapping("/get_order_detail")
    @ResponseBody
    public ServerResponse getOrderDetail(Integer orderId) {

        return orderService.getOrderDetail(orderId);
    }

    @GetMapping("/producer_get_order_list")
    @ResponseBody
    public ServerResponse producerGetOrderList(HttpSession session) {
        Producer producer = (Producer) session.getAttribute(Const.CURRENT_PRODUCER);
        if (producer == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        return orderService.producerGetOrderList(producer.getId());
    }

    @PostMapping("/order_state_receive")
    @ResponseBody
    public ServerResponse orderStateReceive(HttpSession session,Integer orderId) {
        Producer producer = (Producer) session.getAttribute(Const.CURRENT_PRODUCER);
        if (producer == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        return orderService.orderStateReceive(orderId);
    }

    @PostMapping("/order_state_reject")
    @ResponseBody
    public ServerResponse orderStateReject(HttpSession session,Integer orderId) {
        Producer producer = (Producer) session.getAttribute(Const.CURRENT_PRODUCER);
        if (producer == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        return orderService.orderStateReject(orderId);
    }

}
