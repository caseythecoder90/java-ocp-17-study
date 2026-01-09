-- Stored Procedures/Functions for CallableStatement practice
-- Run this file after the database is initialized

-- 1. Function with NO parameters
-- Returns total number of employees
CREATE OR REPLACE FUNCTION get_employee_count()
RETURNS INTEGER AS $$
BEGIN
    RETURN (SELECT COUNT(*) FROM employees);
END;
$$ LANGUAGE plpgsql;

-- 2. Function with IN parameter only
-- Returns employee details by ID
CREATE OR REPLACE FUNCTION get_employee_by_id(emp_id INTEGER)
RETURNS TABLE(
    employee_id INTEGER,
    first_name VARCHAR,
    last_name VARCHAR,
    email VARCHAR,
    department VARCHAR,
    salary NUMERIC
) AS $$
BEGIN
    RETURN QUERY
    SELECT e.employee_id, e.first_name, e.last_name, e.email, e.department, e.salary
    FROM employees e
    WHERE e.employee_id = emp_id;
END;
$$ LANGUAGE plpgsql;

-- 3. Function with IN parameters - calculate bonus
-- Takes salary and returns bonus amount (10% of salary)
CREATE OR REPLACE FUNCTION calculate_bonus(emp_salary NUMERIC)
RETURNS NUMERIC AS $$
BEGIN
    RETURN emp_salary * 0.10;
END;
$$ LANGUAGE plpgsql;

-- 4. Function with multiple IN parameters
-- Returns employees in department with salary above threshold
CREATE OR REPLACE FUNCTION get_employees_by_dept_and_salary(
    dept_name VARCHAR,
    min_salary NUMERIC
)
RETURNS TABLE(
    employee_id INTEGER,
    full_name VARCHAR,
    salary NUMERIC
) AS $$
BEGIN
    RETURN QUERY
    SELECT e.employee_id,
           (e.first_name || ' ' || e.last_name)::VARCHAR as full_name,
           e.salary
    FROM employees e
    WHERE e.department = dept_name
    AND e.salary >= min_salary
    ORDER BY e.salary DESC;
END;
$$ LANGUAGE plpgsql;

-- 5. Function with OUT parameter
-- Gets department statistics
CREATE OR REPLACE FUNCTION get_department_stats(
    dept_name VARCHAR,
    OUT emp_count INTEGER,
    OUT avg_salary NUMERIC,
    OUT total_salary NUMERIC
) AS $$
BEGIN
    SELECT COUNT(*),
           AVG(salary)::NUMERIC(10,2),
           SUM(salary)::NUMERIC(10,2)
    INTO emp_count, avg_salary, total_salary
    FROM employees
    WHERE department = dept_name;
END;
$$ LANGUAGE plpgsql;

-- 6. Function with INOUT parameter
-- Takes a salary and increases it by a percentage (default 5%)
-- The same parameter serves as both input and output
CREATE OR REPLACE FUNCTION apply_raise(
    INOUT current_salary NUMERIC,
    raise_percent NUMERIC DEFAULT 5.0
) AS $$
BEGIN
    current_salary := current_salary * (1 + raise_percent / 100.0);
END;
$$ LANGUAGE plpgsql;

-- 7. Procedure with INOUT parameter (PostgreSQL 11+)
-- Increments a counter and returns the new value
CREATE OR REPLACE PROCEDURE increment_value(
    INOUT counter INTEGER
) AS $$
BEGIN
    counter := counter + 1;
END;
$$ LANGUAGE plpgsql;

-- 8. Function that updates data and returns count
-- Gives raise to all employees in a department
CREATE OR REPLACE FUNCTION give_department_raise(
    dept_name VARCHAR,
    raise_percent NUMERIC
)
RETURNS INTEGER AS $$
DECLARE
    affected_rows INTEGER;
BEGIN
    UPDATE employees
    SET salary = salary * (1 + raise_percent / 100.0)
    WHERE department = dept_name;

    GET DIAGNOSTICS affected_rows = ROW_COUNT;
    RETURN affected_rows;
END;
$$ LANGUAGE plpgsql;

-- 9. Function with multiple OUT parameters
CREATE OR REPLACE FUNCTION get_salary_range(
    dept_name VARCHAR,
    OUT min_sal NUMERIC,
    OUT max_sal NUMERIC,
    OUT avg_sal NUMERIC
) AS $$
BEGIN
    SELECT MIN(salary), MAX(salary), AVG(salary)::NUMERIC(10,2)
    INTO min_sal, max_sal, avg_sal
    FROM employees
    WHERE department = dept_name;
END;
$$ LANGUAGE plpgsql;

-- 10. Function with mixed IN and OUT parameters
CREATE OR REPLACE FUNCTION process_employee_data(
    emp_id INTEGER,
    raise_percent NUMERIC,
    OUT old_salary NUMERIC,
    OUT new_salary NUMERIC,
    OUT difference NUMERIC
) AS $$
BEGIN
    SELECT salary INTO old_salary
    FROM employees
    WHERE employee_id = emp_id;

    new_salary := old_salary * (1 + raise_percent / 100.0);
    difference := new_salary - old_salary;
END;
$$ LANGUAGE plpgsql;
