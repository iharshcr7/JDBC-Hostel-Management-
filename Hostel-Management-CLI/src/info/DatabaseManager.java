package info;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class DatabaseManager {
    private static Connection conn;

    public static void displayAvailableRooms() {
        try {
            String sql = "SELECT room_no, room_type, capacity, current_occupancy, block_name, floor_no FROM rooms WHERE current_occupancy < capacity";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
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

    public static void updateRoomOccupancy() {
        try {
            String sql = "UPDATE rooms r SET current_occupancy = (SELECT COUNT(*) FROM students s WHERE s.room_no = r.room_no)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("❌ Error updating room occupancy: " + e.getMessage());
        }
    }

    private static double calculateFee(String roomType, String sharingType) {
        double baseFee = 0;
        
        // Base fee based on room type
        switch (roomType.toLowerCase()) {
            case "standard":
                baseFee = 100000;
                break;
            case "luxury":
                baseFee = 150000;
                break;
            default:
                baseFee = 100000; // Default to standard
        }
        
        // Adjust fee based on sharing type
        switch (sharingType.toLowerCase()) {
            case "no sharing":
                return baseFee;
            case "2 sharing":
                return baseFee * 0.7;
            case "4 sharing":
                return baseFee * 0.5;
            default:
                return baseFee; // Default to no sharing
        }
    }

    public static void updateStudent(String rollNo) {
        Scanner scanner = new Scanner(System.in);
        try {
            // First check if student exists
            String checkSql = "SELECT * FROM students WHERE roll_no = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
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
                    stmt = conn.prepareStatement(sql);
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
                    stmt = conn.prepareStatement(sql);
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
                    String checkRoomSql = "SELECT capacity, current_occupancy, room_type, sharing_type, block_name, floor_no FROM rooms WHERE room_no = ?";
                    PreparedStatement checkRoomStmt = conn.prepareStatement(checkRoomSql);
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
                    
                    // Update student's room with all related information
                    sql = "UPDATE students SET room_no = ?, room_type = ?, sharing_type = ?, block_name = ?, floor_no = ? WHERE roll_no = ?";
                    stmt = conn.prepareStatement(sql);
                    stmt.setString(1, newRoom);
                    stmt.setString(2, roomRs.getString("room_type"));
                    stmt.setString(3, roomRs.getString("sharing_type"));
                    stmt.setString(4, roomRs.getString("block_name"));
                    stmt.setInt(5, roomRs.getInt("floor_no"));
                    stmt.setString(6, rollNo);
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
                    stmt = conn.prepareStatement(sql);
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
} 