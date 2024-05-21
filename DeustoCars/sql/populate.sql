USE deustocarsdb;

-- Insert customer data
INSERT INTO customerjdo (email, name, surname, dateOfBirth) 
VALUES 
    ('test@gmail.com', 'Billy', 'Bob', '2000-01-01'),
    ('test2@gmail.com', 'Johnny', 'Jones', '1995-05-15'),
    ('test3@gmail.com', 'Evelyn', 'Easton', '1988-11-20'),
    ('x@gmail.com', 'xx', 'xxxx', '2002-10-31'),
    ('juan@gmail.com', 'Juan', 'Martinez', '1990-05-25'),
    ('maria@gmail.com', 'Maria', 'Garcia', '1985-09-12'),
    ('pablo@gmail.com', 'Pablo', 'Fernandez', '1982-07-30'),
    ('laura@gmail.com', 'Laura', 'Lopez', '1993-03-18'),
    ('santiago@gmail.com', 'Santiago', 'Rodriguez', '1998-11-05'),
    ('carla@gmail.com', 'Carla', 'Sanchez', '1979-12-24'),
    ('daniel@gmail.com', 'Daniel', 'Moreno', '1995-08-03'),
    ('teresa@gmail.com', 'Teresa', 'Diaz', '1987-06-15'),
    ('javier@gmail.com', 'Javier', 'Alonso', '1980-04-20'),
    ('angela@gmail.com', 'Angela', 'Ruiz', '1991-02-10'),
    ('sergio@gmail.com', 'Sergio', 'Hernandez', '1976-10-28');

-- Insert vehicle data
INSERT INTO vehiclejdo (numberPlate, brand, model, readyToBorrow, onRepair)
VALUES 
    ('9872SLY', 'Toyota', 'Corolla', true, false),
    ('1234QWR', 'Opel', 'Corsa', true, false),
    ('5678BNM', 'Volkswagen', 'Golf', true, false),
    ('123AB', 'Toyota', 'Corolla', true, false),
    ('123ABC', 'Toyota', 'Corolla', true, false),
    ('456DEF', 'Ford', 'Focus', true, false),
    ('789GHI', 'Opel', 'Corsa', true, true),
    ('321JKL', 'Hyundai', 'Elantra', true, false),
    ('456MNO', 'Kia', 'Sportage', true, false),
    ('789PQR', 'Seat', 'Leon', true, false),
    ('987STU', 'Hyundai', 'Tucson', true, false),
    ('654VWX', 'Citroen', 'C3', true, false),
    ('321YZA', 'Kia', 'Sportage', true, false),
    ('123BCD', 'BMW', '3 Series', true, false),
    ('456EFG', 'Mercedes-Benz', 'C-Class', true, false),
    ('789HIJ', 'Audi', 'A4', true, false),
    ('987LMN', 'Mercedes-Benz', 'GLA', true, false),
    ('654OPQ', 'Audi', 'Q5', true, false),
    ('321RST', 'Ford', 'Fiesta', true, false),
    ('123UVW', 'Volkswagen', 'Tiguan', true, false);

