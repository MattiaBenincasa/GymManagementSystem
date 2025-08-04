package BusinessLogic.AuthService;

import DomainModel.Users.User;
import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {

    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    public static String changePassword(User user, String newPlainPassword) {
        //TODO save new password into DB with DAO
        return hashPassword(newPlainPassword);
    }

}
