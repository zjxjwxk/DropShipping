package com.zjut.dropshipping.dataobject;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author zjxjwxk
 */
@Entity
@Data
public class Goods {

    @Id
    private Integer goodsId;
    private Integer producerId;
    private Integer categoryId;
    private String name;
    private Double price;
    private Integer stock;
    private String status;
    private Date createTime;
    private Date updateTime;
}
