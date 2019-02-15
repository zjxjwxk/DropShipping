package com.zjut.dropshipping.repository;

import com.zjut.dropshipping.dataobject.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author zjxjwxk
 */
public interface OrderRepository extends JpaRepository<Order, Integer> {

    Order findOneByOrderId(Integer orderId);

    @Query("select sum(amount) from Order where goodsId = ?1")
    Integer findSalesVolumeByGoodsId(Integer goodsId);
}
