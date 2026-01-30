package ch15jdbc;

import java.sql.*;

/**
 * CALLABLESTATEMENT - OCP Java 17 Exam
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * WHAT IS A STORED PROCEDURE?
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * A stored procedure is a pre-compiled SQL code stored in the database that
 * can be executed by calling its name.
 *
 * Benefits:
 * - Reusable logic stored in database
 * - Better performance (pre-compiled)
 * - Can accept input parameters (IN)
 * - Can return output parameters (OUT)
 * - Can do both (INOUT)
 *
 * CallableStatement is used to execute stored procedures from Java.
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * CREATING A CALLABLESTATEMENT
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Get from Connection:
 *   CallableStatement cs = conn.prepareCall("{call procedure_name(?)}");
 *
 * Syntax:
 *   {call procedure_name}                    // No parameters
 *   {call procedure_name(?)}                 // One parameter
 *   {call procedure_name(?, ?, ?)}           // Multiple parameters
 *   {? = call function_name(?)}              // Function with return value
 *
 * EXAM NOTE: Curly braces {} and "call" keyword are required!
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * PARAMETER TYPES
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Three types of parameters:
 *
 * 1. IN Parameter
 *    - Input only (Java → Database)
 *    - Must SET before calling: cs.setInt(1, value)
 *    - Used to pass data TO the stored procedure
 *
 * 2. OUT Parameter
 *    - Output only (Database → Java)
 *    - Must REGISTER before calling: cs.registerOutParameter(1, Types.INTEGER)
 *    - Used to get data FROM the stored procedure
 *    - Access after execute(): cs.getInt(1)
 *
 * 3. INOUT Parameter
 *    - Both input and output (Java ↔ Database)
 *    - Must SET before calling: cs.setInt(1, value)
 *    - Must REGISTER before calling: cs.registerOutParameter(1, Types.INTEGER)
 *    - Access after execute(): cs.getInt(1)
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * PARAMETER COMPARISON TABLE (MEMORIZE!)
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * ┌───────────┬─────────┬────────────┬──────────────────────┬─────────────────────────┬─────────────┐
 * │ Parameter │ Used    │ Used for   │ Must call            │ Must call               │ Use ? in    │
 * │ Type      │ for IN  │ OUT        │ setXxx()?            │ registerOutParameter()? │ SQL?        │
 * ├───────────┼─────────┼────────────┼──────────────────────┼─────────────────────────┼─────────────┤
 * │ IN        │   ✓     │     ✗      │ YES (before execute) │ NO                      │ YES         │
 * │ OUT       │   ✗     │     ✓      │ NO                   │ YES (before execute)    │ YES         │
 * │ INOUT     │   ✓     │     ✓      │ YES (before execute) │ YES (before execute)    │ YES         │
 * └───────────┴─────────┴────────────┴──────────────────────┴─────────────────────────┴─────────────┘
 *
 * Key Points:
 * - IN: Set value, don't register
 * - OUT: Register, don't set value
 * - INOUT: Both set value AND register
 * - All use ? in the SQL call
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * SETTING IN PARAMETERS - TWO WAYS (UNLIKE PREPAREDSTATEMENT!)
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * CallableStatement can use EITHER:
 * 1. Parameter index (1-based):
 *    cs.setInt(1, 100);
 *
 * 2. Parameter name:
 *    cs.setInt("employee_id", 100);
 *
 * EXAM NOTE: PreparedStatement can ONLY use index!
 *            CallableStatement can use index OR name!
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * REGISTERING OUT PARAMETERS
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Must call registerOutParameter() for OUT and INOUT parameters:
 *
 *   cs.registerOutParameter(int parameterIndex, int sqlType)
 *   cs.registerOutParameter(String parameterName, int sqlType)
 *
 * Common sqlType values (from java.sql.Types):
 * - Types.INTEGER
 * - Types.VARCHAR
 * - Types.DOUBLE
 * - Types.BOOLEAN
 * - Types.DATE
 *
 * EXAM TIP: Must register BEFORE calling execute()!
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * EXECUTING AND RETRIEVING RESULTS
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Execute the procedure:
 *   cs.execute();              // Most common for procedures
 *   cs.executeQuery();         // If procedure returns ResultSet
 *   cs.executeUpdate();        // If procedure modifies data
 *
 * Get OUT parameter values (after execute):
 *   int result = cs.getInt(1);              // By index
 *   int result = cs.getInt("param_name");   // By name
 *
 * EXAM NOTE: executeQuery() is available for CallableStatement!
 *            Use when stored procedure returns a ResultSet.
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * RESULTSET OPTIONS (Recognize but don't need to know details)
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * When creating PreparedStatement or CallableStatement, can specify ResultSet type:
 *
 *   PreparedStatement ps = conn.prepareStatement(sql,
 *                                                 resultSetType,
 *                                                 resultSetConcurrency);
 *
 * RESULTSET TYPE (how cursor moves):
 * ┌──────────────────────────┬─────────────────────────────────────────────────┐
 * │ Constant                 │ Meaning                                         │
 * ├──────────────────────────┼─────────────────────────────────────────────────┤
 * │ TYPE_FORWARD_ONLY        │ Cursor moves forward only (default)             │
 * │ TYPE_SCROLL_INSENSITIVE  │ Scrollable, but doesn't see DB changes          │
 * │ TYPE_SCROLL_SENSITIVE    │ Scrollable, sees DB changes                     │
 * └──────────────────────────┴─────────────────────────────────────────────────┘
 *
 * RESULTSET CONCURRENCY (can update results?):
 * ┌──────────────────────────┬─────────────────────────────────────────────────┐
 * │ Constant                 │ Meaning                                         │
 * ├──────────────────────────┼─────────────────────────────────────────────────┤
 * │ CONCUR_READ_ONLY         │ Cannot update ResultSet (default)               │
 * │ CONCUR_UPDATABLE         │ Can update ResultSet                            │
 * └──────────────────────────┴─────────────────────────────────────────────────┘
 *
 * EXAM TIP: Just recognize these as valid options. Don't need to memorize details!
 *
 * ═══════════════════════════════════════════════════════════════════════════
 */
