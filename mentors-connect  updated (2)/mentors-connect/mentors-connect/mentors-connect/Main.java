// import models.*;
// import services.*;
// import java.util.Scanner;

// public class Main {
//     public static void main(String[] args) {
//         Scanner sc = new Scanner(System.in);
//         AuthService authService = new AuthService();

//         while (true) {
//             System.out.println("\n=== Mentors Meet ===");
//             System.out.println("1. Register\n2. Login\n3. Exit");
//             int choice = sc.nextInt(); sc.nextLine();

//             switch (choice) {
//                 case 1:
//                     authService.register(sc);
//                     break;
//                 case 2:
//                     User user = authService.login(sc);
//                     if (user != null) {
//                         if (user instanceof Mentee)
//                             MenteeService.menteeDashboard((Mentee) user, sc);
//                         else if (user instanceof Mentor)
//                             MentorService.mentorDashboard((Mentor) user, sc);
//                     }
//                     break;
//                 case 3:
//                     System.exit(0);
//             }
//         }
//     }
// }
import models.Mentor;
import models.Mentee;
import services.MentorService;
import services.MenteeService;
import utils.FileUtil;

import java.util.*;

public class Main {
    private static final String USER_FILE = "data/users.txt";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Mentor-Mentee Platform ---");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt(); sc.nextLine();

            switch (choice) {
                case 1:
                    register(sc);
                    break;
                case 2:
                    login(sc);
                    break;
                case 3:
                    System.out.println("Thank you for using the platform.");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void register(Scanner sc) {
        System.out.println("\n--- Registration ---");
        System.out.print("Enter username: ");
        String username = sc.nextLine();

        System.out.print("Enter password: ");
        String password = sc.nextLine();

        System.out.print("Are you a mentor or mentee? ");
        String role = sc.nextLine().toLowerCase();

        if (!role.equals("mentor") && !role.equals("mentee")) {
            System.out.println("Invalid role.");
            return;
        }

        // Check if username already exists
        List<String> users = FileUtil.readLines(USER_FILE);
        for (String user : users) {
            String[] parts = user.split("\\|");
            if (parts.length > 1 && parts[1].equals(username)) {
                System.out.println("Username already taken.");
                return;
            }
        }

        String category = "";
        if (role.equals("mentor")) {
            System.out.print("Enter category (Java/DSA/Web Development/Data Science/Python): ");
            category = sc.nextLine();
            FileUtil.appendLine(USER_FILE, String.join("|", role, username, password, category));
        } else {
            FileUtil.appendLine(USER_FILE, String.join("|", role, username, password));
        }

        System.out.println("✅ Registration successful!");
    }

    private static void login(Scanner sc) {
        System.out.println("\n--- Login ---");
        System.out.print("Enter username: ");
        String username = sc.nextLine();

        System.out.print("Enter password: ");
        String password = sc.nextLine();

        List<String> users = FileUtil.readLines(USER_FILE);
        for (String user : users) {
            String[] parts = user.split("\\|");
            if (parts.length >= 3 && parts[1].equals(username) && parts[2].equals(password)) {
                String role = parts[0];

                if (role.equals("mentor")) {
                    String category = parts.length > 3 ? parts[3] : "Unspecified";
                    Mentor mentor = new Mentor(username, password, category);
                    MentorService.mentorDashboard(mentor, sc);
                } else if (role.equals("mentee")) {
                    Mentee mentee = new Mentee(username, password);
                    MenteeService.menteeDashboard(mentee, sc);
                } else {
                    System.out.println("Unknown role in system.");
                }
                return;
            }
        }

        System.out.println("Invalid username or password.");
    }
}
