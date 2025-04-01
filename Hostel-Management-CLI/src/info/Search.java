package info;

import java.util.*;
import management.StudentManager;
import main.PHHD;

public class Search {
    public static void searchStudent() { 
        System.out.println("\n\t\t======================================");
        System.out.println("\n\t\t\t..:: Search Student ::..");
        System.out.println("\n\t\t======================================");
        System.out.print("\t\tEnter Student Roll Number or Name: ");
        
        String searchQuery = PHHD.scanner.nextLine().trim().toLowerCase();
        
        if (searchQuery.isEmpty()) {
            System.out.println("\n❌ Search query cannot be empty! Try again.");
            return;
        }

        // ✅ Fixed: Updated `StudentManager.getStudentDetails()` call
        String studentDetails = StudentManager.getStudentDetails(searchQuery);
        
        if (studentDetails != null && !studentDetails.isEmpty()) {
            System.out.println("\n\n==== Student Details ====");
            System.out.println(studentDetails);
        } else {
            System.out.println("\n❌ No student records found.");
        }
    }
}
