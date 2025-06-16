// package services;

// import java.util.*;
// // import java.util.stream.Collectors;

// import models.*;
// import utils.FileUtil;


// public class AuthService {
//     private static final String USER_FILE = "data/users.txt";

//     public void register(Scanner sc) {
//         System.out.println("Register as Mentor or Mentee:");
//         String role = sc.nextLine().toLowerCase();

//         System.out.print("Enter username: ");
//         String username = sc.nextLine();
//         System.out.print("Enter password: ");
//         String password = sc.nextLine();

//         String line = role + "|" + username + "|" + password;
//         FileUtil.appendLine(USER_FILE, line);
//         System.out.println("Registration successful!");
//     }

//     public User login(Scanner sc) {
//         System.out.print("Enter username: ");
//         String username = sc.nextLine();
//         System.out.print("Enter password: ");
//         String password = sc.nextLine();

//         List<String> lines = FileUtil.readLines(USER_FILE);
//         for (String line : lines) {
//             String[] parts = line.split("\\|");
//             if (parts[1].equals(username) && parts[2].equals(password)) {
//                 if (parts[0].equals("mentor")) return new Mentor(username, password);
//                 if (parts[0].equals("mentee")) return new Mentee(username, password);
//             }
//         }
//         System.out.println("Invalid credentials.");
//         return null;
//     }
// }



package services;

import java.util.*;
import models.*;
import utils.FileUtil;
import java.util.List;

public class AuthService {
    private static final String USER_FILE = "data/users.txt";
    private static final String USER__FILE = "data/users.txt";  // whatever file you actually use

    private static final TreeMap<String, List<String>> categoryMentorMap = new TreeMap<>();

    public void register(Scanner sc) {
        System.out.println("Register as Mentor or Mentee:");
        String role = sc.nextLine().toLowerCase();

        System.out.print("Enter username: ");
        String username = sc.nextLine();
        System.out.print("Enter password: ");
        String password = sc.nextLine();

        String line;

        if (role.equals("mentor")) {
            System.out.println("Select a category:");
            String[] categories = {"DSA", "Data Science", "Java", "Python","Web Development"};
            for (int i = 0; i < categories.length; i++) {
                System.out.println((i + 1) + ". " + categories[i]);
            }

            int choice = sc.nextInt();
            sc.nextLine(); // consume leftover newline

            if (choice < 1 || choice > categories.length) {
                System.out.println("Invalid category.");
                return;
            }

            String category = categories[choice - 1];
            line = role + "|" + username + "|" + password + "|" + category;

            // Update TreeMap
            categoryMentorMap.putIfAbsent(category, new ArrayList<>());
            categoryMentorMap.get(category).add(username);

        } else {
            line = role + "|" + username + "|" + password;
        }

        FileUtil.appendLine(USER_FILE, line);
        System.out.println("Registration successful!");
    }

    public User login(Scanner sc) {
        System.out.print("Enter username: ");
        String username = sc.nextLine();
        System.out.print("Enter password: ");
        String password = sc.nextLine();

        List<String> lines = FileUtil.readLines(USER_FILE);
        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts[1].equals(username) && parts[2].equals(password)) {
                if (parts[0].equals("mentor")) {
                    String category = parts.length > 3 ? parts[3] : "";
                    return new Mentor(username, password, category);
                } else if (parts[0].equals("mentee")) {
                    return new Mentee(username, password);
                }
            }
        }
        System.out.println("Invalid credentials.");
        return null;
    }

    // Optional: Display all mentors in a category
    public void showMentorsByCategory() {
        for (Map.Entry<String, List<String>> entry : categoryMentorMap.entrySet()) {
            System.out.println("Category: " + entry.getKey());
            for (String mentor : entry.getValue()) {
                System.out.println(" - " + mentor);
            }
        }
    }

  public static boolean isUserRegistered(String username) {
        List<String> lines = FileUtil.readLines(USER__FILE);
        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length >= 2 
                && parts[0].equals("mentee") 
                && parts[1].equals(username)) {
                return true;
            }
        }
        return false;
    }

}




