package ORMTest.DailyEventsTest;
import BusinessLogic.Exceptions.DAOException;
import DomainModel.Membership.Course;
import DomainModel.DailyEvents.DailyClass;
import DomainModel.Users.Trainer;
import ORM.Membership.CourseDAO;
import ORM.DailyEvents.DailyClassDAO;
import ORM.Users.TrainerDAO;
import ORM.Users.UserDAO;
import ORMTest.Users.UserDAOTestUtils;
import TestUtils.DAOTestUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class DailyClassDAOTest {
    private DailyClassDAO dailyClassDAO;
    private CourseDAO courseDAO;
    private TrainerDAO trainerDAO;
    private Course course;
    private Trainer trainer;
    private DailyClass dailyClass;

    @BeforeEach
    void setUp() {
        DAOTestUtils.resetDatabase();
        UserDAO userDAO = new UserDAO();
        this.trainerDAO = new TrainerDAO(userDAO);
        this.courseDAO = new CourseDAO(this.trainerDAO);
        this.dailyClassDAO = new DailyClassDAO(userDAO);
        this.course = new Course("Spinning", "Corso di spinning");
        this.trainer = this.trainerDAO.createTrainer(UserDAOTestUtils.createTrainer("trainer", "trainer@trainer.it"));
        this.course.addTrainer(this.trainer);
        this.course = this.courseDAO.createCourse(this.course);

        this.dailyClass = new DailyClass.Builder()
                .course(this.course)
                .coach(this.trainer)
                .day(LocalDate.now())
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(11, 0))
                .maxParticipants(15)
                .build();

    }

    @Test
    void testCreateDailyClass() {
        assertDoesNotThrow(() -> {
            DailyClass createdDailyClass = this.dailyClassDAO.createDailyClass(this.dailyClass);
            DailyClass retrieved = this.dailyClassDAO.getDailyClassByID(createdDailyClass.getId());

            assertNotNull(retrieved);
            assertEquals(createdDailyClass.getId(), retrieved.getId());
            assertEquals(createdDailyClass.getCourse().getId(), retrieved.getCourse().getId());
            assertEquals(createdDailyClass.getCoach().getId(), retrieved.getCoach().getId());
            assertEquals(createdDailyClass.getDay(), retrieved.getDay());
            assertEquals(createdDailyClass.getStartTime(), retrieved.getStartTime());
            assertEquals(createdDailyClass.getEndTime(), retrieved.getEndTime());
            assertEquals(createdDailyClass.getMaxParticipants(), retrieved.getMaxParticipants());
            assertTrue(retrieved.isActive());
        });
    }

    @Test
    void testUpdateDailyClass() throws DAOException {
        this.dailyClass = this.dailyClassDAO.createDailyClass(this.dailyClass);

        Trainer anotherTrainer = this.trainerDAO.createTrainer(UserDAOTestUtils.createTrainer("otherTrainer", "other@trainer.it"));
        this.course.addTrainer(anotherTrainer);
        this.courseDAO.updateCourse(this.course);
        this.dailyClass.changeCoach(anotherTrainer);
        this.dailyClassDAO.updateDailyClass(dailyClass);

        DailyClass retrieved = this.dailyClassDAO.getDailyClassByID(dailyClass.getId());
        assertEquals(anotherTrainer.getId(), retrieved.getCoach().getId());
    }

    @Test
    void testCancelDailyClass() throws DAOException {
        this.dailyClass = this.dailyClassDAO.createDailyClass(this.dailyClass);
        this.dailyClassDAO.cancelDailyClass(dailyClass.getId());

        DailyClass retrieved = this.dailyClassDAO.getDailyClassByID(dailyClass.getId());
        assertFalse(retrieved.isActive());
    }

    @Test
    void testDeleteDailyClass() throws DAOException {
        this.dailyClass = this.dailyClassDAO.createDailyClass(dailyClass);
        int idToDelete = this.dailyClass.getId();
        this.dailyClassDAO.deleteDailyClass(idToDelete);

        assertThrows(DAOException.class, () -> this.dailyClassDAO.getDailyClassByID(idToDelete));
    }

    @AfterAll
    static void tearDown() {
        DAOTestUtils.resetDatabase();
    }
}
