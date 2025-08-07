package BusinessLogic.Users;

import BusinessLogic.AuthService.PasswordUtils;
import DomainModel.Users.Trainer;
import ORM.Users.TrainerDAO;
import ORM.Users.UserDAO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class TrainerService {
    private final TrainerDAO trainerDAO;
    private final UserDAO userDAO;

    public TrainerService(TrainerDAO trainerDAO, UserDAO userDAO) {
        this.trainerDAO = trainerDAO;
        this.userDAO = userDAO;
    }

    // initialize trainer profile with basic info
    public Trainer createTrainer(String username, String password, String mail, String name, String surname, String phoneNumber, LocalDate birthDate) {
        Trainer trainer = new Trainer();
        trainer.setUsername(username);
        trainer.setPassword(password);
        trainer.setMail(mail);
        trainer.setName(name);
        trainer.setSurname(surname);
        trainer.setPhoneNumber(phoneNumber);
        trainer.setBirthDate(birthDate);

        return this.trainerDAO.createTrainer(trainer);
    }

    // complete customer profile with all trainer info
    // this method can be used also to update trainer info
    public Trainer setUpTrainerInfo(int trainerID, boolean isPersonalTrainer, boolean isCourseCoach) {
        Trainer trainer = this.trainerDAO.getTrainerByID(trainerID);
        trainer.setIsPersonalTrainer(isPersonalTrainer);
        trainer.setIsCourseCoach(isCourseCoach);
        this.trainerDAO.updateTrainer(trainer);
        return trainer;
    }

    public void changeTrainerInfo(int trainerID, String username, String name, String surname, String mail, String phoneNumber) {
        UserService.updateUserInfo(this.userDAO,
                this.trainerDAO.getTrainerByID(trainerID),
                username,
                name,
                surname,
                mail,
                phoneNumber);
    }

    public void changePassword(int trainerID, String oldPassword, String newPassword) {
        Trainer trainer = this.trainerDAO.getTrainerByID(trainerID);
        PasswordUtils.checkPassword(oldPassword, trainer.getHashPassword());
        UserService.changePassword(this.userDAO, trainer, newPassword);
    }

    public ArrayList<Trainer> getAllTrainers() {
        return this.trainerDAO.getAllTrainers();
    }

    public ArrayList<Trainer> getAllPersonalTrainers() {
        ArrayList<Trainer> allTrainers = this.trainerDAO.getAllTrainers();

        return allTrainers.stream()
                .filter(Trainer::isPersonalTrainer)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Trainer> getAllCourseCoach() {
        ArrayList<Trainer> allTrainers = this.trainerDAO.getAllTrainers();

        return allTrainers.stream()
                .filter(Trainer::isCourseCoach)
                .collect(Collectors.toCollection(ArrayList::new));
    }

}
