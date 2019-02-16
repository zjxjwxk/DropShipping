package com.zjut.dropshipping.repository;

import com.zjut.dropshipping.dataobject.Agent;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;

/**
 * @author zjxjwxk
 */
public interface AgentRepository extends JpaRepository<Agent, Integer> {

    Agent findOneById(Integer id);

    Integer countByPhone(String phone);

    Integer countByIdentityNumber(String identityNumber);

    Integer countByExternalShop(String externalShop);

    Agent findByPhoneAndPassword(String phone, String password);

    Page<Agent> findByState(String state, Pageable pageable);
}
