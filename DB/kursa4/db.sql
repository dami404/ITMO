-- СОЗДАНИЕ ТАБЛИЦ
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
    telNumber char(11) not null unique
);







create table master (
	id serial primary key,
	humanId integer not null references human(id),
	rating real default 0,
	photo varchar(50) unique,
	experience smallint not null,
	qualification varchar(100) not null,
	check(rating >= 0 and rating <= 5 and experience >= 0)
);

CREATE OR REPLACE FUNCTION findall_workers()
RETURNS TABLE (
	w_photo varchar(50),
	w_name varchar(30),
	w_rating real,
	w_experience smallint,
	w_id int
)AS
$$
BEGIN
	RETURN QUERY
	select master.photo, concat(human.name,human.surname), master.rating, master.experience, master.id from master
	join human on  human.id=master.humanId;
	END;
$$ LANGUAGE plpgsql;


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
    primary key(planId, clientId)
);

CREATE OR REPLACE FUNCTION findall_subscribes(client_id INT)
RETURNS TABLE (
	sub_type varchar(30),
	sub_start_date date,
	sub_finish_date date,
	sub_price smallint
)AS
$$
BEGIN
	RETURN QUERY
	select plan.name, subscriber.startDate, subscriber.finishDate, plan.cost from subscriber
	join plan on  plan.id=subscriber.planId
	where client_id=subscriber.clientId;
	END;
$$ LANGUAGE plpgsql;



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


-- ТРИГГЕРЫ
CREATE OR REPLACE FUNCTION check_password(
    in_username VARCHAR(255),
    in_password VARCHAR(255)
) RETURNS BOOLEAN AS $$
DECLARE
    hashed_password VARCHAR(255);
BEGIN
    -- Получаем хэшированный пароль для данного логина
    SELECT password into hashed_password
    FROM accounts
    WHERE login = in_username;

    -- Проверяем, совпадает ли хэшированный пароль с введенным паролем
    RETURN hashed_password = in_password;
END;
$$ LANGUAGE plpgsql;




--создание мастера
CREATE OR REPLACE FUNCTION master_insert() RETURNS trigger AS $master_insert$
    BEGIN
	NEW.rating = 0;
	NEW.photo = concat('/include/photos/',NEW.id,'.jpg');
        RETURN NEW;
    END;
$master_insert$ LANGUAGE plpgsql;

CREATE TRIGGER master_insert BEFORE INSERT ON master
    FOR EACH ROW EXECUTE PROCEDURE master_insert();

--создание заказа
CREATE OR REPLACE FUNCTION order_insert() RETURNS trigger AS $order_insert$
DECLARE
client integer;
    BEGIN
select clientId INTO client from subscriber where clientId = NEW.clientId and planId in (select id from plan where techniqueId = NEW.techniqueId) and startDate <= current_timestamp and finishDate >= current_timestamp;
        IF client IS NOT NULL THEN
           NEW.paymentTypeID = 1;
	ELSE NEW.paymentTypeID = 2;
        END IF;
        NEW.orderStatusId = 1;
        RETURN NEW;
    END;
$order_insert$ LANGUAGE plpgsql;

CREATE TRIGGER order_insert BEFORE INSERT ON orders
    FOR EACH ROW EXECUTE PROCEDURE order_insert();

--создание подписки
CREATE OR REPLACE FUNCTION subscriber_insert() RETURNS trigger AS $subscriber_insert$
DECLARE
planA boolean;
    BEGIN
select active INTO planA from plan where id = NEW.planId;
        IF NOT planA THEN 
		RAISE EXCEPTION 'Subscribe is unavailable now';
	END IF;
        RETURN NEW;
    END;
$subscriber_insert$ LANGUAGE plpgsql;

CREATE TRIGGER subscriber_insert BEFORE INSERT ON subscriber
    FOR EACH ROW EXECUTE PROCEDURE subscriber_insert();

--создание отзыва
CREATE OR REPLACE FUNCTION feedback_insert() RETURNS trigger AS $feedback_insert$
DECLARE master integer;
    BEGIN
select masterId INTO master from masterLog where masterId = NEW.masterId and orderId in (select id from orders where clientId = NEW.clientId);
        IF master IS NULL THEN
           RAISE EXCEPTION 'This client doesn’t have any orders with that master';
        END IF;

	NEW.date = current_timestamp;
        RETURN NEW;
    END;
$feedback_insert$ LANGUAGE plpgsql;

CREATE TRIGGER feedback_insert BEFORE INSERT ON feedback
    FOR EACH ROW EXECUTE PROCEDURE feedback_insert();

