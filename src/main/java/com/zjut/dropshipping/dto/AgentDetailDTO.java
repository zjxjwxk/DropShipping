package com.zjut.dropshipping.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author ljx
 */
@Data
public class AgentDetailDTO {

    private String phone;
    private String externalShop;
    private Date joinTime;
    private Integer level;
    private List<String> productSaleList;
}
