package info;

import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import main.PHHD;
import management.Admin;
import management.StudentManager;
import info.Registration;
import info.Rules;
import management.AccessLog;
import management.Warden;
import info.Search;
import info.Information;
import java.io.IOException;
import java.util.List;
import exception.InvalidAgeException;
import database.DatabaseManager;

public class Menu {
    private static final Scanner scanner = PHHD.scanner; // Shared Scanner
    private static String studentRollNo = null;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void displayMainMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n=============================================");
            System.out.println("         ..:: WELCOME TO OUR HOSTEL ::..");
            System.out.println("=============================================");
            System.out.println("1. Login");
            System.out.println("2. View Hostel Rules");
            System.out.println("3. Exit");
            System.out.print("Enter Choice: ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        loginMenu();
                        break;
                    case 2:
                        Rules.displayRules();
                        break;
                    case 3:
                        System.out.println("\n✓ Thank you for using our system!");
                        System.exit(0);
                    default:
                        System.out.println("? Invalid choice!");
                }
            } catch (NumberFormatException e) {
                System.out.println("? Invalid input!");
            }
        }
    }

    private static void loginMenu() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n=== Login Menu ===");
        System.out.println("1. Admin Login");
        System.out.println("2. Student Login");
        System.out.print("Enter Choice: ");

        try {
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    adminLogin();
                    break;
                case 2:
                    studentLogin();
                    break;
                default:
                    System.out.println("? Invalid choice!");
            }
        } catch (NumberFormatException e) {
            System.out.println("? Invalid input!");
        }
    }

    private static void adminLogin() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nEnter Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        if (DatabaseManager.validateAdminLogin(username, password)) {
            System.out.println("\n✓ Admin login successful!");
            adminMenu();
        } else {
            System.out.println("\n❌ Invalid admin credentials!");
        }
    }

    private static void adminMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n=== Admin Menu ===");
            System.out.println("1. Student Management");
            System.out.println("2. Room Management");
            System.out.println("3. Warden Management");
            System.out.println("4. Complaint Management");
            System.out.println("5. View Hostel Rules");
            System.out.println("6. Logout");
            System.out.print("Enter Choice: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1: studentManagementMenu(); break;
                    case 2: roomManagementMenu(); break;
                    case 3: wardenManagementMenu(); break;
                    case 4: complaintManagementMenu(); break;
                    case 5: Rules.displayRules(); break;
                    case 6: return;
                    default: System.out.println("? Invalid choice!");
                }
            } catch (NumberFormatException e) {
                System.out.println("? Invalid input!");
            }
        }
    }

    private static void studentManagementMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n=== Student Management ===");
            System.out.println("1. View All Students");
            System.out.println("2. Add New Student");
            System.out.println("3. Update Student Details");
            System.out.println("4. Delete Student");
            System.out.println("5. Search Student");
            System.out.println("6. Back");
            System.out.print("Enter Choice: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1: DatabaseManager.displayAllStudents(); break;
                    case 2: DatabaseManager.addNewStudent(); break;
                    case 3:
                        System.out.print("Enter Roll Number: ");
                        String rollNo = scanner.nextLine();
                        DatabaseManager.updateStudent(rollNo);
                        break;
                    case 4:
                        System.out.print("Enter Roll Number to delete: ");
                        rollNo = scanner.nextLine();
                        DatabaseManager.deleteStudent(rollNo);
                        break;
                    case 5: searchStudentMenu(); break;
                    case 6: return;
                    default: System.out.println("? Invalid choice!");
                }
            } catch (NumberFormatException e) {
                System.out.println("? Invalid input!");
            }
        }
    }

    private static void roomManagementMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n=== Room Management ===");
            System.out.println("1. View All Rooms");
            System.out.println("2. Add New Room");
            System.out.println("3. Update Room");
            System.out.println("4. Delete Room");
            System.out.println("5. Fix Room Inconsistencies");
            System.out.println("6. Back");
            System.out.print("Enter Choice: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1: DatabaseManager.displayAllRooms(); break;
                    case 2: DatabaseManager.addNewRoom(); break;
                    case 3: DatabaseManager.updateRoom(); break;
                    case 4: DatabaseManager.deleteRoom(); break;
                    case 5: DatabaseManager.fixRoomInconsistencies(); break;
                    case 6: return;
                    default: System.out.println("? Invalid choice!");
                }
            } catch (NumberFormatException e) {
                System.out.println("? Invalid input!");
            }
        }
    }

    private static void wardenManagementMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n=== Warden Management ===");
            System.out.println("1. View All Wardens");
            System.out.println("2. Add New Warden");
            System.out.println("3. Update Warden Details");
            System.out.println("4. Delete Warden");
            System.out.println("5. Search Warden");
            System.out.println("6. Back");
            System.out.print("Enter Choice: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1: DatabaseManager.displayAllWardens(); break;
                    case 2: DatabaseManager.addNewWarden(); break;
                    case 3:
                        System.out.print("Enter Warden ID: ");
                        String wardenId = scanner.nextLine();
                        DatabaseManager.updateWarden(wardenId);
                        break;
                    case 4:
                        System.out.print("Enter Warden ID to delete: ");
                        wardenId = scanner.nextLine();
                        DatabaseManager.deleteWarden(wardenId);
                        break;
                    case 5: searchWardenMenu(); break;
                    case 6: return;
                    default: System.out.println("? Invalid choice!");
                }
            } catch (NumberFormatException e) {
                System.out.println("? Invalid input!");
            }
        }
    }

    private static void complaintManagementMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n=== Complaint Management ===");
            System.out.println("1. View All Complaints");
            System.out.println("2. View Pending Complaints");
            System.out.println("3. Add New Complaint");
            System.out.println("4. Update Complaint Status");
            System.out.println("5. Delete Complaint");
            System.out.println("6. Search Complaints");
            System.out.println("7. Back");
            System.out.print("Enter Choice: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1: DatabaseManager.displayAllComplaints(); break;
                    case 2: DatabaseManager.displayPendingComplaints(); break;
                    case 3: DatabaseManager.addNewComplaint(); break;
                    case 4:
                        System.out.print("Enter Complaint ID: ");
                        int complaintId = Integer.parseInt(scanner.nextLine());
                        DatabaseManager.updateComplaintStatus(complaintId);
                        break;
                    case 5:
                        System.out.print("Enter Complaint ID to delete: ");
                        complaintId = Integer.parseInt(scanner.nextLine());
                        DatabaseManager.deleteComplaint(complaintId);
                        break;
                    case 6: searchComplaintMenu(); break;
                    case 7: return;
                    default: System.out.println("? Invalid choice!");
                }
            } catch (NumberFormatException e) {
                System.out.println("? Invalid input!");
            }
        }
    }

    private static void searchStudentMenu() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n=== Search Student ===");
        System.out.println("1. Search by Roll Number");
        System.out.println("2. Search by Name");
        System.out.print("Enter Choice: ");

        try {
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    System.out.print("Enter Roll Number: ");
                    String rollNo = scanner.nextLine();
                    DatabaseManager.searchStudentByRoll(rollNo);
                    break;
                case 2:
                    System.out.print("Enter Name: ");
                    String name = scanner.nextLine();
                    DatabaseManager.searchStudentByName(name);
                    break;
                default:
                    System.out.println("? Invalid choice!");
            }
        } catch (NumberFormatException e) {
            System.out.println("? Invalid input!");
        }
    }

    private static void studentLogin() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nEnter Roll Number: ");
        String rollNo = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        if (DatabaseManager.validateStudentLogin(rollNo, password)) {
            System.out.println("\n✓ Student login successful!");
            studentMenu(rollNo);
        } else {
            System.out.println("\n❌ Invalid student credentials!");
        }
    }

    private static void studentMenu(String rollNo) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n=== Student Menu ===");
            System.out.println("1. View My Details");
            System.out.println("2. View Room Details");
            System.out.println("3. View Payment Status");
            System.out.println("4. File Complaint");
            System.out.println("5. View My Complaints");
            System.out.println("6. View Hostel Rules");
            System.out.println("7. Logout");
            System.out.print("Enter Choice: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1: DatabaseManager.displayStudentDetails(rollNo); break;
                    case 2: DatabaseManager.displayRoomDetails(rollNo); break;
                    case 3: DatabaseManager.displayPaymentStatus(rollNo); break;
                    case 4: DatabaseManager.fileNewComplaint(rollNo); break;
                    case 5: DatabaseManager.viewMyComplaints(rollNo); break;
                    case 6: Rules.displayRules(); break;
                    case 7: return;
                    default: System.out.println("? Invalid choice!");
                }
            } catch (NumberFormatException e) {
                System.out.println("? Invalid input!");
            }
        }
    }

    public static void viewRules() {
        Rules.displayRules();
    }

    private static void searchWardenMenu() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n=== Search Warden ===");
        System.out.println("1. Search by ID");
        System.out.println("2. Search by Name");
        System.out.print("Enter Choice: ");

        try {
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    System.out.print("Enter Warden ID: ");
                    String id = scanner.nextLine();
                    DatabaseManager.searchWardenById(id);
                    break;
                case 2:
                    System.out.print("Enter Warden Name: ");
                    String name = scanner.nextLine();
                    DatabaseManager.searchWardenByName(name);
                    break;
                default:
                    System.out.println("? Invalid choice!");
            }
        } catch (NumberFormatException e) {
            System.out.println("? Invalid input!");
        }
    }

    private static void searchComplaintMenu() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n=== Search Complaints ===");
        System.out.println("1. Search by Student Roll Number");
        System.out.println("2. Search by Status");
        System.out.print("Enter Choice: ");

        try {
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    System.out.print("Enter Student Roll Number: ");
                    String rollNo = scanner.nextLine();
                    DatabaseManager.searchComplaintsByStudent(rollNo);
                    break;
                case 2:
                    System.out.print("Enter Status: ");
                    String status = scanner.nextLine();
                    DatabaseManager.searchComplaintsByStatus(status);
                    break;
                default:
                    System.out.println("? Invalid choice!");
            }
        } catch (NumberFormatException e) {
            System.out.println("? Invalid input!");
        }
    }
}
