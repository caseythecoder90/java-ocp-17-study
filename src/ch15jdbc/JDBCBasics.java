package ch15jdbc;

import java.sql.*;

/**
 * JDBC BASICS - OCP Java 17 Exam
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * CRUD OPERATIONS AND SQL KEYWORDS
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * ┌─────────────┬──────────────────┐
 * │ Operation   │ SQL Keyword      │
 * ├─────────────┼──────────────────┤
 * │ Create      │ INSERT           │
 * │ Read        │ SELECT           │
 * │ Update      │ UPDATE           │
 * │ Delete      │ DELETE           │
 * └─────────────┴──────────────────┘
 *
 * EXAM NOTE: You are NOT expected to spot SQL syntax errors!
 *            Focus on JDBC API usage, not SQL correctness.
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * KEY JDBC INTERFACES (EXAM FOCUS)
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * ┌────────────────────┬─────────────────────────────────────────────────────┐
 * │ Interface          │ Purpose                                             │
 * ├────────────────────┼─────────────────────────────────────────────────────┤
 * │ Driver             │ Establishes connection to the database              │
 * │ Connection         │ Sends commands to the database                      │
 * │ PreparedStatement  │ Executes a SQL query                                │
 * │ CallableStatement  │ Executes commands stored in the database (stored    │
 * │                    │ procedures)                                         │
 * │ ResultSet          │ Reads the results of a query                        │
 * └────────────────────┴─────────────────────────────────────────────────────┘
 *
 * EXAM NOTE: Statement interface is NOT in scope. Only PreparedStatement and
 *            CallableStatement are tested.
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * JDBC URL STRUCTURE
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Format: jdbc:subprotocol:subname
 *
 * Three parts separated by COLONS:
 * 1. Protocol    - Always "jdbc"
 * 2. Subprotocol - Database vendor name (hsqldb, mysql, postgres, oracle, etc.)
 * 3. Subname     - Database-specific connection details (varies by vendor)
 *
 * Examples:
 * jdbc:postgresql://localhost:5432/ocp_practice
 * jdbc:mysql://localhost:3306/mydb
 * jdbc:oracle:thin:@localhost:1521:orcl
 * jdbc:hsqldb:file:zoo
 *
 * EXAM TIP: Memorize the format - three parts, always starts with "jdbc"
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * GETTING A CONNECTION
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Two ways to get a Connection:
 * 1. DriverManager (EXAM FOCUS - this is what's covered on the exam)
 * 2. DataSource (NOT on exam)
 *
 * DriverManager uses the FACTORY PATTERN:
 * - Static method: DriverManager.getConnection()
 * - Returns a Connection object
 *
 * Connection is AutoCloseable - ALWAYS use try-with-resources!
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * DriverManager.getConnection() SIGNATURES (MEMORIZE!)
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Three overloaded versions:
 *
 * 1. Connection getConnection(String url) throws SQLException
 *    - Just the JDBC URL
 *    - Use when credentials are in the URL
 *
 * 2. Connection getConnection(String url, String username, String password)
 *       throws SQLException
 *    - Most common for the exam
 *    - Separate username and password parameters
 *
 * 3. Connection getConnection(String url, Properties info) throws SQLException
 *    - Properties object contains username, password, and other settings
 *    - Less common on exam
 *
 * EXAM TRAP: SQLException is a CHECKED exception - must handle or declare!
 *
 * ═══════════════════════════════════════════════════════════════════════════
 */
public class JDBCBasics {

    // Database connection details (from docker-compose.yml)
    private static final String URL = "jdbc:postgresql://localhost:5432/ocp_practice";
    private static final String USERNAME = "ocpuser";
    private static final String PASSWORD = "ocppass123";

