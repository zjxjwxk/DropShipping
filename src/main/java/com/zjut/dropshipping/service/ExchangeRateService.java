package com.zjut.dropshipping.service;

/**
 * @author zjxjwxk
 */
public interface ExchangeRateService {

    Double getExchangePrice(String country, Double originPrice);
}
