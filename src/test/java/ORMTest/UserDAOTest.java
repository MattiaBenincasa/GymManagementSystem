package ORMTest;

import DomainModel.Users.Customer;
import ORM.Users.UserDAO;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class UserDAOTest {

    @Test
    void aUserShouldBeCreated() {
        UserDAO userDAO = new UserDAO();

        Customer customer = new Customer();
        customer.setUsername("username");
        customer.setPassword("password");
        customer.setName("name");
        customer.setSurname("surname");
        customer.setMail("mail@mail.it");
        customer.setPhoneNumber("343434343434");
        customer.setBirthDate(LocalDate.now());

        userDAO.createUser(customer, "CUSTOMER");
    }

    void aUserShouldBeUpdated() {

    }

    void aUserShouldBeDeleted() {

    }
}
