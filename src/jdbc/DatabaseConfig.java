package jdbc;

public class DatabaseConfig {
    public static final String URL = "jdbc:postgresql://localhost:5432/ocp_practice";
    public static final String USER = "ocpuser";
    public static final String PASSWORD = "ocppass123";
    public static final String DRIVER = "org.postgresql.Driver";

    public static void loadDriver() {
        try {
            Class.forName(DRIVER);
            System.out.println("PostgreSQL JDBC Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            System.err.println("PostgreSQL JDBC Driver not found");
            e.printStackTrace();
        }
    }
}
