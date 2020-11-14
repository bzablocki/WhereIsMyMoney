DROP TABLE transactions;
CREATE TABLE IF NOT EXISTS transactions (
        id serial PRIMARY KEY UNIQUE NOT NULL,
        user_id bigint NOT NULL,
        reserved_date date NOT NULL,
        name VARCHAR(255),
        description  VARCHAR(255),
        card_sequence_no  VARCHAR(255),
        transaction_field  VARCHAR(255),
        iban  VARCHAR(255),
        reference  VARCHAR(255),
        date_time  VARCHAR(255),
        value_date  VARCHAR(255),
        type  VARCHAR(255),
        amount NUMERIC(9,2),
		reservation bool default false,
		request bool default false,
        FOREIGN KEY (user_id) REFERENCES users(id) ON UPDATE CASCADE,
 		unique(user_id, reserved_date, name, amount)
    )
