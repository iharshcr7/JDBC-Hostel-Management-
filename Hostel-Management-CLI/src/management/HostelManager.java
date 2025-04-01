package management;

import main.PHHD;
import student.Student;
import exception.InvalidAgeException;
import java.io.*;
import java.util.*;

public class HostelManager {
    private List<Student> students = new ArrayList<>();
    private Set<Integer> allocatedRooms = new HashSet<>();
    private static final String ROOM_FILE = "src/student_information/rooms.txt";
    private static final Set<String> VALID_PAYMENT_METHODS = new HashSet<>(Arrays.asList("upi", "cash", "card", "net banking"));
    private static final Set<Integer> STANDARD_ROOMS = new HashSet<>(Arrays.asList(101, 102, 103));
    private static final Set<Integer> LUXURY_ROOMS = new HashSet<>(Arrays.asList(201, 202, 203));

    public HostelManager() {
        loadRoomAllocations(); // Load allocated rooms when the system starts
    }

    private void loadRoomAllocations() {
        try (BufferedReader reader = new BufferedReader(new FileReader(ROOM_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                int roomNumber = Integer.parseInt(data[0]);
                String studentName = data[1];
                allocatedRooms.add(roomNumber);
                PHHD.roomAllocation.put(roomNumber, studentName);
            }
        } catch (IOException e) {
            System.out.println("ℹ️ No previous room allocations found.");
        }
    }

    private void saveRoomAllocation(int roomNumber, String studentName) {
        try (FileWriter writer = new FileWriter(ROOM_FILE, true)) { // Append mode
            writer.write(roomNumber + "," + studentName + "\n");
        } catch (IOException e) {
            System.out.println("❌ Error saving room allocation: " + e.getMessage());
        }
    }

    public void registerStudent(Student student) {  
        students.add(student);
        allocatedRooms.add(student.getRoomNumber());  
        PHHD.roomAllocation.put(student.getRoomNumber(), student.getName());
        saveRoomAllocation(student.getRoomNumber(), student.getName()); // Ensure persistence

        System.out.println("✅ Student " + student.getName() + " Registered Successfully!");
        System.out.println("🏠 Room Allocated: " + student.getRoomNumber());
    }

    public void listStudents() {
        File folder = new File("src/student_information/");
        File[] files = folder.listFiles();

        if (files == null || files.length == 0) {
            System.out.println("❌ No student records found.");
            return;
        }

        List<String> studentFiles = new ArrayList<>();
        List<String> systemFiles = new ArrayList<>();

        for (File file : files) {
            String fileName = file.getName();
            if (fileName.equals("rooms.txt") || fileName.equals("last_id.txt")) {
                systemFiles.add(fileName);
            } else {
                studentFiles.add(fileName);
            }
        }

        System.out.println("\n📂 Registered Students:");
        int index = 1;
        for (String studentFile : studentFiles) {
            System.out.println(index++ + ". " + studentFile);
        }

        if (!systemFiles.isEmpty()) {
            System.out.println("\n------------------------------");
            System.out.println("📁 System Files:");
            for (String systemFile : systemFiles) {
                System.out.println(index++ + ". " + systemFile);
            }
        }

        System.out.print("\nEnter the number of the file you want to view: ");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        if (choice > 0 && choice <= studentFiles.size()) {
            String selectedFile = studentFiles.get(choice - 1);
            System.out.println("\n📄 Displaying details from: " + selectedFile);
            displayStudentDetails(selectedFile);
        } else if (choice > studentFiles.size() && choice <= (studentFiles.size() + systemFiles.size())) {
            String selectedSystemFile = systemFiles.get(choice - studentFiles.size() - 1);
            System.out.println("\n📁 Displaying system file: " + selectedSystemFile);
            displaySystemFile(selectedSystemFile);
        } else {
            System.out.println("⚠ Invalid choice.");
        }
    }

    private void displaySystemFile(String fileName) {
        File file = new File("src/student_information/" + fileName);
        try (Scanner scanner = new Scanner(file)) {
            System.out.println("\n==== File Contents: " + fileName + " ====\n");
            while (scanner.hasNextLine()) {
                System.out.println(scanner.nextLine());
            }
        } catch (Exception e) {
            System.out.println("⚠ Error reading file: " + fileName);
        }
    }

    public boolean isValidRoom(int roomNumber, String roomQuality) {
        return (roomQuality.equals("Standard") && STANDARD_ROOMS.contains(roomNumber))
            || (roomQuality.equals("Luxury") && LUXURY_ROOMS.contains(roomNumber));
    }

    public Set<Integer> getAllocatedRooms() {
        return allocatedRooms;
    }

    // 📌 Fetch Student by Roll No
    public Student getStudentByRollNo(String rollNo) {
        for (Student student : students) {
            if (student.getRollNumber().equals(rollNo)) {
                return student;
            }
        }
        return null;  // Return null if no student found with the provided rollNo
    }

    private void displayStudentDetails(String selectedFile) {
        File file = new File("src/student_information/" + selectedFile);
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                System.out.println(scanner.nextLine());
            }
        } catch (Exception e) {
            System.out.println("⚠ Error reading student file: " + selectedFile);
        }
    }
}
