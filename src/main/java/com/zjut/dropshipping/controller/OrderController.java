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
    public ServerResponse getOrderDetail(HttpSession session, Integer orderId) {
        if (session.getAttribute(Const.CURRENT_AGENT) != null) {
            Agent agent = (Agent) session.getAttribute(Const.CURRENT_AGENT);
            return orderService.getOrderDetail(agent.getRegion(), orderId);
        } else if (session.getAttribute(Const.CURRENT_PRODUCER) != null) {
            Producer producer = (Producer) session.getAttribute(Const.CURRENT_PRODUCER);
            return orderService.getOrderDetail(producer.getRegion(), orderId);
        } else {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
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

    @PostMapping("/refund_order_state_reject")
    @ResponseBody
    public ServerResponse refundOrderStateReject(HttpSession session,Integer orderId) {
        Producer producer = (Producer) session.getAttribute(Const.CURRENT_PRODUCER);
        if (producer == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        return orderService.refundOrderStateReject(orderId);
    }

    @PostMapping("/refund_order_state_receieve")
    @ResponseBody
    public ServerResponse refundOrderStateReceieve(HttpSession session,Integer orderId) {
        Producer producer = (Producer) session.getAttribute(Const.CURRENT_PRODUCER);
        if (producer == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        return orderService.refundOrderStateReceieve(orderId);
    }

    @PostMapping("/agent_modify_order_state")
    @ResponseBody
    public ServerResponse agentModifyOrderState(HttpSession session, Integer orderId, String type) {
        Agent agent = (Agent) session.getAttribute(Const.CURRENT_AGENT);
        if (agent == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        return orderService.agentModifyOrderState(agent.getId(), orderId, type);
    }

    @GetMapping("/producer_get_refund_order_list")
    @ResponseBody
    public ServerResponse producerGetRefundOrderList(HttpSession session) {
        Producer producer = (Producer) session.getAttribute(Const.CURRENT_PRODUCER);
        if (producer == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        return orderService.producerGetOrderList(producer.getId());
    }

    @GetMapping("/get_evaluation_from_agent_to_producer")
    @ResponseBody
    public ServerResponse getEvaluationFromAgentToProducer(Integer orderId) {
        return orderService.getEvaluationFromAgentToProducer(orderId);
    }

    @PostMapping("/agent_evaluate_to_producer")
    @ResponseBody
    public ServerResponse agentEvaluateToProducer(HttpSession session, Integer orderId, Integer level, String content) {
        Agent agent = (Agent) session.getAttribute(Const.CURRENT_AGENT);
        if (agent == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        return orderService.agentEvaluateToProducer(agent.getId(), orderId, level, content);
    }

    @GetMapping("/get_goods_evaluation")
    @ResponseBody
    public ServerResponse getGoodsEvaluation(Integer orderId) {
        return orderService.getGoodsEvaluation(orderId);
    }

    @PostMapping("/agent_evaluate_to_goods")
    @ResponseBody
    public ServerResponse agentEvaluateToGoods(HttpSession session, Integer orderId, Integer goodsId, Integer level, String content) {
        Agent agent = (Agent) session.getAttribute(Const.CURRENT_AGENT);
        if (agent == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        return orderService.agentEvaluateToGoods(agent.getId(), orderId, goodsId, level, content);
    }

    @GetMapping("/producer_get_evaluation")
    @ResponseBody
    public ServerResponse producerGetEvaluation(HttpSession session,Integer orderId) {
        Producer producer = (Producer) session.getAttribute(Const.CURRENT_PRODUCER);
        if (producer == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        return orderService.producerGetEvaluation(producer.getId(),orderId);
    }

    @PostMapping("/producer_set_evaluation")
    @ResponseBody
    public ServerResponse producerSetEvaluation(HttpSession session,Integer orderId,Integer agentId,Integer level,String content) {
        Producer producer = (Producer) session.getAttribute(Const.CURRENT_PRODUCER);
        if (producer == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        return orderService.producerSetEvaluation(producer.getId(),orderId,agentId,level,content);
    }
}
