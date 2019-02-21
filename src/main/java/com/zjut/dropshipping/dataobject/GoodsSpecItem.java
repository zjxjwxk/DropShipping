package com.zjut.dropshipping.dataobject;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author zjxjwxk
 */
@Entity
@Data
public class GoodsSpecItem {

    @Id
    private Integer goodsSpecId;
    private Integer goodsId;
    private Integer specId;
    private Double priceDifference;
}
