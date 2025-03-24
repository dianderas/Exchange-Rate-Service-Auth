package com.enzoneyra.exchangeservice.service;

import com.enzoneyra.exchangeservice.domain.model.Currency;
import com.enzoneyra.exchangeservice.domain.model.ExchangeRate;
import com.enzoneyra.exchangeservice.domain.repository.ExchangeRateRepository;
import com.enzoneyra.exchangeservice.infrastructure.dto.ExchangeRateRequest;
import com.enzoneyra.exchangeservice.infrastructure.exception.DatabaseException;
import com.enzoneyra.exchangeservice.infrastructure.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExchangeRateServiceTest {

    @Mock
    private ExchangeRateRepository exchangeRateRepository;

    @InjectMocks
    private ExchangeRateService exchangeRateService;

    private final Currency sourceCurrency = Currency.USD;
    private final Currency targetCurrency = Currency.PEN;
    private final BigDecimal fxRateBuy = new BigDecimal("3.80");
    private final BigDecimal fxRateSell = new BigDecimal("3.85");

    @Test
    void getLatestExchangeRate_ShouldReturnExchangeRate_WhenExists() {
        // Arrange
        ExchangeRate expectedRate = ExchangeRate.builder()
                .id(UUID.randomUUID())
                .sourceCurrency(sourceCurrency)
                .targetCurrency(targetCurrency)
                .fxRateBuy(fxRateBuy)
                .fxRateSell(fxRateSell)
                .validUntil(Instant.now().plusSeconds(3600))
                .createdAt(Instant.now())
                .build();

        when(exchangeRateRepository.findLatestRate(sourceCurrency, targetCurrency))
                .thenReturn(Mono.just(expectedRate));

        // Act
        StepVerifier.create(exchangeRateService.getLatestExchangeRate(sourceCurrency, targetCurrency))
                .expectNext(expectedRate)
                .verifyComplete();

        // Assert
        verify(exchangeRateRepository).findLatestRate(sourceCurrency, targetCurrency);
    }

    @Test
    void getLatestExchangeRate_ShouldThrowNotFoundException_WhenNoRateExists() {
        // Arrange
        when(exchangeRateRepository.findLatestRate(sourceCurrency, targetCurrency))
                .thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(exchangeRateService.getLatestExchangeRate(sourceCurrency, targetCurrency))
                .expectErrorMatches(e -> e instanceof NotFoundException && e.getMessage().contains("Exchange rate not found"))
                .verify();

        verify(exchangeRateRepository).findLatestRate(sourceCurrency, targetCurrency);
    }

    @Test
    void saveExchangeRate_ShouldSaveSuccessfully() {
        // Arrange
        ExchangeRateRequest request = new ExchangeRateRequest("USD", "PEN", fxRateBuy, fxRateSell, 60);

        when(exchangeRateRepository.save(any(ExchangeRate.class)))
                .thenReturn(Mono.empty());

        // Act
        StepVerifier.create(exchangeRateService.saveExchangeRate(request))
                .verifyComplete();

        // Assert
        ArgumentCaptor<ExchangeRate> captor = ArgumentCaptor.forClass(ExchangeRate.class);
        verify(exchangeRateRepository).save(captor.capture());

        ExchangeRate savedRate = captor.getValue();
        assertEquals(Currency.USD, savedRate.getSourceCurrency());
        assertEquals(Currency.PEN, savedRate.getTargetCurrency());
        assertEquals(fxRateBuy, savedRate.getFxRateBuy());
        assertEquals(fxRateSell, savedRate.getFxRateSell());
    }

    @Test
    void saveExchangeRate_ShouldThrowDatabaseException_WhenSaveFails() {
        // Arrange
        ExchangeRateRequest request = new ExchangeRateRequest("USD", "PEN", fxRateBuy, fxRateSell, 60);

        when(exchangeRateRepository.save(any(ExchangeRate.class)))
                .thenReturn(Mono.error(new RuntimeException("Database error")));

        // Act & Assert
        StepVerifier.create(exchangeRateService.saveExchangeRate(request))
                .expectErrorMatches(e -> e instanceof DatabaseException && e.getMessage().contains("Error saving exchange rate"))
                .verify();

        verify(exchangeRateRepository).save(any(ExchangeRate.class));
    }
}