package com.enzoneyra.exchangeservice.service;

import com.enzoneyra.exchangeservice.domain.model.Currency;
import com.enzoneyra.exchangeservice.domain.model.ExchangeRate;
import com.enzoneyra.exchangeservice.domain.repository.ExchangeRateRepository;
import com.enzoneyra.exchangeservice.infrastructure.dto.ExchangeRateRequest;
import com.enzoneyra.exchangeservice.infrastructure.exception.DatabaseException;
import com.enzoneyra.exchangeservice.infrastructure.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExchangeRateService {
    private final ExchangeRateRepository exchangeRateRepository;

    public Mono<ExchangeRate> getLatestExchangeRate(Currency sourceCurrency, Currency targetCurrency) {
        return exchangeRateRepository.findLatestRate(sourceCurrency, targetCurrency)
                .doOnNext(rate -> log.info("Exchange Rate Found: {}", rate))
                .doOnError(error -> log.error("Error en findLatestRate", error))
                .switchIfEmpty(Mono.error(new NotFoundException("Exchange rate not found")));
    }

    public Mono<Void> saveExchangeRate(ExchangeRateRequest request) {
        return Mono.zip(
                        Currency.fromString(request.getSourceCurrency()),
                        Currency.fromString(request.getTargetCurrency())
                )
                .flatMap(tuple -> {
                    ExchangeRate exchangeRate = ExchangeRate.builder()
                            .id(UUID.randomUUID())
                            .sourceCurrency(tuple.getT1())
                            .targetCurrency(tuple.getT2())
                            .fxRateBuy(request.getFxRateBuy())
                            .fxRateSell(request.getFxRateSell())
                            .createdAt(Instant.now())
                            .validUntil(Instant.now().plus(request.getDurationMinutes(), ChronoUnit.MINUTES))
                            .build();

                    return exchangeRateRepository.save(exchangeRate)
                            .onErrorMap(e -> new DatabaseException("Error saving exchange rate", e))
                            .then();
                });
    }
}
