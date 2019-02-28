package com.zjut.dropshipping.dataobject;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.util.Date;

/**
 * @author zjxjwxk
 */
@Entity
@Data
@IdClass(ShoppingCartMultiKeys.class)
public class ShoppingCart {

    @Id
    private Integer agentId;
    private String goodsSpecIds;
    private Integer amount;
    @CreatedDate
    private Date createTime;

    public ShoppingCart() {
    }

    public ShoppingCart(Integer agentId, String goodsSpecIds, Integer amount) {
        this.agentId = agentId;
        this.goodsSpecIds = goodsSpecIds;
        this.amount = amount;
    }
}
