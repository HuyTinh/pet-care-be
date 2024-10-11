CREATE TABLE appointments
(
    id               BIGINT AUTO_INCREMENT NOT NULL,
    account_id       BIGINT       NULL,
    first_name       VARCHAR(255) NULL,
    last_name        VARCHAR(255) NULL,
    email            VARCHAR(255) NULL,
    phone_number     VARCHAR(255) NULL,
    appointment_date date         NULL,
    appointment_time time         NULL,
    status           VARCHAR(255) NULL,
    created_at       datetime     NULL,
    updated_at       datetime     NULL,
    CONSTRAINT pk_appointments PRIMARY KEY (id)
);

CREATE TABLE appointments_services
(
    appointments_id BIGINT       NOT NULL,
    services_name   VARCHAR(255) NOT NULL,
    CONSTRAINT pk_appointments_services PRIMARY KEY (appointments_id, services_name)
);

CREATE TABLE hospital_services
(
    name          VARCHAR(255) NOT NULL, `
    description ` VARCHAR(255) NULL,
    status        VARCHAR(255) NULL,
    CONSTRAINT pk_hospital_services PRIMARY KEY (name)
);

CREATE TABLE pets
(
    id             BIGINT AUTO_INCREMENT NOT NULL,
    name           VARCHAR(255) NULL,
    age            VARCHAR(255) NULL,
    weight DOUBLE NULL,
    species        VARCHAR(255) NULL,
    appointment_id BIGINT       NULL,
    CONSTRAINT pk_pets PRIMARY KEY (id)
);

CREATE TABLE species
(
    name   VARCHAR(255) NOT NULL,
    status BIT(1)       NULL,
    CONSTRAINT pk_species PRIMARY KEY (name)
);

ALTER TABLE pets
    ADD CONSTRAINT FK_PETS_ON_APPOINTMENT FOREIGN KEY (appointment_id) REFERENCES appointments (id);

ALTER TABLE appointments_services
    ADD CONSTRAINT fk_appser_on_appointment FOREIGN KEY (appointments_id) REFERENCES appointments (id);

ALTER TABLE appointments_services
    ADD CONSTRAINT fk_appser_on_hospital_service_entity FOREIGN KEY (services_name) REFERENCES hospital_services (name);

