import java.util.*;
import java.sql.*;

public class RoomBook {
    static String url = "jdbc:mysql://localhost:3306/hotel";
    static String username = "root";
    static String password = "tiger";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.cj.jdbc.Driver");

        Connection con = DriverManager.getConnection(url, username, password);

        Statement st = con.createStatement();

        if (con.isClosed()) {
            System.out.println("Connection closed");
        } else {
            System.out.println("Connected to DB");
        }

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("Hotel Management System:");
            System.out.println("Press 1 for new Reservation:");
            System.out.println("Press 2 for view Reservations:");
            System.out.println("Press 3 for get Room number:");
            System.out.println("Press 4 for Update Reservation:");
            System.out.println("Press 5 to view availabel room");
            System.out.println("Press 6 for Delete Reservation:");
            System.out.println("Press 0 for exit:");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    newReservation(sc, con, st);
                    break;
                case 2:
                    viewReservation(st);
                    break;
                case 3:
                    getRoomNumber(st, sc);
                    break;
                case 4:
                    updateReservation(con, st, sc);
                    break;
                case 5:
                    viewAvailabelRoom(st);
                    break;
                case 6:
                    checkOutGuest(con, st, sc);
                    break;
                case 0:
                    // exit();
                    return;
                default:
                    System.out.println("Please enter right choice:");
                    break;
            }

        }
    }

    public static void newReservation(Scanner sc, Connection con, Statement st) throws SQLException {
        System.out.println("Enter the guest name:");
        String guestName = sc.next();
        sc.nextLine();
        System.out.println("Enter the contact number:");
        String contactNumber = sc.next();
        System.out.println("Assign the room number");
        int roomNumber = sc.nextInt();

        if (isRoomAvailable(st, roomNumber)) {
            String query = "insert into guests(guestName, roomNumber,contactNumber) values(?,?,?)";

            PreparedStatement pst = con.prepareStatement(query);

            pst.setString(1, guestName);
            pst.setInt(2, roomNumber);
            pst.setString(3, contactNumber);

            int rowsInserted = pst.executeUpdate();
            updateRoomStatus(con, roomNumber, true);
            if (rowsInserted > 0) {
                System.out.println("Reservation Successful");
            } else {
                System.out.println("Reservation failed");
            }

        } else {
            System.out.println("Room number is already reserved");
        }

    }

    public static void viewReservation(Statement st) throws SQLException {
        String query = "select * from guests";

        ResultSet rset = st.executeQuery(query);
        while (rset.next()) {
            int guestId = rset.getInt("guestID");
            String guestName = rset.getString("guestName");
            int roomNumber = rset.getInt("roomNumber");
            String contactNumber = rset.getString("contactNumber");
            Timestamp checkInDate = rset.getTimestamp("checkInDate");

            System.out.println(guestId + " " + guestName + " " + roomNumber + " " + contactNumber + " " + checkInDate);

        }
    }

    public static void getRoomNumber(Statement st, Scanner sc) throws SQLException {
        System.out.println("Enter the guestID:");
        int guestID = sc.nextInt();

        String q = "select roomNumber from guests where guestId= " + guestID;

        ResultSet rset = st.executeQuery(q);

        if (rset.next()) {
            int roomNumber = rset.getInt("roomNumber");
            System.out.println("Room number of guestId " + guestID + " is:" + roomNumber);
        } else {
            System.out.println("Reservation not found for given guestId");
        }

    }

    public static void updateReservation(Connection con, Statement st, Scanner sc) throws SQLException {
        System.out.println("Enter guest ID to Update");
        int guestId = sc.nextInt();

        if (isExist(st, guestId)) {
            System.out.println("Enter the name to update");
            String newGuestName = sc.next();
            sc.nextLine();
            System.out.println("Assign new room number");
            int newRoomNumber = sc.nextInt();
            System.out.println("Enter the new contact number");
            String newContactNumber = sc.next();

            String q = "update guests set guestName= ?, roomNumber= ?, contactNumber= ? where guestId= " + guestId;

            PreparedStatement pst = con.prepareStatement(q);

            pst.setString(1, newGuestName);
            pst.setInt(2, newRoomNumber);
            pst.setString(3, newContactNumber);

            int rowsAffected = pst.executeUpdate();

            updateRoomStatus(con, newRoomNumber, true);

            if (rowsAffected > 0) {
                System.out.println("Updated");
            } else {
                System.out.println("Failed");
            }

        }
    }

    public static void checkOutGuest(Connection con, Statement st, Scanner sc) throws SQLException {
        System.out.println("Enter the room number:");
        int roomNumber = sc.nextInt();
        String q = "delete from guests where roomNumber = " + roomNumber;

        PreparedStatement pst = con.prepareStatement(q);
        int rowsAffected = pst.executeUpdate();
        if (rowsAffected > 0) {
            updateRoomStatus(con, roomNumber, false);
            System.out.println("Guest check out from room number:" + roomNumber);
        }

    }

    public static boolean isExist(Statement st, int guestId) throws SQLException {
        String q = "select * from guests where guestId= " + guestId;

        ResultSet rset = st.executeQuery(q);

        if (rset.next()) {
            return true;
        } else {
            return false;
        }
    }

    public static void updateRoomStatus(Connection con, int roomNumber, boolean isOccupied) throws SQLException {
        String query = "update rooms set isOccupied=? where roomNumber=?";

        PreparedStatement pst = con.prepareStatement(query);

        pst.setBoolean(1, isOccupied);
        pst.setInt(2, roomNumber);

        pst.executeUpdate();

    }

    public static void viewAvailabelRoom(Statement st) {
        String q = "select roomNumber from rooms where isOccupied=" + false;

        try {
            ResultSet rset = st.executeQuery(q);
            while (rset.next()) {
                int availableRoom = rset.getInt(1);

                System.out.println(availableRoom);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static boolean isRoomAvailable(Statement st, int roomNumber) throws SQLException {
        String q = "select * from rooms where roomNumber= " + roomNumber;

        ResultSet rset = st.executeQuery(q);

        if (rset.next()) {
            return false;
        } else {
            return true;
        }

    }
}
