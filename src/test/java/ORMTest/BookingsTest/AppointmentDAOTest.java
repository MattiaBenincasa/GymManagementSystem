package ORMTest.BookingsTest;

import BusinessLogic.DTOs.WeightRoomBookingInfo;
import BusinessLogic.Exceptions.DAOException;
import DomainModel.Bookings.Appointment;
import DomainModel.DailyEvents.TrainerAvailability;
import DomainModel.Membership.CustomerMembership;
import DomainModel.Membership.WRMembershipType;
import DomainModel.Membership.WeightRoomMembership;
import DomainModel.Users.Customer;
import DomainModel.Users.Trainer;
import ORM.Bookings.AppointmentDAO;
import ORM.DailyEvents.TrainerAvailabilityDAO;
import ORM.DiscountStrategy.DiscountsDAO;
import ORM.Membership.CourseDAO;
import ORM.Membership.CustomerMembershipDAO;
import ORM.Membership.MembershipDAO;
import ORM.Membership.WeightRoomMembershipDAO;
import ORM.Users.CustomerDAO;
import ORM.Users.TrainerDAO;
import ORM.Users.UserDAO;
import ORMTest.Users.UserDAOTestUtils;
import TestUtils.DAOTestUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentDAOTest {
    private AppointmentDAO appointmentDAO;
    private Trainer trainer;
    private Customer customer;
    private TrainerAvailabilityDAO trainerAvailabilityDAO;
    private CustomerMembershipDAO customerMembershipDAO;

    @BeforeEach
    void setup() {
        DAOTestUtils.resetDatabase();
        UserDAO userDAO = new UserDAO();
        CustomerDAO customerDAO = new CustomerDAO(userDAO);
        TrainerDAO trainerDAO = new TrainerDAO(userDAO);
        this.customerMembershipDAO = new CustomerMembershipDAO(new CourseDAO(trainerDAO), new MembershipDAO(new DiscountsDAO()));
        this.trainerAvailabilityDAO = new TrainerAvailabilityDAO(trainerDAO);
        this.appointmentDAO = new AppointmentDAO(this.trainerAvailabilityDAO, customerDAO);
        this.trainer = trainerDAO.createTrainer(UserDAOTestUtils.createTrainer("trainer", "trainer@trainer.it"));
        this.customer = customerDAO.createCustomer(UserDAOTestUtils.createCustomer("customer", "customer@customer.it"));
    }

    @Test
    void testCreateAppointment() {
        TrainerAvailability trainerAvailability = new TrainerAvailability(this.trainer);
        trainerAvailability.setDay(LocalDate.of(2025, 2, 2));
        trainerAvailability.setStartTime(LocalTime.of(9, 30));
        trainerAvailability.setEndTime(LocalTime.of(10, 30));
        trainerAvailability = this.trainerAvailabilityDAO.createTrainerAvailability(trainerAvailability);
        Appointment appointment = new Appointment(this.customer, trainerAvailability);
        appointment.setAppointmentPurpose("cambio scheda");
        this.appointmentDAO.createAppointment(appointment);
        Appointment retrieved = this.appointmentDAO.getAppointment(trainerAvailability.getId(), this.customer.getId());

        assertEquals(appointment.getAppointmentPurpose(), retrieved.getAppointmentPurpose());
        assertEquals(appointment.getTrainerAvailability().getId(), retrieved.getTrainerAvailability().getId());
        assertEquals(appointment.getCustomer().getId(), retrieved.getCustomer().getId());
    }

    @Test
    void testDeleteAppointment() {
        assertThrows(DAOException.class, ()->{
            TrainerAvailability trainerAvailability = new TrainerAvailability(this.trainer);
            trainerAvailability.setDay(LocalDate.of(2025, 2, 2));
            trainerAvailability.setStartTime(LocalTime.of(9, 30));
            trainerAvailability.setEndTime(LocalTime.of(10, 30));
            trainerAvailability = this.trainerAvailabilityDAO.createTrainerAvailability(trainerAvailability);
            Appointment appointment = new Appointment(this.customer, trainerAvailability);
            appointment.setAppointmentPurpose("cambio scheda");
            this.appointmentDAO.createAppointment(appointment);

            this.appointmentDAO.deleteAppointment(appointment);
           this.appointmentDAO.getAppointment(trainerAvailability.getId(), this.customer.getId());
        });
    }

    @Test
    void testWeightRoomBookingInfoRetrieving() {
        TrainerAvailability trainerAvailability = new TrainerAvailability(this.trainer);
        trainerAvailability.setDay(LocalDate.of(2025, 2, 2));
        trainerAvailability.setStartTime(LocalTime.of(9, 30));
        trainerAvailability.setEndTime(LocalTime.of(10, 30));
        trainerAvailability = this.trainerAvailabilityDAO.createTrainerAvailability(trainerAvailability);
        Appointment appointment = new Appointment(this.customer, trainerAvailability);
        appointment.setAppointmentPurpose("cambio scheda");

        // creating new membership
        WeightRoomMembershipDAO weightRoomMembershipDAO = new WeightRoomMembershipDAO(new MembershipDAO(new DiscountsDAO()));
        WeightRoomMembership weightRoomMembership = new WeightRoomMembership();
        weightRoomMembership.setName("Sala pesi");
        weightRoomMembership.setDescription("descrizione");
        weightRoomMembership.setType(WRMembershipType.BASE);
        weightRoomMembership.setDurationInDays(30);
        weightRoomMembership.setPrice(new BigDecimal("150.00"));

        weightRoomMembership = weightRoomMembershipDAO.createWeightRoomMembership(weightRoomMembership);
        CustomerMembership customerMembership = new CustomerMembership(LocalDate.now(), weightRoomMembership, this.customer);
        this.customerMembershipDAO.createCustomerMembership(customerMembership);

        WeightRoomBookingInfo weightRoomBookingInfo = this.appointmentDAO.getWeightRoomBookingInfo(customer, appointment.getTrainerAvailability().getDay());
        //no bookings
        assertEquals(customerMembership.getExpiryDate(), weightRoomBookingInfo.getMembershipExpiry());
        assertEquals(weightRoomMembership.getType(), weightRoomBookingInfo.getMembershipType());
        assertEquals(0, weightRoomBookingInfo.getCustomerMonthlyAppointmentsDone());

        //one booking
        this.appointmentDAO.createAppointment(appointment);
        weightRoomBookingInfo = this.appointmentDAO.getWeightRoomBookingInfo(customer, appointment.getTrainerAvailability().getDay());
        assertEquals(1, weightRoomBookingInfo.getCustomerMonthlyAppointmentsDone());


    }



    @AfterAll
    static void teardown(){
        DAOTestUtils.resetDatabase();
    }

}
