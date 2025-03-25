-- init-databases.sql
CREATE EXTENSION dblink;

DO
$$
    BEGIN
        IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'auth_service') THEN
            PERFORM dblink_exec('dbname=postgres', 'CREATE DATABASE auth_service');
        END IF;
        IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'inventory_service') THEN
            PERFORM dblink_exec('dbname=postgres', 'CREATE DATABASE inventory_service');
        END IF;
        IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'order_service') THEN
            PERFORM dblink_exec('dbname=postgres', 'CREATE DATABASE order_service');
        END IF;
    END
$$;