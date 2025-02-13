CREATE TABLE category (
    id SERIAL PRIMARY KEY,
    "name" varchar(100) NOT NULL,
    parent_category int,
    "description" varchar(1000),
    CONSTRAINT fk_category_category FOREIGN KEY (parent_category) REFERENCES category(id),
    CONSTRAINT uq_category_name UNIQUE ("name")
);