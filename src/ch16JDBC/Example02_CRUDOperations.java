package ch16jdbc;

import java.sql.*;

/**
 * OCP JDBC Practice: CRUD Operations (Create, Read, Update, Delete)
 *
 * Key concepts:
 * - INSERT, UPDATE, DELETE statements
 * - executeUpdate() returns affected row count
 * - Getting auto-generated keys
 * - PreparedStatement for parameterized queries
 */
public class Example02_CRUDOperations {

    public static void main(String[] args) {
        DatabaseConfig.loadDriver();

        // CREATE - Insert new employee
        insertEmployee("Sarah", "Connor", "sarah.connor@example.com", "Engineering", 92000.00);

        // READ - Query employee
        readEmployeeByEmail("sarah.connor@example.com");

        // UPDATE - Update employee salary
        updateEmployeeSalary("sarah.connor@example.com", 98000.00);

        // READ again to verify update
        readEmployeeByEmail("sarah.connor@example.com");

        // DELETE - Remove employee
        deleteEmployee("sarah.connor@example.com");

        // READ to verify deletion
        readEmployeeByEmail("sarah.connor@example.com");
    }

    // CREATE
    private static void insertEmployee(String firstName, String lastName,
                                      String email, String department, double salary) {
        System.out.println("\n=== INSERT Operation ===");
        String sql = "INSERT INTO employees (first_name, last_name, email, department, salary) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(
                DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, email);
            pstmt.setString(4, department);
            pstmt.setDouble(5, salary);

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Rows inserted: " + rowsAffected);

            // Get the auto-generated employee_id
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    System.out.println("Generated employee_id: " + id);
                }
            }

        } catch (SQLException e) {
            System.err.println("Insert failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // READ
    private static void readEmployeeByEmail(String email) {
        System.out.println("\n=== READ Operation ===");
        String sql = "SELECT * FROM employees WHERE email = ?";

        try (Connection conn = DriverManager.getConnection(
                DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    System.out.printf("Found: %s %s, Dept: %s, Salary: $%.2f%n",
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("department"),
                            rs.getDouble("salary"));
                } else {
                    System.out.println("No employee found with email: " + email);
                }
            }

        } catch (SQLException e) {
            System.err.println("Read failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // UPDATE
    private static void updateEmployeeSalary(String email, double newSalary) {
        System.out.println("\n=== UPDATE Operation ===");
        String sql = "UPDATE employees SET salary = ? WHERE email = ?";

        try (Connection conn = DriverManager.getConnection(
                DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, newSalary);
            pstmt.setString(2, email);

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Rows updated: " + rowsAffected);
            if (rowsAffected > 0) {
                System.out.printf("Updated salary to $%.2f for %s%n", newSalary, email);
            }

        } catch (SQLException e) {
            System.err.println("Update failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // DELETE
    private static void deleteEmployee(String email) {
        System.out.println("\n=== DELETE Operation ===");
        String sql = "DELETE FROM employees WHERE email = ?";

        try (Connection conn = DriverManager.getConnection(
                DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Rows deleted: " + rowsAffected);
            if (rowsAffected > 0) {
                System.out.println("Deleted employee with email: " + email);
            }

        } catch (SQLException e) {
            System.err.println("Delete failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
