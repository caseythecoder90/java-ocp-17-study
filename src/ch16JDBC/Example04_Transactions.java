package ch16JDBC;

import java.sql.*;

/**
 * OCP JDBC Practice: Transaction Management
 *
 * Key concepts:
 * - Auto-commit mode (default is true)
 * - Manual transaction control with commit() and rollback()
 * - Savepoints for partial rollbacks
 * - Transaction isolation levels
 */
public class Example04_Transactions {

    public static void main(String[] args) {
        DatabaseConfig.loadDriver();

        // Example 1: Basic transaction with commit
        basicTransactionExample();

        // Example 2: Transaction with rollback
        rollbackExample();

        // Example 3: Savepoints
        savepointExample();
    }

    private static void basicTransactionExample() {
        System.out.println("=== Example 1: Basic Transaction with Commit ===");
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(
                    DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);

            // Disable auto-commit to start a transaction
            conn.setAutoCommit(false);
            System.out.println("Auto-commit disabled. Transaction started.");

            // Insert multiple employees in one transaction
            String insertSQL = "INSERT INTO employees (first_name, last_name, email, department, salary) " +
                              "VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                // Insert employee 1
                pstmt.setString(1, "Michael");
                pstmt.setString(2, "Scott");
                pstmt.setString(3, "michael.scott@example.com");
                pstmt.setString(4, "Management");
                pstmt.setDouble(5, 85000.00);
                pstmt.executeUpdate();
                System.out.println("Inserted Michael Scott");

                // Insert employee 2
                pstmt.setString(1, "Dwight");
                pstmt.setString(2, "Schrute");
                pstmt.setString(3, "dwight.schrute@example.com");
                pstmt.setString(4, "Sales");
                pstmt.setDouble(5, 75000.00);
                pstmt.executeUpdate();
                System.out.println("Inserted Dwight Schrute");

                // Commit the transaction
                conn.commit();
                System.out.println("Transaction committed successfully!");
            }

        } catch (SQLException e) {
            System.err.println("Transaction failed: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("Transaction rolled back.");
                } catch (SQLException ex) {
                    System.err.println("Rollback failed: " + ex.getMessage());
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);  // Restore auto-commit
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void rollbackExample() {
        System.out.println("\n=== Example 2: Transaction with Rollback ===");
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(
                    DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
            conn.setAutoCommit(false);

            String insertSQL = "INSERT INTO employees (first_name, last_name, email, department, salary) " +
                              "VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                // Insert valid employee
                pstmt.setString(1, "Pam");
                pstmt.setString(2, "Beesly");
                pstmt.setString(3, "pam.beesly@example.com");
                pstmt.setString(4, "Reception");
                pstmt.setDouble(5, 55000.00);
                pstmt.executeUpdate();
                System.out.println("Inserted Pam Beesly");

                // Simulate error - duplicate email (will fail)
                pstmt.setString(1, "Jim");
                pstmt.setString(2, "Halpert");
                pstmt.setString(3, "pam.beesly@example.com");  // Duplicate email!
                pstmt.setString(4, "Sales");
                pstmt.setDouble(5, 65000.00);
                pstmt.executeUpdate();
                System.out.println("Inserted Jim Halpert");

                conn.commit();
            }

        } catch (SQLException e) {
            System.err.println("Error occurred: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("Transaction rolled back - neither employee was inserted.");
                } catch (SQLException ex) {
                    System.err.println("Rollback failed: " + ex.getMessage());
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void savepointExample() {
        System.out.println("\n=== Example 3: Savepoints ===");
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(
                    DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
            conn.setAutoCommit(false);

            String insertSQL = "INSERT INTO employees (first_name, last_name, email, department, salary) " +
                              "VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                // Insert first employee
                pstmt.setString(1, "Angela");
                pstmt.setString(2, "Martin");
                pstmt.setString(3, "angela.martin@example.com");
                pstmt.setString(4, "Accounting");
                pstmt.setDouble(5, 62000.00);
                pstmt.executeUpdate();
                System.out.println("Inserted Angela Martin");

                // Create savepoint after first insert
                Savepoint savepoint1 = conn.setSavepoint("SavePoint1");
                System.out.println("Savepoint created after first insert");

                // Insert second employee
                pstmt.setString(1, "Kevin");
                pstmt.setString(2, "Malone");
                pstmt.setString(3, "kevin.malone@example.com");
                pstmt.setString(4, "Accounting");
                pstmt.setDouble(5, 58000.00);
                pstmt.executeUpdate();
                System.out.println("Inserted Kevin Malone");

                // Simulate decision to rollback second insert only
                System.out.println("Rolling back to savepoint (Kevin will not be inserted)");
                conn.rollback(savepoint1);

                // Commit the transaction (only Angela is inserted)
                conn.commit();
                System.out.println("Transaction committed. Only Angela Martin was inserted.");

                // Release savepoint
                conn.releaseSavepoint(savepoint1);
            }

        } catch (SQLException e) {
            System.err.println("Savepoint example failed: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Rollback failed: " + ex.getMessage());
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
