package com.zjut.dropshipping.dto;

import com.zjut.dropshipping.dataobject.Specification;
import lombok.Data;

import java.util.List;

/**
 * @author zjxjwxk
 */
@Data
public class OrderItemDTO {

    private Integer goodsId;
    private String name;
    private List<Specification> specificationList;
    private Integer amount;
    private Double price;
}
