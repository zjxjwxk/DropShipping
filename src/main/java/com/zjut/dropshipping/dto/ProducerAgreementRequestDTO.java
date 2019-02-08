package com.zjut.dropshipping.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author zjxjwxk
 */
@Data
public class ProducerAgreementRequestDTO {

    private Integer id;
    private String name;
    private String contactName;
    private String contactPhone;
    private String region;
    private Integer registerCapital;
    private Date registerTime;
    private String content;

    private Date requestTime;
}
