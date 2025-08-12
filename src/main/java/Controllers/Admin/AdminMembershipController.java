package Controllers.Admin;

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
import java.util.HashSet;
import java.util.List;


public class AdminMembershipController {
    private final CourseMembershipService courseMembershipService;
    private final WRMembershipService wrMembershipService;
    private final BundleService bundleService;
    private final DiscountService discountService;
    private final RegistrationFeeService registrationFeeService;

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
        return courseMembershipService.createCourseMembership(courseID, name, description, price, durationInDays, weeklyAccess);
    }

    public void deleteCourseMembership(int courseMembershipID) {
        courseMembershipService.deleteCourseMembership(courseMembershipID);
    }

    public void addDiscountToCourseMembership(int discountID, int courseMembershipID) {
        courseMembershipService.addDiscountToCourseMembership(discountID, courseMembershipID);
    }

    public WeightRoomMembership createWRMembership(String name, String description, BigDecimal price, int durationInDays, WRMembershipType type) {
        return wrMembershipService.createWeightRoomMembership(name, description, price, durationInDays, type);
    }

    public void deleteWRMembership(int wrMembershipID) {
        wrMembershipService.deleteWeightRoomMembership(wrMembershipID);
    }

    public void addDiscountToWRMembership(int discountID, int wrMembershipID) {
        wrMembershipService.addDiscountToWRMembership(discountID, wrMembershipID);
    }

    public Bundle createBundle(String name, String description, List<Integer> membershipsID, List<Integer> discountsID) {
        return bundleService.createBundle(name, description, membershipsID, discountsID);
    }

    public void updateBundle(Bundle updatedBundle) {
        bundleService.updateBundle(updatedBundle);
    }

    public void deleteBundle(int bundleID) {
        bundleService.deleteBundle(bundleID);
    }

    public int createPercentageDiscount(String description, boolean isSpecialOffer, int percentage) {
        return discountService.createPercentageDiscount(description, isSpecialOffer, percentage);
    }

    public int createFixedDiscount(String description, boolean isSpecialOffer, int discountInEuro) {
        return discountService.createFixedDiscount(description, isSpecialOffer, discountInEuro);
    }

    public int createCustomerBasedDiscount(CustomerCategory category, int percentage, String description) {
        return discountService.createCustomerBasedDiscount(category, percentage, description);
    }

    public int createSeasonDiscount(HashSet<Month> months, int percentage, String description) {
        return discountService.createSeasonDiscount(months, percentage, description);
    }

    public void updateDiscount(DiscountStrategy discount) {
        discountService.updateDiscount(discount);
    }

    public void deleteDiscount(int discountID) {
        discountService.deleteDiscount(discountID);
    }

    public PercentageDiscount getPercentageDiscount(int discountID) {
        return discountService.getPercentageDiscount(discountID);
    }

    public FixedDiscount getFixedDiscount(int discountID) {
        return discountService.getFixedDiscount(discountID);
    }

    public CustomerBasedDiscount getCustomerBasedDiscount(int discountID) {
        return discountService.getCustomerBasedDiscount(discountID);
    }

    public SeasonDiscount getSeasonDiscount(int discountID) {
        return discountService.seasonDiscount(discountID);
    }

    public void updateRegistrationFee(String description, BigDecimal price) {
        this.registrationFeeService.updateRegistrationFee(price, description);
    }

}
