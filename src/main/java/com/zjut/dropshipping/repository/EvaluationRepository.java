package com.zjut.dropshipping.repository;

import com.zjut.dropshipping.dataobject.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author ljx
 */
public interface EvaluationRepository extends JpaRepository<Evaluation, Integer> {

    @Query("select avg(level) from Evaluation where direction = 1 and agentId = ?1")
    Integer findLevelByAgentId(Integer agentId);

    Evaluation findOneByOrderId(Integer OrderId);

    Evaluation findByOrderIdAndProducerId(Integer producerId,Integer agentId);
}
