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

create table storage(
	cookieName varchar (255) not null,
	pallets int not null,
	primary key (cookieName),
	foreign key (cookieName) references cookies(cookieName)
);


create table customers (
	customerName varchar (255) not null,
	adress varchar (255) not null,
	primary key (customerName, adress)
);

create table orders(
	customerName varchar (255) not null,
	deliveryDate date,
	ifDelivered char (1) default 'n',
	foreign key (customerName) references customers (customerName)
);

create table recipes(
	cookieName varchar (255) not null,
	flour int (10) default '0',
	butter int (10) default '0',
	icingSuger int (10) default '0',
	roastedChoppedNuts int (10) default '0',
	fineGroundNuts int (10) default '0',
	groundRoastedNuts int (10) default '0',
	breadCrumbs int (10) default '0',
	sugar int (10) default '0',
	eggWhites int (10) default '0',
	chocolate int (10) default '0',
	marzipan int (10) default '0',
	eggs int (10) default '0',
	potatoStarch int (10) default '0',
	wheatFlour int (10) default '0',
	sodianBicarbonate int (10) default '0',
	vanilla int (10) default '0',
	choppedAlmonds int (10) default '0',
	cinnamon int (10) default '0',
	vanillaSugar int (10) default '0'
);

create table rawMaterial(
	ingredients varchar (255) not null,
	totalAmount int,
	storedDate date,
	lastUpdate date,
	primary key (ingredients)
);

