INSERT INTO tb_user(name, email, password, role) VALUES ('Paulo Manoel', 'paulo@gmail.com', '$2a$10$50LdbNrJl5SLXQbCuZXCmO/47ufF6zU4iwIOCCPb.gBoRSUdXsn2O', 0);

INSERT INTO tb_professional(name, registration_number, email, phone_number, created_at, updated_at) VALUES ('Mauro Silva', 'CRP SP/123456', 'mauro@gmail.com', '91234-1234', '2025-07-13T17:00:00Z', '2025-07-13T17:00:00Z');

INSERT INTO tb_client(name, cpf, birth_date, email, phone_number, appointment_price, appointment_frequency, treatment_start_date, appointment_duration_in_minutes, created_at, updated_at) VALUES ('Lucas Souza', '123.123.123-12', '1999-06-24', 'lucas@gmail.com', '94321-4321', 80.0, 7,'2025-08-01T13:00:00', 60, '2025-07-22T19:00:00Z', '2025-07-22T19:00:00Z');
INSERT INTO tb_client(name, cpf, birth_date, email, phone_number, appointment_price, appointment_frequency, treatment_start_date, appointment_duration_in_minutes, created_at, updated_at) VALUES ('Matheus Amaro', '321.321.321-21', '2001-04-27', 'matheus@gmail.com', '91243-4312', 70.0, 14,'2025-08-01T13:00:00', 30, '2025-07-22T20:00:00Z', '2025-07-22T20:00:00Z');

INSERT INTO tb_emergency_contact(name, email, phone_number, relationship, client_id, created_at, updated_at) VALUES ('Douglas Souza', 'douglas@gmail.com', '93214-3214', 1, 1, '2025-07-22T19:00:00Z', '2025-07-22T19:00:00Z');
INSERT INTO tb_emergency_contact(name, email, phone_number, relationship, client_id, created_at, updated_at) VALUES ('Isabel Amaro', 'isabel@gmail.com', '3214-3214', 0, 2, '2025-07-22T20:00:00Z', '2025-07-22T20:00:00Z');

INSERT INTO tb_appointment(start_time, end_time, appointment_status, price, paid, client_id, created_at, updated_at) VALUES ('2025-08-01T13:00:00', '2025-08-01T14:00:00', 3, 80.0, false, 1, '2025-07-22T19:00:00Z', '2025-07-22T19:00:00Z');
INSERT INTO tb_appointment(start_time, end_time, appointment_status, price, paid, client_id, created_at, updated_at) VALUES ('2025-08-01T16:00:00', '2025-08-01T17:00:00', 3, 85.0, true, 2, '2025-07-22T20:00:00Z', '2025-07-22T20:00:00Z');
