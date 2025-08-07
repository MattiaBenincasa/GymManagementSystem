package BusinessLogic.Users;

import DomainModel.Users.User;
import ORM.Users.UserDAO;

//services related to profile managing (change username, change password, change name...) and delete profile
public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void deleteUser(int userID) {
        this.userDAO.deleteUser(userID);
    }

    public static void updateUserInfo(UserDAO userDAO, User user, String username, String name, String surname, String mail, String phoneNumber) {
        user.setUsername(username);
        user.setName(name);
        user.setSurname(surname);
        user.setMail(mail);
        user.setPhoneNumber(phoneNumber);
        userDAO.updateUser(user);
    }

    public static void changePassword(UserDAO userDAO, User user, String newPassword) {
        user.setPassword(newPassword);
        userDAO.updateUser(user);
    }

}
