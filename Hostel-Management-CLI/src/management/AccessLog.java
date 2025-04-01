package management;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AccessLog {
    private String studentName;
    private String rollNumber;
    private String action; // "Entry" or "Exit"
    private LocalDateTime timestamp;

    private static final List<AccessLog> logEntries = new ArrayList<>();
    private static final String LOG_FILE = "access_logs.txt";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public AccessLog(String studentName, String rollNumber, String action) {
        // ✅ Validate Entry-Exit Order
        if (action.equalsIgnoreCase("Exit") && !isLastEntry(rollNumber, "Entry")) {
            System.out.println("⚠ Cannot log 'Exit' without a prior 'Entry' for Roll No: " + rollNumber);
            return;
        }

        this.studentName = studentName;
        this.rollNumber = rollNumber;
        this.action = action;
        this.timestamp = LocalDateTime.now();

        logEntries.add(this);
        System.out.println("📌 Log Entry: " + studentName + " (Roll No: " + rollNumber + ") - " + action + " at " + timestamp);
    }

    // ✅ View All Logs
    public static void viewAllLogs() {
        if (logEntries.isEmpty()) {
            System.out.println("✅ No access logs recorded.");
            return;
        }
        System.out.println("\n📜 Hostel Entry/Exit Logs:");
        for (AccessLog log : logEntries) {
            System.out.println("🔹 " + log.studentName + " (Roll No: " + log.rollNumber + ") - " + log.action + " at " + log.timestamp);
        }
    }

    // ✅ View Logs for a Specific Student
    public static void viewLogsForStudent(String rollNumber) {
        boolean found = false;
        System.out.println("\n📜 Access Log for Roll No: " + rollNumber);
        for (AccessLog log : logEntries) {
            if (log.rollNumber.equals(rollNumber)) {
                System.out.println("🔹 " + log.studentName + " - " + log.action + " at " + log.timestamp);
                found = true;
            }
        }
        if (!found) {
            System.out.println("⚠ No logs found for Roll No: " + rollNumber);
        }
    }

    // ✅ Check if the Last Entry was a Specific Action
    private static boolean isLastEntry(String rollNumber, String expectedAction) {
        for (int i = logEntries.size() - 1; i >= 0; i--) {
            AccessLog log = logEntries.get(i);
            if (log.rollNumber.equals(rollNumber)) {
                return log.action.equals(expectedAction);
            }
        }
        return false;
    }

    public static void logAccess(String username, String role) {
        try {
            LocalDateTime now = LocalDateTime.now();
            String timestamp = now.format(formatter);
            
            try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE, true))) {
                writer.println(timestamp + " | " + username + " | " + role);
            }
        } catch (IOException e) {
            System.out.println("❌ Error logging access: " + e.getMessage());
        }
    }

    public static void viewLogs() {
        try {
            File file = new File(LOG_FILE);
            if (!file.exists()) {
                System.out.println("\n📝 No access logs found.");
                return;
            }

            System.out.println("\n\t\t======================================");
            System.out.println("\n\t\t\t..:: ACCESS LOGS ::..");
            System.out.println("\n\t\t======================================");

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                List<String> logs = new ArrayList<>();
                while ((line = reader.readLine()) != null) {
                    logs.add(line);
                }

                // Display last 50 logs
                int start = Math.max(0, logs.size() - 50);
                for (int i = start; i < logs.size(); i++) {
                    System.out.println("\n\t\t" + logs.get(i));
                }
            }

            System.out.println("\n\t\t======================================");
            System.out.println("\n\t\tPress Enter to continue...");
            new java.util.Scanner(System.in).nextLine();
        } catch (IOException e) {
            System.out.println("❌ Error reading logs: " + e.getMessage());
        }
    }
}
