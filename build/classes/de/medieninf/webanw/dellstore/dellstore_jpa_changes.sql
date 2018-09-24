-- in order to use JPA we need a version column in each table

ALTER TABLE categories ADD version int DEFAULT 1 NOT NULL;
ALTER TABLE cust_hist  ADD version int DEFAULT 1 NOT NULL;
ALTER TABLE customers  ADD version int DEFAULT 1 NOT NULL;
ALTER TABLE inventory  ADD version int DEFAULT 1 NOT NULL;
ALTER TABLE orderlines ADD version int DEFAULT 1 NOT NULL;
ALTER TABLE orders     ADD version int DEFAULT 1 NOT NULL;
ALTER TABLE products   ADD version int DEFAULT 1 NOT NULL;
