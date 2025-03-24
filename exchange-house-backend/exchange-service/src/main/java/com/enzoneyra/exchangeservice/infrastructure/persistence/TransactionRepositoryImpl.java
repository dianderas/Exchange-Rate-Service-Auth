package com.enzoneyra.exchangeservice.infrastructure.persistence;

import com.enzoneyra.exchangeservice.domain.model.Transaction;
import com.enzoneyra.exchangeservice.domain.repository.TransactionRepository;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {
    private final R2dbcEntityTemplate template;

    public TransactionRepositoryImpl(R2dbcEntityTemplate template) {
        this.template = template;
    }
    @Override
    public Mono<Void> save(Transaction transaction) {
        return template.insert(transaction).then();
    }
}
