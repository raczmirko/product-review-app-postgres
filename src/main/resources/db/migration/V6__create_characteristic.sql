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