// package services;

// import models.*;
// import utils.FileUtil;
// import java.util.*;

// public class MentorService {
//     private static final String APPOINTMENT_FILE = "data/appointments.txt";
//     private static final String FEEDBACK_FILE = "data/feedbacks.txt";
//    String waitlistFile = "data/waitlist_" + mentorUsername + ".txt";





//     // CRUD operations for appointments
//     // Create, Read, Update, Delete

//     public static void mentorDashboard(Mentor mentor, Scanner sc) {
//         while (true) {
//             System.out.println("\n--- Mentor Dashboard ---");
//             System.out.println("1. View Appointments");
//             System.out.println("2. Add Appointment");
//             System.out.println("3. View Feedbacks");  
//             System.out.println("4. Logout");
//             int choice = sc.nextInt(); sc.nextLine();

//             switch (choice) {
//                 case 1:
//                     viewAppointments(mentor, sc);
//                     break;

//                 case 2:
//                     addAppointment(mentor, sc);
//                     break;

//                        case 3:
//         viewFeedbacks();  // NEW
//         break;

//                 case 4:
//                     return;
//             }
//         }
//     }

//     // View appointments (Read)
//     private static void viewAppointments(Mentor mentor, Scanner sc) {
//         List<String> all = FileUtil.readLines(APPOINTMENT_FILE);
//         List<String> updated = new ArrayList<>();

//         for (String line : all) {
//             Appointment a = Appointment.fromString(line);

//             if (a == null) {
//                 // Invalid line format, skip it
//                 updated.add(line); // Or skip it entirely if you want
//                 continue;
//             }

//             if (a.getMentorUsername().equals(mentor.getUsername())) {
//                 System.out.println(a.getMenteeUsername() + " on " + a.getDate() + " at " + a.getTime() + " [" + a.getStatus() + "]");
//                 System.out.println("1. Accept  2. Reject  3. Modify  4. Delete  5. Skip");
//                 int opt = sc.nextInt(); sc.nextLine();
//                 if (opt == 1) a.setStatus("accepted");
//                 else if (opt == 2) a.setStatus("rejected");
//                 else if (opt == 3) {
//                     System.out.print("New date: ");
//                     a.setDate(sc.nextLine());

//                     System.out.print("New time: ");
//                     a.setTime(sc.nextLine());

//                     a.setStatus("modified");
//                 } else if (opt == 4) {
//                     // Delete appointment
//                     deleteAppointment(a);
//                     continue; // Skip further processing of this appointment
//                 }
//             }

//             updated.add(a.toString());
//         }

//         FileUtil.writeLines(APPOINTMENT_FILE, updated);
//     }

//     // Add a new appointment (Create)
//     private static void addAppointment(Mentor mentor, Scanner sc) {
//         System.out.print("Enter mentee username: ");
//         String menteeUsername = sc.nextLine();
//         System.out.print("Enter appointment date (yyyy-mm-dd): ");
//         String date = sc.nextLine();
//         System.out.print("Enter appointment time (hh:mm): ");
//         String time = sc.nextLine();

//         Appointment newAppointment = new Appointment(menteeUsername, mentor.getUsername(), date, time, "pending");

//         FileUtil.appendLine(APPOINTMENT_FILE, newAppointment.toString());
//         System.out.println("Appointment added successfully!");


//         // Inside addAppointment() method:
// long count = FileUtil.readLines(APPOINTMENT_FILE).stream()
//     .filter(line -> line.contains(mentor.getUsername()))
//     .count();

// if (count >= 5) { // max 5 appointments
//     addToWaitlist(menteeUsername, sc, mentor.getUsername());
//     return;
// }

//     }
// // Delete an appointment (Delete)
// private static void deleteAppointment(Appointment appointment) {
//     List<String> all = FileUtil.readLines(APPOINTMENT_FILE);
//     List<String> updated = new ArrayList<>();

//     for (String line : all) {
//         Appointment a = Appointment.fromString(line);

//         // Add all appointments except the one that is to be deleted
//         if (a != null && !a.equals(appointment)) {
//             updated.add(a.toString());
//         }
//     }

//     FileUtil.writeLines(APPOINTMENT_FILE, updated);
//     System.out.println("Appointment deleted successfully!");

//     // ✅ Serve the waitlist using mentorUsername from appointment object
//   serveWaitlistIfAny(appointment.getMentorUsername());

// }





//     // View feedbacks submitted by mentees
// private static void viewFeedbacks() {
//     List<String> feedbacks = FileUtil.readLines(FEEDBACK_FILE);

//     if (feedbacks.isEmpty()) {
//         System.out.println("No feedbacks available.");
//         return;
//     }

