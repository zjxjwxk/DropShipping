package com.zjut.dropshipping.repository;

import com.zjut.dropshipping.dataobject.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author zjxjwxk
 */
public interface OrderRepository extends JpaRepository<Order, Integer> {

    Order findOneByOrderId(Integer orderId);

    List<Order> findByAgentId(Integer agentId);

    @Query("select sum(amount) from Order where goodsId = ?1")
    Integer findAmountByGoodsId(Integer goodsId);

    @Query("select sum(amount) from Order where agentId = ?1")
    Integer findAmountByAgentId(Integer agentId);
}
