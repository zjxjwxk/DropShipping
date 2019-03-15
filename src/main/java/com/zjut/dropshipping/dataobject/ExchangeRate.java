package com.zjut.dropshipping.dataobject;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 外币兑人民币汇率实体
 * @author zjxjwxk
 */
@Entity
@Data
public class ExchangeRate {

    @Id
    private String name;
    private Double rate;

    public ExchangeRate() {
    }

    public ExchangeRate(String name, Double rate) {
        this.name = name;
        this.rate = rate;
    }
}
