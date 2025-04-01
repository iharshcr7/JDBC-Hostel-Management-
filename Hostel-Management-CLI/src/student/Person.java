package student;

// Person.java (Abstract Class)
import exception.InvalidAgeException; 

public abstract class Person {
    protected String name;
    protected int age;

    public Person(String name, int age) throws InvalidAgeException {
        if (age < 0) {
            throw new InvalidAgeException("Age cannot be negative.");
        }
        this.name = name;
        this.age = age;
    }

    // Getter methods so that Student.java can access these variables
    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public abstract void displayDetails();
}
