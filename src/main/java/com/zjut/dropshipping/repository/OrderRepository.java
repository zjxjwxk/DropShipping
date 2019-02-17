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

    @Query(value = "SELECT SUM(amount) from order_item WHERE order_id IN (SELECT order_id from `order` WHERE agent_id = ?1)", nativeQuery = true)
    Integer findTotalAmountByAgentId(Integer agentId);
}
