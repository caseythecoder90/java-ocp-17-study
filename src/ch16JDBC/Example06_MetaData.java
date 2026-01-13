package ch16JDBC;

import java.sql.*;

/**
 * OCP JDBC Practice: Database and ResultSet MetaData
 *
 * Key concepts:
 * - DatabaseMetaData: information about the database
 * - ResultSetMetaData: information about result set columns
 * - Useful for dynamic queries and database introspection
 */
public class Example06_MetaData {

    public static void main(String[] args) {
        DatabaseConfig.loadDriver();

        // Example 1: Database MetaData
        databaseMetaDataExample();

        // Example 2: ResultSet MetaData
        resultSetMetaDataExample();
    }

    private static void databaseMetaDataExample() {
        System.out.println("=== Example 1: Database MetaData ===");

        try (Connection conn = DriverManager.getConnection(
                DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD)) {

            DatabaseMetaData dbMetaData = conn.getMetaData();

            System.out.println("Database Product: " + dbMetaData.getDatabaseProductName());
            System.out.println("Database Version: " + dbMetaData.getDatabaseProductVersion());
            System.out.println("Driver Name: " + dbMetaData.getDriverName());
            System.out.println("Driver Version: " + dbMetaData.getDriverVersion());
            System.out.println("JDBC Version: " + dbMetaData.getJDBCMajorVersion() + "." +
                    dbMetaData.getJDBCMinorVersion());
            System.out.println("Max Connections: " + dbMetaData.getMaxConnections());
            System.out.println("Supports Transactions: " + dbMetaData.supportsTransactions());
            System.out.println("Supports Batch Updates: " + dbMetaData.supportsBatchUpdates());

            // List all tables
            System.out.println("\nTables in database:");
            try (ResultSet tables = dbMetaData.getTables(null, null, "%", new String[]{"TABLE"})) {
                while (tables.next()) {
                    String tableName = tables.getString("TABLE_NAME");
                    String tableType = tables.getString("TABLE_TYPE");
                    System.out.printf("  - %s (%s)%n", tableName, tableType);
                }
            }

            // Get columns for employees table
            System.out.println("\nColumns in 'employees' table:");
            try (ResultSet columns = dbMetaData.getColumns(null, null, "employees", "%")) {
                while (columns.next()) {
                    String columnName = columns.getString("COLUMN_NAME");
                    String columnType = columns.getString("TYPE_NAME");
                    int columnSize = columns.getInt("COLUMN_SIZE");
                    String nullable = columns.getString("IS_NULLABLE");
                    System.out.printf("  - %s: %s(%d), Nullable: %s%n",
                            columnName, columnType, columnSize, nullable);
                }
            }

            // Get primary keys
            System.out.println("\nPrimary keys for 'employees' table:");
            try (ResultSet primaryKeys = dbMetaData.getPrimaryKeys(null, null, "employees")) {
                while (primaryKeys.next()) {
                    String columnName = primaryKeys.getString("COLUMN_NAME");
                    String pkName = primaryKeys.getString("PK_NAME");
                    System.out.printf("  - Column: %s, PK Name: %s%n", columnName, pkName);
                }
            }

        } catch (SQLException e) {
            System.err.println("Database metadata example failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void resultSetMetaDataExample() {
        System.out.println("\n=== Example 2: ResultSet MetaData ===");
        String sql = "SELECT employee_id, first_name, last_name, email, salary, hire_date FROM employees LIMIT 1";

        try (Connection conn = DriverManager.getConnection(
                DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            ResultSetMetaData rsMetaData = rs.getMetaData();

            int columnCount = rsMetaData.getColumnCount();
            System.out.println("Number of columns: " + columnCount);

            System.out.println("\nColumn details:");
            for (int i = 1; i <= columnCount; i++) {
                String columnName = rsMetaData.getColumnName(i);
                String columnLabel = rsMetaData.getColumnLabel(i);
                String columnType = rsMetaData.getColumnTypeName(i);
                int columnDisplaySize = rsMetaData.getColumnDisplaySize(i);
                boolean isNullable = rsMetaData.isNullable(i) == ResultSetMetaData.columnNullable;
                boolean isAutoIncrement = rsMetaData.isAutoIncrement(i);

                System.out.printf("Column %d:%n", i);
                System.out.printf("  Name: %s%n", columnName);
                System.out.printf("  Label: %s%n", columnLabel);
                System.out.printf("  Type: %s%n", columnType);
                System.out.printf("  Display Size: %d%n", columnDisplaySize);
                System.out.printf("  Nullable: %s%n", isNullable);
                System.out.printf("  Auto-increment: %s%n", isAutoIncrement);
            }

            // Use metadata to display results dynamically
            System.out.println("\nDynamic result display:");
            if (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = rsMetaData.getColumnName(i);
                    Object value = rs.getObject(i);
                    System.out.printf("  %s: %s%n", columnName, value);
                }
            }

        } catch (SQLException e) {
            System.err.println("ResultSet metadata example failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
