CREATE TABLE "log" (
    id SERIAL PRIMARY KEY,
    "date" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    user_name varchar(128) NOT NULL,
    DML_type varchar(6) NOT NULL,
    table_name varchar(100) NOT NULL,
    "description" text NOT NULL
);