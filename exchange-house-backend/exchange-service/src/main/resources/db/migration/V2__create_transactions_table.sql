CREATE TABLE transactions (
    id UUID PRIMARY KEY,
    type VARCHAR(10) NOT NULL CHECK (type IN ('BUY', 'SELL')),
    amount NUMERIC(18,2) NOT NULL CHECK (amount > 0),
    source_currency VARCHAR(3) NOT NULL CHECK (source_currency IN ('USD', 'PEN')),
    target_currency VARCHAR(3) NOT NULL CHECK (target_currency IN ('USD', 'PEN')),
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);