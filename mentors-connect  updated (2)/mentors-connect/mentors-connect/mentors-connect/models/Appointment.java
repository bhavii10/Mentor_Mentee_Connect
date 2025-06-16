package models;

public class Appointment implements Comparable<Appointment> {
    private String mentorUsername;
    private String menteeUsername;
    public String date;
    public String time;
    private String status;

    // Constructor
    public Appointment(String mentorUsername, String menteeUsername, String date, String time, String status) {
        this.mentorUsername = mentorUsername;
        this.menteeUsername = menteeUsername;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    // Getters
    public String getMentorUsername() {
        return mentorUsername;
    }

    public String getMenteeUsername() {
        return menteeUsername;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }

    // Setters
    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Used for sorting (date + time string comparison)
    @Override
    public int compareTo(Appointment other) {
        String thisDateTime = this.date + " " + this.time;
        String otherDateTime = other.date + " " + other.time;
        return thisDateTime.compareTo(otherDateTime);
    }

    // Convert object to string (to store in file)
    @Override
    public String toString() {
        return mentorUsername + "," + menteeUsername + "," + date + "," + time + "," + status;
    }

    // Create object from stored string
    public static Appointment fromString(String str) {
        String[] parts;
        if (str.contains("|")) {
            parts = str.split("\\|", 5);
        } else {
            parts = str.split(",", 5);
        }

        if (parts.length < 5) {
            System.out.println("❌ Invalid line format: " + str);
            return null;
        }

        return new Appointment(parts[0], parts[1], parts[2], parts[3], parts[4]);
    }
}
