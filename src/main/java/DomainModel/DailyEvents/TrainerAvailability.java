package DomainModel.DailyEvents;

import DomainModel.Users.Trainer;

import java.time.LocalDate;
import java.time.LocalTime;

public class TrainerAvailability {
    private int id;
    private LocalDate day;
    private LocalTime startTime;
    private LocalTime endTime;
    private final Trainer trainer;

    public TrainerAvailability(int id, Trainer trainer) {
        if (!trainer.isPersonalTrainer())
            throw new IllegalStateException("Trainer must be a personal trainer");
        this.id = id;
        this.trainer = trainer;
    }

    public TrainerAvailability(int id, TrainerAvailability trainerAvailability) {
        this.id = id;
        this.day = trainerAvailability.day;
        this.startTime = trainerAvailability.startTime;
        this.endTime = trainerAvailability.endTime;
        this.trainer = trainerAvailability.trainer;
    }

    public TrainerAvailability(Trainer trainer) {
        if (!trainer.isPersonalTrainer())
            throw new IllegalStateException("Trainer must be a personal trainer");
        this.trainer = new Trainer(trainer);
    }

    public TrainerAvailability(TrainerAvailability trainerAvailability) {
        this.id = trainerAvailability.id;
        this.day = trainerAvailability.day;
        this.startTime = trainerAvailability.startTime;
        this.endTime = trainerAvailability.endTime;
        this.trainer = new Trainer(trainerAvailability.trainer);
    }

    public int getId() {
        return this.id;
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
