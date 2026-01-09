package jdbc;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * OCP JDBC Practice: CallableStatement for Stored Procedures
 *
 * Key concepts:
 * - CallableStatement for calling stored procedures/functions
 * - No parameters: call {? = call function_name()}
 * - IN parameters: call {? = call function_name(?)}
 * - OUT parameters: call {call procedure_name(?, ?, ?)}
 * - INOUT parameters: call {call procedure_name(?)}
 * - registerOutParameter() for OUT and INOUT parameters
 * - Setting IN parameters with setXxx() methods
 * - Getting OUT parameters with getXxx() methods
 *
 * IMPORTANT for OCP exam:
 * - Use BigDecimal for NUMERIC/DECIMAL database types (most accurate)
 * - Use setDouble()/getDouble() for DOUBLE PRECISION types
 * - PostgreSQL is strict about type matching
 * - setBigDecimal() and getBigDecimal() are the safest for money/precision values
 */
public class Example08_CallableStatement {

    public static void main(String[] args) {
        DatabaseConfig.loadDriver();

        System.out.println("IMPORTANT: Run stored-procedures.sql first!");
        System.out.println("Execute: docker exec -i jdbc-practice-db psql -U ocpuser -d ocp_practice < stored-procedures.sql\n");

        // Example 1: No parameters
        callFunctionNoParameters();

        // Example 2: IN parameters only
        callFunctionWithInParameters();

        // Example 3: OUT parameters
        callFunctionWithOutParameters();

        // Example 4: INOUT parameters
        callFunctionWithInOutParameters();

        // Example 5: Mixed IN and OUT parameters
        callFunctionWithMixedParameters();

        // Example 6: Calling procedure (PostgreSQL 11+)
        callProcedureWithInOut();
    }

