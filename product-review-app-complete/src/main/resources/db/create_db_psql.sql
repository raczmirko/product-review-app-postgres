CREATE TABLE "role" (
    "name" varchar(100),
    CONSTRAINT pk_role_name PRIMARY KEY("name")
);

CREATE TABLE "user" (
    id SERIAL PRIMARY KEY,
    username varchar(100) NOT NULL,
    "password" varchar(1000) NOT NULL,
    country char(3) NOT NULL,
    registered timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "role" varchar(100) NOT NULL,
    active boolean NOT NULL,
    CONSTRAINT fk_user_country FOREIGN KEY (country) REFERENCES country(country_code),
    CONSTRAINT fk_user_role FOREIGN KEY ("role") REFERENCES "role"("name"),
    CONSTRAINT uq_user_username UNIQUE (username),
    CONSTRAINT chk_user_date_is_recent CHECK(
        registered <= CURRENT_TIMESTAMP AND
        registered >= CURRENT_TIMESTAMP - INTERVAL '1 minute'
    )
);

CREATE TABLE brand (
    id SERIAL PRIMARY KEY,
    "name" varchar(100) NOT NULL,
    country char(3) NOT NULL,
    "description" varchar(1000),
    CONSTRAINT fk_brand_country FOREIGN KEY (country) REFERENCES country(country_code),
    CONSTRAINT uq_brand_name UNIQUE ("name")
);

CREATE TABLE category (
    id SERIAL PRIMARY KEY,
    "name" varchar(100) NOT NULL,
    parent_category int,
    "description" varchar(1000),
    CONSTRAINT fk_category_category FOREIGN KEY (parent_category) REFERENCES category(id),
    CONSTRAINT uq_category_name UNIQUE ("name")
);

CREATE TABLE characteristic (
    id SERIAL PRIMARY KEY,
    "name" varchar(100) NOT NULL,
    unit_of_measure_name varchar(100),
    unit_of_measure varchar(100),
    "description" varchar(100),
    CONSTRAINT uq_characteristic_unit_of_measure UNIQUE ("name", unit_of_measure),
    CONSTRAINT chk_characteristic_unit_and_unit_name_both_present CHECK
        ((unit_of_measure_name IS NULL AND unit_of_measure IS NULL) OR
         (unit_of_measure_name IS NOT NULL AND unit_of_measure IS NOT NULL))
);

CREATE TABLE category_x_characteristic (
    category int,
    characteristic int,
    CONSTRAINT pk_category_x_characteristic PRIMARY KEY (category, characteristic),
    CONSTRAINT fk_category_x_characteristic_category FOREIGN KEY (category) REFERENCES category(id),
    CONSTRAINT fk_category_x_characteristic_characteristic FOREIGN KEY (characteristic) REFERENCES characteristic(id)
);

CREATE TABLE article (
    id SERIAL PRIMARY KEY,
    "name" varchar(100) NOT NULL,
    brand int NOT NULL,
    category int NOT NULL,
    "description" varchar(1000),
    CONSTRAINT fk_article_brand FOREIGN KEY (brand) REFERENCES brand(id),
    CONSTRAINT fk_article_category FOREIGN KEY (category) REFERENCES category(id)
);

CREATE TABLE packaging (
    id SERIAL PRIMARY KEY,
    amount smallint NOT NULL,
    unit_of_measure varchar(100),
    unit_of_measure_name varchar(100),
    size varchar(100),
    CONSTRAINT chk_packaging_amount_non_negative CHECK (amount >= 0),
    CONSTRAINT chk_packaging_unit_and_unit_name_both_present CHECK
        ((unit_of_measure_name IS NULL AND unit_of_measure IS NULL) OR
         (unit_of_measure_name IS NOT NULL AND unit_of_measure IS NOT NULL))
);

CREATE TABLE product (
    id SERIAL PRIMARY KEY,
    article int NOT NULL,
    packaging int NOT NULL,
    CONSTRAINT fk_product_article FOREIGN KEY (article) REFERENCES article(id),
    CONSTRAINT fk_product_packaging FOREIGN KEY (packaging) REFERENCES packaging(id)
);

CREATE TABLE product_characteristic_value (
    product int,
    characteristic int,
    "value" varchar(100) NOT NULL,
    CONSTRAINT pk_product_characteristic_value PRIMARY KEY (product, characteristic),
    CONSTRAINT fk_product_characteristic_value_article FOREIGN KEY (product) REFERENCES product(id)
);

CREATE TABLE product_image (
    id SERIAL PRIMARY KEY,
    product int NOT NULL,
    "image" bytea NOT NULL,
    CONSTRAINT fk_product_image_product FOREIGN KEY (product) REFERENCES product(id)
);

CREATE TABLE aspect (
    id SERIAL PRIMARY KEY,
    "name" varchar(100) NOT NULL,
    question varchar(100) NOT NULL,
    category int NOT NULL,
    CONSTRAINT fk_aspect_category FOREIGN KEY (category) REFERENCES category(id)
);

CREATE TABLE review_head (
    "user" int,
    product int,
    "date" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    overall_review varchar(1000) NOT NULL,
    recommended boolean NOT NULL,
    purchase_country char(3) NOT NULL,
    value_for_price smallint NOT NULL,
    CONSTRAINT pk_review_head PRIMARY KEY ("user", product),
    CONSTRAINT fk_review_head_user FOREIGN KEY ("user") REFERENCES "user"(id),
    CONSTRAINT fk_review_head_product FOREIGN KEY (product) REFERENCES product(id),
    CONSTRAINT fk_review_head_country FOREIGN KEY (purchase_country) REFERENCES country(country_code),
    CONSTRAINT chk_value_for_price_between_1_and_5 CHECK(
        value_for_price >= 1 AND value_for_price <= 5
    ),
    CONSTRAINT chk_review_head_date_is_recent CHECK(
        "date" <= CURRENT_TIMESTAMP AND
        "date" >= CURRENT_TIMESTAMP - INTERVAL '1 minute'
    )
);

CREATE TABLE review_body (
    "user" int,
    product int,
    aspect int,
    score smallint NOT NULL,
    CONSTRAINT pk_review_body PRIMARY KEY ("user", product, aspect),
    CONSTRAINT fk_review_body_review_head FOREIGN KEY ("user", product) REFERENCES review_head("user", product),
    CONSTRAINT chk_review_body_score_between_1_and_5 CHECK (
        score >= 1 AND score <= 5
    )
);

CREATE TABLE "log" (
    id SERIAL PRIMARY KEY,
    "date" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "user" varchar(128) NOT NULL DEFAULT CURRENT_USER,
    DML_type varchar(6) NOT NULL,
    "table" varchar(100) NOT NULL,
    "description" text NOT NULL
);
