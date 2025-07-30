package DomainModel;

import java.time.LocalDate;
import java.time.LocalTime;

public class DailyClass {
    private LocalDate day;
    private LocalTime time;
    private int maxParticipants;
    private final Course course;

    public DailyClass(Course course) {
        this.course = new Course(course);
    }

    public DailyClass(DailyClass dailyClass) {
        this.day = dailyClass.day;
        this.time = dailyClass.time;
        this.maxParticipants = dailyClass.maxParticipants;
        this.course = new Course(dailyClass.course);
    }

    public Course getCourse() {
        return new Course(course);
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }
}
