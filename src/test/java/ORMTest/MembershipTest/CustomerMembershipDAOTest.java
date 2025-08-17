package ORMTest.MembershipTest;

import DomainModel.Membership.CustomerMembership;
import DomainModel.Membership.Course;
import DomainModel.Membership.CourseMembership;
import DomainModel.Membership.WeightRoomMembership;
import DomainModel.Membership.WRMembershipType;
import DomainModel.Users.Customer;
import ORM.DiscountsDAO;
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
    private WeightRoomMembershipDAO wrMembershipDAO;
    private CourseMembershipDAO courseMembershipDAO;

    private Customer customer;
    private WeightRoomMembership wrMembership;
    private CourseMembership courseMembership;

    @BeforeEach
    void setUp() {
        DAOTestUtils.resetDatabase();
        MembershipDAO membershipDAO = new MembershipDAO(new DiscountsDAO());
        UserDAO userDAO = new UserDAO();
        TrainerDAO trainerDAO = new TrainerDAO(userDAO);
        CourseDAO courseDAO = new CourseDAO(trainerDAO);
        this.customerMembershipDAO = new CustomerMembershipDAO(courseDAO, membershipDAO);
        CustomerDAO customerDAO = new CustomerDAO(userDAO);
        this.wrMembershipDAO = new WeightRoomMembershipDAO(membershipDAO);
        this.courseMembershipDAO = new CourseMembershipDAO(membershipDAO, courseDAO);

        //creating customers and memberships
        this.customer = customerDAO.createCustomer(UserDAOTestUtils.createCustomer("customer", "customer@customer.it"));
        this.wrMembership = new WeightRoomMembership();
        this.wrMembership.setName("abbonamento base");
        this.wrMembership.setPrice(new BigDecimal("300"));
        this.wrMembership.setType(WRMembershipType.PERSONAL);
        this.wrMembership.setDurationInDays(90);

        this.courseMembership = new CourseMembership();
        this.courseMembership.setName("Abbonamento Corso Yoga");
        this.courseMembership.setDescription("descrizione");
        this.courseMembership.setPrice(new BigDecimal("300"));
        this.courseMembership.setDurationInDays(90);
        Course course = courseDAO.createCourse(new Course("corso Yoga", "Descrizione"));
        this.courseMembership.setCourse(course);
        this.courseMembership = courseMembershipDAO.createCourseMembership(courseMembership);

    }


    @Test
    void testAddOneMembershipToCustomer() {
        this.wrMembership = this.wrMembershipDAO.createWeightRoomMembership(this.wrMembership);
        CustomerMembership customerMembership = new CustomerMembership(LocalDate.now(), this.wrMembership, this.customer);
        assertDoesNotThrow(() -> this.customerMembershipDAO.createCustomerMembership(customerMembership));

        List<CustomerMembership> retrievedMemberships = customerMembershipDAO.getAllCustomerMembership(customer);

        assertEquals(1, retrievedMemberships.size());
        CustomerMembership retrieved = retrievedMemberships.getFirst();

        assertEquals(customer.getId(), retrieved.getCustomer().getId());
        assertEquals(wrMembership.getId(), retrieved.getMembership().getId());
    }

    @Test
    void testAddTwoMembershipsToSameCustomer() {
        this.courseMembership = this.courseMembershipDAO.createCourseMembership(this.courseMembership);
        this.wrMembership = this.wrMembershipDAO.createWeightRoomMembership(this.wrMembership);
        CustomerMembership wrCustomerMembership = new CustomerMembership(LocalDate.now(), this.wrMembership, this.customer);
        CustomerMembership courseCustomerMembership = new CustomerMembership(LocalDate.now(), this.courseMembership, this.customer);
        customerMembershipDAO.createCustomerMembership(wrCustomerMembership);
        customerMembershipDAO.createCustomerMembership(courseCustomerMembership);

        ArrayList<CustomerMembership> retrievedMemberships = customerMembershipDAO.getAllCustomerMembership(customer);

        assertEquals(2, retrievedMemberships.size());

        assertEquals(wrMembership.getId(), retrievedMemberships.getFirst().getMembership().getId());
        assertEquals(courseMembership.getId(), retrievedMemberships.get(1).getMembership().getId());
    }
}
