package com.zjut.dropshipping.dto;

import lombok.Data;

import java.util.List;

/**
 * @author zjxjwxk
 */
@Data
public class CategoryDTO {

    private Integer id;
    private String name;
    private Integer rank;
    private Integer childSize;
    private List<CategoryDTO> childList;
    private String imagePath;

}
