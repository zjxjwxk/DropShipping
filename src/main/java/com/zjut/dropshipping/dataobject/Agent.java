package com.zjut.dropshipping.dataobject;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * @author zjxjwxk
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
public class Agent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String phone;
    private String password;
    private String identityNumber;
    private String region;
    private String state;
    @CreatedDate
    private Date joinTime;
    private String externalShop;

    public Agent() {
    }

    public Agent(String name, String phone, String password, String identityNumber, String region, String state, String externalShop) {
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.identityNumber = identityNumber;
        this.region = region;
        this.state = state;
        this.externalShop = externalShop;
    }
}
