insert into accounts (login, password)  values
('kapibara2018','qwerty123'),
('777terminator777','qpwofn2j_83'),
('the_best_master','12345'),
('the_best_client00','987732gj'),
('anton_ivanov1999','923hfhfg2'),
('user29','qwerty100');


insert into human (userId, name, surname, email, telNumber) values
(1,'Ivan','Jsonov','kapibara2018@mail.ru','89218882021'),
(2,'Vlad','Petrov','777terminator777@mail.ru','89002652956'),
(3,'Andrew','Mikhailov','the_best_master@mail.ru','89926750567'),
(4,'Vlad','Petrov','the_best_client00@mail.ru','89543214878'),
(5,'Anton','Ivanov','anton_ivanov1999@mail.ru','89225920982'),
(6,'John','Doe','user29@mail.ru','89010001010');

insert into master (humanId, experience, qualification) values
(1, 2, 'Курсы мастера по починке кухонной техники'),
(2, 1, 'Курсы мастера по починке микроволновок'),
(3, 10, 'Высшее образование в сфере починки кофемашин');



insert into day (name, shortName) values
	('Понедельник','Пн'),
('Вторник','Вт'),
('Среда','Ср'),
('Четверг','Чт'),
('Пятница','Пт'),
('Суббота','Сб'),
('Воскресенье','Вс');

insert into schedule (dayId, masterId) values
(1,1),
(2,1),
(5,1),
(3,2),
(4,2),
(1,3),
(2,3),
(6,3),
(7,3);

insert into qa (question, answer) values
('Как починить туалет, если кинул туда петарду?', 'Никак. Советуем купить новый'),
('Можно ли отмыть плиту после 10 лет использования?','Да, нужно всего лишь одно народное средство…');

insert into techniqueType(name) values
('Кухонная техника'),
('Техника из ванны'),
('Общее');

insert into technique (typeId, name) values
(1, 'Духовая печь'),
(1, 'Микроволновая печь'),
(1, 'Кофемашина'),
(2,'Стиральная машина');

insert into plan (techniqueId, name, cost) values
(1,'Защити свою духовку!', 990),
(2,'Защити свою микроволновку!', 790),
(3,'Защити свою кофемашинку!', 1290);

insert into client (humanId, address) values
(4, 'ул. Пушкина д. 45 кв. 23'),
(5, 'ул. Пушкина д. 45 кв. 22'),
(6, 'ул. Пушкина д. 45 кв. 21');

insert into owner (techniqueId ,clientId ,  buyDate ) values
(1, 1, '23.02.2012'),
(2, 2, '01.12.2021'),
(3, 3, '15.10.2017');


insert into subscriber (planId ,clientId ,startDate , finishDate) values
(1, 1,'24.03.2019','24.09.2019'),
(2, 2, '04.11.2021','04.05.2022'),
(3, 3, '14.10.2019','14.04.2020');

insert into orderStatus (name) values
('В поиске мастера'),
('В процессе выполнения'),
('Завершен'),
('Отменен');

insert into paymentType (name) values
('Подписка'),
('Безналичный');

insert into orders (clientId, techniqueId, paymentTypeId , content , cost , date) values
(1,1,2,'Сломалась духовка!!ПОМОГИТЕ!!!', 3000, '14.08.2019'),
(2,2,1,'Сломалась микроволновка!!ПОМОГИТЕ!!!', 2500, '18.02.2022'),
(3,3,1,'Сломалась кофемашинка!!ПОМОГИТЕ!!!', 4500, '06.06.2019');

insert into masterStatus (name) values
('Работает'),
('Завершил заказ'),
('Был заменен');

insert into masterLog (orderId , masterId, masterStatusId ) values
(1, 1 , 2),
(2, 2 , 2),
(3, 3 , 2);

insert into feedback (clientId, masterId, content, rating) values
(1,1,'Спасибо мастеру! Все быстро починил!' , 3),
(2,2, 'Лучший мастер евер!!!',  5),
(3,3, 'Работа выполнена быстро и четко. Благодарю мастера за оперативность.' ,4 );

insert into requestStatus (name) values
('Новый'),
('В работе'),
('Закрыт');

insert into supportRequest (authorId, content, theme) values
(1,'Не получается создать заказ', 'Создание заказа'),
(2,'Не устраивает работа мастера', 'Работа мастера');
