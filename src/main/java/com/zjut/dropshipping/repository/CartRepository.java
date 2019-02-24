package com.zjut.dropshipping.repository;

import com.zjut.dropshipping.dataobject.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author zjxjwxk
 */
public interface CartRepository extends JpaRepository<ShoppingCart, Integer> {

    List<ShoppingCart> findByAgentId(Integer agentId);

    ShoppingCart findByGoodsSpecIds(String goodsSpecIds);
}
