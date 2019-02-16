package com.zjut.dropshipping.dataobject;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author zjxjwxk
 */
@Entity
@Data
public class Category {

    @Id
    private Integer id;
    private Integer parentId;
    private String name;
    private Integer rank;
    @CreatedDate
    private Date createTime;
    @LastModifiedDate
    private Date updateTime;
}
