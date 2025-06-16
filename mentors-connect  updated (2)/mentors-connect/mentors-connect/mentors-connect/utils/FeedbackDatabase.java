package utils;

import models.Feedback;
import java.sql.*;

public class FeedbackDatabase {
    static final String DB_URL = "jdbc:mysql://localhost:3306/mentors_meet";
    static final String USER = "root"; // change if needed
    static final String PASS = "password"; // change if needed

    public static void insertFeedback(Feedback fb) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sql = "INSERT INTO feedback (session_id, mentee_name, mentor_name, rating, comment) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, fb.sessionId);
            stmt.setString(2, fb.menteeName);
            stmt.setString(3, fb.mentorName);
            stmt.setInt(4, fb.rating);
            stmt.setString(5, fb.comment);
            stmt.executeUpdate();
            System.out.println("✅ Stored in SQL for " + fb.mentorName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
