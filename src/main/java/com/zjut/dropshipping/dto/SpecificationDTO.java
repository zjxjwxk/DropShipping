package com.zjut.dropshipping.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @author zjxjwxk
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
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
