package com.zjut.dropshipping.dataobject;

import lombok.Data;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import java.util.Date;

/**
 * 外币兑人民币汇率实体
 * @author zjxjwxk
 */
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class ExchangeRate {

    @Id
    private String name;
    private Double rate;
    @LastModifiedDate
    private Date updateTime;

    public ExchangeRate() {
    }

    public ExchangeRate(String name, Double rate) {
        this.name = name;
        this.rate = rate;
    }
}
