package management;

import java.util.*;

public class Rooms {
    private static final HashMap<String, Integer> feeStructure = new HashMap<>();
    private static final List<Integer> availableStandardRooms = new ArrayList<>(Arrays.asList(101, 102, 103));
    private static final List<Integer> availableLuxuryRooms = new ArrayList<>(Arrays.asList(201, 202, 203));
    private static final HashMap<Integer, String> assignedRooms = new HashMap<>(); // Stores assigned rooms (roomNumber → studentName)

    static {
        // Initialize Fee Structure
        feeStructure.put("Standard_4_Sharing", 60000);
        feeStructure.put("Standard_2_Sharing", 70000);
        feeStructure.put("Standard_1_Sharing", 110000);
        feeStructure.put("Standard_No_Sharing", 130000);

        feeStructure.put("Luxury_4_Sharing", 80000);
        feeStructure.put("Luxury_2_Sharing", 120000);
        feeStructure.put("Luxury_1_Sharing", 130000);
        feeStructure.put("Luxury_No_Sharing", 150000);
    }

    // 📌 Display Available Rooms
    public static void displayRooms() {
        System.out.println("\n🏠 Available Standard Rooms: " + availableStandardRooms);
        System.out.println("🏠 Available Luxury Rooms: " + availableLuxuryRooms);
    }

    // 📌 Assign Room to Student
    public static int assignRoom(String roomType, String studentName) {
        List<Integer> roomList = roomType.contains("Luxury") ? availableLuxuryRooms : availableStandardRooms;

        if (!roomList.isEmpty()) {
            int assignedRoom = roomList.remove(0); // Assign first available room
            assignedRooms.put(assignedRoom, studentName); // Store assignment
            System.out.println("✅ Room " + assignedRoom + " assigned to " + studentName);
            return assignedRoom;
        } else {
            System.out.println("❌ No rooms available in this category.");
            return -1; // Indicates failure
        }
    }

    // 📌 Free Room When Student Leaves
    public static void freeRoom(int roomNumber) {
        if (assignedRooms.containsKey(roomNumber)) {
            assignedRooms.remove(roomNumber); // Remove student-room mapping
            if (roomNumber >= 200) {
                availableLuxuryRooms.add(roomNumber);
                Collections.sort(availableLuxuryRooms); // Keep sorted
            } else {
                availableStandardRooms.add(roomNumber);
                Collections.sort(availableStandardRooms); // Keep sorted
            }
            System.out.println("✅ Room " + roomNumber + " is now available.");
        } else {
            System.out.println("⚠ Room " + roomNumber + " was not assigned.");
        }
    }

    // 📌 Get Fee for Room Type
    public static int getFee(String roomType) {
        return feeStructure.getOrDefault(roomType, 0);
    }

    // 📌 View Assigned Rooms
    public static void viewAssignedRooms() {
        if (assignedRooms.isEmpty()) {
            System.out.println("📌 No rooms are currently assigned.");
        } else {
            System.out.println("\n🏠 Assigned Rooms:");
            for (Map.Entry<Integer, String> entry : assignedRooms.entrySet()) {
                System.out.println("🔹 Room " + entry.getKey() + " → " + entry.getValue());
            }
        }
    }

    // 📌 Check if a Room is Occupied
    public static boolean isRoomOccupied(int roomNumber) {
        return assignedRooms.containsKey(roomNumber);
    }
}
