package Controllers.Admin;

import BusinessLogic.AuthService.Session;
import BusinessLogic.DailyEvents.DailyClassService;
import BusinessLogic.Memberships.CourseService;
import DomainModel.DailyEvents.DailyClass;
import DomainModel.Membership.Course;
import DomainModel.Users.Trainer;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class AdminCourseController {
    private final CourseService courseService;
    private final DailyClassService dailyClassService;
    private Session session;

    public AdminCourseController(CourseService courseService, DailyClassService dailyClassService) {
        this.courseService = courseService;
        this.dailyClassService = dailyClassService;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Course createCourse(String name, String description) {
        Session.validateSession(this.session);
        return courseService.createCourse(name, description);
    }

    public void addTrainerToCourse(int courseID, int trainerID) {
        Session.validateSession(this.session);
        courseService.addNewTrainerToCourse(courseID, trainerID);
    }

    public void deleteCourse(int courseID) {
        Session.validateSession(this.session);
        courseService.deleteCourse(courseID);
    }

    public DailyClass addDailyClass(LocalDate day, LocalTime startTime, LocalTime endTime, int maxParticipants, int courseID, Trainer coach) {
        Session.validateSession(this.session);
        return dailyClassService.addDailyCLass(day, startTime, endTime, maxParticipants, courseID, coach);
    }

    public ArrayList<DailyClass> addWeeklyClassSchedule(DayOfWeek dayOfWeek, LocalDate startDate, LocalDate endDate, int courseID, LocalTime startTime, LocalTime endTime, int maxParticipants, Trainer coach) {
        Session.validateSession(this.session);
        return dailyClassService.addWeeklyClassSchedule(dayOfWeek, startDate, endDate, courseID, startTime, endTime, maxParticipants, coach);
    }

    public void deleteDailyClass(int dailyClassID) {
        Session.validateSession(this.session);
        dailyClassService.RemoveDailyClass(dailyClassID);
    }

    public void cancelDailyClass(int dailyClassID) {
        Session.validateSession(this.session);
        dailyClassService.cancelDailyClass(dailyClassID);
    }

    public ArrayList<DailyClass> getAllDailyClasses() {
        Session.validateSession(this.session);
        return dailyClassService.getAllDailyClass();
    }

    public ArrayList<DailyClass> getAllDailyClassesByCourseID(int courseID) {
        Session.validateSession(this.session);
        return dailyClassService.getAllDailyClassOfCourse(courseID);
    }
}
