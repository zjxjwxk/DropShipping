package com.zjut.dropshipping.service;

import com.zjut.dropshipping.common.ServerResponse;
import com.zjut.dropshipping.dataobject.Producer;

/**
 * @author ljx
 */
public interface ProducerService {

    ServerResponse<String> register(Producer producer);

    ServerResponse<Producer> login(String contactPhone, String password);

    ServerResponse getRecommendProducer(Integer producerId, Integer pageNumber, Integer numberOfElements);


}
