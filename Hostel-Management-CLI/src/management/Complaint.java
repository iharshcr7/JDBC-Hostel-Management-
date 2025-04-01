package management;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Complaint {
    private String studentName;
    private String rollNumber;
    private String complaintText;
    private String status; // "Pending" or "Resolved"
    private String dateFiled;

    private static List<Complaint> complaintsList = new ArrayList<>();

    public Complaint(String studentName, String rollNumber, String complaintText) {
        this.studentName = studentName;
        this.rollNumber = rollNumber;
        this.complaintText = complaintText;
        this.status = "Pending"; // Default status

        // Get current date & time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        this.dateFiled = LocalDateTime.now().format(formatter);

        // Prevent duplicate complaints
        for (Complaint c : complaintsList) {
            if (c.rollNumber.equals(rollNumber) && c.complaintText.equalsIgnoreCase(complaintText) && c.status.equals("Pending")) {
                System.out.println("⚠ Complaint already exists for Roll No: " + rollNumber);
                return;
            }
        }

        complaintsList.add(this);
        System.out.println("📩 Complaint registered from " + studentName + " (Roll No: " + rollNumber + ") on " + dateFiled);
    }

    // 📌 View All Complaints
    public static void viewAllComplaints() {
        if (complaintsList.isEmpty()) {
            System.out.println("✅ No complaints at the moment.");
            return;
        }
        System.out.println("\n📜 Complaint List:");
        for (Complaint c : complaintsList) {
            System.out.println("🔹 " + c.studentName + " (Roll No: " + c.rollNumber + ") - " + c.complaintText +
                    " [Status: " + c.status + "] | Filed on: " + c.dateFiled);
        }
    }

    // 📌 View Only Pending Complaints
    public static void viewPendingComplaints() {
        boolean found = false;
        System.out.println("\n📜 Pending Complaints:");
        for (Complaint c : complaintsList) {
            if (c.status.equals("Pending")) {
                System.out.println("🔹 " + c.studentName + " (Roll No: " + c.rollNumber + ") - " + c.complaintText +
                        " | Filed on: " + c.dateFiled);
                found = true;
            }
        }
        if (!found) {
            System.out.println("✅ No pending complaints.");
        }
    }

    // 📌 Resolve a Complaint (Only Admins)
    public static void resolveComplaint(String rollNumber) {
        for (Complaint c : complaintsList) {
            if (c.rollNumber.equals(rollNumber) && c.status.equals("Pending")) {
                c.markResolved();
                return;
            }
        }
        System.out.println("⚠ No pending complaints found for Roll No: " + rollNumber);
    }

    // 📌 Mark Complaint as Resolved
    public void markResolved() {
        this.status = "Resolved"; // Update status to "Resolved"
        System.out.println("✅ Complaint from Roll No: " + this.rollNumber + " has been resolved.");
    }
}
