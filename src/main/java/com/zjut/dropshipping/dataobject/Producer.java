package com.zjut.dropshipping.dataobject;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * @author zjxjwxk
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Producer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String password;
    private String contactName;
    private String contactPhone;
    private String contactIdentityNumber;
    private String region;
    private String licenseNumber;
    private String state;
    @CreatedDate
    private Date joinTime;
    @NumberFormat(pattern = "#,###")
    private Integer registerCapital;
    @CreatedDate
    private Date registerTime;
    private String credibility;
    private String content;

    public void setNull() {
        this.setContactIdentityNumber(null);
        this.setLicenseNumber(null);
        this.setState(null);
        this.setJoinTime(null);
        this.setCredibility(null);
    }
}
