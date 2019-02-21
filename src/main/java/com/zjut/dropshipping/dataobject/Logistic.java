package com.zjut.dropshipping.dataobject;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

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
public class Logistic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer logisticId;
    private Integer orderId;
    private String name;
    private String logisticNumber;
    private String state;
    private Date deliveryDate;
    private Double price;
}
