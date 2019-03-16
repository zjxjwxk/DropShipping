package com.zjut.dropshipping.repository;

import com.zjut.dropshipping.dataobject.CountryCurrency;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zjxjwxk
 */
public interface CountryCurrencyRepository extends JpaRepository<CountryCurrency, String> {

    CountryCurrency findByCountry(String country);
}
