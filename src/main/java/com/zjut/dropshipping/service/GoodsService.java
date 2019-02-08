package com.zjut.dropshipping.service;

import com.zjut.dropshipping.common.ServerResponse;

public interface GoodsService {

    ServerResponse getList(String keyword, Integer categoryId,
                                             Integer pageNum, Integer pageSize,
                                             String orderBy);

    ServerResponse getDetail(Integer goodsId);

    ServerResponse getSalesVolume(Integer goodsId);

    ServerResponse getEvaluation(Integer goodsId);

    ServerResponse getProducer(Integer goodsId);
}
