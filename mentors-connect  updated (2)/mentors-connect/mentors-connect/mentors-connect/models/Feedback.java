package models;

public class Feedback {
    public String sessionId;
    public String menteeName;
    public String mentorName;
    public int rating;
    public String comment;

    public Feedback(String sessionId, String menteeName, String mentorName, int rating, String comment) {
        this.sessionId = sessionId;
        this.menteeName = menteeName;
        this.mentorName = mentorName;
        this.rating = rating;
        this.comment = comment;
    }

    public void display() {
        System.out.println("Session: " + sessionId + ", Mentee: " + menteeName + ", Rating: " + rating);
        System.out.println("Comment: " + comment);
    }
}
