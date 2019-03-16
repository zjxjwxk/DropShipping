package com.zjut.dropshipping.service;

import com.zjut.dropshipping.common.ServerResponse;

/**
 * @author zjxjwxk
 */
public interface GoodsService {

    ServerResponse getList(String country, String keyword, Integer categoryId, Integer producerId, Integer agreementAgentId,
                           Integer pageNum, Integer pageSize, String orderBy);

    ServerResponse getDetail(String country, Integer goodsId);

    ServerResponse getSalesVolume(Integer goodsId);

    ServerResponse getEvaluation(Integer goodsId);

    ServerResponse getProducer(Integer goodsId);

    ServerResponse getSpecification(Integer goodsId);

    ServerResponse addGoodsModel(Integer goodsId, String name, String value, Double price);
}
