package com.enzoneyra.exchangeservice.infrastructure.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {
    private String type;

    @Positive(message = "Exchange rate must be positive")
    private BigDecimal amount;

    @Pattern(regexp = "^[A-Z]{3}$", message = "Invalid currency format")
    private String sourceCurrency;

    @Pattern(regexp = "^[A-Z]{3}$", message = "Invalid currency format")
    private String targetCurrency;
}
