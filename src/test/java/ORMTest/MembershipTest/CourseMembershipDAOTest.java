package ORMTest.MembershipTest;

import Exceptions.DAOException;
import DomainModel.DiscountStrategy.FixedDiscount;
import DomainModel.DiscountStrategy.PercentageDiscount;
import DomainModel.Membership.Course;
import DomainModel.Membership.CourseMembership;
import ORM.DiscountsDAO;
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
    private DiscountsDAO discountsDAO;
    private Course course;
    private CourseMembership courseMembership;

    @BeforeEach
    void setUp() {
        DAOTestUtils.resetDatabase();
        this.discountsDAO = new DiscountsDAO();
        this.trainerDAO = new TrainerDAO(new UserDAO());
        this.courseDAO = new CourseDAO(this.trainerDAO);
        this.courseMembershipDAO = new CourseMembershipDAO(new MembershipDAO(this.discountsDAO), this.courseDAO);
        this.course = this.courseDAO.createCourse(new Course("Corso di Yoga", "Descrizione corso"));
        this.courseMembership = new CourseMembership();
        this.courseMembership.setName("Abbonamento Yoga");
        this.courseMembership.setDescription("Abbonamento per il corso di Yoga");
        this.courseMembership.setPrice(new BigDecimal("75.00"));
        this.courseMembership.setDurationInDays(30);
        this.courseMembership.setCourse(course);
        this.courseMembership.setWeeklyAccess(2);
    }

    @Test
    void testCreateCourseMembership() {
        assertDoesNotThrow(() -> {
            this.courseMembership = courseMembershipDAO.createCourseMembership(courseMembership);
            CourseMembership retrieved = courseMembershipDAO.getCourseMembershipByID(this.courseMembership.getId());
            assertEquals(this.courseMembership.getId(), retrieved.getId());
            assertEquals(this.courseMembership.getName(), retrieved.getName());
            assertEquals(this.courseMembership.getDescription(), retrieved.getDescription());
            assertEquals(this.courseMembership.getCourse().getId(), retrieved.getCourse().getId());
            assertEquals(this.courseMembership.getCourse().getName(), retrieved.getCourse().getName());
            assertEquals(this.courseMembership.getCourse().getDescription(), retrieved.getCourse().getDescription());

        });
    }

    @Test
    void testUpdateCourseMembership() {
        this.courseMembership = this.courseMembershipDAO.createCourseMembership(this.courseMembership);

        Course course2 = courseDAO.createCourse(new Course("Pilates", "nuova descrizione"));

        this.courseMembership.setName("Abbonamento Pilates Updated");
        this.courseMembership.setDescription("Descrizione Updated");
        this.courseMembership.setPrice(new BigDecimal("85.00"));
        this.courseMembership.setCourse(course2);
        this.courseMembership.setWeeklyAccess(3);
        courseMembershipDAO.updateCourseMembership(this.courseMembership);

        CourseMembership retrieved = courseMembershipDAO.getCourseMembershipByID(this.courseMembership.getId());
        assertEquals("Abbonamento Pilates Updated", retrieved.getName());
        assertEquals("Descrizione Updated", retrieved.getDescription());
        assertEquals(new BigDecimal("85.00"), retrieved.getPrice());
        assertEquals(3, retrieved.getWeeklyAccess());
        assertEquals(course2.getId(), retrieved.getCourse().getId());

        //creating discounts and adding them to membership
        PercentageDiscount percentageDiscount = new PercentageDiscount(10,
                true,
                "percentage discount");

        FixedDiscount fixedDiscount = new FixedDiscount(30,
                false,
                "fixed discount");
        int idPercentage = this.discountsDAO.createDiscount(percentageDiscount);
        int idFixed = this.discountsDAO.createDiscount(fixedDiscount);
        this.courseMembership.addDiscount(this.discountsDAO.getDiscountByID(idPercentage));
        this.courseMembership.addDiscount(this.discountsDAO.getDiscountByID(idFixed));

        this.courseMembershipDAO.updateCourseMembership(this.courseMembership);
        retrieved = this.courseMembershipDAO.getCourseMembershipByID(this.courseMembership.getId());
        assertEquals(this.courseMembership.getDiscounts().getFirst().getId(),
                retrieved.getDiscounts().getFirst().getId());

        assertEquals(this.courseMembership.getDiscounts().get(1).getId(),
                retrieved.getDiscounts().get(1).getId());

    }

    @Test
    void testDeleteCourseMembership() throws DAOException {
        this.courseMembership = this.courseMembershipDAO.createCourseMembership(courseMembership);
        this.courseMembershipDAO.deleteCourseMembership(courseMembership.getId());

        assertThrows(DAOException.class, () -> {
            courseMembershipDAO.getCourseMembershipByID(this.courseMembership.getId());
        });
    }

    @AfterAll
    static void tearDown() {
        DAOTestUtils.resetDatabase();
    }

}