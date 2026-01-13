package ch16JDBC;

import java.sql.*;

/**
 * OCP JDBC Practice: Basic Connection and Query
 *
 * Key concepts:
 * - DriverManager.getConnection()
 * - try-with-resources for auto-closing resources
 * - Statement vs PreparedStatement
 * - ResultSet navigation
 */
public class Example01_BasicConnection {

    public static void main(String[] args) {
        DatabaseConfig.loadDriver();

        // Example 1: Basic connection using try-with-resources
        System.out.println("=== Example 1: Basic Connection ===");
        try (Connection conn = DriverManager.getConnection(
                DatabaseConfig.URL,
                DatabaseConfig.USER,
                DatabaseConfig.PASSWORD)) {

            System.out.println("Connected to database successfully!");
            System.out.println("Database: " + conn.getCatalog());
            System.out.println("Auto-commit: " + conn.getAutoCommit());

        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
            e.printStackTrace();
        }

        // Example 2: Simple query with Statement
        System.out.println("\n=== Example 2: Simple Query with Statement ===");
        String query = "SELECT * FROM employees";

        try (Connection conn = DriverManager.getConnection(
                DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("employee_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String department = rs.getString("department");
                double salary = rs.getDouble("salary");

                System.out.printf("ID: %d, Name: %s %s, Dept: %s, Salary: $%.2f%n",
                        id, firstName, lastName, department, salary);
            }

        } catch (SQLException e) {
            System.err.println("Query failed: " + e.getMessage());
            e.printStackTrace();
        }

        // Example 3: Query with PreparedStatement (preferred for security)
        System.out.println("\n=== Example 3: Query with PreparedStatement ===");
        String preparedQuery = "SELECT * FROM employees WHERE department = ?";

        try (Connection conn = DriverManager.getConnection(
                DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(preparedQuery)) {

            pstmt.setString(1, "Engineering");

            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.println("Engineering Department Employees:");
                while (rs.next()) {
                    System.out.printf("  %s %s - $%.2f%n",
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getDouble("salary"));
                }
            }

        } catch (SQLException e) {
            System.err.println("PreparedStatement query failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
