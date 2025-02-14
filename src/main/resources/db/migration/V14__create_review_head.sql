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