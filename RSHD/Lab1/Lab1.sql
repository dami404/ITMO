-- \prompt "Введите название схемы: " input_schema_name
-- SET my.schema_name TO :'input_schema_name';

\prompt "Введите название таблицы: " input_table_name
SET my.table_name TO :input_table_name;

DO
$$
    DECLARE
        result_column          record;
        result_string          text;
        table_name             text;
        schema_name            text;
        table_name_full        text[];
        table_name_full_length int;
        table_exists           boolean;
    BEGIN
        --         table_name := current_setting('my.table_name');
--         schema_name := current_setting('my.schema_name');

        table_name := current_setting('my.table_name');
        table_name_full := string_to_array(table_name, '.');
        table_name_full_length := cardinality(table_name_full);

        IF table_name_full_length = 2 THEN
            schema_name := table_name_full[1];
            table_name := table_name_full[2];
        ELSEIF table_name_full_length = 1 THEN
            schema_name := CURRENT_SCHEMA;
        ELSE
            RAISE EXCEPTION 'Некорректный ввод (неверное кол-во аргументов)';
        END IF;

        SELECT EXISTS (SELECT * FROM pg_tables t WHERE t.tablename = table_name AND t.schemaname = schema_name)
        INTO table_exists;

        IF NOT table_exists THEN
            RAISE EXCEPTION 'Таблица %.% не существует', schema_name, table_name;
        END IF;

        result_string = format(E'\nТаблица: %s.%s\n', schema_name, table_name);
        result_string = result_string ||
                        format(E'%-25s %-35s %-10s %-5s\n', 'Триггер', 'Функция', 'Активен', 'Привязанные столбцы');
        result_string = result_string ||
                        format(E'%-25s %-35s %-10s %-5s\n', '------------------------', '------------------------',
                               '---------', '------------------------');

        FOR result_column IN
            SELECT _trigger.tgname                       AS trigger_name,
                   _function.proname                     AS function_name,
                   (SELECT COALESCE(array_to_string(array_agg(attname), ', '), '-')
                    FROM pg_attribute _column
                             JOIN pg_class _table ON _column.attrelid = _table.oid
                             JOIN pg_namespace _schema ON _table.relnamespace = _schema.oid
                    WHERE _column.attnum = ANY (_trigger.tgattr)
                      AND _table.relname = table_name
                      AND _schema.nspname = schema_name) AS column_names,
                   CASE
                       WHEN _trigger.tgenabled = 'D' THEN 'Нет'
                       ELSE 'Да'
                       END                               AS trigger_active
            FROM pg_trigger _trigger
                     JOIN pg_class _table ON _trigger.tgrelid = _table.oid
                     JOIN pg_namespace _schema ON _table.relnamespace = _schema.oid
                     JOIN pg_proc _function ON _trigger.tgfoid = _function.oid
            WHERE _trigger.tgisinternal = FALSE
              AND _table.relname = table_name
              AND _schema.nspname = schema_name
            LOOP
                result_string = result_string || format(E'%-25s %-35s %-10s %-5s\n',
                                                        result_column.trigger_name,
                                                        result_column.function_name,
                                                        result_column.trigger_active,
                                                        result_column.column_names);
            END LOOP;

        RAISE NOTICE '%', result_string;
    END
$$ LANGUAGE plpgsql;

RESET my.table_name;
RESET my.schema.name;