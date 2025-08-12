package ORMTest.Users;

import DomainModel.Users.*;

import java.time.LocalDate;

public class UserDAOTestUtils {

    public static Customer createCustomer(String username, String email) {
        Customer customer = new Customer();
        customer.setUsername(username);
        customer.setPassword("password");
        customer.setName("name");
        customer.setSurname("surname");
        customer.setMail(email);
        customer.setPhoneNumber("343434343434");
        customer.setBirthDate(LocalDate.of(2003, 2, 18));
        customer.setCustomerCategory(CustomerCategory.STUDENT);
        return customer;
    }

    public static Trainer createTrainer(String username, String email) {
        Trainer trainer = new Trainer();
        trainer.setUsername(username);
        trainer.setPassword("password");
        trainer.setName("name");
        trainer.setSurname("surname");
        trainer.setMail(email);
        trainer.setPhoneNumber("343434343434");
        trainer.setBirthDate(LocalDate.of(2003, 2, 18));
        trainer.setIsPersonalTrainer(true);
        trainer.setIsCourseCoach(true);
        return trainer;
    }

    public static Staff createReceptionist(String username, String email) {
        Staff staff = new Staff(StaffRole.RECEPTIONIST);
        staff.setUsername(username);
        staff.setPassword("password");
        staff.setName("name");
        staff.setSurname("surname");
        staff.setMail(email);
        staff.setPhoneNumber("343434343434");
        staff.setBirthDate(LocalDate.of(2003, 2, 18));
        return staff;
    }

    public static Staff createAdmin(String username, String email) {
        Staff staff = new Staff(StaffRole.ADMIN);
        staff.setUsername(username);
        staff.setPassword("password");
        staff.setName("name");
        staff.setSurname("surname");
        staff.setMail(email);
        staff.setPhoneNumber("343434343434");
        staff.setBirthDate(LocalDate.of(2003, 2, 18));
        return staff;
    }

}
