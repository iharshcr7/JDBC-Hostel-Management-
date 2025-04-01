package main;

import java.util.*;
import java.io.*;
import info.*;
import management.*;
import database.DatabaseManager;

public class PHHD {  
    public static HashMap<Integer, String> roomAllocation = new HashMap<>();
    public static Scanner scanner = new Scanner(System.in); 
    public static HostelManager hostelManager = new HostelManager();
    private static final String ROOM_FILE = new File(System.getProperty("user.dir"), "src/student_information/rooms.txt").getAbsolutePath();

    static {
        createDirectory();
        loadRoomData();
    }

    public static void main(String[] args) {
        // Initialize database first
        DatabaseManager.initializeDatabase();
        
        // Start the application
        Menu.displayMainMenu();
    }

    private static void createDirectory() {
        File directory = new File("src/student_information");
        if (!directory.exists() && directory.mkdirs()) {
            System.out.println("✅ Directory created: " + directory.getAbsolutePath());
        } else {
            System.out.println("Directory already exists: " + directory.getAbsolutePath());
        }
    }

    // ✅ Load room data from file
    private static void loadRoomData() {
        File file = new File(ROOM_FILE);
        if (!file.exists()) {
            System.out.println("⚠ No room data found. Admin must allocate rooms.");
            return;
        }

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (!line.contains("=")) continue;  

                String[] data = line.split("=");
                if (data.length == 2) {
                    try {
                        int roomNum = Integer.parseInt(data[0].trim());
                        String occupant = data[1].trim();
                        
                        // ✅ Prevent duplicate entries
                        if (roomAllocation.containsKey(roomNum)) {
                            System.out.println("⚠ Warning: Duplicate entry for Room " + roomNum + ". Skipping...");
                            continue;
                        }
                        
                        roomAllocation.put(roomNum, occupant);
                    } catch (NumberFormatException e) {
                        System.out.println("⚠ Skipping invalid room data: " + line);
                    }
                }
            }
            System.out.println("✅ Room data loaded successfully!");
        } catch (IOException e) {
            System.out.println("❌ Error loading room data: " + e.getMessage());
        }
    }

    // ✅ Save updated room data to file
    public static void saveRoomData() {
        createDirectory();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ROOM_FILE))) {
            for (Map.Entry<Integer, String> entry : roomAllocation.entrySet()) {
                writer.write(entry.getKey() + "=" + entry.getValue());
                writer.newLine();
            }
            System.out.println("✅ Room data saved successfully!");
        } catch (IOException e) {
            System.out.println("❌ Error saving room data: " + e.getMessage());
        }
    }
}
