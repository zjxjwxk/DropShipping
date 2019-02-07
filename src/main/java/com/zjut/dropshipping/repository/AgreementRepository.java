package com.zjut.dropshipping.repository;

import com.zjut.dropshipping.dataobject.Agreement;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zjxjwxk
 */
public interface AgreementRepository extends JpaRepository<Agreement, Integer> {

    Agreement findByProducerIdAndAgentId(Integer producerId, Integer agentId);
}