--обновление рейтинга при появлении отзыва
CREATE OR REPLACE FUNCTION feedback_after_insert() RETURNS trigger AS $feedback_after_insert$
DECLARE newRating real;
    BEGIN
	select AVG(rating)into newRating from feedback where masterId = NEW.masterId;
        update master SET rating = newRating where id = NEW.masterId;
	RETURN NEW;
    END;
$feedback_after_insert$ LANGUAGE plpgsql;

CREATE TRIGGER feedback_after_insert AFTER INSERT ON feedback
    FOR EACH ROW EXECUTE PROCEDURE feedback_after_insert();

--завершение или смена мастера в заказе
CREATE OR REPLACE FUNCTION masterLog_before_update() RETURNS trigger AS $masterLog_before_update$
    BEGIN
	IF NEW.masterStatusId  == 1 THEN
           RAISE EXCEPTION 'This status wrong';
	ELSE IF NEW.masterStatusId == 3 THEN UPDATE orders SET orderStatusId = 1 where id = NEW.orderId;
	ELSE IF NEW.masterStatusId == 2 THEN UPDATE orders SET orderStatusId = 3 where id = NEW.orderId;
	END IF;
	NEW.endDate = current_timestamp;
	RETURN NEW;
	END;
$masterLog_before_update$ LANGUAGE plpgsql;

CREATE TRIGGER masterLog_before_update BEFORE UPDATE ON masterLog
    FOR EACH ROW EXECUTE PROCEDURE masterLog_before_update();

--назначение мастера на заказ
CREATE OR REPLACE FUNCTION masterLog_before_insert() RETURNS trigger AS $masterLog_before_insert$
DECLARE
id integer;
    BEGIN
select masterStatusId into id from MasterLog where orderId = NEW.orderId and (masterStatusId=1 or masterStatusId=2);
    	IF id IS NOT NULL THEN
           RAISE EXCEPTION 'Order already in work';
END IF;
NEW.masterStatusId = 1;
	NEW.beginDate = current_timestamp;
RETURN NEW;
    END;
$masterLog_before_insert$ LANGUAGE plpgsql;

CREATE TRIGGER masterLog_before_insert BEFORE INSERT ON masterLog
    FOR EACH ROW EXECUTE PROCEDURE masterLog_before_insert();

--мастер назначен на заказ
CREATE OR REPLACE FUNCTION masterLog_after_insert() RETURNS trigger AS $masterLog_after_insert$
    BEGIN
	UPDATE orders SET orderStatusId = 2 where id = NEW.orderId;
	RETURN NEW;
    END;
$masterLog_after_insert$ LANGUAGE plpgsql;

CREATE TRIGGER masterLog_after_insert AFTER INSERT ON masterLog
    FOR EACH ROW EXECUTE PROCEDURE masterLog_after_insert();

--создание обращения ТП
CREATE OR REPLACE FUNCTION supportRequest_before_insert() RETURNS trigger AS $supportRequest_before_insert$
    BEGIN
    	NEW.statusId = 1;
	NEW.date = current_timestamp;
RETURN NEW;
    END;
$supportRequest_before_insert$ LANGUAGE plpgsql;

CREATE TRIGGER supportRequest_before_insert BEFORE INSERT ON supportRequest
    FOR EACH ROW EXECUTE PROCEDURE supportRequest_before_insert();

--удаление обращения ТП
CREATE OR REPLACE FUNCTION supportRequest_before_delete() RETURNS trigger AS $supportRequest_before_delete$
    BEGIN
	IF OLD.requestStatus != 3 THEN
		RAISE EXCEPTION 'Request is not done.';
	END IF;
    RETURN OLD;
    END;
$supportRequest_before_delete$ LANGUAGE plpgsql;

CREATE TRIGGER supportRequest_before_delete BEFORE DELETE ON supportRequest
    FOR EACH ROW EXECUTE PROCEDURE supportRequest_before_delete();

--удаление подписки пользователя
CREATE OR REPLACE FUNCTION subscriber_before_delete() RETURNS trigger AS $subscriber_before_delete$
    BEGIN
	IF OLD.endDate >= current_timestamp THEN
		RAISE EXCEPTION 'Subscribe is still active.';
	END IF;
    RETURN OLD;
    END;
$subscriber_before_delete$ LANGUAGE plpgsql;

CREATE TRIGGER subscriber_before_delete BEFORE DELETE ON subscriber
    FOR EACH ROW EXECUTE PROCEDURE subscriber_before_delete();

