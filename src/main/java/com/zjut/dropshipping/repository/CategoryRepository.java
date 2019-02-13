package com.zjut.dropshipping.repository;

import com.zjut.dropshipping.dataobject.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author zjxjwxk
 */
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Category findOneById(Integer categoryId);

    List<Category> findByParentId(Integer parentId);
}
