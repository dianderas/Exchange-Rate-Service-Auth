package com.enzoneyra.exchangeservice.service;

import com.enzoneyra.exchangeservice.domain.model.Currency;
import com.enzoneyra.exchangeservice.domain.model.ExchangeRate;
import com.enzoneyra.exchangeservice.domain.model.Transaction;
import com.enzoneyra.exchangeservice.domain.model.TransactionType;
import com.enzoneyra.exchangeservice.domain.repository.ExchangeRateRepository;
import com.enzoneyra.exchangeservice.domain.repository.TransactionRepository;
import com.enzoneyra.exchangeservice.infrastructure.dto.TransactionRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private ExchangeRateRepository exchangeRateRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void processTransaction_ShouldCreateAndSaveTransaction_WhenValidRequest() {
        // Arrange
        TransactionRequest request = new TransactionRequest("BUY", new BigDecimal("100"), "USD", "PEN" );

        Currency sourceCurrency = Currency.USD;
        Currency targetCurrency = Currency.PEN;
        TransactionType transactionType = TransactionType.BUY;
        BigDecimal fxRateBuy = new BigDecimal("3.80");
        BigDecimal expectedAmount = request.getAmount().multiply(fxRateBuy);

        ExchangeRate exchangeRate = ExchangeRate.builder()
                .fxRateBuy(fxRateBuy)
                .fxRateSell(new BigDecimal("3.85"))
                .build();

        when(exchangeRateRepository.findLatestRate(sourceCurrency, targetCurrency))
                .thenReturn(Mono.just(exchangeRate));
        when(transactionRepository.save(any(Transaction.class)))
                .thenReturn(Mono.empty());

        // Act
        StepVerifier.create(transactionService.processTransaction(request))
                .expectNextMatches(transaction ->
                        transaction.getType() == transactionType &&
                                transaction.getSourceCurrency() == sourceCurrency &&
                                transaction.getTargetCurrency() == targetCurrency &&
                                transaction.getAmount().compareTo(expectedAmount) == 0)
                .verifyComplete();

        // Assert
        verify(exchangeRateRepository).findLatestRate(sourceCurrency, targetCurrency);

        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(captor.capture());

        Transaction savedTransaction = captor.getValue();
        assertEquals(sourceCurrency, savedTransaction.getSourceCurrency());
        assertEquals(targetCurrency, savedTransaction.getTargetCurrency());
        assertEquals(expectedAmount, savedTransaction.getAmount());
        assertEquals(transactionType, savedTransaction.getType());
    }

    @Test
    void processTransaction_ShouldReturnError_WhenExchangeRateNotFound() {
        // Arrange
        TransactionRequest request = new TransactionRequest("SELL", new BigDecimal("200"), "USD", "PEN");
        when(exchangeRateRepository.findLatestRate(any(), any()))
                .thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(transactionService.processTransaction(request))
                .expectErrorMatches(e -> e instanceof RuntimeException && e.getMessage().contains("Exchange rate not found"))
                .verify();

        verify(exchangeRateRepository).findLatestRate(any(), any());
        verifyNoInteractions(transactionRepository);
    }

    @Test
    void processTransaction_ShouldReturnError_WhenTransactionSaveFails() {
        // Arrange
        TransactionRequest request = new TransactionRequest("BUY", new BigDecimal("350"), "USD", "PEN");

        Currency sourceCurrency = Currency.USD;
        Currency targetCurrency = Currency.PEN;

        ExchangeRate exchangeRate = ExchangeRate.builder()
                .fxRateBuy(new BigDecimal("3.80"))
                .fxRateSell(new BigDecimal("3.85"))
                .build();

        when(exchangeRateRepository.findLatestRate(sourceCurrency, targetCurrency))
                .thenReturn(Mono.just(exchangeRate));

        when(transactionRepository.save(any(Transaction.class)))
                .thenReturn(Mono.error(new RuntimeException("Database save error")));

        // Act & Assert
        StepVerifier.create(transactionService.processTransaction(request))
                .expectErrorMatches(e -> e instanceof RuntimeException && e.getMessage().contains("Database save error"))
                .verify();

        verify(exchangeRateRepository).findLatestRate(sourceCurrency, targetCurrency);
        verify(transactionRepository).save(any(Transaction.class));
    }
}
