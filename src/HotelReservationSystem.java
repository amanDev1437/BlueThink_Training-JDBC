import java.util.*;
import java.sql.*;

public class HotelReservationSystem {

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
            System.out.println("Press 5 for Delete Reservation:");
            System.out.println("Press 0 for exit:");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    newReservation(con, sc);
                    break;
                case 2:
                    viewReservation(st);
                    break;
                case 3:
                    getRoomNumber(sc, st);
                    break;
                case 4:
                    updateReservation(con, st, sc);
                    break;
                case 5:
                    deleteReservation(sc, st);
                    break;
                case 0:
                    exit();
                    return;
                default:
                    System.out.println("Please enter right choice:");
                    break;
            }

        }
    }

    public static void newReservation(Connection con, Scanner sc) {
        System.out.println("Enter the name of guest:");
        String guestName = sc.next();
        sc.nextLine();
        System.out.println("Assign the room number:");
        int roomNumber = sc.nextInt();
        System.out.println("Enter the contact:");
        String contactNumber = sc.next();

        String query = "insert into reservations(guestName,roomNumber,contactNumber) values(?,?,?)";

        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, guestName);
            pst.setInt(2, roomNumber);
            pst.setString(3, contactNumber);

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Reservation successful");
            } else {
                System.out.println("Reservation failed");
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }

    }

    public static void viewReservation(Statement st) {
        String q = "select * from reservations";
        try {
            ResultSet rset = st.executeQuery(q);
            while (rset.next()) {
                int resId = rset.getInt("resId");
                String guestName = rset.getString("guestName");
                int roomNumber = rset.getInt("roomNumber");
                String contactNumber = rset.getString("contactNumber");
                Timestamp resDate = rset.getTimestamp("resDate");
                System.out.println(resId + " " + guestName + " " + roomNumber + " " + contactNumber + " " + resDate);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }

    }

    public static void getRoomNumber(Scanner sc, Statement st) throws SQLException {
        System.out.println("Enter the Reservation ID:");
        int resId = sc.nextInt();

        String q = "select roomNumber from reservations where resId = " + resId;
        ResultSet rset = st.executeQuery(q);
        if (rset.next()) {
            int roomNumber = rset.getInt("roomNumber");
            System.out.println("Room number for Reservation ID " + resId + " is:" + roomNumber);
        } else {
            System.out.println("Reservation not found for given ID and guest name");
        }
    }

    public static void updateReservation(Connection con, Statement st, Scanner sc) throws SQLException {
        System.out.println("Enter the reservation id to update");
        int resId = sc.nextInt();
        sc.nextLine();
        if (isExist(st, resId)) {

            System.out.println("Enter the name to update");
            String newName = sc.next();

            System.out.println("Assign the new room number");
            int newRoomNumber = sc.nextInt();

            System.out.println("Enter the new contact number");
            String newContactNumber = sc.next();

            String q = "update reservations set guestName =?, roomNumber =?, contactNumber= ?";

            PreparedStatement pst = con.prepareStatement(q);
            pst.setString(1, newName);
            pst.setInt(2, newRoomNumber);
            pst.setString(3, newContactNumber);

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Reservation updated");
            }

        } else {
            System.out.println("Reservation does not exist with:" + resId);
        }

    }

    public static void deleteReservation(Scanner sc, Statement st) throws SQLException {
        System.out.println("Enter the Reservation ID:");
        int resId = sc.nextInt();

        String q = "delete from reservations where resId= " + resId;

        int rowsAffected = st.executeUpdate(q);

        if (rowsAffected > 0) {
            System.out.println("Reservation deleted");
        } else {
            System.out.println("Reservation id does not exist");
        }

    }

    public static void exit() {
        System.out.println("Thanks for using Hotel Resrvations System");
        System.out.println("exiting the app...");
    }

    public static boolean isExist(Statement st, int resId) throws SQLException {

        String q = "select * from reservations where resID = " + resId;

        ResultSet rset = st.executeQuery(q);
        if (rset.next()) {
            return true;
        } else {
            return false;
        }

    }

}
