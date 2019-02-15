package com.zjut.dropshipping.dataobject;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.io.Serializable;
import java.util.Date;

/**
 * @author zjxjwxk
 */
@Entity
@Data
@IdClass(GoodsEvaluation.class)
public class GoodsEvaluation implements Serializable {

    @Id
    private Integer goodsId;
    private Integer agentId;
    private Integer level;
    private String content;
    @CreatedDate
    private Date createTime;
}
