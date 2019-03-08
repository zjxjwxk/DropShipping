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

    @Query(value="select sum(order_id) from `order` where producer_id = ?1 " +
            "and state= '已完成 '" +
            "and curdate() between date_format( DATE_SUB( curdate(), INTERVAL ?2 MONTH ),'%y-%m-%d') " +
            "and  date_format( DATE_SUB( curdate(), INTERVAL ?3 MONTH ), '%y-%m-%d' )", nativeQuery = true)
    Integer getProducerOrderAmountByMonth(Integer producerId,int timeGetAhead,int timeGetBehind);

    @Query(value="select sum(order_id) from `order` where producer_id = ?1 " +
            "and state= '已完成 '" +
            "and curdate() between date_format( DATE_SUB( curdate(), INTERVAL ?2 DAY ),'%y-%m-%d') " +
            "and  date_format( DATE_SUB( curdate(), INTERVAL ?3 DAY ), '%y-%m-%d' )", nativeQuery = true)
    Integer getProducerOrderAmountByDay(Integer producerId,int timeGetAhead,int timeGetBehind);


}
