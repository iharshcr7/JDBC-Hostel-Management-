package management;

import java.util.HashMap;
import java.util.Map;

public class Cafeteria {
    private static final HashMap<String, Integer> mealPlans = new HashMap<>();
    private static final HashMap<String, String> studentMealSubscriptions = new HashMap<>();
    private static final HashMap<String, Integer> mealPayments = new HashMap<>(); // (Student → Amount Paid)

    static {
        // Initialize Meal Plans
        mealPlans.put("Standard", 3000);  // ₹3000 per month
        mealPlans.put("Premium", 5000);   // ₹5000 per month
        mealPlans.put("Deluxe", 7000);    // ₹7000 per month
    }

    // 📌 Display Meal Plans
    public static void displayMealPlans() {
        if (mealPlans.isEmpty()) {
            System.out.println("❌ No meal plans available.");
            return;
        }

        System.out.println("\n🍽️ Available Meal Plans:");
        for (Map.Entry<String, Integer> entry : mealPlans.entrySet()) {
            System.out.println("🔹 " + entry.getKey() + " Plan - ₹" + entry.getValue() + "/month");
        }
    }

    // 📌 Subscribe Student to a Meal Plan
    public static void subscribeStudent(String studentName, String plan, int amountPaid) {
        if (studentName == null || studentName.isEmpty() || plan == null || plan.isEmpty()) {
            System.out.println("❌ Invalid student name or meal plan!");
            return;
        }
        if (!mealPlans.containsKey(plan)) {
            System.out.println("❌ Invalid meal plan selection!");
            return;
        }

        int planCost = mealPlans.get(plan);
        if (amountPaid < planCost) {
            System.out.println("⚠ Insufficient payment! " + studentName + " must pay ₹" + planCost);
            return;
        }

        // If student already has a plan, update it
        if (studentMealSubscriptions.containsKey(studentName)) {
            System.out.println("🔄 " + studentName + " changed from " + studentMealSubscriptions.get(studentName) + " to " + plan);
        } else {
            System.out.println("✅ " + studentName + " subscribed to the " + plan + " Meal Plan!");
        }

        studentMealSubscriptions.put(studentName, plan);
        mealPayments.put(studentName, amountPaid);
    }

    // 📌 View Student Meal Subscription
    public static void viewStudentSubscription(String studentName) {
        if (studentMealSubscriptions.containsKey(studentName)) {
            System.out.println("📌 " + studentName + " is subscribed to the " + studentMealSubscriptions.get(studentName) + " Meal Plan.");
        } else {
            System.out.println("⚠ " + studentName + " has not subscribed to any meal plan.");
        }
    }

    // 📌 Unsubscribe Student from Meal Plan
    public static void unsubscribeStudent(String studentName) {
        if (studentMealSubscriptions.remove(studentName) != null) {
            mealPayments.remove(studentName);
            System.out.println("✅ " + studentName + " has been unsubscribed from the meal plan.");
        } else {
            System.out.println("⚠ " + studentName + " was not subscribed to any meal plan.");
        }
    }

    // 📌 View All Subscribed Students
    public static void viewAllSubscriptions() {
        if (studentMealSubscriptions.isEmpty()) {
            System.out.println("📌 No students are subscribed to meal plans.");
            return;
        }
        System.out.println("\n🍽️ Meal Plan Subscriptions:");
        for (Map.Entry<String, String> entry : studentMealSubscriptions.entrySet()) {
            System.out.println("🔹 " + entry.getKey() + " → " + entry.getValue());
        }
    }
}
