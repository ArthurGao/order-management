USE test;

CREATE TABLE IF NOT EXISTS orders
(
    id               varchar(255) NOT NULL,
    date             TIMESTAMP,
    amount           DECIMAL(20, 3),
    currency_code    varchar(3),
    transaction_type varchar(10),
    PRIMARY KEY (id)
);
