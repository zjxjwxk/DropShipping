package com.zjut.dropshipping.service;

import com.zjut.dropshipping.common.ServerResponse;

/**
 * @author zjxjwxk
 */
public interface CartService {

    ServerResponse getList(Integer agentId, String country);

    ServerResponse add(Integer agentId, String goodsSpecIds, Integer amount);

    ServerResponse delete(Integer agentId, String goodsSpecIds);

    ServerResponse update(Integer agentId, String goodsSpecIds, Integer amount);
}
