INSERT INTO clients (id, name, surname, version) VALUES (1, 'John', 'Doe', 0);
INSERT INTO clients (id, name, surname, version) VALUES (2, 'Anna', 'Smith', 0);
INSERT INTO clients (id, name, surname, version) VALUES (3, 'Robert', 'Johnson', 0);
INSERT INTO clients (id, name, surname, version) VALUES (4, 'Emily', 'Davis', 0);
INSERT INTO clients (id, name, surname, version) VALUES (5, 'Michael', 'Brown', 0);

INSERT INTO restaurant (id, name, officeAddress, version) VALUES (501, 'Pasta Palace', '123 Main St', 0);
INSERT INTO restaurant (id, name, officeAddress, version) VALUES (502, 'Burger Barn', '456 Elm St', 0);
INSERT INTO restaurant (id, name, officeAddress, version) VALUES (503, 'Pizza Place', '789 Maple Ave', 0);

INSERT INTO worker (id, name, surname, id_card, version) VALUES (201, 'Alice', 'Walker', NULL, 0);
INSERT INTO worker (id, name, surname, id_card, version) VALUES (202, 'Bob', 'Turner', NULL, 0);
INSERT INTO worker (id, name, surname, id_card, version) VALUES (203, 'Charlie', 'Lee', NULL, 0);
INSERT INTO worker (id, name, surname, id_card, version) VALUES (401, 'David', 'Brown', NULL, 0);
INSERT INTO worker (id, name, surname, id_card, version) VALUES (402, 'Eva', 'Green', NULL, 0);
INSERT INTO worker (id, name, surname, id_card, version) VALUES (403, 'Frank', 'White', NULL, 0);
INSERT INTO worker (id, name, surname, id_card, version) VALUES (404, 'Grace', 'Black', NULL, 0);

INSERT INTO orders (orderId, client_id, worker_id, price, startTime, endTime, active, destination, version) VALUES (1001, 1, 201, 49.99, '2025-10-09 12:00:00', '2025-10-09 12:45:00', TRUE, 'Central Park', 0);
INSERT INTO food_order (orderId, restaurant_id, restaurantAddress) VALUES (1001, 501, '123 Main St');

INSERT INTO orders (orderId, client_id, worker_id, price, startTime, endTime, active, destination, version) VALUES (1004, 2, 202, 29.99, '2025-10-09 10:00:00', '2025-10-09 10:30:00', TRUE, 'Times Square', 0);
INSERT INTO food_order (orderId, restaurant_id, restaurantAddress) VALUES (1004, 502, '456 Elm St');

INSERT INTO orders (orderId, client_id, worker_id, price, startTime, endTime, active, destination, version) VALUES (1005, 3, 203, 34.50, '2025-10-09 18:15:00', '2025-10-09 18:45:00', FALSE, 'Brooklyn Bridge', 0);
INSERT INTO food_order (orderId, restaurant_id, restaurantAddress) VALUES (1005, 503, '789 Maple Ave');

INSERT INTO orders (orderId, client_id, worker_id, price, startTime, endTime, active, destination, version) VALUES (1003, 1, 401, 79.00, '2025-10-09 14:00:00', '2025-10-09 14:30:00', FALSE, 'Downtown Plaza', 0);
INSERT INTO taxi_order (orderId, type, startLocation, licensePlate) VALUES (1003, 'PREMIUM', 'North Square', 'ABC-1234');

INSERT INTO orders (orderId, client_id, worker_id, price, startTime, endTime, active, destination, version) VALUES (1008, 5, 402, 59.00, '2025-10-09 11:30:00', '2025-10-09 12:05:00', TRUE, 'Union Station', 0);
INSERT INTO taxi_order (orderId, type, startLocation, licensePlate) VALUES (1008, 'COMFORT', 'East Village', 'XYZ-9876');

INSERT INTO orders (orderId, client_id, worker_id, price, startTime, endTime, active, destination, version) VALUES (1009, 3, 403, 99.00, '2025-10-09 19:00:00', '2025-10-09 19:40:00', TRUE, 'Airport Terminal B', 0);
INSERT INTO taxi_order (orderId, type, startLocation, licensePlate) VALUES (1009, 'PREMIUM', 'City Hall', 'JKL-5678');

INSERT INTO orders (orderId, client_id, worker_id, price, startTime, endTime, active, destination, version) VALUES (1010, 4, 404, 45.00, '2025-10-09 08:30:00', '2025-10-09 09:00:00', FALSE, 'SoHo', 0);
INSERT INTO taxi_order (orderId, type, startLocation, licensePlate) VALUES (1010, 'NORMAL', 'Chelsea Market', 'MNO-2222');