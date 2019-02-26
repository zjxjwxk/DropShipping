package com.zjut.dropshipping.dto;

import lombok.Data;

/**
 * @author zjxjwxk
 */
@Data
public class SpecificationDTO {

    private Integer goodsSpecId;
    private String name;
    private String value;
    private Double priceDifference;

    public SpecificationDTO() {
    }

    public SpecificationDTO(Integer goodsSpecId, String name, String value, Double priceDifference) {
        this.goodsSpecId = goodsSpecId;
        this.name = name;
        this.value = value;
        this.priceDifference = priceDifference;
    }
}
