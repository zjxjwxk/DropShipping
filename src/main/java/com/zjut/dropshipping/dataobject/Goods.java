package com.zjut.dropshipping.dataobject;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer goodsId;
    private Integer producerId;
    private Integer categoryId;
    private String name;
    private Double price;
    private Integer stock;
    private String state;
    @CreatedDate
    private Date createTime;
    @LastModifiedDate
    private Date updateTime;
    private String content;

}
