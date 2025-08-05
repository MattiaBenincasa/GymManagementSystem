package ORMTest.Users;

import BusinessLogic.Exceptions.DAOException;
import DomainModel.Users.Trainer;
import ORM.Users.TrainerDAO;
import ORM.Users.UserDAO;
import TestUtils.DAOTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class TrainerDAOTest {

    @BeforeEach
    void setUp() {
        DAOTestUtils.resetDatabase();
    }

    @Test
    void aTrainerShouldBeCreated() {
        UserDAO userDAO = new UserDAO();
        TrainerDAO trainerDAO = new TrainerDAO(userDAO);
        Trainer trainer = UserDAOTestUtils.createTrainer("trainer", "mailTrainer@mail.it");
        Assertions.assertDoesNotThrow(()->{
            int id = trainerDAO.createTrainer(trainer);
            trainerDAO.getTrainerByID(id);
        });
    }

    @Test
    void aTrainerShouldBeDeleted() {
        UserDAO userDAO = new UserDAO();
        TrainerDAO trainerDAO = new TrainerDAO(userDAO);
        Trainer trainer = UserDAOTestUtils.createTrainer("trainer", "mailTrainer@mail.it");
        int id = trainerDAO.createTrainer(trainer);
        Assertions.assertDoesNotThrow(()->{
            userDAO.deleteUser(id);
        });
        Assertions.assertThrows(DAOException.class, ()->{
            trainerDAO.getTrainerByID(id);
        });
    }

    @Test
    void aTrainerShouldBeUpdated() {
        UserDAO userDAO = new UserDAO();
        TrainerDAO trainerDAO = new TrainerDAO(userDAO);
        Trainer trainer = UserDAOTestUtils.createTrainer("trainer", "mailTrainer@mail.it");
        int id = trainerDAO.createTrainer(trainer);

        trainer = trainerDAO.getTrainerByID(id);
        trainer.setUsername("new_username");
        trainer.setPassword("new_plain_password");
        trainer.setName("new_name");
        trainer.setSurname("new_surname");
        trainer.setMail("new_mail@new_mail.com");
        trainer.setPhoneNumber("11111111111");
        trainer.setBirthDate(LocalDate.of(1990, 1, 1));
        trainer.setIsPersonalTrainer(false);
        trainer.setIsCourseCoach(false);
        trainerDAO.updateTrainer(trainer);

        Trainer updatedTrainer = trainerDAO.getTrainerByID(id);

        Assertions.assertEquals(trainer, updatedTrainer);
    }

}
