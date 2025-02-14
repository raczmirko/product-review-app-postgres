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