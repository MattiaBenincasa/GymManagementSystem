package BusinessLogic.DailyEvents;

import BusinessLogic.Exceptions.LateCancellationException;
import BusinessLogic.Exceptions.UnauthorizedException;
import DomainModel.DailyEvents.TrainerAvailability;
import DomainModel.Users.Trainer;
import ORM.DailyEvents.TrainerAvailabilityDAO;
import ORM.Users.TrainerDAO;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class TrainerAvailabilityService {
    private final TrainerAvailabilityDAO trainerAvailabilityDAO;
    private final TrainerDAO trainerDAO;

    public TrainerAvailabilityService(TrainerAvailabilityDAO trainerAvailabilityDAO, TrainerDAO trainerDAO) {
        this.trainerDAO = trainerDAO;
        this.trainerAvailabilityDAO = trainerAvailabilityDAO;
    }

    public TrainerAvailability addtrainerAvailability(int trainerID, LocalTime startTime, LocalTime endTime, LocalDate day) {
        TrainerAvailability trainerAvailability = new TrainerAvailability(this.trainerDAO.getTrainerByID(trainerID));
        trainerAvailability.setDay(day);
        trainerAvailability.setStartTime(startTime);
        trainerAvailability.setEndTime(endTime);
        return this.trainerAvailabilityDAO.createTrainerAvailability(trainerAvailability);
    }

    public void deleteTrainerAvailability(int trainerID, int trainerAvailabilityID) {
        TrainerAvailability trainerAvailability = this.trainerAvailabilityDAO.getTrainerAvailabilityByID(trainerAvailabilityID);

        if (trainerAvailability.getTrainer().getId() != trainerID)
            throw new UnauthorizedException("You can delete only yours availabilities");

        LocalDateTime classStartDateTime = LocalDateTime.of(trainerAvailability.getDay(), trainerAvailability.getStartTime());
        LocalDateTime oneDaysFromNow = classStartDateTime.plusDays(1);

        if (classStartDateTime.isBefore(oneDaysFromNow))
            throw new LateCancellationException("An availability can be removed at least 1 day before the class starts");

        this.trainerAvailabilityDAO.deleteTrainerAvailability(trainerAvailabilityID);
    }

    public ArrayList<TrainerAvailability> addWeeklyTrainerAvailabilities(DayOfWeek dayOfWeek, LocalDate startDate, LocalDate endDate, int trainerID, LocalTime startTime, LocalTime endTime) {
        ArrayList<TrainerAvailability> trainerAvailabilities = new ArrayList<>();
        Trainer trainer = this.trainerDAO.getTrainerByID(trainerID);
        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            if (currentDate.getDayOfWeek() == dayOfWeek) {
                TrainerAvailability trainerAvailability = new TrainerAvailability(trainer);
                trainerAvailability.setDay(currentDate);
                trainerAvailability.setStartTime(startTime);
                trainerAvailability.setEndTime(endTime);

                this.trainerAvailabilityDAO.createTrainerAvailability(trainerAvailability);
                trainerAvailabilities.add(trainerAvailability);
            }
            currentDate = currentDate.plusDays(1);
        }
        return trainerAvailabilities;
    }

    public ArrayList<TrainerAvailability> getAvailabilitiesOfAllTrainers() {
        return this.trainerAvailabilityDAO.getAllTrainersAvailability();
    }

    public ArrayList<TrainerAvailability> getAvailabilitiesByTrainerID(int trainerID) {
        return trainerAvailabilityDAO.getAllTrainerAvailabilityByTrainerID(trainerID);
    }

}
