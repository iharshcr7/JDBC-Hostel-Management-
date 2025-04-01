package management;

import java.util.*;

public class Building {
    private String name;
    private int floors;
    private int totalRooms;
    private String wardenName;
    
    private static List<Building> buildings = new ArrayList<>();

    public Building(String name, int floors, int totalRooms, String wardenName) {
        if (name == null || name.isEmpty() || floors <= 0 || totalRooms <= 0 || wardenName == null || wardenName.isEmpty()) {
            throw new IllegalArgumentException("❌ Invalid building details provided.");
        }

        this.name = name;
        this.floors = floors;
        this.totalRooms = totalRooms;
        this.wardenName = wardenName;
        buildings.add(this);
    }
    
    public static void displayBuildings() {
        if (buildings.isEmpty()) {
            System.out.println("❌ No buildings available.");
            return;
        }

        System.out.println("\n🏢 Hostel Buildings:");
        for (Building b : buildings) {
            System.out.println("➡ " + b.name + " | Floors: " + b.floors + " | Rooms: " + b.totalRooms + " | Warden: " + b.wardenName);
        }
    }
    
    public String getName() {
        return name;
    }
    
    public int getFloors() {
        return floors;
    }
    
    public int getTotalRooms() {
        return totalRooms;
    }
    
    public String getWardenName() {
        return wardenName;
    }
}
