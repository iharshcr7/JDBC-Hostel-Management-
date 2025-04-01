package management;

import student.Student;
import java.util.*;

public class Agreement {
    private static HashMap<String, AgreementDetails> studentAgreements = new HashMap<>(); // (RollNo → Agreement Details)

    // 📌 Agreement Details Class
    static class AgreementDetails {
        String terms;
        Date startDate;
        Date expiryDate;

        public AgreementDetails(String terms, Date startDate, Date expiryDate) {
            this.terms = terms;
            this.startDate = startDate;
            this.expiryDate = expiryDate;
        }
    }

    // 📌 Add Agreement for Student
    public static void addAgreement(Student student, String terms, Date startDate, Date expiryDate) {
        if (student == null) {
            System.out.println("❌ Invalid student information.");
            return;
        }

        String rollNumber = student.getRollNumber();
        if (studentAgreements.containsKey(rollNumber)) {
            System.out.println("⚠ Agreement already exists for " + student.getName());
            return;
        }

        studentAgreements.put(rollNumber, new AgreementDetails(terms, startDate, expiryDate));
        System.out.println("📜 Agreement signed for " + student.getName() + " (Roll No: " + rollNumber + ")");
    }

    // 📌 View Agreement
    public static void viewAgreement(String rollNumber) {
        if (rollNumber == null || rollNumber.isEmpty()) {
            System.out.println("❌ Invalid roll number.");
            return;
        }

        AgreementDetails details = studentAgreements.get(rollNumber);
        if (details != null) {
            System.out.println("\n📌 Agreement for Roll No: " + rollNumber);
            System.out.println("📜 Terms: " + details.terms);
            System.out.println("📆 Start Date: " + details.startDate);
            System.out.println("📆 Expiry Date: " + details.expiryDate);
        } else {
            System.out.println("⚠ No agreement found for this student.");
        }
    }

    // 📌 Remove Agreement
    public static void removeAgreement(String rollNumber) {
        if (rollNumber == null || rollNumber.isEmpty()) {
            System.out.println("❌ Invalid roll number.");
            return;
        }

        if (studentAgreements.remove(rollNumber) != null) {
            System.out.println("✅ Agreement removed for Roll No: " + rollNumber);
        } else {
            System.out.println("⚠ No agreement found to remove.");
        }
    }

    // 📌 List All Active Agreements
    public static void listActiveAgreements() {
        if (studentAgreements.isEmpty()) {
            System.out.println("📌 No active agreements.");
            return;
        }

        System.out.println("\n📜 Active Agreements:");
        for (Map.Entry<String, AgreementDetails> entry : studentAgreements.entrySet()) {
            System.out.println("🔹 Roll No: " + entry.getKey() + " | Expiry Date: " + entry.getValue().expiryDate);
        }
    }
}
