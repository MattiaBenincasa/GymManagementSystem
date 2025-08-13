package Controllers.Admin;

import BusinessLogic.AuthService.Session;
import BusinessLogic.DiscountService.DiscountService;
import BusinessLogic.Memberships.BundleService;
import BusinessLogic.Memberships.CourseMembershipService;
import BusinessLogic.Memberships.RegistrationFeeService;
import BusinessLogic.Memberships.WRMembershipService;
import BusinessLogic.Users.StaffService;
import BusinessLogic.Users.TrainerService;
import DomainModel.DiscountStrategy.*;
import DomainModel.Membership.*;
import DomainModel.Users.CustomerCategory;

import java.math.BigDecimal;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class AdminMembershipController {
    private final CourseMembershipService courseMembershipService;
    private final WRMembershipService wrMembershipService;
    private final BundleService bundleService;
    private final DiscountService discountService;
    private final RegistrationFeeService registrationFeeService;
    private Session session;

    public AdminMembershipController(CourseMembershipService courseMembershipService,
                                     WRMembershipService wrMembershipService,
                                     DiscountService discountService,
                                     StaffService staffService,
                                     TrainerService trainerService,
                                     BundleService bundleService,
                                     RegistrationFeeService registrationFeeService) {

        this.courseMembershipService = courseMembershipService;
        this.wrMembershipService = wrMembershipService;
        this.discountService = discountService;
        this.bundleService = bundleService;
        this.registrationFeeService = registrationFeeService;
    }

    public CourseMembership createCourseMembership(int courseID, String name, String description, BigDecimal price, int durationInDays, int weeklyAccess) {
        Session.validateSession(session);
        return courseMembershipService.createCourseMembership(courseID, name, description, price, durationInDays, weeklyAccess);
    }

    public void deleteCourseMembership(int courseMembershipID) {
        Session.validateSession(session);
        courseMembershipService.deleteCourseMembership(courseMembershipID);
    }

    public ArrayList<CourseMembership> getAllCourseMembership() {
        Session.validateSession(session);
        return courseMembershipService.getAllCourseMembership();
    }

    public void updateCourseMembership(CourseMembership courseMembershipUpdated) {
        Session.validateSession(session);
        this.courseMembershipService.updateCourseMembership(courseMembershipUpdated);
    }

    public void addDiscountToCourseMembership(int discountID, int courseMembershipID) {
        Session.validateSession(session);
        courseMembershipService.addDiscountToCourseMembership(discountID, courseMembershipID);
    }

    public void removeDiscountFromCourseMembership(int discountID, int courseMembershipID) {
        Session.validateSession(session);
        courseMembershipService.removeDiscountFromCourseMembership(discountID, courseMembershipID);
    }

    public WeightRoomMembership createWRMembership(String name, String description, BigDecimal price, int durationInDays, WRMembershipType type) {
        Session.validateSession(session);
        return wrMembershipService.createWeightRoomMembership(name, description, price, durationInDays, type);
    }

    public void deleteWRMembership(int wrMembershipID) {
        Session.validateSession(session);
        wrMembershipService.deleteWeightRoomMembership(wrMembershipID);
    }

    public void updateWeightRoomMembership(WeightRoomMembership updatedWeightRoomMembership) {
        Session.validateSession(session);
        wrMembershipService.updateWeightRoomMembership(updatedWeightRoomMembership);
    }

    public ArrayList<WeightRoomMembership> getAllWeightRoomMembership() {
        return wrMembershipService.getAllWeightRoomMembership();
    }

    public void addDiscountToWRMembership(int discountID, int wrMembershipID) {
        Session.validateSession(session);
        wrMembershipService.addDiscountToWRMembership(discountID, wrMembershipID);
    }

    public void removeDiscountFromWRMembership(int discountID, int courseMembershipID) {
        wrMembershipService.removeDiscountFromWRMembership(discountID, courseMembershipID);
    }

    public Bundle createBundle(String name, String description, List<Integer> membershipsID, List<Integer> discountsID) {
        Session.validateSession(session);
        return bundleService.createBundle(name, description, membershipsID, discountsID);
    }

    public void updateBundle(Bundle updatedBundle) {
        Session.validateSession(session);
        bundleService.updateBundle(updatedBundle);
    }

    public void deleteBundle(int bundleID) {
        Session.validateSession(session);
        bundleService.deleteBundle(bundleID);
    }

    public int createPercentageDiscount(String description, boolean isSpecialOffer, int percentage) {
        Session.validateSession(session);
        return discountService.createPercentageDiscount(description, isSpecialOffer, percentage);
    }

    public int createFixedDiscount(String description, boolean isSpecialOffer, int discountInEuro) {
        Session.validateSession(session);
        return discountService.createFixedDiscount(description, isSpecialOffer, discountInEuro);
    }

    public int createCustomerBasedDiscount(CustomerCategory category, int percentage, String description) {
        Session.validateSession(session);
        return discountService.createCustomerBasedDiscount(category, percentage, description);
    }

    public int createSeasonDiscount(HashSet<Month> months, int percentage, String description) {
        Session.validateSession(session);
        return discountService.createSeasonDiscount(months, percentage, description);
    }

    public void updateDiscount(DiscountStrategy discount) {
        Session.validateSession(session);
        discountService.updateDiscount(discount);
    }

    public void deleteDiscount(int discountID) {
        Session.validateSession(session);
        discountService.deleteDiscount(discountID);
    }

    public PercentageDiscount getPercentageDiscount(int discountID) {
        Session.validateSession(session);
        return discountService.getPercentageDiscount(discountID);
    }

    public FixedDiscount getFixedDiscount(int discountID) {
        Session.validateSession(session);
        return discountService.getFixedDiscount(discountID);
    }

    public CustomerBasedDiscount getCustomerBasedDiscount(int discountID) {
        Session.validateSession(session);
        return discountService.getCustomerBasedDiscount(discountID);
    }

    public SeasonDiscount getSeasonDiscount(int discountID) {
        Session.validateSession(session);
        return discountService.seasonDiscount(discountID);
    }

    public void updateRegistrationFee(String description, BigDecimal price) {
        Session.validateSession(session);
        this.registrationFeeService.updateRegistrationFee(price, description);
    }

    public void setSession(Session session) {
        Session.validateSession(session);
        this.session = session;
    }
}
