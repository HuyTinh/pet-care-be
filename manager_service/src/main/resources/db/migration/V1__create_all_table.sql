CREATE TABLE accounts(
     id int PRIMARY KEY AUTO_INCREMENT,
     email varchar(255) NOT NULL UNIQUE,
     password varchar(255) NOT NULL,
     status bit NOT NULL
);
-- bảng 2
CREATE TABLE roles(
    id int PRIMARY KEY AUTO_INCREMENT,
    name varchar(255) NOT NULL,
    status bit NOT NULL
);
-- bảng 3
CREATE TABLE profiles(
     id int PRIMARY KEY AUTO_INCREMENT,
     last_name varchar(255) NOT NULL,
     first_name varchar(255) NOT NULL,
     role_id int NOT NULL,
     account_id int NOT NULL,
     FOREIGN KEY (role_id) REFERENCES Roles(id) ON DELETE CASCADE ON UPDATE CASCADE,
     FOREIGN KEY (account_id) REFERENCES Accounts(id) ON DELETE CASCADE ON UPDATE CASCADE
);
-- bảng 4
CREATE TABLE customers(
    id int PRIMARY KEY AUTO_INCREMENT,
    last_name varchar(255) NOT NULL,
    first_name varchar(255) NOT NULL,
    phone_number varchar(255) NOT NULL,
    account_id int NULL,
    status bit NOT NULL,
    FOREIGN KEY (account_id) REFERENCES Accounts(id) ON DELETE CASCADE ON UPDATE CASCADE
);
-- bảng 5
CREATE TABLE species(
   id int PRIMARY KEY AUTO_INCREMENT,
   name varchar(255) NOT NULL
);
-- bảng 6
CREATE TABLE pets(
    id int PRIMARY KEY AUTO_INCREMENT,
    name varchar(255) NOT NULL,
    age varchar(60) NOT NULL,
    weight decimal(5,2) NOT NULL,
    note text NULL, -- text vì rộng hơn varchar(255)
    customer_id int NOT NULL,
    species_id int NOT NULL,
    status bit NOT NULL,
                     FOREIGN KEY (customer_id) REFERENCES Customers(id) ON DELETE CASCADE ON UPDATE CASCADE,
                     FOREIGN KEY (species_id) REFERENCES Species(id) ON DELETE CASCADE ON UPDATE CASCADE
);
-- bảng 7
CREATE TABLE appointments(
                             id int PRIMARY KEY AUTO_INCREMENT,
                             appointment_date date NOT NULL,
                             appointment_hour time NOT NULL,
                             customer_id int NOT NULL,
                             status bit NOT NULL,
                             status_accept varchar(100) NOT NULL,
                             FOREIGN KEY (customer_id) REFERENCES Customers(id) ON DELETE CASCADE ON UPDATE CASCADE
);



-- bảng 11
CREATE TABLE medicines(
                          id int PRIMARY KEY AUTO_INCREMENT,
                          name varchar(255) NOT NULL


);
-- bảng 12
CREATE TABLE prescriptions(
                              id int PRIMARY KEY AUTO_INCREMENT,
                              create_date date NOT NULL,
                              pet_id int NOT NULL,
                              profile_id int NOT NULL,
                              status bit NOT NULL,
                              note text NULL,
                              FOREIGN KEY (pet_id) REFERENCES Pets(id) ON DELETE CASCADE ON UPDATE CASCADE,
                              FOREIGN KEY (profile_id) REFERENCES Profiles(id) ON DELETE CASCADE ON UPDATE CASCADE
);
-- bảng 13
CREATE TABLE prescription_details(
                                     id int PRIMARY KEY AUTO_INCREMENT,
                                     medicine_id int NOT NULL,
                                     prescription_id int NOT NULL,
                                     note text NOT NULL,
                                     quantity int NOT NULL,
                                     FOREIGN KEY (medicine_id) REFERENCES Medicines(id) ON DELETE CASCADE ON UPDATE CASCADE,
                                     FOREIGN KEY (prescription_id) REFERENCES Prescriptions(id) ON DELETE CASCADE ON UPDATE CASCADE
);
-- bảng 14
CREATE TABLE service_types(
                              id int PRIMARY KEY AUTO_INCREMENT,
                              name varchar(255) NOT NULL,
                              status bit NOT NULL
);
-- bảng 15
CREATE TABLE services(
                         id int PRIMARY KEY AUTO_INCREMENT,
                         name varchar(255) NOT NULL,
                         price double NOT NULL,
                         service_type_id int NOT NULL,
                         status bit NOT NULL,
                         FOREIGN KEY (service_type_id) REFERENCES Service_types(id) ON DELETE CASCADE ON UPDATE CASCADE
);
-- bảng 16
CREATE TABLE payments (
                          id int PRIMARY KEY AUTO_INCREMENT,
                          name varchar(255) NOT NULL,
                          status bit NOT NULL
);
-- bảng 17
CREATE TABLE invoices(
                         id int PRIMARY KEY AUTO_INCREMENT,
                         create_date date NOT NULL,
                         pet_id int NOT NULL,
                         payment_id int NOT NULL,
                         note text NULL,
                         total double NOT NULL,
                         status bit NOT NULL,
                         payment_status varchar(255) NOT NULL,
                         FOREIGN KEY (pet_id) REFERENCES Pets(id) ON DELETE CASCADE ON UPDATE CASCADE,
                         FOREIGN KEY (payment_id) REFERENCES Payments(id) ON DELETE CASCADE ON UPDATE CASCADE
);
-- bảng 18
CREATE TABLE invoice_service_details(
                                        id int PRIMARY KEY AUTO_INCREMENT,
                                        discount double NULL,
                                        price double NOT NULL,
                                        service_id int NOT NULL,
                                        invoice_id int NOT NULL,
                                        note text NULL,
                                        FOREIGN KEY (service_id) REFERENCES Services(id) ON DELETE CASCADE ON UPDATE CASCADE,
                                        FOREIGN KEY (invoice_id) REFERENCES Invoices(id) ON DELETE CASCADE ON UPDATE CASCADE
);
-- bảng 19
CREATE TABLE invoice_medicine_details(
                                         id int PRIMARY KEY AUTO_INCREMENT,
                                         quantity int NOT NULL,
                                         price double NOT NULL,
                                         medicine_id int NOT NULL,
                                         invoice_id int NOT NULL,
                                         note text NULL,
                                         FOREIGN KEY (medicine_id) REFERENCES Medicines(id) ON DELETE CASCADE ON UPDATE CASCADE,
                                         FOREIGN KEY (invoice_id) REFERENCES Invoices(id) ON DELETE CASCADE ON UPDATE CASCADE
);
-- bảng 20
CREATE TABLE appointment_Services(
                                     id int PRIMARY KEY AUTO_INCREMENT,
                                     appointment_id int NOT NULL,
                                     services_id int NOT NULL,
                                     FOREIGN KEY (appointment_id) REFERENCES Appointments(id) ON DELETE CASCADE ON UPDATE CASCADE,
                                     FOREIGN KEY (services_id) REFERENCES Services(id) ON DELETE CASCADE ON UPDATE CASCADE
);