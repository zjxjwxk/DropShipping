package com.zjut.dropshipping.dto;

import com.zjut.dropshipping.dataobject.Specification;
import lombok.Data;

import java.util.List;

/**
 * @author zjxjwxk
 */
@Data
public class ShoppingCartItemDTO {

    private Integer goodsId;
    private String goodsName;

    private List<Specification> specificationList;

    private Double price;
    private Integer amount;

    public void addPrice(Double priceDifference) {
        price += priceDifference;
    }
}
