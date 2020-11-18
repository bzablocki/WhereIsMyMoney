DROP TABLE categories CASCADE;
CREATE TABLE IF NOT EXISTS categories (
        id serial PRIMARY KEY UNIQUE NOT NULL,
        name VARCHAR(255)
    );

DROP TABLE patterns CASCADE;
CREATE TABLE IF NOT EXISTS patterns (
        id serial PRIMARY KEY UNIQUE NOT NULL,
		pattern VARCHAR(255),
        category_id bigint NOT NULL,
		FOREIGN KEY (category_id) REFERENCES categories(id) ON UPDATE CASCADE
    );


DROP TABLE user_pattern;
CREATE TABLE IF NOT EXISTS user_pattern (
        id serial PRIMARY KEY UNIQUE NOT NULL,
        user_id bigint NOT NULL, 
		pattern_id bigint NOT NULL,
        
        FOREIGN KEY (user_id) REFERENCES users(id) ON UPDATE CASCADE,
        FOREIGN KEY (pattern_id) REFERENCES patterns(id) ON UPDATE CASCADE ON DELETE CASCADE
    )
	
DROP TABLE transaction_pattern;
CREATE TABLE IF NOT EXISTS transaction_pattern (
        id serial PRIMARY KEY UNIQUE NOT NULL,
        transaction_id bigint NOT NULL, 
		pattern_id bigint NOT NULL,
        
        FOREIGN KEY (transaction_id) REFERENCES transactions(id) ON UPDATE CASCADE ON DELETE CASCADE,
        FOREIGN KEY (pattern_id) REFERENCES patterns(id) ON UPDATE CASCADE ON DELETE CASCADE
    )

