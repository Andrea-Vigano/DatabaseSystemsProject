-- Create table Admin
CREATE TABLE Admin (
    adminID VARCHAR2(10) PRIMARY KEY,
    username VARCHAR2(255),
    passwordHash VARCHAR2(255),
    email VARCHAR2(255),
    phoneNumber VARCHAR2(20)
);

-- Create table Category
CREATE TABLE Category (
    categoryID VARCHAR2(10) PRIMARY KEY,
    name VARCHAR2(255)
);

-- Create table Users
CREATE TABLE Users (
    userID VARCHAR2(10) PRIMARY KEY,
    name VARCHAR2(255),
    username VARCHAR2(255),
    passwordHash VARCHAR2(255),
    email VARCHAR2(255),
    address VARCHAR2(255),
    phoneNumber VARCHAR2(20),
    CONSTRAINT FK_ADDUSE FOREIGN KEY (userID) REFERENCES Users(userID)
);

-- Create table Address
CREATE TABLE Address(
    addressID VARCHAR2(10) PRIMARY KEY,
    userID VARCHAR2(10),
    name VARCHAR2(255),
    CONSTRAINT FK_ADDUSE FOREIGN KEY (userID) REFERENCES Users(userID)
);

-- Create table Product
CREATE TABLE Product (
    productID VARCHAR2(10) PRIMARY KEY,
    name VARCHAR2(255),
    description CLOB,
    price NUMBER(10, 2),
    brand VARCHAR2(255),
    quantity NUMBER,
    supplier VARCHAR2(255),
    warehouse VARCHAR2(255),
    review NUMBER(10, 2),
    categoryID VARCHAR2(10),
    adminID VARCHAR2(10),
    CONSTRAINT FK_PROCAT FOREIGN KEY (categoryID) REFERENCES Category(categoryID),
    CONSTRAINT FK_PROADM FOREIGN KEY (adminID) REFERENCES Admin(adminID)
);

-- Create table ShoppingCart
CREATE TABLE ShoppingCart (
    cartID VARCHAR2(10) PRIMARY KEY,
    quantity NUMBER,
    price NUMBER(10, 2),
    dateAdded DATE,
    userID VARCHAR2(10),
    productID VARCHAR2(10),
    CONSTRAINT FK_SHOUSE FOREIGN KEY (userID) REFERENCES Users(userID),
    CONSTRAINT FK_SHOPRO FOREIGN KEY (productID) REFERENCES Product(productID)
);

-- Create table Orders
CREATE TABLE Orders (
    orderID VARCHAR2(20) PRIMARY KEY,
    dateCreated DATE,
    totalPrice NUMBER(10, 2),
    warehouse VARCHAR2(255),
    userID VARCHAR2(10),
    addressID VARCHAR2(10),
    CONSTRAINT FK_ORDUSE FOREIGN KEY (userID) REFERENCES Users(userID),
    CONSTRAINT FK_ORDADD FOREIGN KEY (addressID) REFERENCES Address(addressID)
);

-- Create table OrderLineItems
CREATE TABLE OrderLineItems (
    orderLineItemID VARCHAR2(10) PRIMARY KEY,
    quantity NUMBER,
    productID VARCHAR2(10),
    orderID VARCHAR2(10),
    price NUMBER(10, 2),
    CONSTRAINT FK_OLIPRO FOREIGN KEY (productID) REFERENCES Product(productID),
    CONSTRAINT FK_OLIORD FOREIGN KEY (orderID) REFERENCES Orders(orderID)
);

-- Create table Report
CREATE TABLE Report (
    reportID VARCHAR2(10) PRIMARY KEY,
    name VARCHAR2(255),
    revenue NUMBER(10, 2),
    sales NUMBER,
    adminID VARCHAR2(10)
);

-- Create table Promotion
CREATE TABLE Promotion (
    startDate DATE,
    endDate DATE,
    discountPercentage NUMBER,
    description VARCHAR2(255),
    productID VARCHAR2(10),
    CONSTRAINT FK_PROMPRO FOREIGN KEY (productID) REFERENCES Product(productID)
);

-- Insert into Product table
INSERT INTO Product (productID, name, description, price, brand, quantity, supplier, warehouse, review, categoryID, adminID)
VALUES ('1', 'Air Force shoes', 'Nike Air Force shoes', 450.00, 'Nike', 987, 'Nike', 'Hong Kong', 4.65, '1', '1');

INSERT INTO Product (productID, name, description, price, brand, quantity, supplier, warehouse, review, categoryID, adminID)
VALUES ('2', 'New Balance 550 shoes', 'New Balance 550 shoes', 577.00, 'New Balance', 1297, 'New Balance', 'Shanghai', 4.32, '1', '1');

