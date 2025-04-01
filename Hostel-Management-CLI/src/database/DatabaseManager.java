package database;

import java.sql.*;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class DatabaseManager {
    private static Connection connection = null;
    
    public static void initializeDatabase() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n=== Database Configuration ===");
        System.out.print("Enter MySQL username: ");
        String username = scanner.nextLine();
        
        System.out.print("Enter MySQL password: ");
        String password = scanner.nextLine();
        
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Connect to MySQL server
            connection = DriverManager.getConnection("jdbc:mysql://localhost", username, password);
            Statement stmt = connection.createStatement();
            
            // Create database
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS hostel_management");
            stmt.executeUpdate("USE hostel_management");
            
            // Create tables
            createTables(stmt);
            
            // Insert hardcoded data
            insertInitialData(stmt);
            
            System.out.println("✓ Database initialized successfully!");
            
        } catch (Exception e) {
            System.out.println("❌ Database initialization failed: " + e.getMessage());
            System.exit(1);
        }
    }
    
    private static void createTables(Statement stmt) throws SQLException {
        // Admin table
        stmt.executeUpdate(
            "CREATE TABLE IF NOT EXISTS admins (" +
            "username VARCHAR(50) PRIMARY KEY," +
            "password VARCHAR(50) NOT NULL" +
            ")"
        );
        
        // Student table
        stmt.executeUpdate(
            "CREATE TABLE IF NOT EXISTS students (" +
            "roll_no VARCHAR(20) PRIMARY KEY," +
            "name VARCHAR(100) NOT NULL," +
            "college VARCHAR(100)," +
            "department VARCHAR(50)," +
            "semester VARCHAR(20)," +
            "age INT," +
            "mobile VARCHAR(15)," +
            "room_no VARCHAR(10)," +
            "room_type VARCHAR(20)," +
            "sharing_type VARCHAR(20)," +
            "block_name VARCHAR(20)," +
            "floor_no INT," +
            "amount_paid DECIMAL(10,2)," +
            "amount_due DECIMAL(10,2)," +
            "password VARCHAR(50) NOT NULL" +
            ")"
        );

        // Add Wardens table
        stmt.executeUpdate(
            "CREATE TABLE IF NOT EXISTS wardens (" +
            "warden_id VARCHAR(20) PRIMARY KEY," +
            "name VARCHAR(100) NOT NULL," +
            "age INT," +
            "mobile VARCHAR(15)," +
            "assigned_hostel VARCHAR(50)," +
            "block_name VARCHAR(20)," +
            "joining_date DATE" +
            ")"
        );

        // Add Complaints table
        stmt.executeUpdate(
            "CREATE TABLE IF NOT EXISTS complaints (" +
            "complaint_id INT AUTO_INCREMENT PRIMARY KEY," +
            "student_roll_no VARCHAR(20)," +
            "complaint_text TEXT," +
            "status VARCHAR(20)," +
            "filing_date DATETIME," +
            "resolution_date DATETIME," +
            "FOREIGN KEY (student_roll_no) REFERENCES students(roll_no)" +
            ")"
        );

        // Create rooms table
        stmt.executeUpdate(
            "CREATE TABLE IF NOT EXISTS rooms (" +
            "room_no VARCHAR(10) PRIMARY KEY," +
            "room_type VARCHAR(20) NOT NULL," +
            "capacity INT NOT NULL," +
            "current_occupancy INT DEFAULT 0," +
            "floor_no INT," +
            "block_name VARCHAR(20)" +
            ")"
        );

        // Add initial rooms if the rooms table is empty
        ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM rooms");
        rs.next();
        if (rs.getInt(1) == 0) {
            // Add Standard Rooms (101-105)
            for (int i = 1; i <= 5; i++) {
                String roomNo = "10" + i;
                stmt.executeUpdate(
                    "INSERT INTO rooms (room_no, room_type, capacity, current_occupancy, floor_no, block_name) " +
                    "VALUES ('" + roomNo + "', 'Standard', 4, 0, 1, 'A Block')"
                );
            }

            // Add Luxury Rooms (201-205)
            for (int i = 1; i <= 5; i++) {
                String roomNo = "20" + i;
                stmt.executeUpdate(
                    "INSERT INTO rooms (room_no, room_type, capacity, current_occupancy, floor_no, block_name) " +
                    "VALUES ('" + roomNo + "', 'Luxury', 4, 0, 2, 'B Block')"
                );
            }
            System.out.println("✓ Initial rooms added successfully!");
        }
    }
    
    private static void insertInitialData(Statement stmt) throws SQLException {
        // Insert default admin
        stmt.executeUpdate(
            "INSERT IGNORE INTO admins (username, password) VALUES " +
            "('admin', 'admin123')"
        );
        
        // Insert sample students
        stmt.executeUpdate(
            "INSERT IGNORE INTO students VALUES " +
            "('23000929', 'Harsh', 'NUV', 'BTech', '4th', 20, '9876543210', '101', 'Standard', 'No Sharing', 'A Block', 1, 129000, 1000, '101962AY')," +
            "('23000930', 'Priya', 'NUV', 'BTech', '3rd', 19, '9876543211', '102', 'Standard', '2 Sharing', 'A Block', 1, 100000, 20000, 'priya123')," +
            "('23000931', 'Raj', 'NUV', 'MTech', '2nd', 22, '9876543212', '103', 'Standard', '4 Sharing', 'A Block', 1, 55000, 5000, 'raj123')"
        );

        // Insert sample wardens
        stmt.executeUpdate(
            "INSERT IGNORE INTO wardens VALUES " +
            "('W001', 'Ramesh Kumar', 45, '9876543210', 'DHHP', 'A Block', '2025-01-01')," +
            "('W002', 'Suresh Verma', 42, '9876543211', 'DHHP', 'B Block', '2025-01-01')"
        );

        // Insert sample complaint
        stmt.executeUpdate(
            "INSERT IGNORE INTO complaints (student_roll_no, complaint_text, status, filing_date) VALUES " +
            "('23000929', 'AC not working properly in room 101. Please check and repair.', 'Pending', NOW())"
        );

        // Update room occupancies after inserting sample students
        stmt.executeUpdate(
            "UPDATE rooms r SET current_occupancy = (" +
            "SELECT COUNT(*) FROM students s WHERE s.room_no = r.room_no)"
        );
        
        System.out.println("✓ Initial data inserted successfully!");
    }
    
    public static Connection getConnection() {
        return connection;
    }
    
    // CRUD Operations for Students
    public static void displayAllStudents() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM students");
            
            System.out.println("\n=== Student Records ===");
            while (rs.next()) {
                System.out.println("\nRoll No: " + rs.getString("roll_no"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("College: " + rs.getString("college"));
                System.out.println("Department: " + rs.getString("department"));
                System.out.println("Semester: " + rs.getString("semester"));
                System.out.println("Room: " + rs.getString("room_no") + " (" + rs.getString("room_type") + ", " + rs.getString("sharing_type") + ")");
                System.out.println("Payment Status: Paid=" + rs.getDouble("amount_paid") + ", Due=" + rs.getDouble("amount_due"));
                System.out.println("------------------------");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error displaying students: " + e.getMessage());
        }
    }
    
    public static void updateStudent(String rollNo) {
        Scanner scanner = new Scanner(System.in);
        try {
            // First check if student exists
            String checkSql = "SELECT * FROM students WHERE roll_no = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkSql);
            checkStmt.setString(1, rollNo);
            ResultSet rs = checkStmt.executeQuery();
            
            if (!rs.next()) {
                System.out.println("❌ Student not found!");
                return;
            }

            // Store current student details
            String currentName = rs.getString("name");
            String currentMobile = rs.getString("mobile");
            String currentRoom = rs.getString("room_no");
            double currentAmountPaid = rs.getDouble("amount_paid");
            double currentAmountDue = rs.getDouble("amount_due");

            System.out.println("\n=== Update Student ===");
            System.out.println("1. Update Name");
            System.out.println("2. Update Mobile");
            System.out.println("3. Update Room");
            System.out.println("4. Update Payment");
            System.out.print("Enter choice: ");
            
            int choice = Integer.parseInt(scanner.nextLine());
            String sql = "";
            PreparedStatement stmt;
            
            switch (choice) {
                case 1:
                    System.out.print("Enter new name: ");
                    String newName = scanner.nextLine();
                    if (newName.equals(currentName)) {
                        System.out.println("❌ New name is same as current name!");
                        return;
                    }
                    sql = "UPDATE students SET name = ? WHERE roll_no = ?";
                    stmt = connection.prepareStatement(sql);
                    stmt.setString(1, newName);
                    stmt.setString(2, rollNo);
                    break;
                    
                case 2:
                    System.out.print("Enter new mobile: ");
                    String newMobile = scanner.nextLine();
                    if (newMobile.equals(currentMobile)) {
                        System.out.println("❌ New mobile is same as current mobile!");
                        return;
                    }
                    sql = "UPDATE students SET mobile = ? WHERE roll_no = ?";
                    stmt = connection.prepareStatement(sql);
                    stmt.setString(1, newMobile);
                    stmt.setString(2, rollNo);
                    break;
                    
                case 3:
                    System.out.println("\n=== Available Rooms ===");
                    displayAvailableRooms();
                    System.out.print("Enter new room number: ");
                    String newRoom = scanner.nextLine();
                    if (newRoom.equals(currentRoom)) {
                        System.out.println("❌ New room is same as current room!");
                        return;
                    }
                    // First check if the new room exists and has space
                    String checkRoomSql = "SELECT capacity, current_occupancy, room_type, block_name, floor_no FROM rooms WHERE room_no = ?";
                    PreparedStatement checkRoomStmt = connection.prepareStatement(checkRoomSql);
                    checkRoomStmt.setString(1, newRoom);
                    ResultSet roomRs = checkRoomStmt.executeQuery();
                    
                    if (!roomRs.next()) {
                        System.out.println("❌ Room not found!");
                        return;
                    }
                    
                    if (roomRs.getInt("current_occupancy") >= roomRs.getInt("capacity")) {
                        System.out.println("❌ Room is already full!");
                        return;
                    }
                    
                    // Get current sharing type from student's record
                    String currentSharingType = rs.getString("sharing_type");
                    
                    // Update student's room with all related information
                    sql = "UPDATE students SET room_no = ?, room_type = ?, block_name = ?, floor_no = ? WHERE roll_no = ?";
                    stmt = connection.prepareStatement(sql);
                    stmt.setString(1, newRoom);
                    stmt.setString(2, roomRs.getString("room_type"));
                    stmt.setString(3, roomRs.getString("block_name"));
                    stmt.setInt(4, roomRs.getInt("floor_no"));
                    stmt.setString(5, rollNo);
                    int roomUpdateResult = stmt.executeUpdate();
                    
                    if (roomUpdateResult > 0) {
                        // Update room occupancy
                        updateRoomOccupancy();
                        System.out.println("✓ Room updated successfully!");
                        return;
                    } else {
                        System.out.println("❌ Failed to update room!");
                        return;
                    }
                    
                case 4:
                    System.out.print("Enter new amount paid: ");
                    double newAmountPaid = Double.parseDouble(scanner.nextLine());
                    if (newAmountPaid == currentAmountPaid) {
                        System.out.println("❌ New amount is same as current amount!");
                        return;
                    }
                    // Calculate new amount due based on total fee
                    double totalFee = calculateFee(rs.getString("room_type"), rs.getString("sharing_type"));
                    double newAmountDue = totalFee - newAmountPaid;
                    
                    sql = "UPDATE students SET amount_paid = ?, amount_due = ? WHERE roll_no = ?";
                    stmt = connection.prepareStatement(sql);
                    stmt.setDouble(1, newAmountPaid);
                    stmt.setDouble(2, newAmountDue);
                    stmt.setString(3, rollNo);
                    int paymentUpdateResult = stmt.executeUpdate();
                    
                    if (paymentUpdateResult > 0) {
                        System.out.println("✓ Payment status updated successfully!");
                        System.out.printf("New Payment Status: Paid=%.2f, Due=%.2f\n", newAmountPaid, newAmountDue);
                        return;
                    } else {
                        System.out.println("❌ Failed to update payment status!");
                        return;
                    }
                    
                default:
                    System.out.println("❌ Invalid choice!");
                    return;
            }
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✓ Student details updated successfully!");
            } else {
                System.out.println("❌ Failed to update student details!");
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Error updating student: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid payment amount format!");
        }
    }
    
    public static boolean validateStudentLogin(String rollNo, String password) {
        try {
            PreparedStatement pstmt = connection.prepareStatement(
                "SELECT * FROM students WHERE roll_no = ? AND password = ?"
            );
            pstmt.setString(1, rollNo);
            pstmt.setString(2, password);
            
            return pstmt.executeQuery().next();
        } catch (SQLException e) {
            System.out.println("❌ Error validating student login: " + e.getMessage());
            return false;
        }
    }
    
    public static boolean validateAdminLogin(String username, String password) {
        try {
            PreparedStatement pstmt = connection.prepareStatement(
                "SELECT * FROM admins WHERE username = ? AND password = ?"
            );
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            return pstmt.executeQuery().next();
        } catch (SQLException e) {
            System.out.println("❌ Error validating admin login: " + e.getMessage());
            return false;
        }
    }

    // Warden Management Methods
    public static void displayAllWardens() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM wardens");
            
            System.out.println("\n=== Warden Records ===");
            System.out.println("Warden ID | Name         | Age | Mobile       | Hostel      | Block    | Joining Date");
            System.out.println("----------------------------------------------------------------------------------------");
            while (rs.next()) {
                System.out.printf("%-9s | %-11s | %-3d | %-11s | %-11s | %-8s | %s\n",
                    rs.getString("warden_id"),
                    rs.getString("name"),
                    rs.getInt("age"),
                    rs.getString("mobile"),
                    rs.getString("assigned_hostel"),
                    rs.getString("block_name"),
                    rs.getDate("joining_date"));
            }
            System.out.println("----------------------------------------------------------------------------------------");
        } catch (SQLException e) {
            System.out.println("❌ Error displaying wardens: " + e.getMessage());
        }
    }

    public static void addNewWarden() {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("\n=== Add New Warden ===");
            System.out.print("Enter Warden ID (e.g., W003): ");
            String wardenId = scanner.nextLine();
            
            System.out.print("Enter Name: ");
            String name = scanner.nextLine();
            
            System.out.print("Enter Age: ");
            int age = Integer.parseInt(scanner.nextLine());
            
            System.out.print("Enter Mobile: ");
            String mobile = scanner.nextLine();
            
            System.out.print("Enter Assigned Hostel: ");
            String hostel = scanner.nextLine();

            System.out.print("Enter Block Name: ");
            String blockName = scanner.nextLine();

            String sql = "INSERT INTO wardens VALUES (?, ?, ?, ?, ?, ?, CURDATE())";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, wardenId);
            pstmt.setString(2, name);
            pstmt.setInt(3, age);
            pstmt.setString(4, mobile);
            pstmt.setString(5, hostel);
            pstmt.setString(6, blockName);
            
            pstmt.executeUpdate();
            System.out.println("✓ Warden added successfully!");
        } catch (SQLException e) {
            System.out.println("❌ Error adding warden: " + e.getMessage());
        }
    }

    public static void updateWarden(String wardenId) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("\n=== Update Warden ===");
            System.out.println("1. Update Name");
            System.out.println("2. Update Mobile");
            System.out.println("3. Update Assigned Hostel");
            System.out.println("4. Update Block Name");
            System.out.print("Enter choice: ");
            
            int choice = Integer.parseInt(scanner.nextLine());
            String sql = "";
            String newValue = "";
            
            switch (choice) {
                case 1:
                    System.out.print("Enter new name: ");
                    newValue = scanner.nextLine();
                    sql = "UPDATE wardens SET name = ? WHERE warden_id = ?";
                    break;
                case 2:
                    System.out.print("Enter new mobile: ");
                    newValue = scanner.nextLine();
                    sql = "UPDATE wardens SET mobile = ? WHERE warden_id = ?";
                    break;
                case 3:
                    System.out.print("Enter new hostel: ");
                    newValue = scanner.nextLine();
                    sql = "UPDATE wardens SET assigned_hostel = ? WHERE warden_id = ?";
                    break;
                case 4:
                    System.out.print("Enter new block name: ");
                    newValue = scanner.nextLine();
                    sql = "UPDATE wardens SET block_name = ? WHERE warden_id = ?";
                    break;
                default:
                    System.out.println("❌ Invalid choice!");
                    return;
            }
            
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, newValue);
            pstmt.setString(2, wardenId);
            
            if (pstmt.executeUpdate() > 0) {
                System.out.println("✓ Warden updated successfully!");
            } else {
                System.out.println("❌ Warden not found!");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error updating warden: " + e.getMessage());
        }
    }

    public static void deleteWarden(String wardenId) {
        try {
            String sql = "DELETE FROM wardens WHERE warden_id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, wardenId);
            
            if (pstmt.executeUpdate() > 0) {
                System.out.println("✓ Warden deleted successfully!");
            } else {
                System.out.println("❌ Warden not found!");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error deleting warden: " + e.getMessage());
        }
    }

    public static void displayPendingComplaints() {
        try {
            String sql = "SELECT c.*, s.name FROM complaints c JOIN students s ON c.student_roll_no = s.roll_no WHERE c.status = 'Pending'";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            System.out.println("\n=== Pending Complaints ===");
            while (rs.next()) {
                System.out.println("\nComplaint ID: " + rs.getInt("complaint_id"));
                System.out.println("Student: " + rs.getString("name") + " (" + rs.getString("student_roll_no") + ")");
                System.out.println("Complaint: " + rs.getString("complaint_text"));
                System.out.println("Filed On: " + rs.getTimestamp("filing_date"));
                System.out.println("------------------------");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error displaying pending complaints: " + e.getMessage());
        }
    }

    public static void addNewComplaint() {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("\n=== Add New Complaint ===");
            System.out.print("Enter Student Roll Number: ");
            String rollNo = scanner.nextLine();
            
            System.out.println("Enter Complaint Text: ");
            String complaintText = scanner.nextLine();
            
            String sql = "INSERT INTO complaints (student_roll_no, complaint_text, status, filing_date) VALUES (?, ?, 'Pending', NOW())";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, rollNo);
            pstmt.setString(2, complaintText);
            
            pstmt.executeUpdate();
            System.out.println("✓ Complaint added successfully!");
        } catch (SQLException e) {
            System.out.println("❌ Error adding complaint: " + e.getMessage());
        }
    }

    public static void deleteComplaint(int complaintId) {
        try {
            String sql = "DELETE FROM complaints WHERE complaint_id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, complaintId);
            
            if (pstmt.executeUpdate() > 0) {
                System.out.println("✓ Complaint deleted successfully!");
            } else {
                System.out.println("❌ Complaint not found!");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error deleting complaint: " + e.getMessage());
        }
    }

    public static void searchStudentByRoll(String rollNo) {
        try {
            String sql = "SELECT * FROM students WHERE roll_no = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, rollNo);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                displayStudentRecord(rs);
            } else {
                System.out.println("❌ Student not found!");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error searching student: " + e.getMessage());
        }
    }

    public static void searchStudentByName(String name) {
        try {
            String sql = "SELECT * FROM students WHERE name LIKE ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, "%" + name + "%");
            ResultSet rs = pstmt.executeQuery();
            
            boolean found = false;
            while (rs.next()) {
                displayStudentRecord(rs);
                found = true;
            }
            
            if (!found) {
                System.out.println("❌ No students found with that name!");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error searching students: " + e.getMessage());
        }
    }

    public static void displayStudentDetails(String rollNo) {
        try {
            String sql = "SELECT * FROM students WHERE roll_no = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, rollNo);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                displayStudentRecord(rs);
            } else {
                System.out.println("❌ Student details not found!");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error displaying student details: " + e.getMessage());
        }
    }

    public static void displayRoomDetails(String rollNo) {
        try {
            String sql = "SELECT room_no, room_type, sharing_type FROM students WHERE roll_no = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, rollNo);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                System.out.println("\n=== Room Details ===");
                System.out.println("Room Number: " + rs.getString("room_no"));
                System.out.println("Room Type: " + rs.getString("room_type"));
                System.out.println("Sharing Type: " + rs.getString("sharing_type"));
            } else {
                System.out.println("❌ Room details not found!");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error displaying room details: " + e.getMessage());
        }
    }

    public static void displayPaymentStatus(String rollNo) {
        try {
            String sql = "SELECT amount_paid, amount_due FROM students WHERE roll_no = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, rollNo);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                System.out.println("\n=== Payment Status ===");
                System.out.println("Amount Paid: Rs." + rs.getDouble("amount_paid"));
                System.out.println("Amount Due: Rs." + rs.getDouble("amount_due"));
            } else {
                System.out.println("❌ Payment details not found!");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error displaying payment status: " + e.getMessage());
        }
    }

    private static void displayStudentRecord(ResultSet rs) throws SQLException {
        System.out.println("\n=== Student Details ===");
        System.out.println("Roll Number: " + rs.getString("roll_no"));
        System.out.println("Name: " + rs.getString("name"));
        System.out.println("College: " + rs.getString("college"));
        System.out.println("Department: " + rs.getString("department"));
        System.out.println("Semester: " + rs.getString("semester"));
        System.out.println("Age: " + rs.getInt("age"));
        System.out.println("Mobile: " + rs.getString("mobile"));
        if (rs.getString("room_no") != null) {
            System.out.println("Room: " + rs.getString("room_no") + " (" + rs.getString("room_type") + ", " + rs.getString("sharing_type") + ")");
            System.out.println("Block: " + rs.getString("block_name"));
            System.out.println("Floor: " + rs.getInt("floor_no"));
        }
        System.out.println("------------------------");
    }

    public static void searchWardenById(String wardenId) {
        try {
            String sql = "SELECT * FROM wardens WHERE warden_id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, wardenId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                System.out.println("\n=== Warden Details ===");
                System.out.println("Warden ID: " + rs.getString("warden_id"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Age: " + rs.getInt("age"));
                System.out.println("Mobile: " + rs.getString("mobile"));
                System.out.println("Assigned Hostel: " + rs.getString("assigned_hostel"));
                System.out.println("Joining Date: " + rs.getDate("joining_date"));
            } else {
                System.out.println("❌ Warden not found!");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error searching warden: " + e.getMessage());
        }
    }

    public static void searchWardenByName(String name) {
        try {
            String sql = "SELECT * FROM wardens WHERE name LIKE ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, "%" + name + "%");
            ResultSet rs = pstmt.executeQuery();
            
            boolean found = false;
            while (rs.next()) {
                System.out.println("\n=== Warden Details ===");
                System.out.println("Warden ID: " + rs.getString("warden_id"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Age: " + rs.getInt("age"));
                System.out.println("Mobile: " + rs.getString("mobile"));
                System.out.println("Assigned Hostel: " + rs.getString("assigned_hostel"));
                System.out.println("Joining Date: " + rs.getDate("joining_date"));
                System.out.println("------------------------");
                found = true;
            }
            
            if (!found) {
                System.out.println("❌ No wardens found with that name!");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error searching wardens: " + e.getMessage());
        }
    }

    public static void searchComplaintsByStudent(String rollNo) {
        try {
            String sql = "SELECT c.*, s.name FROM complaints c " +
                        "JOIN students s ON c.student_roll_no = s.roll_no " +
                        "WHERE c.student_roll_no = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, rollNo);
            ResultSet rs = pstmt.executeQuery();
            
            boolean found = false;
            while (rs.next()) {
                System.out.println("\n=== Complaint Details ===");
                System.out.println("Complaint ID: " + rs.getInt("complaint_id"));
                System.out.println("Student: " + rs.getString("name") + " (" + rs.getString("student_roll_no") + ")");
                System.out.println("Complaint: " + rs.getString("complaint_text"));
                System.out.println("Status: " + rs.getString("status"));
                System.out.println("Filed On: " + rs.getTimestamp("filing_date"));
                if (rs.getTimestamp("resolution_date") != null) {
                    System.out.println("Resolved On: " + rs.getTimestamp("resolution_date"));
                }
                System.out.println("------------------------");
                found = true;
            }
            
            if (!found) {
                System.out.println("❌ No complaints found for this student!");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error searching complaints: " + e.getMessage());
        }
    }

    public static void searchComplaintsByStatus(String status) {
        try {
            String sql = "SELECT c.*, s.name FROM complaints c " +
                        "JOIN students s ON c.student_roll_no = s.roll_no " +
                        "WHERE c.status = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();
            
            boolean found = false;
            while (rs.next()) {
                System.out.println("\n=== Complaint Details ===");
                System.out.println("Complaint ID: " + rs.getInt("complaint_id"));
                System.out.println("Student: " + rs.getString("name") + " (" + rs.getString("student_roll_no") + ")");
                System.out.println("Complaint: " + rs.getString("complaint_text"));
                System.out.println("Filed On: " + rs.getTimestamp("filing_date"));
                if (rs.getTimestamp("resolution_date") != null) {
                    System.out.println("Resolved On: " + rs.getTimestamp("resolution_date"));
                }
                System.out.println("------------------------");
                found = true;
            }
            
            if (!found) {
                System.out.println("❌ No complaints found with status: " + status);
            }
        } catch (SQLException e) {
            System.out.println("❌ Error searching complaints: " + e.getMessage());
        }
    }

    public static void fileNewComplaint(String rollNo) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("\n=== File New Complaint ===");
            System.out.println("Enter your complaint:");
            String complaintText = scanner.nextLine();
            
            String sql = "INSERT INTO complaints (student_roll_no, complaint_text, status, filing_date) VALUES (?, ?, 'Pending', NOW())";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, rollNo);
            pstmt.setString(2, complaintText);
            
            pstmt.executeUpdate();
            System.out.println("✓ Complaint filed successfully!");
        } catch (SQLException e) {
            System.out.println("❌ Error filing complaint: " + e.getMessage());
        }
    }

    public static void viewMyComplaints(String rollNo) {
        try {
            String sql = "SELECT * FROM complaints WHERE student_roll_no = ? ORDER BY filing_date DESC";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, rollNo);
            ResultSet rs = pstmt.executeQuery();
            
            System.out.println("\n=== My Complaints ===");
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("\nComplaint ID: " + rs.getInt("complaint_id"));
                System.out.println("Status: " + rs.getString("status"));
                System.out.println("Filed On: " + rs.getTimestamp("filing_date"));
                System.out.println("Complaint: " + rs.getString("complaint_text"));
                if (rs.getTimestamp("resolution_date") != null) {
                    System.out.println("Resolved On: " + rs.getTimestamp("resolution_date"));
                }
                System.out.println("------------------------");
            }
            
            if (!found) {
                System.out.println("You haven't filed any complaints yet.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error viewing complaints: " + e.getMessage());
        }
    }

    // Room Management Methods
    public static void displayAllRooms() {
        try {
            String sql = "SELECT * FROM rooms ORDER BY room_no";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            System.out.println("\n=== Room Details ===");
            System.out.println("Room No | Type     | Capacity | Occupied | Floor | Block");
            System.out.println("--------------------------------------------------------");
            while (rs.next()) {
                System.out.printf("%-7s | %-8s | %-8d | %-8d | %-5d | %s\n",
                    rs.getString("room_no"),
                    rs.getString("room_type"),
                    rs.getInt("capacity"),
                    rs.getInt("current_occupancy"),
                    rs.getInt("floor_no"),
                    rs.getString("block_name")
                );
            }
        } catch (SQLException e) {
            System.out.println("❌ Error displaying rooms: " + e.getMessage());
        }
    }

    public static void addNewRoom() {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("\n=== Add New Room ===");
            System.out.print("Enter Room Number: ");
            String roomNo = scanner.nextLine();
            
            System.out.println("Room Type:");
            System.out.println("1. Standard");
            System.out.println("2. Luxury");
            System.out.print("Enter choice (1-2): ");
            String roomType = scanner.nextLine().equals("1") ? "Standard" : "Luxury";
            
            System.out.println("Room Capacity:");
            System.out.println("1. 4 Sharing (4 students)");
            System.out.println("2. 2 Sharing (2 students)");
            System.out.println("3. 1 Sharing (1 student)");
            System.out.println("4. No Sharing (1 student)");
            System.out.print("Enter choice (1-4): ");
            int choice = Integer.parseInt(scanner.nextLine());
            int capacity;
            switch(choice) {
                case 1: capacity = 4; break;
                case 2: capacity = 2; break;
                case 3:
                case 4: capacity = 1; break;
                default: throw new IllegalArgumentException("Invalid capacity choice");
            }
            
            System.out.print("Enter Floor Number: ");
            int floorNo = Integer.parseInt(scanner.nextLine());
            
            System.out.print("Enter Block Name: ");
            String blockName = scanner.nextLine();
            
            String sql = "INSERT INTO rooms (room_no, room_type, capacity, current_occupancy, floor_no, block_name) " +
                        "VALUES (?, ?, ?, 0, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, roomNo);
            pstmt.setString(2, roomType);
            pstmt.setInt(3, capacity);
            pstmt.setInt(4, floorNo);
            pstmt.setString(5, blockName);
            
            pstmt.executeUpdate();
            System.out.println("✓ Room added successfully!");
        } catch (SQLException e) {
            System.out.println("❌ Error adding room: " + e.getMessage());
        }
    }

    public static void updateRoom() {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("\n=== Update Room ===");
            System.out.print("Enter Room Number to update: ");
            String roomNo = scanner.nextLine();
            
            System.out.println("What would you like to update?");
            System.out.println("1. Room Type");
            System.out.println("2. Capacity");
            System.out.println("3. Block Name");
            System.out.print("Enter choice: ");
            
            int choice = Integer.parseInt(scanner.nextLine());
            String sql = "";
            
            switch (choice) {
                case 1:
                    System.out.println("New Room Type (1. Standard, 2. Luxury): ");
                    String roomType = scanner.nextLine().equals("1") ? "Standard" : "Luxury";
                    sql = "UPDATE rooms SET room_type = ? WHERE room_no = ?";
                    PreparedStatement pstmt = connection.prepareStatement(sql);
                    pstmt.setString(1, roomType);
                    pstmt.setString(2, roomNo);
                    pstmt.executeUpdate();
                    break;
                    
                case 2:
                    System.out.println("New Capacity (1-4): ");
                    int capacity = Integer.parseInt(scanner.nextLine());
                    sql = "UPDATE rooms SET capacity = ? WHERE room_no = ?";
                    pstmt = connection.prepareStatement(sql);
                    pstmt.setInt(1, capacity);
                    pstmt.setString(2, roomNo);
                    pstmt.executeUpdate();
                    break;
                    
                case 3:
                    System.out.print("New Block Name: ");
                    String blockName = scanner.nextLine();
                    sql = "UPDATE rooms SET block_name = ? WHERE room_no = ?";
                    pstmt = connection.prepareStatement(sql);
                    pstmt.setString(1, blockName);
                    pstmt.setString(2, roomNo);
                    pstmt.executeUpdate();
                    break;
            }
            System.out.println("✓ Room updated successfully!");
        } catch (SQLException e) {
            System.out.println("❌ Error updating room: " + e.getMessage());
        }
    }

    public static void deleteRoom() {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("\n=== Delete Room ===");
            System.out.print("Enter Room Number to delete: ");
            String roomNo = scanner.nextLine();
            
            // Check if room is occupied
            String checkSql = "SELECT current_occupancy FROM rooms WHERE room_no = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkSql);
            checkStmt.setString(1, roomNo);
            ResultSet rs = checkStmt.executeQuery();
            
            if (rs.next() && rs.getInt("current_occupancy") > 0) {
                System.out.println("❌ Cannot delete room: Room is currently occupied!");
                return;
            }
            
            String sql = "DELETE FROM rooms WHERE room_no = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, roomNo);
            
            if (pstmt.executeUpdate() > 0) {
                System.out.println("✓ Room deleted successfully!");
            } else {
                System.out.println("❌ Room not found!");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error deleting room: " + e.getMessage());
        }
    }

    private static String getSharingType(int sharingChoice) {
        switch (sharingChoice) {
            case 1: return "4 Sharing";
            case 2: return "2 Sharing";
            case 3: return "1 Sharing";
            case 4: return "No Sharing";
            default: throw new IllegalArgumentException("Invalid sharing choice");
        }
    }

    // Add this method to update room occupancy
    public static void updateRoomOccupancy() {
        try {
            // First display current student room allocations
            System.out.println("\n=== Current Room Allocations ===");
            String selectSql = "SELECT roll_no, name, room_no, room_type, sharing_type FROM students " +
                              "WHERE room_no IS NOT NULL";
            Statement selectStmt = connection.createStatement();
            ResultSet rs = selectStmt.executeQuery(selectSql);
            
            while (rs.next()) {
                System.out.printf("Room %s: %s (%s) - %s, %s\n",
                    rs.getString("room_no"),
                    rs.getString("name"),
                    rs.getString("roll_no"),
                    rs.getString("room_type"),
                    rs.getString("sharing_type"));
            }

            // Update room occupancies
            String updateSql = "UPDATE rooms r SET current_occupancy = (" +
                              "SELECT COUNT(*) FROM students s WHERE s.room_no = r.room_no)";
            Statement updateStmt = connection.createStatement();
            updateStmt.executeUpdate(updateSql);

            // Display updated room occupancies
            System.out.println("\n=== Updated Room Occupancies ===");
            String roomsSql = "SELECT room_no, room_type, capacity, current_occupancy FROM rooms " +
                             "WHERE current_occupancy > 0 ORDER BY room_no";
            rs = selectStmt.executeQuery(roomsSql);
            
            while (rs.next()) {
                System.out.printf("Room %s (%s): %d/%d occupied\n",
                    rs.getString("room_no"),
                    rs.getString("room_type"),
                    rs.getInt("current_occupancy"),
                    rs.getInt("capacity"));
            }
            
            System.out.println("\n✓ Room occupancies updated successfully!");
        } catch (SQLException e) {
            System.out.println("❌ Error updating room occupancies: " + e.getMessage());
        }
    }

    public static List<String> getAvailableRooms(String roomType, String sharingType) {
        List<String> availableRooms = new ArrayList<>();
        try {
            int maxOccupancy;
            switch (sharingType) {
                case "No Sharing":
                case "1 Sharing":
                    maxOccupancy = 0;  // Room must be empty
                    break;
                case "2 Sharing":
                    maxOccupancy = 1;  // Can have at most 1 student
                    break;
                case "4 Sharing":
                    maxOccupancy = 3;  // Can have at most 3 students
                    break;
                default:
                    throw new IllegalArgumentException("Invalid sharing type");
            }

            String sql = "SELECT room_no FROM rooms " +
                        "WHERE room_type = ? AND current_occupancy <= ? " +
                        "ORDER BY room_no";
            
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, roomType);
            pstmt.setInt(2, maxOccupancy);
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                availableRooms.add(rs.getString("room_no"));
            }
        } catch (SQLException e) {
            System.out.println("❌ Error getting available rooms: " + e.getMessage());
        }
        return availableRooms;
    }

    public static void registerStudent(String rollNo, String name, String college, 
        String department, String semester, int age, String mobile, String password) {
        try {
            // First get room preferences
            Scanner scanner = new Scanner(System.in);
            System.out.println("\n=== Room Selection ===");
            
            // Get room type preference
            System.out.println("Select Room Type:");
            System.out.println("1. Standard");
            System.out.println("2. Luxury");
            System.out.print("Enter choice (1-2): ");
            String roomType = scanner.nextLine().equals("1") ? "Standard" : "Luxury";
            
            // Get sharing type preference
            System.out.println("\nSelect Sharing Type:");
            System.out.println("1. 4 Sharing");
            System.out.println("2. 2 Sharing");
            System.out.println("3. 1 Sharing");
            System.out.println("4. No Sharing");
            System.out.print("Enter choice (1-4): ");
            int sharingChoice = Integer.parseInt(scanner.nextLine());
            String sharingType = getSharingType(sharingChoice);
            
            // Get available rooms
            List<String> availableRooms = getAvailableRooms(roomType, sharingType);
            
            if (availableRooms.isEmpty()) {
                System.out.println("❌ No rooms available for selected preferences!");
                return;
            }
            
            // Display available rooms
            System.out.println("\nAvailable Rooms:");
            for (String roomNo : availableRooms) {
                System.out.println("Room " + roomNo);
            }
            
            // Get room selection
            System.out.print("\nEnter Room Number from available rooms: ");
            String selectedRoom = scanner.nextLine();
            
            if (!availableRooms.contains(selectedRoom)) {
                System.out.println("❌ Invalid room selection!");
                return;
            }
            
            // Get block and floor information from the selected room
            String roomInfoSql = "SELECT block_name, floor_no FROM rooms WHERE room_no = ?";
            PreparedStatement roomInfoStmt = connection.prepareStatement(roomInfoSql);
            roomInfoStmt.setString(1, selectedRoom);
            ResultSet roomInfo = roomInfoStmt.executeQuery();
            roomInfo.next();
            String blockName = roomInfo.getString("block_name");
            int floorNo = roomInfo.getInt("floor_no");
            
            // Calculate fees
            double totalFee = calculateFee(roomType, sharingType);
            System.out.printf("\nTotal Fee: Rs. %.2f\n", totalFee);
            System.out.print("Enter Initial Payment Amount: ");
            double amountPaid = Double.parseDouble(scanner.nextLine());
            double amountDue = totalFee - amountPaid;
            
            // Insert student record
            String sql = "INSERT INTO students (roll_no, name, college, department, " +
                        "semester, age, mobile, room_no, room_type, sharing_type, " +
                        "block_name, floor_no, amount_paid, amount_due, password) VALUES " +
                        "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, rollNo);
            pstmt.setString(2, name);
            pstmt.setString(3, college);
            pstmt.setString(4, department);
            pstmt.setString(5, semester);
            pstmt.setInt(6, age);
            pstmt.setString(7, mobile);
            pstmt.setString(8, selectedRoom);
            pstmt.setString(9, roomType);
            pstmt.setString(10, sharingType);
            pstmt.setString(11, blockName);
            pstmt.setInt(12, floorNo);
            pstmt.setDouble(13, amountPaid);
            pstmt.setDouble(14, amountDue);
            pstmt.setString(15, password);
            
            pstmt.executeUpdate();
            
            // Update room occupancy
            updateRoomOccupancy();
            
            System.out.println("✓ Student registered successfully!");
            System.out.printf("Amount Due: Rs. %.2f\n", amountDue);
            
        } catch (SQLException e) {
            System.out.println("❌ Error registering student: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid number format!");
        } catch (IllegalArgumentException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

    public static void fixRoomInconsistencies() {
        try {
            // Fix room type for Room 102 (currently shows Luxury for Priya but was Standard in rooms table)
            String updateStudentSql = "UPDATE students SET room_type = " +
                                    "(SELECT room_type FROM rooms WHERE room_no = students.room_no) " +
                                    "WHERE room_no IS NOT NULL";
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(updateStudentSql);
            
            System.out.println("✓ Room type inconsistencies fixed!");
        } catch (SQLException e) {
            System.out.println("❌ Error fixing room inconsistencies: " + e.getMessage());
        }
    }

    private static double calculateFee(String roomType, String sharingType) {
        if ("Standard".equals(roomType)) {
            switch (sharingType) {
                case "4 Sharing": return 60000.0;
                case "2 Sharing": return 70000.0;
                case "1 Sharing": return 110000.0;
                case "No Sharing": return 130000.0;
                default: throw new IllegalArgumentException("Invalid sharing type");
            }
        } else if ("Luxury".equals(roomType)) {
            switch (sharingType) {
                case "4 Sharing": return 80000.0;
                case "2 Sharing": return 120000.0;
                case "1 Sharing": return 130000.0;
                case "No Sharing": return 150000.0;
                default: throw new IllegalArgumentException("Invalid sharing type");
            }
        }
        throw new IllegalArgumentException("Invalid room type");
    }

    public static void addNewStudent() {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("\n=== Add New Student ===");
            System.out.print("Enter Roll Number: ");
            String rollNo = scanner.nextLine();
            
            System.out.print("Enter Name: ");
            String name = scanner.nextLine();
            
            System.out.print("Enter College: ");
            String college = scanner.nextLine();
            
            System.out.print("Enter Department: ");
            String department = scanner.nextLine();
            
            System.out.print("Enter Semester: ");
            String semester = scanner.nextLine();
            
            System.out.print("Enter Age: ");
            int age = Integer.parseInt(scanner.nextLine());
            
            System.out.print("Enter Mobile: ");
            String mobile = scanner.nextLine();

            System.out.print("Enter Password: ");
            String password = scanner.nextLine();

            registerStudent(rollNo, name, college, department, semester, age, mobile, password);
            
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid number format!");
        }
    }

    public static void deleteStudent(String rollNo) {
        try {
            String sql = "DELETE FROM students WHERE roll_no = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, rollNo);
            
            if (pstmt.executeUpdate() > 0) {
                System.out.println("✓ Student deleted successfully!");
                // Update room occupancy after deleting student
                updateRoomOccupancy();
            } else {
                System.out.println("❌ Student not found!");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error deleting student: " + e.getMessage());
        }
    }

    public static void displayAllComplaints() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(
                "SELECT c.*, s.name FROM complaints c " +
                "JOIN students s ON c.student_roll_no = s.roll_no"
            );
            
            System.out.println("\n=== Complaint Records ===");
            while (rs.next()) {
                System.out.println("\nComplaint ID: " + rs.getInt("complaint_id"));
                System.out.println("Student: " + rs.getString("name") + " (" + rs.getString("student_roll_no") + ")");
                System.out.println("Complaint: " + rs.getString("complaint_text"));
                System.out.println("Status: " + rs.getString("status"));
                System.out.println("Filed On: " + rs.getTimestamp("filing_date"));
                if (rs.getTimestamp("resolution_date") != null) {
                    System.out.println("Resolved On: " + rs.getTimestamp("resolution_date"));
                }
                System.out.println("------------------------");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error displaying complaints: " + e.getMessage());
        }
    }

    public static void updateComplaintStatus(int complaintId) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("\n=== Update Complaint Status ===");
            System.out.println("1. Pending");
            System.out.println("2. In Progress");
            System.out.println("3. Resolved");
            System.out.print("Enter new status (1-3): ");
            
            int choice = Integer.parseInt(scanner.nextLine());
            String status;
            
            switch (choice) {
                case 1:
                    status = "Pending";
                    break;
                case 2:
                    status = "In Progress";
                    break;
                case 3:
                    status = "Resolved";
                    break;
                default:
                    System.out.println("❌ Invalid choice!");
                    return;
            }

            String sql = "UPDATE complaints SET status = ?, resolution_date = ? WHERE complaint_id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, status);
            pstmt.setTimestamp(2, status.equals("Resolved") ? new Timestamp(System.currentTimeMillis()) : null);
            pstmt.setInt(3, complaintId);
            
            if (pstmt.executeUpdate() > 0) {
                System.out.println("✓ Complaint status updated successfully!");
            } else {
                System.out.println("❌ Complaint not found!");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error updating complaint: " + e.getMessage());
        }
    }

    public static void displayAvailableRooms() {
        try {
            String sql = "SELECT r.*, COUNT(s.roll_no) as current_occupancy " +
                        "FROM rooms r " +
                        "LEFT JOIN students s ON r.room_no = s.room_no " +
                        "GROUP BY r.room_no " +
                        "ORDER BY r.room_no";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            System.out.println("\nRoom No | Type     | Capacity | Occupied | Available | Block    | Floor");
            System.out.println("----------------------------------------------------------------");
            while (rs.next()) {
                int available = rs.getInt("capacity") - rs.getInt("current_occupancy");
                System.out.printf("%-8s | %-9s | %-8d | %-9d | %-9d | %-9s | %-5d\n",
                    rs.getString("room_no"),
                    rs.getString("room_type"),
                    rs.getInt("capacity"),
                    rs.getInt("current_occupancy"),
                    available,
                    rs.getString("block_name"),
                    rs.getInt("floor_no"));
            }
            System.out.println("----------------------------------------------------------------");
        } catch (SQLException e) {
            System.out.println("❌ Error displaying available rooms: " + e.getMessage());
        }
    }
} 