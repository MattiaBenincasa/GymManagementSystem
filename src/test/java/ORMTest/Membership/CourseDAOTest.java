package ORMTest.Membership;

import BusinessLogic.Exceptions.DAOException;
import DomainModel.Membership.Course;
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
        Trainer trainer1 = this.trainerDAO.createTrainer(UserDAOTestUtils.createTrainer("trainer_1", "mailTrainer1@mail.it"));
        Trainer trainer2 = this.trainerDAO.createTrainer(UserDAOTestUtils.createTrainer("trainer_2", "mailTrainer2@mail.it"));
        Course newCourse = new Course("Corso di Yoga", "Un corso di yoga avanzato");
        newCourse.addTrainer(trainer1);
        newCourse.addTrainer(trainer2);
        assertDoesNotThrow(()->{
            Course courseWithId = courseDAO.createCourse(newCourse);
            courseDAO.getCourseByID(courseWithId.getId());
            assertEquals(trainer1.getId(), courseWithId.getTrainers().getFirst().getId());
            assertEquals(trainer2.getId(), courseWithId.getTrainers().get(1).getId());
        });
    }

    @Test
    void testUpdateCourse() {
        Course originalCourse = courseDAO.createCourse(new Course("Boxe", "corso di boxe"));
        Trainer trainer = this.trainerDAO.createTrainer(UserDAOTestUtils.createTrainer("trainer", "mailTrainer@mail.it"));
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
        Trainer trainer = this.trainerDAO.createTrainer(UserDAOTestUtils.createTrainer("trainer", "mailTrainer@mail.it"));
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