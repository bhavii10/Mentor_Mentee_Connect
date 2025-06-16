package models;

public abstract class User {
    protected String username;
    protected String password;
    protected String role; // "mentor" or "mentee"

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() { return username; }
    public String getRole() { return role; }
    public boolean checkPassword(String input) {
        return this.password.equals(input);
    }
}
