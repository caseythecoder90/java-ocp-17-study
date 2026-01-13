package ch16JDBC;

import java.sql.*;

/**
 * OCP JDBC Practice: Batch Processing
 *
 * Key concepts:
 * - addBatch() to add statements to batch
 * - executeBatch() to execute all statements at once
 * - clearBatch() to clear batch
 * - Batch processing improves performance for multiple similar operations
 */
public class Example05_BatchProcessing {

    public static void main(String[] args) {
        DatabaseConfig.loadDriver();

        // Example 1: Batch inserts with PreparedStatement
        batchInsertExample();

        // Example 2: Mixed batch operations
        mixedBatchExample();
    }

    private static void batchInsertExample() {
        System.out.println("=== Example 1: Batch Insert ===");
        String sql = "INSERT INTO employees (first_name, last_name, email, department, salary) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(
                DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);

            // Employee 1
            pstmt.setString(1, "Stanley");
            pstmt.setString(2, "Hudson");
            pstmt.setString(3, "stanley.hudson@example.com");
            pstmt.setString(4, "Sales");
            pstmt.setDouble(5, 69000.00);
            pstmt.addBatch();

            // Employee 2
            pstmt.setString(1, "Phyllis");
            pstmt.setString(2, "Vance");
            pstmt.setString(3, "phyllis.vance@example.com");
            pstmt.setString(4, "Sales");
            pstmt.setDouble(5, 67000.00);
            pstmt.addBatch();

            // Employee 3
            pstmt.setString(1, "Oscar");
            pstmt.setString(2, "Martinez");
            pstmt.setString(3, "oscar.martinez@example.com");
            pstmt.setString(4, "Accounting");
            pstmt.setDouble(5, 71000.00);
            pstmt.addBatch();

            // Execute batch
            int[] results = pstmt.executeBatch();

            // Check results
            System.out.println("Batch execution results:");
            for (int i = 0; i < results.length; i++) {
                System.out.printf("  Statement %d: %d row(s) affected%n", i + 1, results[i]);
            }

            conn.commit();
            System.out.println("Batch insert committed successfully!");

        } catch (BatchUpdateException e) {
            System.err.println("Batch update failed: " + e.getMessage());
            System.err.println("Update counts for successful statements:");
            int[] updateCounts = e.getUpdateCounts();
            for (int i = 0; i < updateCounts.length; i++) {
                System.out.printf("  Statement %d: %d%n", i + 1, updateCounts[i]);
            }
        } catch (SQLException e) {
            System.err.println("Batch insert failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void mixedBatchExample() {
        System.out.println("\n=== Example 2: Mixed Batch Operations ===");

        try (Connection conn = DriverManager.getConnection(
                DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
             Statement stmt = conn.createStatement()) {

            conn.setAutoCommit(false);

            // Add multiple different statements to batch
            stmt.addBatch("UPDATE employees SET salary = salary * 1.03 WHERE department = 'Sales'");
            stmt.addBatch("UPDATE employees SET salary = salary * 1.04 WHERE department = 'Engineering'");
            stmt.addBatch("DELETE FROM employees WHERE email LIKE '%test.com'");

            // Execute batch
            int[] results = stmt.executeBatch();

            System.out.println("Batch execution results:");
            System.out.printf("  Sales salary update: %d row(s) affected%n", results[0]);
            System.out.printf("  Engineering salary update: %d row(s) affected%n", results[1]);
            System.out.printf("  Test emails deleted: %d row(s) affected%n", results[2]);

            conn.commit();
            System.out.println("Mixed batch operations committed successfully!");

        } catch (SQLException e) {
            System.err.println("Mixed batch failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
