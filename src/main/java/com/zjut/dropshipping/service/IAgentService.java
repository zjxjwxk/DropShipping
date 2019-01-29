package com.zjut.dropshipping.service;

import com.zjut.dropshipping.dataobject.Agent;

/**
 * @author zjxjwxk
 */
public interface IAgentService {

    public Agent register(String name, String phone, String identityNumber
            , String region, String state, String externalShop);
}