-- Insert renting data
INSERT INTO rentingjdo (PK, CUSTOMER_EMAIL_OID, ENDDATE, STARTDATE, VEHICLE_NUMBERPLATE_OID) 
VALUES
    (1, (SELECT email FROM customerjdo WHERE email = 'test@gmail.com'), '2021-10-14', '2021-10-07', (SELECT numberPlate FROM vehiclejdo WHERE numberPlate = '9872SLY')),
    (2, (SELECT email FROM customerjdo WHERE email = 'test2@gmail.com'), '2022-02-22', '2023-02-05', (SELECT numberPlate FROM vehiclejdo WHERE numberPlate = '1234QWR')),
    (3, (SELECT email FROM customerjdo WHERE email = 'test3@gmail.com'), '2022-03-25', '2023-03-11', (SELECT numberPlate FROM vehiclejdo WHERE numberPlate = '5678BNM')),
    (4, (SELECT email FROM customerjdo WHERE email = 'x@gmail.com'), '2022-04-12', '2023-04-03', (SELECT numberPlate FROM vehiclejdo WHERE numberPlate = '123AB')),
    (5, (SELECT email FROM customerjdo WHERE email = 'juan@gmail.com'), '2022-05-20', '2023-05-02', (SELECT numberPlate FROM vehiclejdo WHERE numberPlate = '123ABC')),
    (6, (SELECT email FROM customerjdo WHERE email = 'maria@gmail.com'), '2023-06-23', '2023-06-14', (SELECT numberPlate FROM vehiclejdo WHERE numberPlate = '456DEF')),
    (7, (SELECT email FROM customerjdo WHERE email = 'pablo@gmail.com'), '2023-07-15', '2023-07-08', (SELECT numberPlate FROM vehiclejdo WHERE numberPlate = '789GHI')),
    (8, (SELECT email FROM customerjdo WHERE email = 'laura@gmail.com'), '2023-08-18', '2023-08-09', (SELECT numberPlate FROM vehiclejdo WHERE numberPlate = '321JKL')),
    (9, (SELECT email FROM customerjdo WHERE email = 'santiago@gmail.com'), '2023-09-22', '2023-09-12', (SELECT numberPlate FROM vehiclejdo WHERE numberPlate = '456MNO')),
    (10, (SELECT email FROM customerjdo WHERE email = 'carla@gmail.com'), '2023-10-25', '2023-10-15', (SELECT numberPlate FROM vehiclejdo WHERE numberPlate = '789PQR')),
    (11, (SELECT email FROM customerjdo WHERE email = 'daniel@gmail.com'), '2023-11-20', '2023-11-03', (SELECT numberPlate FROM vehiclejdo WHERE numberPlate = '987STU')),
    (12, (SELECT email FROM customerjdo WHERE email = 'teresa@gmail.com'), '2023-12-28', '2023-12-17', (SELECT numberPlate FROM vehiclejdo WHERE numberPlate = '654VWX')),
    (13, (SELECT email FROM customerjdo WHERE email = 'javier@gmail.com'), '2024-01-23', '2024-01-10', (SELECT numberPlate FROM vehiclejdo WHERE numberPlate = '321YZA')),
    (14, (SELECT email FROM customerjdo WHERE email = 'angela@gmail.com'), '2024-02-28', '2024-02-17', (SELECT numberPlate FROM vehiclejdo WHERE numberPlate = '123BCD')),
    (15, (SELECT email FROM customerjdo WHERE email = 'sergio@gmail.com'), '2024-03-20', '2024-03-10', (SELECT numberPlate FROM vehiclejdo WHERE numberPlate = '456EFG')),
    (16, (SELECT email FROM customerjdo WHERE email = 'test@gmail.com'), '2024-04-22', '2024-04-15', (SELECT numberPlate FROM vehiclejdo WHERE numberPlate = '789GHI')),
    (17, (SELECT email FROM customerjdo WHERE email = 'test2@gmail.com'), '2024-05-27', '2024-05-10', (SELECT numberPlate FROM vehiclejdo WHERE numberPlate = '987STU')),
    (18, (SELECT email FROM customerjdo WHERE email = 'test3@gmail.com'), '2024-06-25', '2024-06-13', (SELECT numberPlate FROM vehiclejdo WHERE numberPlate = '654VWX')),
    (19, (SELECT email FROM customerjdo WHERE email = 'x@gmail.com'), '2024-07-28', '2024-07-19', (SELECT numberPlate FROM vehiclejdo WHERE numberPlate = '321YZA')),
    (20, (SELECT email FROM customerjdo WHERE email = 'juan@gmail.com'), '2024-08-30', '2024-08-22', (SELECT numberPlate FROM vehiclejdo WHERE numberPlate = '123BCD')),
    (21, (SELECT email FROM customerjdo WHERE email = 'maria@gmail.com'), '2024-09-28', '2024-09-17', (SELECT numberPlate FROM vehiclejdo WHERE numberPlate = '9872SLY'));

    -- Insert user data
    INSERT INTO user (login, password, isAdmin) 
    VALUES 
        ('user@user.com', 'user', false),
        ('admin@admin.com', 'admin', true);
