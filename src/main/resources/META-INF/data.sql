-- Insert sample data
INSERT INTO clients (id, name, surname) VALUES (1, 'John', 'Doe');

-- orders (base table for inheritance)
INSERT INTO orders (orderId, client_id, price, startTime, endTime, active, destination) VALUES (1001, 1, 49.99, '2025-10-09 12:00:00', '2025-10-09 12:45:00', TRUE, 'Central Park');

-- food_order
INSERT INTO food_order (orderId, restaurantId, workerId, restaurantAddress) VALUES (1001, 501, 201, '123 Main St');

-- scooter_order
INSERT INTO orders (orderId, client_id, price, startTime, endTime, active, destination) VALUES (1002, 1, 19.50, '2025-10-09 13:00:00', '2025-10-09 13:20:00', TRUE, '5th Avenue');
INSERT INTO scooter_order (orderId, scooterId, startLocation) VALUES (1002, 301, 'East Station');

-- taxi_order
INSERT INTO orders (orderId, client_id, price, startTime, endTime, active, destination) VALUES (1003, 1, 79.00, '2025-10-09 14:00:00', '2025-10-09 14:30:00', FALSE, 'Downtown Plaza');
INSERT INTO taxi_order (orderId, workerId, type, startLocation, licensePlate) VALUES (1003, 401, 'PREMIUM', 'North Square', 'ABC-1234');
