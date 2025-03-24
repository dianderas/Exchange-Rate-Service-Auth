package com.enzoneyra.exchangeservice.domain.model;

import reactor.core.publisher.Mono;

import java.util.Arrays;

public enum Currency {
    USD,
    PEN,
    EUR;

    public static Mono<Currency> fromString(String code) {
        return Mono.justOrEmpty(Arrays.stream(Currency.values())
                        .filter(c -> c.name().equalsIgnoreCase(code))
                        .findFirst())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Invalid currency code: " + code)));
    }
}
