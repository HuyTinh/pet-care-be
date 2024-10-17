-- INSERT THÔNG TIN CUSTOMERS
INSERT INTO customers (first_name, last_name, phone_number, account_id, status)
VALUES ('Hoang','Tong','018452352',NULL, 1);

INSERT INTO customers (first_name, last_name, phone_number, account_id, status)
VALUES ('Le','Khach','069789865',NULL, 1);

INSERT INTO customers (first_name, last_name, phone_number, account_id, status)
VALUES ('Le','Khach','0654321456',NULL, 1);

INSERT INTO customers (first_name, last_name, phone_number, account_id, status)
VALUES ('Hoang','Tong','0698453211',NULL, 1);

INSERT INTO customers (first_name, last_name, phone_number, account_id, status)
VALUES ('Bao','Tong','0465132451',NULL, 1);

-- INSERT THÔNG TIN SPECIES

insert into species (name) values ('Persian Cat');
insert into species (name) values ('Siamese Cat');
insert into species (name) values ('Maine Coon Cat');
insert into species (name) values ('Scottish Fold Cat');
insert into species (name) values ('British Shorthair Cat');
insert into species (name) values ('Siberian Husky Dog');
insert into species (name) values ('Poodle Dog');
insert into species (name) values ('Golden Retriever Dog');
insert into species (name) values ('German Shepherd Dog');
insert into species (name) values ('Corgi Dog');

-- INSERT THÔNG TIN PET

INSERT INTO pets (name, age, weight, note, customer_id, species_id, status)
VALUES ('Lily','6 months',3.5,NULL,1,1,1);
INSERT INTO pets (name, age, weight, note, customer_id, species_id, status)
VALUES ('Black','1 year 3 months',5.1,NULL,2,2,1);
INSERT INTO pets (name, age, weight, note, customer_id, species_id, status)
VALUES ('Yell','2 years 5 months',10.2,NULL,3,2,1);
INSERT INTO pets (name, age, weight, note, customer_id, species_id, status)
VALUES ('Tom','5 months',3.1,NULL,1,2,1);
INSERT INTO pets (name, age, weight, note, customer_id, species_id, status)
VALUES ('Thomas','2 months',2.1,NULL,4,3,1);

-- INSERT THÔNG TIN ACCOUNT
INSERT INTO accounts (email, password, status) VALUES
                                                   ('user1@example.com', 'password1', 1),
                                                   ('user2@example.com', 'password2', 1),
                                                   ('user3@example.com', 'password3', 1),
                                                   ('user4@example.com', 'password4', 1),
                                                   ('user5@example.com', 'password5', 1);
-- INSERT THÔNG TIN PROFILES
ALTER TABLE profiles
    ADD COLUMN status BIT;

INSERT INTO profiles (last_name, first_name, role_id, account_id, status) VALUES
                                                                              ('Nguyen', 'An', 1, 1,1),
                                                                              ('Tran', 'Binh', 2, 2, 1),
                                                                              ('Le', 'Linh', 1, 3, 1),
                                                                              ('Pham', 'Mai', 3, 4, 1),
                                                                              ('Hoang', 'Hanh', 2, 5, 1);

-- INSERT THÔNG TIN ĐƠN THUỐC
ALTER TABLE prescriptions
    ADD COLUMN disease_name varchar(255);

INSERT INTO prescriptions (create_date, disease_name, pet_id, profile_id, status, note) VALUES
                                                                                            ('2024-01-10', 'Allergy', 1, 1, 1, 'Administer antihistamines daily.'),
                                                                                            ('2024-02-15', 'Fever', 2, 2, 1, NULL),  -- No specific instructions
                                                                                            ('2024-03-20', 'Infection', 3, 3, 0, 'Monitor for any changes in behavior.'),
                                                                                            ('2024-04-05', 'Dental issue', 1, 4, 1, 'Schedule dental cleaning next month.'),
                                                                                            ('2024-05-25', 'Skin rash', 2, 5, 0, 'Apply topical cream twice a day.');

-- INSERT THÔNG TIN CHI TIẾT ĐƠN THUỐC
INSERT INTO prescription_details (medicine_id, prescription_id, note, quantity) VALUES
                                                                                    (1, 1, 'Take 1 pill twice a day.', 6),  -- Medicine ID 1 for Prescription ID 1
                                                                                    (2, 1, 'Take 2 pills once a day.', 10),  -- Medicine ID 2 for Prescription ID 1
                                                                                    (3, 2, 'Apply cream twice a day.', 10),   -- Medicine ID 3 for Prescription ID 2
                                                                                    (1, 2, 'Take 1 pill every 12 hours.', 10), -- Medicine ID 1 for Prescription ID 3
                                                                                    (2, 4, 'Take 1 pill in the morning.', 7);  -- Medicine ID 2 for Prescription ID 4