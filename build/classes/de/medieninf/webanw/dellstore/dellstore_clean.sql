-- remove all tables in reverse order of creation to ensure that removal 
-- does not fail because of dependencies

DROP TABLE reorder;
DROP TABLE orderlines;
DROP TABLE orders;
DROP TABLE products;
DROP TABLE inventory;
DROP TABLE cust_hist;
DROP TABLE customers;
DROP TABLE categories;
