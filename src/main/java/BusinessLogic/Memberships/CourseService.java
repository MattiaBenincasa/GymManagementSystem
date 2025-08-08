package BusinessLogic.Memberships;

import DomainModel.Membership.Course;
import DomainModel.Users.Trainer;
import ORM.Membership.CourseDAO;
import ORM.Users.TrainerDAO;

import java.util.ArrayList;

public class CourseService {
    private final CourseDAO courseDAO;
    private final TrainerDAO trainerDAO;

    public CourseService(CourseDAO courseDAO, TrainerDAO trainerDAO) {
        this.courseDAO = courseDAO;
        this.trainerDAO = trainerDAO;
    }

    public Course createCourse(String name, String description) {
        Course course = new Course(name, description);
        return this.courseDAO.createCourse(course);
    }

    public void addTrainersToCourse(int courseID, ArrayList<Integer> trainersIDs) {
        ArrayList<Trainer> trainers = new ArrayList<>();
        for (Integer id : trainersIDs)
            trainers.add(this.trainerDAO.getTrainerByID(id));

        Course course = this.courseDAO.getCourseByID(courseID);
        course.setTrainers(trainers);
        this.courseDAO.updateCourse(course);
    }

    public void addNewTrainerToCourse(int courseID, int trainerID) {
        Course course = this.courseDAO.getCourseByID(courseID);
        Trainer trainer = this.trainerDAO.getTrainerByID(trainerID);

        course.addTrainer(trainer);
        this.courseDAO.updateCourse(course);
    }

    public void deleteTrainerFromCourse(int courseID, int trainerID) {
        Course course = this.courseDAO.getCourseByID(courseID);
        Trainer trainer = this.trainerDAO.getTrainerByID(trainerID);
        course.removeTrainer(trainer);
        this.courseDAO.updateCourse(course);
    }

    public Course getCourseById(int id) {
        return this.courseDAO.getCourseByID(id);
    }

    public void updateCourse(Course updatedCourse) {
        this.courseDAO.updateCourse(updatedCourse);
    }

    public void deleteCourse(int id) {
        this.courseDAO.deleteCourse(id);
    }

    public ArrayList<Course> getAllCourse() {
        return this.courseDAO.getAllCourse();
    }

    public ArrayList<Course> getAllTrainerCourse(int trainerID) {
        return this.courseDAO.getCoursesByTrainerID(trainerID);
    }
}
