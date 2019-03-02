package com.zjut.dropshipping.dataobject;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.io.Serializable;
import java.util.Date;

/**
 * @author zjxjwxk
 */
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
@IdClass(RefundStatus.class)
public class RefundStatus implements Serializable {

    @Id
    private Integer orderId;
    private String refundStatus;
    @CreatedDate
    private Date updateTime;

    public RefundStatus() {
    }

    public RefundStatus(Integer orderId, String refundStatus) {
        this.orderId = orderId;
        this.refundStatus = refundStatus;
    }
}
