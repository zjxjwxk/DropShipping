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
import java.util.List;

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
