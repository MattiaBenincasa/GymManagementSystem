package ORMTest.Membership;

import DomainModel.Membership.CustomerMembership;
import DomainModel.Course;
import DomainModel.Membership.CourseMembership;
import DomainModel.Membership.WeightRoomMembership;
import DomainModel.Membership.WRMembershipType;
import DomainModel.Users.Customer;
import ORM.Membership.*;
import ORM.Users.CustomerDAO;
import ORM.Users.TrainerDAO;
import ORM.Users.UserDAO;
import ORMTest.Users.UserDAOTestUtils;
import TestUtils.DAOTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomerMembershipDAOTest {
    private CustomerMembershipDAO customerMembershipDAO;
    private CustomerDAO customerDAO;
    private WeightRoomMembershipDAO wrMembershipDAO;
    private CourseMembershipDAO courseMembershipDAO;
    private CourseDAO courseDAO;
    private TrainerDAO trainerDAO;
    private UserDAO userDAO;
    private MembershipDAO membershipDAO;

    @BeforeEach
    void setUp() {
        DAOTestUtils.resetDatabase();
        userDAO = new UserDAO();
        membershipDAO = new MembershipDAO();
        courseDAO = new CourseDAO(trainerDAO);
        customerMembershipDAO = new CustomerMembershipDAO(courseDAO, membershipDAO);
        customerDAO = new CustomerDAO(userDAO);
        wrMembershipDAO = new WeightRoomMembershipDAO(membershipDAO);
        courseMembershipDAO = new CourseMembershipDAO(membershipDAO, courseDAO);
        trainerDAO = new TrainerDAO(userDAO);
    }


    @Test
    void testAddOneMembershipToCustomer() {
        Customer customer = UserDAOTestUtils.createCustomer("customer", "customer@customer.it");
        int customerId = customerDAO.createCustomer(customer);
        customer = customerDAO.getCustomerByID(customerId);
        WeightRoomMembership wrMembership = new WeightRoomMembership();
        wrMembership.setName("abbonamento base");
        wrMembership.setPrice(new BigDecimal("300"));
        wrMembership.setType(WRMembershipType.PERSONAL);
        wrMembership.setDurationInDays(90);
        int wrMembershipId = wrMembershipDAO.createWeightRoomMembership(wrMembership);
        wrMembership = wrMembershipDAO.getWeightRoomMembershipByID(wrMembershipId);

        CustomerMembership customerMembership = new CustomerMembership(LocalDate.now(), wrMembership, customer);

        assertDoesNotThrow(() -> customerMembershipDAO.createCustomerMembership(customerMembership));

        List<CustomerMembership> retrievedMemberships = customerMembershipDAO.getAllCustomerMembership(customer);

        assertEquals(1, retrievedMemberships.size());
        CustomerMembership retrieved = retrievedMemberships.getFirst();

        assertEquals(customer.getId(), retrieved.getCustomer().getId());
        assertEquals(wrMembership.getId(), retrieved.getMembership().getId());
    }

    @Test
    void testAddTwoMembershipsToSameCustomer() {
        Customer customer = UserDAOTestUtils.createCustomer("customer", "email@email.it");
        int customerId = customerDAO.createCustomer(customer);
        customer = customerDAO.getCustomerByID(customerId);

        //membership 1
        WeightRoomMembership wrMembership = new WeightRoomMembership();
        wrMembership.setName("abbonamento base");
        wrMembership.setPrice(new BigDecimal("300"));
        wrMembership.setType(WRMembershipType.PERSONAL);
        wrMembership.setDurationInDays(90);
        int wrMembershipId = wrMembershipDAO.createWeightRoomMembership(wrMembership);
        wrMembership = wrMembershipDAO.getWeightRoomMembershipByID(wrMembershipId);

        //membership 2
        CourseMembership courseMembership = new CourseMembership();
        courseMembership.setName("Abbonamento Corso Yoga");
        courseMembership.setDescription("descrizione");
        courseMembership.setPrice(new BigDecimal("300"));
        courseMembership.setDurationInDays(90);

        Course course = new Course("corso Yoga", "Descrizione");
        int courseId = courseDAO.createCourse(course);
        course = courseDAO.getCourseByID(courseId);
        courseMembership.setCourse(course);
        int courseMembershipID = courseMembershipDAO.createCourseMembership(courseMembership);
        courseMembership = courseMembershipDAO.getCourseMembershipByID(courseMembershipID);

        CustomerMembership wrCustomerMembership = new CustomerMembership(LocalDate.now(), wrMembership, customer);
        CustomerMembership courseCustomerMembership = new CustomerMembership(LocalDate.now(), courseMembership, customer);
        customerMembershipDAO.createCustomerMembership(wrCustomerMembership);
        customerMembershipDAO.createCustomerMembership(courseCustomerMembership);

        ArrayList<CustomerMembership> retrievedMemberships = customerMembershipDAO.getAllCustomerMembership(customer);

        assertEquals(2, retrievedMemberships.size());

        assertEquals(wrMembershipId, retrievedMemberships.getFirst().getMembership().getId());
        assertEquals(courseMembershipID, retrievedMemberships.get(1).getMembership().getId());
    }
}
