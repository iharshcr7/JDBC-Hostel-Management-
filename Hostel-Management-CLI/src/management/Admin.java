package management;

import java.io.*;
import java.util.*;
import exception.InvalidAgeException;

public class Admin {
    private static final String STUDENT_FILE = "student_credentials.txt";
    private static final String ADMIN_FILE = "admin_credentials.txt";
    private static final String COMPLAINTS_FILE = "complaints.txt";
    private static final String RULES_FILE = "hostel_rules.txt";
    private static final String PAYMENTS_FILE = "payments.txt";
    private static String loggedInAdmin = ""; // Tracks the logged-in admin

    public static void adminLogin() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Admin Username: ");
        String username = sc.nextLine();
        System.out.print("Enter Admin Password: ");
        String password = sc.nextLine();

        // Check credentials from file
        try (BufferedReader reader = new BufferedReader(new FileReader(ADMIN_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] credentials = line.split(",");
                if (credentials.length == 2 && credentials[0].equals(username) && credentials[1].equals(password)) {
                    loggedInAdmin = username; // Set the logged-in admin
                    System.out.println("✅ Welcome " + loggedInAdmin + "!");
                    return;
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Error checking admin credentials.");
        }

        System.out.println("❌ Invalid credentials. Try again.");
    }

    public static String getLoggedInAdmin() {
        return loggedInAdmin;
    }

    public static void manageStudents() {
        System.out.println("\n📋 Managing Students...");
        // Implement student management logic here
    }

    public static void manageRooms() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n\t\t======================================");
            System.out.println("\n\t\t\t..::ROOM MANAGEMENT::..");
            System.out.println("\n\t\t======================================");
            System.out.println("\n\t\t 1. View All Rooms Status");
            System.out.println("\n\t\t 2. View Occupied Rooms");
            System.out.println("\n\t\t 3. View Vacant Rooms");
            System.out.println("\n\t\t 4. Change Room Assignment");
            System.out.println("\n\t\t 5. Back to Admin Menu");
            System.out.println("\n\t\t======================================");
            System.out.print("\n\t\t\t   CHOICE :- ");

            if (!scanner.hasNextInt()) {
                System.out.println("❌ Invalid input! Enter a number between 1 and 5.");
                scanner.next();
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    viewAllRoomsStatus();
                    break;
                case 2:
                    viewOccupiedRooms();
                    break;
                case 3:
                    viewVacantRooms();
                    break;
                case 4:
                    changeRoomAssignment(scanner);
                    break;
                case 5:
                    running = false;
                    break;
                default:
                    System.out.println("❌ Invalid choice! Try again.");
                    break;
            }
        }
    }

    private static void viewAllRoomsStatus() {
        try {
            File roomFile = new File("room_details.txt");
            if (!roomFile.exists() || roomFile.length() == 0) {
                System.out.println("\n❌ No room records found!");
                return;
            }

            System.out.println("\n\t\t======================================");
            System.out.println("\n\t\t\t..::ALL ROOMS STATUS::..");
            System.out.println("\n\t\t======================================");

            // Get all room numbers (101-104 for Standard, 201-204 for Luxury)
            Set<String> allRooms = new HashSet<>();
            for (int i = 101; i <= 104; i++) allRooms.add(String.valueOf(i));
            for (int i = 201; i <= 204; i++) allRooms.add(String.valueOf(i));

            // Read occupied rooms
            Map<String, String[]> occupiedRooms = new HashMap<>();
            BufferedReader reader = new BufferedReader(new FileReader(roomFile));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    occupiedRooms.put(parts[0], parts);
                    allRooms.remove(parts[0]);
                }
            }
            reader.close();

            // Display Standard Rooms
            System.out.println("\n\t\tSTANDARD ROOMS (101-104):");
            System.out.println("\n\t\t----------------------------------------");
            for (int i = 101; i <= 104; i++) {
                String roomNum = String.valueOf(i);
                if (occupiedRooms.containsKey(roomNum)) {
                    String[] details = occupiedRooms.get(roomNum);
                    System.out.println("\n\t\tRoom " + roomNum + ":");
                    System.out.println("\t\tStatus: Occupied");
                    System.out.println("\t\tType: " + details[1]);
                    System.out.println("\t\tSharing: " + details[2]);
                    System.out.println("\t\tStudent: " + details[3]);
                } else {
                    System.out.println("\n\t\tRoom " + roomNum + ":");
                    System.out.println("\t\tStatus: Vacant");
                }
            }

            // Display Luxury Rooms
            System.out.println("\n\t\tLUXURY ROOMS (201-204):");
            System.out.println("\n\t\t----------------------------------------");
            for (int i = 201; i <= 204; i++) {
                String roomNum = String.valueOf(i);
                if (occupiedRooms.containsKey(roomNum)) {
                    String[] details = occupiedRooms.get(roomNum);
                    System.out.println("\n\t\tRoom " + roomNum + ":");
                    System.out.println("\t\tStatus: Occupied");
                    System.out.println("\t\tType: " + details[1]);
                    System.out.println("\t\tSharing: " + details[2]);
                    System.out.println("\t\tStudent: " + details[3]);
                } else {
                    System.out.println("\n\t\tRoom " + roomNum + ":");
                    System.out.println("\t\tStatus: Vacant");
                }
            }

            System.out.println("\n\t\t======================================");
        } catch (IOException e) {
            System.out.println("❌ Error reading room details: " + e.getMessage());
        }
    }

    private static void viewOccupiedRooms() {
        try {
            File roomFile = new File("room_details.txt");
            if (!roomFile.exists() || roomFile.length() == 0) {
                System.out.println("\n❌ No occupied rooms found!");
                return;
            }

            System.out.println("\n\t\t======================================");
            System.out.println("\n\t\t\t..::OCCUPIED ROOMS::..");
            System.out.println("\n\t\t======================================");

            BufferedReader reader = new BufferedReader(new FileReader(roomFile));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    System.out.println("\n\t\tRoom " + parts[0] + ":");
                    System.out.println("\t\tType: " + parts[1]);
                    System.out.println("\t\tSharing: " + parts[2]);
                    System.out.println("\t\tStudent: " + parts[3]);
                    System.out.println("\t\t----------------------------------------");
                }
            }
            reader.close();

            System.out.println("\n\t\t======================================");
        } catch (IOException e) {
            System.out.println("❌ Error reading room details: " + e.getMessage());
        }
    }

    private static void viewVacantRooms() {
        try {
            System.out.println("\n\t\t======================================");
            System.out.println("\n\t\t\t..::VACANT ROOMS::..");
            System.out.println("\n\t\t======================================");

            // Get all occupied rooms
            Set<String> occupiedRooms = new HashSet<>();
            File roomFile = new File("room_details.txt");
            if (roomFile.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(roomFile));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 1) {
                        occupiedRooms.add(parts[0]);
                    }
                }
                reader.close();
            }

            // Display Standard Rooms
            System.out.println("\n\t\tSTANDARD ROOMS (101-104):");
            System.out.println("\n\t\t----------------------------------------");
            for (int i = 101; i <= 104; i++) {
                String roomNum = String.valueOf(i);
                if (!occupiedRooms.contains(roomNum)) {
                    System.out.println("\n\t\tRoom " + roomNum + ":");
                    System.out.println("\t\tStatus: Vacant");
                    System.out.println("\t\tType: Standard");
                }
            }

            // Display Luxury Rooms
            System.out.println("\n\t\tLUXURY ROOMS (201-204):");
            System.out.println("\n\t\t----------------------------------------");
            for (int i = 201; i <= 204; i++) {
                String roomNum = String.valueOf(i);
                if (!occupiedRooms.contains(roomNum)) {
                    System.out.println("\n\t\tRoom " + roomNum + ":");
                    System.out.println("\t\tStatus: Vacant");
                    System.out.println("\t\tType: Luxury");
                }
            }

            System.out.println("\n\t\t======================================");
        } catch (IOException e) {
            System.out.println("❌ Error reading room details: " + e.getMessage());
        }
    }

    private static void changeRoomAssignment(Scanner scanner) {
        System.out.print("\n\t\tEnter Student Roll Number: ");
        String rollNo = scanner.nextLine();

        // Check if student exists and get current room
        String currentRoom = getStudentRoom(rollNo);
        if (currentRoom == null) {
            System.out.println("\n❌ Student not found!");
            return;
        }

        System.out.println("\n\t\tCurrent Room: " + currentRoom);
        System.out.print("\n\t\tEnter New Room Number: ");
        String newRoom = scanner.nextLine();

        // Validate new room number
        if (!isValidRoomNumber(newRoom)) {
            System.out.println("\n❌ Invalid room number! Must be between 101-104 (Standard) or 201-204 (Luxury)");
            return;
        }

        // Check if new room is already occupied
        if (isRoomOccupied(newRoom)) {
            System.out.println("\n❌ Room " + newRoom + " is already occupied!");
            return;
        }

        // Update room assignment
        if (updateRoomAssignment(rollNo, currentRoom, newRoom)) {
            System.out.println("\n✅ Room assignment updated successfully!");
        } else {
            System.out.println("\n❌ Error updating room assignment!");
        }
    }

    private static String getStudentRoom(String rollNo) {
        try {
            File file = new File("room_details.txt");
            if (!file.exists()) return null;

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4 && parts[3].equals(rollNo)) {
                    reader.close();
                    return parts[0];
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("❌ Error reading room details: " + e.getMessage());
        }
        return null;
    }

    private static boolean isValidRoomNumber(String roomNum) {
        return roomNum.matches("10[1-4]|20[1-4]");
    }

    private static boolean updateRoomAssignment(String rollNo, String oldRoom, String newRoom) {
        try {
            File inputFile = new File("room_details.txt");
            File tempFile = new File("temp_room_details.txt");
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            PrintWriter writer = new PrintWriter(new FileWriter(tempFile));

            String line;
            boolean found = false;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4 && parts[3].equals(rollNo)) {
                    // Update room number while keeping other details
                    writer.println(newRoom + "," + parts[1] + "," + parts[2] + "," + parts[3]);
                    found = true;
                } else {
                    writer.println(line);
                }
            }

            writer.close();
            reader.close();

            if (!found) {
                tempFile.delete();
                return false;
            }

            if (!inputFile.delete()) {
                tempFile.delete();
                return false;
            }

            if (!tempFile.renameTo(inputFile)) {
                tempFile.delete();
                return false;
            }

            return true;
        } catch (IOException e) {
            System.out.println("❌ Error updating room assignment: " + e.getMessage());
            return false;
        }
    }

    private static boolean isRoomOccupied(String roomNum) {
        try {
            File file = new File("room_details.txt");
            if (!file.exists()) return false;

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 1 && parts[0].equals(roomNum)) {
                    reader.close();
                    return true;
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("❌ Error checking room occupancy: " + e.getMessage());
        }
        return false;
    }

    // Admin Menu with added methods
    public static void adminMenu() {
        Scanner sc = new Scanner(System.in);

        if (!checkAdminExists()) {
            System.out.println("\n⚠ No Admin Found! Please Register an Admin First.");
            registerAdmin();
        }

        // Admin login before accessing the menu
        adminLogin();

        while (true) {
            System.out.println("\n🔷 ADMIN MENU 🔷");
            System.out.println("1. Register New Student");
            System.out.println("2. View All Students");
            System.out.println("3. Manage Complaints");
            System.out.println("4. View Hostel Rules");
            System.out.println("5. Manage Rooms");
            System.out.println("6. View Payments");
            System.out.println("7. Manage Warden");
            System.out.println("8. Logout");
            System.out.print("Enter Choice: ");

            if (!sc.hasNextInt()) {
                System.out.println("❌ Invalid Input! Enter 1-8.");
                sc.next();
                continue;
            }

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1: registerStudent(); break;
                case 2: viewAllStudents(); break;
                case 3: manageComplaints(); break;
                case 4: viewHostelRules(); break;
                case 5: manageRooms(); break;
                case 6: viewAllPayments(); break;
                case 7: manageWarden(); break;
                case 8:
                    System.out.println("\n🔴 Logging Out...");
                    return;
                default:
                    System.out.println("❌ Invalid Choice! Try Again.");
            }
        }
    }

    private static boolean checkAdminExists() {
        File file = new File(ADMIN_FILE);
        return file.exists() && file.length() > 0;
    }

    public static void registerAdmin() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ADMIN_FILE, true))) {
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter Admin Username: ");
            String username = sc.nextLine();
            System.out.print("Enter Admin Password: ");
            String password = sc.nextLine();
            writer.println(username + "," + password);
            System.out.println("✅ Admin Registered Successfully!");
        } catch (IOException e) {
            System.out.println("❌ Error registering admin: " + e.getMessage());
        }
    }

    public static void registerStudent() {
        System.out.println("✅ Admin " + Admin.getLoggedInAdmin() + " is registering the student.");

        try (PrintWriter writer = new PrintWriter(new FileWriter(STUDENT_FILE, true))) {
            Scanner sc = new Scanner(System.in);

            // Student details input
            System.out.print("Enter First Name: ");
            String firstName = sc.nextLine();
            System.out.print("Enter Last Name: ");
            String lastName = sc.nextLine();
            System.out.print("Enter College Name: ");
            String college = sc.nextLine();
            System.out.print("Enter Department: ");
            String department = sc.nextLine();
            System.out.print("Enter Semester: ");
            String semester = sc.nextLine();
            System.out.print("Enter Roll Number (Username): ");
            String rollNo = sc.next();
            sc.nextLine(); // Consume newline
            System.out.print("Enter Age: ");
            int age = sc.nextInt();
            sc.nextLine(); // Consume newline
            System.out.print("Enter Mobile Number (10 digits): ");
            String mobileNumber = sc.nextLine();

            // Room Type selection
            System.out.println("\nSelect Room Type:");
            System.out.println("1. Standard Room");
            System.out.println("2. Luxury Room");
            System.out.print("Enter Choice: ");
            int roomChoice = sc.nextInt();
            sc.nextLine(); // Consume newline

            String roomType = (roomChoice == 1) ? "Standard" : "Luxury";
            int roomNumber = assignRoom(roomType);

            if (roomNumber == -1) {
                System.out.println("❌ No available rooms for " + roomType);
                return;
            }

            // Fee selection
            System.out.println("\nSelect Fee Structure:");
            System.out.println("1. 4 Sharing Rs.80,000");
            System.out.println("2. 2 Sharing Rs.1,20,000");
            System.out.println("3. 1 Sharing Rs.1,30,000");
            System.out.println("4. No Sharing Rs.1,50,000");
            System.out.print("Enter choice (1-4): ");
            int feeChoice = sc.nextInt();
            sc.nextLine(); // Consume newline

            int totalFee = 0;
            switch (feeChoice) {
                case 1: totalFee = 80000; break;
                case 2: totalFee = 120000; break;
                case 3: totalFee = 130000; break;
                case 4: totalFee = 150000; break;
                default: System.out.println("❌ Invalid fee choice."); return;
            }

            // Payment details
            System.out.println("\nTotal Fee: Rs." + totalFee);
            System.out.print("Enter Amount to Pay: ");
            int amountPaid = sc.nextInt();
            sc.nextLine(); // Consume newline

            if (amountPaid < totalFee) {
                System.out.println("❌ Insufficient Payment.");
                return;
            }

            // Generate student credentials
            String defaultPassword = generateDefaultPassword(rollNo);
            
            // Store student details with credentials
            writer.println(rollNo + "," + defaultPassword + "," + firstName + " " + lastName + "," + 
                          college + "," + department + "," + semester + "," + age + "," + 
                          mobileNumber + "," + roomType + "," + roomNumber + "," + totalFee + "," + 
                          amountPaid + "," + (totalFee - amountPaid) + ",UPI"); // Added pending amount

            System.out.println("\n✅ Student Registered Successfully!");
            System.out.println("🔹 Username: " + rollNo);
            System.out.println("🔹 Password: " + defaultPassword);
            System.out.println("🔹 Room Assigned: " + roomNumber);
            System.out.println("🔹 Total Fee: Rs." + totalFee);
            System.out.println("🔹 Amount Paid: Rs." + amountPaid);
            System.out.println("🔹 Amount Pending: Rs." + (totalFee - amountPaid));
        } catch (IOException e) {
            System.out.println("❌ Error registering student: " + e.getMessage());
        }
    }

    private static String generateDefaultPassword(String rollNo) {
        // Generate a default password using roll number and a random number
        Random rand = new Random();
        int randomNum = 1000 + rand.nextInt(9000); // 4-digit random number
        return rollNo + "@" + randomNum;
    }

    private static int assignRoom(String roomType) {
        int[] standardRooms = {101, 102, 103, 104};
        int[] luxuryRooms = {201, 202, 203, 204};
        Set<Integer> assignedRooms = new HashSet<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("room_details.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 1) {
                    assignedRooms.add(Integer.parseInt(data[0]));
                }
            }
        } catch (IOException ignored) {
            // If file doesn't exist, no rooms are assigned yet
        }

        int[] availableRooms = (roomType.equals("Standard")) ? standardRooms : luxuryRooms;
        for (int room : availableRooms) {
            if (!assignedRooms.contains(room)) {
                return room;
            }
        }
        return -1; // No rooms available
    }

    // New Methods added
    public static void viewAllStudents() {
        try {
            File studentFile = new File("student_details.txt");
            File roomFile = new File("room_details.txt");
            File paymentFile = new File("payment_details.txt");

            if (!studentFile.exists() || studentFile.length() == 0) {
                System.out.println("\n❌ No students registered yet!");
                return;
            }

            System.out.println("\n\t\t======================================");
            System.out.println("\n\t\t\t..::ALL STUDENT DETAILS::..");
            System.out.println("\n\t\t======================================");

            BufferedReader studentReader = new BufferedReader(new FileReader(studentFile));
            String studentLine;
            while ((studentLine = studentReader.readLine()) != null) {
                String[] studentParts = studentLine.split(",");
                if (studentParts.length >= 8) {
                    String studentId = studentParts[0];
                    String name = studentParts[1];
                    String college = studentParts[2];
                    String department = studentParts[3];
                    String semester = studentParts[4];
                    String age = studentParts[5];
                    String mobile = studentParts[6];
                    String rollNo = studentParts[7];

                    // Get room details
                    String roomDetails = getRoomDetails(rollNo);
                    // Get payment details
                    String paymentDetails = getPaymentDetails(rollNo);

                    System.out.println("\n\t\tStudent ID: " + studentId);
                    System.out.println("\n\t\tName: " + name);
                    System.out.println("\n\t\tCollege: " + college);
                    System.out.println("\n\t\tDepartment: " + department);
                    System.out.println("\n\t\tSemester: " + semester);
                    System.out.println("\n\t\tRoll Number: " + rollNo);
                    System.out.println("\n\t\tAge: " + age);
                    System.out.println("\n\t\tMobile Number: " + mobile);
                    if (roomDetails != null) {
                        System.out.println("\n\t\t" + roomDetails);
                    }
                    if (paymentDetails != null) {
                        System.out.println("\n\t\t" + paymentDetails);
                    }
                    System.out.println("\n\t\t----------------------------------------");
                }
            }
            studentReader.close();

            System.out.println("\n\t\t======================================");
        } catch (IOException e) {
            System.out.println("❌ Error reading student details: " + e.getMessage());
        }
    }

    private static String getRoomDetails(String rollNo) {
        try {
            File file = new File("room_details.txt");
            if (!file.exists()) return null;

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4 && parts[3].equals(rollNo)) {
                    reader.close();
                    return "Room Number: " + parts[0] + 
                           "\n\t\tRoom Type: " + parts[1] + 
                           "\n\t\tSharing Type: " + parts[2];
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("❌ Error reading room details: " + e.getMessage());
        }
        return null;
    }

    private static String getPaymentDetails(String rollNo) {
        try {
            File file = new File("payment_details.txt");
            if (!file.exists()) return null;

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3 && parts[0].equals(rollNo)) {
                    reader.close();
                    return "Amount Paid: Rs." + parts[1] + 
                           "\n\t\tAmount Due: Rs." + parts[2];
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("❌ Error reading payment details: " + e.getMessage());
        }
        return null;
    }

    public static void manageComplaints() {
        File file = new File(COMPLAINTS_FILE);
        if (!file.exists()) {
            System.out.println("❌ No complaints found.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(COMPLAINTS_FILE))) {
            String line;
            System.out.println("\n📜 All Complaints:");
            System.out.println("----------------------------------------");
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\\|");
                if (data.length >= 4) {
                    System.out.println("Time: " + data[0]);
                    System.out.println("Student: " + data[1]);
                    System.out.println("Roll No: " + data[2]);
                    System.out.println("Complaint: " + data[3]);
                    System.out.println("----------------------------------------");
                }
            }

            System.out.println("\n1. Mark Complaint as Resolved");
            System.out.println("2. Back to Menu");
            System.out.print("Enter Choice: ");
            Scanner sc = new Scanner(System.in);
            int choice = sc.nextInt();

            if (choice == 1) {
                System.out.print("Enter Roll Number of complaint to resolve: ");
                String rollNo = sc.next();
                resolveComplaint(rollNo);
            }
        } catch (IOException e) {
            System.out.println("❌ Error reading complaints: " + e.getMessage());
        }
    }

    private static void resolveComplaint(String rollNo) {
        try {
            File inputFile = new File(COMPLAINTS_FILE);
            File tempFile = new File("temp_complaints.txt");
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            PrintWriter writer = new PrintWriter(new FileWriter(tempFile));

            String line;
            boolean found = false;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\\|");
                if (data.length >= 3 && data[2].equals(rollNo)) {
                    found = true;
                    System.out.println("✅ Complaint marked as resolved.");
                    continue;
                }
                writer.println(line);
            }

            writer.close();
            reader.close();

            if (!found) {
                System.out.println("❌ No complaint found for Roll No: " + rollNo);
                tempFile.delete();
                return;
            }

            if (!inputFile.delete()) {
                System.out.println("❌ Error updating complaints file.");
                tempFile.delete();
                return;
            }

            if (!tempFile.renameTo(inputFile)) {
                System.out.println("❌ Error updating complaints file.");
                tempFile.delete();
                return;
            }

        } catch (IOException e) {
            System.out.println("❌ Error resolving complaint: " + e.getMessage());
        }
    }

    public static void viewHostelRules() {
        System.out.println("📜 Viewing Hostel Rules...");
        // Implement hostel rules view logic here
    }

    public static void viewAllPayments() {
        try (BufferedReader reader = new BufferedReader(new FileReader(STUDENT_FILE))) {
            String line;
            System.out.println("\n💰 Payment Status of All Students:");
            System.out.println("----------------------------------------");
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 13) {
                    System.out.println("Roll No: " + data[0]);
                    System.out.println("Name: " + data[2]);
                    System.out.println("Total Fee: Rs." + data[10]);
                    System.out.println("Amount Paid: Rs." + data[11]);
                    System.out.println("Amount Pending: Rs." + data[12]);
                    System.out.println("----------------------------------------");
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Error reading payment records: " + e.getMessage());
        }
    }

    public static void manageWarden() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n\t\t======================================");
            System.out.println("\n\t\t\t..::WARDEN MANAGEMENT::..");
            System.out.println("\n\t\t======================================");
            System.out.println("\n\t\t 1. Add New Warden");
            System.out.println("\n\t\t 2. View All Wardens");
            System.out.println("\n\t\t 3. View Warden by Hostel");
            System.out.println("\n\t\t 4. Update Warden Details");
            System.out.println("\n\t\t 5. Remove Warden");
            System.out.println("\n\t\t 6. Back to Admin Menu");
            System.out.println("\n\t\t======================================");
            System.out.print("\n\t\t\t   CHOICE :- ");

            if (!scanner.hasNextInt()) {
                System.out.println("❌ Invalid input! Enter a number between 1 and 6.");
                scanner.next();
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            try {
                switch (choice) {
                    case 1:
                        addNewWarden(scanner);
                        break;
                    case 2:
                        viewAllWardens();
                        break;
                    case 3:
                        viewWardenByHostel(scanner);
                        break;
                    case 4:
                        updateWardenDetails(scanner);
                        break;
                    case 5:
                        removeWarden(scanner);
                        break;
                    case 6:
                        running = false;
                        break;
                    default:
                        System.out.println("❌ Invalid choice! Try again.");
                        break;
                }
            } catch (Exception e) {
                System.out.println("❌ Error: " + e.getMessage());
            }
        }
    }

    private static void addNewWarden(Scanner scanner) throws IOException, InvalidAgeException {
        System.out.println("\n\t\t======================================");
        System.out.println("\n\t\t\t..::ADD NEW WARDEN::..");
        System.out.println("\n\t\t======================================");

        System.out.print("\n\t\tEnter Warden Name: ");
        String name = scanner.nextLine();

        System.out.print("\n\t\tEnter Warden Age: ");
        int age = Integer.parseInt(scanner.nextLine());

        System.out.print("\n\t\tEnter Assigned Hostel (Standard/Luxury): ");
        String hostel = scanner.nextLine();

        System.out.print("\n\t\tEnter Contact Number: ");
        String contact = scanner.nextLine();

        System.out.print("\n\t\tEnter Email: ");
        String email = scanner.nextLine();

        Warden warden = new Warden(name, age, hostel, contact, email);
        warden.saveToFile();
        System.out.println("\n✅ Warden added successfully!");
    }

    private static void viewAllWardens() throws IOException {
        List<Warden> wardens = Warden.getAllWardens();
        
        if (wardens.isEmpty()) {
            System.out.println("\n❌ No wardens found!");
            return;
        }

        System.out.println("\n\t\t======================================");
        System.out.println("\n\t\t\t..::ALL WARDENS::..");
        System.out.println("\n\t\t======================================");

        for (Warden warden : wardens) {
            System.out.println("\n\t\tWarden Details:");
            System.out.println("\t\tName: " + warden.getName());
            System.out.println("\t\tAge: " + warden.getAge());
            System.out.println("\t\tAssigned Hostel: " + warden.getHostelAssigned());
            System.out.println("\t\tContact: " + warden.getContactNumber());
            System.out.println("\t\tEmail: " + warden.getEmail());
            System.out.println("\t\t----------------------------------------");
        }

        System.out.println("\n\t\t======================================");
    }

    private static void viewWardenByHostel(Scanner scanner) throws IOException {
        System.out.print("\n\t\tEnter Hostel Type (Standard/Luxury): ");
        String hostel = scanner.nextLine();

        Warden warden = Warden.getWardenByHostel(hostel);
        
        if (warden == null) {
            System.out.println("\n❌ No warden assigned to " + hostel + " hostel!");
            return;
        }

        System.out.println("\n\t\t======================================");
        System.out.println("\n\t\t\t..::WARDEN DETAILS::..");
        System.out.println("\n\t\t======================================");
        System.out.println("\n\t\tWarden Details:");
        System.out.println("\t\tName: " + warden.getName());
        System.out.println("\t\tAge: " + warden.getAge());
        System.out.println("\t\tAssigned Hostel: " + warden.getHostelAssigned());
        System.out.println("\t\tContact: " + warden.getContactNumber());
        System.out.println("\t\tEmail: " + warden.getEmail());
        System.out.println("\n\t\t======================================");
    }

    private static void updateWardenDetails(Scanner scanner) throws IOException, InvalidAgeException {
        System.out.print("\n\t\tEnter Hostel Type (Standard/Luxury): ");
        String hostel = scanner.nextLine();

        Warden currentWarden = Warden.getWardenByHostel(hostel);
        if (currentWarden == null) {
            System.out.println("\n❌ No warden assigned to " + hostel + " hostel!");
            return;
        }

        System.out.println("\n\t\tCurrent Warden Details:");
        System.out.println("\t\tName: " + currentWarden.getName());
        System.out.println("\t\tAge: " + currentWarden.getAge());
        System.out.println("\t\tContact: " + currentWarden.getContactNumber());
        System.out.println("\t\tEmail: " + currentWarden.getEmail());

        System.out.println("\n\t\tEnter New Details:");
        System.out.print("\t\tNew Name (press Enter to keep current): ");
        String name = scanner.nextLine();
        if (name.isEmpty()) name = currentWarden.getName();

        System.out.print("\t\tNew Age (press Enter to keep current): ");
        String ageStr = scanner.nextLine();
        int age = ageStr.isEmpty() ? currentWarden.getAge() : Integer.parseInt(ageStr);

        System.out.print("\t\tNew Contact (press Enter to keep current): ");
        String contact = scanner.nextLine();
        if (contact.isEmpty()) contact = currentWarden.getContactNumber();

        System.out.print("\t\tNew Email (press Enter to keep current): ");
        String email = scanner.nextLine();
        if (email.isEmpty()) email = currentWarden.getEmail();

        Warden newWarden = new Warden(name, age, hostel, contact, email);
        if (Warden.updateWarden(hostel, newWarden)) {
            System.out.println("\n✅ Warden details updated successfully!");
        } else {
            System.out.println("\n❌ Failed to update warden details!");
        }
    }

    private static void removeWarden(Scanner scanner) throws IOException {
        System.out.print("\n\t\tEnter Hostel Type (Standard/Luxury): ");
        String hostel = scanner.nextLine();

        if (Warden.deleteWarden(hostel)) {
            System.out.println("\n✅ Warden removed successfully!");
        } else {
            System.out.println("\n❌ No warden found for " + hostel + " hostel!");
        }
    }
}

