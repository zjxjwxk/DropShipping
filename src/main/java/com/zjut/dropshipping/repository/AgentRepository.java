package com.zjut.dropshipping.repository;

import com.zjut.dropshipping.dataobject.Agent;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;


/**
 * @author zjxjwxk
 */
public interface AgentRepository extends JpaRepository<Agent, Integer> {

    Agent findOneById(Integer id);

    Integer countByPhone(String phone);

    Integer countByIdentityNumber(String identityNumber);

    Integer countByExternalShop(String externalShop);

    Agent findByPhoneAndPassword(String phone, String password);

    Page<Agent> findAllByState(String state, Pageable pageable);

    @Query(value="select * from agent where id in (select agent_id from agreement where producer_id =?1 and state='正常') and state='正常' ", nativeQuery = true)
    Page<Agent> findAcceptedAgentByProducerId(Integer producerId, Pageable pageable);

    @Query("select new Agent(agent.id, agent.name) from Agent agent where id = ?1")
    Agent findIdAndName(Integer id);

}
