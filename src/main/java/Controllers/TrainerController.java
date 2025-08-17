package Controllers;

import BusinessLogic.AuthService.Session;
import BusinessLogic.Bookings.AppointmentTrainerBookingService;
import BusinessLogic.DailyEvents.DailyClassService;
import BusinessLogic.DailyEvents.TrainerAvailabilityService;
import BusinessLogic.Users.TrainerService;
import DomainModel.Bookings.Appointment;
import DomainModel.DailyEvents.TrainerAvailability;
import DomainModel.Users.Trainer;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class TrainerController {
    private final TrainerService trainerService;
    private final TrainerAvailabilityService trainerAvailabilityService;
    private final DailyClassService dailyClassService;
    private final AppointmentTrainerBookingService appointmentTrainerBookingService;
    private Session session;

    public TrainerController(TrainerService trainerService, TrainerAvailabilityService trainerAvailabilityService, DailyClassService dailyClassService, AppointmentTrainerBookingService appointmentTrainerBookingService) {
        this.trainerService = trainerService;
        this.trainerAvailabilityService = trainerAvailabilityService;
        this.dailyClassService = dailyClassService;
        this.appointmentTrainerBookingService = appointmentTrainerBookingService;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public void changePersonalInfo(String username, String name, String surname, String mail, String phoneNumber) {
        Session.validateSession(this.session);
        trainerService.changeTrainerInfo(this.session.getUserID(), username, name, surname, mail, phoneNumber);
    }

    public void changePassword(String oldPassword, String newPassword) {
        Session.validateSession(this.session);
        trainerService.changePassword(this.session.getUserID(), oldPassword, newPassword);
    }

    // Metodi da DailyClassService
    public void cancelDailyClass(int dailyClassID) {
        Session.validateSession(this.session);
        dailyClassService.cancelDailyClass(dailyClassID);
    }

    // Metodi da TrainerAvailabilityService
    public TrainerAvailability addTrainerAvailability(LocalTime startTime, LocalTime endTime, LocalDate day) {
        Session.validateSession(this.session);
        return trainerAvailabilityService.addtrainerAvailability(this.session.getUserID(), startTime, endTime, day);
    }

    public ArrayList<TrainerAvailability> addWeeklyTrainerAvailabilities(DayOfWeek dayOfWeek, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        Session.validateSession(this.session);
        return this.trainerAvailabilityService.addWeeklyTrainerAvailabilities(dayOfWeek, startDate, endDate, this.session.getUserID(), startTime, endTime);
    }

    public void deleteTrainerAvailability(int availabilityId) {
        Session.validateSession(this.session);
        this.trainerAvailabilityService.deleteTrainerAvailability(this.session.getUserID(), availabilityId);
    }

    public ArrayList<TrainerAvailability> getTrainerAvailabilities(int trainerId) {
        Session.validateSession(this.session);
        return trainerAvailabilityService.getAvailabilitiesByTrainerID(trainerId);
    }

    public ArrayList<TrainerAvailability> getAvailabilitiesOfAllTrainers() {
        Session.validateSession(this.session);
        return trainerAvailabilityService.getAvailabilitiesOfAllTrainers();
    }

    public Trainer getPersonalInfo() {
        Session.validateSession(this.session);
        return trainerService.getTrainerByID(this.session.getUserID());
    }

    public ArrayList<Appointment> getAllAppointments() {
        Session.validateSession(this.session);
        return this.appointmentTrainerBookingService.getAllDailyAppointment(this.session.getUserID());
    }
}
