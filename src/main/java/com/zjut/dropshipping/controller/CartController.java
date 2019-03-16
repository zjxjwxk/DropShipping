package com.zjut.dropshipping.controller;

import com.zjut.dropshipping.common.Const;
import com.zjut.dropshipping.common.ResponseCode;
import com.zjut.dropshipping.common.ServerResponse;
import com.zjut.dropshipping.dataobject.Agent;
import com.zjut.dropshipping.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * @author zjxjwxk
 */
@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/get_list")
    @ResponseBody
    public ServerResponse getList(HttpSession session) {
        Agent agent = (Agent) session.getAttribute(Const.CURRENT_AGENT);
        if (agent == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        return cartService.getList(agent.getId(), agent.getRegion());
    }

    @PostMapping("/add")
    @ResponseBody
    public ServerResponse add(HttpSession session, String goodsSpecIds, Integer amount) {
        Agent agent = (Agent) session.getAttribute(Const.CURRENT_AGENT);
        if (agent == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        return cartService.add(agent.getId(), goodsSpecIds, amount);
    }

    @PostMapping("/delete")
    @ResponseBody
    public ServerResponse delete(HttpSession session, String goodsSpecIds) {
        Agent agent = (Agent) session.getAttribute(Const.CURRENT_AGENT);
        if (agent == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        return cartService.delete(agent.getId(), goodsSpecIds);
    }

    @PostMapping("/update")
    @ResponseBody
    public ServerResponse update(HttpSession session, String goodsSpecIds, Integer amount) {
        Agent agent = (Agent) session.getAttribute(Const.CURRENT_AGENT);
        if (agent == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        return cartService.update(agent.getId(), goodsSpecIds, amount);
    }
}
