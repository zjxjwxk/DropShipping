package com.zjut.dropshipping.repository;

import com.zjut.dropshipping.dataobject.RefundStatus;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zjxjwxk
 */
public interface RefundStatusRepository extends JpaRepository<RefundStatus, Integer> {

    RefundStatus findOneByOrderId(Integer orderId);

}
