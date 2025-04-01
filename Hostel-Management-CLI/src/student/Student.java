package student;

import exception.InvalidAgeException;
import java.io.*;
import java.util.Scanner;

public class Student extends Person implements Identifiable {
    private static final String COMPLAINTS_FILE = "complaints.txt";
    private static final String RULES_FILE = "hostel_rules.txt";
    private static final String CAFETERIA_FILE = "cafeteria.txt";

    private static int idCounter = loadLastId();
    private int id;
    private String college;
    private String department;
    private String semester;
    private String rollNumber;
    private String mobileNumber;
    private String username;
    private String password;
    private int amountPaid;
    private int amountDue;
    private String paymentMethod;
    private int roomNumber;
    private String roomQuality;

    // ✅ Updated Constructor (Added username & password)
    public Student(String name, int age, String college, String department, String semester, 
                   String rollNumber, String mobileNumber, String username, String password) throws InvalidAgeException {
        super(name, age);

        if (age < 16) {
            throw new InvalidAgeException("Age must be at least 16.");
        }
        if (!isValidMobileNumber(mobileNumber)) {
            throw new IllegalArgumentException("Invalid mobile number! Must be exactly 10 digits.");
        }

        this.id = idCounter++;  
        saveLastId(idCounter);  

        this.college = college;
        this.department = department;
        this.semester = semester;
        this.rollNumber = rollNumber;
        this.mobileNumber = mobileNumber;
        this.username = username;
        this.password = password;
        this.amountPaid = 0;
        this.amountDue = 0;
        this.paymentMethod = "Not Paid";
        this.roomNumber = 0;
        this.roomQuality = "Not Assigned";
    }

    @Override
    public int getId() {
        return id;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public String getUsername() {  
        return username;
    }

    public String getPassword() {  
        return password;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public String getRoomQuality() {
        return roomQuality;
    }

    public int getAmountPaid() {
        return amountPaid;
    }

    public int getAmountDue() {
        return amountDue;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    // ✅ Register Complaint
    public void registerComplaint() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(COMPLAINTS_FILE, true))) {
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter Complaint Details: ");
            String complaintText = sc.nextLine();
            writer.println(rollNumber + ": " + complaintText);
            System.out.println("✅ Complaint Registered Successfully!");
        } catch (IOException e) {
            System.out.println("❌ Error registering complaint: " + e.getMessage());
        }
    }

    // ✅ View Personal Details (Room, Fees, etc.)
    public void viewPersonalDetails() {
        System.out.println("\n🔹 Your Details 🔹");
        System.out.println("ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("College: " + college);
        System.out.println("Department: " + department);
        System.out.println("Semester: " + semester);
        System.out.println("Roll Number: " + rollNumber);
        System.out.println("Room Number: " + (roomNumber > 0 ? roomNumber : "Not Assigned"));
        System.out.println("Room Type: " + roomQuality);
        System.out.println("Total Fee: " + (amountPaid + amountDue));
        System.out.println("Amount Paid: " + amountPaid);
        System.out.println("Amount Due: " + amountDue);
        System.out.println("Payment Method: " + paymentMethod);
    }

    // ✅ View Hostel Rules
    public void viewHostelRules() {
        try (BufferedReader reader = new BufferedReader(new FileReader(RULES_FILE))) {
            String line;
            System.out.println("\n🏠 Hostel Rules:");
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("❌ Error reading hostel rules: " + e.getMessage());
        }
    }

    // ✅ View Cafeteria Meal Plans
    public void viewCafeteriaPlans() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CAFETERIA_FILE))) {
            String line;
            System.out.println("\n🍽 Cafeteria Meal Plans:");
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("❌ Error reading cafeteria plans: " + e.getMessage());
        }
    }

    private static boolean isValidMobileNumber(String mobileNumber) {
        return mobileNumber.matches("\\d{10}");
    }

    private static int loadLastId() {
        File file = new File("src/student_information/last_id.txt");
        if (!file.exists()) {
            return 1000;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            if (line != null && line.matches("\\d+")) {
                return Integer.parseInt(line);
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error reading last_id.txt, resetting ID counter.");
        }
        return 1000;
    }

    private static void saveLastId(int id) {
        File file = new File("src/student_information/last_id.txt");  
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write(String.valueOf(id));  
        } catch (IOException e) {
            System.out.println("Error saving last ID: " + e.getMessage());
        }
    }

    @Override
    public void displayDetails() {
        viewPersonalDetails();
    }
}
