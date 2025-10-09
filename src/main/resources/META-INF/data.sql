INSERT INTO clients (id, name, surname, version) VALUES (1, 'John', 'Doe', 0);
INSERT INTO clients (id, name, surname, version) VALUES (2, 'Anna', 'Smith', 0);
INSERT INTO clients (id, name, surname, version) VALUES (3, 'Robert', 'Johnson', 0);
INSERT INTO clients (id, name, surname, version) VALUES (4, 'Emily', 'Davis', 0);
INSERT INTO clients (id, name, surname, version) VALUES (5, 'Michael', 'Brown', 0);

-- food_order
INSERT INTO orders (orderId, client_id, price, startTime, endTime, active, destination) VALUES (1001, 1, 49.99, '2025-10-09 12:00:00', '2025-10-09 12:45:00', TRUE, 'Central Park');
INSERT INTO food_order (orderId, restaurantId, workerId, restaurantAddress) VALUES (1001, 501, 201, '123 Main St');

INSERT INTO orders (orderId, client_id, price, startTime, endTime, active, destination) VALUES (1004, 2, 29.99, '2025-10-09 10:00:00', '2025-10-09 10:30:00', TRUE, 'Times Square');
INSERT INTO food_order (orderId, restaurantId, workerId, restaurantAddress) VALUES (1004, 502, 202, '456 Elm St');

INSERT INTO orders (orderId, client_id, price, startTime, endTime, active, destination) VALUES (1005, 3, 34.50, '2025-10-09 18:15:00', '2025-10-09 18:45:00', FALSE, 'Brooklyn Bridge');
INSERT INTO food_order (orderId, restaurantId, workerId, restaurantAddress) VALUES (1005, 503, 203, '789 Maple Ave');

-- scooter_order
INSERT INTO orders (orderId, client_id, price, startTime, endTime, active, destination) VALUES (1002, 1, 19.50, '2025-10-09 13:00:00', '2025-10-09 13:20:00', TRUE, '5th Avenue');
INSERT INTO scooter_order (orderId, scooterId, startLocation) VALUES (1002, 301, 'East Station');

INSERT INTO orders (orderId, client_id, price, startTime, endTime, active, destination) VALUES (1006, 4, 22.00, '2025-10-09 09:30:00', '2025-10-09 09:55:00', TRUE, 'Harlem');
INSERT INTO scooter_order (orderId, scooterId, startLocation) VALUES (1006, 302, 'West Station');

INSERT INTO orders (orderId, client_id, price, startTime, endTime, active, destination) VALUES (1007, 2, 25.75, '2025-10-09 16:10:00', '2025-10-09 16:40:00', FALSE, 'Madison Ave');
INSERT INTO scooter_order (orderId, scooterId, startLocation) VALUES (1007, 303, 'Battery Park');

-- taxi_order
INSERT INTO orders (orderId, client_id, price, startTime, endTime, active, destination) VALUES (1003, 1, 79.00, '2025-10-09 14:00:00', '2025-10-09 14:30:00', FALSE, 'Downtown Plaza');
INSERT INTO taxi_order (orderId, workerId, type, startLocation, licensePlate) VALUES (1003, 401, 'PREMIUM', 'North Square', 'ABC-1234');

INSERT INTO orders (orderId, client_id, price, startTime, endTime, active, destination) VALUES (1008, 5, 59.00, '2025-10-09 11:30:00', '2025-10-09 12:05:00', TRUE, 'Union Station');
INSERT INTO taxi_order (orderId, workerId, type, startLocation, licensePlate) VALUES (1008, 402, 'COMFORT', 'East Village', 'XYZ-9876');

INSERT INTO orders (orderId, client_id, price, startTime, endTime, active, destination) VALUES (1009, 3, 99.00, '2025-10-09 19:00:00', '2025-10-09 19:40:00', TRUE, 'Airport Terminal B');
INSERT INTO taxi_order (orderId, workerId, type, startLocation, licensePlate) VALUES (1009, 403, 'PREMIUM', 'City Hall', 'JKL-5678');

INSERT INTO orders (orderId, client_id, price, startTime, endTime, active, destination) VALUES (1010, 4, 45.00, '2025-10-09 08:30:00', '2025-10-09 09:00:00', FALSE, 'SoHo');
INSERT INTO taxi_order (orderId, workerId, type, startLocation, licensePlate) VALUES (1010, 404, 'NORMAL', 'Chelsea Market', 'MNO-2222');
