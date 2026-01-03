-- Sample database schema for OCP JDBC practice

-- Create tables for practice
CREATE TABLE employees (
    employee_id SERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE,
    department VARCHAR(50),
    salary DECIMAL(10, 2),
    hire_date DATE DEFAULT CURRENT_DATE
);

CREATE TABLE departments (
    department_id SERIAL PRIMARY KEY,
    department_name VARCHAR(100) NOT NULL,
    location VARCHAR(100)
);

CREATE TABLE projects (
    project_id SERIAL PRIMARY KEY,
    project_name VARCHAR(100) NOT NULL,
    start_date DATE,
    end_date DATE,
    budget DECIMAL(12, 2)
);

-- Insert sample data
INSERT INTO departments (department_name, location) VALUES
    ('Engineering', 'San Francisco'),
    ('Marketing', 'New York'),
    ('Sales', 'Chicago'),
    ('HR', 'Boston');

INSERT INTO employees (first_name, last_name, email, department, salary, hire_date) VALUES
    ('John', 'Doe', 'john.doe@example.com', 'Engineering', 85000.00, '2022-01-15'),
    ('Jane', 'Smith', 'jane.smith@example.com', 'Marketing', 72000.00, '2022-03-20'),
    ('Bob', 'Johnson', 'bob.johnson@example.com', 'Engineering', 95000.00, '2021-11-10'),
    ('Alice', 'Williams', 'alice.williams@example.com', 'Sales', 68000.00, '2023-02-01'),
    ('Charlie', 'Brown', 'charlie.brown@example.com', 'HR', 65000.00, '2023-05-15');

INSERT INTO projects (project_name, start_date, end_date, budget) VALUES
    ('Website Redesign', '2024-01-01', '2024-06-30', 150000.00),
    ('Mobile App Development', '2024-03-01', '2024-12-31', 300000.00),
    ('Marketing Campaign Q1', '2024-01-01', '2024-03-31', 50000.00);

-- Create a view for practice
CREATE VIEW employee_summary AS
SELECT
    e.employee_id,
    e.first_name || ' ' || e.last_name AS full_name,
    e.department,
    e.salary
FROM employees e;
