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

    List<Order> findByProducerId(Integer producerId);

    List<Order> findByAgentId(Integer agentId);

    @Query(value="select sum(amount) from order_item where order_id in (select order_id from `order` where agent_id= ?1)", nativeQuery = true)
    Integer findAmountByAgentId(Integer agentId);

}
