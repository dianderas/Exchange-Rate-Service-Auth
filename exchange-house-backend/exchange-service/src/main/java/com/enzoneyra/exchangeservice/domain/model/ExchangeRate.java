package com.enzoneyra.exchangeservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("exchange_rates")
public class ExchangeRate {
    @Id
    private UUID id;

    @Column("fx_rate_buy")
    private BigDecimal fxRateBuy;

    @Column("fx_rate_sell")
    private BigDecimal fxRateSell;

    @Column("source_currency")
    private Currency sourceCurrency;

    @Column("target_currency")
    private Currency targetCurrency;

    @Column("valid_until")
    private Instant validUntil;

    @Column("created_at")
    private Instant createdAt;

    @Column("updated_at")
    private Instant updatedAt;
}
