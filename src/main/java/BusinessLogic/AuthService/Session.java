package BusinessLogic.AuthService;

import DomainModel.Users.User;

public class Session {
    private int userID;
    private String role;

    public Session (int userID, String role) {
        this.userID = userID;
        this.role = role;
    }

    public int getUserID() {
        return userID;
    }

    public String getRole() {
        return role;
    }
}
