package BusinessLogic.Bookings;

import BusinessLogic.DTOs.CustomerInfo;
import BusinessLogic.DTOs.WeightRoomBookingInfo;
import Exceptions.LateCancellationException;
import BusinessLogic.Validators.*;
import DomainModel.Bookings.Appointment;
import DomainModel.DailyEvents.TrainerAvailability;
import DomainModel.Users.Customer;
import ORM.Bookings.AppointmentDAO;
import ORM.Users.CustomerDAO;
import ORM.Users.UserDAO;

import java.time.LocalTime;
import java.util.ArrayList;

public class AppointmentTrainerBookingService {
    private final UserDAO userDAO;
    private final AppointmentDAO appointmentDAO;
    private final CustomerDAO customerDAO;

    public AppointmentTrainerBookingService(UserDAO userDAO, AppointmentDAO appointmentDAO, CustomerDAO customerDAO) {
        this.userDAO = userDAO;
        this.appointmentDAO = appointmentDAO;
        this.customerDAO = customerDAO;
    }

    /*
     * execute a sequence of validators to check if a specific customer can book a
     * specific appointment. If all validators pass then instantiate an Appointment and save
     * it with AppointmentDAO
     * */
    public void takeAppointmentWithTrainer(int customerID, TrainerAvailability trainerAvailability) {
        Customer customer = this.customerDAO.getCustomerByID(customerID);
        CustomerInfo customerInfo = userDAO.getCustomerBookingInfo(customer);
        WeightRoomBookingInfo weightRoomBookingInfo = appointmentDAO.getWeightRoomBookingInfo(customer, trainerAvailability.getDay());
        Validator appointmentTrainerValidator = new FeeValidator(customerInfo)
                .setNextValidator(new MedCertificateValidator(customerInfo))
                .setNextValidator(new MembershipValidator(weightRoomBookingInfo.getMembershipExpiry()))
                .setNextValidator(new WeightRoomBookingValidator(weightRoomBookingInfo));

        appointmentTrainerValidator.validate();

        Appointment appointment = new Appointment(customer, trainerAvailability);
        this.appointmentDAO.createAppointment(appointment);
    }

    // A customer can delete an appointment no later than one hour before the start time.
    public void deleteAnAppointmentWithTrainer(Appointment appointment) {
        LocalTime startTime = appointment.getTrainerAvailability().getStartTime();

        if (LocalTime.now().plusHours(1).isAfter(startTime))
            throw new LateCancellationException("A booking can be deleted no later than one hour before the class starts.");

        this.appointmentDAO.deleteAppointment(appointment);
    }

    public void changeNotesForTrainer(String newNotes, Appointment appointment) {
        appointment.setAppointmentPurpose(newNotes);
        this.appointmentDAO.updateAppointment(appointment);
    }

    public ArrayList<Appointment> getAllDailyAppointment(int trainerID) {
        return this.appointmentDAO.getAllDailyAppointment(trainerID);
    }

    public ArrayList<Appointment> getAllCustomerDailyAppointment(int customerID) {
        return this.appointmentDAO.getAllCustomerDailyAppointment(customerID);
    }
}
