package com.zjut.dropshipping.service;

import com.zjut.dropshipping.common.ServerResponse;
import com.zjut.dropshipping.dto.CategoryDTO;

import java.util.List;

/**
 * @author zjxjwxk
 */
public interface CategoryService {

    ServerResponse<List<CategoryDTO>> getAllCategories();

    List<Integer> getCategoryAndChildrenIdListByParentId(Integer categoryId);
}
