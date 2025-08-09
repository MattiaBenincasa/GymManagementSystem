package BusinessLogic.Memberships;

import DomainModel.DiscountStrategy.DiscountStrategy;
import DomainModel.Membership.Course;
import DomainModel.Membership.CourseMembership;
import ORM.DiscountStrategy.DiscountsDAO;
import ORM.Membership.CourseDAO;
import ORM.Membership.CourseMembershipDAO;

import java.math.BigDecimal;
import java.util.ArrayList;

public class CourseMembershipService {
    private final CourseMembershipDAO courseMembershipDAO;
    private final CourseDAO courseDAO;
    private final DiscountsDAO discountsDAO;

    public CourseMembershipService(CourseDAO courseDAO, CourseMembershipDAO courseMembershipDAO, DiscountsDAO discountsDAO) {
        this.courseMembershipDAO = courseMembershipDAO;
        this.courseDAO = courseDAO;
        this.discountsDAO = discountsDAO;
    }

    public CourseMembership createCourseMembership(int courseID, String name, String description, BigDecimal price, int durationInDays, int weeklyAccess) {
        Course course = this.courseDAO.getCourseByID(courseID);
        CourseMembership courseMembership = new CourseMembership();
        courseMembership.setName(name);
        courseMembership.setDescription(description);
        courseMembership.setPrice(price);
        courseMembership.setDurationInDays(durationInDays);
        courseMembership.setWeeklyAccess(weeklyAccess);
        courseMembership.setCourse(course);
        return this.courseMembershipDAO.createCourseMembership(courseMembership);
    }

    public CourseMembership getCourseMembershipByID(int id) {
        return this.courseMembershipDAO.getCourseMembershipByID(id);
    }

    public void updateCourseMembership(CourseMembership courseMembershipUpdated) {
        this.courseMembershipDAO.updateCourseMembership(courseMembershipUpdated);
    }

    public void deleteCourseMembership(int id) {
        this.courseMembershipDAO.deleteCourseMembership(id);
    }

    public ArrayList<CourseMembership> getAllCourseMembership() {
        return this.courseMembershipDAO.getAllCourseMembership();
    }

    public void addDiscountToCourseMembership(int discountID, int courseMembershipID) {
        CourseMembership courseMembership = this.getCourseMembershipByID(courseMembershipID);
        DiscountStrategy discountStrategy = this.discountsDAO.getDiscountByID(discountID);
        courseMembership.addDiscount(discountStrategy);
        this.courseMembershipDAO.updateCourseMembership(courseMembership);
    }

    public void removeDiscountFromCourseMembership(int discountID, int courseMembershipID) {
        CourseMembership courseMembership = this.getCourseMembershipByID(courseMembershipID);
        DiscountStrategy discountStrategy = this.discountsDAO.getDiscountByID(discountID);
        courseMembership.removeDiscount(discountStrategy);
        this.courseMembershipDAO.updateCourseMembership(courseMembership);
    }

}
