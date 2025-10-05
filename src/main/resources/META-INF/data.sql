-- Drop the table if it already exists
DROP TABLE IF EXISTS clients;

-- Create the table
CREATE TABLE clients (
                         id SERIAL PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         surname VARCHAR(255) NOT NULL
);

-- Insert sample data
INSERT INTO clients (name, surname) VALUES
                                        ('Alice', 'Nowak'),
                                        ('Bob', 'Kowalski'),
                                        ('Charlie', 'Wiśniewski'),
                                        ('Diana', 'Zielińska'),
                                        ('Ethan', 'Lewandowski');
