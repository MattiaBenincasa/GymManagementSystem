package ORMTest.Users;

import DomainModel.Users.*;

import java.time.LocalDate;

public class UserDAOTestUtils {

    public static Customer createCustomer() {
        Customer customer = new Customer();
        customer.setUsername("customer");
        customer.setPassword("password");
        customer.setName("name");
        customer.setSurname("surname");
        customer.setMail("mail@mail.it");
        customer.setPhoneNumber("343434343434");
        customer.setBirthDate(LocalDate.of(2003, 2, 18));
        customer.setCustomerCategory(CustomerCategory.STUDENT);
        return customer;
    }

    public static Trainer createTrainer() {
        Trainer trainer = new Trainer();
        trainer.setUsername("trainer");
        trainer.setPassword("password");
        trainer.setName("name");
        trainer.setSurname("surname");
        trainer.setMail("mail@mail.it");
        trainer.setPhoneNumber("343434343434");
        trainer.setBirthDate(LocalDate.of(2003, 2, 18));
        trainer.setIsPersonalTrainer(true);
        trainer.setIsCourseCoach(true);
        return trainer;
    }

    public static Staff createStaff() {
        Staff staff = new Staff(StaffRole.RECEPTIONIST);
        staff.setUsername("staff");
        staff.setPassword("password");
        staff.setName("name");
        staff.setSurname("surname");
        staff.setMail("mail@mail.it");
        staff.setPhoneNumber("343434343434");
        staff.setBirthDate(LocalDate.of(2003, 2, 18));
        return staff;
    }

}
