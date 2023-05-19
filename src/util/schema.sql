DROP DATABASE IF EXISTS thogakade_layered;
CREATE DATABASE thogakade_layered;
USE thogakade_layered;

CREATE TABLE Customer
(
    customerId VARCHAR(10),
    name       VARCHAR(50)  NOT NULL,
    address    VARCHAR(100) NOT NULL,
    CONSTRAINT PRIMARY KEY (customerId)
);

CREATE TABLE Item
(
    itemCode    VARCHAR(10),
    description VARCHAR(150),
    qtyOnHand   INT,
    unitPrice   DECIMAL(10, 2) NOT NULL,
    CONSTRAINT PRIMARY KEY (itemCode)
);

CREATE TABLE Orders
(
    orderId    VARCHAR(10),
    customerId VARCHAR(10),
    date       DATE NOT NULL,
    CONSTRAINT PRIMARY KEY (orderId),
    CONSTRAINT FOREIGN KEY (customerId) REFERENCES customer (customerId) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE orderDetails
(
    orderId   VARCHAR(10),
    itemCode  VARCHAR(10),
    qty       INT NOT NULL,
    unitPrice DECIMAL(10, 2),
    CONSTRAINT PRIMARY KEY (orderId, itemCode),
    CONSTRAINT FOREIGN KEY (orderId) REFERENCES orders (orderId) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FOREIGN KEY (itemCode) REFERENCES item (itemCode) ON DELETE CASCADE ON UPDATE CASCADE
);


