package com.zjut.dropshipping.repository;

import com.zjut.dropshipping.dataobject.Agent;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zjxjwxk
 */
public interface AgentRepository extends JpaRepository<Agent, Integer> {

    Integer countByPhone(String phone);

    Integer countByIdentityNumber(String identityNumber);

    Integer countByExternalShop(String externalShop);
}
