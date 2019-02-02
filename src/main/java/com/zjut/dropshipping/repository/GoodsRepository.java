package com.zjut.dropshipping.repository;

import com.zjut.dropshipping.dataobject.Goods;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zjxjwxk
 */
public interface GoodsRepository extends JpaRepository<Goods, Integer> {

    Page<Goods> findByCategoryId(Integer categoryId, Pageable pageable);
}
