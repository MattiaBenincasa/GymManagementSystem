package Controllers.Customer;

import BusinessLogic.Bookings.AppointmentTrainerBookingService;
import BusinessLogic.Bookings.ClassBookingService;
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

    public CustomerController(ActivationMembershipService activationMembershipService, ClassBookingService classBookingService, CustomerService customerService, AppointmentTrainerBookingService appointmentTrainerBookingService) {
        this.customerService = customerService;
        this.activationMembershipService = activationMembershipService;
        this.appointmentTrainerBookingService = appointmentTrainerBookingService;
        this.classBookingService = classBookingService;
    }


    public Customer createCustomer(String username, String password, String email) {
        return customerService.createCustomer(username, password, email);
    }

    public void changeCustomerInfo(int customerID, String username, String name, String surname, String mail, String phoneNumber) {
        customerService.changeCustomerInfo(customerID, username, name, surname, mail, phoneNumber);
    }

    public void changePassword(int customerID, String oldPassword, String newPassword) {
        customerService.changePassword(customerID, oldPassword, newPassword);
    }

    public ArrayList<CustomerMembership> getAllCustomerMembership(int customerID) {
        return this.activationMembershipService.getAllCustomerMembership(customerID);
    }

    public LocalDate getActiveCustomerFeeExpiry(int customerID) {
        return this.activationMembershipService.getActiveCustomerFeeExpiry(customerID);
    }

    public void bookAClass(Customer customer, DailyClass dailyClass) {
        this.classBookingService.bookAClass(customer, dailyClass);
    }

    public void deleteClassBooking(Booking booking) {
        this.classBookingService.deleteClassBooking(booking);
    }

    public List<Booking> getAllCustomerBookings(int customerID) {
        return this.classBookingService.getAllCustomerBookings(customerID);
    }

    public void takeAppointmentWithTrainer(Customer customer, TrainerAvailability trainerAvailability) {
        this.appointmentTrainerBookingService.takeAppointmentWithTrainer(customer, trainerAvailability);
    }

    public void deleteAnAppointmentWithTrainer(Appointment appointment) {
        this.appointmentTrainerBookingService.deleteAnAppointmentWithTrainer(appointment);
    }

    public void changeNotesForTrainer(String newNotes, Appointment appointment) {
        this.appointmentTrainerBookingService.changeNotesForTrainer(newNotes, appointment);
    }

    public ArrayList<Appointment> getAllDailyAppointment(int trainerID) {
        return appointmentTrainerBookingService.getAllDailyAppointment(trainerID);
    }

}
