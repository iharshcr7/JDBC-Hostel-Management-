package management;

import java.io.*;
import java.util.*;
import student.Person;
import exception.InvalidAgeException;

public class Warden extends Person {
    private String hostelAssigned;
    private String contactNumber;
    private String email;
    private static final String WARDEN_FILE = "warden_details.txt";

    // ✅ Constructor with validation
    public Warden(String name, int age, String hostelAssigned, String contactNumber, String email) throws InvalidAgeException {
        super(name, age);
        
        if (hostelAssigned == null || hostelAssigned.trim().isEmpty()) {
            throw new IllegalArgumentException("❌ Hostel name cannot be empty.");
        }
        
        this.hostelAssigned = hostelAssigned;
        this.contactNumber = contactNumber;
        this.email = email;
    }

    // ✅ Getter for assigned hostel
    public String getHostelAssigned() {
        return hostelAssigned;
    }

    // ✅ Setter to update the assigned hostel with validation
    public void setHostelAssigned(String newHostel) {
        if (newHostel == null || newHostel.trim().isEmpty()) {
            throw new IllegalArgumentException("❌ New hostel name cannot be empty.");
        }
        this.hostelAssigned = newHostel;
        System.out.println("✅ Warden reassigned to " + newHostel);
    }

    // ✅ Display warden details
    @Override
    public void displayDetails() {
        System.out.println("\n👤 Warden Name: " + getName() + 
                           "\n📅 Age: " + getAge() + 
                           "\n🏠 Assigned Hostel: " + hostelAssigned);
    }

    // Getters
    public String getContactNumber() { return contactNumber; }
    public String getEmail() { return email; }

    // Save warden details to file
    public void saveToFile() throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(WARDEN_FILE, true))) {
            writer.println(String.format("%s,%d,%s,%s,%s",
                getName(), getAge(), getHostelAssigned(), getContactNumber(), getEmail()));
        }
    }

    // Read all wardens from file
    public static List<Warden> getAllWardens() throws IOException {
        List<Warden> wardens = new ArrayList<>();
        File file = new File(WARDEN_FILE);
        
        if (!file.exists() || file.length() == 0) {
            return wardens;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    try {
                        wardens.add(new Warden(
                            parts[0],                    // name
                            Integer.parseInt(parts[1]),  // age
                            parts[2],                    // assignedHostel
                            parts[3],                    // contactNumber
                            parts[4]                     // email
                        ));
                    } catch (InvalidAgeException e) {
                        System.out.println("❌ Invalid age for warden: " + parts[0]);
                        continue;
                    }
                }
            }
        }
        return wardens;
    }

    // Get warden by hostel
    public static Warden getWardenByHostel(String hostel) throws IOException {
        List<Warden> wardens = getAllWardens();
        for (Warden warden : wardens) {
            if (warden.getHostelAssigned().equals(hostel)) {
                return warden;
            }
        }
        return null;
    }

    // Update warden details
    public static boolean updateWarden(String hostel, Warden newDetails) throws IOException {
        List<Warden> wardens = getAllWardens();
        boolean found = false;

        try (PrintWriter writer = new PrintWriter(new FileWriter(WARDEN_FILE, false))) {
            for (Warden warden : wardens) {
                if (warden.getHostelAssigned().equals(hostel)) {
                    writer.println(String.format("%s,%d,%s,%s,%s",
                        newDetails.getName(),
                        newDetails.getAge(),
                        newDetails.getHostelAssigned(),
                        newDetails.getContactNumber(),
                        newDetails.getEmail()));
                    found = true;
                } else {
                    writer.println(String.format("%s,%d,%s,%s,%s",
                        warden.getName(),
                        warden.getAge(),
                        warden.getHostelAssigned(),
                        warden.getContactNumber(),
                        warden.getEmail()));
                }
            }
        }
        return found;
    }

    // Delete warden
    public static boolean deleteWarden(String hostel) throws IOException {
        List<Warden> wardens = getAllWardens();
        boolean found = false;

        try (PrintWriter writer = new PrintWriter(new FileWriter(WARDEN_FILE, false))) {
            for (Warden warden : wardens) {
                if (!warden.getHostelAssigned().equals(hostel)) {
                    writer.println(String.format("%s,%d,%s,%s,%s",
                        warden.getName(),
                        warden.getAge(),
                        warden.getHostelAssigned(),
                        warden.getContactNumber(),
                        warden.getEmail()));
                } else {
                    found = true;
                }
            }
        }
        return found;
    }
}
