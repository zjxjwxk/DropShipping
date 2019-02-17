package com.zjut.dropshipping.dataobject;

import lombok.Data;

import javax.persistence.Entity;

/**
 * @author zjxjwxk
 */
@Entity
@Data
public class OrderItem {

    private String orderId;
    private String goodsId;
    private String amount;
}
