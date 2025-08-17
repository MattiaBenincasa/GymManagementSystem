package BusinessLogic.Memberships;

import DomainModel.DiscountStrategy.DiscountStrategy;
import DomainModel.Membership.WRMembershipType;
import DomainModel.Membership.WeightRoomMembership;
import ORM.DiscountsDAO;
import ORM.Membership.WeightRoomMembershipDAO;

import java.math.BigDecimal;
import java.util.ArrayList;

public class WRMembershipService {
    private final WeightRoomMembershipDAO weightRoomMembershipDAO;
    private final DiscountsDAO discountsDAO;

    public WRMembershipService(WeightRoomMembershipDAO weightRoomMembershipDAO, DiscountsDAO discountsDAO) {
        this.weightRoomMembershipDAO = weightRoomMembershipDAO;
        this.discountsDAO = discountsDAO;
    }

    public WeightRoomMembership createWeightRoomMembership(String name, String description, BigDecimal price, int durationInDays, WRMembershipType type) {
        WeightRoomMembership weightRoomMembership = new WeightRoomMembership();
        weightRoomMembership.setName(name);
        weightRoomMembership.setDescription(description);
        weightRoomMembership.setPrice(price);
        weightRoomMembership.setDurationInDays(durationInDays);
        weightRoomMembership.setType(type);
        return this.weightRoomMembershipDAO.createWeightRoomMembership(weightRoomMembership);
    }

    public WeightRoomMembership getWeightRoomMembershipByID(int id) {
        return this.weightRoomMembershipDAO.getWeightRoomMembershipByID(id);
    }

    public void updateWeightRoomMembership(WeightRoomMembership updatedWeightRoomMembership) {
        this.weightRoomMembershipDAO.updateWeightRoomMembership(updatedWeightRoomMembership);
    }

    public void deleteWeightRoomMembership(int id) {
        this.weightRoomMembershipDAO.deleteWRMembership(id);
    }

    public ArrayList<WeightRoomMembership> getAllWeightRoomMembership() {
        return this.weightRoomMembershipDAO.getAllWRMembership();
    }

    public void addDiscountToWRMembership(int discountID, int courseMembershipID) {
        WeightRoomMembership weightRoomMembership = this.getWeightRoomMembershipByID(courseMembershipID);
        DiscountStrategy discountStrategy = this.discountsDAO.getDiscountByID(discountID);
        weightRoomMembership.addDiscount(discountStrategy);
        this.weightRoomMembershipDAO.updateWeightRoomMembership(weightRoomMembership);
    }

    public void removeDiscountFromWRMembership(int discountID, int courseMembershipID) {
        WeightRoomMembership weightRoomMembership = this.getWeightRoomMembershipByID(courseMembershipID);
        DiscountStrategy discountStrategy = this.discountsDAO.getDiscountByID(discountID);
        weightRoomMembership.removeDiscount(discountStrategy);
        this.weightRoomMembershipDAO.updateWeightRoomMembership(weightRoomMembership);
    }

}
