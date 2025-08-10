package ORMTest.DailyEventsTest;

import BusinessLogic.Exceptions.DAOException;
import DomainModel.DailyEvents.TrainerAvailability;
import DomainModel.Users.Trainer;
import ORM.DailyEvents.TrainerAvailabilityDAO;
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

public class TrainerAvailabilityDAOTest {
    private Trainer trainer;
    private TrainerAvailabilityDAO trainerAvailabilityDAO;
    private TrainerAvailability trainerAvailability;


    @BeforeEach
    void setup() {
        DAOTestUtils.resetDatabase();
        TrainerDAO trainerDAO = new TrainerDAO(new UserDAO());
        this.trainerAvailabilityDAO = new TrainerAvailabilityDAO(trainerDAO);
        this.trainer = trainerDAO.createTrainer(UserDAOTestUtils.createTrainer("trainer", "trainer@user.it"));

        this.trainerAvailability = new TrainerAvailability(this.trainer);
        this.trainerAvailability.setDay(LocalDate.of(2025, 1, 1));
        this.trainerAvailability.setStartTime(LocalTime.of(9, 0));
        this.trainerAvailability.setEndTime(LocalTime.of(10, 30));
    }

    @Test
    void testCreateTrainerAvailability() {
        this.trainerAvailability = this.trainerAvailabilityDAO.createTrainerAvailability(this.trainerAvailability);
        TrainerAvailability retrieved = this.trainerAvailabilityDAO.getTrainerAvailabilityByID(this.trainerAvailability.getId());

        assertEquals(trainerAvailability.getId(), retrieved.getId());
        assertEquals(trainerAvailability.getDay(), retrieved.getDay());
        assertEquals(trainerAvailability.getTrainer().getId(), retrieved.getTrainer().getId());
        assertEquals(trainerAvailability.getStartTime(), retrieved.getStartTime());
        assertEquals(trainerAvailability.getEndTime(), retrieved.getEndTime());
    }

    @Test
    void testUpdateTrainerAvailability() {
        this.trainerAvailability = this.trainerAvailabilityDAO.createTrainerAvailability(this.trainerAvailability);

        this.trainerAvailability.setStartTime(LocalTime.of(8, 30));
        this.trainerAvailability.setEndTime(LocalTime.of(9,30));
        this.trainerAvailability.setDay(LocalDate.of(2025, 2, 2));

        this.trainerAvailabilityDAO.updateTrainerAvailability(this.trainerAvailability);
        TrainerAvailability retrieved = this.trainerAvailabilityDAO.getTrainerAvailabilityByID(this.trainerAvailability.getId());
        assertEquals(this.trainerAvailability.getStartTime(), retrieved.getStartTime());
        assertEquals(this.trainerAvailability.getEndTime(), retrieved.getEndTime());
        assertEquals(this.trainerAvailability.getDay(), retrieved.getDay());

    }

    @Test
    void testDeleteTrainerAvailability() {
        this.trainerAvailability = this.trainerAvailabilityDAO.createTrainerAvailability(this.trainerAvailability);

        this.trainerAvailabilityDAO.deleteTrainerAvailability(this.trainerAvailability.getId());

        assertThrows(DAOException.class, ()->{
            this.trainerAvailabilityDAO.getTrainerAvailabilityByID(this.trainerAvailability.getId());
        });
    }

    @AfterAll
    static void teardown() {
        DAOTestUtils.resetDatabase();
    }

}
