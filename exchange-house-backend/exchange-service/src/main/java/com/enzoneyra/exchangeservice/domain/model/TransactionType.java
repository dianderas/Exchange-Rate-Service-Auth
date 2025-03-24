package com.enzoneyra.exchangeservice.domain.model;

import reactor.core.publisher.Mono;
import java.util.Arrays;

public enum TransactionType {
    SELL,
    BUY;

    public static Mono<TransactionType> fromString(String code) {
        return Mono.justOrEmpty(Arrays.stream(TransactionType.values())
                        .filter(c -> c.name().equalsIgnoreCase(code))
                        .findFirst())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Invalid transaction type code: " + code)));
    }
}
