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

    public TrainerAvailability createTrainerAvailability(TrainerAvailability dailyClass) {return null;}

    public void deleteTrainerAvailability(int trainerAvailabilityID) {}

    public TrainerAvailability getTrainerAvailabilityByID(int trainerAvailabilityID) {return null;}

    public ArrayList<TrainerAvailability> getAllTrainersAvailability() {return null;}

    public ArrayList<TrainerAvailability> getAllTrainerAvailabilityByTrainerID(int trainerID) {return null;}
}
