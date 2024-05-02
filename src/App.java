import java.sql.*;

public class App {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://localhost:3306/world";
            String username = "root";
            String password = "tiger";

            Connection con = DriverManager.getConnection(url, username, password);

            if (con.isClosed()) {
                System.out.println("Connection is Closed");
            } else {
                System.out.println("Connected to Database");
            }

            Statement st = con.createStatement();

            // String query = "select * from city";
            // ResultSet rs = st.executeQuery(query);

            // while (rs.next()) {
            // // System.out.println(rs.getInt(1) + " " + rs.getString(2));
            // }

            // ResultSetMetaData rsmd = rs.getMetaData();
            // // System.out.println(rsmd);

            // DatabaseMetaData dbmd = con.getMetaData();
            // System.out.println(dbmd.getDatabaseProductName());
            // System.out.println(dbmd.getDriverVersion());
            // System.out.println(dbmd.getDriverName());

            // Insert data into table
            String name = "AmanSingh01";
            String city = "Prayagraj";
            String q = "insert into table1(name, city) values(?,?)";

            PreparedStatement pst = con.prepareStatement(q);
            pst.setString(1, name);
            pst.setString(2, city);

            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("inserted");
            } else {
                System.out.println("something went wrong");
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