public class CallableStatementExamples {

    private static final String URL = "jdbc:postgresql://localhost:5432/ocp_practice";
    private static final String USERNAME = "ocpuser";
    private static final String PASSWORD = "ocppass123";

    public static void main(String[] args) {
        // First, create stored procedures in database
        setupStoredProcedures();

        // Then demonstrate each type
        demonstrateNoParameters();
        demonstrateInParameter();
        demonstrateOutParameter();
        demonstrateInOutParameter();
        demonstrateParameterByName();
        demonstrateExecuteQuery();
        demonstrateResultSetOptions();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // Setup - Create Stored Procedures in Database
    // ═══════════════════════════════════════════════════════════════════════════
    private static void setupStoredProcedures() {
        System.out.println("=== SETTING UP STORED PROCEDURES ===\n");

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement stmt = conn.createStatement()) {

            // Drop existing procedures if they exist
            try {
                stmt.execute("DROP PROCEDURE IF EXISTS get_employee_count");
                stmt.execute("DROP PROCEDURE IF EXISTS get_employee_info");
                stmt.execute("DROP PROCEDURE IF EXISTS get_salary_by_id");
                stmt.execute("DROP PROCEDURE IF EXISTS update_and_return_salary");
                stmt.execute("DROP FUNCTION IF EXISTS get_dept_employees");
            } catch (SQLException e) {
                // Ignore errors for non-existent procedures
            }

            // 1. No parameters - just return count
            stmt.execute(
                "CREATE PROCEDURE get_employee_count() " +
                "LANGUAGE SQL " +
                "AS $$ " +
                "  SELECT COUNT(*) FROM employees; " +
                "$$"
            );

            // 2. IN parameter - get employee info by ID
            stmt.execute(
                "CREATE PROCEDURE get_employee_info(IN emp_id INTEGER) " +
                "LANGUAGE SQL " +
                "AS $$ " +
                "  SELECT first_name, last_name, department " +
                "  FROM employees WHERE employee_id = emp_id; " +
                "$$"
            );

            // 3. OUT parameter - get salary by ID
            stmt.execute(
                "CREATE PROCEDURE get_salary_by_id(IN emp_id INTEGER, OUT emp_salary NUMERIC) " +
                "LANGUAGE plpgsql " +
                "AS $$ " +
                "BEGIN " +
                "  SELECT salary INTO emp_salary FROM employees WHERE employee_id = emp_id; " +
                "END; " +
                "$$"
            );

            // 4. INOUT parameter - double the salary
            stmt.execute(
                "CREATE PROCEDURE update_and_return_salary(INOUT emp_salary NUMERIC) " +
                "LANGUAGE plpgsql " +
                "AS $$ " +
                "BEGIN " +
                "  emp_salary := emp_salary * 2; " +
                "END; " +
                "$$"
            );

            // 5. Function that returns a ResultSet (for executeQuery demo)
            stmt.execute(
                "CREATE FUNCTION get_dept_employees(dept_name VARCHAR) " +
                "RETURNS TABLE(first_name VARCHAR, last_name VARCHAR, salary NUMERIC) " +
                "LANGUAGE SQL " +
                "AS $$ " +
                "  SELECT first_name, last_name, salary " +
                "  FROM employees WHERE department = dept_name; " +
                "$$"
            );

            System.out.println("✓ All stored procedures created successfully\n");

        } catch (SQLException e) {
            System.out.println("Setup error: " + e.getMessage());
            System.out.println("(This is OK if procedures already exist)\n");
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // 1. Calling Stored Procedure with NO PARAMETERS
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateNoParameters() {
        System.out.println("=== STORED PROCEDURE: NO PARAMETERS ===\n");

        // SQL call syntax: {call procedure_name}
        String sql = "{call get_employee_count()}";

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             CallableStatement cs = conn.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {

            if (rs.next()) {
                int count = rs.getInt(1);
                System.out.println("Employee count: " + count);
            }

            System.out.println("\nKey Points:");
            System.out.println("  - Use conn.prepareCall() to create CallableStatement");
            System.out.println("  - Syntax: {call procedure_name}");
            System.out.println("  - Curly braces {} are required!");
            System.out.println("  - executeQuery() returns ResultSet\n");

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // 2. Calling Stored Procedure with IN PARAMETER
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateInParameter() {
        System.out.println("=== STORED PROCEDURE: IN PARAMETER ===\n");

        // SQL call syntax: {call procedure_name(?)}
        String sql = "{call get_employee_info(?)}";

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             CallableStatement cs = conn.prepareCall(sql)) {

            // Set IN parameter (must do BEFORE execute)
            int employeeId = 1;
            cs.setInt(1, employeeId);

            System.out.println("Calling procedure with IN parameter: " + employeeId);

            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    String dept = rs.getString("department");
                    System.out.println("  Result: " + firstName + " " + lastName + " (" + dept + ")");
                }
            }

            System.out.println("\nKey Points:");
            System.out.println("  - IN parameter: Java → Database");
            System.out.println("  - Must call setXxx() BEFORE execute");
            System.out.println("  - Do NOT call registerOutParameter()");
            System.out.println("  - Use ? as placeholder in SQL\n");

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // 3. Calling Stored Procedure with OUT PARAMETER
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateOutParameter() {
        System.out.println("=== STORED PROCEDURE: OUT PARAMETER ===\n");

        // SQL call syntax: {call procedure_name(?, ?)}
        String sql = "{call get_salary_by_id(?, ?)}";

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             CallableStatement cs = conn.prepareCall(sql)) {

            // Parameter 1: IN parameter (employee ID)
            cs.setInt(1, 1);

            // Parameter 2: OUT parameter (salary) - must register!
            cs.registerOutParameter(2, Types.NUMERIC);

            System.out.println("Calling procedure with IN and OUT parameters");

            // Execute the procedure
            cs.execute();

            // Get OUT parameter value (AFTER execute)
            double salary = cs.getDouble(2);
            System.out.println("  Salary returned: $" + salary);

            System.out.println("\nKey Points:");
            System.out.println("  - OUT parameter: Database → Java");
            System.out.println("  - Must call registerOutParameter() BEFORE execute");
            System.out.println("  - Do NOT call setXxx()");
            System.out.println("  - Get value with getXxx() AFTER execute");
            System.out.println("  - Specify SQL type (Types.NUMERIC, Types.INTEGER, etc.)\n");

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // 4. Calling Stored Procedure with INOUT PARAMETER
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateInOutParameter() {
        System.out.println("=== STORED PROCEDURE: INOUT PARAMETER ===\n");

        // SQL call syntax: {call procedure_name(?)}
        String sql = "{call update_and_return_salary(?)}";

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             CallableStatement cs = conn.prepareCall(sql)) {

            double originalSalary = 50000.0;

            // INOUT parameter: Must BOTH set AND register!
            // 1. Set the input value
            cs.setDouble(1, originalSalary);

            // 2. Register as output parameter
            cs.registerOutParameter(1, Types.NUMERIC);

            System.out.println("Original salary: $" + originalSalary);
            System.out.println("Calling procedure that doubles salary...");

            // Execute the procedure
            cs.execute();

            // Get the modified value
            double newSalary = cs.getDouble(1);
            System.out.println("New salary: $" + newSalary);

            System.out.println("\nKey Points:");
            System.out.println("  - INOUT parameter: Java ↔ Database (both directions)");
            System.out.println("  - Must call setXxx() BEFORE execute (input)");
            System.out.println("  - Must call registerOutParameter() BEFORE execute");
            System.out.println("  - Get value with getXxx() AFTER execute (output)");
            System.out.println("  - Do BOTH operations for INOUT!\n");

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // 5. Setting Parameters BY NAME (Not available in PreparedStatement!)
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateParameterByName() {
        System.out.println("=== SETTING PARAMETERS BY NAME ===\n");

        String sql = "{call get_salary_by_id(?, ?)}";

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             CallableStatement cs = conn.prepareCall(sql)) {

            // CallableStatement can use parameter names!
            // (PreparedStatement can ONLY use indices)

            System.out.println("Two ways to set parameters:\n");

            // Method 1: By index (works like PreparedStatement)
            System.out.println("1. By INDEX:");
            cs.setInt(1, 2);                          // First parameter
            cs.registerOutParameter(2, Types.NUMERIC); // Second parameter
            System.out.println("   cs.setInt(1, 2)");
            System.out.println("   cs.registerOutParameter(2, Types.NUMERIC)");

            cs.execute();
            System.out.println("   Result: $" + cs.getDouble(2));

            // Method 2: By name (CallableStatement only!)
            System.out.println("\n2. By NAME (CallableStatement only!):");
            cs.setInt("emp_id", 3);
            cs.registerOutParameter("emp_salary", Types.NUMERIC);
            System.out.println("   cs.setInt(\"emp_id\", 3)");
            System.out.println("   cs.registerOutParameter(\"emp_salary\", Types.NUMERIC)");

            cs.execute();
            System.out.println("   Result: $" + cs.getDouble("emp_salary"));

            System.out.println("\nEXAM NOTE:");
            System.out.println("  - PreparedStatement: Index ONLY");
            System.out.println("  - CallableStatement: Index OR Name\n");

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // 6. executeQuery() with CallableStatement
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateExecuteQuery() {
        System.out.println("=== executeQuery() WITH CALLABLESTATEMENT ===\n");

        // Calling a function that returns a result set
        String sql = "{call get_dept_employees(?)}";

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, "Engineering");

            System.out.println("Calling function that returns ResultSet:");

            // executeQuery() is available for CallableStatement!
            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    double salary = rs.getDouble("salary");
                    System.out.println("  " + firstName + " " + lastName + " - $" + salary);
                }
            }

            System.out.println("\nKey Point:");
            System.out.println("  - executeQuery() IS available for CallableStatement");
            System.out.println("  - Returns ResultSet just like PreparedStatement");
            System.out.println("  - Use when stored procedure returns a result set\n");

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // 7. ResultSet Options (Recognize as valid, don't need to know details)
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateResultSetOptions() {
        System.out.println("=== RESULTSET OPTIONS (Recognize, don't memorize details) ===\n");

        String sql = "SELECT first_name, last_name FROM employees";

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {

            // Creating PreparedStatement with ResultSet options
            System.out.println("Creating statements with ResultSet options:\n");

            // Default: TYPE_FORWARD_ONLY, CONCUR_READ_ONLY
            try (PreparedStatement ps1 = conn.prepareStatement(sql)) {
                System.out.println("1. Default (no options):");
                System.out.println("   TYPE_FORWARD_ONLY + CONCUR_READ_ONLY");
            }

            // Scrollable, read-only
            try (PreparedStatement ps2 = conn.prepareStatement(sql,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY)) {
                System.out.println("\n2. TYPE_SCROLL_INSENSITIVE + CONCUR_READ_ONLY");
                System.out.println("   (Can scroll, but read-only)");
            }

            // Scrollable, updatable
            try (PreparedStatement ps3 = conn.prepareStatement(sql,
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE)) {
                System.out.println("\n3. TYPE_SCROLL_SENSITIVE + CONCUR_UPDATABLE");
                System.out.println("   (Can scroll and update)");
            }

            System.out.println("\nResultSet Type Options:");
            System.out.println("  TYPE_FORWARD_ONLY       - Forward only (default)");
            System.out.println("  TYPE_SCROLL_INSENSITIVE - Scrollable, doesn't see changes");
            System.out.println("  TYPE_SCROLL_SENSITIVE   - Scrollable, sees changes");

            System.out.println("\nResultSet Concurrency Options:");
            System.out.println("  CONCUR_READ_ONLY        - Read-only (default)");
            System.out.println("  CONCUR_UPDATABLE        - Can update");

            System.out.println("\nEXAM TIP: Just recognize these as valid options!");
            System.out.println("          Don't need to memorize details.\n");

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// QUICK REFERENCE FOR EXAM
// ═══════════════════════════════════════════════════════════════════════════
//
// Creating CallableStatement:
//   CallableStatement cs = conn.prepareCall("{call proc_name(?)}");
//
// Parameter Types:
//   IN    - Set value, don't register
//   OUT   - Register, don't set value
//   INOUT - Both set AND register
//
// Setting Parameters:
//   cs.setInt(1, value)              // By index
//   cs.setInt("param_name", value)   // By name (CallableStatement only!)
//
// Registering OUT Parameters:
//   cs.registerOutParameter(1, Types.INTEGER)
//   cs.registerOutParameter("name", Types.INTEGER)
//
// Executing:
//   cs.execute()        // General purpose
//   cs.executeQuery()   // Returns ResultSet (available!)
//   cs.executeUpdate()  // Modifies data
//
// Getting OUT Values:
//   int result = cs.getInt(1)              // By index
//   int result = cs.getInt("param_name")   // By name
//
// Key Differences from PreparedStatement:
//   - CallableStatement can use parameter names (PreparedStatement cannot)
//   - CallableStatement has registerOutParameter() for OUT/INOUT
//   - CallableStatement uses {call ...} syntax
//
// ═══════════════════════════════════════════════════════════════════════════
