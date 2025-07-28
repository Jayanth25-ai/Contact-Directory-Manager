package contacts;

import java.sql.*;
import java.util.Scanner;

public class Contactdirectory {
    static final String URL = "jdbc:oracle:thin:@Dharshan:1521:XE";
    static final String USER = "jayanth";
    static final String PASS = "jayanth25";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to database.");

            while (true) {
                System.out.println("\n--- Contact Directory Menu ---");
                System.out.println("1. Insert Contact");
                System.out.println("2. View Contacts");
                System.out.println("3. Update Contact");
                System.out.println("4. Delete Contact");
                System.out.println("5. Insert Address");
                System.out.println("6. View Addresses");
                System.out.println("7. Insert Call Log");
                System.out.println("8. View Call Logs");
                System.out.println("9. Exit");
                System.out.println("10. Delete Call Log");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1: insertContact(conn, scanner); break;
                    case 2: viewContacts(conn); break;
                    case 3: updateContact(conn, scanner); break;
                    case 4: deleteContact(conn, scanner); break;
                    case 5: insertAddress(conn, scanner); break;
                    case 6: viewAddresses(conn); break;
                    case 7: insertCallLog(conn, scanner); break;
                    case 8: viewCallLogs(conn); break;
                    case 9:
                        System.out.println("Exiting...");
                        return;
                    case 10: deleteCallLog(conn, scanner); break;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            }

        } catch (SQLException e) {
            System.err.println("Database connection failed!");
            e.printStackTrace();
        }
    }

    private static void insertContact(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Phone: ");
        String phone = scanner.nextLine();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter DOB (YYYY-MM-DD): ");
        String dob = scanner.nextLine();

        String sql = "INSERT INTO contacts (id, name, phone, email, dob) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setString(3, phone);
            ps.setString(4, email);
            ps.setString(5, dob);
            int res = ps.executeUpdate();
            System.out.println(res + " contact inserted.");
        }
    }

    private static void viewContacts(Connection conn) throws SQLException {
        String sql = "SELECT * FROM contacts";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\n--- Contacts ---");
            while (rs.next()) {
                System.out.printf("ID: %d, Name: %s, Phone: %s, Email: %s, DOB: %s%n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("dob"));
            }
        }
    }

    private static void updateContact(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter Contact ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter New Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter New Phone: ");
        String phone = scanner.nextLine();
        System.out.print("Enter New Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter New DOB (YYYY-MM-DD): ");
        String dob = scanner.nextLine();

        String sql = "UPDATE contacts SET name=?, phone=?, email=?, dob=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setString(3, email);
            ps.setString(4, dob);
            ps.setInt(5, id);
            int res = ps.executeUpdate();
            System.out.println(res + " contact(s) updated.");
        }
    }

    private static void deleteContact(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter Contact ID to delete: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM call_logs WHERE contact_id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }

        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM address WHERE contact_id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }

        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM contacts WHERE id=?")) {
            ps.setInt(1, id);
            int res = ps.executeUpdate();
            System.out.println(res + " contact(s) deleted.");
        }
    }

    private static void insertAddress(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter Address ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Contact ID: ");
        int contactId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Street: ");
        String street = scanner.nextLine();
        System.out.print("Enter City: ");
        String city = scanner.nextLine();
        System.out.print("Enter Pincode: ");
        String pincode = scanner.nextLine();

        String sql = "INSERT INTO address (id, contact_id, street, city, pincode) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setInt(2, contactId);
            ps.setString(3, street);
            ps.setString(4, city);
            ps.setString(5, pincode);
            int res = ps.executeUpdate();
            System.out.println(res + " address inserted.");
        }
    }

    private static void viewAddresses(Connection conn) throws SQLException {
        String sql = "SELECT * FROM address";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n--- Addresses ---");
            while (rs.next()) {
                System.out.printf("ID: %d, Contact ID: %d, Street: %s, City: %s, Pincode: %s%n",
                        rs.getInt("id"),
                        rs.getInt("contact_id"),
                        rs.getString("street"),
                        rs.getString("city"),
                        rs.getString("pincode"));
            }
        }
    }

    private static void insertCallLog(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter Call Log ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Contact ID: ");
        int contactId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Call Time (YYYY-MM-DD HH24:MI:SS): ");
        String callTime = scanner.nextLine();
        System.out.print("Enter Call Duration in minutes: ");
        int durationMinutes = scanner.nextInt();
        scanner.nextLine();

        String sql = "INSERT INTO call_logs (id, contact_id, call_time, duration_minutes) VALUES (?, ?, TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI:SS'), ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setInt(2, contactId);
            ps.setString(3, callTime);
            ps.setInt(4, durationMinutes);
            int res = ps.executeUpdate();
            System.out.println(res + " call log inserted.");
        }
    }

    private static void viewCallLogs(Connection conn) throws SQLException {
        String sql = "SELECT * FROM call_logs ORDER BY id";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n--- Call Logs ---");
            while (rs.next()) {
                int id = rs.getInt("id");
                int contactId = rs.getInt("contact_id");
                Timestamp callTime = rs.getTimestamp("call_time");
                int durationMin = rs.getInt("duration_minutes");

                String formattedDuration = formatDuration(durationMin * 60);  // convert minutes to seconds
                System.out.printf("ID: %d, Contact ID: %d, Call Time: %s, Duration: %s%n",
                        id, contactId, callTime.toString(), formattedDuration);
            }
        }
    }

    private static void deleteCallLog(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter Call Log ID to delete: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        String sql = "DELETE FROM call_logs WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int res = ps.executeUpdate();
            if (res > 0) {
                System.out.println("Call log deleted successfully.");
            } else {
                System.out.println("No call log found with the given ID.");
            }
        }
    }

    private static String formatDuration(int totalSeconds) {
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;
        return String.format("%d hr %d min %d sec", hours, minutes, seconds);
    }
}
