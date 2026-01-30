package ch15jdbc;

import java.sql.*;

/**
 * PREPAREDSTATEMENT - OCP Java 17 Exam
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * STATEMENT HIERARCHY (EXAM FOCUS)
 * ═══════════════════════════════════════════════════════════════════════════
 *
 *              Statement (interface) - NOT on exam
 *                   ↑
 *                   |
 *       ┌───────────┴───────────┐
 *       |                       |
 * PreparedStatement    CallableStatement
 * (EXAM FOCUS)         (EXAM FOCUS)
 *
 * EXAM NOTE: Statement interface is NOT in scope!
 *            Only PreparedStatement and CallableStatement are tested.
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * CREATING A PREPAREDSTATEMENT
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Get from Connection:
 *   PreparedStatement ps = conn.prepareStatement(sql);
 *
 * IMPORTANT:
 * - Represents a SQL statement you want to run
 * - SQL statement is MANDATORY when creating
 * - Each distinct SQL statement needs its own prepareStatement() call
 * - PreparedStatement is AutoCloseable (use try-with-resources)
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * EXECUTE METHODS - CRITICAL FOR EXAM!
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Three methods to execute SQL:
 *
 * 1. int executeUpdate()
 *    - For: INSERT, UPDATE, DELETE (data modification statements)
 *    - Returns: int (number of rows affected)
 *    - Use for: Statements that CHANGE data
 *
 * 2. ResultSet executeQuery()
 *    - For: SELECT (query statements)
 *    - Returns: ResultSet (query results)
 *    - Use for: Statements that READ data
 *
 * 3. boolean execute()
 *    - For: Any SQL statement (can run query OR update)
 *    - Returns: boolean
 *      • true = ResultSet available (was a query)
 *      • false = update count available (was an update)
 *    - If true: use getResultSet()
 *    - If false: use getUpdateCount()
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * WHICH METHOD FOR WHICH SQL? (MEMORIZE THIS TABLE!)
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * ┌──────────────────┬────────┬────────┬────────┬────────┐
 * │ Method           │ DELETE │ INSERT │ UPDATE │ SELECT │
 * ├──────────────────┼────────┼────────┼────────┼────────┤
 * │ executeUpdate()  │   ✓    │   ✓    │   ✓    │   ✗    │
 * │ executeQuery()   │   ✗    │   ✗    │   ✗    │   ✓    │
 * │ execute()        │   ✓    │   ✓    │   ✓    │   ✓    │
 * └──────────────────┴────────┴────────┴────────┴────────┘
 *
 * EXAM TRAP: Using wrong method throws SQLException!
 *            executeUpdate() with SELECT → SQLException
 *            executeQuery() with INSERT → SQLException
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * RETURN TYPES (MEMORIZE!)
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * ┌──────────────────┬────────────────────────────────────────┐
 * │ Method           │ Return Type                            │
 * ├──────────────────┼────────────────────────────────────────┤
 * │ executeUpdate()  │ int (rows affected)                    │
 * │ executeQuery()   │ ResultSet (query results)              │
 * │ execute()        │ boolean (true=ResultSet, false=update) │
 * └──────────────────┴────────────────────────────────────────┘
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * BINDING VARIABLES (PARAMETERS)
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Use ? as a placeholder (binding variable)
 *   String sql = "SELECT * FROM employees WHERE department = ?";
 *
 * CRITICAL: JDBC uses 1-based indexing (NOT 0-based!)
 *   First ? is index 1
 *   Second ? is index 2
 *   etc.
 *
 * SETTER METHODS (MEMORIZE THESE!):
 * - setBoolean(int parameterIndex, boolean x)
 * - setDouble(int parameterIndex, double x)
 * - setInt(int parameterIndex, int x)
 * - setLong(int parameterIndex, long x)
 * - setNull(int parameterIndex, int sqlType)
 * - setObject(int parameterIndex, Object x)
 * - setString(int parameterIndex, String x)
 *
 * EXAM TIPS:
 * - Parameter indices start at 1, not 0!
 * - Binding variables are "remembered" - can change and re-execute
 * - Can update multiple records with same PreparedStatement
 * - Setting a parameter that doesn't exist → SQLException
 *
 * ═══════════════════════════════════════════════════════════════════════════
 */
public class PreparedStatementExamples {

    private static final String URL = "jdbc:postgresql://localhost:5432/ocp_practice";
    private static final String USERNAME = "ocpuser";
    private static final String PASSWORD = "ocppass123";

