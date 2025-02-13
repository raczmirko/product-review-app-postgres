CREATE TABLE packaging (
    id SERIAL PRIMARY KEY,
    name varchar(100) NOT NULL,
    amount smallint NOT NULL,
    unit_of_measure varchar(100),
    unit_of_measure_name varchar(100),
    size varchar(100),
    CONSTRAINT chk_packaging_amount_non_negative CHECK (amount >= 0),
    CONSTRAINT chk_packaging_unit_and_unit_name_both_present CHECK
        ((unit_of_measure_name IS NULL AND unit_of_measure IS NULL) OR
         (unit_of_measure_name IS NOT NULL AND unit_of_measure IS NOT NULL))
);