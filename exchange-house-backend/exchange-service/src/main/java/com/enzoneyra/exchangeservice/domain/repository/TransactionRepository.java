package com.enzoneyra.exchangeservice.domain.repository;

import com.enzoneyra.exchangeservice.domain.model.Transaction;
import reactor.core.publisher.Mono;

public interface TransactionRepository {
    Mono<Void> save(Transaction transaction);
}
