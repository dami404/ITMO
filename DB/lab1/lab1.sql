

CREATE TABLE Company(
    ID                  SERIAL PRIMARY KEY,
    Name                VARCHAR(45) NOT NULL,
    Location            VARCHAR(45) NOT NULL,
    NumberOfWorkers     INTEGER DEFAULT 0
);


CREATE TABLE Employee(
    ID                  SERIAL PRIMARY KEY,
    Name                VARCHAR(45) NOT NULL,
    Age                 INTEGER DEFAULT 14
    Sex                 VARCHAR(1),
    CHECK               (Sex='М' OR Sex='Ж')
);


CREATE TABLE Contract
(
    EmployeeID          INTEGER REFERENCES Employee,
    CompanyID           INTEGER REFERENCES Company,
    PRIMARY KEY         (EmployeeID, CompanyID)
);


CREATE TABLE Outfit(
    ID                  SERIAL PRIMARY KEY,
    Size                VARCHAR(4),
    Type                VARCHAR(32) NOT NULL,
    Color               VARCHAR(32),
    EmployeeID          INTEGER REFERENCES Employee
);


CREATE TABLE Relationship(
    PersonOneFeelings   VARCHAR(32),
    PersonTwoFeelings   VARCHAR(32),
    PersonOneID         INTEGER REFERENCES Employee,
    PersonTwoID         INTEGER REFERENCES Employee,
    PRIMARY KEY         (PersonOneID, PersonTwoID)
);



INSERT INTO Company (Name, Location, NumberOfWorkers)
VALUES ('Apple','USA, California',99999),                -- 1
        ('Yandex','Russian Federation, Moscow', 12345);  -- 2


INSERT INTO Employee (Name, Sex, Age)
VALUES ('Джек','М',26),   -- 1
       ('Роза','Ж',21),   -- 2
       ('Лео','М',23),    -- 3
       ('Педро','М',42),  -- 4
       ('Антуан','М',33), -- 5
       ('Мария','Ж',41),  -- 6
       ('Роман','М',24),  -- 7
       ('Анна','Ж',19);   -- 8

INSERT INTO Contract (CompanyID, EmployeeID)
VALUES (1,1),
       (1,2),
       (1,3),
       (1,4),
       (1,5),
       (2,6),
       (2,7),
       (2,8);


INSERT INTO Outfit (EmployeeID, Size, Type, Color)
VALUES (1,'L','Костюм','Черный'),
       (2,'XS','Юбка и рубашка','Белый'),
       (3,'XXL','Толстовка и шорты','Красный и черный'),
       (4,'XL','Рубашка и джинсы','Синий'),
       (5,'L','Костюм'),
       (6,'S','Платье','Розовый'),
       (7,'M','Костюм','Темно-синий'),
       (8,'XXS','Толстовка и джинсы');


INSERT INTO Relationship (PersonOneID,PersonTwoID,PersonOneFeelings,PersonTwoFeelings)
VALUES (1,2,'Влюблен в нее','Влюблена в него'),
       (8,2,'Завидует','Недолюбливает'),
       (3,4,'Считает другом','Очень хорошего мнения'),
       (1,7,'Знакомые','Знакомые'),
       (5,6,'Бывший парень','Бывшая девушка');