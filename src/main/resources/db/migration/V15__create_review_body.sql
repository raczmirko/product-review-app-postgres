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