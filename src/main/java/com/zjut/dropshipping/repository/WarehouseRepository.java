package com.zjut.dropshipping.repository;
import com.zjut.dropshipping.dataobject.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface WarehouseRepository extends JpaRepository<Warehouse, Integer>  {

    @Query(value="select * from warehouse where producer_id = ?1 group by country" ,nativeQuery=true)
    List<Warehouse> findByProducerIdGroupByCountry(Integer producerId);

    @Query(value="select id from warehouse where country = ?1" ,nativeQuery=true)
    List<Integer> findIdByCountry(String country);

    List<Warehouse> findByCountry(String country);

}
