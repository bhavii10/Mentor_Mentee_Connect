package models;

public class Mentee extends User {
    public Mentee(String username, String password) {
        super(username, password, "mentee");
    }
}