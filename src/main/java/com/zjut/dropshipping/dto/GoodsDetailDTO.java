package com.zjut.dropshipping.dto;

import lombok.Data;

/**
 * @author zjxjwxk
 */
@Data
public class GoodsDetailDTO {

    private Integer goodsId;
    private Integer producerId;
    private Integer categoryId;
    private String name;
    private Double price;
    private Integer stock;
    private String status;
}
