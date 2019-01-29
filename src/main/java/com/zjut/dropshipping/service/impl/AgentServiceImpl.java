package com.zjut.dropshipping.service.impl;

import com.zjut.dropshipping.dataobject.Agent;
import com.zjut.dropshipping.repository.AgentRepository;
import com.zjut.dropshipping.service.IAgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zjxjwxk
 */
@Service("iAgentService")
public class AgentServiceImpl implements IAgentService {

    private final AgentRepository agentRepository;

    @Autowired
    public AgentServiceImpl(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
    }

    @Override
    public Agent register(String name, String phone, String identityNumber
            , String region, String state, String externalShop) {
        Agent agent = new Agent(name, phone, identityNumber, region, state, externalShop);
        return agentRepository.save(agent);
    }
}
