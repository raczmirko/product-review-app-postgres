CREATE TABLE product_image (
    id SERIAL PRIMARY KEY,
    product int NOT NULL,
    "image" bytea NOT NULL,
    CONSTRAINT fk_product_image_product FOREIGN KEY (product) REFERENCES product(id)
);