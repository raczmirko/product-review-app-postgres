CREATE TABLE aspect (
    id SERIAL PRIMARY KEY,
    "name" varchar(100) NOT NULL,
    question varchar(100) NOT NULL,
    category int NOT NULL,
    CONSTRAINT fk_aspect_category FOREIGN KEY (category) REFERENCES category(id)
);