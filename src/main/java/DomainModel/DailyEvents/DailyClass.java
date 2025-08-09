package DomainModel.DailyEvents;

import BusinessLogic.Exceptions.LateCancellationException;
import DomainModel.Membership.Course;
import DomainModel.Users.Trainer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public final class DailyClass {
    private final int id;
    private final LocalDate day;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final int maxParticipants;
    private final Course course;
    private boolean isActive;
    private Trainer coach;

    public DailyClass(DailyClass dailyClass) {
        this.id = dailyClass.id;
        this.day = dailyClass.day;
        this.startTime = dailyClass.startTime;
        this.endTime = dailyClass.endTime;
        this.maxParticipants = dailyClass.maxParticipants;
        this.course = dailyClass.course;
        this.isActive = dailyClass.isActive;
        this.coach = new Trainer(dailyClass.coach);
    }

    private DailyClass(Builder builder) {
        this.id = builder.id;
        this.day = builder.day;
        this.startTime = builder.startTime;
        this.endTime = builder.endTime;
        this.maxParticipants = builder.maxParticipants;
        this.course = builder.course;
        this.isActive = builder.isActive;
        this.coach = builder.coach;
    }

    public int getId() {
        return id;
    }

    public LocalDate getDay() {
        return day;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public Course getCourse() {
        return course;
    }

    public boolean isActive() {
        return isActive;
    }

    public Trainer getCoach() {
        return this.coach;
    }

    public void changeCoach(Trainer trainer) {
        if (!trainer.isCourseCoach())
            throw new IllegalStateException("Only course coach can be set as coach");
        if(!course.getTrainers().contains(trainer))
            throw new IllegalStateException("Only trainers of " + this.course.getName() + " can be selected as trainers of this class");
        this.coach = trainer;
    }

    public void cancel() {
        LocalDateTime classStartDateTime = LocalDateTime.of(this.day, this.startTime);
        LocalDateTime twoHoursFromNow = classStartDateTime.plusHours(2);

        if (classStartDateTime.isBefore(twoHoursFromNow))
            throw new LateCancellationException("A class can be cancelled up to two hours before the start time.");
        this.isActive = false;
    }

    public static class Builder {
        private int id;
        private LocalDate day;
        private LocalTime startTime;
        private LocalTime endTime;
        private int maxParticipants;
        private Course course;
        private boolean isActive = true;
        private Trainer coach;

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder day(LocalDate day) {
            this.day = day;
            return this;
        }

        public Builder startTime(LocalTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public Builder endTime(LocalTime endTime) {
            this.endTime = endTime;
            return this;
        }

        public Builder maxParticipants(int maxParticipants) {
            this.maxParticipants = maxParticipants;
            return this;
        }

        public Builder course(Course course) {
            this.course = course;
            return this;
        }

        public Builder isActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public Builder coach(Trainer trainer) {
            this.coach = trainer;
            return this;
        }

        public DailyClass build() {
            if (day == null || startTime == null || endTime == null || course == null) {
                throw new IllegalStateException("Required fields (day, startTime, endTime, course) not set.");
            }
            if (startTime.isAfter(endTime)) {
                throw new IllegalStateException("startTime must be before endTime.");
            }
            if (maxParticipants <= 0) {
                throw new IllegalStateException("maxParticipants must be greater than 0.");
            }
            if (!coach.isCourseCoach())
                throw new IllegalStateException("Only course coach can be set as coach");
            if (!course.getTrainers().contains(coach))
                throw new IllegalStateException("Only trainers of " + this.course.getName() + " can be selected as trainers of this class");
            return new DailyClass(this);
        }
    }
}