//     System.out.println("\n--- Feedbacks Submitted by Mentees ---");
//     int count = 1;
//     for (String feedback : feedbacks) {
//         System.out.println(count++ + ". " + feedback);
//     }
// }





// //ADD TO WAITLIST 
// private static void addToWaitlist(String menteeUsername, Scanner sc, String mentorUsername) {
//     System.out.print("Is this a VIP/urgent request? (yes/no): ");
//     String vipInput = sc.nextLine().trim().toLowerCase();
//     int priority = vipInput.equals("yes") ? 1 : 0;

//     WaitlistEntry entry = new WaitlistEntry(menteeUsername, priority);
//     FileUtil.appendLine("data/waitlist_" + mentorUsername + ".txt", entry.toString());

//     System.out.println("Mentor is currently full. You've been added to the waitlist.");
// }


// // Serve waitlist (PriorityQueue)
// private static void serveWaitlistIfAny(String mentorUsername) {
//     String waitlistFile = "data/waitlist_" + mentorUsername + ".txt"; // Waitlist file ka path banayein
//     List<String> lines = FileUtil.readLines(waitlistFile);
//     PriorityQueue<WaitlistEntry> pq = new PriorityQueue<>();

//     for (String line : lines) {
//         pq.add(WaitlistEntry.fromString(line)); // Waitlist ke entries ko queue mein daalein
//     }

//     if (!pq.isEmpty()) {
//         WaitlistEntry next = pq.poll(); // Queue se next entry ko nikaalein
//         System.out.println("✅ Notify mentee: " + next.getMenteeUsername() + " - slot is now available.");

//         List<String> updated = new ArrayList<>();
//         while (!pq.isEmpty()) {
//             updated.add(pq.poll().toString()); // Update karte hue waitlist file mein daalein
//         }

//         FileUtil.writeLines(waitlistFile, updated);
//     }
// }
// }





package services;

import models.*;
import utils.FileUtil;
import java.util.*;
import java.io.File;  // Add this line
import java.io.IOException;  // Add this line to handle IOExceptions
import services.NotificationService; 

public class MentorService {
    private static final String APPOINTMENT_FILE = "data/appointments.txt";
    private static final String FEEDBACK_FILE = "data/feedbacks.txt";

    // CRUD operations for appointments
    // Create, Read, Update, Delete

    public static void mentorDashboard(Mentor mentor, Scanner sc) {
        while (true) {
            System.out.println("\n--- Mentor Dashboard ---");
            System.out.println("1. View Appointments");
            System.out.println("2. Add Appointment");
            System.out.println("3. View Feedbacks");  
            System.out.println("4. Logout");
            int choice = sc.nextInt(); sc.nextLine();

            switch (choice) {
                case 1:
                    viewAppointments(mentor, sc);
                    break;

                case 2:
                    addAppointment(mentor, sc);
                    break;

                case 3:
                    viewFeedbacks();  // NEW
                    break;

                case 4:
                    return;
            }
        }
    }

    // View appointments (Read)
    private static void viewAppointments(Mentor mentor, Scanner sc) {
        List<String> all = FileUtil.readLines(APPOINTMENT_FILE);
        List<String> updated = new ArrayList<>();

        for (String line : all) {
            Appointment a = Appointment.fromString(line);

            if (a == null) {
                // Invalid line format, skip it
                updated.add(line); // Or skip it entirely if you want
                continue;
            }

            if (a.getMentorUsername().equals(mentor.getUsername())) {
                System.out.println(a.getMenteeUsername() + " on " + a.getDate() + " at " + a.getTime() + " [" + a.getStatus() + "]");
                System.out.println("1. Accept  2. Reject  3. Modify  4. Delete  5. Skip");
                int opt = sc.nextInt(); sc.nextLine();
                if (opt == 1) a.setStatus("accepted");
                else if (opt == 2) a.setStatus("rejected");
                else if (opt == 3) {
                    System.out.print("New date: ");
                    a.setDate(sc.nextLine());

                    System.out.print("New time: ");
                    a.setTime(sc.nextLine());

                    a.setStatus("modified");
                } else if (opt == 4) {
                    // Delete appointment
                    deleteAppointment(a);
                    continue; // Skip further processing of this appointment
                }
            }

            updated.add(a.toString());
        }

        FileUtil.writeLines(APPOINTMENT_FILE, updated);
    }

