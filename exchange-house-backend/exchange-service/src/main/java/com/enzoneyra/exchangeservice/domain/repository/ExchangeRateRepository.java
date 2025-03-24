package com.enzoneyra.exchangeservice.domain.repository;

import com.enzoneyra.exchangeservice.domain.model.Currency;
import com.enzoneyra.exchangeservice.domain.model.ExchangeRate;
import reactor.core.publisher.Mono;

public interface ExchangeRateRepository {
    Mono<Void> save(ExchangeRate exchangeRate);
    Mono<ExchangeRate> findLatestRate(Currency sourceCurrency, Currency targetCurrency);
}