    public static void main(String[] args) {
        demonstrateExecuteUpdate();
        demonstrateExecuteQuery();
        demonstrateExecute();
        demonstrateBindingVariables();
        demonstrateReusingPreparedStatement();
        demonstrateExamTraps();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // executeUpdate() - For INSERT, UPDATE, DELETE
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateExecuteUpdate() {
        System.out.println("=== executeUpdate() - INSERT, UPDATE, DELETE ===\n");

        String url = URL;
        String username = USERNAME;
        String password = PASSWORD;

        try (Connection conn = DriverManager.getConnection(url, username, password)) {

            // INSERT
            String insertSql = "INSERT INTO employees (first_name, last_name, email, department, salary) " +
                               "VALUES ('Test', 'Employee', 'test@example.com', 'Engineering', 50000)";
            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                int rowsInserted = ps.executeUpdate();
                System.out.println("INSERT: " + rowsInserted + " row(s) inserted");
            }

            // UPDATE
            String updateSql = "UPDATE employees SET salary = 55000 WHERE email = 'test@example.com'";
            try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
                int rowsUpdated = ps.executeUpdate();
                System.out.println("UPDATE: " + rowsUpdated + " row(s) updated");
            }

            // DELETE
            String deleteSql = "DELETE FROM employees WHERE email = 'test@example.com'";
            try (PreparedStatement ps = conn.prepareStatement(deleteSql)) {
                int rowsDeleted = ps.executeUpdate();
                System.out.println("DELETE: " + rowsDeleted + " row(s) deleted");
            }

            System.out.println("\nKey Points:");
            System.out.println("  - executeUpdate() returns int (rows affected)");
            System.out.println("  - Use for: INSERT, UPDATE, DELETE");
            System.out.println("  - Do NOT use for SELECT (throws SQLException)\n");

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // executeQuery() - For SELECT
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateExecuteQuery() {
        System.out.println("=== executeQuery() - SELECT ===\n");

        String sql = "SELECT first_name, last_name, department FROM employees LIMIT 3";

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("Query results:");
            while (rs.next()) {
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String dept = rs.getString("department");
                System.out.println("  " + firstName + " " + lastName + " (" + dept + ")");
            }

            System.out.println("\nKey Points:");
            System.out.println("  - executeQuery() returns ResultSet");
            System.out.println("  - Use for: SELECT only");
            System.out.println("  - Do NOT use for INSERT/UPDATE/DELETE (throws SQLException)\n");

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // execute() - For any SQL statement
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateExecute() {
        System.out.println("=== execute() - Any SQL Statement ===\n");

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {

            // Example 1: SELECT (returns true)
            String selectSql = "SELECT COUNT(*) FROM employees";
            try (PreparedStatement ps = conn.prepareStatement(selectSql)) {
                boolean isResultSet = ps.execute();
                System.out.println("SELECT with execute():");
                System.out.println("  Returns: " + isResultSet + " (true = ResultSet available)");

                if (isResultSet) {
                    try (ResultSet rs = ps.getResultSet()) {
                        if (rs.next()) {
                            System.out.println("  Count: " + rs.getInt(1));
                        }
                    }
                }
            }

            // Example 2: INSERT (returns false)
            String insertSql = "INSERT INTO employees (first_name, last_name, email, department, salary) " +
                               "VALUES ('Temp', 'User', 'temp@example.com', 'HR', 40000)";
            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                boolean isResultSet = ps.execute();
                System.out.println("\nINSERT with execute():");
                System.out.println("  Returns: " + isResultSet + " (false = update count available)");

                if (!isResultSet) {
                    int count = ps.getUpdateCount();
                    System.out.println("  Rows inserted: " + count);
                }
            }

            // Cleanup
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM employees WHERE email = 'temp@example.com'")) {
                ps.executeUpdate();
            }

            System.out.println("\nKey Points:");
            System.out.println("  - execute() returns boolean");
            System.out.println("  - true → use getResultSet()");
            System.out.println("  - false → use getUpdateCount()");
            System.out.println("  - Works with ANY SQL statement\n");

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // Binding Variables (Parameters)
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateBindingVariables() {
        System.out.println("=== BINDING VARIABLES (?) ===\n");

        // SQL with binding variables (?)
        String sql = "SELECT first_name, last_name, salary FROM employees WHERE department = ? AND salary > ?";

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Set parameters (REMEMBER: 1-based indexing!)
            ps.setString(1, "Engineering");  // First ? (index 1)
            ps.setDouble(2, 80000.0);        // Second ? (index 2)

            System.out.println("Query: Find Engineering employees with salary > 80000");
            System.out.println("  Parameter 1 (department): 'Engineering'");
            System.out.println("  Parameter 2 (salary): 80000.0\n");

            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("Results:");
                while (rs.next()) {
                    System.out.println("  " + rs.getString(1) + " " +
                                       rs.getString(2) + " - $" + rs.getDouble(3));
                }
            }

            System.out.println("\nAll setter methods (memorize for exam):");
            System.out.println("  setBoolean(int index, boolean x)");
            System.out.println("  setDouble(int index, double x)");
            System.out.println("  setInt(int index, int x)");
            System.out.println("  setLong(int index, long x)");
            System.out.println("  setNull(int index, int sqlType)");
            System.out.println("  setObject(int index, Object x)");
            System.out.println("  setString(int index, String x)");
            System.out.println("\nEXAM TRAP: Indices start at 1, NOT 0!\n");

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // Reusing PreparedStatement with Different Parameters
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateReusingPreparedStatement() {
        System.out.println("=== REUSING PREPAREDSTATEMENT ===\n");

        String sql = "SELECT COUNT(*) FROM employees WHERE department = ?";

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Execute with first parameter
            ps.setString(1, "Engineering");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Engineering employees: " + rs.getInt(1));
                }
            }

            // Change parameter and execute again
            ps.setString(1, "Marketing");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Marketing employees: " + rs.getInt(1));
                }
            }

            // Change parameter again
            ps.setString(1, "Sales");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Sales employees: " + rs.getInt(1));
                }
            }

            System.out.println("\nKey Point:");
            System.out.println("  - Same PreparedStatement used multiple times");
            System.out.println("  - Parameters are 'remembered' and can be changed");
            System.out.println("  - Efficient for multiple similar operations\n");

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // Exam Traps
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateExamTraps() {
        System.out.println("=== EXAM TRAPS ===\n");

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {

            // TRAP 1: Wrong method for SQL type
            System.out.println("1. Using executeUpdate() with SELECT:");
            String selectSql = "SELECT * FROM employees";
            try (PreparedStatement ps = conn.prepareStatement(selectSql)) {
                ps.executeUpdate();  // WRONG - throws SQLException
            } catch (SQLException e) {
                System.out.println("   ✗ SQLException thrown! (expected)");
                System.out.println("   Message: " + e.getMessage().substring(0, 50) + "...");
            }

            // TRAP 2: Using executeQuery() with INSERT
            System.out.println("\n2. Using executeQuery() with INSERT:");
            String insertSql = "INSERT INTO employees (first_name, last_name, email) VALUES ('A', 'B', 'c@d.com')";
            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                ps.executeQuery();  // WRONG - throws SQLException
            } catch (SQLException e) {
                System.out.println("   ✗ SQLException thrown! (expected)");
                System.out.println("   Message: " + e.getMessage().substring(0, 50) + "...");
            }

            // TRAP 3: 0-based index (common mistake!)
            System.out.println("\n3. Using index 0 for binding variable:");
            String sql = "SELECT * FROM employees WHERE department = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(0, "Engineering");  // WRONG - indices start at 1
            } catch (SQLException e) {
                System.out.println("   ✗ SQLException thrown! (expected)");
                System.out.println("   Message: " + e.getMessage());
            }

            System.out.println("\nKEY EXAM TRAPS:");
            System.out.println("  ✗ executeUpdate() with SELECT → SQLException");
            System.out.println("  ✗ executeQuery() with INSERT/UPDATE/DELETE → SQLException");
            System.out.println("  ✗ Parameter index 0 → SQLException (must start at 1!)");
            System.out.println("  ✗ Setting parameter that doesn't exist → SQLException");

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// QUICK REFERENCE FOR EXAM
// ═══════════════════════════════════════════════════════════════════════════
//
// Execute Methods:
//   executeUpdate() → int (for INSERT, UPDATE, DELETE)
//   executeQuery()  → ResultSet (for SELECT only)
//   execute()       → boolean (any SQL, true=ResultSet, false=update count)
//
// Binding Variables:
//   Use ? in SQL
//   Indices start at 1 (NOT 0!)
//   Setters: setInt, setString, setDouble, setBoolean, setLong, setNull, setObject
//
// Common SQLException Causes:
//   - Wrong execute method for SQL type
//   - Index 0 or out of bounds for parameters
//   - Accessing non-existent column in ResultSet
//
// ═══════════════════════════════════════════════════════════════════════════
