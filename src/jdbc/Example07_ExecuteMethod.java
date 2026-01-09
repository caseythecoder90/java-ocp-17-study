package jdbc;

import java.sql.*;

/**
 * OCP JDBC Practice: execute() Method and Result Processing
 *
 * Key concepts:
 * - execute() returns boolean: true if ResultSet, false if update count
 * - getResultSet() retrieves the ResultSet when execute() returns true
 * - getUpdateCount() retrieves update count when execute() returns false
 * - getMoreResults() moves to next result in case of multiple results
 * - Difference between execute(), executeQuery(), and executeUpdate()
 */
public class Example07_ExecuteMethod {

    public static void main(String[] args) {
        DatabaseConfig.loadDriver();

        // Example 1: execute() with SELECT query (returns ResultSet)
        executeWithSelect();

        // Example 2: execute() with UPDATE statement (returns update count)
        executeWithUpdate();

        // Example 3: execute() with DELETE statement (returns update count)
        executeWithDelete();

        // Example 4: Handling unknown SQL type at runtime
        handleUnknownSQL("SELECT * FROM employees WHERE department = 'Sales'");
        handleUnknownSQL("UPDATE employees SET salary = salary * 1.02 WHERE department = 'HR'");
        handleUnknownSQL("DELETE FROM employees WHERE email = 'nonexistent@example.com'");
    }

    private static void executeWithSelect() {
        System.out.println("=== Example 1: execute() with SELECT ===");
        String sql = "SELECT employee_id, first_name, last_name, department FROM employees LIMIT 3";

        try (Connection conn = DriverManager.getConnection(
                DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
             Statement stmt = conn.createStatement()) {

            // execute() returns true if first result is a ResultSet
            boolean hasResultSet = stmt.execute(sql);

            System.out.println("execute() returned: " + hasResultSet);

            if (hasResultSet) {
                System.out.println("Result is a ResultSet");
                try (ResultSet rs = stmt.getResultSet()) {
                    while (rs.next()) {
                        System.out.printf("  %d: %s %s (%s)%n",
                                rs.getInt("employee_id"),
                                rs.getString("first_name"),
                                rs.getString("last_name"),
                                rs.getString("department"));
                    }
                }
            } else {
                int updateCount = stmt.getUpdateCount();
                System.out.println("Update count: " + updateCount);
            }

        } catch (SQLException e) {
            System.err.println("Example 1 failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void executeWithUpdate() {
        System.out.println("\n=== Example 2: execute() with UPDATE ===");
        String sql = "UPDATE employees SET salary = salary * 1.01 WHERE department = 'Marketing'";

        try (Connection conn = DriverManager.getConnection(
                DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
             Statement stmt = conn.createStatement()) {

            // execute() returns false if first result is an update count
            boolean hasResultSet = stmt.execute(sql);

            System.out.println("execute() returned: " + hasResultSet);

            if (hasResultSet) {
                System.out.println("Result is a ResultSet (unexpected for UPDATE)");
                try (ResultSet rs = stmt.getResultSet()) {
                    // Process ResultSet
                }
            } else {
                int updateCount = stmt.getUpdateCount();
                System.out.println("Update count: " + updateCount + " row(s) updated");
            }

        } catch (SQLException e) {
            System.err.println("Example 2 failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void executeWithDelete() {
        System.out.println("\n=== Example 3: execute() with DELETE ===");
        String sql = "DELETE FROM employees WHERE email = 'test@example.com'";

        try (Connection conn = DriverManager.getConnection(
                DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
             Statement stmt = conn.createStatement()) {

            boolean hasResultSet = stmt.execute(sql);

            System.out.println("execute() returned: " + hasResultSet);

            if (hasResultSet) {
                System.out.println("Result is a ResultSet");
                try (ResultSet rs = stmt.getResultSet()) {
                    // Process ResultSet
                }
            } else {
                int updateCount = stmt.getUpdateCount();
                System.out.println("Delete count: " + updateCount + " row(s) deleted");
            }

        } catch (SQLException e) {
            System.err.println("Example 3 failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Example 4: Generic method to handle any SQL statement
     * Useful when you don't know at compile-time whether SQL is query or update
     */
    private static void handleUnknownSQL(String sql) {
        System.out.println("\n=== Example 4: Handling Unknown SQL Type ===");
        System.out.println("SQL: " + sql);

        try (Connection conn = DriverManager.getConnection(
                DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
             Statement stmt = conn.createStatement()) {

            boolean isResultSet = stmt.execute(sql);

            // Loop to handle multiple results (though most queries return only one)
            do {
                if (isResultSet) {
                    // Process ResultSet
                    System.out.println("Processing ResultSet:");
                    try (ResultSet rs = stmt.getResultSet()) {
                        ResultSetMetaData metaData = rs.getMetaData();
                        int columnCount = metaData.getColumnCount();

                        // Print column headers
                        for (int i = 1; i <= columnCount; i++) {
                            System.out.print(metaData.getColumnName(i) + "\t");
                        }
                        System.out.println();

                        // Print rows
                        int rowCount = 0;
                        while (rs.next() && rowCount < 5) {  // Limit to 5 rows for display
                            for (int i = 1; i <= columnCount; i++) {
                                System.out.print(rs.getString(i) + "\t");
                            }
                            System.out.println();
                            rowCount++;
                        }
                        System.out.println("(" + rowCount + " row(s) displayed)");
                    }
                } else {
                    // Process update count
                    int updateCount = stmt.getUpdateCount();
                    if (updateCount == -1) {
                        // No more results
                        System.out.println("No more results");
                        break;
                    } else {
                        System.out.println("Update count: " + updateCount + " row(s) affected");
                    }
                }

                // Move to next result (if any)
                isResultSet = stmt.getMoreResults();

            } while (isResultSet || stmt.getUpdateCount() != -1);

        } catch (SQLException e) {
            System.err.println("Handling unknown SQL failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Bonus: Comparison of execute(), executeQuery(), and executeUpdate()
     */
    public static void demonstrateMethodDifferences() {
        System.out.println("\n=== Method Differences Summary ===");
        System.out.println("""

                1. executeQuery():
                   - Returns: ResultSet
                   - Use for: SELECT statements only
                   - Throws SQLException if used with INSERT/UPDATE/DELETE

                2. executeUpdate():
                   - Returns: int (row count)
                   - Use for: INSERT, UPDATE, DELETE, DDL statements
                   - Throws SQLException if used with SELECT

                3. execute():
                   - Returns: boolean (true = ResultSet, false = update count)
                   - Use for: Any SQL statement, or unknown statement type
                   - Most flexible but requires checking return value
                   - Use getResultSet() if returns true
                   - Use getUpdateCount() if returns false

                OCP Exam Tips:
                - execute() is useful for dynamic SQL where type is unknown
                - getUpdateCount() returns -1 when there are no more results
                - getMoreResults() is used for statements returning multiple results
                - PreparedStatement also has these three methods
                """);
    }
}
