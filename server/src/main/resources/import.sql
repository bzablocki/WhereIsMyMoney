-- DROP TABLE public.users CASCADE;
-- DROP TABLE public.authority CASCADE;
-- DROP TABLE public.user_authority CASCADE;

-- -- Table: users
-- -- CREATE TABLE IF NOT EXISTS users (
--         id serial PRIMARY KEY,
--         username VARCHAR(100) UNIQUE NOT NULL,
--         password VARCHAR(355) NOT NULL,
--         firstname VARCHAR(355) NOT NULL,
--         lastname VARCHAR(355) NOT NULL,
--         created_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
--         updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
--     )
--
-- TABLESPACE pg_default;
-- ALTER TABLE users OWNER TO postgres;
--
-- -- Table: authority
-- CREATE TABLE IF NOT EXISTS authority (
--         id serial PRIMARY KEY,
--         name VARCHAR(100) UNIQUE NOT NULL,
--         created_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
--         updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
--     )
--
-- TABLESPACE pg_default;
-- ALTER TABLE authority OWNER TO postgres;
--
--
-- -- Table: user_authority
--
-- CREATE TABLE IF NOT EXISTS user_authority (
--         user_id int NOT NULL,
--         authority_id int NOT NULL,
--         PRIMARY KEY (user_id, authority_id),
--         FOREIGN KEY (user_id) REFERENCES users(id) ON UPDATE CASCADE,
--         FOREIGN KEY (authority_id) REFERENCES authority(id) ON UPDATE CASCADE
--     )
--
-- TABLESPACE pg_default;
-- ALTER TABLE user_authority OWNER TO postgres;

-- the password hash is generated by BCrypt Calculator Generator(https://www.dailycred.com/article/bcrypt-calculator)
-- INSERT INTO users (id, username, password, firstname, lastname) VALUES (1, 'user', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', 'Fan', 'Jin');
-- INSERT INTO users (id, username, password, firstname, lastname) VALUES (2, 'admin', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', 'Jing', 'Xiao');
-- INSERT INTO users (username, password, firstname, lastname) VALUES ('user2', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', 'Jing', 'Xiao');
--
-- INSERT INTO authority (id, name) VALUES (1, 'ROLE_USER');
-- INSERT INTO authority (id, name) VALUES (2, 'ROLE_ADMIN');
--
-- INSERT INTO user_authority (user_id, authority_id) VALUES (1, 1);
-- INSERT INTO user_authority (user_id, authority_id) VALUES (2, 1);
-- INSERT INTO user_authority (user_id, authority_id) VALUES (2, 2);
-- INSERT INTO user_authority (user_id, authority_id) VALUES (3, 1);

-- DROP TABLE transactions;
-- CREATE TABLE IF NOT EXISTS transactions (
--         id serial PRIMARY KEY UNIQUE NOT NULL,
--         user_id bigint NOT NULL,
--         reserved_date date NOT NULL,
--         name VARCHAR(255),
--         description  VARCHAR(255),
--         card_sequence_no  VARCHAR(255),
--         transaction_field  VARCHAR(255),
--         iban  VARCHAR(255),
--         reference  VARCHAR(255),
--         date_time  VARCHAR(255),
--         value_date  VARCHAR(255),
--         type  VARCHAR(255),
--         amount NUMERIC(9,2),
-- 		reservation bool default false,
-- 		request bool default false,
--         FOREIGN KEY (user_id) REFERENCES users(id) ON UPDATE CASCADE,
-- 		unique(user_id, reserved_date, name, amount)
--     )
--
--     SELECT (user_name) from USER

-- INSERT INTO transactions (id, user_id, reserved_date, name) VALUES (null, 3, '2020-10-26', 'Name: Malwina Anna Witczak ')