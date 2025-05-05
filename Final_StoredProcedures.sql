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