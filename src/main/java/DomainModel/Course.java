package DomainModel;

import DomainModel.Users.Trainer;

import java.util.ArrayList;

public class Course {
    private int id;
    private String name;
    private String description;
    private ArrayList<Trainer> trainers;

    public Course(Course course) {
        this.id = course.id;
        this.name = course.name;
        this.description = course.description;
        this.trainers = new ArrayList<>(course.trainers);
    }

    public Course(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.trainers = new ArrayList<>();
    }

    public Course(String name, String description) {
        this.name = name;
        this.description = description;
        this.trainers = new ArrayList<>();
    }

    public Course(int id, Course course) {
        this.id = id;
        this.name = course.name;
        this.description = course.description;
        this.trainers = new ArrayList<>(course.getTrainers());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void addTrainer(Trainer trainer) {
        if (!trainer.isCourseCoach())
            throw new IllegalArgumentException("Trainer " + trainer.toString() + " is not a Course Coach");
        this.trainers.add(trainer);
    }

    public void removeTrainer(Trainer trainer) {
        this.trainers.remove(trainer);
    }

    public ArrayList<Trainer> getTrainers() {
        return new ArrayList<>(this.trainers);
    }

    public void setTrainers(ArrayList<Trainer> trainers) {
        this.trainers = trainers;
    }
}
