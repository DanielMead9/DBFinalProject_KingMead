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

Create table if not exists Supplier(
SupplierName varchar(50) not null,
SupplierLocation varchar(50),
Primary Key (SupplierName));

Create Table if not exists SupplierMapping(
SupplierName varchar(50),
ProductName varchar(50),
Foreign Key (SupplierName) references Supplier(SupplierName),
Foreign Key (ProductName) references Product(ProductName));
