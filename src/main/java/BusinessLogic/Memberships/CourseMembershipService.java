package BusinessLogic.Memberships;

import DomainModel.Membership.CourseMembership;
import ORM.Membership.CourseMembershipDAO;

import java.math.BigDecimal;
import java.util.ArrayList;

public class CourseMembershipService {
    private final CourseMembershipDAO courseMembershipDAO;

    public CourseMembershipService(CourseMembershipDAO courseMembershipDAO) {
        this.courseMembershipDAO = courseMembershipDAO;
    }

    public CourseMembership createCourseMembership(String name, String description, BigDecimal price, int durationInDays, int weeklyAccess) {
        CourseMembership courseMembership = new CourseMembership();
        courseMembership.setName(name);
        courseMembership.setDescription(description);
        courseMembership.setPrice(price);
        courseMembership.setDurationInDays(durationInDays);
        courseMembership.setWeeklyAccess(weeklyAccess);
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

}