    // Add a new appointment (Create)
  private static void addAppointment(Mentor mentor, Scanner sc) {
    System.out.print("Enter mentee username: ");
    String menteeUsername = sc.nextLine();

    // Check if mentee is registered
    if (!AuthService.isUserRegistered(menteeUsername)) {
        System.out.println("⚠️ Mentee '" + menteeUsername + "' is not registered. Please ask them to sign up first.");
        return;
    }

    System.out.print("Enter appointment date (yyyy-mm-dd): ");
    String date = sc.nextLine();

    System.out.print("Enter appointment time (hh:mm): ");
    String time = sc.nextLine();

    // Check if mentor already has 5 appointments on this date
    int count = countAppointmentsForMentorOnDate(mentor.getUsername(), date);

    if (count >= 5) {
        System.out.println("❗Mentor already has 5 appointments on this date.");
        // Add to waitlist
        addToWaitlist(menteeUsername, sc, mentor.getUsername());
        return;
    }

    // Proceed to booking
    Appointment newAppointment = new Appointment(menteeUsername, mentor.getUsername(), date, time, "pending");
    FileUtil.appendLine(APPOINTMENT_FILE, newAppointment.toString());
    System.out.println("✅ Appointment added successfully!");
}



    // Delete an appointment (Delete)
    private static void deleteAppointment(Appointment appointment) {
        List<String> all = FileUtil.readLines(APPOINTMENT_FILE);
        List<String> updated = new ArrayList<>();

        for (String line : all) {
            Appointment a = Appointment.fromString(line);

            // Add all appointments except the one that is to be deleted
            if (a != null && !a.equals(appointment)) {
                updated.add(a.toString());
            }
        }

        FileUtil.writeLines(APPOINTMENT_FILE, updated);
        System.out.println("Appointment deleted successfully!");

        // ✅ Serve the waitlist using mentorUsername from appointment object
        serveWaitlistIfAny(appointment.getMentorUsername());
    }

    // View feedbacks submitted by mentees
    private static void viewFeedbacks() {
        List<String> feedbacks = FileUtil.readLines(FEEDBACK_FILE);

        if (feedbacks.isEmpty()) {
            System.out.println("No feedbacks available.");
            return;
        }

        System.out.println("\n--- Feedbacks Submitted by Mentees ---");
        int count = 1;
        for (String feedback : feedbacks) {
            System.out.println(count++ + ". " + feedback);
        }
    }

    // ADD TO WAITLIST
    private static void addToWaitlist(String menteeUsername, Scanner sc, String mentorUsername) {
    System.out.print("Enter preferred date (e.g., 2025-05-15): ");
    String date = sc.nextLine().trim();

    System.out.print("Enter preferred time (e.g., 4:00 PM): ");
    String time = sc.nextLine().trim();

    System.out.print("Is this a VIP/urgent request? (yes/no): ");
    String vipInput = sc.nextLine().trim().toLowerCase();
    int priority = vipInput.equals("yes") ? 1 : 0;

    WaitlistEntry entry = new WaitlistEntry(menteeUsername, date, time, priority);
    FileUtil.appendLine("data/waitlist_" + mentorUsername + ".txt", entry.toString());

    System.out.println("Mentor is currently full. You've been added to the waitlist.");
}


    // Serve waitlist (PriorityQueue)
  private static void serveWaitlistIfAny(String mentorUsername) {
    String waitlistFile = "data/waitlist_" + mentorUsername + ".txt";
    File waitlist = new File(waitlistFile);

    if (!waitlist.exists()) {
        System.out.println("📂 No waitlist for this mentor.");
        return;
    }

    List<String> lines = FileUtil.readLines(waitlistFile);
    PriorityQueue<WaitlistEntry> pq = new PriorityQueue<>();

    for (String line : lines) {
        if (!line.trim().isEmpty()) {
            pq.add(WaitlistEntry.fromString(line));
        }
    }

    if (!pq.isEmpty()) {
        WaitlistEntry next = pq.poll();
        String menteeUsername = next.getMenteeUsername();
        String date = next.getDate();
        String time = next.getTime();
        System.out.println("✅ Notifying mentee: " + menteeUsername + " - slot is now available.");

        String notificationFile = "data/notifications_" + menteeUsername + ".txt";
        String message = "📢 You have been moved from waitlist to an active appointment with mentor "
            + mentorUsername + " on " + date + " at " + time + ".";
        FileUtil.appendLine(notificationFile, message);

        // Update waitlist file
        List<String> updated = new ArrayList<>();
        while (!pq.isEmpty()) {
            updated.add(pq.poll().toString());
        }
        FileUtil.writeLines(waitlistFile, updated);

        System.out.println("🔔 Notification added to file: " + notificationFile);
    } else {
        System.out.println("📭 No mentees on the waitlist.");
    }
}



private static int countAppointmentsForMentorOnDate(String mentorUsername, String date) {
    List<String> lines = FileUtil.readLines("data/appointments.txt");
    int count = 0;

    for (String line : lines) {
        String[] parts = line.split(",");
        if (parts.length >= 3 && parts[1].equals(mentorUsername) && parts[2].equals(date)) {
            count++;
        }
    }

    return count;
}


}
