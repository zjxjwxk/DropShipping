package com.zjut.dropshipping.dto;

import lombok.Data;

/**
 * @author zjxjwxk
 */
@Data
public class OrderDetailDTO {

    private Integer orderId;
    private Integer producerId;
    private String producerName;
    private String region;

    private String buyerName;
    private String buyerPhone;
    private String buyerAddress;
    private String buyerRemark;
}
