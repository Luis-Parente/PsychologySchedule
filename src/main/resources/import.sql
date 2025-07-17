INSERT INTO tb_professional(name, registration_number, email, phone_number, created_at, updated_at) VALUES ('Mauro Silva', 'CRP SP/123456', 'mauro@gmail.com', '91234-1234', '2025-07-13T17:00:00Z', '2025-07-13T17:00:00Z');

INSERT INTO tb_client(name, cpf, birth_date, email, phone_number, created_at, updated_at) VALUES ('Lucas Souza', '123.123.123-12', '1999-06-24', 'lucas@gmail.com', '94321-4321', '2025-07-15T19:00:00Z', '2025-07-15T19:00:00Z');
INSERT INTO tb_client(name, cpf, birth_date, email, phone_number, created_at, updated_at) VALUES ('Matheus Amaro', '321.321.321-21', '2001-04-27', 'matheus@gmail.com', '91243-4312', '2025-07-14T18:00:00Z', '2025-07-14T18:00:00Z');

INSERT INTO tb_emergency_contact(name, email, phone_number, relationship, client_id, created_at, updated_at) VALUES ('Douglas Souza', 'douglas@gmail.com', '93214-3214', 1, 1, '2025-07-15T19:00:00Z', '2025-07-15T19:00:00Z');
INSERT INTO tb_emergency_contact(name, email, phone_number, relationship, client_id, created_at, updated_at) VALUES ('Isabel Amaro', 'isabel@gmail.com', '3214-3214', 0, 2, '2025-07-14T18:00:00Z', '2025-07-14T18:00:00Z');

INSERT INTO tb_subscription_plan(appointment_price, appointment_frequency, start_date, appointment_duration, client_id, created_at, updated_at) VALUES (100.0, 7, '2025-07-24T15:00:00Z', 3600000, 1, '2025-07-15T19:00:00Z', '2025-07-15T19:00:00Z');
INSERT INTO tb_subscription_plan(appointment_price, appointment_frequency, start_date, appointment_duration, client_id, created_at, updated_at) VALUES (85.0, 14, '2025-07-25T16:00:00Z', 3600000, 2, '2025-07-14T18:00:00Z', '2025-07-14T18:00:00Z');

INSERT INTO tb_appointment(start_time, end_time, appointment_status, price, paid, plan_id, created_at, updated_at) VALUES ('2025-07-24T15:00:00Z', '2025-07-24T16:00:00Z', 3, 100.0, false, 1, '2025-07-15T19:00:00Z', '2025-07-15T19:00:00Z');
INSERT INTO tb_appointment(start_time, end_time, appointment_status, price, paid, plan_id, created_at, updated_at) VALUES ('2025-07-24T16:00:00Z', '2025-07-24T17:00:00Z', 3, 85.0, true, 2, '2025-07-14T18:00:00Z', '2025-07-14T18:00:00Z');