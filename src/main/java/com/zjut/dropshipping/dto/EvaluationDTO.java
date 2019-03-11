package com.zjut.dropshipping.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EvaluationDTO {

    private Integer level;
    private String content;
    private Date createTime;

    public EvaluationDTO() {
    }

    public EvaluationDTO(Integer level, String content) {
        this.level = level;
        this.content = content;
    }
}
