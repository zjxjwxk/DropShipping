package com.zjut.dropshipping.repository;

import com.zjut.dropshipping.dataobject.Logistic;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zjxjwxk
 */
public interface LogisticRepository extends JpaRepository<Logistic, Integer> {

    Logistic findByOrderId(Integer orderId);
}
