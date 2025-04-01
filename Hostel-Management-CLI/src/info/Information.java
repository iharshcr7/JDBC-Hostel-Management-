package info;

import java.util.Scanner;
import main.PHHD;

public class Information {
    public Information(Scanner scanner) {
        boolean check = true;
        do {
            System.out.println("\n\t\t======================================");
            System.out.println("\n\t\t\t..::ROOMS OF HOSTEL::..");
            System.out.println("\n\t\t======================================");
            System.out.println("\n\t\t 1. :- Standard Rooms");
            System.out.println("\n\t\t 2. :- Luxury Rooms");
            System.out.println("\n\t\t 3. :- Back To Main Menu");
            System.out.println("\n\t\t======================================");
            System.out.print("\n\t\t\t   CHOICE :- ");

            if (!scanner.hasNextInt()) {
                System.out.println("❌ Invalid input! Enter a number between 1 and 3.");
                scanner.next();
                continue;
            }
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    new StandardRoom(scanner);
                    break;
                case 2:
                    new LuxuryRoom(scanner);
                    break;
                case 3:
                    check = false;
                    break;
                default:
                    System.out.println("❌ Invalid choice! Try again.");
                    break;
            }
        } while (check);
    }
}

class StandardRoom {
    StandardRoom(Scanner scanner) {
        System.out.println("\n\n\t    ====================================================");
        System.out.println("\n\t\t     ..::Standard Rooms for Students::..");
        System.out.println("\n\t    ====================================================");
        System.out.println("\n\t\tRoom Type\tFee");
        System.out.println("\n\t\t4 Sharing\tRs.60,000");
        System.out.println("\n\t\t2 Sharing\tRs.70,000");
        System.out.println("\n\t\t1 Sharing\tRs.1,10,000");
        System.out.println("\n\t\tNo Sharing\tRs.1,30,000");

        // Dynamically fetch available Standard rooms
        System.out.print("\n\t\tAvailable Standard Rooms: ");
        boolean available = false;
        for (int room : PHHD.roomAllocation.keySet()) {
            if (room >= 101 && room <= 199 && PHHD.roomAllocation.get(room).equals("AVAILABLE")) {
                System.out.print(room + " ");
                available = true;
            }
        }
        if (!available) {
            System.out.print("None");
        }
        System.out.println("\n\n\t\tPress Enter to continue...");
        scanner.nextLine();
    }
}

class LuxuryRoom {
    LuxuryRoom(Scanner scanner) {
        System.out.println("\n\n\t    ====================================================");
        System.out.println("\n\t\t     ..::Luxury Rooms for Students::..");
        System.out.println("\n\t    ====================================================");
        System.out.println("\n\t\tRoom Type\tFee");
        System.out.println("\n\t\t4 Sharing\tRs.80,000");
        System.out.println("\n\t\t2 Sharing\tRs.1,20,000");
        System.out.println("\n\t\t1 Sharing\tRs.1,30,000");
        System.out.println("\n\t\tNo Sharing\tRs.1,50,000");

        // Dynamically fetch available Luxury rooms
        System.out.print("\n\t\tAvailable Luxury Rooms: ");
        boolean available = false;
        for (int room : PHHD.roomAllocation.keySet()) {
            if (room >= 201 && room <= 299 && PHHD.roomAllocation.get(room).equals("AVAILABLE")) {
                System.out.print(room + " ");
                available = true;
            }
        }
        if (!available) {
            System.out.print("None");
        }
        System.out.println("\n\n\t\tPress Enter to continue...");
        scanner.nextLine();
    }
}
