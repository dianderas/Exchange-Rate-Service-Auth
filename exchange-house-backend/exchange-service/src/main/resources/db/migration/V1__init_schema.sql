CREATE TABLE exchange_rates (
    id UUID PRIMARY KEY,
    fx_rate_buy DECIMAL(10, 4) NOT NULL,
    fx_rate_sell DECIMAL(10, 4) NOT NULL,
    source_currency VARCHAR(3) NOT NULL,
    target_currency VARCHAR(3) NOT NULL,
    valid_until TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE OR REPLACE FUNCTION update_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER set_timestamp
    BEFORE UPDATE ON exchange_rates
    FOR EACH ROW
    EXECUTE FUNCTION update_timestamp();

INSERT INTO exchange_rates (id,
                            fx_rate_buy,
                            fx_rate_sell,
                            source_currency,
                            target_currency,
                            valid_until,
                            created_at,
                            updated_at)
VALUES ('00000000-0000-0000-0000-000000000001',
        3.8000,
        3.8500,
        'USD',
        'PEN',
        NOW() + INTERVAL '1 day',
        NOW(),
        NOW()
       );