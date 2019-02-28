package com.zjut.dropshipping.controller;

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
import java.util.List;

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
                                        @RequestBody OrderItem[] orderItemList,
                                        String remark, String buyerName,
                                        String buyerPhone, String address) {
        Agent agent = (Agent) session.getAttribute(Const.CURRENT_AGENT);
        if (agent == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        return orderService.agentSaveOrder(orderId, agent.getId(), orderItemList, remark, buyerName, buyerPhone, address);
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



}
