package com.enzoneyra.exchangeservice.infrastructure.persistence;

import com.enzoneyra.exchangeservice.domain.model.Currency;
import com.enzoneyra.exchangeservice.domain.model.ExchangeRate;
import com.enzoneyra.exchangeservice.domain.repository.ExchangeRateRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class ExchangeRateRepositoryImpl implements ExchangeRateRepository {
    private final R2dbcEntityTemplate template;

    public ExchangeRateRepositoryImpl(R2dbcEntityTemplate template) {
        this.template = template;
    }

    @Override
    public Mono<ExchangeRate> findLatestRate(Currency sourceCurrency, Currency targetCurrency) {
        return template
                .select(
                        Query.query(
                                        Criteria.where("source_currency").is(sourceCurrency.name())
                                                .and("target_currency").is(targetCurrency.name())
                                )
                                .sort(Sort.by(Sort.Order.desc("valid_until")))
                                .limit(1),
                        ExchangeRate.class
                )
                .next();
    }

    @Override
    public Mono<Void> save(ExchangeRate exchangeRate) {
        return template.insert(exchangeRate).then();
    }
}
