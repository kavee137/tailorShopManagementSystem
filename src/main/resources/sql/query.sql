
DROP DATABASE tailorShop;

CREATE DATABASE tailorShop;

use tailorShop;

CREATE TABLE user (
    userID VARCHAR(20) PRIMARY KEY,
    userName VARCHAR(100) NOT NULL,
    userEmail VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    status VARCHAR(20) DEFAULT 'Active'
);

CREATE TABLE supplier (
    supplierID VARCHAR(20) PRIMARY KEY,
    NIC VARCHAR(20) UNIQUE,
    supplierName VARCHAR(100),
    supplierAddress VARCHAR(255),
    supplierContact VARCHAR(20),
    status VARCHAR(20) DEFAULT 'Active'
);

CREATE TABLE customer (
    customerID VARCHAR(20) PRIMARY KEY,
    NIC VARCHAR(50) NOT NULL UNIQUE,
    customerName VARCHAR(100) NOT NULL,
    customerAddress VARCHAR(255) NOT NULL,
    customerTel VARCHAR(20) NOT NULL,
    userID VARCHAR(20) NOT NULL,
    status VARCHAR(20) DEFAULT 'Active',
    FOREIGN KEY (userID) REFERENCES user(userID) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE payment (
    paymentID INT PRIMARY KEY AUTO_INCREMENT,
    paymentType VARCHAR(50),
    price DECIMAL(10, 2)
);

CREATE TABLE fabric (
    fabricID VARCHAR(20) PRIMARY KEY,
    supplierID VARCHAR(20),
    fabricName VARCHAR(100),
    fabricColor VARCHAR(50),
    fabricQtyOnHand INT,
    FOREIGN KEY (supplierID) REFERENCES supplier(supplierID) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE employee (
    employeeID VARCHAR(20) PRIMARY KEY,
    userID VARCHAR(20) DEFAULT 'U01',
    NIC VARCHAR(20),
    position VARCHAR(50),
    employeeName VARCHAR(100),
    phoneNumber VARCHAR(20),
    employeeAddress VARCHAR(255),
    salary DECIMAL (10, 2),
    status VARCHAR(50) DEFAULT 'Active',
    FOREIGN KEY (userID) REFERENCES user(userID) ON UPDATE CASCADE ON DELETE CASCADE
);


CREATE TABLE product (
    productID INT PRIMARY KEY AUTO_INCREMENT,
    productName VARCHAR(100),
    productColor VARCHAR(50),
    productSize VARCHAR(20),
    unitPrice DECIMAL(10, 2),
    qtyOnHand INT
);

CREATE TABLE reservation (
    reservationID INT PRIMARY KEY AUTO_INCREMENT,
    customerID VARCHAR(20),
    paymentID INT,
    reservationDate DATE,
    returnDate DATE,
    status VARCHAR(20) DEFAULT 'Incomplete', -- Adding default value
    FOREIGN KEY (customerID) REFERENCES customer(customerID) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (paymentID) REFERENCES payment(paymentID) ON UPDATE CASCADE ON DELETE CASCADE
);


CREATE TABLE reservationDetails (
    reservationID INT,
    productID INT,
    qty INT,
    total DECIMAL(10, 2),
    FOREIGN KEY (reservationID) REFERENCES reservation(reservationID) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (productID) REFERENCES product(productID) ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (reservationID, productID)
);

CREATE TABLE orders (
    orderID VARCHAR(20) PRIMARY KEY,
    customerID VARCHAR(20),
    paymentID INT,
    employeeID VARCHAR(20),
    orderDate DATE NOT NULL,
    returnDate DATE NOT NULL,
    status VARCHAR(20) DEFAULT 'processing', -- Adding default value
    FOREIGN KEY (customerID) REFERENCES customer(customerID) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (paymentID) REFERENCES payment(paymentID) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (employeeID) REFERENCES employee(employeeID) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE orderDetails (
    orderID VARCHAR(20),
    fabricID VARCHAR(20),
    description VARCHAR(255),
    measurements VARCHAR(255),
    fabricSize DECIMAL(10, 2),
    unitPrice DECIMAL(10, 2),
    qty INT,
    total DECIMAL(10, 2),
    FOREIGN KEY (orderID) REFERENCES orders(orderID) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (fabricID) REFERENCES fabric(fabricID) ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (orderID, fabricID)
);

show tables;





-- Inserting into user table
INSERT INTO user (userID, userName, userEmail,password) VALUES
('U01', 'kaveesha', 'kaveesha@example.com','kaveesha1234'),
('U02', 'rukshan', 'rukshan@example.com','rukshan1234'),
('U03', 'nimal', 'nimal@example.com','nimal1234'),
('U04', 'a', '@example.com','a');

-- Inserting into supplier table
INSERT INTO supplier (supplierID, NIC, supplierName, supplierAddress, supplierContact) VALUES
('S01', '230', 'Fernando', 'Galle', '0734567890'),
('S02', '231', 'Jayawardena', 'Matara', '0745678901'),
('S03', '232', 'Gunawardena', 'Anuradhapura', '0756789012'),
('S04', '233', 'Wickramasinghe', 'Polonnaruwa', '0767890123'),
('S05', '234', 'Dissanayake', 'Trincomalee', '0778901234');

-- Inserting into customer table
INSERT INTO customer (customerID, NIC, customerName, customerAddress, customerTel, userID) VALUES
('C001', '658373827V','Alice', 'Matugama', '0763452876', 'U01'),
('C002', '654321787V', 'Bobie', 'Rathnapura', '0752538465', 'U01'),
('C003', '786543980V', 'John', 'Baduraliya', '0785666654','U01'),
('C004', '876540989V', 'Smith', 'Galle', '0776767545','U01');


-- Inserting into payment table
INSERT INTO payment (paymentType, price) VALUES
('Cash', 3600.00),
('Cash', 1500.00),
('Cash', 1500.00);


-- Inserting into employee table
INSERT INTO employee (employeeID, NIC, position, employeeName, phoneNumber, employeeAddress, salary) VALUES
('E01', '200356789V', 'General Manager', 'Kaveesha', '0786578654', 'Colombo',45000.00),
('E02', '987635462V', 'Inventory Manager', 'Rukshan', '0705879156', 'Panadura',38000.00),
('E03', '738463883V', 'Cashier', 'Nimali', '0784536484', 'Galle', 25000.00),
('E04', '998763743V', 'Tailor', 'Kasun', '0715637776', 'Matugama', 29000.00),
('E05', '200036478V', 'Tailor', 'Kavindu', '0765647365', 'Agalawatta', 29000.00),
('E06', '200348472V', 'Tailor', 'Supun', '0784254242', 'Baduraliya', 29000.00),
('E07', '876265367V', 'Tailor', 'Nipun', '0702435675', 'Matugama', 29000.00),
('E08', '200363743V', 'Customer Representative', 'Thanuri', '0715637776', 'Matugama', 25000.00),
('E09', '996378463V', 'Customer Representative', 'Nethmi', '0778364856', 'Colombo', 25000.00),
('E10', '937463872V', 'Customer Representative', 'Damith', '0762837462', 'Galle', 25000.00);

-- Inserting into fabric table
INSERT INTO fabric (fabricID, supplierID, fabricName, fabricColor, fabricQtyOnHand) VALUES
('F01', 'S01', 'Cotton', 'Black', 100),
('F02', 'S01', 'Cotton', 'Red', 50),
('F03', 'S01', 'Cotton', 'Gray', 75),
('F04', 'S01', 'Cotton', 'White', 100),
('F05', 'S01', 'Cotton', 'Cream', 50),
('F06', 'S01', 'Cotton', 'Blue', 60),
('F07', 'S01', 'Silk', 'Black', 100),
('F08', 'S01', 'Silk', 'Red', 50),
('F09', 'S01', 'Silk', 'Gray', 75),
('F10', 'S01', 'Silk', 'White', 100),
('F11', 'S01', 'Silk', 'Cream', 50),
('F12', 'S01', 'Silk', 'Blue', 60),
('F13', 'S02', 'Wool', 'Black', 100),
('F14', 'S02', 'Wool', 'Red', 50),
('F15', 'S02', 'Wool', 'Gray', 75),
('F16', 'S02', 'Wool', 'White', 100),
('F17', 'S02', 'Wool', 'Cream', 50),
('F18', 'S02', 'Wool', 'Blue', 60),
('F19', 'S02', 'Linen', 'Black', 100),
('F20', 'S02', 'Linen', 'Red', 50),
('F21', 'S02', 'Linen', 'Gray', 75),
('F22', 'S02', 'Linen', 'White', 100),
('F23', 'S02', 'Linen', 'Cream', 50),
('F24', 'S02', 'Linen', 'Blue', 60),
('F25', 'S03', 'Velvet', 'Black', 100),
('F26', 'S03', 'Velvet', 'Red', 50),
('F27', 'S03', 'Velvet', 'Gray', 75),
('F28', 'S03', 'Velvet', 'White', 100),
('F29', 'S03', 'Velvet', 'Cream', 50),
('F30', 'S03', 'Velvet', 'Blue', 60),
('F31', 'S04', 'Denim', 'Black', 100),
('F32', 'S04', 'Denim', 'Red', 50),
('F33', 'S04', 'Denim', 'Gray', 75),
('F34', 'S04', 'Denim', 'White', 100),
('F35', 'S04', 'Denim', 'Cream', 50),
('F36', 'S04', 'Denim', 'Blue', 60),
('F37', 'S05', 'Polyester', 'Black', 100),
('F38', 'S05', 'Polyester', 'Red', 50),
('F39', 'S05', 'Polyester', 'Gray', 75),
('F40', 'S05', 'Polyester', 'White', 100),
('F41', 'S05', 'Polyester', 'Cream', 50),
('F42', 'S05', 'Polyester', 'Blue', 60);

-- Inserting into orders table
INSERT INTO orders (orderID, customerID, paymentID, employeeID, orderDate, returnDate, status) VALUES
('O001', 'C001', 1, 'E04', '2024-04-01', '2024-04-07', 'processing'); -- measurements Shirts - Shoulder, Length, Chest, Sleeve          measurements Trouser - size, Length, Thigh, Bottom



-- Inserting into orderDetails table
INSERT INTO orderDetails (orderID, fabricID, description, measurements, fabricSize, unitPrice, qty, total) VALUES
('O001', 'F04', 'Shirt', '13.5, 23.5, 34, 6', 2.3, 1800.00, 1, 1800.00),
('O001','F34', 'Shirt', '13, 20, 33, 2', 2, 1800.00, 1, 1800.00);


-- Inserting into product table
INSERT INTO product (productName, productColor, productSize, unitPrice, qtyOnHand) VALUES
('Blazer', 'Black', '20', 1500.00, 5),
('Blazer', 'Black', '22', 1500.00, 4),
('Blazer', 'Black', '24', 1500.00, 5),
('Blazer', 'Black', '26', 1500.00, 5),
('Blazer', 'Black', '28', 1500.00, 5),
('Blazer', 'Black', '30', 1500.00, 5),
('Blazer', 'Black', '32', 1500.00, 5),
('Blazer', 'Black', '34', 1500.00, 5),
('Blazer', 'Black', '36', 1500.00, 5),
('Blazer', 'Black', '38', 1500.00, 5),
('Blazer', 'Black', '40', 1500.00, 5),

('Blazer', 'Red', '20', 1500.00, 5),
('Blazer', 'Red', '22', 1500.00, 5),
('Blazer', 'Red', '24', 1500.00, 5),
('Blazer', 'Red', '26', 1500.00, 5),
('Blazer', 'Red', '28', 1500.00, 5),
('Blazer', 'Red', '30', 1500.00, 5),
('Blazer', 'Red', '32', 1500.00, 5),
('Blazer', 'Red', '34', 1500.00, 5),
('Blazer', 'Red', '36', 1500.00, 5),
('Blazer', 'Red', '38', 1500.00, 5),
('Blazer', 'Red', '40', 1500.00, 5),

('Blazer', 'Green', '20', 1500.00, 5),
('Blazer', 'Green', '22', 1500.00, 5),
('Blazer', 'Green', '24', 1500.00, 5),
('Blazer', 'Green', '26', 1500.00, 5),
('Blazer', 'Green', '28', 1500.00, 5),
('Blazer', 'Green', '30', 1500.00, 5),
('Blazer', 'Green', '32', 1500.00, 5),
('Blazer', 'Green', '34', 1500.00, 5),
('Blazer', 'Green', '36', 1500.00, 5),
('Blazer', 'Green', '38', 1500.00, 5),
('Blazer', 'Green', '40', 1500.00, 5),

('Blazer', 'Blue', '20', 1500.00, 5),
('Blazer', 'Blue', '22', 1500.00, 5),
('Blazer', 'Blue', '24', 1500.00, 5),
('Blazer', 'Blue', '26', 1500.00, 5),
('Blazer', 'Blue', '28', 1500.00, 5),
('Blazer', 'Blue', '30', 1500.00, 5),
('Blazer', 'Blue', '32', 1500.00, 5),
('Blazer', 'Blue', '34', 1500.00, 5),
('Blazer', 'Blue', '36', 1500.00, 5),
('Blazer', 'Blue', '38', 1500.00, 5),
('Blazer', 'Blue', '40', 1500.00, 5),

('Blazer', 'Cream', '20', 1500.00, 5),
('Blazer', 'Cream', '22', 1500.00, 5),
('Blazer', 'Cream', '24', 1500.00, 5),
('Blazer', 'Cream', '26', 1500.00, 5),
('Blazer', 'Cream', '28', 1500.00, 5),
('Blazer', 'Cream', '30', 1500.00, 5),
('Blazer', 'Cream', '32', 1500.00, 5),
('Blazer', 'Cream', '34', 1500.00, 5),
('Blazer', 'Cream', '36', 1500.00, 5),
('Blazer', 'Cream', '38', 1500.00, 5),
('Blazer', 'Cream', '40', 1500.00, 5),

('Blazer', 'Brown', '20', 1500.00, 5),
('Blazer', 'Brown', '22', 1500.00, 5),
('Blazer', 'Brown', '24', 1500.00, 5),
('Blazer', 'Brown', '26', 1500.00, 5),
('Blazer', 'Brown', '28', 1500.00, 5),
('Blazer', 'Brown', '30', 1500.00, 5),
('Blazer', 'Brown', '32', 1500.00, 5),
('Blazer', 'Brown', '34', 1500.00, 5),
('Blazer', 'Brown', '36', 1500.00, 5),
('Blazer', 'Brown', '38', 1500.00, 5),
('Blazer', 'Brown', '40', 1500.00, 5),

('Blazer', 'Gray', '20', 1500.00, 5),
('Blazer', 'Gray', '22', 1500.00, 5),
('Blazer', 'Gray', '24', 1500.00, 5),
('Blazer', 'Gray', '26', 1500.00, 5),
('Blazer', 'Gray', '28', 1500.00, 5),
('Blazer', 'Gray', '30', 1500.00, 5),
('Blazer', 'Gray', '32', 1500.00, 5),
('Blazer', 'Gray', '34', 1500.00, 5),
('Blazer', 'Gray', '36', 1500.00, 5),
('Blazer', 'Gray', '38', 1500.00, 5),
('Blazer', 'Gray', '40', 1500.00, 5),

('Blazer', 'Navy', '20', 1500.00, 5),
('Blazer', 'Navy', '22', 1500.00, 5),
('Blazer', 'Navy', '24', 1500.00, 5),
('Blazer', 'Navy', '26', 1500.00, 5),
('Blazer', 'Navy', '28', 1500.00, 5),
('Blazer', 'Navy', '30', 1500.00, 5),
('Blazer', 'Navy', '32', 1500.00, 5),
('Blazer', 'Navy', '34', 1500.00, 5),
('Blazer', 'Navy', '36', 1500.00, 5),
('Blazer', 'Navy', '38', 1500.00, 5),
('Blazer', 'Navy', '40', 1500.00, 5),

('Trouser', 'Black', '20', 1500.00, 5),
('Trouser', 'Black', '22', 1500.00, 5),
('Trouser', 'Black', '24', 1500.00, 5),
('Trouser', 'Black', '26', 1500.00, 5),
('Trouser', 'Black', '28', 1500.00, 5),
('Trouser', 'Black', '30', 1500.00, 5),
('Trouser', 'Black', '32', 1500.00, 5),
('Trouser', 'Black', '34', 1500.00, 5),
('Trouser', 'Black', '36', 1500.00, 5),
('Trouser', 'Black', '38', 1500.00, 5),
('Trouser', 'Black', '40', 1500.00, 5),

('Trouser', 'Red', '20', 1500.00, 5),
('Trouser', 'Red', '22', 1500.00, 5),
('Trouser', 'Red', '24', 1500.00, 5),
('Trouser', 'Red', '26', 1500.00, 5),
('Trouser', 'Red', '28', 1500.00, 5),
('Trouser', 'Red', '30', 1500.00, 5),
('Trouser', 'Red', '32', 1500.00, 5),
('Trouser', 'Red', '34', 1500.00, 5),
('Trouser', 'Red', '36', 1500.00, 5),
('Trouser', 'Red', '38', 1500.00, 5),
('Trouser', 'Red', '40', 1500.00, 5),

('Trouser', 'Green', '20', 1500.00, 5),
('Trouser', 'Green', '22', 1500.00, 5),
('Trouser', 'Green', '24', 1500.00, 5),
('Trouser', 'Green', '26', 1500.00, 5),
('Trouser', 'Green', '28', 1500.00, 5),
('Trouser', 'Green', '30', 1500.00, 5),
('Trouser', 'Green', '32', 1500.00, 5),
('Trouser', 'Green', '34', 1500.00, 5),
('Trouser', 'Green', '36', 1500.00, 5),
('Trouser', 'Green', '38', 1500.00, 5),
('Trouser', 'Green', '40', 1500.00, 5),

('Trouser', 'Blue', '20', 1500.00, 5),
('Trouser', 'Blue', '22', 1500.00, 5),
('Trouser', 'Blue', '24', 1500.00, 5),
('Trouser', 'Blue', '26', 1500.00, 5),
('Trouser', 'Blue', '28', 1500.00, 5),
('Trouser', 'Blue', '30', 1500.00, 5),
('Trouser', 'Blue', '32', 1500.00, 5),
('Trouser', 'Blue', '34', 1500.00, 5),
('Trouser', 'Blue', '36', 1500.00, 5),
('Trouser', 'Blue', '38', 1500.00, 5),
('Trouser', 'Blue', '40', 1500.00, 5),

('Trouser', 'Cream', '20', 1500.00, 5),
('Trouser', 'Cream', '22', 1500.00, 5),
('Trouser', 'Cream', '24', 1500.00, 5),
('Trouser', 'Cream', '26', 1500.00, 5),
('Trouser', 'Cream', '28', 1500.00, 5),
('Trouser', 'Cream', '30', 1500.00, 5),
('Trouser', 'Cream', '32', 1500.00, 5),
('Trouser', 'Cream', '34', 1500.00, 5),
('Trouser', 'Cream', '36', 1500.00, 5),
('Trouser', 'Cream', '38', 1500.00, 5),
('Trouser', 'Cream', '40', 1500.00, 5),

('Trouser', 'Brown', '20', 1500.00, 5),
('Trouser', 'Brown', '22', 1500.00, 5),
('Trouser', 'Brown', '24', 1500.00, 5),
('Trouser', 'Brown', '26', 1500.00, 5),
('Trouser', 'Brown', '28', 1500.00, 5),
('Trouser', 'Brown', '30', 1500.00, 5),
('Trouser', 'Brown', '32', 1500.00, 5),
('Trouser', 'Brown', '34', 1500.00, 5),
('Trouser', 'Brown', '36', 1500.00, 5),
('Trouser', 'Brown', '38', 1500.00, 5),
('Trouser', 'Brown', '40', 1500.00, 5),

('Trouser', 'Gray', '20', 1500.00, 5),
('Trouser', 'Gray', '22', 1500.00, 5),
('Trouser', 'Gray', '24', 1500.00, 5),
('Trouser', 'Gray', '26', 1500.00, 5),
('Trouser', 'Gray', '28', 1500.00, 5),
('Trouser', 'Gray', '30', 1500.00, 5),
('Trouser', 'Gray', '32', 1500.00, 5),
('Trouser', 'Gray', '34', 1500.00, 5),
('Trouser', 'Gray', '36', 1500.00, 5),
('Trouser', 'Gray', '38', 1500.00, 5),
('Trouser', 'Gray', '40', 1500.00, 5),

('Trouser', 'Navy', '20', 1500.00, 5),
('Trouser', 'Navy', '22', 1500.00, 5),
('Trouser', 'Navy', '24', 1500.00, 5),
('Trouser', 'Navy', '26', 1500.00, 5),
('Trouser', 'Navy', '28', 1500.00, 5),
('Trouser', 'Navy', '30', 1500.00, 5),
('Trouser', 'Navy', '32', 1500.00, 5),
('Trouser', 'Navy', '34', 1500.00, 5),
('Trouser', 'Navy', '36', 1500.00, 5),
('Trouser', 'Navy', '38', 1500.00, 5),
('Trouser', 'Navy', '40', 1500.00, 5);


-- Inserting into reservation table
INSERT INTO reservation (customerID, paymentID, reservationDate, returnDate, status) VALUES
('C003', 2, '2024-04-07', '2024-04-09', 'Incomplete'),
('C004', 3, '2024-04-08', '2024-04-10', 'Incomplete');


-- Inserting into reservationDetails table
INSERT INTO reservationDetails (reservationID, productID, qty) VALUES
(1, 3, 1),
(2, 5, 1);


SELECT * FROM user;
SELECT * FROM supplier;
SELECT * FROM customer;
SELECT * FROM payment;
SELECT * FROM fabric;
SELECT * FROM employee;
SELECT * FROM product;
SELECT * FROM reservation;
SELECT * FROM reservationDetails;
SELECT * FROM orders;
SELECT * FROM orderDetails;


