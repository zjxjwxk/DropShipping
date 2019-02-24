package com.zjut.dropshipping.repository;

import com.zjut.dropshipping.dataobject.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author zjxjwxk
 */
public interface SpecificationRepository extends JpaRepository<Specification, Integer> {

    Specification findBySpecId(Integer specId);

    Specification findByName(String name);

    Specification findByValue(String value);
}
