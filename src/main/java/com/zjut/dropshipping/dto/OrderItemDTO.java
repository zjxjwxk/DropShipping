package com.zjut.dropshipping.dto;

import lombok.Data;

/**
 * @author zjxjwxk
 */
@Data
public class OrderItemDTO {

    private Integer goodsId;
    private String name;
    private Integer amount;
    private Double price;
}
