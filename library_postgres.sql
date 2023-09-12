-- Create the 'library' database if it doesn't exist
CREATE DATABASE library;

-- Connect to the 'library' database
\c library;

-- Create the 'books' table
CREATE TABLE books (
  id serial PRIMARY KEY,
  title varchar(255) NOT NULL,
  author varchar(255) NOT NULL,
  ISBN varchar(255) NOT NULL,
  quantity int DEFAULT 1
);

-- Insert data into the 'books' table
INSERT INTO books (title, author, ISBN, quantity)
VALUES
  ('mobydick', 'me', '#3332', 2),
  ('1984', 'goerge', '#3213', 2),
  ('haytam life', 'haytam', '#4111', 0),
  ('قطار الحياة', 'ziani hamid', '#2211', 1),
  ('خواطر', 'ahmed shegiri', '#6231', 2);

-- Create the 'borrow' table
CREATE TABLE borrow (
  id serial PRIMARY KEY,
  book_id int NOT NULL,
  reader_id int NOT NULL,
  borrow_date date NOT NULL,
  return_date date NOT NULL,
  status varchar(255)
);

-- Insert data into the 'borrow' table
INSERT INTO borrow (book_id, reader_id, borrow_date, return_date, status)
VALUES
  (1, 4, '2023-09-04', '2023-09-05', 'lost'),
  (5, 6, '2023-09-07', '2023-09-12', 'ongoing'),
  (5, 6, '2023-09-07', '2023-09-10', 'lost'),
  (2, 4, '2023-09-11', '2023-09-13', 'ongoing');

-- Create a trigger for the 'borrow' table
CREATE OR REPLACE FUNCTION quantity_tr()
RETURNS TRIGGER AS $$
BEGIN
  UPDATE books SET quantity = quantity - 1 WHERE id = NEW.book_id;
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER quantity_tr BEFORE INSERT ON borrow
FOR EACH ROW EXECUTE FUNCTION quantity_tr();

-- Create the 'returns' table
CREATE TABLE returns (
  id serial PRIMARY KEY,
  book_id int NOT NULL,
  reader_id int NOT NULL,
  return_date date NOT NULL
);

-- Insert data into the 'returns' table
INSERT INTO returns (book_id, reader_id, return_date)
VALUES
  (1, 4, '2023-09-05'),
  (1, 4, '2023-09-05'),
  (1, 4, '2023-09-06'),
  (5, 4, '2023-09-06'),
  (1, 4, '2023-09-07'),
  (2, 4, '2023-09-07'),
  (1, 4, '2023-09-11');

-- Create a trigger for the 'returns' table
CREATE OR REPLACE FUNCTION quantity_return_tr()
RETURNS TRIGGER AS $$
BEGIN
  UPDATE books SET quantity = quantity + 1 WHERE id = NEW.book_id;
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER quantity_return_tr BEFORE INSERT ON returns
FOR EACH ROW EXECUTE FUNCTION quantity_return_tr();

-- Create the 'users' table
CREATE TABLE users (
  id serial PRIMARY KEY,
  name varchar(255) NOT NULL,
  email varchar(255) NOT NULL UNIQUE,
  password varchar(255) NOT NULL,
  role int NOT NULL
);

-- Insert data into the 'users' table
INSERT INTO users (name, email, password, role)
VALUES
  ('test', 'admin@gmail.com', 'password', 1),
  ('me', 'me@gmail.com', 'password', 2),
  ('abdo', 'abdo@gmail.com', 'password', 2),
  ('admin2', 'admin2@gmail.com', 'password', 1);
