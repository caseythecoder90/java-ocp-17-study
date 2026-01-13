package practicetestbook.testone;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Example {

    public static void main(String[] args) throws SQLException {
        var url = "jdbc:hsqldb:file:mammals";
        var sql = "SELECT name FROM pandas WHERE location = ?";
        try (var conn = DriverManager.getConnection(url);
             var stmt = conn.prepareStatement(sql);          // s1
             var rs = stmt.executeQuery()) {
            stmt.setString(1, "DC");
            if (rs.next())
                System.out.println(rs.getString("name"));   // s2
            else
                System.out.println("No match");
        }

    }
}
