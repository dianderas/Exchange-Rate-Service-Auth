package com.enzoneyra.exchangeservice.service;

import com.enzoneyra.exchangeservice.domain.model.Currency;
import com.enzoneyra.exchangeservice.domain.model.Transaction;
import com.enzoneyra.exchangeservice.domain.model.TransactionType;
import com.enzoneyra.exchangeservice.domain.repository.ExchangeRateRepository;
import com.enzoneyra.exchangeservice.domain.repository.TransactionRepository;
import com.enzoneyra.exchangeservice.infrastructure.dto.TransactionRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {
    private final ExchangeRateRepository exchangeRateRepository;
    private final TransactionRepository transactionRepository;

    public Mono<Transaction> processTransaction(TransactionRequest request) {
        return Mono.zip(
                        TransactionType.fromString(request.getType()),
                        Currency.fromString(request.getSourceCurrency()),
                        Currency.fromString(request.getTargetCurrency())
                ).flatMap(tuple -> {
                    TransactionType type = tuple.getT1();
                    Currency source = tuple.getT2();
                    Currency target = tuple.getT3();

                    return exchangeRateRepository.findLatestRate(source, target)
                            .switchIfEmpty(Mono.error(new RuntimeException("Exchange rate not found")))
                            .flatMap(rate -> {
                                BigDecimal exchangeRate = (type == TransactionType.BUY) ? rate.getFxRateBuy() : rate.getFxRateSell();
                                BigDecimal amountTarget = request.getAmount().multiply(exchangeRate);

                                Transaction transaction = Transaction.builder()
                                        .id(UUID.randomUUID())
                                        .type(type)
                                        .amount(amountTarget)
                                        .sourceCurrency(source)
                                        .targetCurrency(target)
                                        .createdAt(Instant.now())
                                        .updatedAt(Instant.now())
                                        .build();

                                return transactionRepository.save(transaction)
                                        .thenReturn(transaction);
                            });
                }).doOnNext(response -> log.info("Transaction processed: {}", response))
                .doOnError(error -> log.error("Error processing transaction", error));
    }
}
