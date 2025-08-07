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
                    Customer customerWithId = customerDAO.createCustomer(customer);
                    customerDAO.getCustomerByID(customerWithId.getId());
        });

    }

    @Test
    void aCustomerShouldBeDeleted() {
        UserDAO userDAO = new UserDAO();
        CustomerDAO customerDAO = new CustomerDAO(userDAO);
        Customer customer = customerDAO.createCustomer(UserDAOTestUtils.createCustomer("customer", "mail@mail.it"));
        Assertions.assertDoesNotThrow(()->{
            userDAO.deleteUser(customer.getId());
        });
        Assertions.assertThrows(DAOException.class, ()->{
            customerDAO.getCustomerByID(customer.getId());
        });
    }

    @Test
    void aCustomerShouldBeUpdate() {
        UserDAO userDAO = new UserDAO();
        CustomerDAO customerDAO = new CustomerDAO(userDAO);
        Customer customer = customerDAO.createCustomer(UserDAOTestUtils.createCustomer("customer", "mail@mail.it"));

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
        Customer updatedCustomer = customerDAO.getCustomerByID(customer.getId());
        Assertions.assertEquals(customer, updatedCustomer);

        //update med. Certificate

        MedicalCertificate medicalCertificate = new MedicalCertificate(LocalDate.now(), 12, false);
        customer.setMedicalCertificate(medicalCertificate);
        customerDAO.updateCustomer(customer);
        updatedCustomer = customerDAO.getCustomerByID(customer.getId());
        Assertions.assertEquals(customer, updatedCustomer);
        Assertions.assertEquals(customer.getMedicalCertificate(), updatedCustomer.getMedicalCertificate());

    }
}
