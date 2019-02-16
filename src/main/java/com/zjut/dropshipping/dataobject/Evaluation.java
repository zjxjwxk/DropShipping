package com.zjut.dropshipping.dataobject;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author ljx
 */
@Data
@Entity
@Table(name = "`evaluation`")
@EntityListeners(AuditingEntityListener.class)
@IdClass(Evaluation.class)
public class Evaluation implements Serializable {
    @Id
    private Integer producerId;
    private Integer agentId;
    private Integer direction;
    private Integer level;
    private String content;
    @CreatedDate
    private Date createTime;
}
