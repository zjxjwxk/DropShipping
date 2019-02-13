package com.zjut.dropshipping.controller;

import com.zjut.dropshipping.common.ServerResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zjxjwxk
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @PostMapping("/agent_add_order")
    @ResponseBody
    public ServerResponse agentAddOrder(Integer goodsId, Integer amount) {
        return null;
    }
}
