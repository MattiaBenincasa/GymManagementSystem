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
    void testCreateCourseAndTrainers() {
        Trainer trainer1 = UserDAOTestUtils.createTrainer("trainer", "mailTrainer@mail.it");
        Trainer trainer2 = UserDAOTestUtils.createTrainer("trainer_2", "mailTrainer2@mail.it");
        int id = this.trainerDAO.createTrainer(trainer1);
        int id2 = this.trainerDAO.createTrainer(trainer2);
        trainer1 = this.trainerDAO.getTrainerByID(id);
        trainer2 = this.trainerDAO.getTrainerByID(id2);
        Course newCourse = new Course("Corso di Yoga", "Un corso di yoga avanzato");
        newCourse.addTrainer(trainer1);
        newCourse.addTrainer(trainer2);
        assertDoesNotThrow(()->{
            Course courseWithId = courseDAO.createCourse(newCourse);
            courseDAO.getCourseByID(courseWithId.getId());
        });
    }

    @Test
    void testUpdateCourse() throws DAOException {
        Course originalCourse = courseDAO.createCourse(new Course("Boxe", "corso di boxe"));
        Trainer trainer = UserDAOTestUtils.createTrainer("trainer", "mailTrainer@mail.it");
        int id = this.trainerDAO.createTrainer(trainer);
        trainer = this.trainerDAO.getTrainerByID(id);
        originalCourse.setName("Corso Aggiornato");
        originalCourse.setDescription("Descrizione aggiornata");
        originalCourse.addTrainer(trainer);

        courseDAO.updateCourse(originalCourse);
        Course updatedCourse = courseDAO.getCourseByID(originalCourse.getId());

        Course retrievedCourse = courseDAO.getCourseByID(originalCourse.getId());
        assertEquals("Corso Aggiornato", retrievedCourse.getName());
        assertEquals("Descrizione aggiornata", retrievedCourse.getDescription());
        assertEquals(1, retrievedCourse.getTrainers().size());
    }

    @Test
    void testDeleteCourse() throws DAOException {
        Trainer trainer = UserDAOTestUtils.createTrainer("trainer", "mailTrainer@mail.it");
        int id = trainerDAO.createTrainer(trainer);
        trainer = this.trainerDAO.getTrainerByID(id);
        Course courseToDelete = new Course("Zumba", "Corso da eliminare");
        courseToDelete.addTrainer(trainer);
        courseToDelete = this.courseDAO.createCourse(courseToDelete);
        courseDAO.deleteCourse(courseToDelete.getId());
        int courseId = courseToDelete.getId();
        DAOException exception = assertThrows(DAOException.class, () -> {
            courseDAO.getCourseByID(courseId);
        });
        assertEquals("Course with ID " + courseId + " not found.", exception.getMessage());
    }
}