INSERT INTO transaction_validator.record_entry (id, transaction_reference, account_number, start_balance, mutation, description,
                            end_balance)
VALUES ('02021dbf-975e-4b0d-83df-9ca4ef10bfa2', 1234, 'NL91ABNA0417164300', 100.00, 20.00, 'Test transaction 1',
        120.00),
    ('12021dbf-975e-4b0d-83df-9ca4ef10bfa2', 1235, 'NL27SNSB0917829871', 200.00, -50.00, 'Test transaction 2',
        150.00) ON CONFLICT (transaction_reference) DO NOTHING;