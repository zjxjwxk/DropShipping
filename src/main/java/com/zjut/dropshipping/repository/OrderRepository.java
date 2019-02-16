package com.zjut.dropshipping.repository;

import com.zjut.dropshipping.dataobject.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author zjxjwxk
 */
public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query(value="select sum(amount) from Order where goodsId = ?1", nativeQuery = true)
    Integer findAmountByGoodsId(Integer goodsId);

    @Query(value="select sum(amount) from Order where agentId = ?1", nativeQuery = true)
    Integer findAmountByAgentId(Integer agentId);
}
