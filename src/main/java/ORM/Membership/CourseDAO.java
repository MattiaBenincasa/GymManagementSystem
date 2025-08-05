package ORM.Membership;

import BusinessLogic.Exceptions.DAOException;
import DomainModel.Course;
import DomainModel.Users.Trainer;
import ORM.ConnectionManager;
import ORM.Users.TrainerDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {
    private final Connection connection;
    private final TrainerDAO trainerDAO;

    public CourseDAO(TrainerDAO trainerDAO) {
        this.connection = ConnectionManager.getSingleInstance().getConnection();
        this.trainerDAO = trainerDAO;
    }

    public int createCourse(Course course) throws DAOException {
        String sql = "INSERT INTO Course (name, description) VALUES (?, ?)";
        int courseId = 0;

        try (PreparedStatement statement = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, course.getName());
            statement.setString(2, course.getDescription());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    courseId = generatedKeys.getInt(1);
                } else {
                    throw new DAOException("Creating course failed, no ID obtained.");
                }
            }

            // insert of trainers into db
            if (course.getTrainers() != null && !course.getTrainers().isEmpty()) {
                String trainerSql = "INSERT INTO CourseTrainer (course_id, trainer_id) VALUES (?, ?)";
                try (PreparedStatement trainerStatement = this.connection.prepareStatement(trainerSql)) {
                    for (Trainer trainer : course.getTrainers()) {
                        trainerStatement.setInt(1, courseId);
                        trainerStatement.setInt(2, trainer.getId());
                        trainerStatement.addBatch();
                    }
                    trainerStatement.executeBatch();
                }
            }
            return courseId;
        } catch (SQLException e) {
            throw new DAOException("Error during INSERT into Course: " + e.getMessage(), e);
        }
    }

    public void updateCourse(Course course) throws DAOException {
        String updateCourseSql = "UPDATE Course SET name = ?, description = ? WHERE id = ?";

        try (PreparedStatement statement = this.connection.prepareStatement(updateCourseSql)) {
            statement.setString(1, course.getName());
            statement.setString(2, course.getDescription());
            statement.setInt(3, course.getId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Updating course failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DAOException("Error during UPDATE of Course: " + e.getMessage(), e);
        }

        //to update trainers I will delete all trainers and insert new ones
        String deleteTrainersSql = "DELETE FROM CourseTrainer WHERE course_id = ?";
        try (PreparedStatement deleteStatement = this.connection.prepareStatement(deleteTrainersSql)) {
            deleteStatement.setInt(1, course.getId());
            deleteStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error during DELETE from CourseTrainer: " + e.getMessage(), e);
        }

        if (course.getTrainers() != null && !course.getTrainers().isEmpty()) {
            String insertTrainersSql = "INSERT INTO CourseTrainer (course_id, trainer_id) VALUES (?, ?)";
            try (PreparedStatement insertStatement = this.connection.prepareStatement(insertTrainersSql)) {
                for (Trainer trainer : course.getTrainers()) {
                    insertStatement.setInt(1, course.getId());
                    insertStatement.setInt(2, trainer.getId());
                    insertStatement.addBatch();
                }
                insertStatement.executeBatch();
            } catch (SQLException e) {
                throw new DAOException("Error during INSERT into CourseTrainer: " + e.getMessage(), e);
            }
        }
    }

    public Course getCourseByID(int courseID) throws DAOException {
        String courseSql = "SELECT id, name, description FROM Course WHERE id = ?";
        String trainersSql = "SELECT trainer_id FROM CourseTrainer WHERE course_id = ?";
        Course course = null;

        try (PreparedStatement courseStatement = this.connection.prepareStatement(courseSql)) {
            courseStatement.setInt(1, courseID);
            try (ResultSet courseRs = courseStatement.executeQuery()) {
                if (courseRs.next()) {
                    course = new Course(courseRs.getInt("id"),
                            courseRs.getString("name"),
                            courseRs.getString("description"));
                } else {
                    throw new DAOException("Course with ID " + courseID + " not found.");
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error during SELECT from Course: " + e.getMessage(), e);
        }

        // Recupera i trainer associati al corso
        List<Trainer> trainers = new ArrayList<>();
        try (PreparedStatement trainersStatement = this.connection.prepareStatement(trainersSql)) {
            trainersStatement.setInt(1, courseID);
            try (ResultSet trainersRs = trainersStatement.executeQuery()) {
                while (trainersRs.next()) {
                    int trainerId = trainersRs.getInt("trainer_id");
                    Trainer trainer = this.trainerDAO.getTrainerByID(trainerId);
                    trainers.add(trainer);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error during SELECT from CourseTrainer: " + e.getMessage(), e);
        }
        course.setTrainers(new ArrayList<>(trainers));
        return course;
    }

    public void deleteCourse(int courseID) throws DAOException {
        String sql = "DELETE FROM Course WHERE id = ?";

        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setInt(1, courseID);

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Deleting course failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DAOException("Error during DELETE from Course: " + e.getMessage(), e);
        }
    }

}
