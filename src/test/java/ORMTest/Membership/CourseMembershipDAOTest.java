package ORMTest.Membership;

import BusinessLogic.Exceptions.DAOException;
import DomainModel.Course;
import DomainModel.Membership.CourseMembership;
import ORM.Membership.CourseDAO;
import ORM.Membership.CourseMembershipDAO;
import ORM.Membership.MembershipDAO;
import ORM.Users.TrainerDAO;
import ORM.Users.UserDAO;
import TestUtils.DAOTestUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class CourseMembershipDAOTest {
    private CourseMembershipDAO courseMembershipDAO;
    private CourseDAO courseDAO;
    private TrainerDAO trainerDAO;

    @BeforeEach
    void setUp() {
        DAOTestUtils.resetDatabase();
        trainerDAO = new TrainerDAO(new UserDAO());
        courseDAO = new CourseDAO(this.trainerDAO);
        courseMembershipDAO = new CourseMembershipDAO(new MembershipDAO(), this.courseDAO);
    }

    @Test
    void testCreateCourseMembership() {
        Course course = courseDAO.createCourse(new Course("Corso di Yoga", "Descrizione corso"));
        CourseMembership courseMembership = new CourseMembership();
        courseMembership.setName("Abbonamento Yoga");
        courseMembership.setDescription("Abbonamento per il corso di Yoga");
        courseMembership.setPrice(new BigDecimal("75.00"));
        courseMembership.setDurationInDays(30);
        courseMembership.setCourse(course);
        courseMembership.setWeeklyAccess(2);

        assertDoesNotThrow(() -> {
            CourseMembership courseMembershipWithID = courseMembershipDAO.createCourseMembership(courseMembership);
            CourseMembership retrieved = courseMembershipDAO.getCourseMembershipByID(courseMembershipWithID.getId());
            assertEquals(courseMembershipWithID.getId(), retrieved.getId());
            assertEquals(courseMembershipWithID.getName(), retrieved.getName());
            assertEquals(courseMembershipWithID.getDescription(), retrieved.getDescription());
            assertEquals(courseMembershipWithID.getCourse().getId(), retrieved.getCourse().getId());
        });
    }

    @Test
    void testUpdateCourseMembership() throws DAOException {
        Course course1 = courseDAO.createCourse(new Course("Corso di Yoga", "Descrizione corso"));

        CourseMembership courseMembership = new CourseMembership();
        courseMembership.setName("Abbonamento Yoga");
        courseMembership.setDescription("Abbonamento per il corso di Yoga");
        courseMembership.setPrice(new BigDecimal("75.00"));
        courseMembership.setDurationInDays(30);
        courseMembership.setCourse(course1);
        courseMembership.setWeeklyAccess(2);

        CourseMembership courseMembershipWithID = courseMembershipDAO.createCourseMembership(courseMembership);

        Course course2 = courseDAO.createCourse(new Course("Pilates", "nuova descrizione"));

        courseMembershipWithID.setName("Abbonamento Pilates Updated");
        courseMembershipWithID.setDescription("Descrizione Updated");
        courseMembershipWithID.setPrice(new BigDecimal("85.00"));
        courseMembershipWithID.setCourse(course2);
        courseMembershipWithID.setWeeklyAccess(3);
        courseMembershipDAO.updateCourseMembership(courseMembershipWithID);

        CourseMembership retrieved = courseMembershipDAO.getCourseMembershipByID(courseMembershipWithID.getId());
        assertEquals("Abbonamento Pilates Updated", retrieved.getName());
        assertEquals("Descrizione Updated", retrieved.getDescription());
        assertEquals(new BigDecimal("85.00"), retrieved.getPrice());
        assertEquals(3, retrieved.getWeeklyAccess());
        assertEquals(course2.getId(), retrieved.getCourse().getId());
    }

    @Test
    void testDeleteCourseMembership() throws DAOException {

        Course course = this.courseDAO.createCourse(new Course("Corso di Yoga", "Descrizione corso"));

        CourseMembership courseMembership = new CourseMembership();
        courseMembership.setName("Abbonamento Yoga");
        courseMembership.setDescription("Abbonamento per il corso di Yoga");
        courseMembership.setPrice(new BigDecimal("75.00"));
        courseMembership.setDurationInDays(30);
        courseMembership.setCourse(course);
        courseMembership.setWeeklyAccess(2);

        courseMembership = courseMembershipDAO.createCourseMembership(courseMembership);

        courseMembershipDAO.deleteCourseMembership(courseMembership.getId());
        int id = courseMembership.getId();
        assertThrows(DAOException.class, () -> {
            courseMembershipDAO.getCourseMembershipByID(id);
        });
    }

    @AfterAll
    static void tearDown() {
        DAOTestUtils.resetDatabase();
    }

}