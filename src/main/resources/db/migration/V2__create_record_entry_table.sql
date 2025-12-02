CREATE EXTENSION IF NOT EXISTS "pgcrypto";
CREATE TABLE IF NOT EXISTS transaction_validator.record_entry (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    transaction_reference bigint UNIQUE NOT NULL,
    account_number varchar(40),
    start_balance numeric(15,2),
    mutation numeric(15,2),
    description varchar(50),
    end_balance numeric(15,2)
    );