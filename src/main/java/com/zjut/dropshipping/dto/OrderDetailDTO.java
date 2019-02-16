package com.zjut.dropshipping.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author zjxjwxk
 */
@Data
public class OrderDetailDTO {

    private Integer orderId;
    private String orderState;

    private Integer producerId;
    private String producerName;
    private String region;

    private String goodsContent;
    private Double goodsPrice;

    private String buyerName;
    private String buyerPhone;
    private String buyerAddress;
    private String buyerRemark;

    private Integer logisticId;
    private String logisticNumber;
    private String logisticName;
    private Double logisticPrice;
    private String logisticState;
    private Date deliveryDate;
}
