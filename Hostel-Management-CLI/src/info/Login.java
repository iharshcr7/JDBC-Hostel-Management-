package info;

import java.io.*;
import java.util.Scanner;
import management.StudentManager;

public class Login {
    private static final String ADMIN_FILE = "admin_credentials.txt";
    private static final String STUDENT_FILE = "student_credentials.txt";

    public static String login(Scanner scanner) {
        System.out.println("\n? Login to DHHP Hostel System");
        System.out.println("1. Admin");
        System.out.println("2. Student");
        System.out.print("Enter Choice: ");

        if (!scanner.hasNextInt()) {
            System.out.println("❌ Invalid input! Enter a number (1-2).");
            scanner.nextLine(); // Consume invalid input
            return "Invalid";
        }

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                return adminLogin(scanner);
            case 2:
                return studentLogin(scanner);
            default:
                System.out.println("❌ Invalid choice! Try again.");
                return "Invalid";
        }
    }

    private static String adminLogin(Scanner scanner) {
        // Check if admin credentials file exists
        File file = new File(ADMIN_FILE);
        if (!file.exists() || file.length() == 0) {
            System.out.println("\n❌ No admin account found!");
            System.out.println("Please register as admin first.");
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
            return "Invalid";
        }

        System.out.print("Admin Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        if (validateCredentials(username, password, ADMIN_FILE)) {
            System.out.println("\n✅ Login successful!");
            return "Admin";
        } else {
            System.out.println("\n❌ Invalid credentials!");
            return "Invalid";
        }
    }

    private static String studentLogin(Scanner scanner) {
        // Check if student credentials file exists
        File file = new File(STUDENT_FILE);
        if (!file.exists() || file.length() == 0) {
            System.out.println("\n❌ No student accounts found!");
            System.out.println("Please contact admin to register.");
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
            return "Invalid";
        }

        System.out.print("Roll Number: ");
        String rollNo = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        if (StudentManager.authenticate(rollNo, password)) {
            System.out.println("\n✅ Login successful!");
            return "Student";
        } else {
            System.out.println("\n❌ Invalid credentials!");
            return "Invalid";
        }
    }

    private static boolean validateCredentials(String username, String password, String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] credentials = line.split(",");
                if (credentials.length >= 2 && credentials[0].equals(username) && credentials[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Error reading credentials: " + e.getMessage());
        }
        return false;
    }

    public static boolean ensureAdminExists() {
        // Remove this check since we want to allow multiple admins
        return false;  // Always return false to allow new admin registration
    }
    
    public static void registerAdmin() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n? Registering New Admin...\n");
        
        System.out.print("Enter admin username: ");
        String username = scanner.nextLine();
        
        System.out.print("Enter admin password: ");
        String password = scanner.nextLine();
        
        try (FileWriter writer = new FileWriter(ADMIN_FILE, true)) {
            writer.write(username + "," + password + "\n");
            System.out.println("\n✓ Admin registered successfully!");
        } catch (IOException e) {
            System.out.println("❌ Error registering admin: " + e.getMessage());
        }
    }
} 