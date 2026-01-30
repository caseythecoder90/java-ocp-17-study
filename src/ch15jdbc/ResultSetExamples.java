package ch15jdbc;

import java.sql.*;

/**
 * RESULTSET - OCP Java 17 Exam
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * RESULTSET BASICS
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * A ResultSet reads the results of a query (SELECT statement).
 *
 * KEY CONCEPT: ResultSet has a CURSOR
 * - Cursor points to the current location in the data
 * - Initially, cursor is BEFORE the first row
 * - Use next() to move cursor forward one row
 * - next() returns boolean:
 *     • true  = successfully moved to a row
 *     • false = no more rows available
 *
 * EXAM CRITICAL: Cursor starts BEFORE first row, not ON first row!
 *                Must call next() before accessing data.
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * NAVIGATING WITH next()
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * boolean next() throws SQLException
 * - Moves cursor to next row
 * - Returns true if there is a row
 * - Returns false if no more rows
 *
 * Common patterns:
 *
 * 1. Loop through all rows:
 *    while (rs.next()) {
 *        // Process current row
 *    }
 *
 * 2. Single row expected:
 *    if (rs.next()) {
 *        // Process the row
 *    }
 *
 * 3. Exactly N rows:
 *    for (int i = 0; i < n && rs.next(); i++) {
 *        // Process row
 *    }
 *
 * EXAM TRAP: Attempting to access data when cursor is not on a valid row
 *            throws SQLException!
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * GETTING DATA FROM RESULTSET
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * GETTER METHODS (MEMORIZE THESE!):
 * - boolean getBoolean(int columnIndex)
 * - boolean getBoolean(String columnLabel)
 * - double getDouble(int columnIndex)
 * - double getDouble(String columnLabel)
 * - int getInt(int columnIndex)
 * - int getInt(String columnLabel)
 * - long getLong(int columnIndex)
 * - long getLong(String columnLabel)
 * - Object getObject(int columnIndex)
 * - Object getObject(String columnLabel)
 * - String getString(int columnIndex)
 * - String getString(String columnLabel)
 *
 * TWO WAYS TO ACCESS COLUMNS:
 *
 * 1. By column INDEX (1-based, NOT 0-based!)
 *    rs.getInt(1)      // First column
 *    rs.getString(2)   // Second column
 *
 * 2. By column NAME (column label from SQL)
 *    rs.getInt("employee_id")
 *    rs.getString("first_name")
 *
 * EXAM CRITICAL: Column indices start at 1, NOT 0!
 *                Using index 0 or non-existent column → SQLException
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * COMMON SQLEXCEPTION CAUSES WITH RESULTSET
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * SQLException is thrown when:
 * 1. Accessing data before calling next()
 * 2. Accessing data after next() returns false
 * 3. Using column index 0 (must start at 1)
 * 4. Using column index > number of columns
 * 5. Using column name that doesn't exist
 * 6. Accessing ResultSet after it's been closed
 *
 * ═══════════════════════════════════════════════════════════════════════════
 */
public class ResultSetExamples {

    private static final String URL = "jdbc:postgresql://localhost:5432/ocp_practice";
    private static final String USERNAME = "ocpuser";
    private static final String PASSWORD = "ocppass123";

