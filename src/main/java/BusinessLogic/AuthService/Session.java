package BusinessLogic.AuthService;

import BusinessLogic.Exceptions.InvalidSessionException;

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

    public static void validateSession(Session session) {
        if (session==null || !session.isValid())
            throw new InvalidSessionException("Current session is invalid");
    }
}
