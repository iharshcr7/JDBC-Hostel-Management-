package management;

import student.Student;
import java.util.HashMap;

public class RentalPayment {
    private static HashMap<String, Integer> paymentRecords = new HashMap<>(); // (Roll Number → Total Paid Amount)

    // 📌 Make Payment
    public static void makePayment(Student student, int amount, int totalFee) {
        if (amount <= 0) {
            System.out.println("❌ Invalid payment amount. Please enter a valid amount.");
            return;
        }

        int totalPaid = paymentRecords.getOrDefault(student.getRollNumber(), 0) + amount;
        paymentRecords.put(student.getRollNumber(), totalPaid);

        System.out.println("💰 Payment of Rs." + amount + " received from " + student.getName());

        if (totalPaid >= totalFee) {
            System.out.println("✅ Full payment completed for " + student.getName() + " (Roll No: " + student.getRollNumber() + ").");
        } else {
            System.out.println("⚠ Pending Balance: Rs." + (totalFee - totalPaid));
        }
    }

    // 📌 View Payment Status
    public static void viewPayment(String rollNumber, int totalFee) {
        int paidAmount = paymentRecords.getOrDefault(rollNumber, 0);
        if (paidAmount > 0) {
            System.out.println("✅ Payment Record: Rs." + paidAmount + " paid for Roll No: " + rollNumber);
            System.out.println("⚠ Remaining Balance: Rs." + (totalFee - paidAmount));
        } else {
            System.out.println("⚠ No payment found for this student.");
        }
    }

    // 📌 Check if Payment is Completed
    public static boolean isPaymentComplete(String rollNumber, int totalFee) {
        return paymentRecords.getOrDefault(rollNumber, 0) >= totalFee;
    }

    // 📌 View All Payments
    public static void viewAllPayments() {
        if (paymentRecords.isEmpty()) {
            System.out.println("📌 No payments have been made yet.");
            return;
        }

        System.out.println("\n💰 Payment Records:");
        for (String rollNumber : paymentRecords.keySet()) {
            System.out.println("🔹 Roll No: " + rollNumber + " → Rs." + paymentRecords.get(rollNumber));
        }
    }
}
