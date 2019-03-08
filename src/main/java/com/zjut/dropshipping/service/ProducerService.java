package com.zjut.dropshipping.service;

import com.zjut.dropshipping.common.ServerResponse;
import com.zjut.dropshipping.dataobject.Producer;

/**
 * @author ljx
 */
public interface ProducerService {

    ServerResponse<String> register(Producer producer);

    ServerResponse<Producer> login(String contactPhone, String password);

    ServerResponse<String> producerRequestAgreement(Integer agentId, Integer producerId);

    ServerResponse getAgentAgreementRequest(Integer producerId);

    ServerResponse responseAgentAgreementRequest(Integer producerId, Integer agentId, String response);

    ServerResponse getRecommendAgent(Integer pageNumber, Integer numberOfElements);

    ServerResponse getAcceptedAgent(Integer producerId,Integer pageNumber, Integer numberOfElements);

    ServerResponse getDetailAgent(Integer agentId);

    ServerResponse addGoods(Integer producerId,String goodsName,Integer categoryId, Double price, Integer stock,String content);

    ServerResponse getSaleAnalysis(Integer producerId,String time, String form);
}
