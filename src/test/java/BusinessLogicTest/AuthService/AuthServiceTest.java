package BusinessLogicTest.AuthService;

import BusinessLogic.AuthService.AuthService;
import BusinessLogic.AuthService.Session;
import ORM.Users.CustomerDAO;
import ORM.Users.StaffDAO;
import ORM.Users.TrainerDAO;
import ORM.Users.UserDAO;
import ORMTest.Users.UserDAOTestUtils;
import TestUtils.DAOTestUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.naming.AuthenticationException;

import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTest {
    private AuthService authService;

    @BeforeEach
    void setup() {
        DAOTestUtils.resetDatabase();
        UserDAO userDAO = new UserDAO();
        CustomerDAO customerDAO = new CustomerDAO(userDAO);
        TrainerDAO trainerDAO = new TrainerDAO(userDAO);
        StaffDAO staffDAO = new StaffDAO(userDAO);

        customerDAO.createCustomer(UserDAOTestUtils.createCustomer("customer", "customer@customer.it"));
        trainerDAO.createTrainer(UserDAOTestUtils.createTrainer("trainer", "trainer@trainer.it)"));
        staffDAO.createStaff(UserDAOTestUtils.createReceptionist("receptionist", "receptionist@recept.it"));
        staffDAO.createStaff(UserDAOTestUtils.createAdmin("admin", "admin@admin.it"));
        this.authService = new AuthService(userDAO);
    }


    @Test
    void testLoginShouldFailed() {
        // wrong username
        Throwable invalidUsername = assertThrows(AuthenticationException.class, ()->{
            Session session = this.authService.login("wrongUsername", "password");
            assertNull(session);
        });

        assertEquals("Invalid username", invalidUsername.getMessage());

        //wrong password
        Throwable invalidPassword = assertThrows(AuthenticationException.class, ()->{
            Session session = this.authService.login("customer", "wrongPassword");
            assertNull(session);
        });

        assertEquals("Invalid password", invalidPassword.getMessage());
    }

    @Test
    void testLoginLogoutSuccess() {
        assertDoesNotThrow(()->{
            Session session = this.authService.login("customer", "password");
            assertEquals(session.getRole(), "CUSTOMER");
            assertEquals(session.getUserID(), 1);
            assertTrue(session.isValid());
            this.authService.logout(session);
            assertFalse(session.isValid());

        });

        //Test login other users
        assertDoesNotThrow(()->{
            Session trainerSession = this.authService.login("trainer", "password");
            Session receptionistSession = this.authService.login("receptionist", "password");
            Session adminSession = this.authService.login("admin", "password");

            assertEquals(trainerSession.getRole(), "TRAINER");
            assertEquals(receptionistSession.getRole(), "RECEPTIONIST");
            assertEquals(adminSession.getRole(), "ADMIN");
            this.authService.logout(trainerSession);
            this.authService.logout(receptionistSession);
            this.authService.logout(adminSession);
            assertFalse(trainerSession.isValid());
            assertFalse(receptionistSession.isValid());
            assertFalse(adminSession.isValid());
        });
    }

    @AfterAll
    static void teardown() {
        DAOTestUtils.resetDatabase();
    }
}
