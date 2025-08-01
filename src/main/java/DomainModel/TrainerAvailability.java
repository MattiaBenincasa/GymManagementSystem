package DomainModel;

import DomainModel.Users.Trainer;

import java.time.LocalDate;
import java.time.LocalTime;

public class TrainerAvailability {
    private LocalDate day;
    private LocalTime startTime;
    private LocalTime endTime;
    private final Trainer trainer;

    public TrainerAvailability(Trainer trainer) {
        this.trainer = new Trainer(trainer);
    }

    public TrainerAvailability(TrainerAvailability trainerAvailability) {
        this.day = trainerAvailability.day;
        this.startTime = trainerAvailability.startTime;
        this.endTime = trainerAvailability.endTime;
        this.trainer = new Trainer(trainerAvailability.trainer);
    }

    public Trainer getTrainer() {
        return new Trainer(trainer);
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
}
