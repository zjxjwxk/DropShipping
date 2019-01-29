package com.zjut.dropshipping.controller;

import com.zjut.dropshipping.dataobject.Agent;
import com.zjut.dropshipping.service.IAgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zjxjwxk
 */
@RestController
@RequestMapping("/agent")
public class AgentController {

    private final IAgentService iAgentService;

    @Autowired
    public AgentController(IAgentService iAgentService) {
        this.iAgentService = iAgentService;
    }

    @PostMapping("/register")
    public Agent register(String name, String phone, String identityNumber
            , String region, String state, String externalShop) {
        return iAgentService.register(name, phone, identityNumber, region, state, externalShop);
    }
}
