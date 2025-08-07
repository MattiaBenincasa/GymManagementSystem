package BusinessLogic.Users;

import BusinessLogic.AuthService.PasswordUtils;
import DomainModel.Users.Staff;
import DomainModel.Users.StaffRole;
import ORM.Users.StaffDAO;
import ORM.Users.UserDAO;

import java.time.LocalDate;
import java.util.ArrayList;

public class StaffService {
    private final StaffDAO staffDAO;
    private final UserDAO userDAO;

    public StaffService(StaffDAO staffDAO, UserDAO userDAO) {
        this.staffDAO = staffDAO;
        this.userDAO = userDAO;
    }

    public Staff createStaff(String username, String password, String mail, String name, String surname, String phoneNumber, LocalDate birthDate, StaffRole staffRole) {
        Staff staff = new Staff(staffRole);
        staff.setUsername(username);
        staff.setPassword(password);
        staff.setMail(mail);
        staff.setName(name);
        staff.setSurname(surname);
        staff.setPhoneNumber(phoneNumber);
        staff.setBirthDate(birthDate);
        return this.staffDAO.createStaff(staff);
    }

    public void changeStaffInfo(int staffID, String username, String name, String surname, String mail, String phoneNumber) {
        UserService.updateUserInfo(this.userDAO,
                this.staffDAO.getStaffByID(staffID),
                username,
                name,
                surname,
                mail,
                phoneNumber);
    }

    public void changePassword(int staffID, String oldPassword, String newPassword) {
        Staff staff = this.staffDAO.getStaffByID(staffID);
        PasswordUtils.checkPassword(oldPassword, staff.getHashPassword());
        UserService.changePassword(this.userDAO, staff, newPassword);
    }

    public ArrayList<Staff> getAllAdmins() {
        return this.staffDAO.getAllAdmins();
    }

    public ArrayList<Staff> getAllReceptionist() {
        return this.staffDAO.getAllReceptionists();
    }

}
