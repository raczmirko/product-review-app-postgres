CREATE TABLE brand (
    id SERIAL PRIMARY KEY,
    "name" varchar(100) NOT NULL,
    country char(3) NOT NULL,
    "description" varchar(1000),
    CONSTRAINT fk_brand_country FOREIGN KEY (country) REFERENCES country(country_code),
    CONSTRAINT uq_brand_name UNIQUE ("name")
);