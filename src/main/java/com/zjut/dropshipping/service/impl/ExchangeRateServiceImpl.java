package com.zjut.dropshipping.service.impl;

import com.zjut.dropshipping.dataobject.CountryCurrency;
import com.zjut.dropshipping.dataobject.ExchangeRate;
import com.zjut.dropshipping.repository.CountryCurrencyRepository;
import com.zjut.dropshipping.repository.ExchangeRateRepository;
import com.zjut.dropshipping.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zjxjwxk
 */
@Service("ExchangeRateService")
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final CountryCurrencyRepository countryCurrencyRepository;
    private final ExchangeRateRepository exchangeRateRepository;

    @Autowired
    public ExchangeRateServiceImpl(CountryCurrencyRepository countryCurrencyRepository,
                                   ExchangeRateRepository exchangeRateRepository) {
        this.countryCurrencyRepository = countryCurrencyRepository;
        this.exchangeRateRepository = exchangeRateRepository;
    }


    @Override
    public Double getExchangePrice(String country, Double originPrice) {
        if (country == null) {
            country = "美国";
        }
        CountryCurrency countryCurrency = countryCurrencyRepository.findByCountry(country);
        ExchangeRate exchangeRate = exchangeRateRepository.findByName(countryCurrency.getCurrency());
        return originPrice / exchangeRate.getRate();
    }
}
