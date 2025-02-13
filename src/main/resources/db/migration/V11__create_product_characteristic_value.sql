CREATE TABLE product_characteristic_value (
    id SERIAL PRIMARY KEY,
    product int,
    characteristic int,
    "value" varchar(100) NOT NULL,
    CONSTRAINT fk_product_characteristic_value_article FOREIGN KEY (product) REFERENCES product(id),
    CONSTRAINT fk_product_characteristic_value_characteristic FOREIGN KEY (characteristic) REFERENCES characteristic(id),
    CONSTRAINT uq_product_characteristic_value UNIQUE (product, characteristic)
);