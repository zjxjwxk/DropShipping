package com.zjut.dropshipping.dto;

import lombok.Data;

import java.util.List;

/**
 * @author zjxjwxk
 */
@Data
public class OrderItemDTO {

    private Integer goodsId;
    private String name;
    private List<SpecificationDTO> specificationList;
    private Integer amount;
    private Double price;

    public void addPrice(Double priceDifference) {
        price += priceDifference;
    }
}
