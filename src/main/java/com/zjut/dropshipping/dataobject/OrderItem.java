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
    private String goodsSpecIds;
    private Integer amount;

    @Override
    public String toString() {
        return "OrderItem{" +
                "orderId=" + orderId +
                ", goodsId=" + goodsId +
                ", goodsSpecIds='" + goodsSpecIds + '\'' +
                ", amount=" + amount +
                '}';
    }
}
