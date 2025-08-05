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
        courseMembershipDAO = new CourseMembershipDAO(new MembershipDAO(), courseDAO);
    }

    @Test
    void testCreateCourseMembership() throws DAOException {
        Course course = new Course("Corso di Yoga", "Descrizione corso");
        int courseId = courseDAO.createCourse(course);

        CourseMembership courseMembership = new CourseMembership();
        courseMembership.setName("Abbonamento Yoga");
        courseMembership.setDescription("Abbonamento per il corso di Yoga");
        courseMembership.setPrice(new BigDecimal("75.00"));
        courseMembership.setDurationInDays(30);
        courseMembership.setCourse(courseDAO.getCourseByID(courseId));
        courseMembership.setWeeklyAccess(2);

        assertDoesNotThrow(() -> {
            int id = courseMembershipDAO.createCourseMembership(courseMembership);
            courseMembershipDAO.getCourseMembershipByID(id);
        });
    }

    @Test
    void testUpdateCourseMembership() throws DAOException {
        Course course1 = new Course("Corso di Yoga", "Descrizione corso");
        int course1Id = courseDAO.createCourse(course1);

        CourseMembership courseMembership = new CourseMembership();
        courseMembership.setName("Abbonamento Yoga");
        courseMembership.setDescription("Abbonamento per il corso di Yoga");
        courseMembership.setPrice(new BigDecimal("75.00"));
        courseMembership.setDurationInDays(30);
        courseMembership.setCourse(courseDAO.getCourseByID(course1Id));
        courseMembership.setWeeklyAccess(2);

        int id = courseMembershipDAO.createCourseMembership(courseMembership);

        Course course2 = new Course("Pilates", "nuova descrizione");
        int course2Id = courseDAO.createCourse(course2);

        courseMembership = courseMembershipDAO.getCourseMembershipByID(id);
        courseMembership.setName("Abbonamento Pilates Updated");
        courseMembership.setDescription("Descrizione Updated");
        courseMembership.setPrice(new BigDecimal("85.00"));
        courseMembership.setCourse(courseDAO.getCourseByID(course2Id));
        courseMembership.setWeeklyAccess(3);
        courseMembershipDAO.updateCourseMembership(courseMembership);

        CourseMembership retrieved = courseMembershipDAO.getCourseMembershipByID(id);
        assertEquals("Abbonamento Pilates Updated", retrieved.getName());
        assertEquals("Descrizione Updated", retrieved.getDescription());
        assertEquals(new BigDecimal("85.00"), retrieved.getPrice());
        assertEquals(3, retrieved.getWeeklyAccess());
        assertEquals(course2Id, retrieved.getCourse().getId());
    }

    @Test
    void testDeleteCourseMembership() throws DAOException {

        Course course1 = new Course("Corso di Yoga", "Descrizione corso");
        int course1Id = courseDAO.createCourse(course1);

        CourseMembership courseMembership = new CourseMembership();
        courseMembership.setName("Abbonamento Yoga");
        courseMembership.setDescription("Abbonamento per il corso di Yoga");
        courseMembership.setPrice(new BigDecimal("75.00"));
        courseMembership.setDurationInDays(30);
        courseMembership.setCourse(courseDAO.getCourseByID(course1Id));
        courseMembership.setWeeklyAccess(2);

        int id = courseMembershipDAO.createCourseMembership(courseMembership);

        courseMembershipDAO.deleteCourseMembership(id);

        assertThrows(DAOException.class, () -> {
            courseMembershipDAO.getCourseMembershipByID(id);
        });
    }
}