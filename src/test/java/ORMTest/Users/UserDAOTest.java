package ORMTest.Users;

import BusinessLogic.AuthService.PasswordUtils;
import DomainModel.Users.Customer;
import ORM.Users.CustomerDAO;
import ORM.Users.UserDAO;
import org.junit.jupiter.api.*;

import TestUtils.DAOTestUtils;

public class UserDAOTest {

    @BeforeEach
    void setUp() {
        DAOTestUtils.resetDatabase();
    }

    @Test
    void hashPasswordShouldRetrievedFromUsername() {
        UserDAO userDAO = new UserDAO();
        Customer customer = UserDAOTestUtils.createCustomer("customer", "mail@mail.it");
        userDAO.createUser(customer, "CUSTOMER");
        String retrievedHashPassword = userDAO.getHashPasswordFromUsername("customer");
        Assertions.assertDoesNotThrow(
                ()->PasswordUtils.checkPassword("password", retrievedHashPassword)
        );
    }

    @Test
    void userIDShouldRetrievedFromUsername() {
        UserDAO userDAO = new UserDAO();
        CustomerDAO customerDAO = new CustomerDAO(userDAO);
        Customer customer = customerDAO.createCustomer(UserDAOTestUtils.createCustomer("customer", "mail@mail.it"));
        int id_retrieved = userDAO.getIdFromUsername(customer.getUsername());
        Assertions.assertEquals(customer.getId(), id_retrieved);
    }

    @Test
    void roleShouldRetrievedFromUsername() {
        UserDAO userDAO = new UserDAO();
        CustomerDAO customerDAO = new CustomerDAO(userDAO);
        Customer customer = UserDAOTestUtils.createCustomer("customer", "mail@mail.it");
        customerDAO.createCustomer(customer);
        String role = userDAO.getRoleFromUsername(customer.getUsername());
        Assertions.assertEquals(role, "CUSTOMER");
    }

}
