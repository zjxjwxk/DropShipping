package com.zjut.dropshipping.dto;

import lombok.Data;

import java.util.List;

/**
 * @author zjxjwxk
 */
@Data
public class ShoppingCartItemListDTO {

    private Integer producerId;
    private String producerName;
    private List<ShoppingCartItemDTO> shoppingCartItemDTOList;
}
