package com.zjut.dropshipping.dto;

import com.zjut.dropshipping.dataobject.Buyer;
import com.zjut.dropshipping.dataobject.Logistic;
import com.zjut.dropshipping.dataobject.Producer;
import lombok.Data;

import java.util.List;

/**
 * @author zjxjwxk
 */
@Data
public class OrderDTO {

    private Integer orderId;
    private String state;

    private Buyer buyer;
    private List<OrderItemDTO> orderItemList;
    private Logistic logistic;
    private Producer producer;
}
