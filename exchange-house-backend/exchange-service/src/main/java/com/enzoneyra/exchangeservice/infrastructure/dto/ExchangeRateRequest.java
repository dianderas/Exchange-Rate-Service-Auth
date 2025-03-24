package com.enzoneyra.exchangeservice.infrastructure.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateRequest {
    @Pattern(regexp = "^[A-Z]{3}$", message = "Invalid currency format")
    private String sourceCurrency;

    @Pattern(regexp = "^[A-Z]{3}$", message = "Invalid currency format")
    private String targetCurrency;

    @Positive(message = "Exchange rate must be positive")
    private BigDecimal fxRateBuy;

    @Positive(message = "Exchange rate must be positive")
    private BigDecimal fxRateSell;

    @Min(value = 1, message = "Duration must be at least 1 minute")
    private long durationMinutes;
}