--удаление логов мастера
CREATE OR REPLACE FUNCTION masterLog_before_delete() RETURNS trigger AS $masterLog_before_delete$
	DECLARE masterA boolean;
    BEGIN
	IF OLD.masterStatusId != 3 THEN
		RAISE EXCEPTION 'Log with that status unavailable to delete.';
	END IF;
	select active into masterA from accounts where id in (select userId from human where id in (select humanId from master where id = OLD.masterId));
	IF masterA THEN
		RAISE EXCEPTION 'User is still active.';
	END IF;

    RETURN OLD;
    END;
$masterLog_before_delete$ LANGUAGE plpgsql;

CREATE TRIGGER masterLog_before_delete BEFORE DELETE ON masterLog
    FOR EACH ROW EXECUTE PROCEDURE masterLog_before_delete();

--удаление комментария
CREATE OR REPLACE FUNCTION feedback_before_delete() RETURNS trigger AS $feedback_before_delete$
	DECLARE 
	masterA boolean;
	clientA boolean;
    BEGIN
	select active into masterA from accounts where id in (select userId from human where id in (select humanId from master where id = OLD.masterId));
	IF masterA THEN
		RAISE EXCEPTION 'Master is still active.';
	END IF;
	select active into clientA from accounts where id in (select userId from human where id in (select humanId from client where id = OLD.clientId));
	IF clientA THEN
		RAISE EXCEPTION 'Client is still active.';
	END IF;


    RETURN OLD;
    END;
$feedback_before_delete$ LANGUAGE plpgsql;

CREATE TRIGGER feedback_before_delete BEFORE DELETE ON feedback
    FOR EACH ROW EXECUTE PROCEDURE feedback_before_delete();


-- ФУНКЦИИ
-- оформление подписки

CREATE OR REPLACE FUNCTION subscribe_client_plan(client_id INT, plan_id INT, start_date DATE, finish_date DATE)
RETURNS VOID AS
$$
BEGIN
	INSERT INTO subscriber(planId, clientId, startDate, finishDate)
	VALUES (plan_id, client_id, start_date, finish_date);
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_subscribe_plans()
RETURNS TABLE (
	id integer,
	name varchar(30),
	cost smallint,
	tecniqueName varchar(20)
)AS
$$
BEGIN
	RETURN QUERY
	select plan.id, plan.name, plan.cost, technique.name from plan
	join technique on plan.techniqueId = technique.id
	where plan.active = true order by plan.id;
	END;
$$ LANGUAGE plpgsql;



CREATE OR REPLACE FUNCTION findall_faq()
RETURNS TABLE(
	my_question text,
	my_answer text
)AS
$$
BEGIN
	RETURN QUERY
	SELECT question, answer from qa
	END;
$$ LANGUAGE plpgsql;




--создание заказа
CREATE OR REPLACE FUNCTION get_owners(client_id INT)
RETURNS TABLE (
	techniqueId integer,
	buyDate date,
	tecniqueName varchar(20)
)AS
$$
BEGIN
	RETURN QUERY
	select technique.id, owner.buyDate, technique.name from owner
	join technique on owner.techniqueId = technique.id and owner.clientId = client_id;
	END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_masters(date DATE)
RETURNS TABLE (
	masterId integer,
	rating real
)AS
$$
DECLARE
	day smallint;
BEGIN
	day = extract(isodow from date);
	RETURN QUERY
	select master.id, master.rating from master
	join schedule on schedule.masterId = master.id and schedule.dayId = day;
	END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION create_order(client_id INT, technique_id INT, order_content TEXT, order_cost INT, date Date)
RETURNS VOID AS
$$
BEGIN
	INSERT INTO orders(clientId, techniqueId, content, cost, date)
	VALUES (client_id, technique_id, order_content, order_cost, date);
END;
$$ LANGUAGE plpgsql;

-- создание заказа с выбором мастера

CREATE OR REPLACE FUNCTION create_order_with_master(client_id INT, technique_id INT, order_content TEXT, order_cost INT, date DATE,master_id INT)
RETURNS VOID AS
$$
BEGIN
	INSERT INTO orders(clientId, techniqueId, content, cost, date)
	VALUES (client_id, technique_id, order_content, order_cost, date);
    
	INSERT INTO masterLog(orderId, masterId)
	VALUES ((SELECT id FROM orders WHERE clientId = client_id AND techniqueId = technique_id AND date = current_timestamp), master_id);
END;
$$ LANGUAGE plpgsql;

-- просмотр статуса заказа

CREATE OR REPLACE FUNCTION get_order_status(client_id INT, technique_id INT, order_date DATE)
RETURNS VARCHAR(50) AS
$$
DECLARE
	status_name VARCHAR(50);
BEGIN
	SELECT os.name INTO status_name
	FROM orders o
	JOIN orderStatus os ON o.orderStatusId = os.id
	WHERE o.clientId = client_id AND o.techniqueId = technique_id AND o.date = order_date;

	RETURN status_name;
