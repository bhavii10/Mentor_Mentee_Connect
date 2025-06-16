// package services;

// import models.*;
// import utils.FileUtil;

// import java.util.*;

// public class MenteeService {
//     private static final String APPOINTMENT_FILE = "data/appointments.txt";
//     private static final String USER_FILE = "data/users.txt";

//     public static void menteeDashboard(Mentee mentee, Scanner sc) {
//         while (true) {
//             System.out.println("\n--- Mentee Dashboard ---");
//             System.out.println("1. View Mentors");
//             System.out.println("2. Book Appointment");
//             System.out.println("3. Logout");
//             int choice = sc.nextInt(); sc.nextLine();

//             switch (choice) {
//                 case 1:
//                     viewMentors();
//                     break;
//                 case 2:
//                     bookAppointment(mentee, sc);
//                     break;
//                 case 3:
//                     return;
//                 default:
//                     System.out.println("Invalid choice. Try again.");
//             }
//         }
//     }

//     private static void viewMentors() {
//         List<String> users = FileUtil.readLines(USER_FILE);
//         System.out.println("\n--- List of Mentors ---");
//         for (String user : users) {
//             if (user.startsWith("mentor|")) {
//                 String[] parts = user.split("\\|");
//                 if (parts.length >= 2) {
//                     System.out.println("- " + parts[1]);
//                 }
//             }
//         }
//     }

//     private static void bookAppointment(Mentee mentee, Scanner sc) {
//         List<String> users = FileUtil.readLines(USER_FILE);
//         List<String> mentorUsernames = new ArrayList<>();

//         // Get all mentor usernames
//         for (String user : users) {
//             if (user.startsWith("mentor|")) {
//                 String[] parts = user.split("\\|");
//                 if (parts.length >= 2) {
//                     mentorUsernames.add(parts[1]);
//                 }
//             }
//         }

//         if (mentorUsernames.isEmpty()) {
//             System.out.println("No mentors available at the moment.");
//             return;
//         }

//         // Show mentor list
//         System.out.println("\n--- Available Mentors ---");
//         for (int i = 0; i < mentorUsernames.size(); i++) {
//             System.out.println((i + 1) + ". " + mentorUsernames.get(i));
//         }

//         System.out.print("Choose mentor (number): ");
//         int mentorIndex = sc.nextInt(); sc.nextLine();
//         if (mentorIndex < 1 || mentorIndex > mentorUsernames.size()) {
//             System.out.println("Invalid selection.");
//             return;
//         }

//         String chosenMentor = mentorUsernames.get(mentorIndex - 1);

//         System.out.print("Enter date (yyyy-mm-dd): ");
//         String date = sc.nextLine();
//         System.out.print("Enter time (HH:mm): ");
//         String time = sc.nextLine();

//         // Read and filter existing appointments
//         List<String> lines = FileUtil.readLines(APPOINTMENT_FILE);
//         List<Appointment> mentorAppointments = new ArrayList<>();

//         for (String line : lines) {
//             Appointment a = Appointment.fromString(line);
//             if (a != null && a.getMentorUsername().equals(chosenMentor)) {
//                 mentorAppointments.add(a);
//             }
//         }

//         // Sort appointments by date+time
//         Collections.sort(mentorAppointments);

//         Appointment newAppointment = new Appointment(chosenMentor, mentee.getUsername(), date, time, "pending");

//         // Check for clash using binary search
//         int index = Collections.binarySearch(mentorAppointments, newAppointment);
//         if (index >= 0) {
//             System.out.println("⚠️ This mentor is already booked at that time. Please choose a different slot.");
//         } else {
//             FileUtil.appendLine(APPOINTMENT_FILE, newAppointment.toString());
//             System.out.println("✅ Appointment requested successfully!");
//         }
//     }
// }



package services;

