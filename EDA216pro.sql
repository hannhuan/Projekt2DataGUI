set foreign_key_checks = 0;
drop table if exists cookies;
drop table if exists storage;
drop table if exists orders;
drop table if exists recipes;
drop table if exists rawMaterial;
drop table if exists customers;
set foreign_key_checks = 1;

create table cookies(
	cookieName varchar (255) not null,
	primary key (cookieName)
);

create table Pallet(
	cookieName varchar (255) not null,
	palletLabel int not null auto_increment,
	timeStamp datetime not null default CURRENT_TIMESTAMP,
	status int not null default '0',
	primary key (cookieName),
	foreign key (cookieName) references cookies(cookieName)
);
create table BlockedPallets(
	palletLabel int,
	timeStamp datetime,
	primary key(palletLabel),
	foreign key(palletLabel) references pallet(palletLabel),
	foreign key(timeStamp) references pallet(timeStamp)
);	

create table customers (
	customerName varchar (255) not null,
	adress varchar (255) not null,
	primary key (customerName, adress)
);

create table orders(
	customerName varchar (255) not null,
	nbrPallets int (20) default '0',
	deliveryDate date,
	ifDelivered char (1) default 'n',
	primary key(customerName, deliveryDate),
	foreign key (customerName) references customers (customerName)
);

create table orderQuantity(
	customerName varchar (255),
	deliveryDate date,
	cookieName	varchar(255) not null,
	nbrPallets	int (20) default '0',
	primary key(customerName, deliveryDate),
	foreign key (customerName) references orders (customerName),
	foreign key (deliveryDate) references orders (deliveryDate)
);

create table rawMaterial(
	ingredients varchar (255) not null,
	totalAmount int,
	storedDate date,
	lastUpdate date,
	primary key (ingredients)
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
('Nut Cookie', 'fine-Ground Nuts', 750, 'g'),
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

