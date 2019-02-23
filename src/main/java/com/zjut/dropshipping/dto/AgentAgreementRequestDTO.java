package com.zjut.dropshipping.dto;

import lombok.Data;
import org.apache.commons.net.ntp.TimeStamp;

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
    private Date requestTime;
    private Integer monthlysale;
    private Integer level;

}
