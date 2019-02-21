package com.zjut.dropshipping.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author ljx
 */
@Data
public class AgentAgreementRequestDTO {

    private Integer id;
    private String name;
    private String phone;
    private String region;
    private Date joinTime;
}
