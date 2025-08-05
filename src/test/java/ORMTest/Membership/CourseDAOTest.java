package ORMTest.Membership;

import BusinessLogic.Exceptions.DAOException;
import DomainModel.Course;
import DomainModel.Users.Trainer;
import ORM.Membership.CourseDAO;
import ORM.Users.TrainerDAO;
import ORM.Users.UserDAO;
import TestUtils.DAOTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import ORMTest.Users.UserDAOTestUtils;

class CourseDAOTest {
    private CourseDAO courseDAO;
    private TrainerDAO trainerDAO;
    private UserDAO userDAO;

    @BeforeEach
    void setUp() {
        DAOTestUtils.resetDatabase();
        this.userDAO = new UserDAO();
        this.trainerDAO = new TrainerDAO(this.userDAO);
        this.courseDAO = new CourseDAO(this.trainerDAO);
    }

    @Test
    void testCreateCourseAndTrainers() throws DAOException {
        Trainer trainer1 = UserDAOTestUtils.createTrainer();
        int id = this.trainerDAO.createTrainer(trainer1);
        trainer1 = this.trainerDAO.getTrainerByID(id);
        ArrayList<Trainer> trainers = new ArrayList<>();
        trainers.add(trainer1);
        Course newCourse = new Course("Corso di Yoga", "Un corso di yoga avanzato");
        newCourse.setTrainers(trainers);

        assertDoesNotThrow(()->{
            int courseId = courseDAO.createCourse(newCourse);
            courseDAO.getCourseByID(courseId);
        });
    }

    @Test
    void testGetCourseByID_CourseExists() throws DAOException {
        Trainer trainer1 = UserDAOTestUtils.createTrainer();
        int id = this.trainerDAO.createTrainer(trainer1);
        trainer1 = this.trainerDAO.getTrainerByID(id);

        Course course = new Course("Corso di Pilates", "descrizione");
        course.addTrainer(trainer1);
        int courseId = courseDAO.createCourse(course);
        Course retrievedCourse = courseDAO.getCourseByID(courseId);

        assertNotNull(retrievedCourse);
        assertEquals(courseId, retrievedCourse.getId());
        assertEquals("Corso di Pilates", retrievedCourse.getName());
        assertEquals(1, retrievedCourse.getTrainers().size());
    }

    @Test
    void testUpdateCourse() throws DAOException {
        Course originalCourse = new Course("Boxe", "corso di boxe");
        int courseId = courseDAO.createCourse(originalCourse);

        originalCourse = new Course(courseId, originalCourse.getName(), originalCourse.getDescription());

        Trainer trainer = UserDAOTestUtils.createTrainer();
        int id = this.trainerDAO.createTrainer(trainer);
        trainer = this.trainerDAO.getTrainerByID(id);
        originalCourse.setName("Corso Aggiornato");
        originalCourse.setDescription("Descrizione aggiornata");
        originalCourse.addTrainer(trainer);

        courseDAO.updateCourse(originalCourse);
        Course updatedCourse = courseDAO.getCourseByID(courseId);

        Course retrievedCourse = courseDAO.getCourseByID(courseId);
        assertEquals("Corso Aggiornato", retrievedCourse.getName());
        assertEquals("Descrizione aggiornata", retrievedCourse.getDescription());
        assertEquals(1, retrievedCourse.getTrainers().size());
    }

    @Test
    void testDeleteCourse() throws DAOException {
        Trainer trainer = UserDAOTestUtils.createTrainer();
        int id = trainerDAO.createTrainer(trainer);
        trainer = this.trainerDAO.getTrainerByID(id);
        Course courseToDelete = new Course("Zumba", "Corso da eliminare");

        courseToDelete.addTrainer(trainer);
        int courseId = courseDAO.createCourse(courseToDelete);
        courseDAO.deleteCourse(courseId);

        DAOException exception = assertThrows(DAOException.class, () -> {
            courseDAO.getCourseByID(courseId);
        });
        assertEquals("Course with ID " + courseId + " not found.", exception.getMessage());
    }
}