    public static void main(String[] args) {
        demonstrateJDBCURL();
        demonstrateConnectionMethods();
        demonstrateBasicQuery();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // JDBC URL Structure Examples
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateJDBCURL() {
        System.out.println("=== JDBC URL STRUCTURE ===\n");

        // Break down our PostgreSQL URL
        String url = "jdbc:postgresql://localhost:5432/ocp_practice";
        System.out.println("Full URL: " + url);
        System.out.println("\nBreakdown:");
        System.out.println("  Part 1 (Protocol):    jdbc");
        System.out.println("  Part 2 (Subprotocol): postgresql");
        System.out.println("  Part 3 (Subname):     //localhost:5432/ocp_practice");
        System.out.println("                        (host:port/database)");

        // Other vendor examples
        System.out.println("\nOther vendor examples:");
        System.out.println("  MySQL:    jdbc:mysql://localhost:3306/mydb");
        System.out.println("  Oracle:   jdbc:oracle:thin:@localhost:1521:orcl");
        System.out.println("  HSQLDB:   jdbc:hsqldb:file:zoo");

        System.out.println("\nEXAM TIP: Always three parts separated by colons!");
        System.out.println("          First part is ALWAYS 'jdbc'\n");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // Connection Methods - All Three Signatures
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateConnectionMethods() {
        System.out.println("=== GETTING A CONNECTION ===\n");

        // Method 1: URL only
        System.out.println("1. getConnection(String url)");
        System.out.println("   - Use when credentials are in URL");
        // Example (won't work without embedded credentials):
        // try (Connection conn = DriverManager.getConnection(
        //     "jdbc:postgresql://localhost:5432/ocp_practice?user=ocpuser&password=ocppass123")) {
        //     System.out.println("   Connected!");
        // } catch (SQLException e) { }

        // Method 2: URL + username + password (MOST COMMON ON EXAM)
        System.out.println("\n2. getConnection(String url, String username, String password)");
        System.out.println("   - Most common on exam");
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            System.out.println("   ✓ Connected successfully!");
            System.out.println("   Connection class: " + conn.getClass().getSimpleName());
        } catch (SQLException e) {
            System.out.println("   ✗ Connection failed: " + e.getMessage());
        }

        // Method 3: URL + Properties
        System.out.println("\n3. getConnection(String url, Properties info)");
        System.out.println("   - Properties object contains credentials");
        java.util.Properties props = new java.util.Properties();
        props.setProperty("user", USERNAME);
        props.setProperty("password", PASSWORD);
        try (Connection conn = DriverManager.getConnection(URL, props)) {
            System.out.println("   ✓ Connected successfully!");
        } catch (SQLException e) {
            System.out.println("   ✗ Connection failed: " + e.getMessage());
        }

        System.out.println("\nEXAM TIP: SQLException is CHECKED - must handle or declare!");
        System.out.println("          Always use try-with-resources for Connection\n");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // Basic Query Example
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateBasicQuery() {
        System.out.println("=== BASIC CONNECTION & QUERY ===\n");

        String sql = "SELECT COUNT(*) AS employee_count FROM employees";

        // Proper pattern: try-with-resources for AutoCloseable resources
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                int count = rs.getInt("employee_count");
                System.out.println("Total employees in database: " + count);
            }

            System.out.println("\nPattern used:");
            System.out.println("  1. Get Connection from DriverManager (factory pattern)");
            System.out.println("  2. Create PreparedStatement from Connection");
            System.out.println("  3. Execute query to get ResultSet");
            System.out.println("  4. Process ResultSet");
            System.out.println("  5. All resources auto-close (try-with-resources)");

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("\nEXAM TIP: SQLException can occur at ANY step!");
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// QUICK REFERENCE - MEMORIZE FOR EXAM
// ═══════════════════════════════════════════════════════════════════════════
//
// JDBC URL Format:
//   jdbc:subprotocol:subname
//   (Always 3 parts, always starts with "jdbc")
//
// DriverManager.getConnection() - Three signatures:
//   1. getConnection(String url)
//   2. getConnection(String url, String username, String password)  ← Most common
//   3. getConnection(String url, Properties info)
//
// SQLException:
//   - Checked exception
//   - Thrown by all JDBC methods
//   - Must handle or declare
//
// AutoCloseable Resources (use try-with-resources):
//   - Connection
//   - PreparedStatement
//   - CallableStatement
//   - ResultSet
//
// ═══════════════════════════════════════════════════════════════════════════
