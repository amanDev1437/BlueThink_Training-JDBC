import java.sql.*;

public class Test {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/hotel";
    static final String USER = "root";
    static final String PASS = "tiger";

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;

        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();

            // Check-in a guest
            checkInGuest(conn, stmt, "John Doe", "2024-05-03", "2024-05-07", 101, "WiFi, TV");

            // Check-out a guest
            checkOutGuest(conn, stmt, 101);

            // Display available rooms
            displayAvailableRooms(stmt);

            stmt.close();
            conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    static void checkInGuest(Connection conn, Statement stmt, String fullName, String checkInDate, String checkOutDate,
            int roomNumber, String features) throws SQLException {
        String sql = "INSERT INTO Guests (FullName, CheckInDate, CheckOutDate, RoomNumber) VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, fullName);
        preparedStatement.setDate(2, Date.valueOf(checkInDate));
        preparedStatement.setDate(3, Date.valueOf(checkOutDate));
        preparedStatement.setInt(4, roomNumber);
        preparedStatement.executeUpdate();

        updateRoomStatus(stmt, roomNumber, true);

        System.out.println(fullName + " checked in successfully.");
    }

    static void checkOutGuest(Connection conn, Statement stmt, int roomNumber) throws SQLException {
        String sql = "DELETE FROM Guests WHERE RoomNumber=?";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setInt(1, roomNumber);
        preparedStatement.executeUpdate();

        updateRoomStatus(stmt, roomNumber, false);

        System.out.println("Guest checked out from room " + roomNumber + ".");
    }

    static void updateRoomStatus(Statement stmt, int roomNumber, boolean isOccupied) throws SQLException {
        String sql = "UPDATE Rooms SET IsOccupied=? WHERE RoomNumber=?";
        PreparedStatement preparedStatement = stmt.getConnection().prepareStatement(sql);
        preparedStatement.setBoolean(1, isOccupied);
        preparedStatement.setInt(2, roomNumber);
        preparedStatement.executeUpdate();
    }

    static void displayAvailableRooms(Statement stmt) throws SQLException {
        String sql = "SELECT RoomNumber, RoomType, Features FROM Rooms WHERE IsOccupied = FALSE";
        ResultSet rs = stmt.executeQuery(sql);
        System.out.println("Available Rooms:");
        while (rs.next()) {
            int roomNumber = rs.getInt("RoomNumber");
            String roomType = rs.getString("RoomType");
            String features = rs.getString("Features");
            System.out.println("Room Number: " + roomNumber + ", Type: " + roomType + ", Features: " + features);
        }
        rs.close();
    }
}
