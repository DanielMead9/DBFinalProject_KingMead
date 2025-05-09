use DigitalInventory;

drop Procedure if exists GetProducts;
delimiter $$
Create Procedure GetProducts()
Begin 
select *
from Product;
END $$
delimiter ;

drop Procedure if exists UpdateProduct;
delimiter $$
Create Procedure UpdateProduct(myProductName varchar(50), mystAmount int, myshAmount int)
Begin 
Update Product
set AmountStorage = AmountStorage + mystAmount, AmountShelf = AmountShelf + myshAmount
where ProductName = myProductName;
END $$
delimiter ;

Drop procedure if exists GetSingleProduct;
delimiter $$
Create Procedure GetSingleProduct(myProduct varchar(50))
Begin 
select *
from Product
where productName = myproduct;
END $$
delimiter ;

drop Procedure if exists UpdateBuyer;
delimiter $$
Create Procedure UpdateBuyer(myBuyerName varchar(100), myBuyerLocation varchar(100), myBuyerPhone varchar(20), myBuyerEmail varchar(100))
Begin
Update Buyer
set BuyerLocation = myBuyerLocation, BuyerPhone = myBuyerPhone, BuyerEmail = myBuyerEmail
where BuyerName = myBuyerName;
END $$
delimiter ;

DROP PROCEDURE IF EXISTS UpdateStorageOnly;
DELIMITER $$
CREATE PROCEDURE UpdateStorageOnly(
    IN myProductName VARCHAR(50),
    IN deltaStorage INT
)
BEGIN
    UPDATE Product
    SET AmountStorage = AmountStorage + deltaStorage
    WHERE ProductName = myProductName;
END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS InsertOrUpdateBuyer;
DELIMITER $$
CREATE PROCEDURE InsertOrUpdateBuyer(
    IN myBuyerName VARCHAR(100),
    IN myBuyerLocation VARCHAR(100),
    IN myBuyerPhone VARCHAR(20),
    IN myBuyerEmail VARCHAR(100)
)
BEGIN
    IF EXISTS (SELECT * FROM Buyer WHERE BuyerName = myBuyerName) THEN
        UPDATE Buyer
        SET BuyerLocation = myBuyerLocation,
            BuyerPhone = myBuyerPhone,
            BuyerEmail = myBuyerEmail
        WHERE BuyerName = myBuyerName;
    ELSE
        INSERT INTO Buyer(BuyerName, BuyerLocation, BuyerPhone, BuyerEmail)
        VALUES (myBuyerName, myBuyerLocation, myBuyerPhone, myBuyerEmail);
    END IF;
END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS LogLargeOrder;
DELIMITER $$
CREATE PROCEDURE LogLargeOrder(
    IN myBuyerName VARCHAR(100),
    IN myProductName VARCHAR(50),
    IN myQuantity INT
)
BEGIN
    INSERT INTO LargeOrderLog(BuyerName, ProductName, Quantity, OrderDate)
    VALUES (myBuyerName, myProductName, myQuantity, NOW());
END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS GetLowShelfStock;
DELIMITER $$
CREATE PROCEDURE GetLowShelfStock(IN threshold INT)
BEGIN
    SELECT *
    FROM Product
    WHERE AmountShelf < threshold;
END $$
DELIMITER ;

