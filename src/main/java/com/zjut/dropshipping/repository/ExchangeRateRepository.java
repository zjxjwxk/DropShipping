package com.zjut.dropshipping.repository;

import com.zjut.dropshipping.dataobject.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zjxjwxk
 */
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, String> {

    ExchangeRate findByName(String name);
}
