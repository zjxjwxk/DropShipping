package com.zjut.dropshipping.dataobject;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author zjxjwxk
 */
@Entity
@Data
public class CountryCurrency {

    @Id
    private String country;
    private String currency;
}
