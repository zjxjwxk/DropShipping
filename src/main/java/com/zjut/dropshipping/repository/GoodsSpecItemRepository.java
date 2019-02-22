package com.zjut.dropshipping.repository;

import com.zjut.dropshipping.dataobject.GoodsSpecItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author zjxjwxk
 */
public interface GoodsSpecItemRepository extends JpaRepository<GoodsSpecItem, Integer> {

    GoodsSpecItem findByGoodsSpecId(Integer goodsSpecId);

    List<GoodsSpecItem> findByGoodsId(Integer goodsId);
}
