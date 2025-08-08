package ORM.Users;

import BusinessLogic.Exceptions.DAOException;
import DomainModel.Users.Trainer;
import ORM.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TrainerDAO {
    private final Connection connection;
    private final UserDAO userDAO;

    public TrainerDAO(UserDAO userDAO) {
        this.connection = ConnectionManager.getSingleInstance().getConnection();
        this.userDAO = userDAO;
    }

    public Trainer createTrainer(Trainer trainer) {
        int userId = this.userDAO.createUser(trainer, "TRAINER");

        String sql = "INSERT INTO Trainer (id, isPersonalTrainer, isCourseCoach) VALUES (?, ?, ?)";

        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setBoolean(2, trainer.isPersonalTrainer());
            statement.setBoolean(3, trainer.isCourseCoach());

            statement.executeUpdate();
            return new Trainer(userId, trainer);
        } catch (SQLException e) {
            throw new DAOException("Error during INSERT into Trainer: " + e.getMessage(), e);
        }
    }

    public void updateTrainer(Trainer trainer) {
        this.userDAO.updateUser(trainer);

        String sql = "UPDATE Trainer SET isPersonalTrainer = ?, isCourseCoach = ? WHERE id = ?";

        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setBoolean(1, trainer.isPersonalTrainer());
            statement.setBoolean(2, trainer.isCourseCoach());
            statement.setInt(3, trainer.getId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Updating trainer failed, no rows affected in Trainer table.");
            }
        } catch (SQLException e) {
            throw new DAOException("Error during UPDATE of Trainer: " + e.getMessage(), e);
        }
    }

    public Trainer getTrainerByID(int trainerID) {
        String sql = "SELECT u.id, u.username, u.hashPassword, u.name, u.surname, u.mail, u.phoneNumber, u.birthDate, u.role, t.isPersonalTrainer, t.isCourseCoach " +
                "FROM \"User\" u JOIN Trainer t ON u.id = t.id WHERE u.id = ?";

        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setInt(1, trainerID);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Trainer trainer = new Trainer();
                    trainer.setUsername(resultSet.getString("username"));
                    trainer.setPasswordHash(resultSet.getString("hashpassword"));
                    trainer.setName(resultSet.getString("name"));
                    trainer.setSurname(resultSet.getString("surname"));
                    trainer.setMail(resultSet.getString("mail"));
                    trainer.setPhoneNumber(resultSet.getString("phoneNumber"));
                    trainer.setBirthDate(resultSet.getDate("birthDate").toLocalDate());
                    trainer.setIsPersonalTrainer(resultSet.getBoolean("isPersonalTrainer"));
                    trainer.setIsCourseCoach(resultSet.getBoolean("isCourseCoach"));
                    return new Trainer(resultSet.getInt("id"), trainer);
                } else {
                    throw new DAOException("Trainer with ID " + trainerID + " not found.");
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error during SELECT from User and Trainer: " + e.getMessage(), e);
        }
    }

    public ArrayList<Trainer> getAllTrainers() {return null;}

}
