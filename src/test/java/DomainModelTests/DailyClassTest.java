package DomainModelTests;

import DomainModel.DailyEvents.DailyClass;
import DomainModel.Membership.Course;
import DomainModel.Users.Trainer;
import ORMTest.Users.UserDAOTestUtils;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

//class to test builder pattern of DailyClass
public class DailyClassTest {

    @Test
    void dailyClassShouldNotBeCreated() {
        Course course = new Course("Spinning", "corso di spinning");
        Trainer rigthCourseTrainer = UserDAOTestUtils.createTrainer("mariorossi", "rossi@trainer.it");
        Trainer wrongCourseTrainer = UserDAOTestUtils.createTrainer("marioverdi", "verdi@trainer.it");
        Trainer personalTrainerOnly = UserDAOTestUtils.createTrainer("mariobianchi", "bianchi@trainer.it");
        personalTrainerOnly.setIsCourseCoach(false);

        course.addTrainer(rigthCourseTrainer);

        Throwable wrongCoachException = assertThrows(IllegalStateException.class, ()->{
            DailyClass dailyClass = new DailyClass.Builder()
                    .day(LocalDate.now())
                    .startTime(LocalTime.of(16, 30, 00))
                    .endTime(LocalTime.of(17, 30))
                    .course(course)
                    .coach(wrongCourseTrainer)
                    .maxParticipants(13)
                    .build();
        });

        assertEquals("Only trainers of " + course.getName() + " can be selected as trainers of this class", wrongCoachException.getMessage());

        Throwable nonCourseCoachException = assertThrows(IllegalStateException.class, ()->{
            DailyClass dailyClass = new DailyClass.Builder()
                    .day(LocalDate.now())
                    .startTime(LocalTime.of(16, 30, 00))
                    .endTime(LocalTime.of(17, 30))
                    .course(course)
                    .coach(personalTrainerOnly)
                    .maxParticipants(13)
                    .build();
        });

        assertEquals("Only course coach can be set as coach", nonCourseCoachException.getMessage());

        assertDoesNotThrow(()->{
            DailyClass dailyClass = new DailyClass.Builder()
                    .day(LocalDate.now())
                    .startTime(LocalTime.of(16, 30, 00))
                    .endTime(LocalTime.of(17, 30))
                    .course(course)
                    .coach(rigthCourseTrainer)
                    .maxParticipants(13)
                    .build();
        });
    }

}
