package ORM.DailyEvents;

import BusinessLogic.Exceptions.DAOException;
import DomainModel.DailyEvents.DailyClass;
import ORM.ConnectionManager;
import ORM.Membership.CourseDAO;
import ORM.Users.TrainerDAO;
import ORM.Users.UserDAO;
import java.sql.*;
import java.util.ArrayList;
import DomainModel.Membership.Course;
import DomainModel.Users.Trainer;


public class DailyClassDAO {
    private final Connection connection;
    private final CourseDAO courseDAO;
    private final TrainerDAO trainerDAO;

    public DailyClassDAO(UserDAO userDAO) {
        this.connection = ConnectionManager.getSingleInstance().getConnection();
        this.trainerDAO = new TrainerDAO(userDAO);
        this.courseDAO = new CourseDAO(this.trainerDAO);
    }

    public DailyClass createDailyClass(DailyClass dailyClass) {
        String sql = "INSERT INTO DailyClass (course_id, coach_id, day, startime, endtime, maxparticipants, is_active) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, dailyClass.getCourse().getId());
            statement.setInt(2, dailyClass.getCoach().getId());
            statement.setDate(3, Date.valueOf(dailyClass.getDay()));
            statement.setTime(4, java.sql.Time.valueOf(dailyClass.getStartTime()));
            statement.setTime(5, java.sql.Time.valueOf(dailyClass.getEndTime()));
            statement.setInt(6, dailyClass.getMaxParticipants());
            statement.setBoolean(7, dailyClass.isActive());

            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return new DailyClass.Builder()
                            .id(generatedKeys.getInt(1))
                            .day(dailyClass.getDay())
                            .startTime(dailyClass.getStartTime())
                            .endTime(dailyClass.getEndTime())
                            .course(dailyClass.getCourse())
                            .coach(dailyClass.getCoach())
                            .isActive(dailyClass.isActive())
                            .maxParticipants(dailyClass.getMaxParticipants())
                            .build();
                } else {
                    throw new DAOException("Creating daily class failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error during INSERT into DailyClass: " + e.getMessage(), e);
        }
    }

    public void deleteDailyClass(int dailyClassID) {
        String sql = "DELETE FROM DailyClass WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, dailyClassID);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Deletion of daily class with ID " + dailyClassID + " failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DAOException("Error during DELETE from DailyClass: " + e.getMessage(), e);
        }
    }

    public void updateDailyClass(DailyClass dailyClass) throws DAOException {
        String sql = "UPDATE DailyClass SET course_id = ?, coach_id = ?, day = ?, startime = ?, endtime = ?, maxparticipants = ?, is_active = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, dailyClass.getCourse().getId());
            statement.setInt(2, dailyClass.getCoach().getId());
            statement.setDate(3, Date.valueOf(dailyClass.getDay()));
            statement.setTime(4, java.sql.Time.valueOf(dailyClass.getStartTime()));
            statement.setTime(5, java.sql.Time.valueOf(dailyClass.getEndTime()));
            statement.setInt(6, dailyClass.getMaxParticipants());
            statement.setBoolean(7, dailyClass.isActive());
            statement.setInt(8, dailyClass.getId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Updating daily class with ID " + dailyClass.getId() + " failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DAOException("Error during UPDATE on DailyClass: " + e.getMessage(), e);
        }
    }


    // set isActive = false
    public void cancelDailyClass(int dailyClassID) throws DAOException {
        String sql = "UPDATE DailyClass SET is_active = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBoolean(1, false);
            statement.setInt(2, dailyClassID);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Cancelling daily class with ID " + dailyClassID + " failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DAOException("Error during UPDATE on DailyClass (cancel): " + e.getMessage(), e);
        }
    }

    public DailyClass getDailyClassByID(int dailyClassID) throws DAOException {
        String sql = "SELECT id, course_id, coach_id, day, startime, endtime, maxparticipants, is_active FROM DailyClass WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, dailyClassID);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return extractDailyClassFromResultSet(resultSet);
                } else {
                    throw new DAOException("DailyClass with ID " + dailyClassID + " not found.");
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error during SELECT from DailyClass: " + e.getMessage(), e);
        }
    }

    public ArrayList<DailyClass> getAllDailyClasses() throws DAOException {
        ArrayList<DailyClass> dailyClasses = new ArrayList<>();
        String sql = "SELECT id, course_id, coach_id, day, startime, endtime, maxparticipants, is_active FROM DailyClass";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                dailyClasses.add(extractDailyClassFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DAOException("Error during SELECT all from DailyClass: " + e.getMessage(), e);
        }
        return dailyClasses;
    }


    public ArrayList<DailyClass> getAllDailyClassesByCourse(int courseID) {
        ArrayList<DailyClass> dailyClasses = new ArrayList<>();
        String sql = "SELECT id, course_id, coach_id, day, startime, endtime, maxparticipants, is_active FROM DailyClass WHERE course_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, courseID);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    dailyClasses.add(extractDailyClassFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error during SELECT from DailyClass by Course ID: " + e.getMessage(), e);
        }
        return dailyClasses;
    }

    private DailyClass extractDailyClassFromResultSet(ResultSet resultSet) throws SQLException{

        Course course = courseDAO.getCourseByID(resultSet.getInt("course_id"));
        Trainer trainer = trainerDAO.getTrainerByID(resultSet.getInt("coach_id"));
        return new DailyClass.Builder()
                .id(resultSet.getInt("id"))
                .day(resultSet.getDate("day").toLocalDate())
                .startTime(resultSet.getTime("startime").toLocalTime())
                .endTime(resultSet.getTime("endtime").toLocalTime())
                .maxParticipants(resultSet.getInt("maxparticipants"))
                .isActive(resultSet.getBoolean("is_active"))
                .course(course)
                .coach(trainer)
                .build();
    }

}
