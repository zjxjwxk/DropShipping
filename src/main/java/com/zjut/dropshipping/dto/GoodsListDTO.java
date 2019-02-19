package com.zjut.dropshipping.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @author zjxjwxk
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GoodsListDTO {

    private Integer goodsId;
    private String name;
    private Double price;
    private Integer producerId;
    private String producerName;
}
