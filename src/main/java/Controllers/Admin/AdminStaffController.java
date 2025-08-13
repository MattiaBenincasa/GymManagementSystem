package Controllers.Admin;

import BusinessLogic.AuthService.Session;
import BusinessLogic.Users.StaffService;
import BusinessLogic.Users.TrainerService;
import BusinessLogic.Users.UserService;
import DomainModel.Users.Staff;
import DomainModel.Users.StaffRole;
import DomainModel.Users.Trainer;

import java.time.LocalDate;
import java.util.ArrayList;

public class AdminStaffController {
    private final StaffService staffService;
    private final TrainerService trainerService;
    private final UserService userService;
    private Session session;

    public AdminStaffController(StaffService staffService, TrainerService trainerService, UserService userService) {
        this.staffService = staffService;
        this.trainerService = trainerService;
        this.userService = userService;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Staff getPersonalInfo(){
        Session.validateSession(this.session);
        return staffService.getStaffByID(this.session.getUserID());
    }

    public Staff createStaff(String username, String password, String mail, String name, String surname, String phoneNumber, LocalDate birthDate, StaffRole staffRole) {
        Session.validateSession(this.session);
        return staffService.createStaff(username, password, mail, name, surname, phoneNumber, birthDate, staffRole);
    }

    public Trainer createTrainer(String username, String password, String mail, String name, String surname, String phoneNumber, LocalDate birthDate) {
        Session.validateSession(this.session);
        return trainerService.createTrainer(username, password, mail, name, surname, phoneNumber, birthDate);
    }

    public void changeAdminInfo(String username, String name, String surname, String mail, String phoneNumber) {
        Session.validateSession(this.session);
        staffService.changeStaffInfo(this.session.getUserID(), username, name, surname, mail, phoneNumber);
    }

    public void changePassword(String oldPassword, String newPassword) {
        Session.validateSession(this.session);
        staffService.changePassword(this.session.getUserID(), oldPassword, newPassword);
    }

    public ArrayList<Staff> getAllAdmins() {
        Session.validateSession(this.session);
        return staffService.getAllAdmins();
    }

    public ArrayList<Staff> getAllReceptionist() {
        Session.validateSession(this.session);
        return staffService.getAllReceptionist();
    }

    public Trainer setUpTrainerInfo(int trainerID, boolean isPersonalTrainer, boolean isCourseCoach) {
        Session.validateSession(this.session);
        return trainerService.setUpTrainerInfo(trainerID, isPersonalTrainer, isCourseCoach);
    }

    public ArrayList<Trainer> getAllTrainers() {
        Session.validateSession(this.session);
        return trainerService.getAllTrainers();
    }

    public ArrayList<Trainer> getAllPersonalTrainers() {
        Session.validateSession(this.session);
        return trainerService.getAllPersonalTrainers();
    }

    public ArrayList<Trainer> getAllCourseCoach() {
        Session.validateSession(this.session);
        return trainerService.getAllCourseCoach();
    }

    public void deleteUser(int userID) {
        Session.validateSession(this.session);
        this.userService.deleteUser(userID);
    }
}
