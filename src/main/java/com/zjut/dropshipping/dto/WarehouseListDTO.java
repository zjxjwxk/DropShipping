package com.zjut.dropshipping.dto;

import lombok.Data;

import java.util.List;
@Data
public class WarehouseListDTO {
    private String country;
    private List<Integer> warehouseId;
}
