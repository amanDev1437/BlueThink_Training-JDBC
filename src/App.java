import java.sql.*;

public class App {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://localhost:3306/user";
            String username = "root";
            String password = "tiger";

            Connection con = DriverManager.getConnection(url, username, password);

            if (con.isClosed()) {
                System.out.println("Connection is Closed");
            } else {
                System.out.println("Connected to Database");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
