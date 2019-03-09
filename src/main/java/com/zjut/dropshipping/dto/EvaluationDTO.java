package com.zjut.dropshipping.dto;

import lombok.Data;

import java.util.Date;

@Data
public class EvaluationDTO {

    private Integer level;
    private String content;
    private Date createTime;
}
