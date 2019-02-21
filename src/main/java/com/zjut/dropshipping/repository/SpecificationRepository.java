package com.zjut.dropshipping.repository;

import com.zjut.dropshipping.dataobject.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zjxjwxk
 */
public interface SpecificationRepository extends JpaRepository<Specification, Integer> {

    Specification findBySpecId(Integer specId);
}
