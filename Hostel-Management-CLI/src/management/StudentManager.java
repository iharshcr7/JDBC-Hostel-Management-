package management;

import java.io.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import info.Rules;

public class StudentManager {
    private static final String STUDENT_FILE = "student_details.txt";
    private static final String STUDENT_CREDENTIALS_FILE = "student_credentials.txt";
    private static final String COMPLAINTS_FILE = "complaints.txt";
    private static final String RULES_FILE = "hostel_rules.txt";
    private static final String PAYMENTS_FILE = "payments.txt";
    private static final String ROOM_FILE = "room_details.txt";
    private static final String PAYMENT_FILE = "payment_details.txt";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void studentMenu(String rollNo) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n\t\t======================================");
            System.out.println("\n\t\t\t..:: STUDENT MENU ::..");
            System.out.println("\n\t\t======================================");
            System.out.println("\n\t\t 1. View Personal Details");
            System.out.println("\n\t\t 2. View Allocated Room");
            System.out.println("\n\t\t 3. View Payment Status");
            System.out.println("\n\t\t 4. File a Complaint");
            System.out.println("\n\t\t 5. View Hostel Rules");
            System.out.println("\n\t\t 6. View Cafeteria Plans");
            System.out.println("\n\t\t 7. Logout");
            System.out.println("\n\t\t======================================");
            System.out.print("\n\t\t\t   CHOICE :- ");

