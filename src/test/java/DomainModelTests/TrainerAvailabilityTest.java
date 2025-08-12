package DomainModelTests;

import DomainModel.DailyEvents.TrainerAvailability;
import DomainModel.Users.Trainer;
import ORMTest.Users.UserDAOTestUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TrainerAvailabilityTest {

    @Test
    void trainerAvailabilityShouldNotBeCreated() {
        Trainer trainer = UserDAOTestUtils.createTrainer("trainer", "trainer@trainer.it");
        trainer.setIsPersonalTrainer(false);
        assertThrows(IllegalStateException.class, ()->{
            TrainerAvailability trainerAvailability = new TrainerAvailability(trainer);
        });
    }

    @Test
    void trainerAvailabilityShouldBeCreated() {
        Trainer trainer = UserDAOTestUtils.createTrainer("trainer", "trainer@trainer.it");
        trainer.setIsCourseCoach(false);
        assertDoesNotThrow(()->{
            TrainerAvailability trainerAvailability = new TrainerAvailability(trainer);
        });
    }

}
