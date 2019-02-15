package com.zjut.dropshipping.dataobject;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author zjxjwxk
 */
@Entity
@Data
@Table(name = "`order`")
public class Order {

    @Id
    private Integer orderId;
    private Integer agentId;
    private Integer producerId;
    private Integer goodsId;
    private Integer buyerId;
    private Integer amount;
    private String state;
    private String remark;
    @CreatedDate
    private Date createTime;
    private Date updateTime;
}
