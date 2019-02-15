package com.zjut.dropshipping.controller;

import com.zjut.dropshipping.common.Const;
import com.zjut.dropshipping.common.ResponseCode;
import com.zjut.dropshipping.common.ServerResponse;
import com.zjut.dropshipping.dataobject.Agent;
import com.zjut.dropshipping.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

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
                                        Integer goodsId, Integer amount, String remark,
                                        String buyerName, String buyerPhone, String address) {
        Agent agent = (Agent) session.getAttribute(Const.CURRENT_AGENT);
        if (agent == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        return orderService.agentSaveOrder(orderId, agent.getId(), goodsId, amount, remark, buyerName, buyerPhone, address);
    }
}
