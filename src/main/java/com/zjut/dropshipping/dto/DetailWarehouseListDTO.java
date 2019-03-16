package com.zjut.dropshipping.dto;

import lombok.Data;
import com.zjut.dropshipping.dto.WarehouseGoodsListDTO;
import java.util.List;

@Data
public class DetailWarehouseListDTO {
    private Integer id;
    private List<WarehouseGoodsListDTO> goodsList;
}
