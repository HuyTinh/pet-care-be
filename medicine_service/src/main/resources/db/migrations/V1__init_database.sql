CREATE TABLE calculation_units
(
    id     BIGINT AUTO_INCREMENT NOT NULL,
    name   VARCHAR(255) NULL,
    status BIT(1)       NULL,
    CONSTRAINT pk_calculation_units PRIMARY KEY (id)
);

CREATE TABLE locations
(
    id              BIGINT AUTO_INCREMENT NOT NULL,
    area            VARCHAR(255) NULL,
    row_location    INT          NULL,
    column_location INT          NULL,
    status          BIT(1)       NULL,
    CONSTRAINT pk_locations PRIMARY KEY (id)
);

CREATE TABLE manufactures
(
    id     BIGINT AUTO_INCREMENT NOT NULL,
    name   VARCHAR(255) NULL,
    status BIT(1)       NULL,
    CONSTRAINT pk_manufactures PRIMARY KEY (id)
);

CREATE TABLE medicines
(
    id                 BIGINT AUTO_INCREMENT NOT NULL,
    name               VARCHAR(255) NULL,
    manufacturing_date datetime     NULL,
    expiry_date        datetime     NULL,
    quantity           INT          NULL,
    price DOUBLE NULL,
    note               VARCHAR(255) NULL,
    status             BIT(1)       NULL,
    CONSTRAINT pk_medicines PRIMARY KEY (id)
);

CREATE TABLE medicines_calculation_units
(
    calculation_units_id BIGINT NOT NULL,
    medicines_id         BIGINT NOT NULL,
    CONSTRAINT pk_medicines_calculationunits PRIMARY KEY (calculation_units_id, medicines_id)
);

CREATE TABLE medicines_locations
(
    locations_id BIGINT NOT NULL,
    medicines_id BIGINT NOT NULL,
    CONSTRAINT pk_medicines_locations PRIMARY KEY (locations_id, medicines_id)
);

CREATE TABLE medicines_manufactures
(
    manufactures_id BIGINT NOT NULL,
    medicines_id    BIGINT NOT NULL,
    CONSTRAINT pk_medicines_manufactures PRIMARY KEY (manufactures_id, medicines_id)
);

ALTER TABLE medicines_calculation_units
    ADD CONSTRAINT fk_medcaluni_on_calculation_unit FOREIGN KEY (calculation_units_id) REFERENCES calculation_units (id);

ALTER TABLE medicines_calculation_units
    ADD CONSTRAINT fk_medcaluni_on_medicine FOREIGN KEY (medicines_id) REFERENCES medicines (id);

ALTER TABLE medicines_locations
    ADD CONSTRAINT fk_medloc_on_location FOREIGN KEY (locations_id) REFERENCES locations (id);

ALTER TABLE medicines_locations
    ADD CONSTRAINT fk_medloc_on_medicine FOREIGN KEY (medicines_id) REFERENCES medicines (id);

ALTER TABLE medicines_manufactures
    ADD CONSTRAINT fk_medman_on_manufacture FOREIGN KEY (manufactures_id) REFERENCES manufactures (id);

ALTER TABLE medicines_manufactures
    ADD CONSTRAINT fk_medman_on_medicine FOREIGN KEY (medicines_id) REFERENCES medicines (id);