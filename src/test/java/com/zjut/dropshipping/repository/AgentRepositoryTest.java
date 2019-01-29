package com.zjut.dropshipping.repository;

import com.zjut.dropshipping.dataobject.Agent;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AgentRepositoryTest {

    @Autowired
    private AgentRepository repository;

    @Test
    public void findOneTest() {
        Agent agent = repository.findById(1).orElse(null);
        System.out.println(agent);
    }

    @Test
    @Transactional
    public void saveTest() {
        Agent agent = new Agent("Wxk", "12311111111", "12345678", "332624199811111111"
                , "浙江省台州市", "正常", "Apple官方旗舰店");
        Agent result = repository.save(agent);
        Assert.assertNotNull(result);
        repository.save(agent);
    }

    @Test
    public void updateTest() {
        Agent agent = repository.findById(6).orElse(null);
        if (agent != null) {
            agent.setName("Test2");
            repository.save(agent);
        }
    }
}