package com.zjut.dropshipping.service.impl;

import com.zjut.dropshipping.common.ServerResponse;
import com.zjut.dropshipping.dataobject.Category;
import com.zjut.dropshipping.repository.CategoryRepository;
import com.zjut.dropshipping.dto.CategoryDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryServiceImplTest {

    @Autowired
    private CategoryRepository categoryRepository;

    public ServerResponse<List<CategoryDTO>> getAllCategories() {
        return ServerResponse.createBySuccess(getAllCategoriesByParentId(0));
    }

    private List<CategoryDTO> getAllCategoriesByParentId(Integer parentId) {
        List<Category> categoryList = categoryRepository.findByParentId(parentId);
        if (categoryList.size() == 0) {
            return null;
        }
        List<CategoryDTO> categoryDTOList = new ArrayList<>();
        for (Category category :
                categoryList) {
            CategoryDTO categoryDTO = new CategoryDTO();
            categoryDTO.setId(category.getId());
            categoryDTO.setName(category.getName());
            categoryDTO.setRank(category.getRank());
            List<CategoryDTO> childList = getAllCategoriesByParentId(category.getId());
            if (childList == null) {
                categoryDTO.setChildSize(0);
                categoryDTO.setChildList(null);
            } else {
                categoryDTO.setChildSize(childList.size());
                categoryDTO.setChildList(childList);
            }

            categoryDTOList.add(categoryDTO);
        }
        return categoryDTOList;
    }

    @Test
    public void testGetAllCategories() {
        System.out.println((getAllCategoriesByParentId(0)));
    }
}