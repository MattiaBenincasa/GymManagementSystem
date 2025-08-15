package DomainModelTests;

import Exceptions.LateCancellationException;
import DomainModel.DailyEvents.DailyClass;
import DomainModel.Membership.Course;
import DomainModel.Users.Trainer;
import ORMTest.Users.UserDAOTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

//class to test builder pattern of DailyClass
public class DailyClassTest {
    private Course course;
    private Trainer rightCourseTrainer;
    private Trainer wrongCourseTrainer;
    private Trainer personalTrainerOnly;

    @BeforeEach
    void setup() {
        this.course = new Course("Spinning", "corso di spinning");
        this.rightCourseTrainer = UserDAOTestUtils.createTrainer("mariorossi", "rossi@trainer.it");
        this.wrongCourseTrainer = UserDAOTestUtils.createTrainer("marioverdi", "verdi@trainer.it");
        this.personalTrainerOnly = UserDAOTestUtils.createTrainer("mariobianchi", "bianchi@trainer.it");
        personalTrainerOnly.setIsCourseCoach(false);
        this.course.addTrainer(this.rightCourseTrainer);
    }


    @Test
    void dailyClassShouldNotBeCreated() {
        Throwable wrongCoachException = assertThrows(IllegalStateException.class, ()->{
            DailyClass dailyClass = new DailyClass.Builder()
                    .day(LocalDate.now())
                    .startTime(LocalTime.of(16, 30, 00))
                    .endTime(LocalTime.of(17, 30))
                    .course(this.course)
                    .coach(this.wrongCourseTrainer)
                    .maxParticipants(13)
                    .build();
        });

        assertEquals("Only trainers of " + this.course.getName() + " can be selected as trainers of this class", wrongCoachException.getMessage());

        Throwable nonCourseCoachException = assertThrows(IllegalStateException.class, ()->{
            DailyClass dailyClass = new DailyClass.Builder()
                    .day(LocalDate.now())
                    .startTime(LocalTime.of(16, 30, 00))
                    .endTime(LocalTime.of(17, 30))
                    .course(this.course)
                    .coach(this.personalTrainerOnly)
                    .maxParticipants(13)
                    .build();
        });

        assertEquals("Only course coach can be set as coach", nonCourseCoachException.getMessage());
    }

    @Test
    void dailyClassShouldBeCreated() {
        assertDoesNotThrow(()->{
            DailyClass dailyClass = new DailyClass.Builder()
                    .day(LocalDate.now())
                    .startTime(LocalTime.of(16, 30, 00))
                    .endTime(LocalTime.of(17, 30))
                    .course(course)
                    .coach(this.rightCourseTrainer)
                    .maxParticipants(13)
                    .build();
        });
    }

    @Test
    void dailyClassShouldNotBeCancelled() {
        LocalTime twoHoursFromNow = LocalTime.now().plusHours(2);
        LocalTime  fourHoursFromNow = LocalTime.now().plusHours(4);
        DailyClass dailyClass = new DailyClass.Builder()
                .day(LocalDate.now())
                .startTime(twoHoursFromNow)
                .endTime(fourHoursFromNow)
                .course(course)
                .coach(this.rightCourseTrainer)
                .maxParticipants(13)
                .build();

        assertThrows(LateCancellationException.class, dailyClass::cancel);
    }

    @Test
    void dailyClassShouldBeCancelled() {
        LocalTime twoHoursFromNow = LocalTime.now().plusHours(2).plusSeconds(1);
        LocalTime  fourHoursFromNow = LocalTime.now().plusHours(4);
        DailyClass dailyClass = new DailyClass.Builder()
                .day(LocalDate.now())
                .startTime(twoHoursFromNow)
                .endTime(fourHoursFromNow)
                .course(course)
                .coach(this.rightCourseTrainer)
                .maxParticipants(13)
                .build();

        assertDoesNotThrow(dailyClass::cancel);
    }

}
