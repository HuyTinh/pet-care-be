INSERT INTO service_types (name, status) VALUES
                                             ('Grooming', 1),
                                             ('Vaccination', 1),
                                             ('Checkup', 1),
                                             ('Training', 1),
                                             ('Boarding', 1);

INSERT INTO services (name, price, service_type_id, status) VALUES
                                                                ('Full Grooming', 50.0, 1, 1),
                                                                ('Vaccination - Dog', 25.0, 2, 1),
                                                                ('Vaccination - Cat', 25.0, 2, 1),
                                                                ('Annual Checkup', 45.0, 3, 1),
                                                                ('Basic Training', 30.0, 4, 1);

INSERT INTO appointments (appointment_date, appointment_hour, customer_id, status, status_accept) VALUES
('2024-10-01', '10:00:00', 1, 1, 'Pending'),
('2024-10-02', '11:30:00', 2, 1, 'Accepted'),
('2024-10-03', '09:00:00', 3, 1, 'Rejected'),
                                                                                                      ('2024-10-04', '14:00:00', 1, 1, 'Pending'),
                                                                                                      ('2024-10-05', '15:30:00', 4, 1, 'Accepted');
INSERT INTO appointment_services (appointment_id, services_id) VALUES
                                                                   (1, 1),  -- Full Grooming for Appointment 1
                                                                   (2, 2),  -- Vaccination - Dog for Appointment 2
                                                                   (3, 3)  -- Vaccination - Cat for Appointment 3


