package services;

import models.Feedback;
import java.util.*;

public class FeedbackService {
    public static ArrayList<Feedback> feedbackList = new ArrayList<>();
    public static HashMap<String, ArrayList<Feedback>> feedbackByMentor = new HashMap<>();

    public static void addFeedback(Feedback fb) {
        feedbackList.add(fb);
        feedbackByMentor.putIfAbsent(fb.mentorName, new ArrayList<>());
        feedbackByMentor.get(fb.mentorName).add(fb);
    }

    public static void showMentorFeedback(String mentorName) {
        if (feedbackByMentor.containsKey(mentorName)) {
            for (Feedback fb : feedbackByMentor.get(mentorName)) {
                fb.display();
            }
        } else {
            System.out.println("❌ No feedback for this mentor.");
        }
    }
}
