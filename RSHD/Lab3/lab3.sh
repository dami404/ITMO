# основной узел
ssh -J s339018@helios.cs.ifmo.ru:2222 postgres0@pg185 
# резервный узел
ssh -J s339018@helios.cs.ifmo.ru:2222 postgres3@pg168
#копирование конфигов
scp -o ProxyJump=s339018@se.ifmo.ru:2222 postgres0@pg185:~/ehz35/postgresql.conf ./
scp -o ProxyJump=s339018@se.ifmo.ru:2222 postgres0@pg185:~/ehz35/pg_hba.conf ./


scp -o ProxyJump=s339018@se.ifmo.ru:2222 ./postgresql.conf postgres0@pg185:~/ehz35/
scp -o ProxyJump=s339018@se.ifmo.ru:2222 ./pg_hba.conf postgres0@pg185:~/ehz35/

initdb -D $HOME/ehz35 -E ISO_8859_5 --waldir=$HOME/euu73 --locale=ru_RU.ISO8859-5 

scp -o ProxyJump=s339018@se.ifmo.ru:2222 ./postgresql.conf postgres3@pg168:~/ehz35/ 
scp -o ProxyJump=s339018@se.ifmo.ru:2222 ./pg_hba.conf postgres3@pg168:~/ehz35/ 

scp -o ProxyJump=s339018@se.ifmo.ru:2222 ./pg_backup.sh postgres0@pg185:~/ 
scp -o ProxyJump=s339018@se.ifmo.ru:2222 ./backup_cleanup.sh postgres0@pg185:~/

tar xvzf backups/backup_2024-06-04-19-05/base.tar.gz -C ehz35
tar xvzf backups/backup_2024-06-04-19-05/pg_wal.tar.gz -C ehz35/pg_wal
tar xvzf backups/backup_2024-06-04-19-05/16385.tar.gz -C kls16


create table my_table3(
    id serial,
    name varchar(10),
    primary key (id) using index tablespace indextablespace
);
insert into my_table3(name) values ('dima1'),('artem1'),('polina1'),('ivan'),('pavel'),('masha');
delete from my_table2 where id in (2,4,6);

tar xvzf basebackups/backup_2024-06-04-21-19/base.tar.gz -C r_ehz35/
tar xvzf basebackups/backup_2024-06-04-21-19/pg_wal.tar.gz -C r_ehz35/pg_wal

tar xvzf basebackups/backup_dop1/base.tar.gz -C r_ehz35/
tar xvzf basebackups/backup_dop1/pg_wal.tar.gz -C r_ehz35/pg_wal
tar xvzf basebackups/backup_dop1/16387.tar.gz -C kls16/

chmod 0700 r_ehz35/



pg_dump -c -p 9807 -h localhost -U postgres0 lastgoldsoup > dump.sql
psql -d lastgoldsoup -p 9807 < dump.sql

# 4 этап полностью на основном узле
insert into my_table2(name) values ('dima1'),('artem1'),('polina1');
pg_dump -c -p 9807 -h localhost -U postgres0 lastgoldsoup > dump.sql
delete from my_table2 where id in (2,4,6);
psql -f dump.sql -p 9807 -h localhost -U postgres0 lastgoldsoup < dump.sql
psql -d lastgoldsoup -p 9807