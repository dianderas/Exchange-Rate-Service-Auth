package com.enzoneyra.exchangeservice.infrastructure.dto;

import com.enzoneyra.exchangeservice.domain.model.Currency;
import com.enzoneyra.exchangeservice.domain.model.ExchangeRate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExchangeRateResponse {
    private BigDecimal fxRateBuy;
    private BigDecimal fxRateSell;
    private Currency sourceCurrency;
    private Currency targetCurrency;
    private long validUntil;

    public static ExchangeRateResponse fromDomain(ExchangeRate exchangeRate) {
        return ExchangeRateResponse.builder()
                .sourceCurrency(exchangeRate.getSourceCurrency())
                .targetCurrency(exchangeRate.getTargetCurrency())
                .fxRateBuy(exchangeRate.getFxRateBuy())
                .fxRateSell(exchangeRate.getFxRateSell())
                .validUntil(exchangeRate.getValidUntil().getEpochSecond())
                .build();
    }
}
