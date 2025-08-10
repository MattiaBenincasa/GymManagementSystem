package ORM.DailyEvents;

import BusinessLogic.Exceptions.DAOException;
import DomainModel.DailyEvents.TrainerAvailability;
import DomainModel.Users.Trainer;
import ORM.ConnectionManager;
import ORM.Users.TrainerDAO;

import java.sql.Connection;
import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.sql.*;

public class TrainerAvailabilityDAO {
    private final Connection connection;
    private final TrainerDAO trainerDAO;

    public TrainerAvailabilityDAO(TrainerDAO trainerDAO) {
        this.connection = ConnectionManager.getSingleInstance().getConnection();
        this.trainerDAO = trainerDAO;
    }

    public TrainerAvailability createTrainerAvailability(TrainerAvailability trainerAvailability) {
        String sql = "INSERT INTO TrainerAvailability (trainer_id, day, starttime, finishtime) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, trainerAvailability.getTrainer().getId());
            statement.setDate(2, Date.valueOf(trainerAvailability.getDay()));
            statement.setTime(3, Time.valueOf(trainerAvailability.getStartTime()));
            statement.setTime(4, Time.valueOf(trainerAvailability.getEndTime()));

            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return new TrainerAvailability(generatedKeys.getInt(1), trainerAvailability);
                } else {
                    throw new DAOException("Creating trainer availability failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error during INSERT into TrainerAvailability: " + e.getMessage(), e);
        }
    }

    public void updateTrainerAvailability(TrainerAvailability trainerAvailability) {
        String sql = "UPDATE TrainerAvailability SET trainer_id = ?, day = ?, starttime = ?, finishtime = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, trainerAvailability.getTrainer().getId());
            statement.setDate(2, Date.valueOf(trainerAvailability.getDay()));
            statement.setTime(3, Time.valueOf(trainerAvailability.getStartTime()));
            statement.setTime(4, Time.valueOf(trainerAvailability.getEndTime()));
            statement.setInt(5, trainerAvailability.getId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Updating trainer availability with ID " + trainerAvailability.getId() + " failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DAOException("Error during UPDATE on TrainerAvailability: " + e.getMessage(), e);
        }
    }

    public void deleteTrainerAvailability(int trainerAvailabilityID) {
        String sql = "DELETE FROM TrainerAvailability WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, trainerAvailabilityID);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Deletion of trainer availability with ID " + trainerAvailabilityID + " failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DAOException("Error during DELETE from TrainerAvailability: " + e.getMessage(), e);
        }
    }

    public TrainerAvailability getTrainerAvailabilityByID(int trainerAvailabilityID) {
        String sql = "SELECT id, trainer_id, day, starttime, finishtime FROM TrainerAvailability WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, trainerAvailabilityID);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return extractTrainerAvailabilityFromResultSet(resultSet);
                } else {
                    throw new DAOException("Trainer availability with ID " + trainerAvailabilityID + " not found.");
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error during SELECT from TrainerAvailability: " + e.getMessage(), e);
        }
    }

    public ArrayList<TrainerAvailability> getAllTrainersAvailability() {
        ArrayList<TrainerAvailability> availabilities = new ArrayList<>();
        String sql = "SELECT id, trainer_id, day, starttime, finishtime FROM TrainerAvailability";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                availabilities.add(extractTrainerAvailabilityFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DAOException("Error during SELECT all from TrainerAvailability: " + e.getMessage(), e);
        }
        return availabilities;
    }

    public ArrayList<TrainerAvailability> getAllTrainerAvailabilityByTrainerID(int trainerID) {
        ArrayList<TrainerAvailability> availabilities = new ArrayList<>();
        String sql = "SELECT id, trainer_id, day, starttime, finishtime FROM TrainerAvailability WHERE trainer_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, trainerID);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    availabilities.add(extractTrainerAvailabilityFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error during SELECT from TrainerAvailability by Trainer ID: " + e.getMessage(), e);
        }
        return availabilities;
    }

    private TrainerAvailability extractTrainerAvailabilityFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        Trainer trainer = this.trainerDAO.getTrainerByID(resultSet.getInt("trainer_id"));
        TrainerAvailability trainerAvailability = new TrainerAvailability(id, trainer);
        trainerAvailability.setDay(resultSet.getDate("day").toLocalDate());
        trainerAvailability.setStartTime(resultSet.getTime("starttime").toLocalTime());
        trainerAvailability.setEndTime(resultSet.getTime("finishtime").toLocalTime());

        return trainerAvailability;
    }

}
