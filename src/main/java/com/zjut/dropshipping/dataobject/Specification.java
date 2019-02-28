package com.zjut.dropshipping.dataobject;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author zjxjwxk
 */
@Entity
@Data
public class Specification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer specId;
    private String name;
    private String value;
}
