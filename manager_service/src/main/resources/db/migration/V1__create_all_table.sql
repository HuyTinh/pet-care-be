CREATE TABLE accounts(
     id bigint PRIMARY KEY AUTO_INCREMENT,
     email varchar(255) NOT NULL UNIQUE,
     password varchar(255) NOT NULL,
     status bit NOT NULL
);
-- bảng 2
CREATE TABLE roles(
    id bigint PRIMARY KEY AUTO_INCREMENT,
    name varchar(255) NOT NULL,
    status bit NOT NULL
);
-- bảng 3
CREATE TABLE profiles(
     id bigint PRIMARY KEY AUTO_INCREMENT,
     last_name varchar(255) NOT NULL,
     first_name varchar(255) NOT NULL,
     role_id bigint NOT NULL,
     account_id bigint NOT NULL,
     FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE ON UPDATE CASCADE,
     FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE CASCADE ON UPDATE CASCADE
);
-- bảng 4
CREATE TABLE customers(
    id bigint PRIMARY KEY AUTO_INCREMENT,
    last_name varchar(255) NOT NULL,
    first_name varchar(255) NOT NULL,
    phone_number varchar(255) NOT NULL,
    account_id bigint NULL,
    status bit NOT NULL,
    FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE CASCADE ON UPDATE CASCADE
);
-- bảng 5
CREATE TABLE species(
   id bigint PRIMARY KEY AUTO_INCREMENT,
   name varchar(255) NOT NULL
);
-- bảng 6
CREATE TABLE pets(
    id bigint PRIMARY KEY AUTO_INCREMENT,
    name varchar(255) NOT NULL,
    age varchar(60) NOT NULL,
    weight decimal(5,2) NOT NULL,
    note text NULL, -- text vì rộng hơn varchar(255)
    customer_id bigint NOT NULL,
    species_id bigint NOT NULL,
    status bit NOT NULL,
                     FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE ON UPDATE CASCADE,
                     FOREIGN KEY (species_id) REFERENCES species(id) ON DELETE CASCADE ON UPDATE CASCADE
);
-- bảng 7
CREATE TABLE appointments(
                             id bigint PRIMARY KEY AUTO_INCREMENT,
                             appointment_date date NOT NULL,
                             appointment_hour time NOT NULL,
                             customer_id bigint NOT NULL,
                             status bit NOT NULL,
                             status_accept varchar(100) NOT NULL,
                             FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE ON UPDATE CASCADE
);



-- bảng 11
CREATE TABLE medicines(
                          id bigint PRIMARY KEY AUTO_INCREMENT,
                          name varchar(255) NOT NULL


);
-- bảng 12
CREATE TABLE prescriptions(
                              id bigint PRIMARY KEY AUTO_INCREMENT,
                              create_date date NOT NULL,
                              pet_id bigint NOT NULL,
                              profile_id bigint NOT NULL,
                              status bit NOT NULL,
                              note text NULL,
                              FOREIGN KEY (pet_id) REFERENCES pets(id) ON DELETE CASCADE ON UPDATE CASCADE,
                              FOREIGN KEY (profile_id) REFERENCES profiles(id) ON DELETE CASCADE ON UPDATE CASCADE
);
-- bảng 13
CREATE TABLE prescription_details(
                                     id bigint PRIMARY KEY AUTO_INCREMENT,
                                     medicine_id bigint NOT NULL,
                                     prescription_id bigint NOT NULL,
                                     note text NOT NULL,
                                     quantity int NOT NULL,
                                     FOREIGN KEY (medicine_id) REFERENCES medicines(id) ON DELETE CASCADE ON UPDATE CASCADE,
                                     FOREIGN KEY (prescription_id) REFERENCES prescriptions(id) ON DELETE CASCADE ON UPDATE CASCADE
);
-- bảng 14
CREATE TABLE service_types(
                              id bigint PRIMARY KEY AUTO_INCREMENT,
                              name varchar(255) NOT NULL,
                              status bit NOT NULL
);
-- bảng 15
CREATE TABLE services(
                         id bigint PRIMARY KEY AUTO_INCREMENT,
                         name varchar(255) NOT NULL,
                         price double NOT NULL,
                         service_type_id bigint NOT NULL,
                         status bit NOT NULL,
                         FOREIGN KEY (service_type_id) REFERENCES service_types(id) ON DELETE CASCADE ON UPDATE CASCADE
);
-- bảng 16
CREATE TABLE payments (
                          id bigint PRIMARY KEY AUTO_INCREMENT,
                          name varchar(255) NOT NULL,
                          status bit NOT NULL
);
-- bảng 17
CREATE TABLE invoices(
                         id bigint PRIMARY KEY AUTO_INCREMENT,
                         create_date date NOT NULL,
                         pet_id bigint NOT NULL,
                         payment_id bigint NOT NULL,
                         note text NULL,
                         total double NOT NULL,
                         status bit NOT NULL,
                         payment_status varchar(255) NOT NULL,
                         FOREIGN KEY (pet_id) REFERENCES pets(id) ON DELETE CASCADE ON UPDATE CASCADE,
                         FOREIGN KEY (payment_id) REFERENCES payments(id) ON DELETE CASCADE ON UPDATE CASCADE
);
-- bảng 18
CREATE TABLE invoice_service_details(
                                        id bigint PRIMARY KEY AUTO_INCREMENT,
                                        discount double NULL,
                                        price double NOT NULL,
                                        service_id bigint NOT NULL,
                                        invoice_id bigint NOT NULL,
                                        note text NULL,
                                        FOREIGN KEY (service_id) REFERENCES services(id) ON DELETE CASCADE ON UPDATE CASCADE,
                                        FOREIGN KEY (invoice_id) REFERENCES invoices(id) ON DELETE CASCADE ON UPDATE CASCADE
);
-- bảng 19
CREATE TABLE invoice_medicine_details(
                                         id bigint PRIMARY KEY AUTO_INCREMENT,
                                         quantity int NOT NULL,
                                         price double NOT NULL,
                                         medicine_id bigint NOT NULL,
                                         invoice_id bigint NOT NULL,
                                         note text NULL,
                                         FOREIGN KEY (medicine_id) REFERENCES medicines(id) ON DELETE CASCADE ON UPDATE CASCADE,
                                         FOREIGN KEY (invoice_id) REFERENCES invoices(id) ON DELETE CASCADE ON UPDATE CASCADE
);
-- bảng 20
CREATE TABLE appointment_services(
                                     id bigint PRIMARY KEY AUTO_INCREMENT,
                                     appointment_id bigint NOT NULL,
                                     services_id bigint NOT NULL,
                                     FOREIGN KEY (appointment_id) REFERENCES appointments(id) ON DELETE CASCADE ON UPDATE CASCADE,
                                     FOREIGN KEY (services_id) REFERENCES services(id) ON DELETE CASCADE ON UPDATE CASCADE
);