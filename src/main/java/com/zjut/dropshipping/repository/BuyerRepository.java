package com.zjut.dropshipping.repository;

import com.zjut.dropshipping.dataobject.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zjxjwxk
 */
public interface BuyerRepository extends JpaRepository<Buyer, Integer> {

    Buyer findBuyerById(Integer id);

    Buyer findByNameAndPhoneAndAddress(String name, String phone, String address);
}
