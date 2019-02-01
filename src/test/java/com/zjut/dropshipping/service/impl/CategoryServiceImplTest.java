package com.zjut.dropshipping.service.impl;

import com.zjut.dropshipping.common.ServerResponse;
import com.zjut.dropshipping.dataobject.Category;
import com.zjut.dropshipping.repository.CategoryRepository;
import com.zjut.dropshipping.repository.CategoryRepositoryTest;
import com.zjut.dropshipping.vo.CategoryVO;
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

    public ServerResponse<List<CategoryVO>> getAllCategories() {
        return ServerResponse.createBySuccess(getAllCategoriesByParentId(0));
    }

    private List<CategoryVO> getAllCategoriesByParentId(Integer parentId) {
        List<Category> categoryList = categoryRepository.findByParentId(parentId);
        if (categoryList.size() == 0) {
            return null;
        }
        List<CategoryVO> categoryVOList = new ArrayList<>();
        for (Category category :
                categoryList) {
            CategoryVO categoryVO = new CategoryVO();
            categoryVO.setId(category.getId());
            categoryVO.setName(category.getName());
            categoryVO.setRank(category.getRank());
            List<CategoryVO> childList = getAllCategoriesByParentId(category.getId());
            if (childList == null) {
                categoryVO.setChildSize(0);
                categoryVO.setChildList(null);
            } else {
                categoryVO.setChildSize(childList.size());
                categoryVO.setChildList(childList);
            }

            categoryVOList.add(categoryVO);
        }
        return categoryVOList;
    }

    @Test
    public void testGetAllCategories() {
        System.out.println((getAllCategoriesByParentId(0)));
    }
}