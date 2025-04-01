package info;

import student.Student;
import exception.InvalidAgeException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;

public class Registration {
    private static final Scanner scanner = new Scanner(System.in);
    private static final List<Student> studentList = new ArrayList<>();  // Temporary storage

    public static void registerStudent() {
        try {
            System.out.println("\n🔹 Register a New Student 🔹");

            System.out.print("Enter Name: ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) throw new IllegalArgumentException("❌ Name cannot be empty.");

            System.out.print("Enter Age: ");
            while (!scanner.hasNextInt()) {
                System.out.println("❌ Invalid input! Age must be a number.");
                scanner.next();
            }
            int age = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            if (age < 16) throw new InvalidAgeException("❌ Age must be at least 16.");

            System.out.print("Enter College: ");
            String college = scanner.nextLine().trim();

            System.out.print("Enter Department: ");
            String department = scanner.nextLine().trim();

            System.out.print("Enter Semester: ");
            String semester = scanner.nextLine().trim();

            System.out.print("Enter Roll Number: ");
            String rollNumber = scanner.nextLine().trim();
            if (isRollNumberExists(rollNumber)) throw new IllegalArgumentException("❌ Roll number already exists!");

            System.out.print("Enter Mobile Number (10 digits): ");
            String mobileNumber = scanner.nextLine().trim();
            if (!isValidMobileNumber(mobileNumber)) throw new IllegalArgumentException("❌ Invalid mobile number! Must be exactly 10 digits.");

            // Room Allocation
            System.out.println("\n\t\t======================================");
            System.out.println("\n\t\t\t..::ROOM ALLOCATION::..");
            System.out.println("\n\t\t======================================");
            System.out.println("\n\t\t 1. Standard Room");
            System.out.println("\n\t\t 2. Luxury Room");
            System.out.println("\n\t\t======================================");
            System.out.print("\n\t\t\t   CHOICE :- ");
            
            int roomTypeChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            // Show room details based on choice
            if (roomTypeChoice == 1) {
                System.out.println("\n\t\t======================================");
                System.out.println("\n\t\t\t..::STANDARD ROOMS::..");
                System.out.println("\n\t\t======================================");
                System.out.println("\n\t\tRoom Type\tFee");
                System.out.println("\n\t\t4 Sharing\tRs.60,000");
                System.out.println("\n\t\t2 Sharing\tRs.70,000");
                System.out.println("\n\t\t1 Sharing\tRs.1,10,000");
                System.out.println("\n\t\tNo Sharing\tRs.1,30,000");
                System.out.println("\n\t\tAvailable Rooms: [101, 102, 103, 104]");
            } else {
                System.out.println("\n\t\t======================================");
                System.out.println("\n\t\t\t..::LUXURY ROOMS::..");
                System.out.println("\n\t\t======================================");
                System.out.println("\n\t\tRoom Type\tFee");
                System.out.println("\n\t\t4 Sharing\tRs.80,000");
                System.out.println("\n\t\t2 Sharing\tRs.1,20,000");
                System.out.println("\n\t\t1 Sharing\tRs.1,30,000");
                System.out.println("\n\t\tNo Sharing\tRs.1,50,000");
                System.out.println("\n\t\tAvailable Rooms: [201, 202, 203, 204]");
            }

            System.out.println("\n\t\t Select Room Sharing Option:");
            System.out.println("\n\t\t 1. 4 Sharing");
            System.out.println("\n\t\t 2. 2 Sharing");
            System.out.println("\n\t\t 3. 1 Sharing");
            System.out.println("\n\t\t 4. No Sharing");
            System.out.print("\n\t\t\t   CHOICE :- ");
            
            int sharingChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            String roomType = "";
            int totalFee = 0;
            String roomNumber = "";
            boolean roomSelected = false;

            while (!roomSelected) {
                System.out.print("\n\t\tEnter Room Number: ");
                String requestedRoom = scanner.nextLine();
                
                // Check if room exists and is available
                if (roomTypeChoice == 1) {
                    if (requestedRoom.matches("10[1-4]")) {
                        roomType = "Standard";
                        // Set fee based on sharing choice
                        switch(sharingChoice) {
                            case 1: totalFee = 60000; break;  // 4 Sharing
                            case 2: totalFee = 70000; break;  // 2 Sharing
                            case 3: totalFee = 110000; break; // 1 Sharing
                            case 4: totalFee = 130000; break; // No Sharing
                            default: 
                                System.out.println("\n❌ Invalid sharing choice!");
                                return;
                        }
                    } else {
                        System.out.println("\n❌ Invalid room number! Please select from 101-104");
                        continue;
                    }
                } else {
                    if (requestedRoom.matches("20[1-4]")) {
                        roomType = "Luxury";
                        // Set fee based on sharing choice
                        switch(sharingChoice) {
                            case 1: totalFee = 80000; break;   // 4 Sharing
                            case 2: totalFee = 120000; break;  // 2 Sharing
                            case 3: totalFee = 130000; break;  // 1 Sharing
                            case 4: totalFee = 150000; break;  // No Sharing
                            default: 
                                System.out.println("\n❌ Invalid sharing choice!");
                                return;
                        }
                    } else {
                        System.out.println("\n❌ Invalid room number! Please select from 201-204");
                        continue;
                    }
                }

                // Check if room is already occupied
                if (isRoomOccupied(requestedRoom)) {
                    System.out.println("\n❌ Room " + requestedRoom + " is already occupied!");
                    System.out.println("Please select another room.");
                    continue;
                }

                roomNumber = requestedRoom;
                roomSelected = true;
            }

            String sharing = sharingChoice == 4 ? "No Sharing" : sharingChoice + " Sharing";

            // Handle Payment
            System.out.println("\n\t\t======================================");
            System.out.println("\n\t\t\t..::PAYMENT DETAILS::..");
            System.out.println("\n\t\t======================================");
            System.out.println("\n\t\tTotal Fee: Rs." + totalFee);
            System.out.println("\nEnter Amount to Pay: ");
            String amountStr = scanner.nextLine();
            int amountPaid;
            
            try {
                // Remove any non-numeric characters and spaces
                amountStr = amountStr.replaceAll("[^0-9]", "").trim();
                amountPaid = Integer.parseInt(amountStr);
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid amount format! Please enter a number.");
                return;
            }

            if (amountPaid > totalFee) {
                System.out.println("❌ Amount paid cannot be greater than total fee!");
                return;
            }

            int amountDue = totalFee - amountPaid;

            // Generate username and password
            String username = name.substring(0, 3).toLowerCase() + rollNumber;
            String password = generatePassword();

            // Save student details
            String studentId = String.valueOf(System.currentTimeMillis() % 10000);
            try (PrintWriter writer = new PrintWriter(new FileWriter("student_details.txt", true))) {
                writer.println(studentId + "," + name + "," + college + "," + department + "," + 
                             semester + "," + age + "," + mobileNumber + "," + rollNumber);
            }

            // Save room details
            try (PrintWriter writer = new PrintWriter(new FileWriter("room_details.txt", true))) {
                writer.println(roomNumber + "," + roomType + "," + sharing + "," + rollNumber);
            }

            // Save payment details
            try (FileWriter writer = new FileWriter("payment_details.txt", true)) {
                writer.write(rollNumber + "," + amountPaid + "," + amountDue + "\n");
                System.out.println("\n✓ Payment details saved successfully!");
                System.out.println("? Amount Due: Rs." + amountDue);
            }

            // Save login credentials
            try (PrintWriter writer = new PrintWriter(new FileWriter("student_credentials.txt", true))) {
                writer.println(rollNumber + "," + password);
            }

            System.out.println("\n✅ Student Registered Successfully!");
            System.out.println("➡ Username: " + username);
            System.out.println("➡ Password: " + password + " (Change after first login)");
            System.out.println("\n✅ Payment Received: Rs." + amountPaid);
            System.out.println("✅ Room " + roomNumber + " Allocated Successfully!");

        } catch (InvalidAgeException | IllegalArgumentException | IOException e) {
            System.out.println("❌ Error registering student: " + e.getMessage());
        }
    }

    // ✅ Check if Roll Number Already Exists
    private static boolean isRollNumberExists(String rollNumber) {
        for (Student s : studentList) {
            if (s.getRollNumber().equals(rollNumber)) return true;
        }
        return false;
    }

    // ✅ Validate Mobile Number (10 Digits)
    private static boolean isValidMobileNumber(String mobileNumber) {
        return mobileNumber.matches("\\d{10}");
    }

    // ✅ Generate Secure Password (Mix of 6-digit number & 2 random letters)
    private static String generatePassword() {
        Random rand = new Random();
        int numberPart = 100000 + rand.nextInt(900000);  // 6-digit number
        char letter1 = (char) ('A' + rand.nextInt(26));
        char letter2 = (char) ('A' + rand.nextInt(26));
        return numberPart + "" + letter1 + letter2;  // Example: 654321AB
    }

    // ✅ View Registered Students (For Testing)
    public static void viewRegisteredStudents() {
        if (studentList.isEmpty()) {
            System.out.println("❌ No students registered yet!");
            return;
        }
        System.out.println("\n🔹 Registered Students 🔹");
        for (Student s : studentList) {
            System.out.println("➡ " + s.getName() + " (Username: " + s.getUsername() + ")");
        }
    }

    // Check if room is already occupied
    private static boolean isRoomOccupied(String roomNumber) {
        try {
            File file = new File("room_details.txt");
            if (!file.exists()) return false;

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(roomNumber)) {
                    reader.close();
                    return true;
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("❌ Error checking room availability: " + e.getMessage());
        }
        return false;
    }
}
