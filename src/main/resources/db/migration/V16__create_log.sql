CREATE TABLE "log" (
    id SERIAL PRIMARY KEY,
    "date" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "user" varchar(128) NOT NULL DEFAULT CURRENT_USER,
    DML_type varchar(6) NOT NULL,
    "table" varchar(100) NOT NULL,
    "description" text NOT NULL
);