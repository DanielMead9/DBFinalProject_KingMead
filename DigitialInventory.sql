Create database DigitalInventory;

use DigitalInventory;

Create table if not exists Product(
ProductName varchar(50) not null,
ProductType varchar(20) not null,
SellPrice double(5,2) not null,
CostPrice double(5,2) not null,
AmountStorage int,
AmountShelf int,
Primary Key (ProductName));

Create table if not exists Buyer(
BuyerName varchar(100) not null, 
BuyerLocation varchar(100),
BuyerPhone varchar(20),
BuyerEmail varchar(100),
Primary Key (BuyerName));

CREATE TABLE IF NOT EXISTS LargeOrderLog (
    LogID INT AUTO_INCREMENT PRIMARY KEY,
    BuyerName VARCHAR(100),
    ProductName VARCHAR(50),
    Quantity INT,
    OrderDate DATETIME,
    FOREIGN KEY (BuyerName) REFERENCES Buyer(BuyerName),
    FOREIGN KEY (ProductName) REFERENCES Product(ProductName)
);


