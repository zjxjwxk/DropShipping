package com.zjut.dropshipping.repository;

import com.zjut.dropshipping.dataobject.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Calendar;
/**
 * @author zjxjwxk
 */
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

    List<OrderItem> findByOrderId(Integer orderId);

    @Query("select sum(amount) from OrderItem where goodsId = ?1")
    Integer findTotalAmountByGoodsId(Integer goodsId);

    @Modifying
    @Transactional
    void deleteByOrderId(Integer orderId);

    @Query(value = "select sum(order_item.amount*goods.price) from order_item,goods where order_item.order_id in" +
            "(select order_id from `order` where producer_id=?1 " +
            "and state='已完成' " +
            "and update_time between date_format( DATE_SUB( curdate(), INTERVAL ?2 MONTH ),'%y-%m-%d') " +
            "and  date_format( DATE_SUB( curdate(), INTERVAL ?3 MONTH ), '%y-%m-%d' )) and order_item.goods_id=goods.goods_id  ",nativeQuery = true)
    Integer getProducerSaleByMonth(Integer producerId,int timeGetAhead,int timeGetBehind);

    @Query(value = "select sum(order_item.amount*goods.price) from order_item,goods where order_item.order_id in" +
            "(select order_id from `order` where producer_id=?1 " +
            "and state='已完成' " +
            "and curdate() between date_format( DATE_SUB( curdate(), INTERVAL ?2 DAY ),'%y-%m-%d') " +
            "and  date_format( DATE_SUB( curdate(), INTERVAL ?3 DAY ), '%y-%m-%d' )) and order_item.goods_id=goods.goods_id  ",nativeQuery = true)
    Integer getProducerSaleByDay(Integer producerId,int timeGetAhead,int timeGetBehind);


    @Query(value = "select sum(amount) from order_item where order_id in " +
            "(select order_id from `order` where producer_id=?1 and state='已完成' " +
            "and curdate() between date_format( DATE_SUB( curdate(), INTERVAL ?2 MONTH ),'%y-%m-%d') " +
            "and  date_format( DATE_SUB( curdate(), INTERVAL ?3 MONTH ), '%y-%m-%d' ) ) ",nativeQuery = true)
    Integer getProducerSaleAmountByMonth(Integer producerId,int timeGetAhead,int timeGetBehind);

    @Query(value = "select sum(amount) from order_item where order_id in " +
            "(select order_id from `order` where producer_id=?1 and state='已完成' " +
            "and curdate() between date_format( DATE_SUB( curdate(), INTERVAL ?2 DAY ),'%y-%m-%d') " +
            "and  date_format( DATE_SUB( curdate(), INTERVAL ?3 DAY ), '%y-%m-%d' ) ) ",nativeQuery = true)
    Integer getProducerSaleAmountByDay(Integer producerId,int timeGetAhead,int timeGetBehind);
}
