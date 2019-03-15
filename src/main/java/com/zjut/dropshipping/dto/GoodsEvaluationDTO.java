package com.zjut.dropshipping.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

/**
 * @author zjxjwxk
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GoodsEvaluationDTO {

    private Integer goodsId;
    private Integer agentId;
    private String agentName;
    private Integer level;
    private String content;
    private Date createTime;

    public GoodsEvaluationDTO() {
    }

    public GoodsEvaluationDTO(Integer goodsId, Integer level, String content, Date createTime) {
        this.goodsId = goodsId;
        this.level = level;
        this.content = content;
        this.createTime = createTime;
    }
}
