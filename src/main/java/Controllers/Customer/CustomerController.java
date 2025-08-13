package Controllers.Customer;

import BusinessLogic.AuthService.Session;
import BusinessLogic.Bookings.AppointmentTrainerBookingService;
import BusinessLogic.Bookings.ClassBookingService;
import BusinessLogic.DailyEvents.TrainerAvailabilityService;
import BusinessLogic.Memberships.ActivationMembershipService;
import BusinessLogic.Users.CustomerService;
import DomainModel.Bookings.Appointment;
import DomainModel.Bookings.Booking;
import DomainModel.DailyEvents.DailyClass;
import DomainModel.DailyEvents.TrainerAvailability;
import DomainModel.Membership.CustomerMembership;
import DomainModel.Users.Customer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CustomerController {
    private final CustomerService customerService;
    private final ActivationMembershipService activationMembershipService;
    private final ClassBookingService classBookingService;
    private final AppointmentTrainerBookingService appointmentTrainerBookingService;
    private final TrainerAvailabilityService trainerAvailabilityService;
    private Session session;

    public CustomerController(ActivationMembershipService activationMembershipService, ClassBookingService classBookingService, CustomerService customerService, AppointmentTrainerBookingService appointmentTrainerBookingService, TrainerAvailabilityService trainerAvailabilityService) {
        this.customerService = customerService;
        this.activationMembershipService = activationMembershipService;
        this.appointmentTrainerBookingService = appointmentTrainerBookingService;
        this.classBookingService = classBookingService;
        this.trainerAvailabilityService = trainerAvailabilityService;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Customer createCustomer(String username, String password, String email) {
        return customerService.createCustomer(username, password, email);
    }

    public void changePersonalInfo(String username, String name, String surname, String mail, String phoneNumber) {
        Session.validateSession(this.session);
        customerService.changeCustomerInfo(this.session.getUserID(), username, name, surname, mail, phoneNumber);
    }

    public void changePassword(String oldPassword, String newPassword) {
        Session.validateSession(this.session);
        customerService.changePassword(this.session.getUserID(), oldPassword, newPassword);
    }

    public ArrayList<CustomerMembership> getAllCustomerMembership() {
        Session.validateSession(session);
        return this.activationMembershipService.getAllCustomerMembership(this.session.getUserID());
    }

    public LocalDate getActiveCustomerFeeExpiry() {
        Session.validateSession(session);
        return this.activationMembershipService.getActiveCustomerFeeExpiry(this.session.getUserID());
    }

    public void bookAClass(DailyClass dailyClass) {
        Session.validateSession(session);
        this.classBookingService.bookAClass(this.session.getUserID(), dailyClass);
    }

    public void deleteClassBooking(Booking booking) {
        Session.validateSession(session);
        this.classBookingService.deleteClassBooking(booking);
    }

    public List<Booking> getAllCustomerBookings() {
        Session.validateSession(session);
        return this.classBookingService.getAllCustomerBookings(this.session.getUserID());
    }

    public void takeAppointmentWithTrainer(TrainerAvailability trainerAvailability) {
        Session.validateSession(session);
        this.appointmentTrainerBookingService.takeAppointmentWithTrainer(this.session.getUserID(), trainerAvailability);
    }

    public void deleteAnAppointmentWithTrainer(Appointment appointment) {
        Session.validateSession(session);
        this.appointmentTrainerBookingService.deleteAnAppointmentWithTrainer(appointment);
    }

    public void changeNotesForTrainer(String newNotes, Appointment appointment) {
        Session.validateSession(session);
        this.appointmentTrainerBookingService.changeNotesForTrainer(newNotes, appointment);
    }

    public ArrayList<TrainerAvailability> getAllTrainerAvailabilitiesByTrainerID(int trainerID) {
        return trainerAvailabilityService.getAvailabilitiesByTrainerID(trainerID);
    }

    public ArrayList<Appointment> getAllCustomerAppointment() {
        return this.appointmentTrainerBookingService.getAllCustomerDailyAppointment(this.session.getUserID());
    }
}