            if (!scanner.hasNextInt()) {
                System.out.println("❌ Invalid input! Enter a number between 1 and 7.");
                scanner.next();
                continue;
            }
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    viewPersonalDetails(rollNo);
                    break;
                case 2:
                    viewRoomDetails(rollNo);
                    break;
                case 3:
                    viewPaymentStatus(rollNo);
                    break;
                case 4:
                    fileComplaint(rollNo);
                    break;
                case 5:
                    Rules.displayRules();
                    break;
                case 6:
                    viewCafeteriaPlans();
                    break;
                case 7:
                    System.out.println("\n✅ Logged out successfully!");
                    return;
                default:
                    System.out.println("❌ Invalid choice! Try again.");
                    break;
            }
        }
    }

    public static void viewPersonalDetails(String rollNo) {
        try (BufferedReader reader = new BufferedReader(new FileReader(STUDENT_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 8 && parts[7].equals(rollNo)) {
                    System.out.println("\n\t\t======================================");
                    System.out.println("\n\t\t\t..:: PERSONAL DETAILS ::..");
                    System.out.println("\n\t\t======================================");
                    System.out.println("\n\t\tStudent ID: " + parts[0]);
                    System.out.println("\n\t\tName: " + parts[1]);
                    System.out.println("\n\t\tCollege: " + parts[2]);
                    System.out.println("\n\t\tDepartment: " + parts[3]);
                    System.out.println("\n\t\tSemester: " + parts[4]);
                    System.out.println("\n\t\tAge: " + parts[5]);
                    System.out.println("\n\t\tMobile: " + parts[6]);
                    System.out.println("\n\t\tRoll Number: " + parts[7]);
                    System.out.println("\n\t\t======================================");
                    return;
                }
            }
            System.out.println("\n❌ Student details not found!");
        } catch (IOException e) {
            System.out.println("❌ Error reading student details: " + e.getMessage());
        }
    }

    public static void viewRoomDetails(String rollNo) {
        try (BufferedReader reader = new BufferedReader(new FileReader(ROOM_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4 && parts[3].equals(rollNo)) {
                    System.out.println("\n\t\t======================================");
                    System.out.println("\n\t\t\t..:: ROOM DETAILS ::..");
                    System.out.println("\n\t\t======================================");
                    System.out.println("\n\t\tRoom Number: " + parts[0]);
                    System.out.println("\n\t\tRoom Type: " + parts[1]);
                    System.out.println("\n\t\tSharing Type: " + parts[2]);
                    System.out.println("\n\t\t======================================");
                    return;
                }
            }
            System.out.println("\n❌ Room details not found!");
        } catch (IOException e) {
            System.out.println("❌ Error reading room details: " + e.getMessage());
        }
    }

    public static void viewPaymentStatus(String rollNo) {
        try (BufferedReader reader = new BufferedReader(new FileReader(PAYMENT_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3 && parts[0].equals(rollNo)) {
                    System.out.println("\n\t\t======================================");
                    System.out.println("\n\t\t\t..:: PAYMENT STATUS ::..");
                    System.out.println("\n\t\t======================================");
                    System.out.println("\n\t\tAmount Paid: Rs." + parts[1]);
                    System.out.println("\n\t\tAmount Due: Rs." + parts[2]);
                    System.out.println("\n\t\t======================================");
                    return;
                }
            }
            System.out.println("\n❌ Payment details not found!");
        } catch (IOException e) {
            System.out.println("❌ Error reading payment details: " + e.getMessage());
        }
    }

    public static void fileComplaint(String rollNo) {
        try {
            System.out.println("\n\t\t======================================");
            System.out.println("\n\t\t\t..:: FILE A COMPLAINT ::..");
            System.out.println("\n\t\t======================================");
            System.out.print("\n\t\tEnter your complaint: ");
            String complaint = new Scanner(System.in).nextLine();

            if (complaint.trim().isEmpty()) {
                System.out.println("\n❌ Complaint cannot be empty!");
                return;
            }

            String timestamp = LocalDateTime.now().format(formatter);
            try (PrintWriter writer = new PrintWriter(new FileWriter(COMPLAINTS_FILE, true))) {
                writer.println(timestamp + " | " + rollNo + " | " + complaint);
            }
            System.out.println("\n✅ Complaint filed successfully!");
            System.out.println("\n\t\tPress Enter to continue...");
            new Scanner(System.in).nextLine();
        } catch (IOException e) {
            System.out.println("❌ Error filing complaint: " + e.getMessage());
        }
    }

    public static void viewCafeteriaPlans() {
        System.out.println("\n\t\t======================================");
        System.out.println("\n\t\t\t..:: CAFETERIA PLANS ::..");
        System.out.println("\n\t\t======================================");
        System.out.println("\n\t\t1. Basic Plan (Rs. 2,000/month)");
        System.out.println("\n\t\t   • Breakfast");
        System.out.println("\n\t\t   • Lunch");
        System.out.println("\n\t\t   • Dinner");
        System.out.println("\n\t\t2. Premium Plan (Rs. 3,000/month)");
        System.out.println("\n\t\t   • Breakfast");
        System.out.println("\n\t\t   • Lunch");
        System.out.println("\n\t\t   • Dinner");
        System.out.println("\n\t\t   • Evening Snacks");
        System.out.println("\n\t\t3. Deluxe Plan (Rs. 4,000/month)");
        System.out.println("\n\t\t   • Breakfast");
        System.out.println("\n\t\t   • Lunch");
        System.out.println("\n\t\t   • Dinner");
        System.out.println("\n\t\t   • Evening Snacks");
        System.out.println("\n\t\t   • Weekend Special");
        System.out.println("\n\t\t======================================");
        System.out.println("\n\t\tPress Enter to continue...");
        new Scanner(System.in).nextLine();
    }

    public static String getStudentName(String rollNo) {
        try (BufferedReader reader = new BufferedReader(new FileReader(STUDENT_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 2 && data[0].equals(rollNo)) {
                    return data[1]; // Return actual student name
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Error reading student name: " + e.getMessage());
        }
        return "Unknown Student"; // Default if student not found
    }

    public static boolean authenticate(String rollNo, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(STUDENT_CREDENTIALS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] credentials = line.trim().split(",");
                if (credentials.length >= 2 && 
                    credentials[0].equals(rollNo) && 
                    credentials[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Error reading student credentials: " + e.getMessage());
        }
        return false;
    }

    public static void viewHostelRules() {
        File file = new File(RULES_FILE);
        if (!file.exists()) {
            System.out.println("❌ No hostel rules file found.");
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            System.out.println("\n📜 Hostel Rules:");
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("❌ Error reading hostel rules: " + e.getMessage());
        }
    }

    public static String getStudentDetails(String searchQuery) {
        File file = new File(STUDENT_FILE);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 2 && (data[0].equalsIgnoreCase(searchQuery) || data[1].equalsIgnoreCase(searchQuery))) {
                    return "Roll No: " + data[0] + "\nName: " + data[1];
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Error reading student details: " + e.getMessage());
        }
        return null;
    }

    public static void viewStudentDetails(String rollNo) {
        try {
            File file = new File(STUDENT_FILE);
            if (!file.exists()) {
                System.out.println("❌ No student records found!");
                return;
            }

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            boolean found = false;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 8 && parts[7].equals(rollNo)) {
                    found = true;
                    System.out.println("\n\t\t======================================");
                    System.out.println("\n\t\t\t..:: STUDENT DETAILS ::..");
                    System.out.println("\n\t\t======================================");
                    System.out.println("\n\t\tStudent ID: " + parts[0]);
                    System.out.println("\n\t\tName: " + parts[1]);
                    System.out.println("\n\t\tCollege: " + parts[2]);
                    System.out.println("\n\t\tDepartment: " + parts[3]);
                    System.out.println("\n\t\tSemester: " + parts[4]);
                    System.out.println("\n\t\tRoll Number: " + parts[7]);
                    System.out.println("\n\t\tAge: " + parts[5]);
                    System.out.println("\n\t\tMobile Number: " + parts[6]);
                    
                    // Get room details
                    String roomDetails = getRoomDetails(rollNo);
                    if (roomDetails != null) {
                        System.out.println("\n\t\tRoom Details:");
                        System.out.println(roomDetails);
                    }
                    
                    // Get payment details
                    String paymentDetails = getPaymentDetails(rollNo);
                    if (paymentDetails != null) {
                        System.out.println("\n\t\tPayment Details:");
                        System.out.println(paymentDetails);
                    }
                    
                    System.out.println("\n\t\t======================================");
                    break;
                }
            }
            reader.close();

            if (!found) {
                System.out.println("❌ Student not found!");
            }
        } catch (IOException e) {
            System.out.println("❌ Error reading student details: " + e.getMessage());
        }
    }

    private static String getRoomDetails(String rollNo) {
        try {
            File file = new File(ROOM_FILE);
            if (!file.exists()) return null;

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3 && parts[2].equals(rollNo)) {
                    reader.close();
                    return "\n\t\tRoom Number: " + parts[0] +
                           "\n\t\tRoom Type: " + parts[1];
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
            File file = new File(PAYMENT_FILE);
            if (!file.exists()) return null;

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3 && parts[0].equals(rollNo)) {
                    reader.close();
                    return "\n\t\tAmount Paid: Rs." + parts[1] +
                           "\n\t\tAmount Due: Rs." + parts[2];
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("❌ Error reading payment details: " + e.getMessage());
        }
        return null;
    }

    public static void updatePayment(String rollNo, int amount) {
        try {
            File inputFile = new File(STUDENT_FILE);
            File tempFile = new File("temp_students.txt");
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            PrintWriter writer = new PrintWriter(new FileWriter(tempFile));

            String line;
            boolean found = false;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 13 && data[0].equals(rollNo)) {
                    int totalFee = Integer.parseInt(data[10]);
                    int amountPaid = Integer.parseInt(data[11]) + amount;
                    int pendingAmount = totalFee - amountPaid;
                    
                    // Update the payment information
                    data[11] = String.valueOf(amountPaid);
                    data[12] = String.valueOf(pendingAmount);
                    
                    writer.println(String.join(",", data));
                    found = true;
                    System.out.println("✅ Payment updated successfully!");
                    System.out.println("Amount Paid: Rs." + amountPaid);
                    System.out.println("Pending Amount: Rs." + pendingAmount);
                } else {
                    writer.println(line);
                }
            }

            writer.close();
            reader.close();

            if (!found) {
                System.out.println("❌ Student not found!");
                tempFile.delete();
                return;
            }

            if (!inputFile.delete()) {
                System.out.println("❌ Error updating student file.");
                tempFile.delete();
                return;
            }

            if (!tempFile.renameTo(inputFile)) {
                System.out.println("❌ Error updating student file.");
                tempFile.delete();
                return;
            }

        } catch (IOException e) {
            System.out.println("❌ Error updating payment: " + e.getMessage());
        }
    }
}

