package services;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class NotificationService {
    private static final String NOTIF_FILE = "data/notifications.txt";

    public static void addNotification(String username, String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(NOTIF_FILE, true))) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            writer.write(username + "|" + message + "|" + timestamp);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error adding notification: " + e.getMessage());
        }
    }

    public static List<String> getUserNotifications(String username) {
        List<String> result = new ArrayList<>();
        File file = new File(NOTIF_FILE);
        if (!file.exists()) return result;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 3 && parts[0].equals(username)) {
                    result.add(parts[1] + " (" + parts[2] + ")");
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading notifications: " + e.getMessage());
        }
        return result;
    }
}
