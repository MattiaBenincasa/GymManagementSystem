package ORMTest.Users;

import BusinessLogic.AuthService.PasswordUtils;
import DomainModel.Users.Customer;
import ORM.Users.UserDAO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.Assertions;
import TestUtils.DAOTestUtils;

public class UserDAOTest {

    @BeforeAll
    static void setUp() {
        DAOTestUtils.resetDatabase();
        UserDAO userDAO = new UserDAO();
        // insert a customer into DB to test getHashPasswordFromUsername()
        Customer customer = UserDAOTestUtils.createCustomer();
        userDAO.createUser(customer, "CUSTOMER");
    }

    @Test
    void hashPasswordShouldRetrievedFromUsername() {
        UserDAO userDAO = new UserDAO();
        String retrievedHashPassword = userDAO.getHashPasswordFromUsername("customer");
        Assertions.assertDoesNotThrow(
                ()->PasswordUtils.checkPassword("password", retrievedHashPassword)
        );

    }

    @AfterAll
    static void tearDown() {
        DAOTestUtils.resetDatabase();
    }

}
