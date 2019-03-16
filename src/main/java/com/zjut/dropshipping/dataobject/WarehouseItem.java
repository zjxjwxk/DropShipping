package com.zjut.dropshipping.dataobject;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
@Entity
@Data
@IdClass(WarehouseItemMultiKeys.class)
public class WarehouseItem {
    @Id
    private Integer warehouseId;
    private Integer goodsId;
    private Integer amount;
}
