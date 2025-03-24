package com.enzoneyra.exchangeservice.infrastructure.dto;

import com.enzoneyra.exchangeservice.domain.model.Currency;
import com.enzoneyra.exchangeservice.domain.model.Transaction;
import com.enzoneyra.exchangeservice.domain.model.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionResponse {
    private BigDecimal amountTarget;
    private Currency targetCurrency;
    private TransactionType type;

    public static TransactionResponse fromDomain(Transaction transaction) {
        return TransactionResponse.builder()
                .amountTarget(transaction.getAmount())
                .targetCurrency(transaction.getTargetCurrency())
                .type(transaction.getType())
                .build();
    }
}