END;
$$ LANGUAGE plpgsql;

-- оставление отзыва

CREATE OR REPLACE FUNCTION leave_feedback(client_id INT, master_id INT, feedback_content TEXT, feedback_rating REAL)
RETURNS VOID AS
$$
BEGIN
	INSERT INTO feedback(clientId, masterId, content, rating)
	VALUES (client_id, master_id, feedback_content, feedback_rating);
END;
$$ LANGUAGE plpgsql;

-- поиск ответов на частые вопросы

CREATE OR REPLACE FUNCTION search_faq(question_text TEXT)
RETURNS TEXT AS
$$
DECLARE
	answer_text TEXT;
BEGIN
	SELECT answer INTO answer_text
	FROM qa
	WHERE question = question_text;

	RETURN answer_text;
END;
$$ LANGUAGE plpgsql;

-- составление и просмотр своего расписания

CREATE OR REPLACE FUNCTION get_master_schedule(master_id INT)
RETURNS TABLE (
	day_name VARCHAR(20),
	day_short CHAR(2)
) AS $$
BEGIN
	RETURN QUERY
	SELECT day.name, day.shortName
	FROM day
	JOIN schedule ON day.id = schedule.dayId
	WHERE schedule.masterId = master_id;
END;
$$ LANGUAGE plpgsql;

-- просмотр текущих заказов

CREATE OR REPLACE FUNCTION get_master_current_orders(master_id INT)
RETURNS TABLE (
	order_id INT,
	content TEXT,
	cost INT,
	date DATE
) AS $$
BEGIN
	RETURN QUERY
	SELECT orders.id, orders.content, orders.cost, orders.date
	FROM orders
	JOIN masterLog ON orders.id = masterLog.orderId
	WHERE masterLog.masterId = master_id AND masterLog.endDate IS NULL;
END;
$$ LANGUAGE plpgsql;

-- просмотр истории заказов

CREATE OR REPLACE FUNCTION get_master_order_history(master_id INT)
RETURNS TABLE (
	order_id INT,
	content TEXT,
	cost INT,
	date DATE,
	rating REAL
) AS $$
BEGIN
	RETURN QUERY
	SELECT orders.id, orders.content, orders.cost, orders.date, feedback.rating
	FROM orders
	JOIN masterLog ON orders.id = masterLog.orderId
	LEFT JOIN feedback ON orders.clientId = feedback.clientId
WHERE masterLog.masterId = master_id AND masterLog.endDate IS NOT NULL;
	
END;
$$ LANGUAGE plpgsql;

--прием/отклонение заказов

CREATE OR REPLACE FUNCTION accept_order(order_id INT, master_id INT)
RETURNS VOID AS $$
BEGIN
	INSERT INTO masterLog(orderId, masterId)
	VALUES (order_id, master_id);
END;
$$ LANGUAGE plpgsql;

-- Отклонить заказ мастером
CREATE OR REPLACE FUNCTION reject_order(order_id INT, master_id INT)
RETURNS VOID AS $$
BEGIN
	UPDATE masterLog
	SET masterStatusId = (SELECT id FROM masterStatus WHERE name = 'Заменен')
	WHERE orderId = order_id AND masterId = master_id;
END;
$$ LANGUAGE plpgsql;

--просмотр и редактирование своего профиля

CREATE OR REPLACE FUNCTION get_master_profile(master_id INT)
RETURNS TABLE (
	name VARCHAR(20),
	surname VARCHAR(20),
	rating REAL,
	photo VARCHAR(50),
	experience SMALLINT,
	qualification VARCHAR(100)
) AS $$
BEGIN
	RETURN QUERY
	SELECT human.name, human.surname, master.rating, master.photo, master.experience, master.qualification
	FROM master
	JOIN human ON master.humanId = human.id
	WHERE master.id = master_id;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION change_profile(master_id INT,name_ TEXT, surname_ TEXT, experience_ TEXT, qualification_ TEXT)
RETURNS VOID AS $$
BEGIN
	IF name_ IS NOT NULL THEN
	UPDATE human
	SET name = _name
	where id in (select humanId from master where id = master_id);
	END IF;
IF surname_ IS NOT NULL THEN
	UPDATE human
	SET surname = surname_ 
	where id in (select humanId from master where id = master_id);
	END IF;
IF experience_ IS NOT NULL THEN
	UPDATE master
	SET experience = experience_ 
	where id = master_id;
	END IF;
IF qualification_ IS NOT NULL THEN
	UPDATE master
	SET qualification = qualification_ 
	where id = master_id;
	END IF;
END;
$$ LANGUAGE plpgsql;
