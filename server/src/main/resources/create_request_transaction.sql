DROP TABLE request_transaction;
CREATE TABLE IF NOT EXISTS request_transaction (
        request_id bigint NOT NULL,
        transaction_id bigint NOT NULL,
        FOREIGN KEY (request_id) REFERENCES transactions(id) ON UPDATE CASCADE,
        FOREIGN KEY (transaction_id) REFERENCES transactions(id) ON UPDATE CASCADE
    )
