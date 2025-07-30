package DomainModel;

import java.time.LocalDate;
import java.time.LocalTime;

public class DailyClass {
    private LocalDate day;
    private LocalTime time;
    private int maxParticipants;
    private final Course course;
    private boolean isActive;

    public DailyClass(Course course) {
        this.course = new Course(course);
        isActive = true;
    }

    public DailyClass(DailyClass dailyClass) {
        this.day = dailyClass.day;
        this.time = dailyClass.time;
        this.maxParticipants = dailyClass.maxParticipants;
        this.course = new Course(dailyClass.course);
        this.isActive = dailyClass.isActive;
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

    public boolean isActive() {
        return this.isActive;
    }

    // once set isActive=false, you cannot change it anymore
    public void deleteDailyClass() {
        this.isActive = false;
    }

}
