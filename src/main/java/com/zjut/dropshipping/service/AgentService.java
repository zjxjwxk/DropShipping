package com.zjut.dropshipping.service;

import com.zjut.dropshipping.common.ServerResponse;
import com.zjut.dropshipping.dataobject.Agent;

/**
 * @author zjxjwxk
 */
public interface AgentService {

    ServerResponse<String> register(Agent agent);

    ServerResponse<Agent> login(String phone, String password);

}
