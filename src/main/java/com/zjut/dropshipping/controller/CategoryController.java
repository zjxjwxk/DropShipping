package com.zjut.dropshipping.controller;

import com.zjut.dropshipping.common.ServerResponse;
import com.zjut.dropshipping.service.CategoryService;
import com.zjut.dropshipping.vo.CategoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zjxjwxk
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/get_all_categories")
    @ResponseBody
    public ServerResponse<List<CategoryVO>> getAllCategories() {
        return categoryService.getAllCategories();
    }
}