import models.*;
import utils.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MenteeService {
    private static final String APPOINTMENT_FILE = "data/appointments.txt";
    private static final String USER_FILE = "data/users.txt";
private static final String FEEDBACK_FILE = "data/feedbacks.txt"; // ✅ new file

    public static void menteeDashboard(Mentee mentee, Scanner sc) {
        while (true) {
            System.out.println("\n--- Mentee Dashboard ---");
            System.out.println("1. View Mentors");
            System.out.println("2. Book Appointment");
            System.out.println("3. View Appointments");
            System.out.println("4. View Notifications");


            System.out.println("5. Update Appointment");
            System.out.println("6. Cancel Appointment");
            
            System.out.println("7. Logout");
            System.out.println("8. Give Feedback");
            int choice = sc.nextInt(); sc.nextLine();

            switch (choice) {
                case 1:
                    viewMentors();
                    break;
                case 2:
                    bookAppointment(mentee, sc);
                    break;
                case 3:
                    viewAppointments(mentee);
                    break;


                case 4:
    viewNotifications(mentee.getUsername());

    break;  
                case 5:
                    updateAppointment(mentee, sc);
                    break;
                case 6:
                    cancelAppointment(mentee, sc);
                    break;
                case 7:
                    return;
                     case 8:
                    giveFeedback(mentee, sc); // ✅ new method
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void viewMentors() {
        List<String> allCategories = Arrays.asList("Java", "DSA", "Web Development", "Data Science", "Python");
        TreeMap<String, List<String>> categoryMentorMap = new TreeMap<>();
        for (String category : allCategories) {
            categoryMentorMap.put(category, new ArrayList<>());
        }

        List<String> users = FileUtil.readLines(USER_FILE);
        for (String user : users) {
            if (user.startsWith("mentor|")) {
                String[] parts = user.split("\\|");
                if (parts.length >= 4) {
                    String category = parts[3];
                    String username = parts[1];
                    categoryMentorMap.computeIfAbsent(category, k -> new ArrayList<>()).add(username);
                }
            }
        }

        System.out.println("\nAvailable Categories:");
        List<String> categoryList = new ArrayList<>(categoryMentorMap.keySet());
        for (int i = 0; i < categoryList.size(); i++) {
            System.out.println((i + 1) + ". " + categoryList.get(i));
        }

        Scanner sc = new Scanner(System.in);
        System.out.print("Choose a category (number): ");
        int choice = sc.nextInt(); sc.nextLine();

        if (choice < 1 || choice > categoryList.size()) {
            System.out.println("Invalid category choice.");
            return;
        }

        String selectedCategory = categoryList.get(choice - 1);
        List<String> mentors = categoryMentorMap.getOrDefault(selectedCategory, new ArrayList<>());

        System.out.println("\nMentors in category '" + selectedCategory + "':");
        if (mentors.isEmpty()) {
            System.out.println("No mentor found.");
        } else {
            for (String mentor : mentors) {
                System.out.println("- " + mentor);
            }
        }
    }

    private static void bookAppointment(Mentee mentee, Scanner sc) {
        List<String> allCategories = Arrays.asList("Java", "DSA", "Web Development", "Data Science", "Python");
        TreeMap<String, List<String>> categoryMentorMap = new TreeMap<>();
        for (String category : allCategories) {
            categoryMentorMap.put(category, new ArrayList<>());
        }

        List<String> users = FileUtil.readLines(USER_FILE);
        for (String user : users) {
            if (user.startsWith("mentor|")) {
                String[] parts = user.split("\\|");
                if (parts.length >= 4) {
                    String category = parts[3];
                    String username = parts[1];
                    categoryMentorMap.computeIfAbsent(category, k -> new ArrayList<>()).add(username);
                }
            }
        }

        System.out.println("\n--- Available Categories ---");
        List<String> categoryList = new ArrayList<>(categoryMentorMap.keySet());
        for (int i = 0; i < categoryList.size(); i++) {
            System.out.println((i + 1) + ". " + categoryList.get(i));
        }

        System.out.print("Choose a category (number): ");
        int categoryChoice = sc.nextInt(); sc.nextLine();
        if (categoryChoice < 1 || categoryChoice > categoryList.size()) {
            System.out.println("Invalid category choice.");
            return;
        }

        String selectedCategory = categoryList.get(categoryChoice - 1);
        List<String> mentorsInCategory = categoryMentorMap.getOrDefault(selectedCategory, new ArrayList<>());

        if (mentorsInCategory.isEmpty()) {
            System.out.println("No mentors available in this category.");
            return;
        }

        System.out.println("\n--- Available Mentors in " + selectedCategory + " ---");
        for (int i = 0; i < mentorsInCategory.size(); i++) {
            System.out.println((i + 1) + ". " + mentorsInCategory.get(i));
        }

        System.out.print("Choose mentor (number): ");
        int mentorChoice = sc.nextInt(); sc.nextLine();
        if (mentorChoice < 1 || mentorChoice > mentorsInCategory.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        String chosenMentor = mentorsInCategory.get(mentorChoice - 1);

        System.out.print("Enter date (yyyy-MM-dd): ");
        String date = sc.nextLine();
        System.out.print("Enter time (hh:mm AM/PM): ");
        String time12hr = sc.nextLine();

        String time24hr;
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("hh:mm a");
            SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm");
            time24hr = outputFormat.format(inputFormat.parse(time12hr));
        } catch (Exception e) {
            System.out.println("Invalid time format.");
            return;
        }

        List<String> lines = FileUtil.readLines(APPOINTMENT_FILE);
        for (String line : lines) {
            Appointment a = Appointment.fromString(line);
            if (a != null && a.getMentorUsername().equals(chosenMentor) &&
                a.getDate().equals(date) && a.getTime().equals(time24hr)) {
                System.out.println("⚠️ Already booked. Choose another slot.");
                return;
            }
        }

        Appointment newAppointment = new Appointment(chosenMentor, mentee.getUsername(), date, time24hr, "pending");
        FileUtil.appendLine(APPOINTMENT_FILE, newAppointment.toString());
        System.out.println("✅ Appointment requested!");
    }

    private static void viewAppointments(Mentee mentee) {
        List<String> lines = FileUtil.readLines(APPOINTMENT_FILE);
        List<Appointment> upcomingAppointments = new ArrayList<>();

        String now = getCurrentDateTime();

        for (String line : lines) {
            Appointment a = Appointment.fromString(line);
            if (a != null && a.getMenteeUsername().equals(mentee.getUsername()) && a.getStatus().equals("accepted")) {
                String appointmentDateTime = a.getDate() + " " + a.getTime();
                if (appointmentDateTime.compareTo(now) > 0) {
                    upcomingAppointments.add(a);
                }
            }
        }

        if (upcomingAppointments.isEmpty()) {
            System.out.println("No upcoming appointments found.");
        } else {
            System.out.println("\n--- Listed Appointments ---");
            SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm");
            SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm a");

            for (Appointment appointment : upcomingAppointments) {
                String formattedTime;
                try {
                    formattedTime = outputFormat.format(inputFormat.parse(appointment.getTime()));
                } catch (ParseException e) {
                    formattedTime = appointment.getTime();
                }

                System.out.println("Mentor: " + appointment.getMentorUsername() +
                        ", Date: " + appointment.getDate() +
                        ", Time: " + formattedTime);
            }
        }
    }

    private static void updateAppointment(Mentee mentee, Scanner sc) {
        List<String> lines = FileUtil.readLines(APPOINTMENT_FILE);
        List<Appointment> appointments = new ArrayList<>();

        for (String line : lines) {
            Appointment a = Appointment.fromString(line);
            if (a != null && a.getMenteeUsername().equals(mentee.getUsername())) {
                appointments.add(a);
            }
        }

        if (appointments.isEmpty()) {
            System.out.println("No appointments found to update.");
            return;
        }

        for (int i = 0; i < appointments.size(); i++) {
            Appointment a = appointments.get(i);
            System.out.println((i + 1) + ". " + a.getMentorUsername() + " on " + a.getDate() + " at " + a.getTime());
        }

        System.out.print("Select appointment to update (number): ");
        int choice = sc.nextInt(); sc.nextLine();
        if (choice < 1 || choice > appointments.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        Appointment toUpdate = appointments.get(choice - 1);

        System.out.print("Enter new date (yyyy-MM-dd): ");
        String newDate = sc.nextLine();

        System.out.print("Enter new time (hh:mm AM/PM): ");
        String newTime12hr = sc.nextLine();

        String newTime24hr;
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("hh:mm a");
            SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm");
            newTime24hr = outputFormat.format(inputFormat.parse(newTime12hr));
        } catch (Exception e) {
            System.out.println("Invalid time format.");
            return;
        }

        toUpdate.setDate(newDate);
        toUpdate.setTime(newTime24hr);
        toUpdate.setStatus("pending");

        List<String> updatedLines = new ArrayList<>();
        for (Appointment a : appointments) {
            updatedLines.add(a.toString());
        }

        FileUtil.writeLines(APPOINTMENT_FILE, updatedLines);
        System.out.println("✅ Appointment updated successfully and sent for mentor approval.");
    }

    private static void cancelAppointment(Mentee mentee, Scanner sc) {
        List<String> lines = FileUtil.readLines(APPOINTMENT_FILE);
        List<Appointment> appointments = new ArrayList<>();

        for (String line : lines) {
            Appointment a = Appointment.fromString(line);
            if (a != null && a.getMenteeUsername().equals(mentee.getUsername())) {
                appointments.add(a);
            }
        }

        if (appointments.isEmpty()) {
            System.out.println("No appointments to cancel.");
            return;
        }

        for (int i = 0; i < appointments.size(); i++) {
            Appointment a = appointments.get(i);
            System.out.println((i + 1) + ". " + a.getMentorUsername() + " on " + a.getDate() + " at " + a.getTime());
        }

        System.out.print("Select appointment to cancel (number): ");
        int choice = sc.nextInt(); sc.nextLine();
        if (choice < 1 || choice > appointments.size()) {
            System.out.println("Invalid choice.");
            return;
        }

        appointments.remove(choice - 1);
        List<String> updatedLines = new ArrayList<>();
        for (Appointment a : appointments) {
            updatedLines.add(a.toString());
        }

        FileUtil.writeLines(APPOINTMENT_FILE, updatedLines);
        System.out.println("❌ Appointment cancelled successfully.");
    }

    // private static String getCurrentDateTime() {
    //     return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
    // }



    private static void giveFeedback(Mentee mentee, Scanner sc) {
        List<String> lines = FileUtil.readLines(APPOINTMENT_FILE);
        List<Appointment> appointments = new ArrayList<>();

        for (String line : lines) {
            Appointment a = Appointment.fromString(line);
            if (a != null && a.getMenteeUsername().equals(mentee.getUsername()) && a.getStatus().equals("accepted")) {
                appointments.add(a);
            }
        }

        if (appointments.isEmpty()) {
            System.out.println("No completed appointments to give feedback.");
            return;
        }

        System.out.println("\n--- Your Appointments ---");
        for (int i = 0; i < appointments.size(); i++) {
            Appointment a = appointments.get(i);
            System.out.println((i + 1) + ". " + a.getMentorUsername() + " on " + a.getDate() + " at " + a.getTime());
        }

        System.out.print("Select appointment to give feedback for (number): ");
        int choice = sc.nextInt(); sc.nextLine();

        if (choice < 1 || choice > appointments.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        Appointment selected = appointments.get(choice - 1);

        // Check for existing feedback
        List<String> feedbackLines = FileUtil.readLines(FEEDBACK_FILE);
        for (String fLine : feedbackLines) {
            String[] parts = fLine.split("\\|");
            if (parts.length >= 5 &&
                parts[0].equals(mentee.getUsername()) &&
                parts[1].equals(selected.getMentorUsername()) &&
                parts[2].equals(selected.getDate()) &&
                parts[3].equals(selected.getTime())) {
                System.out.println("⚠️ Feedback already given for this appointment.");
                return;
            }
        }

        System.out.print("Enter your feedback: ");
        String feedback = sc.nextLine();

        String entry = mentee.getUsername() + "|" +
                       selected.getMentorUsername() + "|" +
                       selected.getDate() + "|" +
                       selected.getTime() + "|" +
                       feedback;

        FileUtil.appendLine(FEEDBACK_FILE, entry);
        System.out.println("✅ Feedback submitted. Thank you!");
    }

    // getCurrentDateTime() method (same as previous version)
    private static String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(new Date());
    }



    public static void viewNotifications(String menteeUsername) {
    String notificationFile = "data/notifications_" + menteeUsername + ".txt";

    File file = new File(notificationFile);
    if (!file.exists()) {
        System.out.println("📭 No notifications.");
        return;
    }

    List<String> notifications = FileUtil.readLines(notificationFile);
    if (notifications.isEmpty()) {
        System.out.println("📭 No new notifications.");
        return;
    }

    System.out.println("\n🔔 Your Notifications:");
    for (String note : notifications) {
        System.out.println(" - " + note);
    }

    // Optional: Clear after viewing
    FileUtil.writeLines(notificationFile, new ArrayList<>());
}




}

