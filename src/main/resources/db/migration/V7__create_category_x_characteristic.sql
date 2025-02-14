CREATE TABLE category_x_characteristic (
    category int,
    characteristic int,
    CONSTRAINT pk_category_x_characteristic PRIMARY KEY (category, characteristic),
    CONSTRAINT fk_category_x_characteristic_category FOREIGN KEY (category) REFERENCES category(id),
    CONSTRAINT fk_category_x_characteristic_characteristic FOREIGN KEY (characteristic) REFERENCES characteristic(id)
);