package ORMTest.Users;

import BusinessLogic.Exceptions.DAOException;
import DomainModel.Users.Staff;
import ORM.Users.StaffDAO;
import ORM.Users.UserDAO;
import TestUtils.DAOTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class StaffDAOTest {

    @BeforeEach
    void setUp() {
        DAOTestUtils.resetDatabase();
    }

    @Test
    void aStaffShouldBeCreated() {
        UserDAO userDAO = new UserDAO();
        StaffDAO staffDAO = new StaffDAO(userDAO);
        Staff staff = UserDAOTestUtils.createStaff("staff", "mailStaff@mail.it");
        Assertions.assertDoesNotThrow(()->{
            Staff staffWithId = staffDAO.createStaff(staff);
            staffDAO.getStaffByID(staffWithId.getId());
        });
    }

    @Test
    void aStaffShouldBeDeleted() {
        UserDAO userDAO = new UserDAO();
        StaffDAO staffDAO = new StaffDAO(userDAO);
        Staff staff = staffDAO.createStaff(UserDAOTestUtils.createStaff("staff", "mailStaff@mail.it"));
        Assertions.assertDoesNotThrow(()->{
            userDAO.deleteUser(staff.getId());
        });
        Assertions.assertThrows(DAOException.class, ()->{
            staffDAO.getStaffByID(staff.getId());
        });
    }

    @Test
    void aStaffShouldBeUpdated() {
        UserDAO userDAO = new UserDAO();
        StaffDAO staffDAO = new StaffDAO(userDAO);
        Staff staff = staffDAO.createStaff(UserDAOTestUtils.createStaff("staff", "mailStaff@mail.it"));

        staff = staffDAO.getStaffByID(staff.getId());
        staff.setUsername("new_username");
        staff.setPassword("new_plain_password");
        staff.setName("new_name");
        staff.setSurname("new_surname");
        staff.setMail("new_mail@new_mail.com");
        staff.setPhoneNumber("11111111111");
        staff.setBirthDate(LocalDate.of(1990, 1, 1));
        staffDAO.updateStaff(staff);

        Staff updatedStaff = staffDAO.getStaffByID(staff.getId());

        Assertions.assertEquals(staff, updatedStaff);
    }
}
