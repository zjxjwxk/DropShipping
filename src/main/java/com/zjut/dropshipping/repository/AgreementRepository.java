package com.zjut.dropshipping.repository;

import com.zjut.dropshipping.dataobject.Agreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * @author zjxjwxk
 */
public interface AgreementRepository extends JpaRepository<Agreement, Integer> {

    Agreement findByProducerIdAndAgentId(Integer producerId, Integer agentId);

    @Query("select time from Agreement where producer_id = ?1 and agent_id = ?2")
    Date findTimeByProducerIdAndAgentId(Integer producerId, Integer agentId);

    List<Agreement> findByAgentIdAndState(Integer agentId, String state);

    List<Agreement> findByProducerIdAndState(Integer producerId, String state);

    Agreement findByAgentIdAndProducerIdAndState(Integer agentId, Integer producerId, String state);

    @Query("select producerId from Agreement where agentId = ?1 and state = ?2")
    List<Integer> findProducerIdListByAgentIdAndState(Integer agentId, String state);
}
