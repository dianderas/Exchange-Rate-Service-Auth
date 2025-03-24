package com.enzoneyra.exchangeservice.infrastructure.web;

import com.enzoneyra.exchangeservice.infrastructure.dto.TransactionRequest;
import com.enzoneyra.exchangeservice.infrastructure.dto.TransactionResponse;
import com.enzoneyra.exchangeservice.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Validated
@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
@Slf4j
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<TransactionResponse> createTransaction(@Valid @RequestBody TransactionRequest request) {
        log.info("Received transaction request: {}", request);
        return transactionService.processTransaction(request)
                .map(TransactionResponse::fromDomain);
    }
}
