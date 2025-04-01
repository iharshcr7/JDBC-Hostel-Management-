package management;

import student.Student;
import exception.DuplicateEntry;
import java.util.*;

public class Hostel {
    private int capacity;
    private List<Student> students;

    public Hostel(int capacity) {
        this.capacity = capacity;
        this.students = new ArrayList<>();
    }

    public void addStudent(Student student) throws DuplicateEntry {
        if (students.size() >= capacity) {
            System.out.println("⛔ Hostel is full! Cannot add more students.");
            return;
        }

        for (Student s : students) {
            if (s.getRollNumber().equals(student.getRollNumber())) {
                throw new DuplicateEntry("❌ Student " + student.getName() + " is already in the hostel.");
            }
        }

        students.add(student);
        System.out.println("✅ " + student.getName() + " added to the hostel!");
    }

    public void showAvailableRooms() {
        System.out.println("🏠 Hostel DHHP");
        System.out.println("Available rooms: " + (capacity - students.size()));
    }

    // 📌 Get Allocated Rooms
    public Set<Integer> getAllocatedRooms() {
        Set<Integer> allocatedRooms = new HashSet<>();
        for (Student student : students) {
            allocatedRooms.add(student.getRoomNumber()); // Assuming Student has a getRoomNumber method
        }
        return allocatedRooms;
    }
}
