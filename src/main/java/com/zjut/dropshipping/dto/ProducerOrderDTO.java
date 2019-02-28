package com.zjut.dropshipping.dto;

import com.zjut.dropshipping.dataobject.Buyer;
import com.zjut.dropshipping.dataobject.Logistic;
import com.zjut.dropshipping.dataobject.Agent;
import lombok.Data;

import java.util.List;
/**
 * @author ljx
 */
@Data
public class ProducerOrderDTO {
    private Integer orderId;
    private String state;

    private Buyer buyer;
    private List<OrderItemDTO> orderItemList;
    private Logistic logistic;
    private Agent agent;
}
