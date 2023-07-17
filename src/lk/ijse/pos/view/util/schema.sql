DROP DATABASE IF EXISTS fruitlab;
CREATE DATABASE fruitlab;
USE fruitlab;

CREATE TABLE customer
(
    id      VARCHAR(10),
    name    VARCHAR(50) NOT NULL,
    address VARCHAR(50) NOT NULL,
    CONSTRAINT PRIMARY KEY (id)
);

CREATE TABLE item
(
    code        VARCHAR(10),
    description VARCHAR(100),
    qtyOnHand   INT     NOT NULL,
    unitPrice   DECIMAL NOT NULL,
    CONSTRAINT PRIMARY KEY (code)
);

CREATE TABLE orders
(
    orderId    VARCHAR(10),
    customerId VARCHAR(10) NOT NULL,
    date     DATE,
    CONSTRAINT PRIMARY KEY (orderId),
    CONSTRAINT FOREIGN KEY (customerId) REFERENCES customer (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE order_details
(
    orderId  VARCHAR(10) NOT NULL,
    itemCode VARCHAR(10) NOT NULL,
    qty      INT        NOT NULL,
    unitPrice    DECIMAL,
    CONSTRAINT PRIMARY KEY (orderId, itemCode),
    CONSTRAINT FOREIGN KEY (orderId) REFERENCES orders (orderId) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FOREIGN KEY (itemCode) REFERENCES item (code) ON DELETE CASCADE ON UPDATE CASCADE
);
