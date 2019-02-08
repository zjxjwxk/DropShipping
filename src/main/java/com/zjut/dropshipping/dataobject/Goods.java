package com.zjut.dropshipping.dataobject;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author zjxjwxk
 */
@Entity
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Goods {

    @Id
    private Integer goodsId;
    private Integer producerId;
    private Integer categoryId;
    private String name;
    private Double price;
    private Integer stock;
    private String state;
    private Date createTime;
    private Date updateTime;
    private String content;
}
