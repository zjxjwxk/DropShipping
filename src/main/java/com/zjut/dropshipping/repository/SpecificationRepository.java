package com.zjut.dropshipping.repository;

import com.zjut.dropshipping.dataobject.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

/**
 * @author zjxjwxk
 */
public interface SpecificationRepository extends JpaRepository<Specification, Integer> {

    Specification findBySpecId(Integer specId);

    Specification findByNameAndValue(String name,String value);

    @Query(value="select spec_id from specification where name= ?1 and value=?2", nativeQuery = true)
    Integer findSpeId(String name,String value);

}
