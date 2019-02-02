package com.zjut.dropshipping.service.impl;

import com.zjut.dropshipping.dataobject.Goods;
import com.zjut.dropshipping.repository.GoodsRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodsServiceImplTest {

    @Autowired
    private GoodsRepository goodsRepository;

    private Page<Goods> findGoodsByCategoryId(Integer categoryId) {
        PageRequest pageRequest = PageRequest.of(0, 10);
        return goodsRepository.findByCategoryId(categoryId, pageRequest);
    }

    @Test
    public void testFindByCategoryId() {
        System.out.println(findGoodsByCategoryId(4));
    }

}