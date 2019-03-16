package com.zjut.dropshipping.repository;

import com.zjut.dropshipping.dataobject.WarehouseItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WarehouseItemRepository extends JpaRepository<WarehouseItem, Integer> {
    List<WarehouseItem> findByWarehouseId(Integer warehouseId);

    WarehouseItem findByWarehouseIdAndGoodsId(Integer warehouseId,Integer goodsId);
}
