package ch15jdbc;

/**
 * JDBC EXAM QUICK REFERENCE - OCP Java 17
 *
 * This file contains tables, summaries, and exam tips for quick review.
 * No executable code - just comprehensive reference material.
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * CRUD TO SQL MAPPING
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * ┌─────────────┬──────────────────┐
 * │ CRUD        │ SQL Keyword      │
 * ├─────────────┼──────────────────┤
 * │ Create      │ INSERT           │
 * │ Read        │ SELECT           │
 * │ Update      │ UPDATE           │
 * │ Delete      │ DELETE           │
 * └─────────────┴──────────────────┘
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * JDBC INTERFACES - WHAT YOU NEED TO KNOW
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * ┌────────────────────┬──────────────────────────────────────────────────┐
 * │ Interface          │ Purpose                                          │
 * ├────────────────────┼──────────────────────────────────────────────────┤
 * │ Driver             │ Establishes connection to database               │
 * │ Connection         │ Sends commands to database                       │
 * │ PreparedStatement  │ Executes SQL query (IN SCOPE)                    │
 * │ CallableStatement  │ Executes stored procedures (IN SCOPE)            │
 * │ ResultSet          │ Reads query results                              │
 * │ Statement          │ Base interface (NOT IN SCOPE - don't study!)     │
 * └────────────────────┴──────────────────────────────────────────────────┘
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * JDBC URL STRUCTURE
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Format:  jdbc:subprotocol:subname
 *          └┬─┘ └────┬────┘ └──┬───┘
 *           │        │         └─── Database-specific connection details
 *           │        └───────────── Database vendor (postgres, mysql, oracle, etc.)
 *           └────────────────────── Always "jdbc"
 *
 * Examples:
 *   jdbc:postgresql://localhost:5432/mydb
 *   jdbc:mysql://localhost:3306/mydb
 *   jdbc:oracle:thin:@localhost:1521:orcl
 *   jdbc:hsqldb:file:zoo
 *
 * MEMORIZE: Three parts, separated by colons, first part always "jdbc"
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * DRIVERMANAGER.GETCONNECTION() - THREE SIGNATURES
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * 1. Connection getConnection(String url)
 *    throws SQLException
 *    → Use when credentials are in URL
 *
 * 2. Connection getConnection(String url, String username, String password)
 *    throws SQLException
 *    → MOST COMMON ON EXAM ←
 *
 * 3. Connection getConnection(String url, Properties info)
 *    throws SQLException
 *    → Properties contains username, password, other settings
 *
 * KEY POINTS:
 * - Factory pattern (static method returns Connection)
 * - SQLException is CHECKED - must handle or declare
 * - Connection is AutoCloseable - use try-with-resources
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * PREPAREDSTATEMENT EXECUTE METHODS - CRITICAL!
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * ┌──────────────────┬──────────────┬─────────────────────────────────────┐
 * │ Method           │ Return Type  │ Use For                             │
 * ├──────────────────┼──────────────┼─────────────────────────────────────┤
 * │ executeUpdate()  │ int          │ INSERT, UPDATE, DELETE              │
 * │                  │              │ (returns rows affected)             │
 * ├──────────────────┼──────────────┼─────────────────────────────────────┤
 * │ executeQuery()   │ ResultSet    │ SELECT                              │
 * │                  │              │ (returns query results)             │
 * ├──────────────────┼──────────────┼─────────────────────────────────────┤
 * │ execute()        │ boolean      │ Any SQL statement                   │
 * │                  │              │ true=ResultSet, false=update count  │
 * └──────────────────┴──────────────┴─────────────────────────────────────┘
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * WHICH EXECUTE METHOD FOR WHICH SQL? (MEMORIZE THIS!)
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
 * ✗ = Throws SQLException if used
 * ✓ = Works correctly
 *
 * EXAM TRAPS:
 * - executeUpdate() with SELECT → SQLException
 * - executeQuery() with INSERT/UPDATE/DELETE → SQLException
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * EXECUTE METHOD RETURN TYPES (MEMORIZE!)
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * ┌──────────────────┬────────────────┬──────────────────────────────────┐
 * │ Method           │ Return Type    │ What It Represents               │
 * ├──────────────────┼────────────────┼──────────────────────────────────┤
 * │ executeUpdate()  │ int            │ Number of rows affected          │
 * ├──────────────────┼────────────────┼──────────────────────────────────┤
 * │ executeQuery()   │ ResultSet      │ Query results                    │
 * ├──────────────────┼────────────────┼──────────────────────────────────┤
 * │ execute()        │ boolean        │ true = ResultSet available       │
 * │                  │                │ false = update count available   │
 * │                  │                │   → use getResultSet()           │
 * │                  │                │   → use getUpdateCount()         │
 * └──────────────────┴────────────────┴──────────────────────────────────┘
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * PREPAREDSTATEMENT SETTERS - BINDING VARIABLES
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Binding Variables: Use ? as placeholder in SQL
 *   Example: "SELECT * FROM employees WHERE department = ? AND salary > ?"
 *
 * SETTER METHODS (Memorize these!):
 * ┌──────────────────────────────────────────────────────────────────────────┐
 * │ Method                                                                   │
 * ├──────────────────────────────────────────────────────────────────────────┤
 * │ void setBoolean(int parameterIndex, boolean x)                          │
 * │ void setDouble(int parameterIndex, double x)                            │
 * │ void setInt(int parameterIndex, int x)                                  │
 * │ void setLong(int parameterIndex, long x)                                │
 * │ void setNull(int parameterIndex, int sqlType)                           │
 * │ void setObject(int parameterIndex, Object x)                            │
 * │ void setString(int parameterIndex, String x)                            │
 * └──────────────────────────────────────────────────────────────────────────┘
 *
 * CRITICAL EXAM POINTS:
 * - Parameter indices start at 1, NOT 0!
 * - First ? is index 1, second ? is index 2, etc.
 * - Using index 0 → SQLException
 * - Using index > number of parameters → SQLException
 * - Parameters are "remembered" - can change and re-execute
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * RESULTSET GETTER METHODS
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Each getter has TWO overloads:
 * ┌──────────────────────────────────────────────────────────────────────────┐
 * │ By Column Index (1-based)      │ By Column Name                        │
 * ├────────────────────────────────┼───────────────────────────────────────┤
 * │ getBoolean(int columnIndex)    │ getBoolean(String columnLabel)        │
 * │ getDouble(int columnIndex)     │ getDouble(String columnLabel)         │
 * │ getInt(int columnIndex)        │ getInt(String columnLabel)            │
 * │ getLong(int columnIndex)       │ getLong(String columnLabel)           │
 * │ getObject(int columnIndex)     │ getObject(String columnLabel)         │
 * │ getString(int columnIndex)     │ getString(String columnLabel)         │
 * └────────────────────────────────┴───────────────────────────────────────┘
 *
 * CRITICAL EXAM POINTS:
 * - Column indices start at 1, NOT 0!
 * - First column is index 1, second is index 2, etc.
 * - Column names are case-insensitive
 * - Using index 0 → SQLException
 * - Using non-existent column → SQLException
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * RESULTSET CURSOR NAVIGATION
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Cursor: Points to current row in ResultSet
 * - Initially positioned BEFORE first row
 * - next() moves to next row, returns boolean
 *   • true = moved to valid row
 *   • false = no more rows
 *
 * Common Patterns:
 *
 * 1. Loop through all rows:
 *    while (rs.next()) {
 *        // Process row
 *    }
 *
 * 2. Single row (0 or 1):
 *    if (rs.next()) {
 *        // Process row
 *    }
 *
 * EXAM TRAPS:
 * - Accessing data before calling next() → SQLException
 * - Accessing data after next() returns false → SQLException
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * AUTOCLOSEABLE RESOURCES (Use try-with-resources!)
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * All of these are AutoCloseable:
 * - Connection
 * - PreparedStatement
 * - CallableStatement
 * - ResultSet
 *
 * Best Practice:
 *   try (Connection conn = DriverManager.getConnection(...);
 *        PreparedStatement ps = conn.prepareStatement(...);
 *        ResultSet rs = ps.executeQuery()) {
 *       // Use resources
 *   }  // Auto-closed in reverse order
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * COMMON SQLEXCEPTION CAUSES - KNOW THESE!
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * SQLException is thrown when:
 *
 * Connection:
 * ✗ Invalid URL, username, or password
 * ✗ Database not running
 * ✗ Network issues
 *
 * PreparedStatement:
 * ✗ executeUpdate() with SELECT
 * ✗ executeQuery() with INSERT/UPDATE/DELETE
 * ✗ Parameter index 0 or out of bounds
 * ✗ Not setting all parameters before executing
 *
 * ResultSet:
 * ✗ Accessing data before calling next()
 * ✗ Accessing data after next() returns false
 * ✗ Column index 0 or out of bounds
 * ✗ Non-existent column name
 * ✗ Wrong getter type for column data type
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * INDEX COMPARISON - ARRAYS VS JDBC (CRITICAL!)
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * ┌─────────────────────────┬───────────┬────────────┐
 * │                         │ Arrays    │ JDBC       │
 * ├─────────────────────────┼───────────┼────────────┤
 * │ First element/column    │ 0         │ 1          │
 * │ Second element/column   │ 1         │ 2          │
 * │ Third element/column    │ 2         │ 3          │
 * └─────────────────────────┴───────────┴────────────┘
 *
 * EXAM TRAP: JDBC is 1-based, arrays are 0-based!
 *            Don't confuse them!
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * WHAT'S NOT ON THE EXAM
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * You do NOT need to know:
 * ✗ SQL syntax errors (won't be tested)
 * ✗ Statement interface (only PreparedStatement and CallableStatement)
 * ✗ DataSource (only DriverManager)
 * ✗ Transactions, isolation levels, savepoints
 * ✗ Batch updates
 * ✗ Scrollable or updatable ResultSets
 * ✗ DatabaseMetaData or ResultSetMetaData
 * ✗ Connection pooling
 *
 * Focus on:
 * ✓ JDBC URL format
 * ✓ DriverManager.getConnection()
 * ✓ PreparedStatement: create, execute methods, binding variables
 * ✓ ResultSet: cursor, navigation, getters
 * ✓ SQLException handling
 * ✓ Try-with-resources
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * TOP 10 EXAM TRAPS
 * ═══════════════════════════════════════════════════════════════════════════
 *
 *  1. Using index 0 for parameters or columns (must start at 1!)
 *
 *  2. executeUpdate() with SELECT (throws SQLException)
 *
 *  3. executeQuery() with INSERT/UPDATE/DELETE (throws SQLException)
 *
 *  4. Accessing ResultSet data before calling next()
 *
 *  5. Accessing ResultSet data after next() returns false
 *
 *  6. Forgetting SQLException is CHECKED (must handle or declare)
 *
 *  7. Not using try-with-resources for AutoCloseable resources
 *
 *  8. Wrong number of parameters in PreparedStatement
 *
 *  9. Assuming Statement interface is in scope (it's NOT - only Prepared/Callable)
 *
 * 10. Thinking SQL syntax errors will be tested (they won't!)
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * EXAM DAY CHECKLIST
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Can you answer YES to all of these?
 *
 * □ I know the JDBC URL format (jdbc:subprotocol:subname)
 * □ I know all 3 DriverManager.getConnection() signatures
 * □ I know when to use executeUpdate(), executeQuery(), and execute()
 * □ I know what each execute method returns
 * □ I remember that JDBC indices start at 1, not 0
 * □ I know all PreparedStatement setters (setInt, setString, etc.)
 * □ I know all ResultSet getters (getInt, getString, etc.)
 * □ I understand ResultSet cursor starts BEFORE first row
 * □ I know SQLException is checked
 * □ I remember Statement interface is NOT on the exam
 *
 * If you answered YES to all → You're ready! Good luck! 🍀
 *
 * ═══════════════════════════════════════════════════════════════════════════
 */
public class JDBCExamReference {
    // This class is for reference only - no executable code
    private JDBCExamReference() {
        // Private constructor - this is a reference guide only
    }
}
