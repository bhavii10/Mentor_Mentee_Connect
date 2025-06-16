package models;

public class WaitlistEntry implements Comparable<WaitlistEntry> {
    private String menteeUsername;
    private String date;
    private String time;
    private int priority; // 1 = VIP, 0 = Normal

    public WaitlistEntry(String menteeUsername, String date, String time, int priority) {
        this.menteeUsername = menteeUsername;
        this.date = date;
        this.time = time;
        this.priority = priority;
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

    public int getPriority() {
        return priority;
    }

    @Override
    public int compareTo(WaitlistEntry other) {
        return Integer.compare(other.priority, this.priority); // VIPs first
    }

    @Override
    public String toString() {
        return menteeUsername + "," + date + "," + time + "," + priority;
    }

    public static WaitlistEntry fromString(String line) {
        String[] parts = line.split(",");
        return new WaitlistEntry(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
    }
}

