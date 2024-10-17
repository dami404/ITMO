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
select clientId INTO client from subscriber where clientId = NEW.clientId and planId in (select id from plan where techniqueId = NEW.techniqueId) and startDate <= CURRENT_DATE and finishDate >= CURRENT_DATE;
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
start_date date;
finish_date date;
    BEGIN
select active INTO planA from plan where id = NEW.planId;
        IF NOT planA THEN 
		RAISE EXCEPTION 'Subscribe is unavailable now';
	END IF;
	IF NEW.startDate < CURRENT_DATE THEN
		RAISE EXCEPTION 'Incorrect date';
	END IF;
	IF NEW.startDate > NEW.finishDate THEN
		RAISE EXCEPTION 'Incorrect date';
	END IF;
	select finishDate into finish_date from subscriber where clientId = NEW.clientId and planId = NEW.planID;
	select startDate into start_date from subscriber where clientId = NEW.clientId and planId = NEW.planID;
	IF NEW.finishDate < finish_date THEN
		NEW.finishDate = finish_date;
	END IF;
	IF NEW.startDate > finish_date and finish_date > CURRENT_DATE THEN
		NEW.startDate = start_date;
	END IF;
	IF start_date IS NOT NULL THEN
		UPDATE subscriber SET finishDate = NEW.finishDate, startDate = NEW.startDate where clientId = NEW.clientId and planId = NEW.planID;
		RETURN NULL;
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
	IF NEW.masterStatusId  = 1 THEN
           RAISE EXCEPTION 'This status wrong';
	ELSEIF NEW.masterStatusId = 3 THEN UPDATE orders SET orderStatusId = 1 where id = NEW.orderId;
	ELSEIF NEW.masterStatusId = 2 THEN UPDATE orders SET orderStatusId = 3 where id = NEW.orderId;
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