package ORMTest.BookingsTest;

import DTOs.ClassBookingInfo;
import DomainModel.Bookings.Booking;
import DomainModel.DailyEvents.DailyClass;
import DomainModel.Membership.*;
import DomainModel.Users.Customer;
import DomainModel.Users.Trainer;
import ORM.Bookings.BookingDAO;
import ORM.DailyEvents.DailyClassDAO;
import ORM.DiscountsDAO;
import ORM.Membership.CourseDAO;
import ORM.Membership.CourseMembershipDAO;
import ORM.Membership.CustomerMembershipDAO;
import ORM.Membership.MembershipDAO;
import ORM.Users.CustomerDAO;
import ORM.Users.TrainerDAO;
import ORM.Users.UserDAO;
import ORMTest.Users.UserDAOTestUtils;
import TestUtils.DAOTestUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class BookingDAOTest {
    private Customer customer;
    private DailyClass dailyClass;
    private BookingDAO bookingDAO;
    private Course course;
    private CourseMembershipDAO courseMembershipDAO;
    private CustomerMembershipDAO customerMembershipDAO;
    private DailyClass anotherClass;

    @BeforeEach
    void setup() {
        DAOTestUtils.resetDatabase();
        UserDAO userDAO = new UserDAO();
        TrainerDAO trainerDAO = new TrainerDAO(userDAO);
        CustomerDAO customerDAO = new CustomerDAO(userDAO);
        CourseDAO courseDAO = new CourseDAO(new TrainerDAO(userDAO));
        DiscountsDAO discountsDAO = new DiscountsDAO();
        MembershipDAO membershipDAO = new MembershipDAO(discountsDAO);
        this.courseMembershipDAO = new CourseMembershipDAO(membershipDAO, courseDAO);
        this.customerMembershipDAO = new CustomerMembershipDAO(courseDAO, membershipDAO);
        DailyClassDAO dailyClassDAO = new DailyClassDAO(userDAO);
        this.bookingDAO = new BookingDAO(dailyClassDAO, new CustomerDAO(userDAO));
        //init customer
        this.customer = customerDAO.createCustomer(UserDAOTestUtils.createCustomer("Customer", "customer@customer.it"));

        //init course
        this.course = new Course("Crossfit", "Corso crossfit");
        Trainer coach = trainerDAO.createTrainer(UserDAOTestUtils.createTrainer("crossfit coach", "mail@mail.it"));
        this.course.addTrainer(coach);
        this.course = courseDAO.createCourse(course);

        //init dailyClass
        this.dailyClass = new DailyClass.Builder()
                .course(this.course)
                .coach(coach)
                .day(LocalDate.of(2025, 1, 1))
                .startTime(LocalTime.of(18, 30))
                .endTime(LocalTime.of(19, 30))
                .maxParticipants(12)
                .build();

        this.anotherClass = new DailyClass.Builder()
                .course(this.course)
                .coach(coach)
                .day(LocalDate.of(2025, 1, 3))
                .startTime(LocalTime.of(18, 30))
                .endTime(LocalTime.of(19, 30))
                .maxParticipants(12)
                .build();

        this.dailyClass = dailyClassDAO.createDailyClass(this.dailyClass);
        this.anotherClass = dailyClassDAO.createDailyClass(this.anotherClass);
    }

    @Test
    void testCreateBooking() {
        Booking booking = new Booking(this.customer, this.dailyClass);
        this.bookingDAO.createBooking(booking);

        List<Booking> customerBookings = this.bookingDAO.getAllCustomerBookings(this.customer.getId());

        assertEquals(1, customerBookings.size());
        assertEquals(this.customer.getId(), customerBookings.getFirst().getCustomer().getId());
        assertEquals(this.customer.getId(), customerBookings.getFirst().getDailyClass().getId());
    }

    @Test
    void testDeleteBooking() {
        Booking booking = new Booking(this.customer, this.dailyClass);
        this.bookingDAO.createBooking(booking);

        this.bookingDAO.deleteBooking(booking);
        List<Booking> customerBookings = this.bookingDAO.getAllCustomerBookings(this.customer.getId());

        assertEquals(0, customerBookings.size());
    }

    @Test
    void testClassBookingInfoRetrieving() {
        CourseMembership courseMembership = new CourseMembership();
        courseMembership.setName("Abbonamento crossfit");
        courseMembership.setDescription("abbonamento 3 mesi");
        courseMembership.setDurationInDays(90);
        courseMembership.setPrice(new BigDecimal("250"));
        courseMembership.setWeeklyAccess(2);
        courseMembership.setCourse(this.course);
        courseMembership = this.courseMembershipDAO.createCourseMembership(courseMembership);

        //associate membership to customer
        CustomerMembership customerMembership = new CustomerMembership(LocalDate.now(), courseMembership, customer);
        this.customerMembershipDAO.createCustomerMembership(customerMembership);
        ClassBookingInfo classBookingInfo = this.bookingDAO.getClassBookingInfo(this.customer, this.dailyClass);
        assertEquals(customerMembership.getExpiryDate(), classBookingInfo.getMembershipExpiry());
        assertEquals(this.dailyClass.getMaxParticipants(), classBookingInfo.getClassMaxParticipants());
        assertEquals(0, classBookingInfo.getTotalBookingsInClass());
        assertEquals(courseMembership.getWeeklyAccess(), classBookingInfo.getWeeklyBookingLimit());
        assertEquals(0, classBookingInfo.getBookingsDoneByCustomerThisWeek());

        //new booking
        Booking booking = new Booking(this.customer, this.dailyClass);
        this.bookingDAO.createBooking(booking);
        ClassBookingInfo classBookingInfoAfterBooking = this.bookingDAO.getClassBookingInfo(this.customer, this.dailyClass);
        assertEquals(1, classBookingInfoAfterBooking.getTotalBookingsInClass());
        assertEquals(1, classBookingInfoAfterBooking.getBookingsDoneByCustomerThisWeek());

        //new booking
        Booking anotherBooking = new Booking(this.customer, this.anotherClass);
        this.bookingDAO.createBooking(anotherBooking);
        ClassBookingInfo classBookingAfterSecondBooking = this.bookingDAO.getClassBookingInfo(this.customer, this.anotherClass);
        assertEquals(1, classBookingAfterSecondBooking.getTotalBookingsInClass());
        assertEquals(2, classBookingAfterSecondBooking.getBookingsDoneByCustomerThisWeek());

        //another customer book dailyClass
        CustomerDAO customerDAO = new CustomerDAO(new UserDAO());
        Customer anotherCustomer = customerDAO.createCustomer(UserDAOTestUtils.createCustomer("newCustomer", "new.customer@cust.it"));
        Booking anotherCustomerBooking = new Booking(anotherCustomer, this.dailyClass);
        this.bookingDAO.createBooking(anotherCustomerBooking);
        ClassBookingInfo classBookingInfoOfAnotherCustomer = this.bookingDAO.getClassBookingInfo(anotherCustomer, this.dailyClass);
        classBookingInfo = this.bookingDAO.getClassBookingInfo(this.customer, this.dailyClass); //update booking info of the first customer
        assertEquals(2, classBookingInfoOfAnotherCustomer.getTotalBookingsInClass());
        assertEquals(2, classBookingInfo.getTotalBookingsInClass());
        assertEquals(1, classBookingInfoOfAnotherCustomer.getBookingsDoneByCustomerThisWeek());
    }

    @AfterAll
    static void teardown() {
        DAOTestUtils.resetDatabase();
    }
}
