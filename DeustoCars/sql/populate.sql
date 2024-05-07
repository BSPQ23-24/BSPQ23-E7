USE deustoCarsDB;

-- Insert customer data
INSERT INTO customers (email, name, surname, birth_date) 
VALUES 
    ('test@gmail.com', 'Billy', 'Bob', '2000-01-01'),
    ('test2@gmail.com', 'Johnny', 'Jones', '1995-05-15'),
    ('test3@gmail.com', 'Evelyn', 'Easton', '1988-11-20'),
    ('x@gmail.com', 'xx', 'xxxx', '2002-10-31');

-- Insert vehicle data
INSERT INTO vehicles (number_plate, brand, model, ready_to_borrow, on_repair)
VALUES 
    ('9872SLY', 'Toyota', 'Corolla', true, false),
    ('1234QWR', 'Opel', 'Corsa', true, false),
    ('5678BNM', 'Volkswagen', 'Golf', true, false),
    ('123AB', 'Toyota', 'Corolla', true, false),
    ('123ABC', 'Toyota', 'Corolla', true, false);