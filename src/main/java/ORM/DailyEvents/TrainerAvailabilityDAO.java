package ORM.DailyEvents;

import DomainModel.DailyEvents.TrainerAvailability;
import ORM.ConnectionManager;

import java.sql.Connection;
import java.util.ArrayList;

public class TrainerAvailabilityDAO {
    private final Connection connection;

    public TrainerAvailabilityDAO() {
        this.connection = ConnectionManager.getSingleInstance().getConnection();
    }

    public TrainerAvailability createTrainerAvailability(TrainerAvailability dailyClass) {}

    public void deleteTrainerAvailability(int trainerAvailabilityID) {}

    public TrainerAvailability getTrainerAvailabilityByID(int trainerAvailabilityID) {}

    public ArrayList<TrainerAvailability> getAllTrainersAvailability() {}

    public ArrayList<TrainerAvailability> getAllTrainerAvailabilityByTrainerID(int trainerID) {}
}
