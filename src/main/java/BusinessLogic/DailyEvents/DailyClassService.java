package BusinessLogic.DailyEvents;

import BusinessLogic.Exceptions.LateCancellationException;
import DomainModel.Course;
import DomainModel.DailyClass;
import ORM.Membership.CourseDAO;
import ORM.Membership.DailyClassDAO;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;


public class DailyClassService {
    private final DailyClassDAO dailyClassDAO;
    private final CourseDAO courseDAO;

    public DailyClassService(DailyClassDAO dailyClassDAO, CourseDAO courseDAO) {
        this.dailyClassDAO = dailyClassDAO;
        this.courseDAO = courseDAO;
    }

    public DailyClass addDailyCLass(LocalDate day, LocalTime startTime, LocalTime endTime, int maxParticipants, int courseID) {
        DailyClass dailyClass = new DailyClass.Builder()
                .day(day)
                .startTime(startTime)
                .endTime(endTime)
                .maxParticipants(maxParticipants)
                .course(this.courseDAO.getCourseByID(courseID))
                .isActive(true)
                .build();
        return this.dailyClassDAO.createDailyClass(dailyClass);
    }


    //remove class from db. A class can be removed at least 1 day before the class starts
    public void RemoveDailyClass(int dailyClassID) {
        DailyClass dailyClass = this.dailyClassDAO.getDailyClassByID(dailyClassID);
        LocalDateTime classStartDateTime = LocalDateTime.of(dailyClass.getDay(), dailyClass.getStartTime());
        LocalDateTime oneDaysFromNow = classStartDateTime.plusDays(1);

        if (classStartDateTime.isBefore(oneDaysFromNow))
            throw new LateCancellationException("A class can be removed at least 1 day before the class starts");

        this.dailyClassDAO.deleteDailyClass(dailyClassID);
    }

    public void cancelDailyClass(int dailyClassID) {
        this.dailyClassDAO.cancelDailyClass(dailyClassID);
    }

    public ArrayList<DailyClass> addWeeklyClassSchedule(DayOfWeek dayOfWeek, LocalDate startDate, LocalDate endDate, int courseID, LocalTime startTime, LocalTime endTime, int maxParticipants) {
        ArrayList<DailyClass> classesCreated = new ArrayList<>();
        Course course = this.courseDAO.getCourseByID(courseID);

        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            if (currentDate.getDayOfWeek() == dayOfWeek) {
                DailyClass dailyClass = new DailyClass.Builder()
                        .day(currentDate)
                        .startTime(startTime)
                        .endTime(endTime)
                        .maxParticipants(maxParticipants)
                        .course(course)
                        .build();
                this.dailyClassDAO.createDailyClass(dailyClass);
                classesCreated.add(dailyClass);
            }
            currentDate = currentDate.plusDays(1);
        }
        return classesCreated;
    }

    public ArrayList<DailyClass> getAllDailyClass() {
        return this.dailyClassDAO.getAllDailyClasses();
    }

    public ArrayList<DailyClass> getAllDailyClassOfCourse(int courseID) {
        return this.dailyClassDAO.getAllDailyClassesByCourse(courseID);
    }
}
