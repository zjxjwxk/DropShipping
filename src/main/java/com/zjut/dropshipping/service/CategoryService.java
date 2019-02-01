package com.zjut.dropshipping.service;

import com.zjut.dropshipping.common.ServerResponse;
import com.zjut.dropshipping.vo.CategoryVO;

import java.util.List;

/**
 * @author zjxjwxk
 */
public interface CategoryService {

    ServerResponse<List<CategoryVO>> getAllCategories();
}
