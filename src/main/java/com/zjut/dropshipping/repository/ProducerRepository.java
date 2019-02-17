package com.zjut.dropshipping.repository;


import com.zjut.dropshipping.dataobject.Producer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author ljx
 */
public interface ProducerRepository extends JpaRepository<Producer, Integer> {

    Producer findOneById(Integer id);

    Integer countByName(String name);

    Integer countByContactPhone(String contactPhone);

    Integer countByContactIdentityNumber(String contactIdentityNumber);

    Integer countByLicenseNumber(String name);

    Producer findByContactPhoneAndPassword(String contactPhone, String password);

    @Override
    Page<Producer> findAll(Pageable pageable);

    @Query("select new Producer(producer.id, producer.name) from Producer producer where id = ?1")
    Producer findIdAndNameById(Integer id);
}
