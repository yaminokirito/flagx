public class testDB{
    public static void main(String[] args) {
        try {
            java.sql.Connection conn = echo.core.Database.getConnection();
            System.out.println("[INFO] Connected to MySQL!");
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}