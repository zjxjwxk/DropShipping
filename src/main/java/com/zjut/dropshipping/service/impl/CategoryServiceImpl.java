package com.zjut.dropshipping.service.impl;

import com.zjut.dropshipping.common.ServerResponse;
import com.zjut.dropshipping.dataobject.Category;
import com.zjut.dropshipping.repository.CategoryRepository;
import com.zjut.dropshipping.service.CategoryService;
import com.zjut.dropshipping.utils.PropertiesUtil;
import com.zjut.dropshipping.dto.CategoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author zjxjwxk
 */
@Service("CategoryService")
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ServerResponse<List<CategoryDTO>> getAllCategories() {
        return ServerResponse.createBySuccess(this.getAllCategoriesByParentId(0));
    }

    @Override
    public List<Integer> getCategoryAndChildrenIdListByParentId(Integer categoryId) {
        Set<Category> categorySet = new HashSet<>();
        this.findChildCategory(categorySet, categoryId);

        List<Integer> categoryIdList = new ArrayList<>();
        if (categoryId != null) {
            for (Category categoryItem :
                    categorySet) {
                categoryIdList.add(categoryItem.getId());
            }
        }
        return categoryIdList;
    }

    /**
     * 递归算法，获得子分类节点
     */
    private void findChildCategory(Set<Category> categorySet, Integer categoryId) {
        Category category = categoryRepository.findOneById(categoryId);
        if (category != null) {
            categorySet.add(category);
        }
        // 查找子节点，如果子节点为空退出递归
        List<Category> categoryList = categoryRepository.findByParentId(categoryId);
        for (Category categoryItem :
                categoryList) {
            findChildCategory(categorySet, categoryItem.getId());
        }
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
                categoryDTO.setImagePath(PropertiesUtil.getProperty("ftp.server.http.prefix") + "category/" + category.getId() + ".jpg");
            } else {
                categoryDTO.setChildSize(childList.size());
                categoryDTO.setChildList(childList);
            }

            categoryDTOList.add(categoryDTO);
        }
        return categoryDTOList;
    }
}
