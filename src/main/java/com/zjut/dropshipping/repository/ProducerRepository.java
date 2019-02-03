package com.zjut.dropshipping.repository;

import com.zjut.dropshipping.dataobject.Producer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zjxjwxk
 */
public interface ProducerRepository extends JpaRepository<Producer, Integer> {

    Producer findOneById(Integer id);
}
