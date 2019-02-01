package com.zjut.dropshipping.repository;

import com.zjut.dropshipping.dataobject.Category;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void testFindByParendId() {
        List<Category> categoryList = categoryRepository.findByParentId(111111);
        System.out.println(categoryList.size() == 0);
        System.out.println(categoryList);
    }
}