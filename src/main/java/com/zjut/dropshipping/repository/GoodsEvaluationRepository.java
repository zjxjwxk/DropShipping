package com.zjut.dropshipping.repository;

import com.zjut.dropshipping.dataobject.GoodsEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author zjxjwxk
 */
public interface GoodsEvaluationRepository extends JpaRepository<GoodsEvaluation, Integer> {

    List<GoodsEvaluation> findByGoodsId(Integer goodsId);

    @Query(value="select avg(level) from GoodsEvaluation where goodsId = ?1", nativeQuery = true)
    Double findAverageLevelByGoodsId(Integer goodsId);
}
