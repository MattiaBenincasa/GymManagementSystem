package ORMTest.Users;

import BusinessLogic.Exceptions.DAOException;
import DomainModel.Users.Customer;
import DomainModel.Users.CustomerCategory;
import DomainModel.Users.MedicalCertificate;
import ORM.Users.CustomerDAO;
import ORM.Users.UserDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import TestUtils.DAOTestUtils;

import java.time.LocalDate;

public class CustomerDAOTest {

    @BeforeEach
    void setUp() {
        DAOTestUtils.resetDatabase();
    }

    @Test
    void aCustomerShouldBeCreated() {
        UserDAO userDAO = new UserDAO();
        CustomerDAO customerDAO = new CustomerDAO(userDAO);
        Customer customer = UserDAOTestUtils.createCustomer("customer", "mail@mail.it");
        Assertions.assertDoesNotThrow(()->{
                    int id = customerDAO.createCustomer(customer);
                    customerDAO.getCustomerByID(id);
        });

    }

    @Test
    void aCustomerShouldBeDeleted() {
        UserDAO userDAO = new UserDAO();
        CustomerDAO customerDAO = new CustomerDAO(userDAO);
        Customer customer = UserDAOTestUtils.createCustomer("customer", "mail@mail.it");
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
        UserDAO userDAO = new UserDAO();
        CustomerDAO customerDAO = new CustomerDAO(userDAO);
        Customer customer = UserDAOTestUtils.createCustomer("customer", "mail@mail.it");
        int id = customerDAO.createCustomer(customer);
        //get customer with right id
        customer = customerDAO.getCustomerByID(id);

        //update username
        customer.setUsername("new_username");
        customer.setPassword("new_plain_password");
        customer.setName("new_name");
        customer.setSurname("new_surname");
        customer.setMail("new_mail@new_mail.com");
        customer.setPhoneNumber("11111111111");
        customer.setBirthDate(LocalDate.of(1990, 1, 1));
        customer.setCustomerCategory(CustomerCategory.MILITARY);
        customerDAO.updateCustomer(customer);
        Customer updatedCustomer = customerDAO.getCustomerByID(id);
        Assertions.assertEquals(customer, updatedCustomer);

        //update med. Certificate

        MedicalCertificate medicalCertificate = new MedicalCertificate(LocalDate.now(), 12, false);
        customer.setMedicalCertificate(medicalCertificate);
        customerDAO.updateCustomer(customer);
        updatedCustomer = customerDAO.getCustomerByID(id);
        Assertions.assertEquals(customer, updatedCustomer);
        Assertions.assertEquals(customer.getMedicalCertificate(), updatedCustomer.getMedicalCertificate());

    }
}
