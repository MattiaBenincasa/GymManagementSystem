package BusinessLogic.AuthService;

import BusinessLogic.Exceptions.DAOException;
import ORM.Users.UserDAO;

import javax.naming.AuthenticationException;

public class AuthService {
    private Session currentSession;
    private final UserDAO userDAO;

    public AuthService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public Session login(String username, String plainPassword) throws AuthenticationException {
        try {
            String hashPassword = this.userDAO.getHashPasswordFromUsername(username);

            if (!PasswordUtils.checkPassword(plainPassword, hashPassword))
                throw new AuthenticationException("Invalid password");

            String role = this.userDAO.getRoleFromUsername(username);
            int id = this.userDAO.getIdFromUsername(username);
            this.currentSession = new Session(id, role);
            return this.currentSession;
        } catch (DAOException e) {
            throw new AuthenticationException("Invalid username");
        }
    }

    public void logout() {
        this.currentSession = null;
    }

}