    /**
     * Example 1: Calling function with NO parameters
     * Syntax: {? = call function_name()}
     */
    private static void callFunctionNoParameters() {
        System.out.println("=== Example 1: No Parameters ===");
        String sql = "{? = call get_employee_count()}";

        try (Connection conn = DriverManager.getConnection(
                DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
             CallableStatement cstmt = conn.prepareCall(sql)) {

            // Register the OUT parameter (return value)
            // Parameter index 1 is the return value
            cstmt.registerOutParameter(1, Types.INTEGER);

            // Execute the function
            cstmt.execute();
            Arrays.asList(0, 1, 2).forEach(System.out::println);
            Arrays.asList(0,1,2).stream().forEach(System.out::println);


            // Get the return value
            int employeeCount = cstmt.getInt(1);
            System.out.println("Total employees: " + employeeCount);

        } catch (SQLException e) {
            System.err.println("Example 1 failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Example 2: Calling function with IN parameters only
     * Syntax: {? = call function_name(?)}
     */
    private static void callFunctionWithInParameters() {
        System.out.println("\n=== Example 2: IN Parameters ===");

        // 2a. Simple IN parameter returning scalar value
        String sql1 = "{? = call calculate_bonus(?)}";

        try (Connection conn = DriverManager.getConnection(
                DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
             CallableStatement cstmt = conn.prepareCall(sql1)) {

            // Register OUT parameter (return value)
            cstmt.registerOutParameter(1, Types.NUMERIC);

            // Set IN parameter (use BigDecimal for NUMERIC type)
            cstmt.setBigDecimal(2, new BigDecimal("75000.00"));

            // Execute
            cstmt.execute();

            // Get result
            BigDecimal bonus = cstmt.getBigDecimal(1);
            System.out.printf("Bonus for $75,000 salary: $%.2f%n", bonus);

        } catch (SQLException e) {
            System.err.println("Example 2a failed: " + e.getMessage());
            e.printStackTrace();
        }

        // 2b. IN parameters returning ResultSet
        String sql2 = "{call get_employees_by_dept_and_salary(?, ?)}";

        try (Connection conn = DriverManager.getConnection(
                DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
             CallableStatement cstmt = conn.prepareCall(sql2)) {

            // Set IN parameters
            cstmt.setString(1, "Engineering");
            cstmt.setBigDecimal(2, new BigDecimal("80000.00"));

            // Execute and get ResultSet
            boolean hasResults = cstmt.execute();

            if (hasResults) {
                System.out.println("\nEngineering employees with salary >= $80,000:");
                try (ResultSet rs = cstmt.getResultSet()) {
                    while (rs.next()) {
                        System.out.printf("  ID: %d, Name: %s, Salary: $%.2f%n",
                                rs.getInt("employee_id"),
                                rs.getString("full_name"),
                                rs.getDouble("salary"));
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Example 2b failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Example 3: Calling function with OUT parameters
     * Syntax: {call function_name(?, ?, ?, ?)}
     * First parameter is IN, rest are OUT
     */
    private static void callFunctionWithOutParameters() {
        System.out.println("\n=== Example 3: OUT Parameters ===");

        // 3a. Multiple OUT parameters
        String sql1 = "{call get_department_stats(?, ?, ?, ?)}";

        try (Connection conn = DriverManager.getConnection(
                DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
             CallableStatement cstmt = conn.prepareCall(sql1)) {

            // Set IN parameter
            cstmt.setString(1, "Sales");

            // Register OUT parameters
            cstmt.registerOutParameter(2, Types.INTEGER);  // emp_count
            cstmt.registerOutParameter(3, Types.NUMERIC);  // avg_salary
            cstmt.registerOutParameter(4, Types.NUMERIC);  // total_salary

            // Execute
            cstmt.execute();

            // Get OUT parameter values
            int empCount = cstmt.getInt(2);
            BigDecimal avgSalary = cstmt.getBigDecimal(3);
            BigDecimal totalSalary = cstmt.getBigDecimal(4);

            System.out.println("Sales Department Statistics:");
            System.out.println("  Employee Count: " + empCount);
            System.out.printf("  Average Salary: $%.2f%n", avgSalary);
            System.out.printf("  Total Salary: $%.2f%n", totalSalary);

        } catch (SQLException e) {
            System.err.println("Example 3 failed: " + e.getMessage());
            e.printStackTrace();
        }

        // 3b. Another example with multiple OUT parameters
        String sql2 = "{call get_salary_range(?, ?, ?, ?)}";

        try (Connection conn = DriverManager.getConnection(
                DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
             CallableStatement cstmt = conn.prepareCall(sql2)) {

            cstmt.setString(1, "Engineering");
            cstmt.registerOutParameter(2, Types.NUMERIC);  // min_sal
            cstmt.registerOutParameter(3, Types.NUMERIC);  // max_sal
            cstmt.registerOutParameter(4, Types.NUMERIC);  // avg_sal

            cstmt.execute();

            System.out.println("\nEngineering Department Salary Range:");
            System.out.printf("  Minimum: $%.2f%n", cstmt.getBigDecimal(2));
            System.out.printf("  Maximum: $%.2f%n", cstmt.getBigDecimal(3));
            System.out.printf("  Average: $%.2f%n", cstmt.getBigDecimal(4));

        } catch (SQLException e) {
            System.err.println("Example 3b failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Example 4: Calling function with INOUT parameters
     * INOUT parameter is both input and output
     * Syntax: {call function_name(?)}
     */
    private static void callFunctionWithInOutParameters() {
        System.out.println("\n=== Example 4: INOUT Parameters ===");
        String sql = "{call apply_raise(?, ?)}";

        try (Connection conn = DriverManager.getConnection(
                DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
             CallableStatement cstmt = conn.prepareCall(sql)) {

            BigDecimal currentSalary = new BigDecimal("60000.00");
            BigDecimal raisePercent = new BigDecimal("7.5");

            // Set INOUT parameter (parameter 1)
            cstmt.setBigDecimal(1, currentSalary);

            // Register it as OUT parameter too (INOUT = IN + OUT)
            cstmt.registerOutParameter(1, Types.NUMERIC);

            // Set IN parameter (parameter 2)
            cstmt.setBigDecimal(2, raisePercent);

            // Execute
            cstmt.execute();

            // Get the INOUT parameter value (now contains the new salary)
            BigDecimal newSalary = cstmt.getBigDecimal(1);

            System.out.printf("Original Salary: $%.2f%n", currentSalary);
            System.out.printf("Raise Percent: %.1f%%%n", raisePercent);
            System.out.printf("New Salary: $%.2f%n", newSalary);
            System.out.printf("Increase: $%.2f%n", newSalary.subtract(currentSalary));

        } catch (SQLException e) {
            System.err.println("Example 4 failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Example 5: Mixed IN and OUT parameters
     */
    private static void callFunctionWithMixedParameters() {
        System.out.println("\n=== Example 5: Mixed IN and OUT Parameters ===");
        String sql = "{call process_employee_data(?, ?, ?, ?, ?)}";

        try (Connection conn = DriverManager.getConnection(
                DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
             CallableStatement cstmt = conn.prepareCall(sql)) {

            // Set IN parameters
            cstmt.setInt(1, 1);                              // employee_id
            cstmt.setBigDecimal(2, new BigDecimal("10.0")); // raise_percent

            // Register OUT parameters
            cstmt.registerOutParameter(3, Types.NUMERIC);  // old_salary
            cstmt.registerOutParameter(4, Types.NUMERIC);  // new_salary
            cstmt.registerOutParameter(5, Types.NUMERIC);  // difference

            // Execute
            cstmt.execute();

            // Get OUT parameters
            BigDecimal oldSalary = cstmt.getBigDecimal(3);
            BigDecimal newSalary = cstmt.getBigDecimal(4);
            BigDecimal difference = cstmt.getBigDecimal(5);

            System.out.println("Employee Raise Calculation:");
            System.out.printf("  Old Salary: $%.2f%n", oldSalary);
            System.out.printf("  New Salary: $%.2f%n", newSalary);
            System.out.printf("  Difference: $%.2f%n", difference);

        } catch (SQLException e) {
            System.err.println("Example 5 failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Example 6: Calling stored procedure (not function) with INOUT
     *
     * NOTE: PostgreSQL procedures with INOUT parameters have limited support
     * in JDBC CallableStatement. In PostgreSQL, functions are preferred over
     * procedures for JDBC compatibility.
     *
     * For OCP exam: Focus on functions with IN/OUT/INOUT parameters
     * Oracle and other databases have better procedure support in JDBC
     */
    private static void callProcedureWithInOut() {
        System.out.println("\n=== Example 6: Procedure with INOUT ===");

        // PostgreSQL-specific: Use direct CALL statement instead of CallableStatement
        // This is a database-specific workaround, not typical for OCP exam
        String sql = "CALL increment_value(?)";

        try (Connection conn = DriverManager.getConnection(
                DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD)) {

            // For PostgreSQL procedures, we need to use a different approach
            // Create a function wrapper instead (more JDBC-compatible)
            System.out.println("Note: PostgreSQL procedures have limited JDBC support.");
            System.out.println("For OCP exam, focus on functions which are covered in Examples 1-5.");
            System.out.println("The INOUT concept is demonstrated in Example 4 (apply_raise function).");

            // Alternative: Create a function that does the same thing
            String functionSql = "CREATE OR REPLACE FUNCTION increment_value_func(INOUT counter INTEGER) " +
                                "AS $$ BEGIN counter := counter + 1; END; $$ LANGUAGE plpgsql";

            try (Statement stmt = conn.createStatement()) {
                stmt.execute(functionSql);
                System.out.println("Created function wrapper: increment_value_func");
            }

            // Now call it as a function
            String callSql = "{call increment_value_func(?)}";
            try (CallableStatement cstmt = conn.prepareCall(callSql)) {
                int counter = 5;

                cstmt.setInt(1, counter);
                cstmt.registerOutParameter(1, Types.INTEGER);
                cstmt.execute();

                int newCounter = cstmt.getInt(1);

                System.out.println("Counter before: " + counter);
                System.out.println("Counter after: " + newCounter);
            }

        } catch (SQLException e) {
            System.err.println("Example 6 failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Summary of CallableStatement syntax patterns for OCP exam
     */
    public static void printSyntaxSummary() {
        System.out.println("""

                === CallableStatement Syntax Summary ===

                1. Function with no parameters (returns value):
                   {? = call function_name()}
                   - Must register OUT parameter at index 1

                2. Function with IN parameters only:
                   {? = call function_name(?, ?)}
                   - Index 1: return value (OUT)
                   - Index 2+: IN parameters

                3. Function/Procedure with OUT parameters:
                   {call proc_name(?, ?, ?)}
                   - Register each OUT parameter with registerOutParameter()
                   - Retrieve with getXxx() methods

                4. INOUT parameters:
                   {call proc_name(?)}
                   - Set value with setXxx()
                   - Register with registerOutParameter()
                   - Retrieve modified value with getXxx()

                5. Named parameters (optional, database-specific):
                   PostgreSQL doesn't require named parameters
                   Oracle example: {call proc(param_name => ?)}

                Key Methods:
                - prepareCall(String sql)
                - setXxx(index, value) - for IN and INOUT
                - registerOutParameter(index, Types.XXX) - for OUT and INOUT
                - getXxx(index) - for OUT and INOUT
                - execute() or executeQuery()

                Important Notes:
                - INOUT parameters need both setXxx() AND registerOutParameter()
                - Parameter indices start at 1
                - Functions use {? = call ...}, procedures use {call ...}
                - Always check wasNull() after getting values that might be NULL
                """);
    }
}
