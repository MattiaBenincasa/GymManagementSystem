package ORMTest.Users;

import Exceptions.DAOException;
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
            Trainer trainerWithId = trainerDAO.createTrainer(trainer);
            trainerDAO.getTrainerByID(trainerWithId.getId());
        });
    }

    @Test
    void aTrainerShouldBeDeleted() {
        UserDAO userDAO = new UserDAO();
        TrainerDAO trainerDAO = new TrainerDAO(userDAO);
        Trainer trainer = trainerDAO.createTrainer(UserDAOTestUtils.createTrainer("trainer", "mailTrainer@mail.it"));

        Assertions.assertDoesNotThrow(()->{
            userDAO.deleteUser(trainer.getId());
        });
        Assertions.assertThrows(DAOException.class, ()->{
            trainerDAO.getTrainerByID(trainer.getId());
        });
    }

    @Test
    void aTrainerShouldBeUpdated() {
        UserDAO userDAO = new UserDAO();
        TrainerDAO trainerDAO = new TrainerDAO(userDAO);
        Trainer trainer = trainerDAO.createTrainer(UserDAOTestUtils.createTrainer("trainer", "mailTrainer@mail.it"));

        trainer = trainerDAO.getTrainerByID(trainer.getId());
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

        Trainer updatedTrainer = trainerDAO.getTrainerByID(trainer.getId());

        Assertions.assertEquals(trainer, updatedTrainer);
    }

}