    public static void main(String[] args) {
        demonstrateCursorBasics();
        demonstrateLoopPattern();
        demonstrateSingleRowPattern();
        demonstrateColumnAccess();
        demonstrateGetterMethods();
        demonstrateExamTraps();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // ResultSet Cursor Basics
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateCursorBasics() {
        System.out.println("=== RESULTSET CURSOR BASICS ===\n");

        String sql = "SELECT first_name, last_name FROM employees LIMIT 3";

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("Cursor position at creation: BEFORE first row");
            System.out.println("Must call next() to move to first row\n");

            System.out.println("Calling next() and retrieving data:");

            int rowNumber = 0;
            while (rs.next()) {
                rowNumber++;
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                System.out.println("  Row " + rowNumber + ": " + firstName + " " + lastName);
            }

            System.out.println("\nAfter loop: next() returned false (no more rows)");
            System.out.println("\nEXAM TIP: Cursor starts BEFORE first row, not ON it!\n");

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // Loop Pattern - Process All Rows
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateLoopPattern() {
        System.out.println("=== LOOP PATTERN (All Rows) ===\n");

        String sql = "SELECT department, COUNT(*) as emp_count FROM employees " +
                     "GROUP BY department ORDER BY emp_count DESC";

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("Employees by department:");

            // Standard loop pattern for all rows
            while (rs.next()) {
                String dept = rs.getString("department");
                int count = rs.getInt("emp_count");
                System.out.println("  " + dept + ": " + count);
            }

            System.out.println("\nPattern: while (rs.next()) { ... }");
            System.out.println("  - Most common pattern on exam");
            System.out.println("  - Processes all rows");
            System.out.println("  - Loop ends when next() returns false\n");

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // Single Row Pattern - Process One Row
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateSingleRowPattern() {
        System.out.println("=== SINGLE ROW PATTERN ===\n");

        String sql = "SELECT COUNT(*) as total FROM employees";

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            // Single row pattern with if
            if (rs.next()) {
                int total = rs.getInt("total");
                System.out.println("Total employees: " + total);
            } else {
                System.out.println("No results found");
            }

            System.out.println("\nPattern: if (rs.next()) { ... }");
            System.out.println("  - Use when expecting 0 or 1 rows");
            System.out.println("  - Common with aggregate functions (COUNT, SUM, etc.)");
            System.out.println("  - Safer than while when only one row expected\n");

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // Column Access - By Index vs By Name
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateColumnAccess() {
        System.out.println("=== COLUMN ACCESS - Index vs Name ===\n");

        String sql = "SELECT employee_id, first_name, last_name, salary FROM employees LIMIT 1";

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                System.out.println("TWO WAYS TO ACCESS COLUMNS:\n");

                // Method 1: By index (1-based!)
                System.out.println("1. By INDEX (1-based):");
                int id1 = rs.getInt(1);           // Column 1: employee_id
                String first1 = rs.getString(2);  // Column 2: first_name
                String last1 = rs.getString(3);   // Column 3: last_name
                double salary1 = rs.getDouble(4); // Column 4: salary
                System.out.println("   ID: " + id1 + ", Name: " + first1 + " " + last1 + ", Salary: $" + salary1);

                // Method 2: By name
                System.out.println("\n2. By NAME (column label):");
                int id2 = rs.getInt("employee_id");
                String first2 = rs.getString("first_name");
                String last2 = rs.getString("last_name");
                double salary2 = rs.getDouble("salary");
                System.out.println("   ID: " + id2 + ", Name: " + first2 + " " + last2 + ", Salary: $" + salary2);

                System.out.println("\nEXAM TIPS:");
                System.out.println("  - Column INDEX starts at 1 (NOT 0!)");
                System.out.println("  - Column NAME is case-insensitive");
                System.out.println("  - By name is more readable, but by index is faster");
                System.out.println("  - Both throw SQLException if column doesn't exist\n");
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // All Getter Methods
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateGetterMethods() {
        System.out.println("=== GETTER METHODS (Memorize for Exam) ===\n");

        // Create a test query with various data types
        String sql = "SELECT " +
                     "employee_id, " +           // int
                     "first_name, " +            // String
                     "salary, " +                // double
                     "(salary > 80000) as high_earner, " +  // boolean
                     "employee_id::bigint as id_long " +    // long
                     "FROM employees LIMIT 1";

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                System.out.println("Demonstrating all getter methods:\n");

                // getInt
                int employeeId = rs.getInt("employee_id");
                System.out.println("getInt(\"employee_id\"): " + employeeId);

                // getString
                String firstName = rs.getString("first_name");
                System.out.println("getString(\"first_name\"): " + firstName);

                // getDouble
                double salary = rs.getDouble("salary");
                System.out.println("getDouble(\"salary\"): " + salary);

                // getBoolean
                boolean highEarner = rs.getBoolean("high_earner");
                System.out.println("getBoolean(\"high_earner\"): " + highEarner);

                // getLong
                long idLong = rs.getLong("id_long");
                System.out.println("getLong(\"id_long\"): " + idLong);

                // getObject (works for any type)
                Object idObject = rs.getObject("employee_id");
                System.out.println("getObject(\"employee_id\"): " + idObject +
                                   " (class: " + idObject.getClass().getSimpleName() + ")");

                System.out.println("\nAll getter methods for exam:");
                System.out.println("  getBoolean(int|String)");
                System.out.println("  getDouble(int|String)");
                System.out.println("  getInt(int|String)");
                System.out.println("  getLong(int|String)");
                System.out.println("  getObject(int|String)  ← Works for any type");
                System.out.println("  getString(int|String)");
                System.out.println("\nEach has two overloads: by index or by name\n");
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // Exam Traps
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateExamTraps() {
        System.out.println("=== EXAM TRAPS ===\n");

        String sql = "SELECT employee_id, first_name FROM employees LIMIT 1";

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            // TRAP 1: Accessing data before calling next()
            System.out.println("1. Accessing data BEFORE calling next():");
            try {
                rs.getString("first_name");  // ERROR - cursor not on a row
            } catch (SQLException e) {
                System.out.println("   ✗ SQLException thrown! (expected)");
                System.out.println("   Must call next() first!");
            }

            // Move to first row
            rs.next();

            // TRAP 2: Using column index 0
            System.out.println("\n2. Using column index 0:");
            try {
                rs.getInt(0);  // ERROR - indices start at 1
            } catch (SQLException e) {
                System.out.println("   ✗ SQLException thrown! (expected)");
                System.out.println("   Column indices start at 1, not 0!");
            }

            // TRAP 3: Using non-existent column index
            System.out.println("\n3. Using column index out of bounds:");
            try {
                rs.getString(10);  // ERROR - only 2 columns
            } catch (SQLException e) {
                System.out.println("   ✗ SQLException thrown! (expected)");
                System.out.println("   Only 2 columns in ResultSet!");
            }

            // TRAP 4: Using non-existent column name
            System.out.println("\n4. Using non-existent column name:");
            try {
                rs.getString("non_existent_column");  // ERROR - column doesn't exist
            } catch (SQLException e) {
                System.out.println("   ✗ SQLException thrown! (expected)");
                System.out.println("   Column name doesn't exist!");
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // TRAP 5: Accessing after next() returns false
        System.out.println("\n5. Accessing data after next() returns false:");
        String sql2 = "SELECT first_name FROM employees WHERE 1=0";  // No rows
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql2);
             ResultSet rs = ps.executeQuery()) {

            boolean hasRow = rs.next();  // Returns false - no rows
            System.out.println("   next() returned: " + hasRow);
            if (!hasRow) {
                try {
                    rs.getString("first_name");  // ERROR - no row to read from
                } catch (SQLException e) {
                    System.out.println("   ✗ SQLException thrown! (expected)");
                    System.out.println("   Cannot access data when next() returns false!");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("\nKEY EXAM TRAPS:");
        System.out.println("  ✗ Accessing data before calling next() → SQLException");
        System.out.println("  ✗ Accessing data after next() returns false → SQLException");
        System.out.println("  ✗ Column index 0 → SQLException (must start at 1!)");
        System.out.println("  ✗ Column index > column count → SQLException");
        System.out.println("  ✗ Non-existent column name → SQLException");
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// QUICK REFERENCE FOR EXAM
// ═══════════════════════════════════════════════════════════════════════════
//
// ResultSet Cursor:
//   - Starts BEFORE first row
//   - next() moves to next row, returns boolean
//   - Must call next() before accessing data
//
// Common Patterns:
//   while (rs.next()) { ... }     // All rows
//   if (rs.next()) { ... }        // Single row
//
// Getter Methods (each has two overloads):
//   getBoolean(int index | String name)
//   getDouble(int index | String name)
//   getInt(int index | String name)
//   getLong(int index | String name)
//   getObject(int index | String name)
//   getString(int index | String name)
//
// Column Access:
//   By index: rs.getInt(1)           // Starts at 1, NOT 0!
//   By name:  rs.getInt("employee_id")
//
// SQLException Causes:
//   - Accessing before next() or after next() returns false
//   - Column index 0 or out of bounds
//   - Non-existent column name
//
// ═══════════════════════════════════════════════════════════════════════════
