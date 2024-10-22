INSERT INTO payments (name, status)
VALUES
    ('Cash', 1),
    ('VN Pay', 1);
--
INSERT INTO invoices (create_date, pet_id, payment_id, note, total, status, payment_status)
VALUES
('2024-10-01', 1, 1, 'Invoice for Full Grooming', 100.0, 1, 'Paid'),
('2024-10-01', 2, 1, 'Invoice for Full Grooming', 150.0, 1, 'Paid'),
('2024-10-03', 1, 2, 'Invoice for Vaccination - Dog', 200.0, 1, 'Pending'),
('2024-10-02', 3, 2, 'Invoice for Annual Checkup', 120.0, 1, 'Paid'),
('2024-10-01', 2, 1, 'Invoice for Basic Training', 300.0, 1, 'Unpaid');

--
INSERT INTO invoice_service_details (discount, price, appointment_service_id, invoice_id, note)
VALUES
(10.0, 50.0, 1, 1, 'Full Grooming service'),
(0.0, 50.0, 2, 2, 'Full Grooming service'),
(5.0, 25.0, 3, 3, 'Vaccination - Dog service'),
(0.0, 45.0, 4, 4, 'Annual Checkup service'),
(5.0, 30.0, 5, 5, 'Basic Training service');

--
INSERT INTO invoice_medicine_details (quantity, price, medicine_id, invoice_id, note)
VALUES
(2, 20.0, 1, 1, NULL),
(1, 50.0, 2, 2, NULL),
(3, 15.0, 3, 3, NULL),
(1, 100.0, 4, 4,NULL),
(5, 10.0, 5, 5, NULL);

