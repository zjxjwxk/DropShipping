package com.zjut.dropshipping.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author zjxjwxk
 */
@Data
public class OrderDTO {

    private Integer orderId;
    private Integer goodsId;
    private String goodsName;
    private Double price;
    private Integer amount;
    private String state;
    private Date createTime;

    private String buyerName;
    private String buyerPhone;
    private String buyerAddress;

}
