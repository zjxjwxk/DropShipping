package com.zjut.dropshipping.dataobject;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer producerId;
    private String country;
}
