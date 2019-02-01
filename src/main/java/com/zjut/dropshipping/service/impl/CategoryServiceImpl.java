package com.zjut.dropshipping.service.impl;

import com.zjut.dropshipping.common.ServerResponse;
import com.zjut.dropshipping.dataobject.Category;
import com.zjut.dropshipping.repository.CategoryRepository;
import com.zjut.dropshipping.service.CategoryService;
import com.zjut.dropshipping.utils.PropertiesUtil;
import com.zjut.dropshipping.vo.CategoryVO;
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
                categoryVO.setImagePath(PropertiesUtil.getProperty("ftp.server.http.prefix") + "category/" + category.getId() + ".jpg");
            } else {
                categoryVO.setChildSize(childList.size());
                categoryVO.setChildList(childList);
            }

            categoryVOList.add(categoryVO);
        }
        return categoryVOList;
    }
}
