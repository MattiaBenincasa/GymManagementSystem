package DomainModel;

import java.time.LocalDate;
import java.time.LocalTime;

public class DailyClass {
    private int id;
    private LocalDate day;
    private LocalTime time;
    private int maxParticipants;
    private Course course;
    private boolean isActive;

    public DailyClass(int id) {
        this.id = id;
    }

    public DailyClass(Course course) {
        this.course = new Course(course);
        isActive = true;
    }

    public DailyClass(DailyClass dailyClass) {
        this.id = dailyClass.id;
        this.day = dailyClass.day;
        this.time = dailyClass.time;
        this.maxParticipants = dailyClass.maxParticipants;
        this.course = new Course(dailyClass.course);
        this.isActive = dailyClass.isActive;
    }

    public int getId() {
        return id;
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
