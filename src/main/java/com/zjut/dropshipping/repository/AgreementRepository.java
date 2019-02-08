package com.zjut.dropshipping.repository;

import com.zjut.dropshipping.dataobject.Agreement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author zjxjwxk
 */
public interface AgreementRepository extends JpaRepository<Agreement, Integer> {

    Agreement findByProducerIdAndAgentId(Integer producerId, Integer agentId);

    List<Agreement> findByAgentIdAndState(Integer agentId, String state);
}
