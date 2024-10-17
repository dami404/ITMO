create table accounts(
	id serial primary key,
	login varchar(20) not null unique,
	password varchar(50) not null,
	active boolean not null default true
);
create table human (
id serial primary key,
userId integer references accounts(id),
name varchar(20) not null,
surname varchar(20) not null,
email varchar(30) not null unique,
telNumber char(11) not null unique);
create table master (
	id serial primary key,
	humanId integer not null references human(id),
	rating real default 0,
	photo varchar(50) unique,
	experience smallint not null,
	qualification varchar(100) not null,
	check(rating >= 0 and rating <= 5 and experience >= 0)
);
create table day (
	id serial primary key,
	name varchar(20) unique not null,
	shortName char(2) unique not null
);
create table schedule (
	masterId integer references master(id),
	dayId integer references day(id),
	primary key(masterId, dayId)
);
create table qa (
	id serial primary key,
	question text not null unique,
	answer text not null,
active boolean not null default false
);

create table techniqueType(
	id serial primary key,
	name varchar(20) not null unique
);
create table technique(
	id serial primary key,
	typeId integer references techniqueType(id) not null,
	name varchar(20) not null
);
create table plan(
	id serial primary key,
	techniqueId integer unique references technique(id),
	name varchar(30) not null unique,
	cost smallint not null,
	active boolean not null default true
);
create table client(
	id serial primary key,
	humanId integer references human(id),
address varchar(30) not null
);
create table owner(
	techniqueId integer references technique(id),
	clientId integer references client(id),
	buyDate Date not null,
	primary key(techniqueId, clientId)
);
create table subscriber(
planId integer references plan(id),
clientId integer references client(id),
startDate Date not null,
finishDate Date not null,
primary key(planId, clientId),
check(startDate < finishDate)
);
create table feedback(
id serial primary key,
clientId integer references client(id),
masterId integer references master(id),
content text not null,
rating real not null,
date Date not null
);
create table orderStatus (
	id serial primary key,
	name varchar(50) not null unique
);
create table paymentType (
	id serial primary key,
	name varchar(50) not null unique
);
create table orders (
	id serial primary key,
	clientId integer not null references client(id),
	techniqueId integer not null references technique(id),
	paymentTypeId integer not null references paymentType(id),
	orderStatusId integer not null references orderStatus(id),
	content text not null,
	cost integer not null check(cost > 0),
	date date not null,
	unique(clientId, techniqueId, date)
);
create table masterStatus (
	id serial primary key,
	name varchar(50) not null unique
);
create table masterLog (
	orderId integer references orders(id),
	masterId integer references master(id),
	beginDate date not null,
	endDate date,
	masterStatusId integer not null references masterStatus(id),
	primary key (orderId, masterId)
);
create table requestStatus (
	id serial primary key,
	name varchar(20) not null unique
);
create table supportRequest (
	id serial primary key,
	statusId integer not null references requestStatus(id),
	authorId integer not null references client(id),
	content text not null,
	theme varchar(50) not null,
	date date not null
);

create table newOrders (
	masterId integer references master(id),
	orderId integer references orders(id),
	primary key(masterId,orderId)
);
