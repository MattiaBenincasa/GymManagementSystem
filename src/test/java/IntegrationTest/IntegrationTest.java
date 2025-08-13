package IntegrationTest;

import BusinessLogic.AuthService.PasswordUtils;
import BusinessLogic.Exceptions.InvalidSessionException;
import BusinessLogic.Exceptions.UnauthorizedException;
import Controllers.Admin.AdminCourseController;
import Controllers.Admin.AdminStaffController;
import Controllers.ApplicationManager;
import Controllers.Receptionist.ReceptionistController;
import Controllers.Trainer.TrainerController;
import DomainModel.DailyEvents.DailyClass;
import DomainModel.Users.Staff;
import DomainModel.Users.StaffRole;
import DomainModel.Users.Trainer;
import ORM.Users.StaffDAO;
import ORM.Users.UserDAO;
import ORMTest.Users.UserDAOTestUtils;
import TestUtils.DAOTestUtils;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import javax.naming.AuthenticationException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Objects;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class IntegrationTest {
    static ApplicationManager applicationManager;

    @BeforeAll
    static void setup() {
        // reset db and insert new admin in db
        DAOTestUtils.resetDatabase();
        Staff admin = UserDAOTestUtils.createAdmin("mario.rossi", "mario.rossi@gmail.com");
        StaffDAO staffDAO = new StaffDAO(new UserDAO());
        staffDAO.createStaff(admin);
        applicationManager = new ApplicationManager(); //creation of all controllers with all their dependencies
    }

    @Test
    void test1_loginLogoutAdmin() throws AuthenticationException {
        //admin log in
        applicationManager.login("mario.rossi", "password");
        assertThrows(UnauthorizedException.class, ()->{
            applicationManager.getCustomerController();
            applicationManager.getReceptionistController();
            applicationManager.getTrainerController();
        });

        assertDoesNotThrow(()->{
            applicationManager.getAdminCourseController();
            applicationManager.getAdminStaffController();
            applicationManager.getAdminMembershipController();
        });

        applicationManager.logout();

        Throwable invalidException = assertThrows(InvalidSessionException.class, ()->{
            applicationManager.getAdminMembershipController();
        });

        assertEquals("Current session is invalid", invalidException.getMessage());
    }

    @Test
    void test2_changeAdminInfo() throws AuthenticationException {
        //admin log in
        applicationManager.login("mario.rossi", "password");
        AdminStaffController adminStaffController = applicationManager.getAdminStaffController();

        //change admin profile details
        adminStaffController.changeAdminInfo("mariorossi", "Mario", "Rossi", "mario.rossi@gmail.com", "33344345");
        adminStaffController.changePassword("password", "newpassword");
        Staff adminInfo = adminStaffController.getPersonalInfo();
        assertEquals(adminInfo.getId(), applicationManager.getCurrentSession().getUserID());
        assertEquals("mariorossi", adminInfo.getUsername());
        assertEquals("Mario", adminInfo.getName());
        assertEquals("Rossi", adminInfo.getSurname());
        assertEquals("mario.rossi@gmail.com", adminInfo.getMail());
        assertEquals("33344345", adminInfo.getPhoneNumber());
        assertTrue(PasswordUtils.checkPassword("newpassword", adminInfo.getHashPassword()));
    }

    @Test
    void test3_CRUDStaffAndTrainer() {
        //admin is always logged in
        AdminStaffController adminStaffController = applicationManager.getAdminStaffController();
        Staff receptionist = adminStaffController.createStaff("chiara.solari", "temporary", "chiara.sol@gmail.com", "chiara", "solari", "3452221345" , LocalDate.of(2000, 7, 12), StaffRole.RECEPTIONIST);
        Trainer trainer1 = adminStaffController.createTrainer("gabri.morandi", "temporary", "gabri@gmail.com", "Gabriele", "Morandi", "22222222", LocalDate.of(1990, 4, 4));
        Trainer trainer2 = adminStaffController.createTrainer("viola.pasqui", "temporary", "viola@gmail.com", "Viola", "Pasqui", "233545412", LocalDate.of(1999, 1, 4));
        Trainer trainer3 = adminStaffController.createTrainer("tommaso.verdi", "temporary", "tom@gmail.com", "Tommaso", "Verdi", "22222222", LocalDate.of(1990, 4, 4));
        adminStaffController.setUpTrainerInfo(trainer1.getId(), true, true);
        adminStaffController.setUpTrainerInfo(trainer2.getId(), false, true);
        adminStaffController.setUpTrainerInfo(trainer3.getId(), true, false);

        //get all admins
        ArrayList<Staff> allAdmins = adminStaffController.getAllAdmins();
        assertEquals(1, allAdmins.size());
        assertEquals(applicationManager.getCurrentSession().getUserID(), allAdmins.getFirst().getId());

        //get all receptionists
        ArrayList<Staff> allReceptionist = adminStaffController.getAllReceptionist();
        assertEquals(1, allReceptionist.size());
        assertEquals(receptionist.getId(), allReceptionist.getFirst().getId());

        //get all trainers
        ArrayList<Trainer> allTrainers = adminStaffController.getAllTrainers();
        assertEquals(3, allTrainers.size());
        assertEquals(trainer1.getId(), allTrainers.getFirst().getId());
        assertEquals(trainer2.getId(), allTrainers.get(1).getId());
        assertEquals(trainer3.getId(), allTrainers.get(2).getId());

        //get all courseCoach
        ArrayList<Trainer> allCourseCoach = adminStaffController.getAllCourseCoach();
        assertEquals(2, allCourseCoach.size());

        //get all personal trainers
        ArrayList<Trainer> allPersonalTrainers = adminStaffController.getAllPersonalTrainers();
        assertEquals(2, allPersonalTrainers.size());
        applicationManager.logout();
    }

    @Test
    void test4_changePersonalInfoOfUsersCreatedByAdmin() throws AuthenticationException {
        // receptionist log in
        applicationManager.login("chiara.solari", "temporary");
        ReceptionistController receptionistController = applicationManager.getReceptionistController();

        receptionistController.changePersonalInfo("chiarasolari", "chiara", "solari", "chiara.sol@gmail.com", "3452221345");
        receptionistController.changePassword("temporary", "newpassword");
        Staff receptionistPersonalInfo = receptionistController.getPersonalInfo();

        assertEquals("chiarasolari", receptionistPersonalInfo.getUsername());
        assertTrue(PasswordUtils.checkPassword("newpassword", receptionistPersonalInfo.getHashPassword()));
        applicationManager.logout();

        //trainer 1 log in
        applicationManager.login("gabri.morandi", "temporary");
        TrainerController trainerController = applicationManager.getTrainerController();
        trainerController.changePersonalInfo("gabrimorandi", "Gabriele", "Morandi", "gabri@gmail.com", "22222222");
        trainerController.changePassword("temporary", "newpassword");
        Trainer trainerPersonalInfo = trainerController.getPersonalInfo();
        assertEquals("gabrimorandi", trainerPersonalInfo.getUsername());
        assertTrue(PasswordUtils.checkPassword("newpassword", trainerPersonalInfo.getHashPassword()));
        applicationManager.logout();
    }

    @Test
    void test5_CRUDCourse() throws AuthenticationException {
        //admin log in
        applicationManager.login("mariorossi", "newpassword");
        AdminCourseController adminCourseController = applicationManager.getAdminCourseController();
        AdminStaffController adminStaffController = applicationManager.getAdminStaffController();

        adminCourseController.createCourse("Yoga", "Corso di Yoga");
        ArrayList<Trainer> trainers = adminStaffController.getAllCourseCoach();

        Trainer trainerOfInterest = new Trainer();
        for (Trainer trainer : trainers)
            if (Objects.equals(trainer.getUsername(), "viola.pasqui"))
                trainerOfInterest = trainer;

        adminCourseController.addTrainerToCourse(1, trainerOfInterest.getId());


        //add weekly daily classes
        ArrayList<DailyClass> dailyClasses = adminCourseController.addWeeklyClassSchedule(DayOfWeek.MONDAY,
                LocalDate.of(2026, 3, 1),
                LocalDate.of(2026, 3, 31),
                1,
                LocalTime.of(18, 30),
                LocalTime.of(19, 30),
                20,
                trainerOfInterest);

        //delete last class
        for (DailyClass dailyClass : dailyClasses)
            if (dailyClass.getDay().isEqual(LocalDate.of(2026, 3, 30)))
                adminCourseController.deleteDailyClass(dailyClass.getId());

        ArrayList<DailyClass> allDailyClass = adminCourseController.getAllDailyClasses();
        assertEquals(4, allDailyClass.size());

        //create new course
        adminCourseController.createCourse("Boxe", "Corso di Boxe");

        for (Trainer trainer : trainers)
            if (Objects.equals(trainer.getUsername(), "gabrimorandi"))
                trainerOfInterest = trainer;

        adminCourseController.addTrainerToCourse(2, trainerOfInterest.getId());
        adminCourseController.addDailyClass(LocalDate.of(2026, 3, 3), LocalTime.of(10, 30), LocalTime.of(11, 30), 12, 2, trainerOfInterest);

        ArrayList<DailyClass> boxeDailyClass = adminCourseController.getAllDailyClassesByCourseID(2);
        assertEquals(1, boxeDailyClass.size());
    }



    @AfterAll
    static void teardown() {
        DAOTestUtils.resetDatabase();
    }

}
