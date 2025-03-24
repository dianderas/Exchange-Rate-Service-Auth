package com.enzoneyra.exchangeservice.infrastructure.web;

import com.enzoneyra.exchangeservice.domain.model.Currency;
import com.enzoneyra.exchangeservice.infrastructure.dto.ExchangeRateRequest;
import com.enzoneyra.exchangeservice.infrastructure.dto.ExchangeRateResponse;
import com.enzoneyra.exchangeservice.service.ExchangeRateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Validated
@RestController
@RequestMapping("/exchange-rates")
@RequiredArgsConstructor
@Slf4j
public class ExchangeRateController {
    private final ExchangeRateService exchangeRateService;

    @GetMapping("/{source}/{target}")
    public Mono<ExchangeRateResponse> getExchangeRate(
            @PathVariable String source,
            @PathVariable String target
    ) {
        log.info("getExchangeRate source={}, target={}", source, target);
        return Mono.zip(Currency.fromString(source), Currency.fromString(target))
                .flatMap(tuple ->
                        exchangeRateService.getLatestExchangeRate(tuple.getT1(), tuple.getT2())
                                .map(ExchangeRateResponse::fromDomain));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> saveExchangeRate(@RequestBody @Valid ExchangeRateRequest request) {
        return exchangeRateService.saveExchangeRate(request);
    }
}
