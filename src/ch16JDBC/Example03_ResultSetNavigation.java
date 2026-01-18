package ch16JDBC;

import java.sql.*;

/**
 * OCP JDBC Practice: ResultSet Navigation and Types
 *
 * Key concepts:
 * - ResultSet types: TYPE_FORWARD_ONLY, TYPE_SCROLL_INSENSITIVE, TYPE_SCROLL_SENSITIVE
 * - ResultSet concurrency: CONCUR_READ_ONLY, CONCUR_UPDATABLE
 * - Navigation methods: next(), previous(), first(), last(), absolute(), relative()
 * - Cursor position methods: isFirst(), isLast(), isBeforeFirst(), isAfterLast()
 */
public class Example03_ResultSetNavigation {

    public static void main(String[] args) {
        DatabaseConfig.loadDriver();

        // Example 1: Forward-only ResultSet (default)
        forwardOnlyExample();

        // Example 2: Scrollable ResultSet
        scrollableExample();

        // Example 3: Updatable ResultSet
        updatableResultSetExample();
    }

    private static void forwardOnlyExample() {
        System.out.println("=== Example 1: Forward-Only ResultSet ===");
        String sql = "SELECT * FROM employees ORDER BY employee_id";

        try (Connection conn = DriverManager.getConnection(
                DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
             Statement stmt = conn.createStatement(
                     ResultSet.TYPE_FORWARD_ONLY,
                     ResultSet.CONCUR_READ_ONLY);
             ResultSet rs = stmt.executeQuery(sql)) {

            int count = 0;
            while (rs.next()) {
                count++;
                System.out.printf("Row %d: %s %s%n",
                        count,
                        rs.getString("first_name"),
                        rs.getString("last_name"));
            }
            System.out.println("Total rows: " + count);

            // Attempting to go backward will throw SQLException
            // rs.previous(); // This would fail!

        } catch (SQLException e) {
            System.err.println("Forward-only example failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void scrollableExample() {
        System.out.println("\n=== Example 2: Scrollable ResultSet ===");
        String sql = "SELECT employee_id, first_name, last_name, salary FROM employees ORDER BY employee_id";

        try (Connection conn = DriverManager.getConnection(
                DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
             Statement stmt = conn.createStatement(
                     ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_READ_ONLY);
             ResultSet rs = stmt.executeQuery(sql)) {

            // Move to last row
            if (rs.last()) {
                System.out.println("Last employee: " + rs.getString("first_name") + " " +
                        rs.getString("last_name"));
                System.out.println("Row number: " + rs.getRow());
            }

            // Move to first row
            if (rs.first()) {
                System.out.println("\nFirst employee: " + rs.getString("first_name") + " " +
                        rs.getString("last_name"));
            }

            // Move to specific row (3rd row)
            if (rs.absolute(3)) {
                System.out.println("\n3rd employee: " + rs.getString("first_name") + " " +
                        rs.getString("last_name"));
            }

            // Move relative to current position
            if (rs.relative(-1)) {  // Move back 1 row
                System.out.println("Previous employee: " + rs.getString("first_name") + " " +
                        rs.getString("last_name"));
            }

            // Navigate backward
            System.out.println("\nBackward navigation:");
            rs.afterLast();  // Position after last row
            while (rs.previous()) {
                System.out.printf("  %s %s - $%.2f%n",
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getDouble("salary"));
            }

            // Position checking
            rs.beforeFirst();
            System.out.println("\nIs before first? " + rs.isBeforeFirst());
            rs.next();
            System.out.println("Is first? " + rs.isFirst());

        } catch (SQLException e) {
            System.err.println("Scrollable example failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void updatableResultSetExample() {
        System.out.println("\n=== Example 3: Updatable ResultSet ===");
        String sql = "SELECT employee_id, first_name, last_name, salary FROM employees WHERE department = 'Sales'";

        try (Connection conn = DriverManager.getConnection(
                DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
             Statement stmt = conn.createStatement(
                     ResultSet.TYPE_SCROLL_SENSITIVE,
                     ResultSet.CONCUR_UPDATABLE);
             ResultSet rs = stmt.executeQuery(sql)) {

            // Display current data
            System.out.println("Before update:");
            while (rs.next()) {
                System.out.printf("  %s %s - $%.2f%n",
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getDouble("salary"));
            }

            // Update a row using ResultSet
            rs.first();
            double oldSalary = rs.getDouble("salary");
            double newSalary = oldSalary * 1.05;  // 5% raise

            rs.updateDouble("salary", newSalary);
            rs.updateRow();  // Commit the update to database

            System.out.println("\nAfter 5% raise:");
            rs.beforeFirst();
            while (rs.next()) {
                System.out.printf("  %s %s - $%.2f%n",
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getDouble("salary"));
            }

            // Insert a new row using ResultSet
            rs.moveToInsertRow();
            rs.updateString("first_name", "Tom");
            rs.updateString("last_name", "Hardy");
            rs.updateDouble("salary", 70000.00);
            rs.insertRow();
            rs.moveToCurrentRow();

            System.out.println("\nAfter inserting new employee (note: full insert needs more fields)");

        } catch (SQLException e) {
            System.err.println("Updatable ResultSet example failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
