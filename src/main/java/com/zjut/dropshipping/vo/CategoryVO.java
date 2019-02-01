package com.zjut.dropshipping.vo;

import lombok.Data;

import java.util.List;

/**
 * @author zjxjwxk
 */
@Data
public class CategoryVO {

    private Integer id;
    private String name;
    private Integer rank;
    private Integer childSize;
    private List<CategoryVO> childList;
    private String imagePath;

}
