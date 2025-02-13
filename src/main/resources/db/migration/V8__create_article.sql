CREATE TABLE article (
    id SERIAL PRIMARY KEY,
    "name" varchar(100) NOT NULL,
    brand int NOT NULL,
    category int NOT NULL,
    "description" varchar(1000),
    CONSTRAINT fk_article_brand FOREIGN KEY (brand) REFERENCES brand(id),
    CONSTRAINT fk_article_category FOREIGN KEY (category) REFERENCES category(id)
);