set foreign_key_checks = 0;
drop table if exists cookies;
drop table if exists storage;
drop table if exists orders;
drop table if exists recipes;
drop table if exists rawMaterials;
drop table if exists customers;
drop table if exists pallets;
drop table if exists Blockedpallets;
drop table if exists orderQuantity;
set foreign_key_checks = 1;

create table cookies(
	cookieName varchar (255) not null,
	primary key (cookieName)
);

create table Pallets(
	cookieName varchar (255) not null,
	palletID int not null,
	prodTime datetime not null default CURRENT_TIMESTAMP,
	status int not null default '0',
	primary key (palletID),
	foreign key (cookieName) references cookies(cookieName)
);
create table BlockedPallets(
	palletID int,
	blockedDate datetime,
	primary key(palletID),
	foreign key(palletID) references pallets (palletID)
);	

create table customers (
	customerName varchar (255) not null,
	adress varchar (255) not null,
	primary key (customerName)
);

create table orders(
	orderID		int not null,
	customerName varchar(255) not null,
	deliveryDate date,
	ifDelivered char (1) default 'n',
	primary key(orderID),
	foreign key (customerName) references customers (customerName)
);

create table orderQuantity(
	orderID int not null, 
	palletID int not null,
	primary key(orderID, palletID),
	foreign key (orderID) references orders (orderID),
	foreign key (palletID) references pallets (palletID) 
);

create table rawMaterials(
	ingredient varchar (255) not null,
	totalAmount int,
	storedDate date,
	lastUpdate date,
	unit varchar(5) not null,
	primary key (ingredient)
);

create table recipes(
	cookieName varchar(255) not null,
	ingredient varchar(255) not null,
	recipeAmount double not null,
	unit varchar(5) not null,
	primary key(cookieName, ingredient),
	foreign key(cookieName) references cookies(cookieName),
	foreign key(ingredient) references rawMaterials(ingredient)
);

delimiter //
 create trigger dateCheck before insert on rawmaterials
     for each row
     begin
     if isnull(NEW.storedDate) then
     set NEW.storedDate=curdate();
     set NEW.lastUpdate=curdate();
     end if;
     end;//
     /** har inte kollatom denna funkar ännu*/
     create trigger updateCheck before update on rawmaterials
    -> for each row
    -> begin
    -> if NEW.lastUpdate!=curdate() then
    -> set NEW.lastUpdate=curdate();
    -> end if;
    -> end;//
delimiter ;
insert into cookies value ('Nut Ring');
insert into cookies value ('Nut Cookie');
insert into cookies value ('Amneris');
insert into cookies value ('Tango');
insert into cookies value ('Almond Delight');
insert into cookies value ('Berliner');

/**
åäö funkar inte på min dator, även om sparad som UTF-8
*/
insert into customers value ('Finkakor AB', 'Helsingborg');
insert into customers value ('Småbröd AB', 'Malmö');
insert into customers value ('Kaffebröd AB', 'Landskrona');
insert into customers value ('Bjudkakor AB', 'Ystad');
insert into customers value ('Kalaskakor AB', 'Trelleborg');
insert into customers value ('Partykakor AB', 'Kristianstad');
insert into customers value ('Gästkakor AB', 'Hässleholm');
insert into customers value ('Skånekakor AB', 'Perstorp');

insert into recipes values 
('Nut Ring', 'flour', 450, 'g'),
('Nut Ring', 'butter', 450, 'g'),
('Nut Ring', 'icing Sugar', 190, 'g'),
('Nut Ring', 'roasted, Chopped Nuts', 225, 'g'),
('Nut Cookie', 'fine-ground Nuts', 750, 'g'),
('Nut Cookie', 'ground, Roasted Nuts', 625, 'g'),
('Nut Cookie', 'bread Crumbs', 125, 'g'),
('Nut Cookie', 'sugar', 375, 'g'),
('Nut Cookie', 'egg Whites', 3.5, 'dl'),
('Nut Cookie', 'chocolate', 375, 'g'),
('Amneris', 'Marzipan', 750, 'g'),
('Amneris', 'Butter', 250, 'g'),
('Amneris', 'Eggs', 250, 'g'),
('Amneris', 'Potato starch', 25, 'g'),
('Amneris', 'Wheat flour', 25, 'g'),
('Tango', 'Butter' ,200, 'g'),
('Tango', 'Sugar' ,250, 'g'),
('Tango', 'Flour' ,300, 'g'),
('Tango', 'Sodium bicarbonate' ,4, 'g'),
('Tango', 'Vanilla' ,2, 'g'),
('Almond Delight', 'Butter', 400, 'g'),
('Almond Delight', 'Sugar', 270, 'g'),
('Almond Delight', 'Chopped almonds', 279, 'g'),
('Almond Delight', 'Flour', 400, 'g'),
('Almond Delight', 'Cinnamon', 10, 'g'),
('Berliner', 'Flour', 350, 'g'),
('Berliner', 'Butter', 250, 'g'),
('Berliner', 'Icing sugar', 100, 'g'),
('Berliner', 'Eggs', 50, 'g'),
('Berliner', 'Vanilla sugar', 5, 'g'),
('Berliner', 'Chocolate', 50, 'g')
;

insert into rawmaterials (ingredient, totalAmount, unit) values
('Flour', 1000, 'g'),
('Butter', 1000, 'g'),
('Icing Sugar', 1000, 'g'),
('Roasted, chopped nuts', 1000, 'g'),
('Fine-ground nuts', 1000, 'g'),
('Ground, roasted nuts', 1000, 'g'),
('Marzipan', 1000, 'g'),
('Eggs', 1000, 'g'),
('Bread Crumbs', 1000, 'g'),
('Sugar', 1000, 'g'),
('Egg Whites', 1000, 'dl'),
('Chocolate', 1000, 'g'),
('Potato Starch', 1000, 'g'),
('Wheat Flour', 1000, 'g'),
('Sodium Bicarbonate', 1000, 'g'),
('Vanilla', 1000, 'g'),
('Chopped Almonds', 1000, 'g'),
('Cinnamon', 1000, 'g'),
('Vanilla Sugar', 1000, 'g')
;



