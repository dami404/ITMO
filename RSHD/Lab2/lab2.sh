#!/bin/bash

# Заходим на сервер
ssh -J s339018@helios.cs.ifmo.ru:2222 postgres0@pg185


mkdir ehz35
# Этап 1 - инициализация кластера БД
initdb -D $HOME/ehz35 -E ISO_8859_5 --locale=ru_RU.ISO8859-5 

# Запуск сервера
pg_ctl -D /var/db/postgres0/ehz35 -l my_logs.log start

# Копирование конфигов на локалку
scp -o ProxyJump=s339018@se.ifmo.ru:2222 postgres0@pg185:~/ehz35/postgresql.conf ./
scp -o ProxyJump=s339018@se.ifmo.ru:2222 postgres0@pg185:~/ehz35/pg_hba.conf ./

# применение изменений конфигов pg_hba.conf / postgresql.conf
pg_ctl -D /var/db/postgres0/ehz35/ reload
pg_ctl -D /var/db/postgres0/ehz35/ restart

# Зайти в бд
psql -d postgres -p9807 # через peer
psql -d postgres -U s339018 -h 127.0.0.1 -p 9807 # через sha-256 пароль
psql -d lastgoldsoup -p 9807
psql -d lastgoldsoup -U student1 -h 127.0.0.1 -p 9807


CREATE USER s339018 WITH PASSWORD '123';
# GRANT CONNECT ON DATABASE "lastgoldsoup" TO "student";

# Проверить, что пароль шифруется
SELECT * FROM pg_authid where rolname='s339018';


#  Создание бд
CREATE DATABASE lastgoldsoup TEMPLATE template1; # по дефолту и так template1, но мы решили, чтобы наверняка

# Создание табличного пространства
CREATE TABLESPACE indexTablespace LOCATION '/var/db/postgres0/kls16';


create table my_table(
    id serial,
    name varchar(10)
);

create index on my_table (name) TABLESPACE indexTablespace;

GRANT INSERT ON my_table to student1;
GRANT USAGE, SELECT ON SEQUENCE my_table_id_seq to student1;


SELECT * FROM pg_tablespace;

SELECT relname FROM pg_class WHERE reltablespace IN (SELECT oid FROM pg_tablespace);