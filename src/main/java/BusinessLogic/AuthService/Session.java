package BusinessLogic.AuthService;

public class Session {
    private final int userID;
    private final String role;
    private boolean valid;

    public Session (int userID, String role) {
        this.userID = userID;
        this.role = role;
        valid = true;
    }

    public int getUserID() {
        return userID;
    }

    public String getRole() {
        return role;
    }

    public boolean isValid() {
        return this.valid;
    }

    public void invalid() {
        this.valid = false;
    }
}
