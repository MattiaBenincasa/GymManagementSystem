package DomainModel.Users;

public class Trainer extends User {
    boolean isPersonalTrainer;
    boolean isCourseCoach;

    public Trainer() {}

    public Trainer(int id) {
        super(id);
    }

    public Trainer(Trainer trainer) {
        super(trainer);
        this.isPersonalTrainer = trainer.isPersonalTrainer;
        this.isCourseCoach = trainer.isCourseCoach;
    }

    public void setIsPersonalTrainer(boolean isPersonalTrainer){
        this.isPersonalTrainer = isPersonalTrainer;
    }

    public void setIsCourseCoach(boolean isCourseCoach){
        this.isCourseCoach = isCourseCoach;
    }

    public boolean isPersonalTrainer() {
        return this.isPersonalTrainer;
    }

    public boolean isCourseCoach() {
        return this.isCourseCoach;
    }

}
