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
public class Producer {

    @Id
    private Integer id;
    private String name;
    private String contactName;
    private String contactPhone;
    private String contactIdentityNumber;
    private String region;
    private String licenseNumber;
    private String state;
    private Date joinTime;
    private Integer registerCapital;
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
