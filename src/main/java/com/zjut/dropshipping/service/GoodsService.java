package com.zjut.dropshipping.service;

import com.zjut.dropshipping.common.ServerResponse;

/**
 * @author zjxjwxk
 */
public interface GoodsService {

    ServerResponse getList(String keyword, Integer categoryId, Integer agreementAgentId,
                           Integer pageNum, Integer pageSize, String orderBy);

    ServerResponse getDetail(Integer goodsId);

    ServerResponse getSalesVolume(Integer goodsId);

    ServerResponse getEvaluation(Integer goodsId);

    ServerResponse getProducer(Integer goodsId);

    ServerResponse getSpecification(Integer goodsId);
}
