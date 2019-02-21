package com.zjut.dropshipping.dataobject;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author zjxjwxk
 */
@Entity
@Data
@IdClass(AgreementMultiKeys.class)
@Table(name = "`agreement`")
public class Agreement {

    @Id
    private Integer producerId;
    @Id
    private Integer agentId;
    @CreatedDate
    private Date time;
    private String state;
}
