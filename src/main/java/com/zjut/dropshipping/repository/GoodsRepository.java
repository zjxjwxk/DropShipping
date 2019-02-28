package com.zjut.dropshipping.repository;

import com.zjut.dropshipping.dataobject.Goods;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author zjxjwxk
 */
public interface GoodsRepository extends JpaRepository<Goods, Integer> {

    Page<Goods> findByCategoryId(Integer categoryId, Pageable pageable);

    Goods findByGoodsId(Integer goodsId);

    Page<Goods> findByCategoryIdIn(List<Integer> categoryIdList, Pageable pageable);

    Page<Goods> findByProducerIdInAndCategoryIdIn(List<Integer> producerId, List<Integer> categoryIdList, Pageable pageable);

    Page<Goods> findByProducerIdAndCategoryIdIn(Integer producerId, List<Integer> categoryIdList, Pageable pageable);

    @Query(value = "select pirce from goods where goods_id = ?1",nativeQuery = true)
    Double findPriceByGoodId(Integer goodsId);
}
