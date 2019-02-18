package com.zjut.dropshipping.dataobject;

import lombok.Data;

import javax.persistence.*;

/**
 * @author zjxjwxk
 */
@Entity
@Data
@IdClass(OrderItemMultiKeys.class)
public class OrderItem {

    @Id
    private Integer orderId;
    private Integer goodsId;
    private Integer amount;
}
