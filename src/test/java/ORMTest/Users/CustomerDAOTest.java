package ORMTest.Users;

import BusinessLogic.Exceptions.DAOException;
import DomainModel.Users.Customer;
import ORM.Users.CustomerDAO;
import ORM.Users.UserDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import TestUtils.DAOTestUtils;

public class CustomerDAOTest {

    @BeforeEach
    void setUp() {
        DAOTestUtils.resetDatabase();
    }

    @Test
    void aCustomerShouldBeCreated() {
        UserDAO userDAO = new UserDAO();
        CustomerDAO customerDAO = new CustomerDAO(userDAO);
        Customer customer = UserDAOTestUtils.createCustomer();
        Assertions.assertDoesNotThrow(()->{
                    int id = customerDAO.createCustomer(customer);
                    customerDAO.getCustomerByID(id);
        });

    }

    @Test
    void aCustomerShouldBeDeleted() {
        UserDAO userDAO = new UserDAO();
        CustomerDAO customerDAO = new CustomerDAO(userDAO);
        Customer customer = UserDAOTestUtils.createCustomer();
        int id = customerDAO.createCustomer(customer);
        Assertions.assertDoesNotThrow(()->{
            userDAO.deleteUser(id);
        });
        Assertions.assertThrows(DAOException.class, ()->{
            customerDAO.getCustomerByID(id);
        });
    }

    @Test
    void aCustomerShouldBeUpdate() {

    }
}
