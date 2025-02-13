CREATE TABLE product (
    id SERIAL PRIMARY KEY,
    article int NOT NULL,
    packaging int NOT NULL,
    CONSTRAINT fk_product_article FOREIGN KEY (article) REFERENCES article(id),
    CONSTRAINT fk_product_packaging FOREIGN KEY (packaging) REFERENCES packaging(id)
);