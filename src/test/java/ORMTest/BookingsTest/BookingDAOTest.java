package ORMTest.BookingsTest;

import DomainModel.DailyEvents.DailyClass;
import DomainModel.Membership.*;
import DomainModel.Users.Customer;
import ORM.DiscountStrategy.DiscountsDAO;
import ORM.Membership.CourseDAO;
import ORM.Membership.CourseMembershipDAO;
import ORM.Membership.CustomerMembershipDAO;
import ORM.Membership.MembershipDAO;
import ORM.Users.TrainerDAO;
import ORM.Users.UserDAO;
import ORMTest.Users.UserDAOTestUtils;
import TestUtils.DAOTestUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BookingDAOTest {
    private Customer customer;
    private DailyClass dailyClass;

    @BeforeEach
    void setup() {
        DAOTestUtils.resetDatabase();
        CourseDAO courseDAO = new CourseDAO(new TrainerDAO(new UserDAO()));
        DiscountsDAO discountsDAO = new DiscountsDAO();
        MembershipDAO membershipDAO = new MembershipDAO(discountsDAO);
        CourseMembershipDAO courseMembershipDAO = new CourseMembershipDAO(membershipDAO, courseDAO);
        CustomerMembershipDAO customerMembershipDAO = new CustomerMembershipDAO(courseDAO, membershipDAO);

        //init customer
        this.customer = UserDAOTestUtils.createCustomer("Customer", "customer@customer.it");

        //init membership
        Course course = new Course("Crossfit", "Corso crossfit");
        course = courseDAO.createCourse(course);
        CourseMembership courseMembership = new CourseMembership();
        courseMembership.setName("Abbonamento crossfit");
        courseMembership.setDescription("abbonamento 3 mesi");
        courseMembership.setDurationInDays(90);
        courseMembership.setPrice(new BigDecimal("250"));
        courseMembership.setWeeklyAccess(2);
        courseMembership.setCourse(course);
        courseMembershipDAO.createCourseMembership(courseMembership);

        //customer has membership courseMembership
        CustomerMembership customerMembership = new CustomerMembership(LocalDate.now(), courseMembership, customer);
        customerMembershipDAO.createCustomerMembership(customerMembership);
    }


    @AfterAll
    static void teardown() {
        DAOTestUtils.resetDatabase();
    }
}
