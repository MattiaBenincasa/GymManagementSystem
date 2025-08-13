package Controllers.Trainer;

import BusinessLogic.DailyEvents.DailyClassService;
import BusinessLogic.DailyEvents.TrainerAvailabilityService;
import BusinessLogic.Users.TrainerService;
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

    public TrainerController(TrainerService trainerService, TrainerAvailabilityService trainerAvailabilityService, DailyClassService dailyClassService) {
        this.trainerService = trainerService;
        this.trainerAvailabilityService = trainerAvailabilityService;
        this.dailyClassService = dailyClassService;
    }

    public Trainer createTrainer(String username, String password, String name, String surname, String email, String phoneNumber, LocalDate birthDate) {
        return trainerService.createTrainer(username, password, name, surname, email, phoneNumber, birthDate);
    }

    public void changeTrainerInfo(int trainerID, String username, String name, String surname, String mail, String phoneNumber) {
        trainerService.changeTrainerInfo(trainerID, username, name, surname, mail, phoneNumber);
    }

    public void changePassword(int trainerID, String oldPassword, String newPassword) {
        trainerService.changePassword(trainerID, oldPassword, newPassword);
    }

    // Metodi da DailyClassService
    public void cancelDailyClass(int dailyClassID) {
        dailyClassService.cancelDailyClass(dailyClassID);
    }

    // Metodi da TrainerAvailabilityService
    public TrainerAvailability addTrainerAvailability(int trainerId, LocalTime startTime, LocalTime endTime, LocalDate day) {
        return trainerAvailabilityService.addtrainerAvailability(trainerId, startTime, endTime, day);
    }

    public ArrayList<TrainerAvailability> addWeeklyTrainerAvailabilities(DayOfWeek dayOfWeek, LocalDate startDate, LocalDate endDate, int trainerID, LocalTime startTime, LocalTime endTime) {
        return this.trainerAvailabilityService.addWeeklyTrainerAvailabilities(dayOfWeek, startDate, endDate, trainerID, startTime, endTime);
    }

    public void deleteTrainerAvailability(int availabilityId) {
        this.trainerAvailabilityService.deleteTrainerAvailability(availabilityId);
    }

    public ArrayList<TrainerAvailability> getTrainerAvailabilities(int trainerId) {
        return trainerAvailabilityService.getAvailabilitiesByTrainerID(trainerId);
    }

    public ArrayList<TrainerAvailability> getAvailabilitiesOfAllTrainers() {
        return trainerAvailabilityService.getAvailabilitiesOfAllTrainers();
    }

}
