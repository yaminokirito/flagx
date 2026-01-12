import echo.core.Database;
import java.sql.Connection;

public class test {
    public static void main(String[] args) {
        try {
            Connection conn = Database.getConnection();
            if (conn != null) {
                System.out.println("Connected to MySQL database successfully!");
            } else {
                System.out.println("Failed to connect.");
            }
        } catch (java.sql.SQLException e) {
            System.out.println("SQLException occurred: " + e.getMessage());
        }
    }
}