INSERT INTO Product (productID, name, description, price, brand, quantity, supplier, warehouse, review, categoryID, adminID)
VALUES ('5', 'Li-Ning Shoes', 'Li-Ning Shoes', 299.00, 'HunG him', 20, 'Feng', 'Ruzhe supplier', 4.70, '1', '1');

INSERT INTO Product (productID, name, description, price, brand, quantity, supplier, warehouse, review, categoryID, adminID)
VALUES ('3', 'Nike Dunks 50s Shoes', 'Nike Dunks 50s Shoes', 299.00, 'Nike', 19, 'Angad', 'Singh Suppliers', 4.90, '1', '1');

-- Insert into Address table
INSERT INTO Address (addressID, userID, name)
VALUES ('2', '2', 'street2');

INSERT INTO Address (addressID, userID, name)
VALUES ('4', '5', 'Ho Man Tin');

INSERT INTO Address (addressID, userID, name)
VALUES ('3', '1', 'street1');	

-- Insert into Users table
--Commented Users since we use Password Hashes for security , and can't share passwords in plaintext, recommend using signup functionality for testing 

--INSERT INTO Users (userID, name, username, passwordHash, email, phoneNumber)
--VALUES ('4', 'Ilyas', 'Ilyas', '2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824', 'ilyas28@gmail.com', '22337788');

--INSERT INTO Users (userID, name, username, passwordHash, email, phoneNumber)
--VALUES ('6', 'xhen', '2345', '38083c7ee9121e17401883566a148aa5c2e2d55dc53bc4a94a026517dbff3c6b', '2345@gmail.com', '2345678900');

--INSERT INTO Users (userID, name, username, passwordHash, email, phoneNumber)
--VALUES ('7', 'err', 'errr', 'ceaa28bba4caba687dc31b1bbe79eca3c70c33f871f1ce8f528cf9ab5cfd76dd', '3456@gmail.com', '12345678900000');

--INSERT INTO Users (userID, name, username, passwordHash, email, phoneNumber)
--VALUES ('3', 'testuser', 'testuser', 'ae5deb822e0d71992900471a7199d0d95b8e7c9d05c40a8245a281fd2c1d6684', 'testuser@gmail.com', '123');

--INSERT INTO Users (userID, name, username, passwordHash, email, phoneNumber)
--VALUES ('1', 'Ilyas', 'hello', '87298cc2f31fba73181ea2a9e6ef10dce21ed95e98bdac9c4e1504ea16f486e4', 'ILyas@gmail.com', '777');

--INSERT INTO Users (userID, name, username, passwordHash, email, phoneNumber)
--VALUES ('2', 'Angad', 'hello1', '91e9240f415223910e94a7f52cd5f48f5ee1afc555078f0ab', 'Angad@gmail.com', '666');

--INSERT INTO Users (userID, name, username, passwordHash, email, phoneNumber)
--VALUES ('5', 'kalyan', 'kalyan', '486ea46224d1bb4fb680f34f7c9ad96a8f24ec88be73ea8e5a6c65260e9cb8a7', 'kalyanlimbu135@gtmail.com', '22335566');


-- Insert into Category table
INSERT INTO Category (categoryID, name)
VALUES ('1', 'sport');

INSERT INTO Category (categoryID, name)
VALUES ('2', 'Bags');

INSERT INTO Category (categoryID, name)
VALUES ('3', 't-shirts');

-- Insert into OrderLineItems table
INSERT INTO OrderLineItems (orderLineID, price, quantity, productID, orderID)
VALUES ('5', 299.00, 1, '3', '3');

INSERT INTO OrderLineItems (orderLineID, price, quantity, productID, orderID)
VALUES ('1', 2250.00, 5, '1', '1');

INSERT INTO OrderLineItems (orderLineID, price, quantity, productID, orderID)
VALUES ('3', 57700.00, 100, '2', '2');

INSERT INTO OrderLineItems (orderLineID, price, quantity, productID, orderID)
VALUES ('4', 2700.00, 6, '1', '2');

INSERT INTO OrderLineItems (orderLineID, price, quantity, productID, orderID)
VALUES ('2', 5770.00, 10, '2', '1');



-- Insert into Orders table
INSERT INTO Orders (orderID, dateCreated, totalPrice, warehouse, userID, addressID)
VALUES ('3', TO_DATE('02-DEC-23', 'DD-MON-YY'), 299, 'Singh Suppliers', '5', '4');

INSERT INTO Orders (orderID, dateCreated, totalPrice, warehouse, userID, addressID)
VALUES ('1', TO_DATE('02-DEC-23', 'DD-MON-YY'), 8020, 'Shanghai', '1', '3');

INSERT INTO Orders (orderID, dateCreated, totalPrice, warehouse, userID, addressID)
VALUES ('2', TO_DATE('02-DEC-23', 'DD-MON-YY'), 60400, 'Shanghai', '1', '3');


