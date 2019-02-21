package com.zjut.dropshipping.dataobject;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author zjxjwxk
 */
@Entity
@Data
public class Specification {

    @Id
    private Integer specId;
    private String name;
    private String value;
}
