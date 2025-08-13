package Controllers.Admin;

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

    public AdminStaffController(StaffService staffService, TrainerService trainerService, UserService userService) {
        this.staffService = staffService;
        this.trainerService = trainerService;
        this.userService = userService;
    }

    public Staff createStaff(String username, String password, String mail, String name, String surname, String phoneNumber, LocalDate birthDate, StaffRole staffRole) {
        return staffService.createStaff(username, password, mail, name, surname, phoneNumber, birthDate, staffRole);
    }

    public void changeStaffInfo(int staffID, String username, String name, String surname, String mail, String phoneNumber) {
        staffService.changeStaffInfo(staffID, username, name, surname, mail, phoneNumber);
    }

    public void changePassword(int staffID, String oldPassword, String newPassword) {
        staffService.changePassword(staffID, oldPassword, newPassword);
    }

    public ArrayList<Staff> getAllAdmins() {
        return staffService.getAllAdmins();
    }

    public ArrayList<Staff> getAllReceptionist() {
        return staffService.getAllReceptionist();
    }

    public Trainer setUpTrainerInfo(int trainerID, boolean isPersonalTrainer, boolean isCourseCoach) {
        return trainerService.setUpTrainerInfo(trainerID, isPersonalTrainer, isCourseCoach);
    }

    public void changeTrainerInfo(int trainerID, String username, String name, String surname, String mail, String phoneNumber) {
        trainerService.changeTrainerInfo(trainerID, username, name, surname, mail, phoneNumber);
    }

    public ArrayList<Trainer> getAllTrainers() {
        return trainerService.getAllTrainers();
    }

    public ArrayList<Trainer> getAllPersonalTrainers() {
        return trainerService.getAllPersonalTrainers();
    }

    public ArrayList<Trainer> getAllCourseCoach() {
        return trainerService.getAllCourseCoach();
    }

    public void deleteUser(int userID) {
        this.userService.deleteUser(userID);
    }
}
