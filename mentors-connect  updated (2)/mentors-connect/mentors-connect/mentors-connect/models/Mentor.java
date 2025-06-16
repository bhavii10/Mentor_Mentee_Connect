// package models;

// public class Mentor extends User {
//     public Mentor(String username, String password) {
//         super(username, password, "mentor");
//     }
// }


package models;

public class Mentor extends User {
    private String category;

    public Mentor(String username, String password, String category) {
        super(username, password, "mentor");
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}



