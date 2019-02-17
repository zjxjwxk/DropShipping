package com.zjut.dropshipping.repository;

import com.zjut.dropshipping.dataobject.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author zjxjwxk
 */
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

    List<OrderItem> findByOrderId(Integer orderId);

    @Query("select sum(amount) from OrderItem where goodsId = ?1")
    Integer findTotalAmountByGoodsId(Integer goodsId);
}
