package com.zjut.dropshipping.dataobject;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * @author zjxjwxk
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@Table(name = "`order`")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;
    private Integer agentId;
    private Integer buyerId;
    private Integer producerId;
    private String state;
    private String remark;
    @CreatedDate
    private Date createTime;
    @LastModifiedDate
    private Date updateTime;
}
