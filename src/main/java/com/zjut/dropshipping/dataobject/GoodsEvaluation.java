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
@EntityListeners(AuditingEntityListener.class)
@Data
@IdClass(GoodsEvaluationMultiKeys.class)
public class GoodsEvaluation implements Serializable {

    @Id
    private Integer orderId;
    private Integer goodsId;
    private Integer agentId;
    private Integer level;
    private String content;
    @CreatedDate
    private Date createTime;

    public GoodsEvaluation() {
    }

    public GoodsEvaluation(Integer orderId, Integer goodsId, Integer agentId, Integer level, String content) {
        this.orderId = orderId;
        this.goodsId = goodsId;
        this.agentId = agentId;
        this.level = level;
        this.content = content;
    }
}
